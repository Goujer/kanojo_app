package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeReference<T> implements Type {
    public Type getType() {
        Type[] args;
        Type type = getClass().getGenericSuperclass();
        if ((type instanceof ParameterizedType) && (args = ((ParameterizedType) type).getActualTypeArguments()) != null && args.length == 1) {
            return args[0];
        }
        throw new IllegalStateException("Reference must be specified actual type.");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append("[").append(getType()).append("]");
        return sb.toString();
    }
}
