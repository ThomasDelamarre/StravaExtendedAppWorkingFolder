package com.example.thomas.stravaappwidgetextended.api.authenticator;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.thomas.stravaappwidgetextended.Constants;
import com.example.thomas.stravaappwidgetextended.api.pojo.AuthTokens;
import com.example.thomas.stravaappwidgetextended.sharedPreferences.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthManager {

    private SharedPrefManager shared_pref_manager;
    private Context context;

    public AuthManager(Context context){
        this.context = context;
        shared_pref_manager = new SharedPrefManager(this.context);
    }

    TokenRefresher tokenRefresher = new TokenRefresher() {
        @Override
        public void success(Response<AuthTokens> response) {
            AuthTokens authTokens =  response.body();
            shared_pref_manager.saveAuthToken(authTokens);
        }
    };

    public AuthTokens getAuthTokens(){

        refreshAuthTokens(tokenRefresher);
        AuthTokens auth_token = new AuthTokens();
        auth_token.setExpiresAt(shared_pref_manager.getExpiresAt());
        auth_token.setAccessToken(shared_pref_manager.getAccessToken());
        auth_token.setRefreshToken(shared_pref_manager.getRefreshToken());
        return auth_token;
    }


    private void refreshAuthTokens(final TokenRefresher token_refresher){
        int expires_at = this.shared_pref_manager.getExpiresAt();
        String refresh_token = this.shared_pref_manager.getRefreshToken();

        if (isExpired(expires_at)){
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
                                 //AuthTokens authTokens = (AuthTokens) response.body();
                                 //shared_pref_manager.saveAuthToken(authTokens);
                                 token_refresher.success(response);
                             }

                             @Override
                             public void onFailure(Call call, Throwable t) {
                                 Log.i("Token refresh", "fail");
                             }
                         });
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

    private interface TokenRefresher{
        void success(Response<AuthTokens> response);
    }
}
