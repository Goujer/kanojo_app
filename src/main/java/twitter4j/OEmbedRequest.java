package twitter4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import twitter4j.internal.http.HttpParameter;
import twitter4j.internal.util.z_T4JInternalStringUtil;

public final class OEmbedRequest implements Serializable {
    private static final long serialVersionUID = -4330607167106242987L;
    private Align align = Align.NONE;
    private boolean hideMedia = true;
    private boolean hideThread = true;
    private String lang;
    private int maxWidth;
    private boolean omitScript = false;
    private String[] related = new String[0];
    private final long statusId;
    private final String url;

    public enum Align {
        LEFT,
        CENTER,
        RIGHT,
        NONE
    }

    OEmbedRequest(long statusId2, String url2) {
        this.statusId = statusId2;
        this.url = url2;
    }

    public void setMaxWidth(int maxWidth2) {
        this.maxWidth = maxWidth2;
    }

    public OEmbedRequest MaxWidth(int maxWidth2) {
        this.maxWidth = maxWidth2;
        return this;
    }

    public void setHideMedia(boolean hideMedia2) {
        this.hideMedia = hideMedia2;
    }

    public OEmbedRequest HideMedia(boolean hideMedia2) {
        this.hideMedia = hideMedia2;
        return this;
    }

    public void setHideThread(boolean hideThread2) {
        this.hideThread = hideThread2;
    }

    public OEmbedRequest HideThread(boolean hideThread2) {
        this.hideThread = hideThread2;
        return this;
    }

    public void setOmitScript(boolean omitScript2) {
        this.omitScript = omitScript2;
    }

    public OEmbedRequest omitScript(boolean omitScript2) {
        this.omitScript = omitScript2;
        return this;
    }

    public void setAlign(Align align2) {
        this.align = align2;
    }

    public OEmbedRequest align(Align align2) {
        this.align = align2;
        return this;
    }

    public void setRelated(String[] related2) {
        this.related = related2;
    }

    public OEmbedRequest related(String[] related2) {
        this.related = related2;
        return this;
    }

    public void setLang(String lang2) {
        this.lang = lang2;
    }

    public OEmbedRequest lang(String lang2) {
        this.lang = lang2;
        return this;
    }

    /* access modifiers changed from: package-private */
    public HttpParameter[] asHttpParameterArray() {
        ArrayList<HttpParameter> params = new ArrayList<>(12);
        appendParameter("id", this.statusId, (List<HttpParameter>) params);
        appendParameter("url", this.url, (List<HttpParameter>) params);
        appendParameter("maxwidth", (long) this.maxWidth, (List<HttpParameter>) params);
        params.add(new HttpParameter("hide_media", this.hideMedia));
        params.add(new HttpParameter("hide_thread", this.hideThread));
        params.add(new HttpParameter("omit_script", this.omitScript));
        params.add(new HttpParameter("align", this.align.name().toLowerCase()));
        if (this.related.length > 0) {
            appendParameter("related", z_T4JInternalStringUtil.join(this.related), (List<HttpParameter>) params);
        }
        appendParameter("lang", this.lang, (List<HttpParameter>) params);
        return (HttpParameter[]) params.toArray(new HttpParameter[params.size()]);
    }

    private void appendParameter(String name, String value, List<HttpParameter> params) {
        if (value != null) {
            params.add(new HttpParameter(name, value));
        }
    }

    private void appendParameter(String name, long value, List<HttpParameter> params) {
        if (0 <= value) {
            params.add(new HttpParameter(name, String.valueOf(value)));
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OEmbedRequest that = (OEmbedRequest) o;
        if (this.hideMedia != that.hideMedia) {
            return false;
        }
        if (this.hideThread != that.hideThread) {
            return false;
        }
        if (this.maxWidth != that.maxWidth) {
            return false;
        }
        if (this.omitScript != that.omitScript) {
            return false;
        }
        if (this.statusId != that.statusId) {
            return false;
        }
        if (this.align != that.align) {
            return false;
        }
        if (this.lang == null ? that.lang != null : !this.lang.equals(that.lang)) {
            return false;
        }
        if (!Arrays.equals(this.related, that.related)) {
            return false;
        }
        if (this.url != null) {
            if (this.url.equals(that.url)) {
                return true;
            }
        } else if (that.url == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5 = 1;
        int i6 = 0;
        int hashCode = ((((((int) (this.statusId ^ (this.statusId >>> 32))) * 31) + (this.url != null ? this.url.hashCode() : 0)) * 31) + this.maxWidth) * 31;
        if (this.hideMedia) {
            i = 1;
        } else {
            i = 0;
        }
        int i7 = (hashCode + i) * 31;
        if (this.hideThread) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        int i8 = (i7 + i2) * 31;
        if (!this.omitScript) {
            i5 = 0;
        }
        int i9 = (i8 + i5) * 31;
        if (this.align != null) {
            i3 = this.align.hashCode();
        } else {
            i3 = 0;
        }
        int i10 = (i9 + i3) * 31;
        if (this.related != null) {
            i4 = Arrays.hashCode(this.related);
        } else {
            i4 = 0;
        }
        int i11 = (i10 + i4) * 31;
        if (this.lang != null) {
            i6 = this.lang.hashCode();
        }
        return i11 + i6;
    }

    public String toString() {
        return "OEmbedRequest{statusId=" + this.statusId + ", url='" + this.url + '\'' + ", maxWidth=" + this.maxWidth + ", hideMedia=" + this.hideMedia + ", hideThread=" + this.hideThread + ", omitScript=" + this.omitScript + ", align=" + this.align + ", related=" + (this.related == null ? null : Arrays.asList(this.related)) + ", lang='" + this.lang + '\'' + '}';
    }
}
