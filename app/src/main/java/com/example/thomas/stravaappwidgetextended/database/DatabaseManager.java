package com.example.thomas.stravaappwidgetextended.database;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import com.example.thomas.stravaappwidgetextended.api.pojo.Activity;

public class DatabaseManager {

    private Context context;

    public DatabaseManager(Context context) {
        this.context = context;
    }

    public void dumpCurrentDbAndCreateEmptyOne () {
        StravaActivityDataSource data_source = new StravaActivityDataSource(this.context);
        data_source.open();
        data_source.createNewDatabase();
        data_source.close();
    }

    public void addActivitiesToDatabase(List<Activity> activities) {
        StravaActivityDataSource data_source = new StravaActivityDataSource(this.context);
        data_source.open();

        for (Activity act : activities) {

            //Remove time from date
            String date = act.getStartDate();
            act.setStartDate(date.substring(0, 10));

            if (data_source.exists(act.getId())) {
                Log.i(act.getName(), " - already in db\n");
            } else {
                data_source.insertActiviy(act);
                Log.i(act.getName(), " - added to db\n");
            }
        }

        data_source.close();
    }


    public List<Activity> getAllActivitiesFromDatabase() {
        StravaActivityDataSource data_source = new StravaActivityDataSource(this.context);
        data_source.open();

        List<Activity> activities_to_return;
        activities_to_return = data_source.getAllActivities();

        data_source.close();
        return activities_to_return;
    }

    public void removeActivityFromDatabase(Activity act) {
        StravaActivityDataSource data_source = new StravaActivityDataSource(this.context);
        data_source.open();

        data_source.deleteActivity(act);

        data_source.close();
    }

    public Activity getActivityByName(String name) {
        StravaActivityDataSource data_source = new StravaActivityDataSource(this.context);
        data_source.open();

        List<Activity> activities = data_source.getAllActivities();
        data_source.close();

        for (Activity act: activities) {
            if (act.getName().equals(name)) {
                return act;
            }
        }
        Log.e("Unable to find the activity", "db manager");
        return new Activity();
    }
}