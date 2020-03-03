package com.example.thomas.stravaappwidgetextended.api.requestor;

import com.example.thomas.stravaappwidgetextended.api.pojo.Activity;
import com.example.thomas.stravaappwidgetextended.api.pojo.Athlete;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;


public interface StravaApi {

    @GET("athlete/activities")
    Call <List<Activity>> getLast30Activities(
            @Header("Authorization") String bearer);

    @GET("athlete/activities")
    Call <List<Activity>> getAllActivitiesAfter(
            @Header("Authorization") String bearer,
            @Query("after") long timestamp,
            @Query("per_page") int per_page,
            @Query("page") int page);
}