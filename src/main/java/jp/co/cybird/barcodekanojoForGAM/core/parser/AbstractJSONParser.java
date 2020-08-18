package jp.co.cybird.barcodekanojoForGAM.core.parser;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import jp.co.cybird.barcodekanojoForGAM.Defs;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;

import org.json.JSONObject;

public abstract class AbstractJSONParser<T extends BarcodeKanojoModel> implements JSONParser<T> {

    protected static final String TAG = "AbstractJSONParser";

    protected abstract T parseInner(JSONObject jSONObject) throws IOException, BarcodeKanojoException, BarcodeKanojoParseException;

    public final T parse(JSONObject object) throws BarcodeKanojoParseException, BarcodeKanojoException {
        try {
            return parseInner(object);
        } catch (IOException e) {
            throw new BarcodeKanojoParseException(e.getMessage());
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x004c, code lost:
        throw new java.lang.IllegalArgumentException(TAG + r0.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x004d, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0066, code lost:
        throw new java.lang.IllegalArgumentException(TAG + r0.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0033, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x004d A[ExcHandler: IOException (r0v0 'e' java.io.IOException A[CUSTOM_DECLARE]), Splitter:B:0:0x0000] */
    public static JSONObject createJSONObject(InputStream is) {
		try {
			StringBuilder objJson = new StringBuilder();
			try {
				BufferedReader objBuf = new BufferedReader(new InputStreamReader(is));
				String sLine;
				while ((sLine = objBuf.readLine()) != null) {
					//new JSONObject(sLine);
					objJson.append(sLine);
				}
				is.close();
				objBuf.close();
				return new JSONObject(objJson.toString());
			} catch (Exception e) {
				if (Defs.DEBUG) {
					Log.w(TAG, "Length: " + objJson.length() + " Downloaded: " + objJson.toString());
					Throwable t = e.getCause();
					if (t != null) {
						t.printStackTrace();
					}
					e.printStackTrace();
				}
				is.close();
			}
		} catch (IOException e) {
			if (Defs.DEBUG) e.printStackTrace();
		}
		return null;
    }
}
