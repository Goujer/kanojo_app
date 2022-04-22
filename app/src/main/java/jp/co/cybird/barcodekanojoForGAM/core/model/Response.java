package jp.co.cybird.barcodekanojoForGAM.core.model;

import android.util.Log;

import com.goujer.barcodekanojo.core.model.User;

import java.util.Collection;

import jp.co.cybird.barcodekanojoForGAM.core.util.GeoUtil;

public class Response<T extends BarcodeKanojoModel> extends ModelList<T> implements BarcodeKanojoModel {
	public static final int CODE_SUCCESS = 200;
	public static final int CODE_ERROR_NOT_ENOUGH_TICKET = 202;
	public static final int CODE_ERROR_BAD_REQUEST = 400;
	public static final int CODE_ERROR_UNAUTHORIZED = 401;
    public static final int CODE_ERROR_FORBIDDEN = 403;
	public static final int CODE_ERROR_NOT_FOUND = 404;
	public static final int CODE_ERROR_SERVER = 500;
    public static final int CODE_ERROR_NETWORK = 502;
    public static final int CODE_ERROR_SERVICE_UNAVAILABLE = 503;
    public static final int CODE_FINISHED_CONSUME_TICKET = 600;
    public static final String TAG = "Response";
    private static final long serialVersionUID = 1;
    private int code;
    private String message;

    public Response() {
    }

    public Response(Collection<T> collection) {
        super(collection);
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code2) {
        this.code = code2;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }

