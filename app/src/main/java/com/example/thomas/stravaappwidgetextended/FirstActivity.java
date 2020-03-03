package com.example.thomas.stravaappwidgetextended;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.thomas.stravaappwidgetextended.api.authenticator.InitialStravaAuthActivity;
import com.example.thomas.stravaappwidgetextended.sharedPreferences.SharedPrefManager;

public class FirstActivity extends AppCompatActivity{

    private SharedPrefManager shared_pref_manager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shared_pref_manager = new SharedPrefManager(this);

        Boolean is_authentified = shared_pref_manager.checkIfStravaAccessIsGranted();
        Intent intent;

        if (is_authentified){
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else {
            intent = new Intent(this, InitialStravaAuthActivity.class);
            startActivity(intent);
        }
    }
}
