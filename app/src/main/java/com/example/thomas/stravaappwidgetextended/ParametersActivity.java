package com.example.thomas.stravaappwidgetextended;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thomas.stravaappwidgetextended.api.requestor.RequestManager;
import com.example.thomas.stravaappwidgetextended.appWidget.AppWidgetProvider;
import com.example.thomas.stravaappwidgetextended.graph.ChartManager;
import com.example.thomas.stravaappwidgetextended.sharedPreferences.SharedPrefManager;

import java.time.LocalDate;
import java.util.ArrayList;

public class ParametersActivity extends AppCompatActivity {

    private Button ht_btn;

    private ImageView chart;
    private TextView display_type;
    private TextView x_km_hour;
    private TextView x_unit;
    private TextView x_minutes;
    private ImageButton refresh_appwidget;
    private ImageButton all_btn;
    private ImageButton run_btn;
    private ImageButton ride_btn;
    private ImageButton swim_btn;
    private ArrayList<ImageButton> widget_btns;
    private RelativeLayout appwidget;

    private Button refresh_btn;
    private Button initial_fetch_btn;
    private RadioButton rb_labelson;
    private RadioButton rb_labelsoff;
    private RadioGroup rg_unit;
    private RadioButton rb_distance;
    private RadioButton rb_duration;
    private RadioGroup rg_displaytype;
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
    private SharedPrefManager sharedpref_manager;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_parameters);

        this.context = this;

        this.chart_manager = new ChartManager(this);
        this.request_manager = new RequestManager(this);
        this.sharedpref_manager = new SharedPrefManager(this);

        //Main activity layout
        this.initial_fetch_btn = findViewById(R.id.initial_fetch_btn);
        this.refresh_btn = findViewById(R.id.refresh_data);
        this.rb_labelsoff = findViewById(R.id.rb_labels_off);
        this.rb_labelson = findViewById(R.id.rb_labels_on);
        this.rg_displaytype = findViewById(R.id.rg_displaytype);
        this.rb_currentmonth = findViewById(R.id.rb_currentmonth);
        this.rb_currentweek = findViewById(R.id.rb_currentweek);
        this.rb_sincedate = findViewById(R.id.rb_sincedate);
        this.rb_lastXdays = findViewById(R.id.rb_lastXdays);
        this.rg_unit = findViewById(R.id.rg_unit);
        this.rb_distance = findViewById(R.id.rb_distance);
        this.rb_duration = findViewById(R.id.rb_duration);
        this.number_days = findViewById(R.id.number_days);
        this.calendar_view = findViewById(R.id.calendarview);
        this.layout_calendar = findViewById(R.id.calendar);
        this.layout_since_days = findViewById(R.id.since_days);
        this.progress_bar = findViewById(R.id.progress_bar);
        this.ht_btn = findViewById(R.id.convert_to_ht);

        //Appwidget layout
        this.appwidget = findViewById(R.id.appwidget);
        this.chart = findViewById(R.id.barchart);
        this.display_type = findViewById(R.id.display_type);
        this.x_km_hour = findViewById(R.id.x_km_hour);
        this.x_unit = findViewById(R.id.x_unit);
        this.x_minutes = findViewById(R.id.x_minutes);
        this.all_btn = findViewById(R.id.all_btn);
        this.ride_btn = findViewById(R.id.ride_btn);
        this.run_btn = findViewById(R.id.run_btn);
        this.swim_btn = findViewById(R.id.swim_btn);
        this.refresh_appwidget = findViewById(R.id.refresh);
        this.widget_btns = new ArrayList<>();
        this.widget_btns.add(all_btn);
        this.widget_btns.add(ride_btn);
        this.widget_btns.add(swim_btn);
        this.widget_btns.add(run_btn);

        configureInitialState();
        emulateAppWidget();

        initial_fetch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initial_fetch_btn.setVisibility(View.GONE);
                progress_bar.setVisibility(View.VISIBLE);
                request_manager.fetchOneYearActivities();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        progress_bar.setVisibility(View.GONE);
                        refresh_btn.setVisibility(View.VISIBLE);
                        rg_displaytype.setVisibility(View.VISIBLE);
                        rg_unit.setVisibility(View.VISIBLE);
                    }
                }, 5000); //TODO Not good to have fixed delay, I know ....
                sharedpref_manager.saveInitialFetchDone(Boolean.TRUE);

                //Default first view of the data
                sharedpref_manager.saveDisplayType(Constants.CURRENT_WEEK);
                sharedpref_manager.saveSportType(Constants.ALL_SPORTS);
                emulateAppWidget();
            }
        });

        ht_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ManageDbActivity.class);
                startActivity(intent);
                //request_manager.convertActToHt();
                //Log.e("Converted", "done");
            }
        });

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request_manager.fetchLast30Activities();
                emulateAppWidget();
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
                    sharedpref_manager.saveDisplayType(Constants.CURRENT_WEEK);
                    layout_calendar.setVisibility(View.GONE);
                    layout_since_days.setVisibility(View.GONE);
                    emulateAppWidget();
                }
                break;
            case R.id.rb_currentmonth:
                if (checked){
                    sharedpref_manager.saveDisplayType(Constants.CURRENT_MONTH);
                    layout_calendar.setVisibility(View.GONE);
                    layout_since_days.setVisibility(View.GONE);
                    emulateAppWidget();
                }
                break;
            case R.id.rb_sincedate:
                if (checked){
                    sharedpref_manager.saveDisplayType(Constants.SINCE_DATE);
                    layout_calendar.setVisibility(View.VISIBLE);
                    layout_since_days.setVisibility(View.GONE);
                    appwidget.setVisibility(View.GONE);
                    calendar_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

                        @Override
                        public void onSelectedDayChange(CalendarView view, int year, int month,
                                                        int dayOfMonth) {
                            LocalDate date = LocalDate.of(year, month+1, dayOfMonth); //Strange fix le +1
                            if (date.isBefore(LocalDate.now())) {
                                sharedpref_manager.saveStartDate(date);
                            } else {
                                Toast toast = Toast.makeText(context, "Invalid date (in the future)", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            //TODO make calendar highlight saved date
                            //griser apres aujourd'hui
                        }
                    });
                    calendar_view.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rb_lastXdays:
                if (checked){
                    sharedpref_manager.saveDisplayType(Constants.CUSTOM);
                    layout_calendar.setVisibility(View.GONE);
                    layout_since_days.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    public void onUnitSelected(View view) {
        // Is the view now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.rb_distance:
                if (checked){
                    sharedpref_manager.saveUnit(Constants.DISTANCE);
                    x_unit.setText("km");
                    x_minutes.setVisibility(View.GONE);
                    emulateAppWidget();
                }
                break;
            case R.id.rb_duration:
                if (checked){
                    sharedpref_manager.saveUnit(Constants.DURATION);
                    x_unit.setText("h");
                    x_minutes.setVisibility(View.VISIBLE);
                    emulateAppWidget();
                }
                break;
        }
    }

    public void onLabelSelected(View view) {
        // Is the view now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.rb_labels_on:
                if (checked){
                    sharedpref_manager.saveLabelsChoice(Constants.ENABLED);
                    emulateAppWidget();
                }
                break;
            case R.id.rb_labels_off:
                if (checked){
                    sharedpref_manager.saveLabelsChoice(Constants.DISABLED);
                    emulateAppWidget();
                }
                break;
        }
    }

    public void onApplyButtonClicked(View view) {

        switch(view.getId()) {
            case R.id.btn_apply_calendar:
                calendar_view.setVisibility(View.GONE);
                appwidget.setVisibility(View.VISIBLE);
                emulateAppWidget();
                break;
            case R.id.btn_apply_days:
                String nb_days_text = number_days.getText().toString();
                if (!nb_days_text.isEmpty() && Integer.parseInt(nb_days_text) < 367) {
                    int nb_days_int = Integer.parseInt(nb_days_text);
                    sharedpref_manager.saveNumberDays(nb_days_int);
                    emulateAppWidget();
                } else {
                    number_days.setText("");
                    number_days.setHint("La valeur doit être inférieure à 1 an");
                    //No change of graph in this case
                }
                break;
        }
    }

    private void configureInitialState(){
        String display_type = sharedpref_manager.getDisplayType();
        String unit = sharedpref_manager.getUnit();
        Boolean done = sharedpref_manager.getInitialFetchDone();
        String labels = sharedpref_manager.getLabelsChoice();

        switch (labels) {
            case Constants.ENABLED:
                rb_labelson.setChecked(true);
                rb_labelsoff.setChecked(false);
                break;
            case Constants.DISABLED:
                rb_labelson.setChecked(false);
                rb_labelsoff.setChecked(true);
                break;
        }

        switch (unit) {
            case Constants.DISTANCE:
                rb_distance.setChecked(true);
                rb_duration.setChecked(false);
                break;
            case Constants.DURATION:
                rb_distance.setChecked(false);
                rb_duration.setChecked(true);
                break;
        }


        switch (display_type){
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
                rb_currentmonth.setChecked(false);
                rb_lastXdays.setChecked(false);
                rb_currentweek.setChecked(false);
                rb_sincedate.setChecked(true);
                layout_calendar.setVisibility(View.VISIBLE);
                calendar_view.setVisibility(View.GONE);
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
            rg_displaytype.setVisibility(View.VISIBLE);
            rg_unit.setVisibility(View.VISIBLE);
        } else {
            initial_fetch_btn.setVisibility(View.VISIBLE);
            refresh_btn.setVisibility(View.GONE);
            rg_displaytype.setVisibility(View.GONE);
            rg_unit.setVisibility(View.GONE);
        }
    }

    private void emulateAppWidget(){
        generateOnClickListener(all_btn, Constants.ALL_SPORTS);
        generateOnClickListener(ride_btn, Constants.RIDE);
        generateOnClickListener(run_btn, Constants.RUN);
        generateOnClickListener(swim_btn, Constants.SWIM);

        highlightCorrectButton(widget_btns);
        chart.setImageBitmap(chart_manager.getBarChartInBitmap(sharedpref_manager.getSportType()));
        refresh_appwidget.setVisibility(View.GONE);
        if(sharedpref_manager.getInitialFetchDone()) {
            setDisplayType();
            x_unit.setText(parseDistance(chart_manager.getTotal()));
        } else {
            display_type.setText("Hit the button above to fetch data");
            x_unit.setText("0");
        }
        updateHomeScreenAppWidget();
    }

    private void setDisplayType(){
        String display_type_str = sharedpref_manager.getDisplayType();
        if (display_type_str.equals(Constants.CURRENT_MONTH) || display_type_str.equals(Constants.CURRENT_WEEK)){
            display_type.setText(display_type_str);
        } else if (display_type_str.equals(Constants.CUSTOM)){
            String number_days = Integer.toString(sharedpref_manager.getNumberDays());
            display_type.setText("Last " +number_days + " days");
        } else if (display_type_str.equals(Constants.SINCE_DATE)){
            LocalDate date = sharedpref_manager.getStartDate();
            display_type.setText("Since " + date.getDayOfMonth() + " " + date.getMonth().toString());
        }
    }

    private void generateOnClickListener(final ImageButton btn, final String sport){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllButtonsBlack();
                sharedpref_manager.saveSportType(sport);
                emulateAppWidget();
            }
        } );
    }

    private void setAllButtonsBlack(){
        int black = ContextCompat.getColor(context, R.color.black);
        for (ImageButton btn : widget_btns){
            btn.setColorFilter(black);
        }
    }

    private void updateHomeScreenAppWidget(){
        Intent intent = new Intent(this, AppWidgetProvider.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), AppWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        sendBroadcast(intent);
    }

    private void highlightCorrectButton(ArrayList<ImageButton> widget_btns){
        for (ImageButton btn : widget_btns){
            if (is_correct_button(btn)){
                btn.setColorFilter(ContextCompat.getColor(context, R.color.white));
            }
        }
    }

    private Boolean is_correct_button(ImageButton btn) {
        String sport_type = sharedpref_manager.getSportType();
        if (btn.getTag().toString().equals(sport_type)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private String parseDistance(float distance){
        String distance_str = Float.toString(distance);
        distance_str = distance_str.replace(".",",");
        return distance_str;
    }

    //Forbid going back to previous page
    @Override
    public void onBackPressed() {
        return;
    }
}