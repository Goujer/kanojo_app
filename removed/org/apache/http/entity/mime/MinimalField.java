package org.apache.http.entity.mime;

public class MinimalField {
    private final String name;
    private final String value;

    MinimalField(String name2, String value2) {
        this.name = name2;
        this.value = value2;
    }

    public String getName() {
        return this.name;
    }

    public String getBody() {
        return this.value;
    }

    public String toString() {
        return this.name + ": " + this.value;
    }
}
