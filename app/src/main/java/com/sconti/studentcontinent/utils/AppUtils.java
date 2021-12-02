package com.sconti.studentcontinent.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static void showSnackBar(Activity context, String message)
    {
        Snackbar.make(context.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
    public static void showToastMessage(Activity context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        //Snackbar.make(context.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
    public class Constants{
        public static final long ONE_SECOND = 1000;
        public static final long TWO_SECOND = 2000;
        public static final long THREE_SECOND = 3000;
        public static final long FOUR_SECOND = 4000;
        public static final long TEN_SECOND = 10000;
        public static final long EIGHT_SECOND = 8000;


        public static final String PLACES = "PLACES";
        public static final String ADDS = "ADDS";
        public static final String LAT = "LAT";
        public static final String LON = "LON";
        public static final String RESTAURANT = "restaurant";
        public static final String HOTEL = "lodging";
        public static final String HOSPITAL = "hospital";
        public static final String NAME ="name";
        public static final String EMAIL = "email";
        public static final String MOBILE = "mobile";
        public static final String SELECTED_SUBJECT_ASPIRATION = "selected_subject_aspiration";
        public static final String SELECTED_SUBJECT_ASPIRATION_NAME = "selected_subject_aspiration_name";

        public static final String SELECTED_SUBJECT_KNOWLEDGE = "selected_subject_knowledge";
        public static final String SELECTED_SUBJECT_KNOWLEDGE_NAME = "selected_subject_knowledge_name";

        public static final String SELECTED_SUBJECT_OUTDOOR = "selected_subject_outdoor";
        public static final String SELECTED_SUBJECT_GAME = "selected_subject_game";
        public static final String COLLEGE_NAME = "college_name";


        public static final String SUBJECT_LIST_KNOWLEDGE = "subject_list_knowledge";
        public static final String SUBJECT_LIST_GAMES = "subject_list_games";
        public static final String SUBJECT_LIST_OUTDOOR = "subject_list_outdoor";
        public static final String SUBJECT_LIST_ASPIRATION = "subject_list_aspiartion";


        public static final String OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";
        public static final String ZERO_RESULTS = "ZERO_RESULTS";
        public static final String OK = "OK";

        public static final String FIRST_TIME = "first";
        public static final String SELECTED_LANG = "language";
        public static final String FCM_TOKEN = "fcm_token";
        public static final String VILLAGE = "village";
        public static final String IS_FIRST_TIME ="first_time";
        public static final String ASPIRATION_FIRST = "aspiration_first";
        public static final String KNOWLEDGE_FIRST = "knowledge_first";
        public static final String KNOWLEDGE_FIRST_GENERAL = "knowledge_first_general";
        public static final String ASPIRATION_FIRST_GENERAL = "aspiration_first_general";

        public static final String GAMES_FIRST = "games_first";
        public static final String OUTDOOR_FIRST = "outdoor_first";
        public static final String LIKE_NODE = "numLikes";
        public static final String SHARE_NODE = "numShares";
        public static final String COMMENT_NODE = "numComments";
        public static final String USER_COMMENTS = "userComments";
        public static final String USER_FEEDS = "userFeeds";
        public static final String USER_RESPONSES = "userResponses";
        public static final String USER_ID = "userid";
        public static final String PROFILE = "profile";
        public static final String PROFILE_URL = "profileURL";
        public static final String USER_IMAGE = "user_image";
        public static final String BANNER_URL = "banner_url";

        public static final int QUERY_LIMIT = 21;
        public static final String SELECTED_DEGREE = "selectedDegree" ;
        public static final String SELECTED_ASPIRANT = "interest";
        public static final String NOTIFICATION_DATA = "NOTIFICATION_DATA";
        public static final String NOTIFICATION_DETAILS = "NOTIFICATION_DETAILS";
        public static final String DATA_OF_COMMENT_AND_SLNO = "DATA_OF_COMMENT_AND_SLNO";
        public static final String DATA_OF_COMMENT_AND_SLNO2 = "DATA_OF_COMMENT_AND_SLNO2";
        public static final String DATA_OF_COMMENT_AND_SLNO3 = "DATA_OF_COMMENT_AND_SLNO3";
        public static final String DATA_OF_COMMENT_AND_SLNO4 = "DATA_OF_COMMENT_AND_SLNO4";
        public static final String DATA_OF_COMMENT_AND_SLNO5 = "DATA_OF_COMMENT_AND_SLNO5";
        public static final String DELETE_COMMENT_COUNT="DELETE_COMMENT_COUNT";
        public static final String TRENDING_CATEGORY="TRENDING_CATEGORY";

        public static final String KNOWLEDGE_POST_1 = "KNOWLEDGE_POST_1";
        public static final String ASPIRATION_FIRST_POST_1 = "ASPIRATION_FIRST_POST_1";
        public static final String KNOWLEDGE_FIRST_GENERAL_POST_1 = "KNOWLEDGE_FIRST_GENERAL_POST_1";
        public static final String ASPIRATION_FIRST_GENERAL_POST_1 = "ASPIRATION_FIRST_GENERAL_POST_1";
        public static final String OUTDOOR_POST_1="OUTDOOR_POST_1";
        public static final String UPDATE_KNOWLEDGE_POST_1 = "UPDATE_KNOWLEDGE_POST_1";
        public static final String VIDEO_LIST = "VIDEO_LIST";

        public static final String GIF_KEY ="564ce7370bf347f2b7c0e4746593c179";


        public static final String IS_COMING_FROM_EXT_APP = "IS_COMING_FROM_EXT_APP";
    }
    public static boolean checkInternetStatus() {
        String status ="";//= NetworkUtil.getConnectivityStatusString(activity);
        boolean isNetworkAvailable = true;
        //return isNetworkAvailable;
       /* try {
            switch (status) {
                case "Connected to Internet with Mobile Data":
                    isNetworkAvailable = true;
                    break;
                case "Connected to Internet with WIFI":
                    isNetworkAvailable = true;
                    break;
                default:
                    isNetworkAvailable = false;
                    break;
            }
            //  showSnackBarBasedonStatus(isNetworkAvailable);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return isNetworkAvailable;
    }

    public static String extractYTId(String ytUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(ytUrl);
        if(matcher.find()){
            return matcher.group();
        }
        else {
            return null;
        }
    }
}