    public Alert getAlert() {
        ModelList<Alert> list = getAlertList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    private ModelList<Alert> getAlertList() {
        ModelList<?> list = (ModelList) get(ModelList.class);
        if (list == null) {
            return null;
        }
        ModelList<Alert> alertList = new ModelList<>();
		for (BarcodeKanojoModel item : list) {
			if (item instanceof Alert) {
				alertList.add((Alert) item);
			}
		}
        return alertList;
    }

    public ModelList<Kanojo> getKanojoList() {
        ModelList<?> list = (ModelList) get(ModelList.class);
        if (list == null) {
            return null;
        }
        ModelList<Kanojo> kanojoList = new ModelList<>();
		for (BarcodeKanojoModel item : list) {
			if (item instanceof Kanojo) {
				kanojoList.add((Kanojo) item);
			}
		}
        return kanojoList;
    }

    public ModelList<ActivityModel> getActivityModelList() {
        ModelList<?> list = (ModelList) get(ModelList.class);
        if (list == null) {
            return null;
        }
        ModelList<ActivityModel> activityList = new ModelList<>();
		for (BarcodeKanojoModel item : list) {
			if (item instanceof ActivityModel) {
				activityList.add((ActivityModel) item);
			}
		}
        return activityList;
    }

    public ModelList<Category> getCategoryModelList() {
        ModelList<?> list = (ModelList) get(ModelList.class);
        if (list == null) {
            return null;
        }
        ModelList<Category> categoryList = new ModelList<>();
		for (BarcodeKanojoModel item : list) {
			if (item instanceof Category) {
				categoryList.add((Category) item);
			}
		}
        return categoryList;
    }

    public ModelList<KanojoItemCategory> getKanojoItemCategoryModelList() {
        boolean isFirstItem = true;
        boolean isOtherFirstItem = true;
        boolean isPortionFirstItem = true;
        ModelList<?> list = (ModelList) get(ModelList.class);
        if (list == null) {
            return null;
        }
        ModelList<KanojoItemCategory> categoryList = new ModelList<>();
		for (BarcodeKanojoModel item : list) {
			if (item instanceof KanojoItemCategory) {
				KanojoItemCategory act = (KanojoItemCategory) item;
				if (act.getTitle().equalsIgnoreCase("Wardrobe") && isFirstItem) {
					act.setTitle(act.getTitle() + "   ");
					isFirstItem = false;
				}
				if (act.getTitle().equalsIgnoreCase("Other") && isOtherFirstItem) {
					act.setTitle(act.getTitle() + "   ");
					isOtherFirstItem = false;
				}
				if (act.getTitle().equalsIgnoreCase("Portion") && isPortionFirstItem) {
					act.setTitle(act.getTitle() + "   ");
					isPortionFirstItem = false;
				}
				categoryList.add(act);
			}
		}
        return categoryList;
    }

    public ModelList<KanojoItemCategory> AddKanojoItemCategoryModelList(Response<?> response) {
        ModelList<?> list = (ModelList) response.get(ModelList.class);
        if (list == null) {
            return null;
        }
        new ModelList();
        ModelList<KanojoItemCategory> mCategoryList = getKanojoItemCategoryModelList();
		for (BarcodeKanojoModel item : list) {
			if (item instanceof KanojoItemCategory) {
				mCategoryList.add((KanojoItemCategory) item);
			}
		}
        return mCategoryList;
    }

    public static void checkResponse(BarcodeKanojoModel item) {
        if (item instanceof Response) {
            Response<?> res = (Response) item;
            Log.v(TAG, "[Response]\n code:" + res.getCode());
            int size = res.size();
            for (int i = 0; i < size; i++) {
                checkResponse(res.get(i));
            }
        }
        if (item instanceof ModelList) {
            ModelList<?> list = (ModelList) item;
            int size2 = list.size();
            Log.v(TAG, "[ModelList]");
            for (int i2 = 0; i2 < size2; i2++) {
                checkResponse(list.get(i2));
            }
        }
        if (item instanceof MessageModel) {
            MessageModel message2 = (MessageModel) item;
            item.toString();
            Log.v(TAG, "[Message]\n " + message2.toString());
            for (String str : message2.values()) {
                Log.v(TAG, "\n " + str);
            }
        }
        if (item instanceof KanojoItemCategory) {
            KanojoItemCategory category = (KanojoItemCategory) item;
            Log.v(TAG, "[KanojoItemCategory]\n title:" + category.getTitle());
            checkResponse(category.getItems());
        }
        if (item instanceof KanojoItem) {
            KanojoItem kanojoItem = (KanojoItem) item;
            Log.v(TAG, "[kanojoItem]\n item_id:" + kanojoItem.getItem_id() + "\n title:" + kanojoItem.getTitle() + "\n description" + kanojoItem.getDescription() + "\n image_thumnail_url:" + kanojoItem.getImage_thumbnail_url());
        } else if (item instanceof Alert) {
            Alert alert = (Alert) item;
            Log.v(TAG, "[Alert]\n title:" + alert.getTitle() + "\n body:" + alert.getBody());
        } else if (item instanceof Barcode) {
            Barcode barcode = (Barcode) item;
            Log.v(TAG, "[Barcode]\n barcode: " + barcode.getBarcode() + "\n eye_color:" + barcode.getEye_color() + "\n regognition:" + barcode.getRecognition());
        } else if (item instanceof User) {
            User user = (User) item;
            Log.v(TAG, "[User]\n id:" + user.getId() + "\n name:" + user.getName() + "\n profile_image_url:" + user.getProfile_image_url() + "\n language:" + user.getLanguage() + "\n level:" + user.getLevel() + "\n stamina:" + user.getStamina() + "\n stamina_max:" + user.getStamina_max() + "\n money:" + user.getMoney() + "\n kanojo_count:" + user.getKanojo_count() + "\n generate_count:" + user.getGenerate_count() + "\n scan_count:" + user.getScan_count() + "\n enemy_count:" + user.getEnemy_count() + "\n wish_count:" + user.getWish_count() + "\n relation_status:" + user.getRelation_status());
        } else if (item instanceof Kanojo) {
            Kanojo kanojo = (Kanojo) item;
            Log.v(TAG, "[Kanojo]\n id:" + kanojo.getId() + "\n name:" + kanojo.getName() + "\n barcode: " + kanojo.getBarcode() + "\n geo:" + GeoUtil.geoToString(kanojo.getGeo()) + "\n location:" + kanojo.getLocation() + "\n birth_year:" + kanojo.getBirth_year() + "\n birth_month:" + kanojo.getBirth_month() + "\n birth_day:" + kanojo.getBirth_day() + "\n profile_image_url:" + kanojo.getProfile_image_icon_url() + "\n love_gauge:" + kanojo.getLove_gauge() + "\n follower_count:" + kanojo.getFollower_count() + "\n source:" + kanojo.getSource() + "\n nationality:" + kanojo.getNationality() + "\n relation_status:" + kanojo.getRelation_status() + "\n like_rate:" + kanojo.getLike_rate() + "\n clothes_type:" + kanojo.getClothes_type() + "\n eye_position:" + kanojo.getEye_position() + "\n is_in_room:" + kanojo.isIn_room() + "\n avatar_background_image_url:" + kanojo.getAvatar_background_image_url());
        } else if (item instanceof Product) {
            Product product = (Product) item;
            Log.v(TAG, "[Product]\n barcode: " + product.getBarcode() + "\n name:" + product.getName() + "\n category_id:" + product.getCategory_id() + "\n category:" + product.getCategory() + "\n commment:" + product.getComment() + "\n geo:" + GeoUtil.geoToString(product.getGeo()) + "\n location:" + product.getLocation() + "\n product_image_url:" + product.getProduct_image_url() + "\n scan_count:" + product.getScan_count() + "\n company_name:" + product.getCompany_name() + "\n country:" + product.getCountry());
        } else if (item instanceof ScanHistory) {
            ScanHistory scanHistory = (ScanHistory) item;
            Log.v(TAG, "[ScanHistory]\n barcode: " + scanHistory.getBarcode() + "\n total_count:" + scanHistory.getTotal_count() + "\n kanojo_count:" + scanHistory.getKanojo_count() + "\n friend_count:" + scanHistory.getFriend_count());
        } else {
            if (item instanceof SearchResult) {
                Log.v(TAG, "[SearchResult]\n hit_count" + ((SearchResult) item).getHit_count());
            }
            if (item instanceof ActivityModel) {
                ActivityModel act = (ActivityModel) item;
                Log.v(TAG, "[Activity]\n id:" + act.getId() + "\n activity_type: " + act.getActivity_type() + "\n created_timestamp:" + act.getCreated_timestamp() + "\n activity:" + act.getActivity());
                User user2 = act.getUser();
                if (user2 != null) {
                    Log.v(TAG, "[Activity User]");
                    checkResponse(user2);
                }
                User otheruser = act.getOther_user();
                if (otheruser != null) {
                    Log.v(TAG, "[Activity Other User]");
                    checkResponse(otheruser);
                }
                Kanojo kanojo2 = act.getKanojo();
                if (kanojo2 != null) {
                    Log.v(TAG, "[Activity Kanojo]");
                    checkResponse(kanojo2);
                }
                Product product2 = act.getProduct();
                if (product2 != null) {
                    Log.v(TAG, "[Activity Product]");
                    checkResponse(product2);
                }
            } else if (item instanceof Category) {
                Category category2 = (Category) item;
                Log.v(TAG, "[Category]\n id:" + category2.getId() + "\n name:" + category2.getName());
            }
        }
    }
}
