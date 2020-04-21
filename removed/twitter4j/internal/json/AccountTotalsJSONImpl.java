package twitter4j.internal.json;

import java.io.Serializable;
import twitter4j.AccountTotals;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.internal.http.HttpResponse;
import twitter4j.internal.org.json.JSONObject;

class AccountTotalsJSONImpl extends TwitterResponseImpl implements AccountTotals, Serializable {
    private static final long serialVersionUID = -2291419345865627123L;
    private final int favorites;
    private final int followers;
    private final int friends;
    private final int updates;

    private AccountTotalsJSONImpl(HttpResponse res, JSONObject json) {
        super(res);
        this.updates = z_T4JInternalParseUtil.getInt("updates", json);
        this.followers = z_T4JInternalParseUtil.getInt("followers", json);
        this.favorites = z_T4JInternalParseUtil.getInt("favorites", json);
        this.friends = z_T4JInternalParseUtil.getInt("friends", json);
    }

    AccountTotalsJSONImpl(HttpResponse res, Configuration conf) throws TwitterException {
        this(res, res.asJSONObject());
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, res.asJSONObject());
        }
    }

    AccountTotalsJSONImpl(JSONObject json) throws TwitterException {
        this((HttpResponse) null, json);
    }

    public int getUpdates() {
        return this.updates;
    }

    public int getFollowers() {
        return this.followers;
    }

    public int getFavorites() {
        return this.favorites;
    }

    public int getFriends() {
        return this.friends;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountTotalsJSONImpl that = (AccountTotalsJSONImpl) o;
        if (this.favorites != that.favorites) {
            return false;
        }
        if (this.followers != that.followers) {
            return false;
        }
        if (this.friends != that.friends) {
            return false;
        }
        if (this.updates != that.updates) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((((this.updates * 31) + this.followers) * 31) + this.favorites) * 31) + this.friends;
    }

    public String toString() {
        return "AccountTotalsJSONImpl{updates=" + this.updates + ", followers=" + this.followers + ", favorites=" + this.favorites + ", friends=" + this.friends + '}';
    }
}
