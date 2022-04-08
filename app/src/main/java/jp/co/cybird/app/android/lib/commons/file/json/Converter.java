package jp.co.cybird.app.android.lib.commons.file.json;

import java.lang.reflect.Type;
import jp.co.cybird.app.android.lib.commons.file.json.JSON;

interface Converter {
    Object convert(JSON.JSONContext jSONContext, Object obj, Class<?> cls, Type type) throws Exception;
}
