package com.facebook.model;

public interface GraphPlace extends GraphObject {
    String getCategory();

    String getId();

    GraphLocation getLocation();

    String getName();

    void setCategory(String str);

    void setId(String str);

    void setLocation(GraphLocation graphLocation);

    void setName(String str);
}
