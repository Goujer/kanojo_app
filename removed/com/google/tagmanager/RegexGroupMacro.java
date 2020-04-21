package com.google.tagmanager;

import com.google.analytics.containertag.common.FunctionType;
import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

class RegexGroupMacro extends FunctionCallImplementation {
    private static final String GROUP = Key.GROUP.toString();
    private static final String ID = FunctionType.REGEX_GROUP.toString();
    private static final String IGNORE_CASE = Key.IGNORE_CASE.toString();
    private static final String REGEX = Key.ARG1.toString();
    private static final String TO_MATCH = Key.ARG0.toString();

    public static String getFunctionId() {
        return ID;
    }

    public RegexGroupMacro() {
        super(ID, TO_MATCH, REGEX);
    }

    public boolean isCacheable() {
        return true;
    }

    public TypeSystem.Value evaluate(Map<String, TypeSystem.Value> parameters) {
        TypeSystem.Value toMatch = parameters.get(TO_MATCH);
        TypeSystem.Value regex = parameters.get(REGEX);
        if (toMatch == null || toMatch == Types.getDefaultValue() || regex == null || regex == Types.getDefaultValue()) {
            return Types.getDefaultValue();
        }
        int flags = 64;
        if (Types.valueToBoolean(parameters.get(IGNORE_CASE)).booleanValue()) {
            flags = 64 | 2;
        }
        int groupNumber = 1;
        TypeSystem.Value groupNumberValue = parameters.get(GROUP);
        if (groupNumberValue != null) {
            Long groupNumberLong = Types.valueToInt64(groupNumberValue);
            if (groupNumberLong == Types.getDefaultInt64()) {
                return Types.getDefaultValue();
            }
            groupNumber = groupNumberLong.intValue();
            if (groupNumber < 0) {
                return Types.getDefaultValue();
            }
        }
        try {
            String extracted = null;
            Matcher m = Pattern.compile(Types.valueToString(regex), flags).matcher(Types.valueToString(toMatch));
            if (m.find() && m.groupCount() >= groupNumber) {
                extracted = m.group(groupNumber);
            }
            return extracted == null ? Types.getDefaultValue() : Types.objectToValue(extracted);
        } catch (PatternSyntaxException e) {
            return Types.getDefaultValue();
        }
    }
}
