package com.example.thomas.stravaappwidgetextended.api.authenticator;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.thomas.stravaappwidgetextended.api.pojo.AuthTokens;
import com.example.thomas.stravaappwidgetextended.sharedPreferences.SharedPrefManager;

public class AuthManager {

    private SharedPrefManager shared_pref_manager;
    private Context context;

    public AuthManager(Context context){
        this.context = context;
        shared_pref_manager = new SharedPrefManager(this.context);
    }

    public AuthTokens getAuthTokens(){
        refreshAuthTokens();
        AuthTokens auth_token = new AuthTokens();
        auth_token.setExpiresAt(shared_pref_manager.getExpiresAt());
        auth_token.setAccessToken(shared_pref_manager.getAccessToken());
        auth_token.setRefreshToken(shared_pref_manager.getRefreshToken());
        return auth_token;
    }

    private void refreshAuthTokens(){
        int expires_at = this.shared_pref_manager.getExpiresAt();
        if (isExpired(expires_at)){
            Intent intent = new Intent(this.context, RefreshTokenActivity.class);
            this.context.startActivity(intent);
            Log.i("Auth tokens", "refreshed");

        } else {
            Log.i("Auth tokens", "not expired");
        }
    }


    private Boolean isExpired(int date){
        long current_time_millis = System.currentTimeMillis();
        long current_time_seconds = current_time_millis / 1000;
        if (current_time_seconds < date){
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
}
