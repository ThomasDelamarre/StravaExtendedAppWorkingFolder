package com.example.thomas.stravaappwidgetextended;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.thomas.stravaappwidgetextended.api.requestor.RequestManager;
import com.example.thomas.stravaappwidgetextended.appWidget.ChartManager;
import com.example.thomas.stravaappwidgetextended.appWidget.DataPreparator;
import com.example.thomas.stravaappwidgetextended.database.DatabaseManager;
import com.example.thomas.stravaappwidgetextended.sharedPreferences.SharedPrefManager;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    private Button refresh_btn;
    private Button initial_fetch_btn;
    private ImageView chart;
    private RadioGroup radio_group;
    private RadioButton rb_currentweek;
    private RadioButton rb_currentmonth;
    private RadioButton rb_lastXdays;
    private RadioButton rb_sincedate;
    private LinearLayout layout_since_days;
    private LinearLayout layout_calendar;
    private EditText number_days;
    private CalendarView calendar_view;
    private ProgressBar progress_bar;

    private ChartManager chart_manager;
    private RequestManager request_manager;
    private DataPreparator data_prep;
    private DatabaseManager db_manager;
    private SharedPrefManager sharedpref_manager;

    private String display_type;

    private Context context;

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.context = this;

        this.data_prep = new DataPreparator(this);
        this.chart_manager = new ChartManager(this);
        this.db_manager = new DatabaseManager(this);
        this.request_manager = new RequestManager(this);
        this.sharedpref_manager = new SharedPrefManager(this);

        this.initial_fetch_btn = (Button) findViewById(R.id.initial_fetch_btn);
        this.refresh_btn = (Button) findViewById(R.id.refresh_btn);
        this.chart = (ImageView) findViewById(R.id.graph);
        this.rb_currentmonth = (RadioButton) findViewById(R.id.rb_currentmonth);
        this.rb_currentweek = (RadioButton) findViewById(R.id.rb_currentweek);
        this.rb_sincedate = (RadioButton) findViewById(R.id.rb_sincedate);
        this.rb_lastXdays = (RadioButton) findViewById(R.id.rb_lastXdays);
        this.number_days = (EditText) findViewById(R.id.number_days);
        this.calendar_view = (CalendarView) findViewById(R.id.calendarview);
        this.layout_calendar = (LinearLayout) findViewById(R.id.calendar);
        this.layout_since_days = (LinearLayout) findViewById(R.id.since_days);
        this.progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        this.radio_group = (RadioGroup) findViewById(R.id.radiogroup);

        configureInitialState();

        initial_fetch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initial_fetch_btn.setVisibility(View.GONE);
                progress_bar.setVisibility(View.VISIBLE);
                request_manager.fetchOneYearActivities();
                toast = Toast.makeText(context, "Fetching data...", Toast.LENGTH_SHORT);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        progress_bar.setVisibility(View.GONE);
                        refresh_btn.setVisibility(View.VISIBLE);
                        radio_group.setVisibility(View.VISIBLE);
                    }
                }, 4000); //Not good to have fix delay, I know ....
                sharedpref_manager.saveInitialFetchDone(Boolean.TRUE);
            }
        });

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request_manager.fetchLast30Activities();
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    public void onRadioButtonClicked(View view) {
        // Is the view now checked?
        boolean checked = ((RadioButton) view).isChecked();
        String sport_type = sharedpref_manager.getSportType();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.rb_currentweek:
                if (checked){
                    String display_type = Constants.CURRENT_WEEK;
                    sharedpref_manager.saveDisplayType(display_type);
                    layout_calendar.setVisibility(View.GONE);
                    layout_since_days.setVisibility(View.GONE);
                    chart.setImageBitmap(chart_manager.getBarChartInBitmap(sport_type));
                }
                break;
            case R.id.rb_currentmonth:
                if (checked){
                    String display_type = Constants.CURRENT_MONTH;
                    sharedpref_manager.saveDisplayType(display_type);
                    layout_calendar.setVisibility(View.GONE);
                    layout_since_days.setVisibility(View.GONE);
                    chart.setImageBitmap(chart_manager.getBarChartInBitmap(sport_type));
                }
                break;
            case R.id.rb_sincedate:
                if (checked){
                    String display_type = Constants.SINCE_DATE;
                    sharedpref_manager.saveDisplayType(display_type);
                    layout_calendar.setVisibility(View.VISIBLE);
                    layout_since_days.setVisibility(View.GONE);
                }
                break;
            case R.id.rb_lastXdays:
                if (checked){
                    String display_type = Constants.CUSTOM;
                    sharedpref_manager.saveDisplayType(display_type);
                    layout_calendar.setVisibility(View.GONE);
                    layout_since_days.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    public void onApplyButtonClicked(View view) {

        String sport_type = sharedpref_manager.getSportType();

        switch(view.getId()) {
            case R.id.btn_apply_calendar:
                //String date_lng = calendar_view.getDate();
                //LocalDate date = LocalDate.parse(date_lng);
                //TODO https://stackoverflow.com/questions/46144150/calendarview-getdate-method-returns-current-date-not-selected-date-what-am-i

                break;
            case R.id.btn_apply_days:
                String nb_days_text = number_days.getText().toString();
                if (!nb_days_text.isEmpty() && Integer.parseInt(nb_days_text) < 367) {
                    int nb_days_int = Integer.parseInt(nb_days_text);
                    sharedpref_manager.saveNumberDays(nb_days_int);
                    chart.setImageBitmap(chart_manager.getBarChartInBitmap(sport_type));

                } else {
                    number_days.setText("");
                    number_days.setHint("La valeur doit être inférieure à 1 an");
                    //No change of graph in this case
                }
                break;
        }
    }

    private void configureInitialState(){
        this.display_type = sharedpref_manager.getDisplayType();
        Boolean done = sharedpref_manager.getInitialFetchDone();
        String sport_type = sharedpref_manager.getSportType();

        switch (this.display_type){
            case Constants.CURRENT_MONTH:
                rb_currentweek.setChecked(false);
                rb_lastXdays.setChecked(false);
                rb_sincedate.setChecked(false);
                rb_currentmonth.setChecked(true);
                layout_calendar.setVisibility(View.GONE);
                layout_since_days.setVisibility(View.GONE);
                break;
            case Constants.CURRENT_WEEK:
                rb_currentmonth.setChecked(false);
                rb_lastXdays.setChecked(false);
                rb_sincedate.setChecked(false);
                rb_currentweek.setChecked(true);
                layout_calendar.setVisibility(View.GONE);
                layout_since_days.setVisibility(View.GONE);
                break;
            case Constants.SINCE_DATE:
                //make calendar highlight saved date
                rb_currentmonth.setChecked(false);
                rb_lastXdays.setChecked(false);
                rb_currentweek.setChecked(false);
                rb_sincedate.setChecked(true);
                layout_calendar.setVisibility(View.VISIBLE);
                layout_since_days.setVisibility(View.GONE);
                break;
            case Constants.CUSTOM:
                rb_currentmonth.setChecked(false);
                rb_sincedate.setChecked(false);
                rb_currentweek.setChecked(false);
                rb_lastXdays.setChecked(true);
                layout_calendar.setVisibility(View.GONE);
                layout_since_days.setVisibility(View.VISIBLE);
                break;
        }

        if (done){
            initial_fetch_btn.setVisibility(View.GONE);
            refresh_btn.setVisibility(View.VISIBLE);
            radio_group.setVisibility(View.VISIBLE);
        } else {
            initial_fetch_btn.setVisibility(View.VISIBLE);
            refresh_btn.setVisibility(View.GONE);
            radio_group.setVisibility(View.GONE);
        }

        chart.setImageBitmap(chart_manager.getBarChartInBitmap(sport_type));

    }

}