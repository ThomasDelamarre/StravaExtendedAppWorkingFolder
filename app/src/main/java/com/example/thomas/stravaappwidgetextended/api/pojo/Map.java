
package com.example.thomas.stravaappwidgetextended.api.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Map {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("summary_polyline")
    @Expose
    private Object summaryPolyline;
    @SerializedName("resource_state")
    @Expose
    private Integer resourceState;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getSummaryPolyline() {
        return summaryPolyline;
    }

    public void setSummaryPolyline(Object summaryPolyline) {
        this.summaryPolyline = summaryPolyline;
    }

    public Integer getResourceState() {
        return resourceState;
    }

    public void setResourceState(Integer resourceState) {
        this.resourceState = resourceState;
    }

}
