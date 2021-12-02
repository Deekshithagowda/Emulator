package com.sconti.studentcontinent.activity.newlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.MainActivity;
import com.sconti.studentcontinent.activity.alltabs.SecondMainActivity;
import com.sconti.studentcontinent.activity.starterscreen.CollegeSelectionActivity;
import com.sconti.studentcontinent.activity.starterscreen.StarterActivity;
import com.sconti.studentcontinent.activity.starterscreen.WhatYouWantActivity;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.model.APIResponseModel;
import com.sconti.studentcontinent.model.UserDetails;
import com.sconti.studentcontinent.utils.AppUtils;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.COLLEGE_NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.EMAIL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.FIRST_TIME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.MOBILE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.PROFILE_URL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_ASPIRANT;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_ASPIRATION;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";

    private EditText mEditTextEmail, mEditTextPassword;
    private String email, password;
    private CircularProgressButton mButtonLogin;
    private ImageView mImageViewFacebook, mImageViewGoogle;
    private CallbackManager callbackManager;
    private com.facebook.login.widget.LoginButton loginButton;

    @Override
    protected int getContentView() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        mEditTextEmail = findViewById(R.id.editTextEmail);
        mEditTextPassword = findViewById(R.id.editTextPassword);
        mButtonLogin = findViewById(R.id.cirLoginButton);
        mImageViewFacebook = findViewById(R.id.facebook_login);
        mImageViewGoogle = findViewById(R.id.google_login);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateEmail();
            }
        });
        mImageViewGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initGoogleSignIn();
            }
        });

        mImageViewFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initFaceBook();
            }
        });
    }
    private void initFaceBook(){
        callbackManager = CallbackManager.Factory.create();
        loginButton = new com.facebook.login.widget.LoginButton(LoginActivity.this);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginResult.getAccessToken().getUserId();
                Log.d(TAG, "onSuccess facebook userId: "+loginResult.getAccessToken().getUserId());
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                try {
                                    String id = object.getString("id"); // 01/31/1980 format
                                    String name = object.getString("name"); // 01/31/1980 format
                                    String profile_pic = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                    String email = object.getString("email");
                                    UserDetails userDetails = new UserDetails();
                                    userDetails.setEmail(email);
                                    userDetails.setName(name);
                                    userDetails.setUserId(id);
                                    userDetails.setPhone(String.valueOf(System.currentTimeMillis()).substring(0,9));
                                    userDetails.setPwd("facebook");
                                    String picURL = "https://graph.facebook.com/{userid}/picture?type=square";
                                    picURL = picURL.replace("{userid}", id);
                                    userDetails.setImageURL(picURL);
                                    checkLoginDetails(id, "facebook", 3);
                                    // callNetworkAPI(userDetails);
                                    //registeredSuccess(userDetails);

                                    //SharedPrefsHelper.getInstance().save(USER_ID, id);
                                    // saveAndUpdateData(userDetails);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email, picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "onError: "+exception.getLocalizedMessage());
                // App code
            }
        });
        loginButton.performClick();
    }

    private void initGoogleSignIn(){
        SignInButton signInButton = new SignInButton(LoginActivity.this);
        googleSignIn();
        signIn();

    }
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButton;
    private void googleSignIn()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);
    }

    private int RC_SIGN_IN = 1111;
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void validateEmail(){
        email = mEditTextEmail.getText().toString();
        password = mEditTextPassword.getText().toString();
        if (password.length() < 2) {
            AppUtils.showToastMessage(LoginActivity.this, getString(R.string.correct_pwd));
            return;
        }
        if (email.length() == 0) {
            AppUtils.showToastMessage(LoginActivity.this, getString(R.string.email_correct));
            return;
        }
        checkLoginDetails(email, password, 2);
    }
    private void checkLoginDetails(String id, String pwd, int type) {
        if(AppUtils.checkInternetStatus()) {
            Call<APIResponseModel> apiResponseModelCall = apiInterface.verifyUser(id, pwd, type);
            apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
                @Override
                public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                    if (response.isSuccessful() && response.body().isStatus()) {
                        loginSuccess(response.body());
                    } else {
                        AppUtils.showToastMessage(LoginActivity.this, getString(R.string.user_not_registered));
                    }
                }

                @Override
                public void onFailure(Call<APIResponseModel> call, Throwable t) {
                    AppUtils.showToastMessage(LoginActivity.this, getString(R.string.some_thing_wrong));
                }
            });
        }
        else{
            AppUtils.showToastMessage(LoginActivity.this, getString(R.string.no_inetrnet));
        }
    }

    private void loginSuccess(APIResponseModel userDetails) {
        SharedPrefsHelper.getInstance().save(FIRST_TIME, false);
        SharedPrefsHelper.getInstance().save(NAME, userDetails.getName());
        SharedPrefsHelper.getInstance().save(EMAIL, userDetails.getEmail());
        SharedPrefsHelper.getInstance().save(MOBILE, userDetails.getPhone());
        SharedPrefsHelper.getInstance().save(PROFILE_URL, userDetails.getImageURL());
        SharedPrefsHelper.getInstance().save(USER_ID, userDetails.getId());
        if(userDetails.getSelectedDegree()!=null )
        {
            SharedPrefsHelper.getInstance().save(AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE, userDetails.getSelectedDegree());
        }
        if(userDetails.getInterest()!=null)
        {
            SharedPrefsHelper.getInstance().save(SELECTED_SUBJECT_ASPIRATION, userDetails.getInterest());
        }

        if(userDetails.getVillage()!=null )
        {
            SharedPrefsHelper.getInstance().save(COLLEGE_NAME, userDetails.getVillage());
        }

        if(userDetails.getSelectedDegree()==null && userDetails.getInterest()==null )
        {
            startActivity(new Intent(activity, StarterActivity.class));
        }
        else if(userDetails.getVillage()==null)
        {
            startActivity(new Intent(activity, CollegeSelectionActivity.class));
        }
        else if(userDetails.getSelectedDegree()!=null && userDetails.getInterest()==null)
        {
            startActivity(new Intent(activity, WhatYouWantActivity.class));
        }
        else if(userDetails.getSelectedDegree()==null && userDetails.getInterest()!=null)
        {
            startActivity(new Intent(activity, StarterActivity.class));
        }
        else
        {
            startActivity(new Intent(activity, SecondMainActivity.class));
        }

        finish();
    }

    public void onLoginClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
        //overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(callbackManager!=null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // outdooricon listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            UserDetails userDetails = new UserDetails();
            userDetails.setEmail(account.getEmail());
            userDetails.setName(account.getDisplayName());
            userDetails.setUserId(account.getId());
            userDetails.setPwd("gmail");
            if(account.getPhotoUrl()!=null)
            {
                userDetails.setImageURL(account.getPhotoUrl().toString());
            }
            userDetails.setPhone(String.valueOf(System.currentTimeMillis()).substring(0,9));
            checkLoginDetails(account.getEmail(), "gmail", 2);
            //callNetworkAPI(userDetails);
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            // updateUI(null);
        }
    }
}