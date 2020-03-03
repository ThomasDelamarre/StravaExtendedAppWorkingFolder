
package com.example.thomas.stravaappwidgetextended.api.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Activity {

    @SerializedName("resource_state")
    @Expose
    private Long resourceState;
    @SerializedName("athlete")
    @Expose
    private Athlete athlete;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("moving_time")
    @Expose
    private Long movingTime;
    @SerializedName("elapsed_time")
    @Expose
    private Long elapsedTime;
    @SerializedName("total_elevation_gain")
    @Expose
    private Float totalElevationGain;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("workout_type")
    @Expose
    private Object workoutType;
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("external_id")
    @Expose
    private String externalId;
    @SerializedName("upload_id")
    @Expose
    private Long uploadId;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("start_date_local")
    @Expose
    private String startDateLocal;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("utc_offset")
    @Expose
    private Long utcOffset;
    @SerializedName("start_latlng")
    @Expose
    private Object startLatlng;
    @SerializedName("end_latlng")
    @Expose
    private Object endLatlng;
    @SerializedName("location_city")
    @Expose
    private Object locationCity;
    @SerializedName("location_state")
    @Expose
    private Object locationState;
    @SerializedName("location_country")
    @Expose
    private String locationCountry;
    @SerializedName("start_latitude")
    @Expose
    private Object startLatitude;
    @SerializedName("start_longitude")
    @Expose
    private Object startLongitude;
    @SerializedName("achievement_count")
    @Expose
    private Long achievementCount;
    @SerializedName("kudos_count")
    @Expose
    private Long kudosCount;
    @SerializedName("comment_count")
    @Expose
    private Long commentCount;
    @SerializedName("athlete_count")
    @Expose
    private Long athleteCount;
    @SerializedName("photo_count")
    @Expose
    private Long photoCount;
    @SerializedName("map")
    @Expose
    private Map map;
    @SerializedName("trainer")
    @Expose
    private Boolean trainer;
    @SerializedName("commute")
    @Expose
    private Boolean commute;
    @SerializedName("manual")
    @Expose
    private Boolean manual;
    @SerializedName("private")
    @Expose
    private Boolean _private;
    @SerializedName("flagged")
    @Expose
    private Boolean flagged;
    @SerializedName("gear_id")
    @Expose
    private String gearId;
    @SerializedName("from_accepted_tag")
    @Expose
    private Boolean fromAcceptedTag;
    @SerializedName("average_speed")
    @Expose
    private Double averageSpeed;
    @SerializedName("max_speed")
    @Expose
    private Double maxSpeed;
    @SerializedName("average_cadence")
    @Expose
    private Double averageCadence;
    @SerializedName("average_watts")
    @Expose
    private Float averageWatts;
    @SerializedName("weighted_average_watts")
    @Expose
    private Long weightedAverageWatts;
    @SerializedName("kilojoules")
    @Expose
    private Float kilojoules;
    @SerializedName("device_watts")
    @Expose
    private Boolean deviceWatts;
    @SerializedName("has_heartrate")
    @Expose
    private Boolean hasHeartrate;
    @SerializedName("average_heartrate")
    @Expose
    private Double averageHeartrate;
    @SerializedName("max_heartrate")
    @Expose
    private Long maxHeartrate;
    @SerializedName("max_watts")
    @Expose
    private Long maxWatts;
    @SerializedName("pr_count")
    @Expose
    private Long prCount;
    @SerializedName("total_photo_count")
    @Expose
    private Long totalPhotoCount;
    @SerializedName("has_kudoed")
    @Expose
    private Boolean hasKudoed;
    @SerializedName("suffer_score")
    @Expose
    private Long sufferScore;

    public Long getResourceState() {
        return resourceState;
    }

    public void setResourceState(Long resourceState) {
        this.resourceState = resourceState;
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Long getMovingTime() {
        return movingTime;
    }

    public void setMovingTime(Long movingTime) {
        this.movingTime = movingTime;
    }

    public Long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(Long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public Float getTotalElevationGain() {
        return totalElevationGain;
    }

    public void setTotalElevationGain(Float totalElevationGain) {
        this.totalElevationGain = totalElevationGain;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(Object workoutType) {
        this.workoutType = workoutType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Long getUploadId() {
        return uploadId;
    }

    public void setUploadId(Long uploadId) {
        this.uploadId = uploadId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartDateLocal() {
        return startDateLocal;
    }

    public void setStartDateLocal(String startDateLocal) {
        this.startDateLocal = startDateLocal;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Long getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(Long utcOffset) {
        this.utcOffset = utcOffset;
    }

    public Object getStartLatlng() {
        return startLatlng;
    }

    public void setStartLatlng(Object startLatlng) {
        this.startLatlng = startLatlng;
    }

    public Object getEndLatlng() {
        return endLatlng;
    }

    public void setEndLatlng(Object endLatlng) {
        this.endLatlng = endLatlng;
    }

    public Object getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(Object locationCity) {
        this.locationCity = locationCity;
    }

    public Object getLocationState() {
        return locationState;
    }

    public void setLocationState(Object locationState) {
        this.locationState = locationState;
    }

    public String getLocationCountry() {
        return locationCountry;
    }

    public void setLocationCountry(String locationCountry) {
        this.locationCountry = locationCountry;
    }

    public Object getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(Object startLatitude) {
        this.startLatitude = startLatitude;
    }

    public Object getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(Object startLongitude) {
        this.startLongitude = startLongitude;
    }

    public Long getAchievementCount() {
        return achievementCount;
    }

    public void setAchievementCount(Long achievementCount) {
        this.achievementCount = achievementCount;
    }

    public Long getKudosCount() {
        return kudosCount;
    }

    public void setKudosCount(Long kudosCount) {
        this.kudosCount = kudosCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public Long getAthleteCount() {
        return athleteCount;
    }

    public void setAthleteCount(Long athleteCount) {
        this.athleteCount = athleteCount;
    }

    public Long getPhotoCount() {
        return photoCount;
    }

    public void setPhotoCount(Long photoCount) {
        this.photoCount = photoCount;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Boolean getTrainer() {
        return trainer;
    }

    public void setTrainer(Boolean trainer) {
        this.trainer = trainer;
    }

    public Boolean getCommute() {
        return commute;
    }

    public void setCommute(Boolean commute) {
        this.commute = commute;
    }

    public Boolean getManual() {
        return manual;
    }

    public void setManual(Boolean manual) {
        this.manual = manual;
    }

    public Boolean getPrivate() {
        return _private;
    }

    public void setPrivate(Boolean _private) {
        this._private = _private;
    }

    public Boolean getFlagged() {
        return flagged;
    }

    public void setFlagged(Boolean flagged) {
        this.flagged = flagged;
    }

    public String getGearId() {
        return gearId;
    }

    public void setGearId(String gearId) {
        this.gearId = gearId;
    }

    public Boolean getFromAcceptedTag() {
        return fromAcceptedTag;
    }

    public void setFromAcceptedTag(Boolean fromAcceptedTag) {
        this.fromAcceptedTag = fromAcceptedTag;
    }

    public Double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(Double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Double getAverageCadence() {
        return averageCadence;
    }

    public void setAverageCadence(Double averageCadence) {
        this.averageCadence = averageCadence;
    }

    public Float getAverageWatts() {
        return averageWatts;
    }

    public void setAverageWatts(Float averageWatts) {
        this.averageWatts = averageWatts;
    }

    public Long getWeightedAverageWatts() {
        return weightedAverageWatts;
    }

    public void setWeightedAverageWatts(Long weightedAverageWatts) {
        this.weightedAverageWatts = weightedAverageWatts;
    }

    public Float getKilojoules() {
        return kilojoules;
    }

    public void setKilojoules(Float kilojoules) {
        this.kilojoules = kilojoules;
    }

    public Boolean getDeviceWatts() {
        return deviceWatts;
    }

    public void setDeviceWatts(Boolean deviceWatts) {
        this.deviceWatts = deviceWatts;
    }

    public Boolean getHasHeartrate() {
        return hasHeartrate;
    }

    public void setHasHeartrate(Boolean hasHeartrate) {
        this.hasHeartrate = hasHeartrate;
    }

    public Double getAverageHeartrate() {
        return averageHeartrate;
    }

    public void setAverageHeartrate(Double averageHeartrate) {
        this.averageHeartrate = averageHeartrate;
    }

    public Long getMaxHeartrate() {
        return maxHeartrate;
    }

    public void setMaxHeartrate(Long maxHeartrate) {
        this.maxHeartrate = maxHeartrate;
    }

    public Long getMaxWatts() {
        return maxWatts;
    }

    public void setMaxWatts(Long maxWatts) {
        this.maxWatts = maxWatts;
    }

    public Long getPrCount() {
        return prCount;
    }

    public void setPrCount(Long prCount) {
        this.prCount = prCount;
    }

    public Long getTotalPhotoCount() {
        return totalPhotoCount;
    }

    public void setTotalPhotoCount(Long totalPhotoCount) {
        this.totalPhotoCount = totalPhotoCount;
    }

    public Boolean getHasKudoed() {
        return hasKudoed;
    }

    public void setHasKudoed(Boolean hasKudoed) {
        this.hasKudoed = hasKudoed;
    }

    public Long getSufferScore() {
        return sufferScore;
    }

    public void setSufferScore(Long sufferScore) {
        this.sufferScore = sufferScore;
    }

}