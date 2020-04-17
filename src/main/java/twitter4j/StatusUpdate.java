package twitter4j;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jp.co.cybird.barcodekanojoForGAM.gree.core.model.Inspection;
import twitter4j.internal.http.HttpParameter;

public final class StatusUpdate implements Serializable {
    private static final long serialVersionUID = -3595502688477609916L;
    private boolean displayCoordinates = true;
    private long inReplyToStatusId = -1;
    private GeoLocation location = null;
    private transient InputStream mediaBody;
    private File mediaFile;
    private String mediaName;
    private String placeId = null;
    private boolean possiblySensitive;
    private String status;

    public StatusUpdate(String status2) {
        this.status = status2;
    }

    public String getStatus() {
        return this.status;
    }

    public long getInReplyToStatusId() {
        return this.inReplyToStatusId;
    }

    public void setInReplyToStatusId(long inReplyToStatusId2) {
        this.inReplyToStatusId = inReplyToStatusId2;
    }

    public StatusUpdate inReplyToStatusId(long inReplyToStatusId2) {
        setInReplyToStatusId(inReplyToStatusId2);
        return this;
    }

    public GeoLocation getLocation() {
        return this.location;
    }

    public void setLocation(GeoLocation location2) {
        this.location = location2;
    }

    public StatusUpdate location(GeoLocation location2) {
        setLocation(location2);
        return this;
    }

    public String getPlaceId() {
        return this.placeId;
    }

    public void setPlaceId(String placeId2) {
        this.placeId = placeId2;
    }

    public StatusUpdate placeId(String placeId2) {
        setPlaceId(placeId2);
        return this;
    }

    public boolean isDisplayCoordinates() {
        return this.displayCoordinates;
    }

    public void setDisplayCoordinates(boolean displayCoordinates2) {
        this.displayCoordinates = displayCoordinates2;
    }

    public StatusUpdate displayCoordinates(boolean displayCoordinates2) {
        setDisplayCoordinates(displayCoordinates2);
        return this;
    }

    public void setMedia(File file) {
        this.mediaFile = file;
    }

    public StatusUpdate media(File file) {
        setMedia(file);
        return this;
    }

    public void setMedia(String name, InputStream body) {
        this.mediaName = name;
        this.mediaBody = body;
    }

    /* access modifiers changed from: package-private */
    public boolean isWithMedia() {
        return (this.mediaFile == null && this.mediaName == null) ? false : true;
    }

    public StatusUpdate media(String name, InputStream body) {
        setMedia(name, body);
        return this;
    }

    public void setPossiblySensitive(boolean possiblySensitive2) {
        this.possiblySensitive = possiblySensitive2;
    }

    public StatusUpdate possiblySensitive(boolean possiblySensitive2) {
        setPossiblySensitive(possiblySensitive2);
        return this;
    }

    public boolean isPossiblySensitive() {
        return this.possiblySensitive;
    }

    /* access modifiers changed from: package-private */
    public HttpParameter[] asHttpParameterArray() {
        ArrayList<HttpParameter> params = new ArrayList<>();
        appendParameter(Inspection.STATUS, this.status, (List<HttpParameter>) params);
        if (-1 != this.inReplyToStatusId) {
            appendParameter("in_reply_to_status_id", this.inReplyToStatusId, (List<HttpParameter>) params);
        }
        if (this.location != null) {
            appendParameter("lat", this.location.getLatitude(), (List<HttpParameter>) params);
            appendParameter("long", this.location.getLongitude(), (List<HttpParameter>) params);
        }
        appendParameter("place_id", this.placeId, (List<HttpParameter>) params);
        if (!this.displayCoordinates) {
            appendParameter("display_coordinates", "false", (List<HttpParameter>) params);
        }
        if (this.mediaFile != null) {
            params.add(new HttpParameter("media[]", this.mediaFile));
            params.add(new HttpParameter("possibly_sensitive", this.possiblySensitive));
        } else if (!(this.mediaName == null || this.mediaBody == null)) {
            params.add(new HttpParameter("media[]", this.mediaName, this.mediaBody));
            params.add(new HttpParameter("possibly_sensitive", this.possiblySensitive));
        }
        return (HttpParameter[]) params.toArray(new HttpParameter[params.size()]);
    }

    private void appendParameter(String name, String value, List<HttpParameter> params) {
        if (value != null) {
            params.add(new HttpParameter(name, value));
        }
    }

    private void appendParameter(String name, double value, List<HttpParameter> params) {
        params.add(new HttpParameter(name, String.valueOf(value)));
    }

    private void appendParameter(String name, long value, List<HttpParameter> params) {
        params.add(new HttpParameter(name, String.valueOf(value)));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StatusUpdate that = (StatusUpdate) o;
        if (this.displayCoordinates != that.displayCoordinates) {
            return false;
        }
        if (this.inReplyToStatusId != that.inReplyToStatusId) {
            return false;
        }
        if (this.possiblySensitive != that.possiblySensitive) {
            return false;
        }
        if (this.location == null ? that.location != null : !this.location.equals(that.location)) {
            return false;
        }
        if (this.mediaBody == null ? that.mediaBody != null : !this.mediaBody.equals(that.mediaBody)) {
            return false;
        }
        if (this.mediaFile == null ? that.mediaFile != null : !this.mediaFile.equals(that.mediaFile)) {
            return false;
        }
        if (this.mediaName == null ? that.mediaName != null : !this.mediaName.equals(that.mediaName)) {
            return false;
        }
        if (this.placeId == null ? that.placeId != null : !this.placeId.equals(that.placeId)) {
            return false;
        }
        if (this.status != null) {
            if (this.status.equals(that.status)) {
                return true;
            }
        } else if (that.status == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6 = 1;
        int i7 = 0;
        if (this.status != null) {
            result = this.status.hashCode();
        } else {
            result = 0;
        }
        int i8 = ((result * 31) + ((int) (this.inReplyToStatusId ^ (this.inReplyToStatusId >>> 32)))) * 31;
        if (this.location != null) {
            i = this.location.hashCode();
        } else {
            i = 0;
        }
        int i9 = (i8 + i) * 31;
        if (this.placeId != null) {
            i2 = this.placeId.hashCode();
        } else {
            i2 = 0;
        }
        int i10 = (i9 + i2) * 31;
        if (this.displayCoordinates) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        int i11 = (i10 + i3) * 31;
        if (!this.possiblySensitive) {
            i6 = 0;
        }
        int i12 = (i11 + i6) * 31;
        if (this.mediaName != null) {
            i4 = this.mediaName.hashCode();
        } else {
            i4 = 0;
        }
        int i13 = (i12 + i4) * 31;
        if (this.mediaBody != null) {
            i5 = this.mediaBody.hashCode();
        } else {
            i5 = 0;
        }
        int i14 = (i13 + i5) * 31;
        if (this.mediaFile != null) {
            i7 = this.mediaFile.hashCode();
        }
        return i14 + i7;
    }

    public String toString() {
        return "StatusUpdate{status='" + this.status + '\'' + ", inReplyToStatusId=" + this.inReplyToStatusId + ", location=" + this.location + ", placeId='" + this.placeId + '\'' + ", displayCoordinates=" + this.displayCoordinates + ", possiblySensitive=" + this.possiblySensitive + ", mediaName='" + this.mediaName + '\'' + ", mediaBody=" + this.mediaBody + ", mediaFile=" + this.mediaFile + '}';
    }
}
