package jp.co.cybird.barcodekanojoForGAM.core.parser;

import android.util.Log;

import com.goujer.utils.StringUtilKt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import jp.co.cybird.barcodekanojoForGAM.Defs;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.BarcodeKanojoModel;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class AbstractJSONParser<T extends BarcodeKanojoModel> implements JSONParser<T> {

    protected static final String TAG = "AbstractJSONParser";

    protected abstract T parseInner(JSONObject jSONObject) throws IOException, BarcodeKanojoException;

    public final T parse(JSONObject object) throws BarcodeKanojoException {
        try {
            return parseInner(object);
        } catch (IOException e) {
            throw new BarcodeKanojoParseException(e.getMessage());
        }
    }

    /* JADX WARNING: Had issues */
    public static JSONObject createJSONObject(InputStream is) {
		try {
			StringBuilder objJson = new StringBuilder();
			BufferedReader objBuf = new BufferedReader(new InputStreamReader(is));
			try {
				String sLine;
				while ((sLine = objBuf.readLine()) != null) {
					//new JSONObject(sLine);
					objJson.append(sLine);
				}
				is.close();
				objBuf.close();
				return new JSONObject(objJson.toString());
			} catch (Exception e) {
				is.close();
				objBuf.close();
				if (Defs.DEBUG) {
					Log.w(TAG, "Length: " + objJson.length() + " Downloaded: " + objJson.toString());
					Throwable t = e.getCause();
					if (t != null) {
						t.printStackTrace();
					}
					e.printStackTrace();
				}
				String json = objJson.toString();
				int missingBrackets = StringUtilKt.countOccurrences(json, '{') - StringUtilKt.countOccurrences(json, '}');
				for (int i = 0; i < missingBrackets; i++) {
					objJson.append("}");
				}
				return new JSONObject(objJson.toString());
			}
		} catch (IOException | JSONException e) {
			if (Defs.DEBUG) e.printStackTrace();
		}
		return null;
    }
}
