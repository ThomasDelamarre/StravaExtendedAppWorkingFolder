package com.example.thomas.stravaappwidgetextended.api.authenticator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.thomas.stravaappwidgetextended.Constants;
import com.example.thomas.stravaappwidgetextended.R;


public class InitialStravaAuthActivity extends AppCompatActivity {

    private ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        button = findViewById(R.id.click2);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openStravaApp();
            }
        });
    }

    private void openStravaApp(){
        Uri intentUri = Uri.parse(Constants.STRAVA_URI)
                .buildUpon()
                .appendQueryParameter("client_id", Constants.CLIENT_ID)
                .appendQueryParameter("redirect_uri", Constants.REDIRECT_URI)
                .appendQueryParameter("response_type", Constants.RESPONSE_TYPE_CODE)
                .appendQueryParameter("approval_prompt", Constants.APPROVAL_PROMPT_AUTO)
                .appendQueryParameter("scope", Constants.SCOPE_READ + "," + Constants.SCOPE_PROFILE_READ_ALL + "," + Constants.SCOPE_ACTIVITY_READ_ALL)
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW, intentUri);
        startActivity(intent);
        }
    }
