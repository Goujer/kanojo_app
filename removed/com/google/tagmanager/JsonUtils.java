package com.google.tagmanager;

import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.tagmanager.ResourceUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class JsonUtils {
    private JsonUtils() {
    }

    public static ResourceUtil.ExpandedResource expandedResourceFromJsonString(String jsonString) throws JSONException {
        TypeSystem.Value value = jsonObjectToValue(new JSONObject(jsonString));
        ResourceUtil.ExpandedResourceBuilder builder = ResourceUtil.ExpandedResource.newBuilder();
        for (int i = 0; i < value.getMapKeyCount(); i++) {
            builder.addMacro(ResourceUtil.ExpandedFunctionCall.newBuilder().addProperty(Key.INSTANCE_NAME.toString(), value.getMapKey(i)).addProperty(Key.FUNCTION.toString(), Types.functionIdToValue(ConstantMacro.getFunctionId())).addProperty(ConstantMacro.getValueKey(), value.getMapValue(i)).build());
        }
        return builder.build();
    }

    private static TypeSystem.Value jsonObjectToValue(Object o) throws JSONException {
        return Types.objectToValue(jsonObjectToObject(o));
    }

    @VisibleForTesting
    static Object jsonObjectToObject(Object o) throws JSONException {
        if (o instanceof JSONArray) {
            throw new RuntimeException("JSONArrays are not supported");
        } else if (JSONObject.NULL.equals(o)) {
            throw new RuntimeException("JSON nulls are not supported");
        } else if (!(o instanceof JSONObject)) {
            return o;
        } else {
            JSONObject jsonObject = (JSONObject) o;
            Map<String, Object> map = new HashMap<>();
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                map.put(key, jsonObjectToObject(jsonObject.get(key)));
            }
            return map;
        }
    }
}
