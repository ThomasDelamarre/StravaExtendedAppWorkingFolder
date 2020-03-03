package com.example.thomas.stravaappwidgetextended.api.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthTokens {

    @SerializedName("token_type")
    @Expose
    private String token_type;
    @SerializedName("expires_at")
    @Expose
    private Integer expires_at;
    @SerializedName("expires_in")
    @Expose
    private Integer expires_in;
    @SerializedName("refresh_token")
    @Expose
    private String refresh_token;
    @SerializedName("access_token")
    @Expose
    private String access_token;
    @SerializedName("athlete")
    @Expose
    private Athlete athlete;

    public String getTokenType() {
        return token_type;
    }

    public void setTokenType(String tokenType) {
        this.token_type = tokenType;
    }

    public Integer getExpiresAt() {
        return expires_at;
    }

    public void setExpiresAt(Integer expiresAt) {
        this.expires_at = expiresAt;
    }

    public Integer getExpiresIn() {
        return expires_in;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expires_in = expiresIn;
    }

    public String getRefreshToken() {
        return refresh_token;
    }

    public void setRefreshToken(String refreshToken) {
        this.refresh_token = refreshToken;
    }

    public String getAccessToken() {
        return access_token;
    }

    public void setAccessToken(String accessToken) {
        this.access_token = accessToken;
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

}