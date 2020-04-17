package com.google.tagmanager;

import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.HashMap;
import java.util.Map;

class CustomFunctionCall extends FunctionCallImplementation {
    private static final String ADDITIONAL_PARAMS = Key.ADDITIONAL_PARAMS.toString();
    private static final String FUNCTION_CALL_NAME = Key.FUNCTION_CALL_NAME.toString();
    private static final String ID = FunctionType.FUNCTION_CALL.toString();
    private final CustomEvaluator mFunctionCallEvaluator;

    public interface CustomEvaluator {
        Object evaluate(String str, Map<String, Object> map);
    }

    public static String getFunctionId() {
        return ID;
    }

    public static String getFunctionCallNameKey() {
        return FUNCTION_CALL_NAME;
    }

    public static String getAdditionalParamsKey() {
        return ADDITIONAL_PARAMS;
    }

    public CustomFunctionCall(CustomEvaluator functionCallEvaluator) {
        super(ID, FUNCTION_CALL_NAME);
        this.mFunctionCallEvaluator = functionCallEvaluator;
    }

    public boolean isCacheable() {
        return false;
    }

    public TypeSystem.Value evaluate(Map<String, TypeSystem.Value> parameters) {
        String functionCallName = Types.valueToString(parameters.get(FUNCTION_CALL_NAME));
        Map<String, Object> objectParams = new HashMap<>();
        TypeSystem.Value additionalParamsValue = parameters.get(ADDITIONAL_PARAMS);
        if (additionalParamsValue != null) {
            Object additionalParamsObject = Types.valueToObject(additionalParamsValue);
            if (!(additionalParamsObject instanceof Map)) {
                Log.w("FunctionCallMacro: expected ADDITIONAL_PARAMS to be a map.");
                return Types.getDefaultValue();
            }
            for (Map.Entry<Object, Object> entry : ((Map) additionalParamsObject).entrySet()) {
                objectParams.put(entry.getKey().toString(), entry.getValue());
            }
        }
        try {
            return Types.objectToValue(this.mFunctionCallEvaluator.evaluate(functionCallName, objectParams));
        } catch (Exception e) {
            Log.w("Custom macro/tag " + functionCallName + " threw exception " + e.getMessage());
            return Types.getDefaultValue();
        }
    }
}
