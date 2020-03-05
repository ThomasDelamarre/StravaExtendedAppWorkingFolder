package com.example.thomas.stravaappwidgetextended.appWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.thomas.stravaappwidgetextended.Constants;
import com.example.thomas.stravaappwidgetextended.ParametersActivity;
import com.example.thomas.stravaappwidgetextended.R;
import com.example.thomas.stravaappwidgetextended.api.requestor.RequestManager;
import com.example.thomas.stravaappwidgetextended.sharedPreferences.SharedPrefManager;

public class AppWidgetProvider extends android.appwidget.AppWidgetProvider {

    private ChartManager chart_manager;
    private SharedPrefManager sharedpref_manager;
    private RequestManager request_manager;

    private static String ACTION_WIDGET_REFRESH = "Rfs";
    private static String ACTION_WIDGET_RUN = "Run";
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

        Toast toast = Toast.makeText(context,  "Appwidget updated", Toast.LENGTH_LONG);
        toast.show();

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

        Toast toast = Toast.makeText(context,  "Intent received", Toast.LENGTH_SHORT);
        toast.show();

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
            //faire attendre que le request manager finissse TODO pas sur que besoin
        } else if (intent_extract.equals(ACTION_WIDGET_SWIM)) {
            sport_type = Constants.SWIM;
        } else if (intent_extract.equals(ACTION_WIDGET_RIDE)) {
            sport_type = Constants.RIDE;
        } else if (intent_extract.equals(ACTION_WIDGET_RUN)) {
            sport_type = Constants.RUN;
        } else if (intent_extract.equals(ACTION_WIDGET_ALL)) {
            sport_type = Constants.ALL_SPORTS;
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
        remoteViews.setImageViewBitmap(R.id.barchart, chart_manager.getBarChartInBitmap(sharedpref_manager.getSportType()));
        remoteViews.setTextViewText(R.id.x_km, parseDistance(chart_manager.getTotalDistance())); //Must be done AFTER getChart
        remoteViews.setTextViewText(R.id.display_type, sharedpref_manager.getDisplayType());
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
        }
    }

    private String parseDistance(float distance){
        String distance_str = Float.toString(distance);
        distance_str = distance_str.replace(".",",");
        return distance_str;
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