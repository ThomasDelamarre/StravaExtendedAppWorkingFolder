package com.example.thomas.stravaappwidgetextended.appWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.thomas.stravaappwidgetextended.Constants;
import com.example.thomas.stravaappwidgetextended.graph.ChartManager;
import com.example.thomas.stravaappwidgetextended.ParametersActivity;
import com.example.thomas.stravaappwidgetextended.R;
import com.example.thomas.stravaappwidgetextended.api.requestor.RequestManager;
import com.example.thomas.stravaappwidgetextended.sharedPreferences.SharedPrefManager;

import java.time.LocalDate;

public class AppWidgetProvider extends android.appwidget.AppWidgetProvider {

    private ChartManager chart_manager;
    private SharedPrefManager sharedpref_manager;
    private RequestManager request_manager;

    private static String ACTION_WIDGET_REFRESH = "Rfs";
    private static String ACTION_WIDGET_RUN = "Run";
    private static String ACTION_WIDGET_HT = "Htr";
    private static String ACTION_WIDGET_SWIM = "Swm";
    private static String ACTION_WIDGET_RIDE = "Rde";
    private static String ACTION_WIDGET_ALL = "All";
    private static String ACTION_WIDGET_OPEN_STRAVA = "Sta";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds){
        Log.i("OnDeleted", "True");
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context){
        Log.i("OnEnabled", "True");
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i("OnUpdate", "True");

        chart_manager = new ChartManager(context);
        sharedpref_manager = new SharedPrefManager(context);
        request_manager = new RequestManager(context);

        for (int widgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget);
            updateWidget(context, remoteViews, widgetId, Boolean.TRUE);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("OnReceive", intent.getAction());

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget);

        chart_manager = new ChartManager(context);
        sharedpref_manager = new SharedPrefManager(context);
        request_manager = new RequestManager(context);

        int id=0;

        String intent_extract = intent.getAction().substring(0,3); //Fix because 2 intent must have different actions

        String sport_type = sharedpref_manager.getSportType();

        if (intent_extract.equals(ACTION_WIDGET_REFRESH)) {
            request_manager.fetchLast30Activities();
        } else if (intent_extract.equals(ACTION_WIDGET_SWIM)) {
            sport_type = Constants.SWIM;
        } else if (intent_extract.equals(ACTION_WIDGET_RIDE)) {
            sport_type = Constants.RIDE;
        } else if (intent_extract.equals(ACTION_WIDGET_RUN)) {
            sport_type = Constants.RUN;
        } else if (intent_extract.equals(ACTION_WIDGET_ALL)) {
            sport_type = Constants.ALL_SPORTS;
        } else if (intent_extract.equals(ACTION_WIDGET_HT)) {
            sport_type = Constants.VIRTUAL_RIDE;
        } else if (intent_extract.equals(ACTION_WIDGET_OPEN_STRAVA)) {
            openStravaApp(context);
        } else {
            super.onReceive(context, intent);
        }

        sharedpref_manager.saveSportType(sport_type);

        if (intent.hasExtra("ID")){
            id = intent.getIntExtra("ID", 0);
            updateWidget(context, remoteViews, id, Boolean.TRUE);
        }

        appWidgetManager.updateAppWidget(id, remoteViews);
    }

    private void updateWidget(Context context, RemoteViews remoteViews, int widgetId, Boolean set_intents){
        if (set_intents) {
            setIntents(context, remoteViews, widgetId);
        }
        updateButtons(context, remoteViews);
        setDisplayType(context, remoteViews);
        remoteViews.setImageViewBitmap(R.id.barchart, chart_manager.getBarChartInBitmap(sharedpref_manager.getSportType()));
        updateTotal(context, remoteViews); //Must be done AFTER getChart
    }

    private void updateTotal(Context context, RemoteViews remoteViews){

        String unit = sharedpref_manager.getUnit();

        switch (unit){
            case Constants.DISTANCE:
                remoteViews.setTextViewText(R.id.x_km_hour, parseDistance(chart_manager.getTotal()));
                remoteViews.setTextViewText(R.id.x_unit, "km");
                remoteViews.setViewVisibility(R.id.x_minutes, View.GONE);
                break;
            case Constants.DURATION:
                int[] duration = parseDuration(chart_manager.getTotal());
                remoteViews.setTextViewText(R.id.x_km_hour, Integer.toString(duration[0]));
                remoteViews.setTextViewText(R.id.x_unit, "h");
                remoteViews.setViewVisibility(R.id.x_minutes, View.VISIBLE);
                if (duration[1] < 10){ remoteViews.setTextViewText(R.id.x_minutes, "0"+Integer.toString(duration[1]));}
                else { remoteViews.setTextViewText(R.id.x_minutes, Integer.toString(duration[1]));}
                break;
        }
    }

    private void setDisplayType(Context context, RemoteViews remoteViews){
        String display_type = sharedpref_manager.getDisplayType();
        if (display_type.equals(Constants.CURRENT_MONTH) || display_type.equals(Constants.CURRENT_WEEK)){
            remoteViews.setTextViewText(R.id.display_type, display_type);
        } else if (display_type.equals(Constants.CUSTOM)){
            String number_days = Integer.toString(sharedpref_manager.getNumberDays());
            remoteViews.setTextViewText(R.id.display_type, "Last " + number_days + " days");
        } else if (display_type.equals(Constants.SINCE_DATE)){
            LocalDate date = sharedpref_manager.getStartDate();
            remoteViews.setTextViewText(R.id.display_type, "Since " + date.getDayOfMonth() + " " + date.getMonth().toString());
        }
    }

    private void setIntents(Context context, RemoteViews remoteViews, int widgetId){

        //Intent to open our app
        Intent intent = new Intent(context, ParametersActivity.class);
        intent.putExtra("ID", widgetId);
        PendingIntent actionPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.redirect_to_app, actionPendingIntent);

        //Below are all PendingIntents referring to this class
        remoteViews.setOnClickPendingIntent(R.id.refresh, createBroadcastIntent(context, ACTION_WIDGET_REFRESH, widgetId));
        remoteViews.setOnClickPendingIntent(R.id.swim_btn, createBroadcastIntent(context, ACTION_WIDGET_SWIM, widgetId));
        remoteViews.setOnClickPendingIntent(R.id.ride_btn, createBroadcastIntent(context, ACTION_WIDGET_RIDE, widgetId));
        remoteViews.setOnClickPendingIntent(R.id.run_btn, createBroadcastIntent(context, ACTION_WIDGET_RUN, widgetId));
        remoteViews.setOnClickPendingIntent(R.id.ht_btn, createBroadcastIntent(context, ACTION_WIDGET_HT, widgetId));
        remoteViews.setOnClickPendingIntent(R.id.all_btn, createBroadcastIntent(context, ACTION_WIDGET_ALL, widgetId));
        remoteViews.setOnClickPendingIntent(R.id.strava, createBroadcastIntent(context, ACTION_WIDGET_OPEN_STRAVA, widgetId));
    }

    private PendingIntent createBroadcastIntent(Context context, String action, int id){
        Intent intent = new Intent(context, AppWidgetProvider.class);
        intent.setAction(action + Integer.toString(id));
        intent.putExtra("ID", id);
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return actionPendingIntent;
    }

    private void updateButtons(Context context, RemoteViews remoteViews){
        int white = ContextCompat.getColor(context, R.color.white);
        int black = ContextCompat.getColor(context, R.color.black);

        String sport_type = sharedpref_manager.getSportType();

        //Set all the buttons to dark tint
        remoteViews.setInt(R.id.ride_btn, "setColorFilter", black);
        remoteViews.setInt(R.id.swim_btn, "setColorFilter", black);
        remoteViews.setInt(R.id.run_btn, "setColorFilter", black);
        remoteViews.setInt(R.id.all_btn, "setColorFilter", black);
        remoteViews.setInt(R.id.ht_btn, "setColorFilter", black);


        //Set the selected button to white tint
        switch (sport_type){
            case Constants.SWIM:
                remoteViews.setInt(R.id.swim_btn, "setColorFilter", white);
                break;
            case Constants.RUN:
                remoteViews.setInt(R.id.run_btn, "setColorFilter", white);
                break;
            case Constants.RIDE:
                remoteViews.setInt(R.id.ride_btn, "setColorFilter", white);
                break;
            case Constants.ALL_SPORTS:
                remoteViews.setInt(R.id.all_btn, "setColorFilter", white);
                break;
            case Constants.VIRTUAL_RIDE:
                remoteViews.setInt(R.id.ht_btn, "setColorFilter", white);
                break;
        }
    }

    private String parseDistance(float distance){
        String distance_str = Float.toString(distance);
        distance_str = distance_str.replace(".",",");
        return distance_str;
    }

    private int[] parseDuration(float duration){
        int[] return_data = new int[2];
        return_data[0] = (int) duration;
        return_data[1] = Math.round((duration-return_data[0])*100);
        return return_data;
    }

    private void openStravaApp(Context context){
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage("com.strava");
        try {
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            Log.i("App Strava non installée", e.toString());
        }
    }
}