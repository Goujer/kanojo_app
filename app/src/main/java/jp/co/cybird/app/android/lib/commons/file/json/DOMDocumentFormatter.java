package jp.co.cybird.app.android.lib.commons.file.json;

import jp.co.cybird.app.android.lib.commons.file.json.JSON;
import jp.co.cybird.app.android.lib.commons.file.json.io.OutputSource;
import org.w3c.dom.Document;

/* compiled from: Formatter */
final class DOMDocumentFormatter implements Formatter {
    public static final DOMDocumentFormatter INSTANCE = new DOMDocumentFormatter();

    DOMDocumentFormatter() {
    }

    public boolean format(JSON.JSONContext context, Object src, Object o, OutputSource out) throws Exception {
        return DOMElementFormatter.INSTANCE.format(context, src, ((Document) o).getDocumentElement(), out);
    }
}
