package com.sconti.studentcontinent.backendServices;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.sconti.studentcontinent.model.APIResponseModel;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.provider.ContactsContract.Intents.Insert.PHONE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.COLLEGE_NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.MOBILE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_ASPIRANT;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_DEGREE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_ASPIRATION;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * outdooricon service on outdooricon separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BackgroundService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_ASPIRATION = "com.sconti.studentcontinent.backendServices.action.ASPIRATION";
    private static final String ACTION_DEGREE = "com.sconti.studentcontinent.backendServices.action.DEGREE";
    private static final String ACTION_COLLEGE = "com.sconti.studentcontinent.backendServices.action.COLLEGE";


    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.sconti.studentcontinent.backendServices.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.sconti.studentcontinent.backendServices.extra.PARAM2";

    public BackgroundService() {
        super("BackgroundService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing outdooricon task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionAspiration(Context context) {
        Intent intent = new Intent(context, BackgroundService.class);
        intent.setAction(ACTION_ASPIRATION);
       // intent.putExtra(EXTRA_PARAM1, param1);
        //intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing outdooricon task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionDegree(Context context) {
        Intent intent = new Intent(context, BackgroundService.class);
        intent.setAction(ACTION_DEGREE);
       // intent.putExtra(EXTRA_PARAM1, param1);
       // intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void startActionCollege(Context context) {
        Intent intent = new Intent(context, BackgroundService.class);
        intent.setAction(ACTION_COLLEGE);
        // intent.putExtra(EXTRA_PARAM1, param1);
        // intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            setUpNetwork();
            final String action = intent.getAction();
            if (ACTION_ASPIRATION.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionASPIRATION(param1, param2);
            } else if (ACTION_DEGREE.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionDegree(param1, param2);
            }
            else if (ACTION_COLLEGE.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionCollege(param1, param2);
            }
        }
    }

    protected ApiInterface apiInterface;
    private void setUpNetwork()
    {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionASPIRATION(String param1, String param2) {
        Call<APIResponseModel> call = apiInterface.updateAspiration(SharedPrefsHelper.getInstance().get(MOBILE).toString(), SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_ASPIRATION).toString());
        call.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {

            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {

            }
        });
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDegree(String param1, String param2) {
        // TODO: Handle action Baz
       Call<APIResponseModel> call = apiInterface.updateDegree(SharedPrefsHelper.getInstance().get(MOBILE).toString(), SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE).toString());
       call.enqueue(new Callback<APIResponseModel>() {
           @Override
           public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {

           }

           @Override
           public void onFailure(Call<APIResponseModel> call, Throwable t) {

           }
       });
    }

    private void handleActionCollege(String param1, String param2) {
        // TODO: Handle action Baz
        Call<APIResponseModel> call = apiInterface.updateCollege(SharedPrefsHelper.getInstance().get(MOBILE).toString(), SharedPrefsHelper.getInstance().get(COLLEGE_NAME).toString());
        call.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {

            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {

            }
        });
    }
}
