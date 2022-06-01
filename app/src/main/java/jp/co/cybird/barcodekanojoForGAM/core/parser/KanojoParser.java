package jp.co.cybird.barcodekanojoForGAM.core.parser;

import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoException;
import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import com.goujer.barcodekanojo.core.model.Kanojo;

import org.json.JSONException;
import org.json.JSONObject;

public class KanojoParser extends AbstractJSONParser<Kanojo> {
    protected Kanojo parseInner(JSONObject object) throws BarcodeKanojoException, BarcodeKanojoParseException {
        Kanojo res = new Kanojo();
        try {
            if (object.has("id")) {
                res.setId(object.getInt("id"));
            }
            if (object.has("name")) {
                res.setName(object.getString("name"));
            }
            if (object.has("barcode")) {
                res.setBarcode(object.getString("barcode"));
            }
            if (object.has("geo")) {
                res.setGeo(object.getString("geo"));
            }
            if (object.has("location")) {
                res.setLocation(object.getString("location"));
            }
            if (object.has("birth_year")) {
                res.setBirth_year(object.getInt("birth_year"));
            }
            if (object.has("birth_month")) {
                res.setBirth_month(object.getInt("birth_month"));
            }
            if (object.has("birth_day")) {
                res.setBirth_day(object.getInt("birth_day"));
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
			if (object.has("flirtable")) {
				res.setFlirtable(object.getInt("flirtable"));
			}
            if (object.has("love_gauge")) {
                res.setLove_gauge(object.getInt("love_gauge"));
            }
            if (object.has("follower_count")) {
                res.setFollower_count(object.getInt("follower_count"));
            }
            if (object.has("source")) {
                res.setSource(object.getString("source"));
            }
            if (object.has("nationality")) {
                res.setNationality(object.getString("nationality"));
            }
            if (object.has("relation_status")) {
                res.setRelation_status(object.getInt("relation_status"));
            }
            if (object.has("voted_like")) {
                res.setVoted_like(object.getBoolean("voted_like"));
            }
            if (object.has("in_room")) {
                res.setIn_room(object.getBoolean("in_room"));
            }
            if (object.has("like_rate")) {
                res.setLike_rate(object.getInt("like_rate"));
            }
            if (object.has("status")) {
                res.setStatus(object.getString("status"));
            }
            if (object.has("avatar_background_image_url")) {
                res.setAvatar_background_image_url(object.getString("avatar_background_image_url"));
            }
            if (object.has("on_advertising") && !object.isNull("on_advertising")) {
                res.setOn_advertising(object.getBoolean("on_advertising"));
            }
            if (object.has("advertising_banner_url")) {
                res.setAdvertising_banner_url(object.getString("advertising_banner_url"));
            }
            if (object.has("emotion_status")) {
                res.setEmotion_status(object.getInt("emotion_status"));
            }
            if (object.has("mascot_enabled")) {
                res.setMascotEnable(object.getInt("mascot_enabled"));
            }
            return res;
        } catch (JSONException e) {
            throw new BarcodeKanojoParseException(e.toString());
        }
    }
}
