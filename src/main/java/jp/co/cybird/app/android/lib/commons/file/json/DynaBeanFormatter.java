package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;
import jp.co.cybird.app.android.lib.commons.file.json.util.ClassUtil;

/* compiled from: Formatter */
final class DynaBeanFormatter implements Formatter {
    public static final DynaBeanFormatter INSTANCE = new DynaBeanFormatter();

    DynaBeanFormatter() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00de, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00e9, code lost:
        if ((r9.getCause() instanceof java.lang.Error) != false) goto L_0x00eb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00f1, code lost:
        throw ((java.lang.Error) r9.getCause());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0172, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0173, code lost:
        r3 = r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x01a5, code lost:
        if ((r9.getCause() instanceof java.lang.RuntimeException) != false) goto L_0x01a7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x01ad, code lost:
        throw ((java.lang.RuntimeException) r9.getCause());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x01b4, code lost:
        throw ((java.lang.Exception) r9.getCause());
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00de A[ExcHandler: InvocationTargetException (r9v0 'e' java.lang.reflect.InvocationTargetException A[CUSTOM_DECLARE]), Splitter:B:1:0x000c] */
    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        out.append('{');
        int count = 0;
        try {
            Class<?> dynaBeanClass = ClassUtil.findClass("org.apache.commons.beanutils.DynaBean");
            Object dynaClass = dynaBeanClass.getMethod("getDynaClass", new Class[0]).invoke(o, new Object[0]);
            Object[] dynaProperties = (Object[]) dynaClass.getClass().getMethod("getDynaProperties", new Class[0]).invoke(dynaClass, new Object[0]);
            if (dynaProperties != null && dynaProperties.length > 0) {
                Method getName = dynaProperties[0].getClass().getMethod("getName", new Class[0]);
                Method get = dynaBeanClass.getMethod("get", new Class[]{String.class});
                JSONHint hint = context.getHint();
                int length = dynaProperties.length;
                for (int i = 0; i < length; i++) {
                    Object name = null;
                    name = getName.invoke(dynaProperties[i], new Object[0]);
                    if (name != null) {
                        Object value = null;
                        Exception cause = null;
                        value = get.invoke(o, new Object[]{name});
                        if (value == src) {
                            continue;
                        } else {
                            if (cause == null) {
                                if (context.isSuppressNull() && value == null) {
                                }
                            }
                            if (count != 0) {
                                out.append(',');
                            }
                            if (context.isPrettyPrint()) {
                                out.append(10);
                                int indent = context.getInitialIndent() + context.getDepth() + 1;
                                for (int j = 0; j < indent; j++) {
                                    out.append(context.getIndentText());
                                }
                            }
                            StringFormatter.serialize(context, name.toString(), out);
                            out.append(':');
                            if (context.isPrettyPrint()) {
                                out.append(' ');
                            }
                            context.enter(name, hint);
                            if (cause != null) {
                                throw cause;
                            }
                            context.formatInternal(context.preformatInternal(value), out);
                            context.exit();
                            count++;
                        }
                    }
                }
            }
        } catch (InvocationTargetException e) {
            throw e;
        } catch (Exception e2) {
        } catch (InvocationTargetException e3) {
        } catch (Exception e4) {
        }
        if (context.isPrettyPrint() && count > 0) {
            out.append(10);
            int indent2 = context.getInitialIndent() + context.getDepth();
            for (int j2 = 0; j2 < indent2; j2++) {
                out.append(context.getIndentText());
            }
        }
        out.append('}');
        return true;
    }
}
