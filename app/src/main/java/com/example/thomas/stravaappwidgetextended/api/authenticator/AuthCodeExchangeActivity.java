package com.example.thomas.stravaappwidgetextended.api.authenticator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.thomas.stravaappwidgetextended.MainActivity;
import com.example.thomas.stravaappwidgetextended.Constants;
import com.example.thomas.stravaappwidgetextended.api.pojo.AuthTokens;
import com.example.thomas.stravaappwidgetextended.api.requestor.RequestManager;
import com.example.thomas.stravaappwidgetextended.sharedPreferences.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthCodeExchangeActivity extends AppCompatActivity {

    private SharedPrefManager shared_pref_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        shared_pref_manager = new SharedPrefManager(this);

        Intent intent = getIntent();
        Uri data = intent.getData();
        final String authorization_code = data.getQueryParameter(Constants.RESPONSE_TYPE_CODE);
        Log.i("Auth code handled", "success");

        exchangeAuthCodeforAuthTokens(authorization_code);

        intent = new Intent(this, MainActivity.class);
        Toast toast = Toast.makeText(this, "Authentification succesful!", Toast.LENGTH_SHORT);
        toast.show();
        startActivity(intent);
    }

    public void exchangeAuthCodeforAuthTokens(String authorization_code) {
        String client_id = Constants.CLIENT_ID;
        String client_secret = Constants.CLIENT_SECRET;
        String grant_type = Constants.GRANT_TYPE_AUTH_CODE;

        Retrofit retrofit = AuthentificatorNetworkClient.getRetrofitClient();
        Authenticator authenticator = retrofit.create(Authenticator.class);

        Call call = authenticator.getAllTokens(client_id, client_secret, authorization_code, grant_type);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.i("Auth exchange for token", "success");
                AuthTokens authTokens = (AuthTokens) response.body();
                shared_pref_manager.saveAuthToken(authTokens);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("Authexchangefortoken", "fail");
            }
        });
    }
}