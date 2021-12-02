package com.sconti.studentcontinent;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContinentApplication extends Application {
    private static ContinentApplication mInstance;
    private static DatabaseReference databaseReference;
    private LeastRecentlyUsedCacheEvictor leastRecentlyUsedCacheEvictor;
    private ExoDatabaseProvider exoDatabaseProvider;
    private long exoPlayerCacheSize = 1024 * 1024 * 1024;
    public SimpleCache simpleCache;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        if (leastRecentlyUsedCacheEvictor == null) {
            leastRecentlyUsedCacheEvictor = new LeastRecentlyUsedCacheEvictor(exoPlayerCacheSize);
        }

        if (exoDatabaseProvider != null) {
            exoDatabaseProvider = new ExoDatabaseProvider(this);
        }

        if (simpleCache == null) {
            simpleCache = new SimpleCache(getCacheDir(), leastRecentlyUsedCacheEvictor, exoDatabaseProvider);
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        FirebaseApp.initializeApp(this);
        if(FirebaseApp.getInstance()!=null){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        // ApiClient.intialise();
       /* if(isDeve()) {
            ApiClient.setBaseUrl(AppConstants.appBaseUrlDev);
        }
        else
        {
            ApiClient.setBaseUrl(AppConstants.appBaseUrl);

        }*/

    }
    public static synchronized DatabaseReference getFireBaseRef()
    {
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        if(BuildConfig.DEBUG) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("test");
        }
        else {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("prod");
        }
        return databaseReference;
    }

    public static synchronized ContinentApplication getInstance() {
        return mInstance;
    }

}