package org.apache.james.mime4j.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class StringArrayMap implements Serializable {
    private static final long serialVersionUID = -5833051164281786907L;
    private final Map<String, Object> map = new HashMap();

    public static String asString(Object pValue) {
        if (pValue == null) {
            return null;
        }
        if (pValue instanceof String) {
            return (String) pValue;
        }
        if (pValue instanceof String[]) {
            return ((String[]) pValue)[0];
        }
        if (pValue instanceof List) {
            return (String) ((List) pValue).get(0);
        }
        throw new IllegalStateException("Invalid parameter class: " + pValue.getClass().getName());
    }

    public static String[] asStringArray(Object pValue) {
        if (pValue == null) {
            return null;
        }
        if (pValue instanceof String) {
            return new String[]{(String) pValue};
        } else if (pValue instanceof String[]) {
            return (String[]) pValue;
        } else {
            if (pValue instanceof List) {
                List<?> l = (List) pValue;
                return (String[]) l.toArray(new String[l.size()]);
            }
            throw new IllegalStateException("Invalid parameter class: " + pValue.getClass().getName());
        }
    }

    public static Enumeration<String> asStringEnum(final Object pValue) {
        if (pValue == null) {
            return null;
        }
        if (pValue instanceof String) {
            return new Enumeration<String>() {
                private Object value = pValue;

                public boolean hasMoreElements() {
                    return this.value != null;
                }

                public String nextElement() {
                    if (this.value == null) {
                        throw new NoSuchElementException();
                    }
                    String s = (String) this.value;
                    this.value = null;
                    return s;
                }
            };
        }
        if (pValue instanceof String[]) {
            final String[] values = (String[]) pValue;
            return new Enumeration<String>() {
                private int offset;

                public boolean hasMoreElements() {
                    return this.offset < values.length;
                }

                public String nextElement() {
                    if (this.offset >= values.length) {
                        throw new NoSuchElementException();
                    }
                    String[] strArr = values;
                    int i = this.offset;
                    this.offset = i + 1;
                    return strArr[i];
                }
            };
        } else if (pValue instanceof List) {
            return Collections.enumeration((List) pValue);
        } else {
            throw new IllegalStateException("Invalid parameter class: " + pValue.getClass().getName());
        }
    }

    public static Map<String, String[]> asMap(Map<String, Object> pMap) {
        Map<String, String[]> result = new HashMap<>(pMap.size());
        for (Map.Entry<String, Object> entry : pMap.entrySet()) {
            result.put(entry.getKey(), asStringArray(entry.getValue()));
        }
        return Collections.unmodifiableMap(result);
    }

    /* access modifiers changed from: protected */
    public void addMapValue(Map<String, Object> pMap, String pName, String pValue) {
        Object o = pMap.get(pName);
        if (o == null) {
            o = pValue;
        } else if (o instanceof String) {
            List<Object> list = new ArrayList<>();
            list.add(o);
            list.add(pValue);
            o = list;
        } else if (o instanceof List) {
            ((List) o).add(pValue);
        } else if (o instanceof String[]) {
            List<String> list2 = new ArrayList<>();
            for (String str : (String[]) o) {
                list2.add(str);
            }
            list2.add(pValue);
            o = list2;
        } else {
            throw new IllegalStateException("Invalid object type: " + o.getClass().getName());
        }
        pMap.put(pName, o);
    }

    /* access modifiers changed from: protected */
    public String convertName(String pName) {
        return pName.toLowerCase();
    }

    public String getValue(String pName) {
        return asString(this.map.get(convertName(pName)));
    }

    public String[] getValues(String pName) {
        return asStringArray(this.map.get(convertName(pName)));
    }

    public Enumeration<String> getValueEnum(String pName) {
        return asStringEnum(this.map.get(convertName(pName)));
    }

    public Enumeration<String> getNames() {
        return Collections.enumeration(this.map.keySet());
    }

    public Map<String, String[]> getMap() {
        return asMap(this.map);
    }

    public void addValue(String pName, String pValue) {
        addMapValue(this.map, convertName(pName), pValue);
    }

    public String[] getNameArray() {
        Collection<String> c = this.map.keySet();
        return (String[]) c.toArray(new String[c.size()]);
    }
}
