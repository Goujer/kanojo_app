package jp.co.cybird.barcodekanojoForGAM.core.model;

import java.util.HashMap;

public class MessageModel extends HashMap<String, String> implements BarcodeKanojoModel {
    public static final String DO_ADD_FRIEND = "do_add_friend";
    public static final String DO_GENERATE_KANOJO = "do_generate_kanojo";
    public static final String INFORM_FRIEND = "inform_friend";
    public static final String INFORM_GIRLFRIEND = "inform_girlfriend";
    public static final String NOTIFY_AMENDMENT_INFORMATION = "notify_amendment_information";
    public static final String TAG = "MessageModel";
    private static final long serialVersionUID = 4819493080923777193L;
}
