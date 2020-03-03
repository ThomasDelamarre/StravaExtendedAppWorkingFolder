package com.example.thomas.stravaappwidgetextended.api.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Athlete {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("resource_state")
    @Expose
    private Integer resourceState;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("sex")
    @Expose
    private String sex;
    @SerializedName("premium")
    @Expose
    private Boolean premium;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("badge_type_id")
    @Expose
    private Integer badgeTypeId;
    @SerializedName("profile_medium")
    @Expose
    private String profileMedium;
    @SerializedName("profile")
    @Expose
    private String profile;
    @SerializedName("friend")
    @Expose
    private Object friend;
    @SerializedName("follower")
    @Expose
    private Object follower;
    @SerializedName("follower_count")
    @Expose
    private Integer followerCount;
    @SerializedName("friend_count")
    @Expose
    private Integer friendCount;
    @SerializedName("mutual_friend_count")
    @Expose
    private Integer mutualFriendCount;
    @SerializedName("athlete_type")
    @Expose
    private Integer athleteType;
    @SerializedName("date_preference")
    @Expose
    private String datePreference;
    @SerializedName("measurement_preference")
    @Expose
    private String measurementPreference;
    @SerializedName("clubs")
    @Expose
    private List<Object> clubs = null;
    @SerializedName("ftp")
    @Expose
    private Object ftp;
    @SerializedName("weight")
    @Expose
    private Integer weight;
    @SerializedName("bikes")
    @Expose
    private List<Bike> bikes = null;
    @SerializedName("shoes")
    @Expose
    private List<Shoe> shoes = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getResourceState() {
        return resourceState;
    }

    public void setResourceState(Integer resourceState) {
        this.resourceState = resourceState;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getBadgeTypeId() {
        return badgeTypeId;
    }

    public void setBadgeTypeId(Integer badgeTypeId) {
        this.badgeTypeId = badgeTypeId;
    }

    public String getProfileMedium() {
        return profileMedium;
    }

    public void setProfileMedium(String profileMedium) {
        this.profileMedium = profileMedium;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Object getFriend() {
        return friend;
    }

    public void setFriend(Object friend) {
        this.friend = friend;
    }

    public Object getFollower() {
        return follower;
    }

    public void setFollower(Object follower) {
        this.follower = follower;
    }

    public Integer getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(Integer followerCount) {
        this.followerCount = followerCount;
    }

    public Integer getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(Integer friendCount) {
        this.friendCount = friendCount;
    }

    public Integer getMutualFriendCount() {
        return mutualFriendCount;
    }

    public void setMutualFriendCount(Integer mutualFriendCount) {
        this.mutualFriendCount = mutualFriendCount;
    }

    public Integer getAthleteType() {
        return athleteType;
    }

    public void setAthleteType(Integer athleteType) {
        this.athleteType = athleteType;
    }

    public String getDatePreference() {
        return datePreference;
    }

    public void setDatePreference(String datePreference) {
        this.datePreference = datePreference;
    }

    public String getMeasurementPreference() {
        return measurementPreference;
    }

    public void setMeasurementPreference(String measurementPreference) {
        this.measurementPreference = measurementPreference;
    }

    public List<Object> getClubs() {
        return clubs;
    }

    public void setClubs(List<Object> clubs) {
        this.clubs = clubs;
    }

    public Object getFtp() {
        return ftp;
    }

    public void setFtp(Object ftp) {
        this.ftp = ftp;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public List<Bike> getBikes() {
        return bikes;
    }

    public void setBikes(List<Bike> bikes) {
        this.bikes = bikes;
    }

    public List<Shoe> getShoes() {
        return shoes;
    }

    public void setShoes(List<Shoe> shoes) {
        this.shoes = shoes;
    }

}