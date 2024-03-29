package jp.co.cybird.barcodekanojoForGAM.core.parser;

import jp.co.cybird.barcodekanojoForGAM.core.exception.BarcodeKanojoParseException;
import com.goujer.barcodekanojo.core.model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class UserParser extends AbstractJSONParser<User> {
    protected User parseInner(JSONObject object) throws BarcodeKanojoParseException {
        User res = new User();
        try {
            if (object.has("id")) {
                res.setId(object.getInt("id"));
            }
            if (object.has("name")) {
                res.setName(object.getString("name"));
            }
            if (object.has("language")) {
                res.setLanguage(object.getString("language"));
            }
            if (object.has("level")) {
                res.setLevel(object.getInt("level"));
            }
            if (object.has("stamina")) {
                res.setStamina(object.getInt("stamina"));
            }
            if (object.has("stamina_max")) {
                res.setStamina_max(object.getInt("stamina_max"));
            }
            if (object.has("money")) {
                res.setMoney(object.getInt("money"));
            }
            if (object.has("kanojo_count")) {
                res.setKanojo_count(object.getInt("kanojo_count"));
            }
            if (object.has("generate_count")) {
                res.setGenerate_count(object.getInt("generate_count"));
            }
            if (object.has("scan_count")) {
                res.setScan_count(object.getInt("scan_count"));
            }
            if (object.has("enemy_count")) {
                res.setEnemy_count(object.getInt("enemy_count"));
            }
            if (object.has("friend_count")) {
                res.setWish_count(object.getInt("friend_count"));
            }
            if (object.has("birth_month")) {
                res.setBirth_month(object.getInt("birth_month"));
            }
            if (object.has("birth_day")) {
                res.setBirth_day(object.getInt("birth_day"));
            }
            if (object.has("relation_status")) {
                res.setRelation_status(object.getInt("relation_status"));
            }
            if (object.has("tickets") && !object.getString("tickets").equals("null")) {
                res.setTickets(object.getInt("tickets"));
            }
            if (object.has("birth_year")) {
                res.setBirth_year(object.getInt("birth_year"));
            }
            //if (object.has("email") && !object.getString("email").equals("null")) {
            //    res.setEmail(object.getString("email"));
            //}
            //if (object.has("password") && !object.getString("password").equals("null")) {
            //	String salt = "";
			//	if (object.has("salt") && !object.getString("salt").equals("null")) {
			//		salt = object.getString("salt");
			//	}
            //    res.setCurrentPassword(Password.Companion.saveHashedPassword(object.getString("password"), salt));
            //}
            return res;
        } catch (JSONException e) {
            throw new BarcodeKanojoParseException(e.toString());
        }
    }
}
