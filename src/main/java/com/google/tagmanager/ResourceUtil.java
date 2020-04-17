package com.google.tagmanager;

import com.google.analytics.containertag.common.Key;
import com.google.analytics.containertag.proto.Serving;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class ResourceUtil {
    private ResourceUtil() {
    }

    public static class InvalidResourceException extends Exception {
        public InvalidResourceException(String s) {
            super(s);
        }
    }

    public static class ExpandedFunctionCallBuilder {
        private final Map<String, TypeSystem.Value> mPropertiesMap;

        private ExpandedFunctionCallBuilder() {
            this.mPropertiesMap = new HashMap();
        }

        public ExpandedFunctionCallBuilder addProperty(String key, TypeSystem.Value value) {
            this.mPropertiesMap.put(key, value);
            return this;
        }

        public ExpandedFunctionCall build() {
            return new ExpandedFunctionCall(this.mPropertiesMap);
        }
    }

    public static class ExpandedFunctionCall {
        private final Map<String, TypeSystem.Value> mPropertiesMap;

        private ExpandedFunctionCall(Map<String, TypeSystem.Value> propertiesMap) {
            this.mPropertiesMap = propertiesMap;
        }

        public static ExpandedFunctionCallBuilder newBuilder() {
            return new ExpandedFunctionCallBuilder();
        }

        public void updateCacheableProperty(String key, TypeSystem.Value v) {
            this.mPropertiesMap.put(key, v);
        }

        public Map<String, TypeSystem.Value> getProperties() {
            return Collections.unmodifiableMap(this.mPropertiesMap);
        }

        public String toString() {
            return "Properties: " + getProperties();
        }
    }

    public static class ExpandedRuleBuilder {
        private final List<String> mAddMacroRuleNames;
        private final List<ExpandedFunctionCall> mAddMacros;
        private final List<String> mAddTagRuleNames;
        private final List<ExpandedFunctionCall> mAddTags;
        private final List<ExpandedFunctionCall> mNegativePredicates;
        private final List<ExpandedFunctionCall> mPositivePredicates;
        private final List<String> mRemoveMacroRuleNames;
        private final List<ExpandedFunctionCall> mRemoveMacros;
        private final List<String> mRemoveTagRuleNames;
        private final List<ExpandedFunctionCall> mRemoveTags;

        private ExpandedRuleBuilder() {
            this.mPositivePredicates = new ArrayList();
            this.mNegativePredicates = new ArrayList();
            this.mAddTags = new ArrayList();
            this.mRemoveTags = new ArrayList();
            this.mAddMacros = new ArrayList();
            this.mRemoveMacros = new ArrayList();
            this.mAddMacroRuleNames = new ArrayList();
            this.mRemoveMacroRuleNames = new ArrayList();
            this.mAddTagRuleNames = new ArrayList();
            this.mRemoveTagRuleNames = new ArrayList();
        }

        public ExpandedRuleBuilder addPositivePredicate(ExpandedFunctionCall f) {
            this.mPositivePredicates.add(f);
            return this;
        }

        public ExpandedRuleBuilder addNegativePredicate(ExpandedFunctionCall f) {
            this.mNegativePredicates.add(f);
            return this;
        }

        public ExpandedRuleBuilder addAddTag(ExpandedFunctionCall f) {
            this.mAddTags.add(f);
            return this;
        }

        public ExpandedRuleBuilder addAddTagRuleName(String ruleName) {
            this.mAddTagRuleNames.add(ruleName);
            return this;
        }

        public ExpandedRuleBuilder addRemoveTag(ExpandedFunctionCall f) {
            this.mRemoveTags.add(f);
            return this;
        }

        public ExpandedRuleBuilder addRemoveTagRuleName(String ruleName) {
            this.mRemoveTagRuleNames.add(ruleName);
            return this;
        }

        public ExpandedRuleBuilder addAddMacro(ExpandedFunctionCall f) {
            this.mAddMacros.add(f);
            return this;
        }

        public ExpandedRuleBuilder addAddMacroRuleName(String ruleName) {
            this.mAddMacroRuleNames.add(ruleName);
            return this;
        }

        public ExpandedRuleBuilder addRemoveMacro(ExpandedFunctionCall f) {
            this.mRemoveMacros.add(f);
            return this;
        }

        public ExpandedRuleBuilder addRemoveMacroRuleName(String ruleName) {
            this.mRemoveMacroRuleNames.add(ruleName);
            return this;
        }

        public ExpandedRule build() {
            return new ExpandedRule(this.mPositivePredicates, this.mNegativePredicates, this.mAddTags, this.mRemoveTags, this.mAddMacros, this.mRemoveMacros, this.mAddMacroRuleNames, this.mRemoveMacroRuleNames, this.mAddTagRuleNames, this.mRemoveTagRuleNames);
        }
    }

    public static class ExpandedRule {
        private final List<String> mAddMacroRuleNames;
        private final List<ExpandedFunctionCall> mAddMacros;
        private final List<String> mAddTagRuleNames;
        private final List<ExpandedFunctionCall> mAddTags;
        private final List<ExpandedFunctionCall> mNegativePredicates;
        private final List<ExpandedFunctionCall> mPositivePredicates;
        private final List<String> mRemoveMacroRuleNames;
        private final List<ExpandedFunctionCall> mRemoveMacros;
        private final List<String> mRemoveTagRuleNames;
        private final List<ExpandedFunctionCall> mRemoveTags;

        private ExpandedRule(List<ExpandedFunctionCall> postivePredicates, List<ExpandedFunctionCall> negativePredicates, List<ExpandedFunctionCall> addTags, List<ExpandedFunctionCall> removeTags, List<ExpandedFunctionCall> addMacros, List<ExpandedFunctionCall> removeMacros, List<String> addMacroRuleNames, List<String> removeMacroRuleNames, List<String> addTagRuleNames, List<String> removeTagRuleNames) {
            this.mPositivePredicates = Collections.unmodifiableList(postivePredicates);
            this.mNegativePredicates = Collections.unmodifiableList(negativePredicates);
            this.mAddTags = Collections.unmodifiableList(addTags);
            this.mRemoveTags = Collections.unmodifiableList(removeTags);
            this.mAddMacros = Collections.unmodifiableList(addMacros);
            this.mRemoveMacros = Collections.unmodifiableList(removeMacros);
            this.mAddMacroRuleNames = Collections.unmodifiableList(addMacroRuleNames);
            this.mRemoveMacroRuleNames = Collections.unmodifiableList(removeMacroRuleNames);
            this.mAddTagRuleNames = Collections.unmodifiableList(addTagRuleNames);
            this.mRemoveTagRuleNames = Collections.unmodifiableList(removeTagRuleNames);
        }

        public static ExpandedRuleBuilder newBuilder() {
            return new ExpandedRuleBuilder();
        }

        public List<ExpandedFunctionCall> getPositivePredicates() {
            return this.mPositivePredicates;
        }

        public List<ExpandedFunctionCall> getNegativePredicates() {
            return this.mNegativePredicates;
        }

        public List<ExpandedFunctionCall> getAddTags() {
            return this.mAddTags;
        }

        public List<ExpandedFunctionCall> getRemoveTags() {
            return this.mRemoveTags;
        }

        public List<ExpandedFunctionCall> getAddMacros() {
            return this.mAddMacros;
        }

        public List<String> getAddMacroRuleNames() {
            return this.mAddMacroRuleNames;
        }

        public List<String> getRemoveMacroRuleNames() {
            return this.mRemoveMacroRuleNames;
        }

        public List<String> getAddTagRuleNames() {
            return this.mAddTagRuleNames;
        }

        public List<String> getRemoveTagRuleNames() {
            return this.mRemoveTagRuleNames;
        }

        public List<ExpandedFunctionCall> getRemoveMacros() {
            return this.mRemoveMacros;
        }

        public String toString() {
            return "Positive predicates: " + getPositivePredicates() + "  Negative predicates: " + getNegativePredicates() + "  Add tags: " + getAddTags() + "  Remove tags: " + getRemoveTags() + "  Add macros: " + getAddMacros() + "  Remove macros: " + getRemoveMacros();
        }
    }

    public static class ExpandedResourceBuilder {
        private final Map<String, List<ExpandedFunctionCall>> mMacros;
        private final List<ExpandedFunctionCall> mPredicates;
        private int mResourceFormatVersion;
        private final List<ExpandedRule> mRules;
        private final List<ExpandedFunctionCall> mTags;
        private String mVersion;

        private ExpandedResourceBuilder() {
            this.mRules = new ArrayList();
            this.mTags = new ArrayList();
            this.mPredicates = new ArrayList();
            this.mMacros = new HashMap();
            this.mVersion = "";
            this.mResourceFormatVersion = 0;
        }

        public ExpandedResourceBuilder addRule(ExpandedRule r) {
            this.mRules.add(r);
            return this;
        }

        public ExpandedResourceBuilder addMacro(ExpandedFunctionCall f) {
            String macroName = Types.valueToString(f.getProperties().get(Key.INSTANCE_NAME.toString()));
            List<ExpandedFunctionCall> macroList = this.mMacros.get(macroName);
            if (macroList == null) {
                macroList = new ArrayList<>();
                this.mMacros.put(macroName, macroList);
            }
            macroList.add(f);
            return this;
        }

        public ExpandedResourceBuilder setVersion(String version) {
            this.mVersion = version;
            return this;
        }

        public ExpandedResourceBuilder setResourceFormatVersion(int resourceFormatVersion) {
            this.mResourceFormatVersion = resourceFormatVersion;
            return this;
        }

        public ExpandedResource build() {
            return new ExpandedResource(this.mRules, this.mMacros, this.mVersion, this.mResourceFormatVersion);
        }
    }

    public static class ExpandedResource {
        private final Map<String, List<ExpandedFunctionCall>> mMacros;
        private final int mResourceFormatVersion;
        private final List<ExpandedRule> mRules;
        private final String mVersion;

        private ExpandedResource(List<ExpandedRule> rules, Map<String, List<ExpandedFunctionCall>> macros, String version, int resourceFormatVersion) {
            this.mRules = Collections.unmodifiableList(rules);
            this.mMacros = Collections.unmodifiableMap(macros);
            this.mVersion = version;
            this.mResourceFormatVersion = resourceFormatVersion;
        }

        public static ExpandedResourceBuilder newBuilder() {
            return new ExpandedResourceBuilder();
        }

        public List<ExpandedRule> getRules() {
            return this.mRules;
        }

        public String getVersion() {
            return this.mVersion;
        }

        public int getResourceFormatVersion() {
            return this.mResourceFormatVersion;
        }

        public List<ExpandedFunctionCall> getMacros(String name) {
            return this.mMacros.get(name);
        }

        public Map<String, List<ExpandedFunctionCall>> getAllMacros() {
            return this.mMacros;
        }

        public String toString() {
            return "Rules: " + getRules() + "  Macros: " + this.mMacros;
        }
    }

    public static ExpandedResource getExpandedResource(Serving.Resource resource) throws InvalidResourceException {
        TypeSystem.Value[] expandedValues = new TypeSystem.Value[resource.getValueCount()];
        for (int i = 0; i < resource.getValueCount(); i++) {
            expandValue(i, resource, expandedValues, new HashSet(0));
        }
        ExpandedResourceBuilder builder = ExpandedResource.newBuilder();
        List<ExpandedFunctionCall> tags = new ArrayList<>();
        for (int i2 = 0; i2 < resource.getTagCount(); i2++) {
            tags.add(expandFunctionCall(resource.getTag(i2), resource, expandedValues, i2));
        }
        List<ExpandedFunctionCall> predicates = new ArrayList<>();
        for (int i3 = 0; i3 < resource.getPredicateCount(); i3++) {
            predicates.add(expandFunctionCall(resource.getPredicate(i3), resource, expandedValues, i3));
        }
        List<ExpandedFunctionCall> macros = new ArrayList<>();
        for (int i4 = 0; i4 < resource.getMacroCount(); i4++) {
            ExpandedFunctionCall thisMacro = expandFunctionCall(resource.getMacro(i4), resource, expandedValues, i4);
            builder.addMacro(thisMacro);
            macros.add(thisMacro);
        }
        for (Serving.Rule r : resource.getRuleList()) {
            builder.addRule(expandRule(r, tags, macros, predicates, resource));
        }
        builder.setVersion(resource.getVersion());
        builder.setResourceFormatVersion(resource.getResourceFormatVersion());
        return builder.build();
    }

    public static TypeSystem.Value.Builder newValueBuilderBasedOnValue(TypeSystem.Value v) {
        TypeSystem.Value.Builder result = TypeSystem.Value.newBuilder().setType(v.getType()).addAllEscaping(v.getEscapingList());
        if (v.getContainsReferences()) {
            result.setContainsReferences(true);
        }
        return result;
    }

    private static TypeSystem.Value expandValue(int i, Serving.Resource resource, TypeSystem.Value[] expandedValues, Set<Integer> pendingExpansions) throws InvalidResourceException {
        String macroName;
        if (pendingExpansions.contains(Integer.valueOf(i))) {
            logAndThrow("Value cycle detected.  Current value reference: " + i + "." + "  Previous value references: " + pendingExpansions + ".");
        }
        TypeSystem.Value value = (TypeSystem.Value) getWithBoundsCheck(resource.getValueList(), i, "values");
        if (expandedValues[i] != null) {
            return expandedValues[i];
        }
        TypeSystem.Value toAdd = null;
        pendingExpansions.add(Integer.valueOf(i));
        switch (value.getType()) {
            case LIST:
                TypeSystem.Value.Builder builder = newValueBuilderBasedOnValue(value);
                for (Integer listIndex : getServingValue(value).getListItemList()) {
                    builder.addListItem(expandValue(listIndex.intValue(), resource, expandedValues, pendingExpansions));
                }
                toAdd = builder.build();
                break;
            case MAP:
                TypeSystem.Value.Builder builder2 = newValueBuilderBasedOnValue(value);
                Serving.ServingValue servingValue = getServingValue(value);
                if (servingValue.getMapKeyCount() != servingValue.getMapValueCount()) {
                    logAndThrow("Uneven map keys (" + servingValue.getMapKeyCount() + ") and map values (" + servingValue.getMapValueCount() + ")");
                }
                for (Integer keyIndex : servingValue.getMapKeyList()) {
                    builder2.addMapKey(expandValue(keyIndex.intValue(), resource, expandedValues, pendingExpansions));
                }
                for (Integer valueIndex : servingValue.getMapValueList()) {
                    builder2.addMapValue(expandValue(valueIndex.intValue(), resource, expandedValues, pendingExpansions));
                }
                toAdd = builder2.build();
                break;
            case MACRO_REFERENCE:
                TypeSystem.Value.Builder builder3 = newValueBuilderBasedOnValue(value);
                Serving.ServingValue servingValue2 = getServingValue(value);
                if (!servingValue2.hasMacroNameReference()) {
                    logAndThrow("Missing macro name reference");
                    macroName = "";
                } else {
                    macroName = Types.valueToString(expandValue(servingValue2.getMacroNameReference(), resource, expandedValues, pendingExpansions));
                }
                builder3.setMacroReference(macroName);
                toAdd = builder3.build();
                break;
            case TEMPLATE:
                TypeSystem.Value.Builder builder4 = newValueBuilderBasedOnValue(value);
                for (Integer templateIndex : getServingValue(value).getTemplateTokenList()) {
                    builder4.addTemplateToken(expandValue(templateIndex.intValue(), resource, expandedValues, pendingExpansions));
                }
                toAdd = builder4.build();
                break;
            case STRING:
            case FUNCTION_ID:
            case INTEGER:
            case BOOLEAN:
                toAdd = value;
                break;
        }
        if (toAdd == null) {
            logAndThrow("Invalid value: " + value);
        }
        expandedValues[i] = toAdd;
        pendingExpansions.remove(Integer.valueOf(i));
        return toAdd;
    }

    private static Serving.ServingValue getServingValue(TypeSystem.Value value) throws InvalidResourceException {
        if (!value.hasExtension(Serving.ServingValue.ext)) {
            logAndThrow("Expected a ServingValue and didn't get one. Value is: " + value);
        }
        return (Serving.ServingValue) value.getExtension(Serving.ServingValue.ext);
    }

    private static void logAndThrow(String error) throws InvalidResourceException {
        Log.e(error);
        throw new InvalidResourceException(error);
    }

    private static <T> T getWithBoundsCheck(T[] array, int idx, String listName) throws InvalidResourceException {
        if (idx < 0 || idx >= array.length) {
            logAndThrow("Index out of bounds detected: " + idx + " in " + listName);
        }
        return array[idx];
    }

    private static <T> T getWithBoundsCheck(List<T> list, int idx, String listName) throws InvalidResourceException {
        if (idx < 0 || idx >= list.size()) {
            logAndThrow("Index out of bounds detected: " + idx + " in " + listName);
        }
        return list.get(idx);
    }

    private static ExpandedFunctionCall expandFunctionCall(Serving.FunctionCall functionCall, Serving.Resource resource, TypeSystem.Value[] expandedValues, int idx) throws InvalidResourceException {
        ExpandedFunctionCallBuilder builder = ExpandedFunctionCall.newBuilder();
        for (Integer i : functionCall.getPropertyList()) {
            Serving.Property p = (Serving.Property) getWithBoundsCheck(resource.getPropertyList(), i.intValue(), "properties");
            builder.addProperty((String) getWithBoundsCheck(resource.getKeyList(), p.getKey(), "keys"), (TypeSystem.Value) getWithBoundsCheck((T[]) expandedValues, p.getValue(), "values"));
        }
        return builder.build();
    }

    private static ExpandedRule expandRule(Serving.Rule rule, List<ExpandedFunctionCall> tags, List<ExpandedFunctionCall> macros, List<ExpandedFunctionCall> predicates, Serving.Resource resource) {
        ExpandedRuleBuilder ruleBuilder = ExpandedRule.newBuilder();
        for (Integer i : rule.getPositivePredicateList()) {
            ruleBuilder.addPositivePredicate(predicates.get(i.intValue()));
        }
        for (Integer i2 : rule.getNegativePredicateList()) {
            ruleBuilder.addNegativePredicate(predicates.get(i2.intValue()));
        }
        for (Integer tagIndex : rule.getAddTagList()) {
            ruleBuilder.addAddTag(tags.get(tagIndex.intValue()));
        }
        for (Integer ruleNameIndex : rule.getAddTagRuleNameList()) {
            ruleBuilder.addAddTagRuleName(resource.getValue(ruleNameIndex.intValue()).getString());
        }
        for (Integer tagIndex2 : rule.getRemoveTagList()) {
            ruleBuilder.addRemoveTag(tags.get(tagIndex2.intValue()));
        }
        for (Integer ruleNameIndex2 : rule.getRemoveTagRuleNameList()) {
            ruleBuilder.addRemoveTagRuleName(resource.getValue(ruleNameIndex2.intValue()).getString());
        }
        for (Integer macroIndex : rule.getAddMacroList()) {
            ruleBuilder.addAddMacro(macros.get(macroIndex.intValue()));
        }
        for (Integer ruleNameIndex3 : rule.getAddMacroRuleNameList()) {
            ruleBuilder.addAddMacroRuleName(resource.getValue(ruleNameIndex3.intValue()).getString());
        }
        for (Integer macroIndex2 : rule.getRemoveMacroList()) {
            ruleBuilder.addRemoveMacro(macros.get(macroIndex2.intValue()));
        }
        for (Integer ruleNameIndex4 : rule.getRemoveMacroRuleNameList()) {
            ruleBuilder.addRemoveMacroRuleName(resource.getValue(ruleNameIndex4.intValue()).getString());
        }
        return ruleBuilder.build();
    }
}
