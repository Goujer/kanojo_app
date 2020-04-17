package com.facebook.model;

import java.util.List;
import org.json.JSONArray;

public interface GraphObjectList<T> extends List<T> {
    <U extends GraphObject> GraphObjectList<U> castToListOf(Class<U> cls);

    JSONArray getInnerJSONArray();
}
