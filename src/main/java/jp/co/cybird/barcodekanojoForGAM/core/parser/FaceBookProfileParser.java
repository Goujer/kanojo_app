package jp.co.cybird.barcodekanojoForGAM.core.parser;

import com.google.android.gcm.GCMConstants;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.FaceBookProfile;
import org.json.JSONException;
import org.json.JSONObject;

public class FaceBookProfileParser extends AbstractJSONParser<FaceBookProfile> {
    public static final boolean DEBUG = false;

    /* access modifiers changed from: protected */
    public FaceBookProfile parseInner(JSONObject object) throws BarcodeKanojoException, BarcodeKanojoParseException {
        FaceBookProfile res = new FaceBookProfile();
        try {
            if (object.has(GCMConstants.EXTRA_ERROR)) {
                JSONObject err = object.getJSONObject(GCMConstants.EXTRA_ERROR);
                if (err.has("message")) {
                    res.setMessage(err.getString("message"));
                }
                if (err.has("code")) {
                    res.setCode(err.getString("code"));
                }
                if (err.has("subCode")) {
                    res.setSubCode(err.getString("subCode"));
                }
                if (err.has("type")) {
                    res.setType(err.getString("type"));
                }
                throw new BarcodeKanojoException(res.getMessage());
            }
            if (object.has("name")) {
                res.setName(object.getString("name"));
            }
            if (object.has("gender")) {
                res.setGender(object.getString("gender"));
            }
            if (object.has("birthday")) {
                res.setBirthday(object.getString("birthday"));
            }
            if (object.has("id")) {
                res.setId(object.getString("id"));
            }
            if (object.has("picture") && object.getJSONObject("picture").has("data") && object.getJSONObject("picture").getJSONObject("data").has("url")) {
                res.setUrl(object.getJSONObject("picture").getJSONObject("data").getString("url"));
            }
            return res;
        } catch (JSONException e) {
            res.setMessage(e.getMessage());
        }
    }
}
