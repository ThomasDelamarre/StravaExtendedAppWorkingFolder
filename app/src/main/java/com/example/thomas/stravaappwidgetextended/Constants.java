package com.example.thomas.stravaappwidgetextended;

public class Constants {

    public static final String CLIENT_ID = "28523";
    public static final String CLIENT_SECRET = "e2a65906b1143b6449c34015ad2915e14aa56aa7";
    public static final String GRANT_TYPE_AUTH_CODE = "authorization_code";
    public static final String GRANT_TYPE_REFRESH = "refresh_token";

    public static final String STRAVA_URI = "https://www.strava.com/oauth/mobile/authorize";
    public static final String REDIRECT_URI = "https://com.example.thomas/oauthcallback";
    public static final String AUTH_URL = "https://www.strava.com/oauth/";
    public static final String BASE_URL = "https://www.strava.com/api/v3/";

    public static final String RESPONSE_TYPE_CODE = "code";
    public static final String APPROVAL_PROMPT_AUTO = "auto";
    public static final String APPROVAL_PROMPT_FORCE = "force";

    public static final String SCOPE_READ = "read";
    public static final String SCOPE_ACTIVITY_READ_ALL = "activity:read_all";
    public static final String SCOPE_PROFILE_READ_ALL = "profile:read_all";
    public static final String SCOPE_WRITE = "activity:write";

    public static final String ACCESS_TOKEN = "access token";
    public static final String REFRESH_TOKEN = "refresh token";
    public static final String EXPIRES_AT = "expires at";
    public static final String ACCESS_GRANTED = "has granted Strava access";
    public static final String AUTH_CODE = "auth code";

    public static final String SHARED_PREF_ADDRESS = "com.example.thomas.tokens";

    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public static final String SPORT_TYPE = "Sport type";
    public static final String RUN = "Run";
    public static final String RIDE = "Ride";
    public static final String VIRTUAL_RIDE = "VirtualRide";
    public static final String SWIM = "Swim";
    public static final String ALL_SPORTS = "AllSports";

    public static final String CURRENT_WEEK = "Current week";
    public static final String SINCE_DATE = "Since date";
    public static final String CUSTOM = "Last x days";
    public static final String CURRENT_MONTH = "Current month";

    public static final String DISPLAY_TYPE = "Display type";
    public static final String NUMBER_DAYS = "Number of days";
    public static final String START_DATE = "Start date";

    public static final String INITIAL_FETCH = "Initial fetch";

    public static final String UNIT = "Unit";
    public static final String DISTANCE = "Distance";
    public static final String DURATION = "Duration";

    public static final String LABELS = "Labels";
    public static final String ENABLED = "Enabled";
    public static final String DISABLED = "Disabled";
}
