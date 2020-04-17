package net.nend.android;

final class NendConstants {
    static final boolean IS_DEBUG_CODE = false;
    static final String NEND_UID_FILE_NAME = "NENDUUID";
    static final String VERSION = "2.2.2";

    static class AdDefaultParams {
        static final int HEIGHT = 50;
        static final int MAX_AD_RELOAD_INTERVAL_IN_SECONDS = 99999;
        static final int MIN_AD_RELOAD_INTERVAL_IN_SECONDS = 30;
        static final int RELOAD_INTERVAL_IN_SECONDS = 60;
        static final int WIDTH = 320;

        private AdDefaultParams() {
        }
    }

    static class NendHttpParams {
        static final int CONNECTION_TIMEOUT_IN_SECOND = 10;
        static final int SOCKET_TIMEOUT_IN_SECOND = 10;

        private NendHttpParams() {
        }
    }

    static final class RequestParams {
        static final String DOMAIN = "ad1.nend.net";
        static final String PATH = "na.php";
        static final String PROTOCOL = "http";

        private RequestParams() {
        }
    }

    static final class OptOutParams {
        static final String IMAGE_URL = "http://img1.nend.net/img/common/optout/icon.png";
        static final String PAGE_URL = "http://nend.net/privacy/optsdkgate";

        private OptOutParams() {
        }
    }

    enum MetaData {
        ADSCHEME("NendAdScheme"),
        ADAUTHORITY("NendAdAuthority"),
        ADPATH("NendAdPath"),
        OPT_OUT_URL("NendOptOutUrl"),
        OPT_OUT_IMAGE_URL("NendOptOutImageUrl");
        
        private String name;

        private MetaData(String name2) {
            this.name = name2;
        }

        /* access modifiers changed from: package-private */
        public String getName() {
            return this.name;
        }
    }

    enum Attribute {
        SPOT_ID("NendSpotId"),
        API_KEY("NendApiKey"),
        RELOADABLE("NendReloadable");
        
        private String name;

        private Attribute(String name2) {
            this.name = name2;
        }

        /* access modifiers changed from: package-private */
        public String getName() {
            return this.name;
        }
    }

    private NendConstants() {
    }
}
