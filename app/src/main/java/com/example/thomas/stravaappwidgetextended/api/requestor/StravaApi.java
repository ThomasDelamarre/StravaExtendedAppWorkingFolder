package com.example.thomas.stravaappwidgetextended.api.requestor;

import com.example.thomas.stravaappwidgetextended.api.pojo.Activity;
import com.example.thomas.stravaappwidgetextended.api.pojo.RenameAct;
import com.example.thomas.stravaappwidgetextended.api.pojo.UpdateAct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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

    @GET("athlete/activities")
    Call <List<Activity>> getLastActivity(
            @Header("Authorization") String bearer,
            @Query("page") int page,
            @Query("per_page") int per_page);

    @PUT("activities/{id}")
    Call <Activity> setActivityToHomeTrainer(
            @Header("Authorization") String bearer,
            @Path("id") long id,
            @Body UpdateAct updateAct);

    @PUT("activities/{id}")
    Call <Activity> renameActivity(
            @Header("Authorization") String bearer,
            @Path("id") long id,
            @Body RenameAct renameAct);
}
