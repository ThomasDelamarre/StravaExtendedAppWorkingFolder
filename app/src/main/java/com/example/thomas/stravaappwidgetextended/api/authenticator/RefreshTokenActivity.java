package com.example.thomas.stravaappwidgetextended.api.authenticator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.thomas.stravaappwidgetextended.MainActivity;
import com.example.thomas.stravaappwidgetextended.Constants;
import com.example.thomas.stravaappwidgetextended.api.pojo.AuthTokens;
import com.example.thomas.stravaappwidgetextended.sharedPreferences.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RefreshTokenActivity extends AppCompatActivity {

    private SharedPrefManager shared_pref_manager;

    //TODO merge with main

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.e("refrshtoken", "je suis la");

        shared_pref_manager = new SharedPrefManager(this);
        String refresh_token = shared_pref_manager.getRefreshToken();
        refreshAuthTokens(refresh_token);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void refreshAuthTokens(String refresh_token) {
        String client_id = Constants.CLIENT_ID;
        String client_secret = Constants.CLIENT_SECRET;
        String grant_type = Constants.GRANT_TYPE_REFRESH;

        Retrofit retrofit = AuthentificatorNetworkClient.getRetrofitClient();
        Authenticator authenticator = retrofit.create(Authenticator.class);

        Call call = authenticator.refreshTokens(client_id, client_secret, grant_type, refresh_token);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.i("Token refresh", "success");
                AuthTokens authTokens = (AuthTokens) response.body();
                shared_pref_manager.saveAuthToken(authTokens);
                Log.i("access token", authTokens.getAccessToken());
                Log.i("refresh token", authTokens.getRefreshToken());
                Log.i("expires at", Integer.toString(authTokens.getExpiresAt()));
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.i("Token refresh", "fail");
            }
        });
    }
}
