package twitter4j;

import java.io.Serializable;
import twitter4j.internal.json.z_T4JInternalParseUtil;
import twitter4j.internal.org.json.JSONObject;

class StatusDeletionNoticeImpl implements StatusDeletionNotice, Serializable {
    private static final long serialVersionUID = 1723338404242596062L;
    private long statusId;
    private long userId;

    StatusDeletionNoticeImpl(JSONObject status) {
        this.statusId = z_T4JInternalParseUtil.getLong("id", status);
        this.userId = z_T4JInternalParseUtil.getLong("user_id", status);
    }

    public long getStatusId() {
        return this.statusId;
    }

    public long getUserId() {
        return this.userId;
    }

    public int compareTo(StatusDeletionNotice that) {
        long delta = this.statusId - that.getStatusId();
        if (delta < -2147483648L) {
            return Integer.MIN_VALUE;
        }
        if (delta > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) delta;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StatusDeletionNoticeImpl that = (StatusDeletionNoticeImpl) o;
        if (this.statusId != that.statusId) {
            return false;
        }
        if (this.userId != that.userId) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((int) (this.statusId ^ (this.statusId >>> 32))) * 31) + ((int) (this.userId ^ (this.userId >>> 32)));
    }

    public String toString() {
        return "StatusDeletionNoticeImpl{statusId=" + this.statusId + ", userId=" + this.userId + '}';
    }
}
