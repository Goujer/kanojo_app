package twitter4j.internal.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import twitter4j.json.DataObjectFactory;

public class DataObjectFactoryUtil {
    private static final Method CLEAR_THREAD_LOCAL_MAP;
    private static final Method REGISTER_JSON_OBJECT;

    private DataObjectFactoryUtil() {
        throw new AssertionError("not intended to be instantiated.");
    }

    static {
        Method clearThreadLocalMap = null;
        Method registerJSONObject = null;
        for (Method method : DataObjectFactory.class.getDeclaredMethods()) {
            if (method.getName().equals("clearThreadLocalMap")) {
                clearThreadLocalMap = method;
                clearThreadLocalMap.setAccessible(true);
            } else if (method.getName().equals("registerJSONObject")) {
                registerJSONObject = method;
                registerJSONObject.setAccessible(true);
            }
        }
        if (clearThreadLocalMap == null || registerJSONObject == null) {
            throw new AssertionError();
        }
        CLEAR_THREAD_LOCAL_MAP = clearThreadLocalMap;
        REGISTER_JSON_OBJECT = registerJSONObject;
    }

    public static void clearThreadLocalMap() {
        try {
            CLEAR_THREAD_LOCAL_MAP.invoke((Object) null, new Object[0]);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e2) {
            throw new AssertionError(e2);
        }
    }

    public static <T> T registerJSONObject(T key, Object json) {
        try {
            return REGISTER_JSON_OBJECT.invoke((Object) null, new Object[]{key, json});
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e2) {
            throw new AssertionError(e2);
        }
    }
}
