package jp.co.cybird.app.android.lib.commons.file.json;

import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;

interface Formatter {
    boolean format(JSON.JSONContext jSONContext, Object obj, Object obj2, OutputSource outputSource) throws Exception;
}
