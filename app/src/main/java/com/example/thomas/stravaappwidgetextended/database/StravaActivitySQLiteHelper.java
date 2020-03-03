package com.example.thomas.stravaappwidgetextended.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StravaActivitySQLiteHelper extends SQLiteOpenHelper {

    public static final String TAG = "MySQLiteHelper";

    public static final String TABLE_ACTIVITIES = "activities";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ACT_TYPE = "activity_type";
    public static final String COLUMN_DISTANCE = "distance_meters";
    public static final String COLUMN_MOVING_TIME = "moving_time_seconds";


    private static final String DATABASE_NAME = "activities.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_ACTIVITIES + " ("
            + COLUMN_ID + " strava activity id, "
            + COLUMN_TITLE  + " activity name, "
            + COLUMN_DATE + " activity date, "
            + COLUMN_ACT_TYPE + " activity type, "
            + COLUMN_DISTANCE + " activity distance meters, "
            + COLUMN_MOVING_TIME + " acitivty duration seconds);";

    public StravaActivitySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(StravaActivitySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITIES);
        onCreate(db);
    }
}

