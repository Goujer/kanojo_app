package com.google.ads.searchads;

import android.content.Context;
import android.graphics.Color;
import com.google.ads.AdRequest;
import com.google.ads.mediation.admob.AdMobAdapterExtras;
import java.util.Locale;
import java.util.Map;
import jp.co.cybird.app.android.lib.applauncher.AppLauncherConsts;

public class SearchAdRequest extends AdRequest {
    private String a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;
    private int g;
    private String h;
    private int i;
    private int j;
    private BorderType k;
    private int l;
    private String m;

    public enum BorderType {
        NONE("none"),
        DASHED("dashed"),
        DOTTED("dotted"),
        SOLID("solid");
        
        private String a;

        private BorderType(String param) {
            this.a = param;
        }

        public String toString() {
            return this.a;
        }
    }

    public void setQuery(String query) {
        this.a = query;
    }

    public void setBackgroundColor(int backgroundColor) {
        if (Color.alpha(backgroundColor) == 255) {
            this.b = backgroundColor;
            this.c = 0;
            this.d = 0;
        }
    }

    public void setBackgroundGradient(int from, int to) {
        if (Color.alpha(from) == 255 && Color.alpha(to) == 255) {
            this.b = Color.argb(0, 0, 0, 0);
            this.c = from;
            this.d = to;
        }
    }

    public void setHeaderTextColor(int headerTextColor) {
        this.e = headerTextColor;
    }

    public void setDescriptionTextColor(int descriptionTextColor) {
        this.f = descriptionTextColor;
    }

    public void setAnchorTextColor(int anchorTextColor) {
        this.g = anchorTextColor;
    }

    public void setFontFace(String fontFace) {
        this.h = fontFace;
    }

    public void setHeaderTextSize(int headerTextSize) {
        this.i = headerTextSize;
    }

    public void setBorderColor(int borderColor) {
        this.j = borderColor;
    }

    public void setBorderType(BorderType borderType) {
        this.k = borderType;
    }

    public void setBorderThickness(int borderThickness) {
        this.l = borderThickness;
    }

    public void setCustomChannels(String channelIds) {
        this.m = channelIds;
    }

    public Map<String, Object> getRequestMap(Context context) {
        AdMobAdapterExtras adMobAdapterExtras = (AdMobAdapterExtras) getNetworkExtras(AdMobAdapterExtras.class);
        if (adMobAdapterExtras == null) {
            adMobAdapterExtras = new AdMobAdapterExtras();
            setNetworkExtras(adMobAdapterExtras);
        }
        if (this.a != null) {
            adMobAdapterExtras.getExtras().put(AppLauncherConsts.REQUEST_PARAM_GENERAL, this.a);
        }
        if (Color.alpha(this.b) != 0) {
            adMobAdapterExtras.getExtras().put("bgcolor", a(this.b));
        }
        if (Color.alpha(this.c) == 255 && Color.alpha(this.d) == 255) {
            adMobAdapterExtras.getExtras().put("gradientfrom", a(this.c));
            adMobAdapterExtras.getExtras().put("gradientto", a(this.d));
        }
        if (Color.alpha(this.e) != 0) {
            adMobAdapterExtras.getExtras().put("hcolor", a(this.e));
        }
        if (Color.alpha(this.f) != 0) {
            adMobAdapterExtras.getExtras().put("dcolor", a(this.f));
        }
        if (Color.alpha(this.g) != 0) {
            adMobAdapterExtras.getExtras().put("acolor", a(this.g));
        }
        if (this.h != null) {
            adMobAdapterExtras.getExtras().put("font", this.h);
        }
        adMobAdapterExtras.getExtras().put("headersize", Integer.toString(this.i));
        if (Color.alpha(this.j) != 0) {
            adMobAdapterExtras.getExtras().put("bcolor", a(this.j));
        }
        if (this.k != null) {
            adMobAdapterExtras.getExtras().put("btype", this.k.toString());
        }
        adMobAdapterExtras.getExtras().put("bthick", Integer.toString(this.l));
        if (this.m != null) {
            adMobAdapterExtras.getExtras().put("channel", this.m);
        }
        return super.getRequestMap(context);
    }

    private String a(int i2) {
        return String.format(Locale.US, "#%06x", new Object[]{Integer.valueOf(16777215 & i2)});
    }
}
