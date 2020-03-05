package com.example.thomas.stravaappwidgetextended.appWidget;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.thomas.stravaappwidgetextended.Constants;
import com.example.thomas.stravaappwidgetextended.R;
import com.example.thomas.stravaappwidgetextended.api.pojo.Activity;
import com.example.thomas.stravaappwidgetextended.database.DatabaseManager;
import com.example.thomas.stravaappwidgetextended.sharedPreferences.SharedPrefManager;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


public class DataPreparator {

    private DatabaseManager database_manager;
    private SharedPrefManager sharedpref_manager;
    private Context context;
    private ChronoUnit DAYS;

    public DataPreparator(Context context) {
        this.context = context;
        sharedpref_manager = new SharedPrefManager(this.context);
        database_manager = new DatabaseManager(this.context);
    }

    public ConvenientReturnFormat getChartData(String sport_type){

        ConvenientReturnFormat return_data;

        List<Activity> activities = this.database_manager.getAllActivitiesFromDatabase();

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now( zoneId );

        String data_display_type = sharedpref_manager.getDisplayType();
        int lenght = 0;
        LocalDate start_date = today;

        switch (data_display_type){
            case Constants.CURRENT_MONTH:
                start_date = today.withDayOfMonth(1);
                lenght = today.lengthOfMonth();
                break;
            case Constants.CURRENT_WEEK:
                start_date = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                lenght = 7;
                break;
            case Constants.SINCE_DATE:
                start_date = sharedpref_manager.getStartDate();
                lenght = (int) DAYS.between(start_date, today); //CA bug pas ca ? TODO
                break;
            case Constants.CUSTOM:
                lenght = sharedpref_manager.getNumberDays();
                start_date = today.minusDays(lenght-1);
                break;
            default:
                Log.e("Unexpected error", "Ft GetChartData");
                break;
        }

        ArrayList<BarEntry> entries_before = new ArrayList<>();
        ArrayList<BarEntry> entry_today = new ArrayList<>();
        ArrayList<BarEntry> entries_after = new ArrayList<>();

        ArrayList<String> labels = new ArrayList<>();
        double max_distance = 0;
        int today_index = 0;
        double total_distance = 0;

        for (long i = 0; i < lenght; i++) {
            LocalDate date = start_date.plusDays(i);
            double distance = getDistanceForDay(sport_type, date, activities);
            total_distance += distance;

            if (max_distance<distance) {max_distance = distance;}

            if (date.isBefore(today)) {
                entries_before.add(new BarEntry((int) i, (float) distance));
            } else if (date.isAfter(today)) {
                entries_after.add(new BarEntry((float) i, (float) distance));
            } else {
                today_index = (int) i;
                entry_today.add(new BarEntry((float) i, (float) distance));
            }

            if (lenght < 32) { //TODO For longer period we want to have other labels
                labels.add(getDayLetterFromDate(date));
            }
        }

        BarDataSet bar_dataset_before = new BarDataSet(entries_before, "Distance");
        bar_dataset_before.setColor(ContextCompat.getColor(context,R.color.green));
        bar_dataset_before.setBarBorderColor(ContextCompat.getColor(context,R.color.green));
        bar_dataset_before.setBarBorderWidth(1f);

        BarDataSet bar_dataset_today = new BarDataSet(entry_today, "Distance");
        bar_dataset_today.setColor(ContextCompat.getColor(context,R.color.darkgreen));
        bar_dataset_today.setBarBorderColor(ContextCompat.getColor(context, R.color.darkgreen));
        bar_dataset_today.setBarBorderWidth(1f);

        BarDataSet bar_dataset_after = new BarDataSet(entries_after, "Distance");
        bar_dataset_after.setColor(ContextCompat.getColor(context,R.color.grey));
        bar_dataset_after.setBarBorderColor(ContextCompat.getColor(context, R.color.grey));
        bar_dataset_after.setBarBorderWidth(1f);

        //Add datasets to bardata
        BarData data = new BarData(bar_dataset_before);
        data.addDataSet(bar_dataset_today);
        data.addDataSet(bar_dataset_after);

        //Set bar width - If too big for display size the lib handles
        data.setBarWidth(0.2f);

        return_data = new ConvenientReturnFormat(data, labels, today_index, (float) total_distance);
        return return_data;
    }

    private double getDistanceForDay(String sport_type, LocalDate day, List<Activity> activities){

        double distance_for_the_day = 0;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Activity act : activities) {
            if (act.getType().equals(Constants.VIRTUAL_RIDE)){act.setType(Constants.RIDE);} //Current handling of Virtual rides
            if (act.getType().equals(sport_type) || sport_type.equals(Constants.ALL_SPORTS)){
                String act_date_str = act.getStartDate();
                double act_distance = act.getDistance();
                double act_distance_km = convertToKilometers(act_distance);
                LocalDate date = LocalDate.parse(act_date_str, formatter);
                if (date.equals(day)) {
                    distance_for_the_day += act_distance_km;
                }
            }
        }
        return distance_for_the_day;
    }

    private double convertToKilometers(double distance){
        double dist_km = Math.round(distance/100);
        dist_km = dist_km/10; //To keep one decimal
        return dist_km;
    }

    private String getDayLetterFromDate(LocalDate date){
        int day = date.getDayOfYear();
        //TODO ENABLE IN ENGLISH
        String dateInFrench= date.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM, yyyy", Locale.FRENCH));
        return dateInFrench.substring(0,1).toUpperCase();
    }

    public class ConvenientReturnFormat {
        private final BarData bardata;
        private final ArrayList<String> labels;
        private final int today_index;
        private final float total_distance;

        public ConvenientReturnFormat(BarData first, ArrayList<String> second, int third, float fourth) {
            this.bardata = first;
            this.labels = second;
            this.today_index = third;
            this.total_distance = fourth;
        }

        public BarData getBarData() {
            return bardata;
        }

        public ArrayList<String> getLabels() {
            return labels;
        }

        public int getTodayIndex() {
            return today_index;
        }

        public float getTotalDistance() {
            return total_distance;
        }
    }
}
