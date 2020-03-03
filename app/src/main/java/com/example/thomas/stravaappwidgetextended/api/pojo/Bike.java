package com.example.thomas.stravaappwidgetextended.api.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bike {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("primary")
    @Expose
    private Boolean primary;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("resource_state")
    @Expose
    private Integer resourceState;
    @SerializedName("distance")
    @Expose
    private Integer distance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getResourceState() {
        return resourceState;
    }

    public void setResourceState(Integer resourceState) {
        this.resourceState = resourceState;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}