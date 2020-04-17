package org.jsoup.nodes;

import java.util.Map;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;

public class Attribute implements Map.Entry<String, String>, Cloneable {
    private String key;
    private String value;

    public Attribute(String key2, String value2) {
        Validate.notEmpty(key2);
        Validate.notNull(value2);
        this.key = key2.trim().toLowerCase();
        this.value = value2;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key2) {
        Validate.notEmpty(key2);
        this.key = key2.trim().toLowerCase();
    }

    public String getValue() {
        return this.value;
    }

    public String setValue(String value2) {
        Validate.notNull(value2);
        String old = this.value;
        this.value = value2;
        return old;
    }

    public String html() {
        return this.key + "=\"" + Entities.escape(this.value, new Document("").outputSettings()) + "\"";
    }

    /* access modifiers changed from: protected */
    public void html(StringBuilder accum, Document.OutputSettings out) {
        accum.append(this.key).append("=\"").append(Entities.escape(this.value, out)).append("\"");
    }

    public String toString() {
        return html();
    }

    public static Attribute createFromEncoded(String unencodedKey, String encodedValue) {
        return new Attribute(unencodedKey, Entities.unescape(encodedValue, true));
    }

    /* access modifiers changed from: protected */
    public boolean isDataAttribute() {
        return this.key.startsWith("data-") && this.key.length() > "data-".length();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attribute)) {
            return false;
        }
        Attribute attribute = (Attribute) o;
        if (this.key == null ? attribute.key != null : !this.key.equals(attribute.key)) {
            return false;
        }
        if (this.value != null) {
            if (this.value.equals(attribute.value)) {
                return true;
            }
        } else if (attribute.value == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (this.key != null) {
            result = this.key.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.value != null) {
            i = this.value.hashCode();
        }
        return i2 + i;
    }

    public Attribute clone() {
        try {
            return (Attribute) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
