package com.facebook.model;

import java.util.Date;
import java.util.List;
import org.json.JSONObject;

public interface OpenGraphAction extends GraphObject {
    GraphObject getApplication();

    JSONObject getComments();

    Date getCreatedTime();

    Date getEndTime();

    Date getExpiresTime();

    GraphUser getFrom();

    String getId();

    List<JSONObject> getImage();

    JSONObject getLikes();

    String getMessage();

    GraphPlace getPlace();

    Date getPublishTime();

    String getRef();

    Date getStartTime();

    List<GraphObject> getTags();

    void setApplication(GraphObject graphObject);

    void setComments(JSONObject jSONObject);

    void setCreatedTime(Date date);

    void setEndTime(Date date);

    void setExpiresTime(Date date);

    void setFrom(GraphUser graphUser);

    void setId(String str);

    void setImage(List<JSONObject> list);

    void setLikes(JSONObject jSONObject);

    void setMessage(String str);

    void setPlace(GraphPlace graphPlace);

    void setPublishTime(Date date);

    void setRef(String str);

    void setStartTime(Date date);

    void setTags(List<? extends GraphObject> list);
}
