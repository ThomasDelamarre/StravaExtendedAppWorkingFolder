package com.example.thomas.stravaappwidgetextended;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.thomas.stravaappwidgetextended.api.pojo.Activity;
import com.example.thomas.stravaappwidgetextended.api.requestor.RequestManager;
import com.example.thomas.stravaappwidgetextended.database.DatabaseManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class ManageDbActivity extends AppCompatActivity{

    private Spinner act_spinner;
    private Button delete_act_btn, rename_act_btn, reload_db_btn, convert_ht_btn, apply_name_btn;
    private EditText new_name;

    private DatabaseManager db_manager;
    private RequestManager request_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_managedb);

        act_spinner = findViewById(R.id.act_spinner);
        delete_act_btn = findViewById(R.id.delete_act_btn);
        rename_act_btn = findViewById(R.id.rename_act_btn);
        reload_db_btn = findViewById(R.id.reload_db_btn);
        convert_ht_btn = findViewById(R.id.convert_ht_btn);
        apply_name_btn = findViewById(R.id.apply_name_btn);
        new_name = findViewById(R.id.new_name);

        db_manager = new DatabaseManager(this);
        request_manager = new RequestManager(this);

        act_spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        configureActSpinner();

        delete_act_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity act = getSpinnerActivity();
                db_manager.removeActivityFromDatabase(act);
                configureActSpinner();
            }
        });

        convert_ht_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request_manager.convertActToHt(getSpinnerActivity());
            }
        });

        rename_act_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_name.setVisibility(View.VISIBLE);
                apply_name_btn.setVisibility(View.VISIBLE);
            }
        });

        apply_name_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO CA MARCHE PAS
                request_manager.renameActivity(getSpinnerActivity(), new_name.getText().toString());
                new_name.setVisibility(View.GONE);
                apply_name_btn.setVisibility(View.GONE);
                configureActSpinner();
            }
        });

        reload_db_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db_manager.dumpCurrentDbAndCreateEmptyOne();
                request_manager.fetchOneYearActivities();
                //TODO TIMER POUR LAISSER LE TEMPS DE SE RE REMPLIR
                configureActSpinner();
            }
        });


    }

    private void configureActSpinner(){
        List<Activity> activities = db_manager.getAllActivitiesFromDatabase();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Map<LocalDate,String> act_treeMap = new TreeMap<LocalDate,String>(
                new Comparator<LocalDate>() {
                    @Override
                    public int compare(LocalDate d1, LocalDate d2) {
                        if (d2.isBefore(d1)) { //sort in descending order
                            return -1;
                        }
                        return 2;
                    }
                });

        for (Activity act: activities) {
            act_treeMap.put(LocalDate.parse(act.getStartDate(), formatter), act.getName());
        }

        List<String> list = new ArrayList<>(act_treeMap.values());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        act_spinner.setAdapter(dataAdapter);
    }

    private Activity getSpinnerActivity(){
        String name = String.valueOf(act_spinner.getSelectedItem());
        Activity act = db_manager.getActivityByName(name);
        return act;
    }

    private class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            //TODO Ca sert à quoi ?
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO A rien je crois
        }

    }
}

