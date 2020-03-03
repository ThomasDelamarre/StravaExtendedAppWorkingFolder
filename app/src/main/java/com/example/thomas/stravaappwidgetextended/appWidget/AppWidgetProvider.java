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

import com.example.thomas.stravaappwidgetextended.Constants;
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
    private static String ACTION_WIDGET_STRAVA = "Sta";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds){
        Log.e("OnDeleted", "True");
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context){
        Log.e("Onenabled", "True");
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.e("OnUpdate", "True");
        chart_manager = new ChartManager(context);
        sharedpref_manager = new SharedPrefManager(context);
        request_manager = new RequestManager(context);

        for (int widgetId : appWidgetIds) {
            String sport_type = sharedpref_manager.getSportType(); // Return All_sport by default
            String display_type = sharedpref_manager.getDisplayType();

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget);
            remoteViews.setTextViewText(R.id.display_type, display_type);

            setButtonsColors(context, remoteViews, sport_type);

            Intent intent = new Intent(context, AppWidgetProvider.class);
            intent.setAction(ACTION_WIDGET_REFRESH + Integer.toString(widgetId)); //Fix because 2 intent must have different actions
            intent.putExtra("ID", widgetId);
            PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.refresh, actionPendingIntent);

            intent = new Intent(context, AppWidgetProvider.class);
            intent.setAction(ACTION_WIDGET_SWIM + Integer.toString(widgetId));
            intent.putExtra("ID", widgetId);
            actionPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.swim_btn, actionPendingIntent);

            intent = new Intent(context, AppWidgetProvider.class);
            intent.setAction(ACTION_WIDGET_RIDE + Integer.toString(widgetId));
            intent.putExtra("ID", widgetId);
            actionPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.ride_btn, actionPendingIntent);

            intent = new Intent(context, AppWidgetProvider.class);
            intent.setAction(ACTION_WIDGET_RUN + Integer.toString(widgetId));
            intent.putExtra("ID", widgetId);
            actionPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.run_btn, actionPendingIntent);

            intent = new Intent(context, AppWidgetProvider.class);
            intent.putExtra("ID", widgetId);
            intent.setAction(ACTION_WIDGET_ALL + Integer.toString(widgetId));
            actionPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.all_btn, actionPendingIntent);

            intent = new Intent(context, AppWidgetProvider.class);
            intent.setAction(ACTION_WIDGET_STRAVA + Integer.toString(widgetId));
            actionPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.strava, actionPendingIntent);

            remoteViews.setImageViewBitmap(R.id.barchart, chart_manager.getBarChartInBitmap(sport_type));

            appWidgetManager.updateAppWidget(widgetId, remoteViews);

            Log.e("Id", Integer.toString(widgetId));
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("OnReceive", intent.getAction());
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget);
        int id=0;
        chart_manager = new ChartManager(context);
        sharedpref_manager = new SharedPrefManager(context);
        request_manager = new RequestManager(context);
        String sport_type;
        String intent_extract = intent.getAction().substring(0,3); //Fix because 2 intent must have different actions
        if (intent_extract.equals(ACTION_WIDGET_REFRESH)) {
            request_manager.fetchLast30Activities();
            sport_type = sharedpref_manager.getSportType();
            //faire attendre que le request manager finissse TODO
            remoteViews.setImageViewBitmap(R.id.barchart, chart_manager.getBarChartInBitmap(sport_type));
        } else if (intent_extract.equals(ACTION_WIDGET_SWIM)) {
            sport_type = Constants.SWIM;
            setButtonsColors(context, remoteViews, sport_type);
            remoteViews.setImageViewBitmap(R.id.barchart, chart_manager.getBarChartInBitmap(sport_type));
            sharedpref_manager.saveSportType(sport_type);
        } else if (intent_extract.equals(ACTION_WIDGET_RIDE)) {
            sport_type = Constants.RIDE;
            setButtonsColors(context, remoteViews, sport_type);
            remoteViews.setImageViewBitmap(R.id.barchart, chart_manager.getBarChartInBitmap(sport_type));
            sharedpref_manager.saveSportType(sport_type);
        } else if (intent_extract.equals(ACTION_WIDGET_RUN)) {
            sport_type = Constants.RUN;
            setButtonsColors(context, remoteViews, sport_type);
            remoteViews.setImageViewBitmap(R.id.barchart, chart_manager.getBarChartInBitmap(sport_type));
            sharedpref_manager.saveSportType(sport_type);
        } else if (intent_extract.equals(ACTION_WIDGET_ALL)) {
            sport_type = Constants.ALL_SPORTS;
            setButtonsColors(context, remoteViews, sport_type);
            remoteViews.setImageViewBitmap(R.id.barchart, chart_manager.getBarChartInBitmap(sport_type));
            sharedpref_manager.saveSportType(sport_type);
        } else if (intent_extract.equals(ACTION_WIDGET_STRAVA)) {
            openStravaApp(context);
        } else {
            super.onReceive(context, intent);
            Log.e("je suis bien la", "a chaque fois");
        }
        if (intent.hasExtra("ID")){
            id = intent.getIntExtra("ID", 0);
        }
        remoteViews.setTextViewText(R.id.display_type, sharedpref_manager.getDisplayType());
        super.onReceive(context, intent);
        appWidgetManager.updateAppWidget(id, remoteViews);
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

    private void setButtonsColors(Context context, RemoteViews remoteViews, String sport_type){
        int lighterStravaOrangeValue = ContextCompat.getColor(context, R.color.lighterStravaOrange);
        int lightStravaOrangeValue = ContextCompat.getColor(context, R.color.lightStravaOrange);

        //Set all the buttons to light orange
        remoteViews.setInt(R.id.ride_btn, "setBackgroundColor", lighterStravaOrangeValue);
        remoteViews.setInt(R.id.swim_btn, "setBackgroundColor", lighterStravaOrangeValue);
        remoteViews.setInt(R.id.run_btn, "setBackgroundColor", lighterStravaOrangeValue);
        remoteViews.setInt(R.id.all_btn, "setBackgroundColor", lighterStravaOrangeValue);


        //Set the selected button to strava Orange
        switch (sport_type){
            case Constants.SWIM:
                remoteViews.setInt(R.id.swim_btn, "setBackgroundColor", lightStravaOrangeValue);
                break;
            case Constants.RUN:
                remoteViews.setInt(R.id.run_btn, "setBackgroundColor", lightStravaOrangeValue);
                break;
            case Constants.RIDE:
                remoteViews.setInt(R.id.ride_btn, "setBackgroundColor", lightStravaOrangeValue);
                break;
            case Constants.ALL_SPORTS:
                remoteViews.setInt(R.id.all_btn, "setBackgroundColor", lightStravaOrangeValue);
                break;


        }
    }
}