package com.example.thomas.stravaappwidgetextended.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.thomas.stravaappwidgetextended.Constants;
import com.example.thomas.stravaappwidgetextended.api.pojo.AuthTokens;

import java.time.LocalDate;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefManager {

    private Context context;
    private SharedPreferences shared_pref;

    public SharedPrefManager(Context context){
        this.context = context;
        this.shared_pref = this.context.getSharedPreferences(Constants.SHARED_PREF_ADDRESS, MODE_PRIVATE);
    }

    public void saveAuthToken(AuthTokens tokens){
        String access_token = tokens.getAccessToken();
        String refresh_token = tokens.getRefreshToken();
        int expires_at = tokens.getExpiresAt();

        SharedPreferences.Editor editor = this.shared_pref.edit();
        editor.putInt(Constants.EXPIRES_AT, expires_at);
        editor.putString(Constants.REFRESH_TOKEN, refresh_token);
        editor.putString(Constants.ACCESS_TOKEN, access_token);
        editor.putString(Constants.ACCESS_GRANTED, Constants.TRUE);
        editor.commit();
    }

    public void saveDisplayType(String type){
        SharedPreferences.Editor editor = this.shared_pref.edit();
        editor.putString(Constants.DISPLAY_TYPE, type);
        editor.commit();
    }

    public void saveNumberDays(int nb_days){
        SharedPreferences.Editor editor = this.shared_pref.edit();
        editor.putInt(Constants.NUMBER_DAYS, nb_days);
        editor.commit();
    }

    public void saveInitialFetchDone(Boolean done){
        SharedPreferences.Editor editor = this.shared_pref.edit();
        editor.putBoolean(Constants.INITIAL_FETCH, done);
        editor.commit();
    }

    public void saveStartDate(LocalDate date){
        SharedPreferences.Editor editor = this.shared_pref.edit();
        String date_str = date.toString();
        editor.putString(Constants.START_DATE, date_str);
        editor.commit();
    }

    public void saveSportType(String sport_type){
        SharedPreferences.Editor editor = this.shared_pref.edit();
        editor.putString(Constants.SPORT_TYPE, sport_type);
        editor.commit();
    }

    public Boolean checkIfStravaAccessIsGranted(){
        String access_granted = this.shared_pref.getString(Constants.ACCESS_GRANTED, Constants.FALSE);
        Boolean bool;
        if (access_granted.equals(Constants.TRUE)){
            bool = Boolean.TRUE;
        } else if (access_granted.equals(Constants.FALSE)){
            bool = Boolean.FALSE;
        } else {
            Log.e("Unexpected error", "need to ask for strava auth again");
            bool = Boolean.FALSE;
        }
        return bool;
    }


    public Boolean getInitialFetchDone(){
        Boolean done = this.shared_pref.getBoolean(Constants.INITIAL_FETCH, Boolean.FALSE); //False until initiated
        return done;
    }

    public String getSportType(){
        String display_type = this.shared_pref.getString(Constants.SPORT_TYPE, Constants.ALL_SPORTS); //Default to current week
        return display_type;
    }

    public String getDisplayType(){
        String display_type = this.shared_pref.getString(Constants.DISPLAY_TYPE, Constants.CURRENT_WEEK); //Default to current week
        return display_type;
    }

    public int getNumberDays(){
        int nb_days = this.shared_pref.getInt(Constants.NUMBER_DAYS, 7); // 7 by default
        return nb_days;
    }

    public LocalDate getStartDate(){
        String date_str = this.shared_pref.getString(Constants.START_DATE, LocalDate.now().toString());
        Log.e("erreur ici", date_str);
        LocalDate date = LocalDate.parse(date_str);
        return date;
    }

    public String getAuthCode(){
        String auth_code = this.shared_pref.getString(Constants.AUTH_CODE, null);
        return auth_code;
    }

    public String getAccessToken(){
        String access_token = this.shared_pref.getString(Constants.ACCESS_TOKEN, null);
        return access_token;
    }

    public String getRefreshToken(){
        String refresh_token = this.shared_pref.getString(Constants.REFRESH_TOKEN, null);
        return refresh_token;
    }

    public int getExpiresAt(){
        int expires_at = this.shared_pref.getInt(Constants.EXPIRES_AT, 0);
        return expires_at;
    }
}