package com.example.thomas.stravaappwidgetextended.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.database.SQLException;
import android.util.Log;

import com.example.thomas.stravaappwidgetextended.api.pojo.Activity;

import java.util.ArrayList;
import java.util.List;

public class StravaActivityDataSource {

    public static final String TAG = "StravaActivityDataSource";

    // Database fields
    private SQLiteDatabase database;
    private StravaActivitySQLiteHelper dbHelper;
    private String[] allColumns = { StravaActivitySQLiteHelper.COLUMN_ID,
            StravaActivitySQLiteHelper.COLUMN_TITLE,
            StravaActivitySQLiteHelper.COLUMN_DATE,
            StravaActivitySQLiteHelper.COLUMN_ACT_TYPE,
            StravaActivitySQLiteHelper.COLUMN_DISTANCE,
            StravaActivitySQLiteHelper.COLUMN_ELAPSED_TIME };

    public StravaActivityDataSource(Context context) {
        dbHelper = new StravaActivitySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createNewDatabase(){
        dbHelper.onUpgrade(database, 0, 1); //I believe number of version have no impact
    }

    public void insertActiviy(Activity act) {
        ContentValues values = new ContentValues();
        values.put(StravaActivitySQLiteHelper.COLUMN_ID, act.getId());
        values.put(StravaActivitySQLiteHelper.COLUMN_TITLE, act.getName());
        values.put(StravaActivitySQLiteHelper.COLUMN_DATE, act.getStartDate());
        values.put(StravaActivitySQLiteHelper.COLUMN_ACT_TYPE, act.getType());
        values.put(StravaActivitySQLiteHelper.COLUMN_DISTANCE, act.getDistance());
        values.put(StravaActivitySQLiteHelper.COLUMN_ELAPSED_TIME, act.getElapsedTime());
        long insert = database.insert(StravaActivitySQLiteHelper.TABLE_ACTIVITIES, null, values);
        Log.e("Activity inserted", Long.toString(insert));
    }

    public void deleteActivity(Activity act) {
        long id = act.getId();
        String name = act.getName();
        System.out.println("Activity removed. Name: " + name + "Id: " + id);
        database.delete(StravaActivitySQLiteHelper.TABLE_ACTIVITIES, StravaActivitySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Activity> getAllActivities() {
        List<Activity> activities = new ArrayList<Activity>();

        Cursor cursor = database.query(StravaActivitySQLiteHelper.TABLE_ACTIVITIES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Activity act= cursorToActivity(cursor);
            activities.add(act);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return activities;
    }

    private Activity cursorToActivity(Cursor cursor) {
        Activity act = new Activity();
        act.setId(cursor.getLong(0));
        act.setName(cursor.getString(1));
        act.setStartDate(cursor.getString(2));
        act.setType(cursor.getString(3));
        act.setDistance(cursor.getDouble(4));
        act.setElapsedTime(cursor.getLong(5));
        return act;
    }

    public boolean exists(long id) {
        if (database == null) return false;
        Cursor cursor = database.rawQuery("select 1 from activities where _id=?",
                new String[] {(Long.toString(id))});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}
