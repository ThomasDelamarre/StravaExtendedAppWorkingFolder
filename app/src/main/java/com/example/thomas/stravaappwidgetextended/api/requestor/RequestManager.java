package com.example.thomas.stravaappwidgetextended.api.requestor;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.thomas.stravaappwidgetextended.api.authenticator.AuthManager;
import com.example.thomas.stravaappwidgetextended.api.pojo.Activity;
import com.example.thomas.stravaappwidgetextended.api.pojo.AuthTokens;
import com.example.thomas.stravaappwidgetextended.database.DatabaseManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RequestManager {

    private Retrofit retrofit = NetworkClient.getRetrofitClient();
    private StravaApi stravaApi;

    private DatabaseManager database_manager;
    private AuthManager auth_manager;
    private Context context;

    public RequestManager(Context context) {
        this.context = context;
        this.stravaApi = this.retrofit.create(StravaApi.class);
        this.auth_manager = new AuthManager(this.context);
        this.database_manager = new DatabaseManager(this.context);
    }

    //This method is used at first auth to populate the db and by the "Force full refresh" button
    public void fetchOneYearActivities(){
        AuthTokens auth_tokens = getAuthTokens();
        String bearer = "Bearer " + auth_tokens.getAccessToken();
        long current_time = System.currentTimeMillis()/1000; //We want the date in seconds
        long year = 60*60*24*365;

        for (int i = 1; i<11; i++){ //This allows to get 1000activities
            Call call = this.stravaApi.getAllActivitiesAfter(bearer, current_time - year, 100, i);

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.body() != null) {
                        List<Activity> activities = (List<Activity>) response.body();
                        database_manager.addActivitiesToDatabase(activities);
                        Toast toast = Toast.makeText(context, "Data succesfully fetched!", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("erreur", t.getMessage());
                }
            });
        }
    }

    //This method is used whenever the user wants to refresh the data = fetch last 30 activities
    public void fetchLast30Activities() {
        AuthTokens auth_tokens = getAuthTokens();
        String bearer = "Bearer " + auth_tokens.getAccessToken();
        Log.e("bearer", bearer);

        Call call = this.stravaApi.getLast30Activities(bearer);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body() != null) {
                    List<Activity> activities = (List<Activity>) response.body();
                    database_manager.addActivitiesToDatabase(activities);
                    Toast toast = Toast.makeText(context, "Data succesfully refreshed!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("erreur", t.getMessage());
            }
        });
    }

    private AuthTokens getAuthTokens(){
        return auth_manager.getAuthTokens();
    }

}