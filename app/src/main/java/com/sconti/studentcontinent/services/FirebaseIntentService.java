package com.sconti.studentcontinent.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * outdooricon service on outdooricon separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FirebaseIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String TAG = "FirebaseIntentService";
    private static final String UPDATE_LIKE_ = "com.sconti.studentcontinent.services.action.LIKE";
    private static final String UPDATE_SHARE = "com.sconti.studentcontinent.services.action.SHARE";
    private static final String UPDATE_COMMENT = "com.sconti.studentcontinent.services.action.COMMENT";


    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.sconti.studentcontinent.services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.sconti.studentcontinent.services.extra.PARAM2";

    public FirebaseIntentService() {
        super("FirebaseIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing outdooricon task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, FirebaseIntentService.class);
        intent.setAction(UPDATE_LIKE_);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing outdooricon task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, FirebaseIntentService.class);
        intent.setAction(UPDATE_SHARE);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (UPDATE_LIKE_.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleUpdateLikes(param1, param2);
            } else if (UPDATE_SHARE.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleUpdateShare(param1, param2);
            }

        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleUpdateLikes(String param1, String param2) {
        Log.d(TAG, "handleUpdateLikes: ");

    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleUpdateShare(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
