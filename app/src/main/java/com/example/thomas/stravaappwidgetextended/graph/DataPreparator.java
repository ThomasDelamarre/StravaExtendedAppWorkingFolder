package com.example.thomas.stravaappwidgetextended.graph;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.thomas.stravaappwidgetextended.Constants;
import com.example.thomas.stravaappwidgetextended.R;
import com.example.thomas.stravaappwidgetextended.api.pojo.Activity;
import com.example.thomas.stravaappwidgetextended.database.DatabaseManager;
import com.example.thomas.stravaappwidgetextended.sharedPreferences.SharedPrefManager;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;


import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DataPreparator {

    private DatabaseManager database_manager;
    private SharedPrefManager sharedpref_manager;
    private Context context;

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
        int length = 0;
        LocalDate start_date = today;

        switch (data_display_type){
            case Constants.CURRENT_MONTH:
                start_date = today.withDayOfMonth(1);
                length = today.lengthOfMonth();
                break;
            case Constants.CURRENT_WEEK:
                start_date = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                length = 7;
                break;
            case Constants.SINCE_DATE:
                start_date = sharedpref_manager.getStartDate();
                length = (int) ChronoUnit.DAYS.between(start_date, today) +1;
                break;
            case Constants.CUSTOM:
                length = sharedpref_manager.getNumberDays();
                start_date = today.minusDays(length-1);
                break;
            default:
                Log.e("Unexpected error", "Ft GetChartData");
                break;
        }

        ArrayList<BarEntry> entries_before = new ArrayList<>();
        ArrayList<BarEntry> entry_today = new ArrayList<>();
        ArrayList<BarEntry> entries_after = new ArrayList<>();

        ArrayList<String> labels = new ArrayList<>();
        double max_value = 0;
        int today_index = 0;

        double total = 0;

        for (long i = 0; i < length; i++) {
            LocalDate date = start_date.plusDays(i);
            ArrayList<Double> totals = getTotalForDay(sport_type, date, activities, total);

            double total_for_day = totals.get(0);
            total = totals.get(1);

            if (max_value<total_for_day) {max_value = total_for_day;}

            if (date.isBefore(today)) {
                entries_before.add(new BarEntry((int) i, (float) total_for_day));
            } else if (date.isAfter(today)) {
                entries_after.add(new BarEntry((float) i, (float) total_for_day));
            } else {
                today_index = (int) i;
                entry_today.add(new BarEntry((float) i, (float) total_for_day));
            }

            if (length < 32) {
                //We don't display day labels for duration >1 month
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

        //Remove labels top of bars if decided
        if (sharedpref_manager.getLabelsChoice().equals(Constants.DISABLED)){
            bar_dataset_before.setDrawValues(false);
            bar_dataset_today.setDrawValues(false);
            bar_dataset_after.setDrawValues(false);
        }

        //Add datasets to bardata
        BarData data = new BarData(bar_dataset_before);
        data.addDataSet(bar_dataset_today);
        data.addDataSet(bar_dataset_after);

        //Set bar width - If too big for display size the lib handles
        data.setBarWidth(0.2f);

        return_data = new ConvenientReturnFormat(data, labels, today_index, (float) total);
        return return_data;
    }

    private ArrayList<Double> getTotalForDay(String sport_type, LocalDate day, List<Activity> activities, double current_total){

        //We want to know if we dislay data in km or hh:mm
        String unit = sharedpref_manager.getUnit();

        ArrayList<Double> return_data = new ArrayList<>();
        double total_for_the_day = 0;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Activity act : activities) {
            //if (act.getType().equals(Constants.VIRTUAL_RIDE)){act.setType(Constants.RIDE);} //TODO RESTORE IT AS AN OPTION ??
            if (act.getType().equals(sport_type) || sport_type.equals(Constants.ALL_SPORTS)){
                String act_date_str = act.getStartDate();
                double act_total = 0;

                switch (unit){
                    case Constants.DISTANCE:
                        act_total = act.getDistance();
                        break;
                    case Constants.DURATION:
                        act_total = act.getElapsedTime();
                }

                LocalDate date = LocalDate.parse(act_date_str, formatter);
                if (date.equals(day)) {
                    total_for_the_day += act_total;
                }
            }
        }

        double new_total = 0;
        switch (unit){
            case Constants.DISTANCE:
                total_for_the_day = convertToKilometers(total_for_the_day);
                new_total = current_total + total_for_the_day;
                break;
            case Constants.DURATION:
                new_total = sumDuration(current_total, total_for_the_day);
                total_for_the_day = convertToHoursMinutes(total_for_the_day);
        }

        return_data.add(total_for_the_day);
        return_data.add(new_total);
        return return_data;
    }

    private double convertToHoursMinutes(double seconds){
        double hhmm;
        double hours  = (int) seconds/3600;
        double minutes = (int) (seconds - hours*3600)/60;
        hhmm = hours + minutes/100;
        return hhmm; //Return format is HH,MM
    }

    private double sumDuration(double d1, double d2){
        //d1 must be in hh,mm
        //d2 must be in seconds

        //Messy but working
        double hhmm = 0;
        String d1_as_string = String.valueOf(d1);
        String d1_minutes_as_string = d1_as_string.substring(d1_as_string.length()-2);
        double d1_minutes = Double.valueOf(d1_minutes_as_string);

        double hours  = (int) d2/3600;
        double minutes = (int) (d2 - hours*3600)/60;

        double sum_minutes = (d1_minutes+minutes)%60;
        hhmm = hours + (int) d1 + ((d1_minutes+minutes)-sum_minutes)/60 + sum_minutes/100;

        return hhmm;
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
        private final float total; //either hours or km

        public ConvenientReturnFormat(BarData first, ArrayList<String> second, int third, float fourth) {
            this.bardata = first;
            this.labels = second;
            this.today_index = third;
            this.total = fourth;
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

        public float getTotal() {
            return total;
        }
    }
}
