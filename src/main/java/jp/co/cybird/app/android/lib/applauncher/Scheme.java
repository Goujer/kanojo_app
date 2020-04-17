package jp.co.cybird.app.android.lib.applauncher;

public class Scheme {
    private String description;
    private String icon_url;
    private String priority;
    private String product_id;
    private String scheme;
    private String title;
    private String url;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getIconUrl() {
        return this.icon_url;
    }

    public void setIcon_url(String icon_url2) {
        this.icon_url = icon_url2;
    }

    public String getProduct_id() {
        return this.product_id;
    }

    public void setProduct_id(String product_id2) {
        this.product_id = product_id2;
    }

    public String getPriority() {
        return this.priority;
    }

    public void setPriority(String priority2) {
        this.priority = priority2;
    }

    public String getScheme() {
        return this.scheme;
    }

    public void setScheme(String scheme2) {
        this.scheme = scheme2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url2) {
        this.url = url2;
    }

    public String toString() {
        return "[Scheme]" + "description=" + getDescription() + ",icon_url=" + getIconUrl() + ",packagename=" + getProduct_id() + ",priority=" + getPriority() + ",scheme=" + getScheme() + ",title=" + getTitle() + ",url=" + getUrl();
    }
}
