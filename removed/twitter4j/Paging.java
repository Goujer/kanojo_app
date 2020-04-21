package twitter4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import twitter4j.internal.http.HttpParameter;

public final class Paging implements Serializable {
    static final String COUNT = "count";
    private static HttpParameter[] NULL_PARAMETER_ARRAY = new HttpParameter[0];
    private static List<HttpParameter> NULL_PARAMETER_LIST = new ArrayList(0);
    static final String PER_PAGE = "per_page";
    static char[] S = {'s'};
    static char[] SMCP = {'s', 'm', 'c', 'p'};
    private static final long serialVersionUID = -3285857427993796670L;
    private int count;
    private long maxId;
    private int page;
    private long sinceId;

    /* access modifiers changed from: package-private */
    public List<HttpParameter> asPostParameterList() {
        return asPostParameterList(SMCP, COUNT);
    }

    /* access modifiers changed from: package-private */
    public HttpParameter[] asPostParameterArray() {
        List<HttpParameter> list = asPostParameterList(SMCP, COUNT);
        if (list.size() == 0) {
            return NULL_PARAMETER_ARRAY;
        }
        return (HttpParameter[]) list.toArray(new HttpParameter[list.size()]);
    }

    /* access modifiers changed from: package-private */
    public List<HttpParameter> asPostParameterList(char[] supportedParams) {
        return asPostParameterList(supportedParams, COUNT);
    }

    /* access modifiers changed from: package-private */
    public List<HttpParameter> asPostParameterList(char[] supportedParams, String perPageParamName) {
        List<HttpParameter> pagingParams = new ArrayList<>(supportedParams.length);
        addPostParameter(supportedParams, 's', pagingParams, "since_id", getSinceId());
        addPostParameter(supportedParams, 'm', pagingParams, "max_id", getMaxId());
        addPostParameter(supportedParams, 'c', pagingParams, perPageParamName, (long) getCount());
        addPostParameter(supportedParams, 'p', pagingParams, "page", (long) getPage());
        if (pagingParams.size() == 0) {
            return NULL_PARAMETER_LIST;
        }
        return pagingParams;
    }

    /* access modifiers changed from: package-private */
    public HttpParameter[] asPostParameterArray(char[] supportedParams, String perPageParamName) {
        List<HttpParameter> pagingParams = new ArrayList<>(supportedParams.length);
        addPostParameter(supportedParams, 's', pagingParams, "since_id", getSinceId());
        addPostParameter(supportedParams, 'm', pagingParams, "max_id", getMaxId());
        addPostParameter(supportedParams, 'c', pagingParams, perPageParamName, (long) getCount());
        addPostParameter(supportedParams, 'p', pagingParams, "page", (long) getPage());
        if (pagingParams.size() == 0) {
            return NULL_PARAMETER_ARRAY;
        }
        return (HttpParameter[]) pagingParams.toArray(new HttpParameter[pagingParams.size()]);
    }

    private void addPostParameter(char[] supportedParams, char paramKey, List<HttpParameter> pagingParams, String paramName, long paramValue) {
        boolean supported = false;
        char[] arr$ = supportedParams;
        int len$ = arr$.length;
        int i$ = 0;
        while (true) {
            if (i$ >= len$) {
                break;
            } else if (arr$[i$] == paramKey) {
                supported = true;
                break;
            } else {
                i$++;
            }
        }
        if (!supported && -1 != paramValue) {
            throw new IllegalStateException("Paging parameter [" + paramName + "] is not supported with this operation.");
        } else if (-1 != paramValue) {
            pagingParams.add(new HttpParameter(paramName, String.valueOf(paramValue)));
        }
    }

    public Paging() {
        this.page = -1;
        this.count = -1;
        this.sinceId = -1;
        this.maxId = -1;
    }

    public Paging(int page2) {
        this.page = -1;
        this.count = -1;
        this.sinceId = -1;
        this.maxId = -1;
        setPage(page2);
    }

    public Paging(long sinceId2) {
        this.page = -1;
        this.count = -1;
        this.sinceId = -1;
        this.maxId = -1;
        setSinceId(sinceId2);
    }

    public Paging(int page2, int count2) {
        this(page2);
        setCount(count2);
    }

    public Paging(int page2, long sinceId2) {
        this(page2);
        setSinceId(sinceId2);
    }

    public Paging(int page2, int count2, long sinceId2) {
        this(page2, count2);
        setSinceId(sinceId2);
    }

    public Paging(int page2, int count2, long sinceId2, long maxId2) {
        this(page2, count2, sinceId2);
        setMaxId(maxId2);
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page2) {
        if (page2 < 1) {
            throw new IllegalArgumentException("page should be positive integer. passed:" + page2);
        }
        this.page = page2;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count2) {
        if (count2 < 1) {
            throw new IllegalArgumentException("count should be positive integer. passed:" + count2);
        }
        this.count = count2;
    }

    public Paging count(int count2) {
        setCount(count2);
        return this;
    }

    public long getSinceId() {
        return this.sinceId;
    }

    public void setSinceId(long sinceId2) {
        if (sinceId2 < 1) {
            throw new IllegalArgumentException("since_id should be positive integer. passed:" + sinceId2);
        }
        this.sinceId = sinceId2;
    }

    public Paging sinceId(long sinceId2) {
        setSinceId(sinceId2);
        return this;
    }

    public long getMaxId() {
        return this.maxId;
    }

    public void setMaxId(long maxId2) {
        if (maxId2 < 1) {
            throw new IllegalArgumentException("max_id should be positive integer. passed:" + maxId2);
        }
        this.maxId = maxId2;
    }

    public Paging maxId(long maxId2) {
        setMaxId(maxId2);
        return this;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paging)) {
            return false;
        }
        Paging paging = (Paging) o;
        if (this.count != paging.count) {
            return false;
        }
        if (this.maxId != paging.maxId) {
            return false;
        }
        if (this.page != paging.page) {
            return false;
        }
        if (this.sinceId != paging.sinceId) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((((this.page * 31) + this.count) * 31) + ((int) (this.sinceId ^ (this.sinceId >>> 32)))) * 31) + ((int) (this.maxId ^ (this.maxId >>> 32)));
    }

    public String toString() {
        return "Paging{page=" + this.page + ", count=" + this.count + ", sinceId=" + this.sinceId + ", maxId=" + this.maxId + '}';
    }
}
