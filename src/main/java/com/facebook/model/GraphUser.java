package com.facebook.model;

public interface GraphUser extends GraphObject {
    String getBirthday();

    String getFirstName();

    String getId();

    String getLastName();

    String getLink();

    GraphLocation getLocation();

    String getMiddleName();

    String getName();

    String getUsername();

    void setBirthday(String str);

    void setFirstName(String str);

    void setId(String str);

    void setLastName(String str);

    void setLink(String str);

    void setLocation(GraphLocation graphLocation);

    void setMiddleName(String str);

    void setName(String str);

    void setUsername(String str);
}
