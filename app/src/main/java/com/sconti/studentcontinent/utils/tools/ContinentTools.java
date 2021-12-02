package com.sconti.studentcontinent.utils.tools;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sconti.studentcontinent.ContinentApplication;
import com.sconti.studentcontinent.activity.outdoor.adapter.OutDoorTagsPrefAdapter;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorPostModel;
import com.sconti.studentcontinent.model.LikeDataModel;
import com.sconti.studentcontinent.model.LikeModel;
import com.sconti.studentcontinent.model.NewAPIResponseModel;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

public class ContinentTools {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int CAMERA_PERMISSION = 151;
    // List<OutDoorPostModel> TagDataModelList = new ArrayList<>();
    ;
    //private static final String TAG = "FirstPreferenceFragment";


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean hasInternetConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr != null) {
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) {
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }
        return false;

    }

    public static boolean checkPermissionCamera(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Camera permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean checkInternetStatus() {
        String status = "";//= NetworkUtil.getConnectivityStatusString(activity);
        boolean isNetworkAvailable = false;
        try {
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
        }
        return isNetworkAvailable;
    }

    public static void showToast(String message) {
        try {
            if ((message != null) && (!message.isEmpty())) {
                final Context context = ContinentApplication.getInstance().getBaseContext();
                final CharSequence text = message;
                final int duration = Toast.LENGTH_SHORT;
                Handler mainHandler = new Handler(context.getMainLooper());
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(context, "  " + text + "  ", duration);
                        toast.show();
                    }
                };
                mainHandler.post(myRunnable);
            }
        } catch (Exception e) {
        }
    }

    public void commonUpdateLikeCount(final OutDoorPostModel dataModel, final OutDoorTagsPrefAdapter PostAdapter, ApiInterface apiInterface, final List<OutDoorPostModel> TagDataModelList, int type, boolean isLiked) {

        final String uid = SharedPrefsHelper.getInstance().get(USER_ID, null);

        final LikeModel likeModel = new LikeModel();
        likeModel.setLikeByID(uid);
        if (isLiked) {
            likeModel.setLikeCount(dataModel.getNumLikes() + "");
            likeModel.setPostId(dataModel.getPostId());
            likeModel.setPostedById(dataModel.getPostedById());
            likeModel.setPostedByName(dataModel.getPostedByName());
            likeModel.setLikedByName(SharedPrefsHelper.getInstance().get(NAME));
            Call<NewAPIResponseModel> call = apiInterface.addLike(likeModel, type);
            call.enqueue(new Callback<NewAPIResponseModel>() {
                @Override
                public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                    if (response.body().isStatus()) {
                        if (TagDataModelList != null) {
                            for (int i = 0; i < TagDataModelList.size(); i++) {
                                if (TagDataModelList.get(i).getPostId().equalsIgnoreCase(dataModel.getPostId())) {
                                    TagDataModelList.get(i).setNumLikes(TagDataModelList.get(i).getNumLikes() + 1);
                                    LikeDataModel likeDataModel = new LikeDataModel();

                                    likeDataModel.setLikeByID(uid);
                                    likeDataModel.setLikeCount((dataModel.getNumLikes() + 1) + "");
                                    likeDataModel.setPostID(dataModel.getPostId());
                                    likeModel.setLikedByName(SharedPrefsHelper.getInstance().get(NAME));
                                    likeDataModel.setPostedById(dataModel.getPostedById());
                                    likeDataModel.setPostedByName(dataModel.getPostedByName());

                                    /*likeModel.setPostedById(dataModel.getPostedById());
                                    likeModel.setPostedByName(dataModel.getPostedByName());*/
                                    PostAdapter.notifyDataSetChanged();
                                    if (TagDataModelList.get(i).getLikesData() == null) {
                                        List<LikeDataModel> list = new ArrayList<>();
                                        list.add(likeDataModel);
                                        TagDataModelList.get(i).setLikesData(list);
                                    } else {

                                        TagDataModelList.get(i).getLikesData().add(likeDataModel);

                                    }
                                    break;
                                }
                                PostAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                    Log.d("", "onFailure: " + t.getLocalizedMessage());
                }
            });
        } else {
            likeModel.setLikeCount((dataModel.getNumLikes()) + "");
            likeModel.setPostId(dataModel.getPostId());
            likeModel.setPostedById(dataModel.getPostedById());
            likeModel.setPostedByName(dataModel.getPostedByName());
            likeModel.setLikedByName(SharedPrefsHelper.getInstance().get(NAME));
            Call<NewAPIResponseModel> call = apiInterface.DisLike(likeModel, type);
            call.enqueue(new Callback<NewAPIResponseModel>() {
                @Override
                public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                    for (int i = 0; i < TagDataModelList.size(); i++) {
                        if (TagDataModelList.get(i).getPostId().equalsIgnoreCase(dataModel.getPostId())) {
                            if (TagDataModelList.get(i).getNumLikes() > 0) {
                                TagDataModelList.get(i).setNumLikes(TagDataModelList.get(i).getNumLikes() - 1);

                            }
                        }
                    }
                    PostAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {

                }
            });

        }


    }


    public static long getDateInMillis(String srcDate) {
        String timezoneID = TimeZone.getDefault().getID();
        SimpleDateFormat desiredFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        desiredFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        System.out.println("timezone/" + timezoneID);
        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }
    public static boolean is_word(String s) {
        return (s.trim().length() > 0 && s.trim().split("\\s+").length == 1);
    }
    public static boolean youtubeURl(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile(
                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()){
            return true;
        }
        return false;
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