package jp.co.cybird.barcodekanojoForGAM.core.parser;

import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import jp.co.cybird.barcodekanojoForGAM.core.model.Barcode;
import org.json.JSONException;
import org.json.JSONObject;

public class BarcodeParser extends AbstractJSONParser<Barcode> {
    protected Barcode parseInner(JSONObject object) throws BarcodeKanojoException, BarcodeKanojoParseException {
        Barcode res = new Barcode();
        try {
            if (object.has("barcode")) {
                res.setBarcode(object.getString("barcode"));
            }
            if (object.has("race_type")) {
                res.setRace_type(object.getInt("race_type"));
            }
            if (object.has("eye_type")) {
                res.setEye_type(object.getInt("eye_type"));
            }
            if (object.has("nose_type")) {
                res.setNose_type(object.getInt("nose_type"));
            }
            if (object.has("mouth_type")) {
                res.setMouth_type(object.getInt("mouth_type"));
            }
            if (object.has("face_type")) {
                res.setFace_type(object.getInt("face_type"));
            }
            if (object.has("brow_type")) {
                res.setBrow_type(object.getInt("brow_type"));
            }
            if (object.has("fringe_type")) {
                res.setFringe_type(object.getInt("fringe_type"));
            }
            if (object.has("hair_type")) {
                res.setHair_type(object.getInt("hair_type"));
            }
            if (object.has("accessory_type")) {
                res.setAccessory_type(object.getInt("accessory_type"));
            }
            if (object.has("spot_type")) {
                res.setSpot_type(object.getInt("spot_type"));
            }
            if (object.has("glasses_type")) {
                res.setGlasses_type(object.getInt("glasses_type"));
            }
            if (object.has("body_type")) {
                res.setBody_type(object.getInt("body_type"));
            }
            if (object.has("clothes_type")) {
                res.setClothes_type(object.getInt("clothes_type"));
            }
            if (object.has("ear_type")) {
                res.setEar_type(object.getInt("ear_type"));
            }
            if (object.has("eye_position")) {
                res.setEye_position((float) object.getDouble("eye_position"));
            }
            if (object.has("brow_position")) {
                res.setBrow_position((float) object.getDouble("brow_position"));
            }
            if (object.has("mouth_position")) {
                res.setMouth_position((float) object.getDouble("mouth_position"));
            }
            if (object.has("skin_color")) {
                res.setSkin_color(object.getInt("skin_color"));
            }
            if (object.has("hair_color")) {
                res.setHair_color(object.getInt("hair_color"));
            }
            if (object.has("eye_color")) {
                res.setEye_color(object.getInt("eye_color"));
            }
			if (object.has("flirtable")) {
				res.setFlirtable(object.getInt("flirtable"));
			}
            if (object.has("possession")) {
                res.setPossession(object.getInt("possession"));
            }
            if (object.has("consumption")) {
                res.setConsumption(object.getInt("consumption"));
            }
            if (object.has("recognition")) {
                res.setRecognition(object.getInt("recognition"));
            }
            if (object.has("sexual")) {
                res.setSexual(object.getInt("sexual"));
            }
            return res;
        } catch (JSONException e) {
            throw new BarcodeKanojoParseException(e.toString());
        }
    }
}
