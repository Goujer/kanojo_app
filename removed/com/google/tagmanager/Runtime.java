package com.google.tagmanager;

import android.content.Context;
import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.tagmanager.CacheFactory;
import com.google.tagmanager.CustomFunctionCall;
import com.google.tagmanager.ResourceUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Runtime {
    static final String DEFAULT_RULE_NAME = "Unknown";
    private static final ObjectAndStatic<TypeSystem.Value> DEFAULT_VALUE_AND_STATIC = new ObjectAndStatic<>(Types.getDefaultValue(), true);
    private static final int MAX_CACHE_SIZE = 1048576;
    private final EventInfoDistributor eventInfoDistributor;
    private volatile String mCurrentEventName;
    private final Cache<ResourceUtil.ExpandedFunctionCall, ObjectAndStatic<TypeSystem.Value>> mFunctionCallCache;
    private final Cache<String, ObjectAndStatic<TypeSystem.Value>> mMacroEvaluationCache;
    private final Map<String, MacroInfo> mMacroLookup;
    private final Map<String, FunctionCallImplementation> mMacroMap;
    private final Map<String, FunctionCallImplementation> mPredicateMap;
    private final ResourceUtil.ExpandedResource mResource;
    private final Set<ResourceUtil.ExpandedRule> mRules;
    private final Map<String, FunctionCallImplementation> mTrackingTagMap;

    interface AddRemoveSetPopulator {
        void rulePassed(ResourceUtil.ExpandedRule expandedRule, Set<ResourceUtil.ExpandedFunctionCall> set, Set<ResourceUtil.ExpandedFunctionCall> set2, ResolvedRuleBuilder resolvedRuleBuilder);
    }

    private static class MacroInfo {
        private final Map<ResourceUtil.ExpandedRule, List<String>> mAddMacroNames = new HashMap();
        private final Map<ResourceUtil.ExpandedRule, List<ResourceUtil.ExpandedFunctionCall>> mAddMacros = new HashMap();
        private ResourceUtil.ExpandedFunctionCall mDefault;
        private final Map<ResourceUtil.ExpandedRule, List<String>> mRemoveMacroNames = new HashMap();
        private final Map<ResourceUtil.ExpandedRule, List<ResourceUtil.ExpandedFunctionCall>> mRemoveMacros = new HashMap();
        private final Set<ResourceUtil.ExpandedRule> mRules = new HashSet();

        public Set<ResourceUtil.ExpandedRule> getRules() {
            return this.mRules;
        }

        public void addRule(ResourceUtil.ExpandedRule rule) {
            this.mRules.add(rule);
        }

        public Map<ResourceUtil.ExpandedRule, List<ResourceUtil.ExpandedFunctionCall>> getAddMacros() {
            return this.mAddMacros;
        }

        public Map<ResourceUtil.ExpandedRule, List<String>> getAddMacroRuleNames() {
            return this.mAddMacroNames;
        }

        public Map<ResourceUtil.ExpandedRule, List<String>> getRemoveMacroRuleNames() {
            return this.mRemoveMacroNames;
        }

        public void addAddMacroForRule(ResourceUtil.ExpandedRule rule, ResourceUtil.ExpandedFunctionCall function) {
            List<ResourceUtil.ExpandedFunctionCall> result = this.mAddMacros.get(rule);
            if (result == null) {
                result = new ArrayList<>();
                this.mAddMacros.put(rule, result);
            }
            result.add(function);
        }

        public void addAddMacroRuleNameForRule(ResourceUtil.ExpandedRule rule, String ruleName) {
            List<String> result = this.mAddMacroNames.get(rule);
            if (result == null) {
                result = new ArrayList<>();
                this.mAddMacroNames.put(rule, result);
            }
            result.add(ruleName);
        }

        public Map<ResourceUtil.ExpandedRule, List<ResourceUtil.ExpandedFunctionCall>> getRemoveMacros() {
            return this.mRemoveMacros;
        }

        public void addRemoveMacroForRule(ResourceUtil.ExpandedRule rule, ResourceUtil.ExpandedFunctionCall function) {
            List<ResourceUtil.ExpandedFunctionCall> result = this.mRemoveMacros.get(rule);
            if (result == null) {
                result = new ArrayList<>();
                this.mRemoveMacros.put(rule, result);
            }
            result.add(function);
        }

        public void addRemoveMacroRuleNameForRule(ResourceUtil.ExpandedRule rule, String ruleName) {
            List<String> result = this.mRemoveMacroNames.get(rule);
            if (result == null) {
                result = new ArrayList<>();
                this.mRemoveMacroNames.put(rule, result);
            }
            result.add(ruleName);
        }

        public ResourceUtil.ExpandedFunctionCall getDefault() {
            return this.mDefault;
        }

        public void setDefault(ResourceUtil.ExpandedFunctionCall def) {
            this.mDefault = def;
        }
    }

    public Runtime(Context context, ResourceUtil.ExpandedResource resource, DataLayer dataLayer, CustomFunctionCall.CustomEvaluator functionCallMacroEvaluator, CustomFunctionCall.CustomEvaluator functionCallTagEvaluator, EventInfoDistributor eventInfoDistributor2) {
        if (resource == null) {
            throw new NullPointerException("resource cannot be null");
        }
        this.mResource = resource;
        this.mRules = new HashSet(resource.getRules());
        this.eventInfoDistributor = eventInfoDistributor2;
        this.mFunctionCallCache = new CacheFactory().createCache(MAX_CACHE_SIZE, new CacheFactory.CacheSizeManager<ResourceUtil.ExpandedFunctionCall, ObjectAndStatic<TypeSystem.Value>>() {
            public int sizeOf(ResourceUtil.ExpandedFunctionCall key, ObjectAndStatic<TypeSystem.Value> value) {
                return value.getObject().toByteArray().length;
            }
        });
        this.mMacroEvaluationCache = new CacheFactory().createCache(MAX_CACHE_SIZE, new CacheFactory.CacheSizeManager<String, ObjectAndStatic<TypeSystem.Value>>() {
            public int sizeOf(String key, ObjectAndStatic<TypeSystem.Value> value) {
                return value.getObject().toByteArray().length + key.length();
            }
        });
        this.mTrackingTagMap = new HashMap();
        addTrackingTag(new ArbitraryPixelTag(context));
        addTrackingTag(new CustomFunctionCall(functionCallTagEvaluator));
        addTrackingTag(new UniversalAnalyticsTag(context, dataLayer));
        this.mPredicateMap = new HashMap();
        addPredicate(new ContainsPredicate());
        addPredicate(new EndsWithPredicate());
        addPredicate(new EqualsPredicate());
        addPredicate(new GreaterEqualsPredicate());
        addPredicate(new GreaterThanPredicate());
        addPredicate(new LessEqualsPredicate());
        addPredicate(new LessThanPredicate());
        addPredicate(new RegexPredicate());
        addPredicate(new StartsWithPredicate());
        this.mMacroMap = new HashMap();
        addMacro(new AdvertiserIdMacro(context));
        addMacro(new AdvertisingTrackingEnabledMacro());
        addMacro(new AdwordsClickReferrerMacro(context));
        addMacro(new AppIdMacro(context));
        addMacro(new AppNameMacro(context));
        addMacro(new AppVersionMacro(context));
        addMacro(new ConstantMacro());
        addMacro(new CustomFunctionCall(functionCallMacroEvaluator));
        addMacro(new DataLayerMacro(dataLayer));
        addMacro(new DeviceIdMacro(context));
        addMacro(new DeviceNameMacro());
        addMacro(new EncodeMacro());
        addMacro(new EventMacro(this));
        addMacro(new GtmVersionMacro());
        addMacro(new HashMacro());
        addMacro(new InstallReferrerMacro(context));
        addMacro(new JoinerMacro());
        addMacro(new LanguageMacro());
        addMacro(new MobileAdwordsUniqueIdMacro(context));
        addMacro(new OsVersionMacro());
        addMacro(new PlatformMacro());
        addMacro(new RandomMacro());
        addMacro(new RegexGroupMacro());
        addMacro(new ResolutionMacro(context));
        addMacro(new RuntimeVersionMacro());
        addMacro(new SdkVersionMacro());
        addMacro(new TimeMacro());
        this.mMacroLookup = new HashMap();
        for (ResourceUtil.ExpandedRule rule : this.mRules) {
            if (eventInfoDistributor2.debugMode()) {
                verifyFunctionAndNameListSizes(rule.getAddMacros(), rule.getAddMacroRuleNames(), "add macro");
                verifyFunctionAndNameListSizes(rule.getRemoveMacros(), rule.getRemoveMacroRuleNames(), "remove macro");
                verifyFunctionAndNameListSizes(rule.getAddTags(), rule.getAddTagRuleNames(), "add tag");
                verifyFunctionAndNameListSizes(rule.getRemoveTags(), rule.getRemoveTagRuleNames(), "remove tag");
            }
            for (int i = 0; i < rule.getAddMacros().size(); i++) {
                ResourceUtil.ExpandedFunctionCall function = rule.getAddMacros().get(i);
                String ruleName = DEFAULT_RULE_NAME;
                if (eventInfoDistributor2.debugMode() && i < rule.getAddMacroRuleNames().size()) {
                    ruleName = rule.getAddMacroRuleNames().get(i);
                }
                MacroInfo info = getOrAddMacroInfo(this.mMacroLookup, getFunctionName(function));
                info.addRule(rule);
                info.addAddMacroForRule(rule, function);
                info.addAddMacroRuleNameForRule(rule, ruleName);
            }
            for (int i2 = 0; i2 < rule.getRemoveMacros().size(); i2++) {
                ResourceUtil.ExpandedFunctionCall function2 = rule.getRemoveMacros().get(i2);
                String ruleName2 = DEFAULT_RULE_NAME;
                if (eventInfoDistributor2.debugMode() && i2 < rule.getRemoveMacroRuleNames().size()) {
                    ruleName2 = rule.getRemoveMacroRuleNames().get(i2);
                }
                MacroInfo info2 = getOrAddMacroInfo(this.mMacroLookup, getFunctionName(function2));
                info2.addRule(rule);
                info2.addRemoveMacroForRule(rule, function2);
                info2.addRemoveMacroRuleNameForRule(rule, ruleName2);
            }
        }
        for (Map.Entry<String, List<ResourceUtil.ExpandedFunctionCall>> ent : this.mResource.getAllMacros().entrySet()) {
            for (ResourceUtil.ExpandedFunctionCall function3 : ent.getValue()) {
                if (!Types.valueToBoolean(function3.getProperties().get(Key.NOT_DEFAULT_MACRO.toString())).booleanValue()) {
                    getOrAddMacroInfo(this.mMacroLookup, ent.getKey()).setDefault(function3);
                }
            }
        }
    }

    public Runtime(Context context, ResourceUtil.ExpandedResource resource, DataLayer dataLayer, CustomFunctionCall.CustomEvaluator functionCallMacroEvaluator, CustomFunctionCall.CustomEvaluator functionCallTagEvaluator) {
        this(context, resource, dataLayer, functionCallMacroEvaluator, functionCallTagEvaluator, new NoopEventInfoDistributor());
    }

    public ResourceUtil.ExpandedResource getResource() {
        return this.mResource;
    }

    public synchronized void evaluateTags(String currentEventName) {
        setCurrentEventName(currentEventName);
        EventInfoBuilder eventInfoBuilder = this.eventInfoDistributor.createDataLayerEventEvaluationEventInfo(currentEventName);
        DataLayerEventEvaluationInfoBuilder debugDataLayerBuilder = eventInfoBuilder.createDataLayerEventEvaluationInfoBuilder();
        for (ResourceUtil.ExpandedFunctionCall tag : calculateTagsToRun(this.mRules, debugDataLayerBuilder.createRulesEvaluation()).getObject()) {
            executeFunction(this.mTrackingTagMap, tag, new HashSet(), debugDataLayerBuilder.createAndAddResult());
        }
        eventInfoBuilder.processEventInfo();
        setCurrentEventName((String) null);
    }

    public ObjectAndStatic<TypeSystem.Value> evaluateMacroReference(String macroName) {
        EventInfoBuilder eventInfoBuilder = this.eventInfoDistributor.createMacroEvalutionEventInfo(macroName);
        ObjectAndStatic<TypeSystem.Value> result = evaluateMacroReferenceCycleDetection(macroName, new HashSet(), eventInfoBuilder.createMacroEvaluationInfoBuilder());
        eventInfoBuilder.processEventInfo();
        return result;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public synchronized void setCurrentEventName(String currentEventName) {
        this.mCurrentEventName = currentEventName;
    }

    /* access modifiers changed from: package-private */
    public synchronized String getCurrentEventName() {
        return this.mCurrentEventName;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public ObjectAndStatic<Set<ResourceUtil.ExpandedFunctionCall>> calculateMacrosToRun(String macroName, Set<ResourceUtil.ExpandedRule> rules, Map<ResourceUtil.ExpandedRule, List<ResourceUtil.ExpandedFunctionCall>> addMacros, Map<ResourceUtil.ExpandedRule, List<String>> addMacroRuleNames, Map<ResourceUtil.ExpandedRule, List<ResourceUtil.ExpandedFunctionCall>> removeMacros, Map<ResourceUtil.ExpandedRule, List<String>> removeMacroRuleNames, Set<String> pendingMacroExpansions, RuleEvaluationStepInfoBuilder debugRulesEvaluation) {
        final Map<ResourceUtil.ExpandedRule, List<ResourceUtil.ExpandedFunctionCall>> map = addMacros;
        final Map<ResourceUtil.ExpandedRule, List<String>> map2 = addMacroRuleNames;
        final Map<ResourceUtil.ExpandedRule, List<ResourceUtil.ExpandedFunctionCall>> map3 = removeMacros;
        final Map<ResourceUtil.ExpandedRule, List<String>> map4 = removeMacroRuleNames;
        return calculateGenericToRun(rules, pendingMacroExpansions, new AddRemoveSetPopulator() {
            public void rulePassed(ResourceUtil.ExpandedRule rule, Set<ResourceUtil.ExpandedFunctionCall> add, Set<ResourceUtil.ExpandedFunctionCall> remove, ResolvedRuleBuilder debugRuleBuilder) {
                List<ResourceUtil.ExpandedFunctionCall> thisAddMacro = (List) map.get(rule);
                List<String> thisMacroEnablingRuleNames = (List) map2.get(rule);
                if (thisAddMacro != null) {
                    add.addAll(thisAddMacro);
                    debugRuleBuilder.getAddedMacroFunctions().translateAndAddAll(thisAddMacro, thisMacroEnablingRuleNames);
                }
                List<ResourceUtil.ExpandedFunctionCall> thisRemoveMacro = (List) map3.get(rule);
                List<String> thisRemoveMacroRuleNames = (List) map4.get(rule);
                if (thisRemoveMacro != null) {
                    remove.addAll(thisRemoveMacro);
                    debugRuleBuilder.getRemovedMacroFunctions().translateAndAddAll(thisRemoveMacro, thisRemoveMacroRuleNames);
                }
            }
        }, debugRulesEvaluation);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public ObjectAndStatic<Set<ResourceUtil.ExpandedFunctionCall>> calculateTagsToRun(Set<ResourceUtil.ExpandedRule> rules, RuleEvaluationStepInfoBuilder debugRulesEvaluation) {
        return calculateGenericToRun(rules, new HashSet(), new AddRemoveSetPopulator() {
            public void rulePassed(ResourceUtil.ExpandedRule rule, Set<ResourceUtil.ExpandedFunctionCall> add, Set<ResourceUtil.ExpandedFunctionCall> remove, ResolvedRuleBuilder debugRuleBuilder) {
                add.addAll(rule.getAddTags());
                remove.addAll(rule.getRemoveTags());
                debugRuleBuilder.getAddedTagFunctions().translateAndAddAll(rule.getAddTags(), rule.getAddTagRuleNames());
                debugRuleBuilder.getRemovedTagFunctions().translateAndAddAll(rule.getRemoveTags(), rule.getRemoveTagRuleNames());
            }
        }, debugRulesEvaluation);
    }

    private static MacroInfo getOrAddMacroInfo(Map<String, MacroInfo> macroLookup, String key) {
        MacroInfo result = macroLookup.get(key);
        if (result != null) {
            return result;
        }
        MacroInfo result2 = new MacroInfo();
        macroLookup.put(key, result2);
        return result2;
    }

    private ObjectAndStatic<Set<ResourceUtil.ExpandedFunctionCall>> calculateGenericToRun(Set<ResourceUtil.ExpandedRule> rules, Set<String> pendingMacroExpansions, AddRemoveSetPopulator setPopulator, RuleEvaluationStepInfoBuilder debugRulesEvaluation) {
        Set<ResourceUtil.ExpandedFunctionCall> add = new HashSet<>();
        Set<ResourceUtil.ExpandedFunctionCall> remove = new HashSet<>();
        boolean allStatic = true;
        for (ResourceUtil.ExpandedRule rule : rules) {
            ResolvedRuleBuilder debugRuleBuilder = debugRulesEvaluation.createResolvedRuleBuilder();
            ObjectAndStatic<Boolean> result = evaluatePredicatesInRule(rule, pendingMacroExpansions, debugRuleBuilder);
            if (result.getObject().booleanValue()) {
                setPopulator.rulePassed(rule, add, remove, debugRuleBuilder);
            }
            allStatic = allStatic && result.isStatic();
        }
        add.removeAll(remove);
        debugRulesEvaluation.setEnabledFunctions(add);
        return new ObjectAndStatic<>(add, allStatic);
    }

    private static String getFunctionName(ResourceUtil.ExpandedFunctionCall functionCall) {
        return Types.valueToString(functionCall.getProperties().get(Key.INSTANCE_NAME.toString()));
    }

    private static void addFunctionImplToMap(Map<String, FunctionCallImplementation> addTo, FunctionCallImplementation impl) {
        if (addTo.containsKey(impl.getInstanceFunctionId())) {
            throw new IllegalArgumentException("Duplicate function type name: " + impl.getInstanceFunctionId());
        }
        addTo.put(impl.getInstanceFunctionId(), impl);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void addMacro(FunctionCallImplementation macro) {
        addFunctionImplToMap(this.mMacroMap, macro);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void addTrackingTag(FunctionCallImplementation tag) {
        addFunctionImplToMap(this.mTrackingTagMap, tag);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void addPredicate(FunctionCallImplementation predicate) {
        addFunctionImplToMap(this.mPredicateMap, predicate);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public ObjectAndStatic<Boolean> evaluatePredicate(ResourceUtil.ExpandedFunctionCall predicate, Set<String> pendingMacroExpansions, ResolvedFunctionCallBuilder debugFunctionCall) {
        ObjectAndStatic<TypeSystem.Value> result = executeFunction(this.mPredicateMap, predicate, pendingMacroExpansions, debugFunctionCall);
        Boolean predicateResult = Types.valueToBoolean(result.getObject());
        debugFunctionCall.setFunctionResult(Types.objectToValue(predicateResult));
        return new ObjectAndStatic<>(predicateResult, result.isStatic());
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public ObjectAndStatic<Boolean> evaluatePredicatesInRule(ResourceUtil.ExpandedRule rule, Set<String> pendingMacroExpansions, ResolvedRuleBuilder debugRuleBuilder) {
        boolean allStatic;
        boolean allStatic2 = true;
        for (ResourceUtil.ExpandedFunctionCall f : rule.getNegativePredicates()) {
            ObjectAndStatic<Boolean> result = evaluatePredicate(f, pendingMacroExpansions, debugRuleBuilder.createNegativePredicate());
            if (result.getObject().booleanValue()) {
                debugRuleBuilder.setValue(Types.objectToValue(false));
                return new ObjectAndStatic<>(false, result.isStatic());
            } else if (!allStatic2 || !result.isStatic()) {
                allStatic2 = false;
            } else {
                allStatic2 = true;
            }
        }
        for (ResourceUtil.ExpandedFunctionCall f2 : rule.getPositivePredicates()) {
            ObjectAndStatic<Boolean> result2 = evaluatePredicate(f2, pendingMacroExpansions, debugRuleBuilder.createPositivePredicate());
            if (!result2.getObject().booleanValue()) {
                debugRuleBuilder.setValue(Types.objectToValue(false));
                return new ObjectAndStatic<>(false, result2.isStatic());
            } else if (!allStatic2 || !result2.isStatic()) {
                allStatic = false;
            } else {
                allStatic = true;
            }
        }
        debugRuleBuilder.setValue(Types.objectToValue(true));
        return new ObjectAndStatic<>(true, allStatic2);
    }

    private ObjectAndStatic<TypeSystem.Value> evaluateMacroReferenceCycleDetection(String macroName, Set<String> pendingMacroExpansions, MacroEvaluationInfoBuilder debugMacroEvaluation) {
        ResourceUtil.ExpandedFunctionCall macro;
        ObjectAndStatic<TypeSystem.Value> cachedResult = this.mMacroEvaluationCache.get(macroName);
        if (cachedResult != null && !this.eventInfoDistributor.debugMode()) {
            return cachedResult;
        }
        MacroInfo macroInfo = this.mMacroLookup.get(macroName);
        if (macroInfo == null) {
            Log.e("Invalid macro: " + macroName);
            return DEFAULT_VALUE_AND_STATIC;
        }
        ObjectAndStatic<Set<ResourceUtil.ExpandedFunctionCall>> macrosToRun = calculateMacrosToRun(macroName, macroInfo.getRules(), macroInfo.getAddMacros(), macroInfo.getAddMacroRuleNames(), macroInfo.getRemoveMacros(), macroInfo.getRemoveMacroRuleNames(), pendingMacroExpansions, debugMacroEvaluation.createRulesEvaluation());
        if (macrosToRun.getObject().isEmpty()) {
            macro = macroInfo.getDefault();
        } else {
            if (macrosToRun.getObject().size() > 1) {
                Log.w("Multiple macros active for macroName " + macroName);
            }
            macro = (ResourceUtil.ExpandedFunctionCall) macrosToRun.getObject().iterator().next();
        }
        if (macro == null) {
            return DEFAULT_VALUE_AND_STATIC;
        }
        ObjectAndStatic<TypeSystem.Value> macroResult = executeFunction(this.mMacroMap, macro, pendingMacroExpansions, debugMacroEvaluation.createResult());
        ObjectAndStatic<TypeSystem.Value> toReturn = macroResult == DEFAULT_VALUE_AND_STATIC ? DEFAULT_VALUE_AND_STATIC : new ObjectAndStatic<>(macroResult.getObject(), macrosToRun.isStatic() && macroResult.isStatic());
        if (toReturn.isStatic()) {
            this.mMacroEvaluationCache.put(macroName, toReturn);
        }
        return toReturn;
    }

    private ObjectAndStatic<TypeSystem.Value> macroExpandValue(TypeSystem.Value value, Set<String> pendingMacroExpansions, ValueBuilder debugValueBuilder) {
        if (!value.getContainsReferences()) {
            return new ObjectAndStatic<>(value, true);
        }
        switch (value.getType()) {
            case LIST:
                TypeSystem.Value.Builder builder = ResourceUtil.newValueBuilderBasedOnValue(value);
                for (int i = 0; i < value.getListItemCount(); i++) {
                    ObjectAndStatic<TypeSystem.Value> macroExpand = macroExpandValue(value.getListItem(i), pendingMacroExpansions, debugValueBuilder.getListItem(i));
                    if (macroExpand == DEFAULT_VALUE_AND_STATIC) {
                        return DEFAULT_VALUE_AND_STATIC;
                    }
                    builder.addListItem(macroExpand.getObject());
                }
                return new ObjectAndStatic<>(builder.build(), false);
            case MAP:
                TypeSystem.Value.Builder builder2 = ResourceUtil.newValueBuilderBasedOnValue(value);
                if (value.getMapKeyCount() != value.getMapValueCount()) {
                    Log.e("Invalid serving value: " + value.toString());
                    return DEFAULT_VALUE_AND_STATIC;
                }
                for (int i2 = 0; i2 < value.getMapKeyCount(); i2++) {
                    ObjectAndStatic<TypeSystem.Value> macroExpandKey = macroExpandValue(value.getMapKey(i2), pendingMacroExpansions, debugValueBuilder.getMapKey(i2));
                    ObjectAndStatic<TypeSystem.Value> macroExpandValue = macroExpandValue(value.getMapValue(i2), pendingMacroExpansions, debugValueBuilder.getMapValue(i2));
                    if (macroExpandKey == DEFAULT_VALUE_AND_STATIC || macroExpandValue == DEFAULT_VALUE_AND_STATIC) {
                        return DEFAULT_VALUE_AND_STATIC;
                    }
                    builder2.addMapKey(macroExpandKey.getObject());
                    builder2.addMapValue(macroExpandValue.getObject());
                }
                return new ObjectAndStatic<>(builder2.build(), false);
            case MACRO_REFERENCE:
                if (pendingMacroExpansions.contains(value.getMacroReference())) {
                    Log.e("Macro cycle detected.  Current macro reference: " + value.getMacroReference() + "." + "  Previous macro references: " + pendingMacroExpansions.toString() + ".");
                    return DEFAULT_VALUE_AND_STATIC;
                }
                pendingMacroExpansions.add(value.getMacroReference());
                ObjectAndStatic<TypeSystem.Value> result = ValueEscapeUtil.applyEscapings(evaluateMacroReferenceCycleDetection(value.getMacroReference(), pendingMacroExpansions, debugValueBuilder.createValueMacroEvaluationInfoExtension()), value.getEscapingList());
                pendingMacroExpansions.remove(value.getMacroReference());
                return result;
            case TEMPLATE:
                TypeSystem.Value.Builder builder3 = ResourceUtil.newValueBuilderBasedOnValue(value);
                for (int i3 = 0; i3 < value.getTemplateTokenCount(); i3++) {
                    ObjectAndStatic<TypeSystem.Value> macroExpand2 = macroExpandValue(value.getTemplateToken(i3), pendingMacroExpansions, debugValueBuilder.getTemplateToken(i3));
                    if (macroExpand2 == DEFAULT_VALUE_AND_STATIC) {
                        return DEFAULT_VALUE_AND_STATIC;
                    }
                    builder3.addTemplateToken(macroExpand2.getObject());
                }
                return new ObjectAndStatic<>(builder3.build(), false);
            default:
                Log.e("Unknown type: " + value.getType());
                return DEFAULT_VALUE_AND_STATIC;
        }
    }

    private ObjectAndStatic<TypeSystem.Value> executeFunction(Map<String, FunctionCallImplementation> implMap, ResourceUtil.ExpandedFunctionCall functionCall, Set<String> pendingMacroExpansions, ResolvedFunctionCallBuilder debugFunctionCall) {
        TypeSystem.Value functionIdValue = functionCall.getProperties().get(Key.FUNCTION.toString());
        if (functionIdValue == null) {
            Log.e("No function id in properties");
            return DEFAULT_VALUE_AND_STATIC;
        }
        String functionId = functionIdValue.getFunctionId();
        FunctionCallImplementation impl = implMap.get(functionId);
        if (impl == null) {
            Log.e(functionId + " has no backing implementation.");
            return DEFAULT_VALUE_AND_STATIC;
        }
        ObjectAndStatic<TypeSystem.Value> cachedResult = this.mFunctionCallCache.get(functionCall);
        if (cachedResult != null && !this.eventInfoDistributor.debugMode()) {
            return cachedResult;
        }
        Map<String, TypeSystem.Value> expandedParams = new HashMap<>();
        boolean allParamsStatic = true;
        for (Map.Entry<String, TypeSystem.Value> originalParam : functionCall.getProperties().entrySet()) {
            ObjectAndStatic<TypeSystem.Value> result = macroExpandValue(originalParam.getValue(), pendingMacroExpansions, debugFunctionCall.createResolvedPropertyBuilder(originalParam.getKey()).createPropertyValueBuilder(originalParam.getValue()));
            if (result == DEFAULT_VALUE_AND_STATIC) {
                return DEFAULT_VALUE_AND_STATIC;
            }
            if (result.isStatic()) {
                functionCall.updateCacheableProperty(originalParam.getKey(), result.getObject());
            } else {
                allParamsStatic = false;
            }
            expandedParams.put(originalParam.getKey(), result.getObject());
        }
        if (!impl.hasRequiredKeys(expandedParams.keySet())) {
            Log.e("Incorrect keys for function " + functionId + " required " + impl.getRequiredKeys() + " had " + expandedParams.keySet());
            return DEFAULT_VALUE_AND_STATIC;
        }
        boolean cacheable = allParamsStatic && impl.isCacheable();
        ObjectAndStatic<TypeSystem.Value> result2 = new ObjectAndStatic<>(impl.evaluate(expandedParams), cacheable);
        if (cacheable) {
            this.mFunctionCallCache.put(functionCall, result2);
        }
        debugFunctionCall.setFunctionResult(result2.getObject());
        return result2;
    }

    private static void verifyFunctionAndNameListSizes(List<ResourceUtil.ExpandedFunctionCall> functionList, List<String> ruleNameList, String operation) {
        if (functionList.size() != ruleNameList.size()) {
            Log.i("Invalid resource: imbalance of rule names of functions for " + operation + " operation. Using default rule name instead");
        }
    }
}
