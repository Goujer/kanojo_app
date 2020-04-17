package jp.co.cybird.app.android.lib.commons.file.json;

import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;
import org.w3c.dom.CharacterData;

/* compiled from: Formatter */
final class CharacterDataFormatter implements Formatter {
    public static final CharacterDataFormatter INSTANCE = new CharacterDataFormatter();

    CharacterDataFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        return StringFormatter.INSTANCE.format(context, src, ((CharacterData) o).getData(), out);
    }
}
