package com.sconti.studentcontinent.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.FCM_TOKEN;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.NAME;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseInstanceIDSer";

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
       // sendRegistrationToServer(refreshedToken);
    }

    private void storeRegIdInPref(String token) {
        SharedPrefsHelper.getInstance().save(FCM_TOKEN, token);
        if(SharedPrefsHelper.getInstance().get(NAME, null)!=null)
        {
           /* DatabaseReference databaseReference;
            databaseReference = HamsaApplication.getFireBaseRef();
            UserDetails details = new UserDetails();
            details.setName(SharedPrefsHelper.getInstance().get(NAME, "null"));
            details.setEmail(SharedPrefsHelper.getInstance().get(EMAIL, "null"));
            details.setMobile(SharedPrefsHelper.getInstance().get(MOBILE, "null"));
            details.setvillage(SharedPrefsHelper.getInstance().get(VILLAGE, "null"));
            details.setToken(token);
            databaseReference.child(SharedPrefsHelper.getInstance().get(MOBILE, "null")).setValue(details);*/
        }
    }
}
