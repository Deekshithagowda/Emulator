package com.sconti.studentcontinent.activity.newlogin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.starterscreen.StarterActivity;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.model.APIResponseModel;
import com.sconti.studentcontinent.model.UserDetails;
import com.sconti.studentcontinent.utils.AppUtils;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.EMAIL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.FIRST_TIME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.MOBILE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.PROFILE_URL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";

    private EditText mEditTextName, mEditTextPhone, mEditTextEmail, mEditTextPassword;
    private CircularProgressButton mButtonRegister;
    private String name, phone, email, password, url;
    private CallbackManager callbackManager;
    private Button mButtonFacebook;
    private ImageView mImageViewFaceBook, mImageViewGmail;
    private com.facebook.login.widget.LoginButton loginButton;
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    String mVerificationId, phoneNumber, otp;
    private FirebaseAuth mAuth;
    private TextInputEditText mInputEditTextOTP;

    private TextInputLayout otpfld;
    private ScrollView scrollView;
    private Button mButtonVerifyOTP;
    private Button mButtonResendOTP;
    private TextInputLayout otpInputFld;
    private CircleImageView mImageViewAvatar;


    @Override
    protected int getContentView() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        changeStatusBarColor();
        mEditTextName = findViewById(R.id.editTextName);
        mEditTextEmail = findViewById(R.id.editTextEmail);
        mEditTextPassword = findViewById(R.id.editTextPassword);
        mEditTextPhone = findViewById(R.id.editTextMobile);
        mButtonRegister = findViewById(R.id.cirRegisterButton);
        mImageViewFaceBook = findViewById(R.id.facebook_register);
        mImageViewGmail = findViewById(R.id.gmail_register);
        firebaseAuth = FirebaseAuth.getInstance();
        mButtonVerifyOTP = findViewById(R.id.btn_verify_otp);
        scrollView = findViewById(R.id.scrollview_main);
        mInputEditTextOTP = findViewById(R.id.et_otp);
        mButtonResendOTP = findViewById(R.id.btn_resend_otp);
        otpInputFld = findViewById(R.id.til_otp);
    }

    @Override
    protected void initData() {

    }
    private void initFaceBook(){
        callbackManager = CallbackManager.Factory.create();
        loginButton = new com.facebook.login.widget.LoginButton(RegisterActivity.this);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginResult.getAccessToken().getUserId();
                Log.e(TAG, "onSuccess facebook userId: "+loginResult.getAccessToken().getUserId());
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
                                    //userDetails.setPhone(name.substring(0,3));
                                    userDetails.setPwd("facebook");
                                    String picURL = "https://graph.facebook.com/{userid}/picture?type=square";
                                    picURL = picURL.replace("{userid}", id);
                                    userDetails.setImageURL(picURL);
                                    callNetworkAPI(userDetails);
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
                Log.e(TAG, "onCancel: ");
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e(TAG, "onError: "+exception.getLocalizedMessage());
                // App code
            }
        });
        loginButton.performClick();
    }

    @Override
    protected void initListener() {
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        mImageViewFaceBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               initFaceBook();
            }
        });

        mImageViewGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initGoogleSignIn();
            }
        });

        mButtonVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mInputEditTextOTP.getText().toString()!=null && mInputEditTextOTP.getText().toString().length()==6)
                {
                    verifyPhoneNumberWithCode(mVerificationId, mInputEditTextOTP.getText().toString());
                }
                else
                {
                    AppUtils.showToastMessage(RegisterActivity.this, getString(R.string.valid_otp));
                }
            }
        });

        mButtonResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendVerificationCode(mEditTextPhone.getText().toString(), mResendToken);
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.e(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                   // inputEditTextPhone.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                   /* Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();*/
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                Log.e(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
                mButtonVerifyOTP.setVisibility(View.VISIBLE);
                otpInputFld.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mButtonResendOTP.setVisibility(View.VISIBLE);
                    }
                }, 60000);

            }
        };



    }

    private void registerUser() {
        name = mEditTextName.getText().toString().trim();
        phone = mEditTextPhone.getText().toString().trim();
        email = mEditTextEmail.getText().toString().trim();
        password = mEditTextPassword.getText().toString().trim();

        if(name.length()<2)
        {
            AppUtils.showToastMessage(RegisterActivity.this, getString(R.string.name_required));
            return;
        }
        if(phone.length()!=10)
        {
            AppUtils.showToastMessage(RegisterActivity.this, getString(R.string.phone_correct));
            return;
        }
        if(password.length()<2)
        {
            AppUtils.showToastMessage(RegisterActivity.this, getString(R.string.correct_pwd));
            return;
        }
        if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches()))
        {
            AppUtils.showToastMessage(RegisterActivity.this,getString(R.string.email_correct));
            return;
        }
        UserDetails userDetails = new UserDetails();
        userDetails.setEmail(email);
        userDetails.setMobile(phone);
        userDetails.setName(name);
        userDetails.setPwd(password);
        userDetails.setUserId(phone);
        if(AppUtils.checkInternetStatus())
        {
            startPhoneNumberVerification("+91" + mEditTextPhone.getText().toString());

        }
        else{
            AppUtils.showToastMessage(RegisterActivity.this, getString(R.string.no_inetrnet));
        }
    }

    private void callNetworkAPI(UserDetails userDetails) {
        Call<APIResponseModel> apiResponseModelCall = apiInterface.insertUser(userDetails);
        apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                if(response.isSuccessful() && response.body().isStatus())
                {
                    APIResponseModel apiResponseModel = new APIResponseModel();
                    apiResponseModel.setPhone(phone);
                    apiResponseModel.setEmail(email);
                    apiResponseModel.setId(email);
                    apiResponseModel.setName(name);
                    apiResponseModel.setImageURL(url);
                    registeredSuccess(apiResponseModel);
                }
                else {
                    AppUtils.showToastMessage(RegisterActivity.this, getString(R.string.already_registered));
                }
            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                AppUtils.showToastMessage(RegisterActivity.this, getString(R.string.some_thing_wrong));
            }
        });
    }

    private void registeredSuccess(APIResponseModel userDetails) {
        SharedPrefsHelper.getInstance().save(FIRST_TIME, false);
        SharedPrefsHelper.getInstance().save(NAME, userDetails.getName());
        SharedPrefsHelper.getInstance().save(EMAIL, userDetails.getEmail());
        SharedPrefsHelper.getInstance().save(MOBILE, userDetails.getPhone());
        SharedPrefsHelper.getInstance().save(PROFILE_URL, userDetails.getImageURL());
        SharedPrefsHelper.getInstance().save(USER_ID, userDetails.getId());
        startActivity(new Intent(activity, StarterActivity.class));
        finish();
    }


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
      //  overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);

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
            Log.e(TAG, "handleSignInResult: "+userDetails.getName());
          //  Log.e(TAG, "handleSignInResult: "+userDetails.getImageURL(mImageViewAvatar));
            Log.e(TAG, "handleSignInResult: "+userDetails.getUserId());
            Log.e(TAG, "handleSignInResult: "+userDetails.getEmail());
            callNetworkAPI(userDetails);
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
           // updateUI(null);
        }
    }

    private void initGoogleSignIn(){
        SignInButton signInButton = new SignInButton(RegisterActivity.this);
        googleSignIn();
        signIn();

    }
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButton;
    private void googleSignIn()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
               // .requestIdToken(getString(R.string.server_client_id))
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

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e(TAG, "signInWithCredential:success");
                            FirebaseUser currentUser = task.getResult().getUser();
                            UserProfileChangeRequest update = new UserProfileChangeRequest.Builder().setDisplayName(mEditTextPhone.getText().toString()).build();
                            currentUser.updateProfile(update);
                            UserDetails userDetails = new UserDetails();
                            userDetails.setPhone(mEditTextPhone.getText().toString());
                            userDetails.setPwd(mEditTextPassword.getText().toString());
                            userDetails.setUserId(currentUser.getUid());
                            userDetails.setName(mEditTextName.getText().toString());
                            userDetails.setEmail(mEditTextEmail.getText().toString());
                            callNetworkAPI(userDetails);

                           //registeredSuccess(userDetails);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(RegisterActivity.this, "Invalid Code", Toast.LENGTH_LONG).show();
                                // mVerificationField.setError("Invalid code.");
                            }
                        }
                    }
                });
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mEditTextPhone.getText().toString();
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length()!=10) {
            mEditTextPhone.setError("Invalid phone number.");
            return false;
        }
        return true;
    }

}

