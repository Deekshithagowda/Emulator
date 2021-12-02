package com.sconti.studentcontinent.activity.newlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.sconti.studentcontinent.activity.alltabs.SecondMainActivity;
import com.sconti.studentcontinent.activity.starterscreen.CollegeSelectionActivity;
import com.sconti.studentcontinent.activity.starterscreen.StarterActivity;
import com.sconti.studentcontinent.activity.starterscreen.WhatYouWantActivity;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.model.APIResponseModel;
import com.sconti.studentcontinent.model.UserDetails;
import com.sconti.studentcontinent.utils.AppUtils;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import java.util.concurrent.TimeUnit;

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
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_ASPIRATION;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_ASPIRATION_NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

public class NewLoginActivity extends BaseActivity {
    private static final String TAG = "NewLoginActivity";
    private EditText mEditTextMobile, editTextName, mInputCode1, mInputCode2, mInputCode3, mInputCode4, mInputCode5, mInputCode6;
    private PinView lytOTP;
    private TextView mHeadLineLoginPage;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    String mVerificationId, phoneNumber, otp;
    private ProgressBar mLinearLayoutProgressBar, mProgressbarOtp , mUpdateNameProgressbar;
    private FirebaseAuth mAuth;
    TextView mOtpTime;
    private LinearLayout mLoginLayout, mOtpEnterLayout, mUpdateName , mResendOtpLine;
    private CircularProgressButton loginButton, mVerifyOtpButton, mUpdateNameButton, mResendOTPButton;


    @Override
    protected int getContentView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        return R.layout.activity_new_login;
    }

    @Override
    protected void initView() {
        mLinearLayoutProgressBar = findViewById(R.id.progressbar);
        mEditTextMobile = findViewById(R.id.editTextEmail);
       // lytOTP = findViewById(R.id.editTextOTP);
        editTextName = findViewById(R.id.editTextName);
        //   mEditTextMobile = findViewById(R.id.textInputEmail);
        //  editTextName = findViewById(R.id.textInputName);
        mHeadLineLoginPage = findViewById(R.id.HeadLine_login_page);
        //    lytOTP = findViewById(R.id.textInpuOTP);
        loginButton = findViewById(R.id.cirLoginButton);
        mLoginLayout = findViewById(R.id.loginLayout);
        mOtpEnterLayout = findViewById(R.id.otpEnterLayout);
        mUpdateName = findViewById(R.id.updateName);
        mOtpTime = findViewById(R.id.OtpTime);
        mVerifyOtpButton = findViewById(R.id.verifyOtpButton);
        mUpdateNameButton = findViewById(R.id.UpdateNameButton);
        mResendOTPButton = findViewById(R.id.ResendOTPButton);
        mProgressbarOtp = findViewById(R.id.progressbarOtp);
        mResendOtpLine = findViewById(R.id.resendOtpLine);
        mUpdateNameProgressbar = findViewById(R.id.UpdateNameProgressbar);
        mInputCode1 = findViewById(R.id.InputCode1);
        mInputCode2 = findViewById(R.id.InputCode2);
        mInputCode3 = findViewById(R.id.InputCode3);
        mInputCode4 = findViewById(R.id.InputCode4);
        mInputCode5 = findViewById(R.id.InputCode5);
        mInputCode6 = findViewById(R.id.InputCode6);
    }


    Animation RightSwipe, RightSwipe2;
    int WhichLyt = 0;

    @Override
    protected void initData() {
        RightSwipe = AnimationUtils.loadAnimation(NewLoginActivity.this, R.anim.left);
        RightSwipe2 = AnimationUtils.loadAnimation(NewLoginActivity.this, R.anim.left_left);


        RightSwipe.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (WhichLyt == 1) {
                    mEditTextMobile.setVisibility(View.GONE);
                    mLoginLayout.setVisibility(View.GONE);
                 //   lytOTP.setVisibility(View.VISIBLE);
                    mOtpEnterLayout.setVisibility(View.VISIBLE);

                    new CountDownTimer(60000, 1000) {

                        @SuppressLint("SetTextI18n")
                        public void onTick(long millisUntilFinished) {
                            mVerifyOtpButton.setVisibility(View.VISIBLE);
                            mResendOTPButton.setVisibility(View.GONE);
                         //   InputCodeTrue();
                            mResendOtpLine.setVisibility(View.VISIBLE);
                            mOtpTime.setText("" + millisUntilFinished / 1000 + " secs");
                        }

                        @SuppressLint("SetTextI18n")
                        public void onFinish() {
                            InputCodeNull();
                          //  InputCodeFalse();
                            mVerifyOtpButton.setVisibility(View.GONE);
                            mResendOTPButton.setVisibility(View.VISIBLE);
                            mResendOTPButton.setEnabled(true);
                            mResendOTPButton.setVisibility(View.VISIBLE);
                            mResendOtpLine.setVisibility(View.GONE);
                        }
                    }.start();

                    editTextName.setVisibility(View.GONE);
                    mUpdateName.setVisibility(View.GONE);
                 //   lytOTP.requestFocus();
                    mProgressbarOtp.setVisibility(View.GONE);
                    loginButton.setText(R.string.verify_otp);
                } else if (WhichLyt == 2) {
                    mEditTextMobile.setVisibility(View.GONE);
                    mLoginLayout.setVisibility(View.GONE);
                 //   lytOTP.setVisibility(View.GONE);
                    mOtpEnterLayout.setVisibility(View.GONE);
                    editTextName.setVisibility(View.VISIBLE);
                    mUpdateName.setVisibility(View.VISIBLE);
                    editTextName.requestFocus();
                    loginButton.setText(getString(R.string.update_name));
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        RightSwipe.setDuration(500);
        RightSwipe2.setDuration(500);


    }

    @Override
    protected void initListener() {
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (loginButton.getText().toString().equalsIgnoreCase("Login")) {
//                    if (mEditTextMobile.getText() != null && mEditTextMobile.getText().toString().trim().length() == 10) {
//                        startPhoneNumberVerification("+91" + mEditTextMobile.getText().toString().trim());
//                    } else {
//                        AppUtils.showToastMessage(NewLoginActivity.this, getString(R.string.enter_phone));
//                    }
//                } else if (mVerifyOtpButton.getText().toString().equalsIgnoreCase("Verify OTP")) {
//                    Log.d(TAG, "onClick: ");
//                    if (lytOTP.getText() != null && lytOTP.getText().toString().trim().length() > 2) {
//                        verifyPhoneNumberWithCode(mVerificationId, lytOTP.getText().toString());
//                    } else if (lytOTP.getText() != null && lytOTP.getText().toString().trim().length() != 6) {
//                        AppUtils.showToastMessage(NewLoginActivity.this, getString(R.string.valid_otp));
//                    }
//                } else if (loginButton.getText().toString().equalsIgnoreCase("update name")) {
//                    if (editTextName.getText() != null && editTextName.getText().toString().trim().length() > 3) {
//                        updateName(editTextName.getText().toString().trim());
//                    }
//                }
//            }
//        });


//        SmsReceiver.bindListener(new SmsListener() {
//            @Override
//            public void messageReceived(String messageText) {
//                ed.setText(messageText);
//            }
//        });

        new OTP_Receiver().setEditText(mInputCode1 , mInputCode2 , mInputCode3 ,mInputCode4 , mInputCode5 , mInputCode6);
        requestsmspermission();



        //unregisterReceiver(new OTP_Receiver());




        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditTextMobile.getText() != null && mEditTextMobile.getText().toString().trim().length() == 10) {
                    loginButton.setEnabled(false);
                    mEditTextMobile.setEnabled(false);
                    mLinearLayoutProgressBar.setVisibility(View.VISIBLE);
                    startPhoneNumberVerification("+91" + mEditTextMobile.getText().toString().trim());
                } else {
                    AppUtils.showToastMessage(NewLoginActivity.this, getString(R.string.enter_phone));
                    mLinearLayoutProgressBar.setVisibility(View.GONE);
                    loginButton.setEnabled(true);
                    mEditTextMobile.setEnabled(true);
                }
            }
        });

        mVerifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputCode1.getText().toString().trim().isEmpty() || mInputCode2.getText().toString().trim().isEmpty() || mInputCode3.getText().toString().trim().isEmpty() ||
                        mInputCode4.getText().toString().trim().isEmpty() || mInputCode5.getText().toString().trim().isEmpty() || mInputCode6.getText().toString().trim().isEmpty()) {
                    mProgressbarOtp.setVisibility(View.GONE);
                    AppUtils.showToastMessage(NewLoginActivity.this, getString(R.string.valid_otp));
                    mVerifyOtpButton.setEnabled(true);
                  //  InputCodeTrue();
                }else {

                    mProgressbarOtp.setVisibility(View.VISIBLE);

                    String OTPCode =
                            mInputCode1.getText().toString() +
                                    mInputCode2.getText().toString() +
                                    mInputCode3.getText().toString() +
                                    mInputCode4.getText().toString() +
                                    mInputCode5.getText().toString() +
                                    mInputCode6.getText().toString();

                    verifyPhoneNumberWithCode(mVerificationId, OTPCode);
                    mVerifyOtpButton.setEnabled(false);
                    mResendOTPButton.setEnabled(false);
                   // InputCodeFalse();
                }



//                if (lytOTP.getText() != null && lytOTP.getText().toString().trim().length() > 2) {
//                    verifyPhoneNumberWithCode(mVerificationId, lytOTP.getText().toString());
//                } else if (lytOTP.getText() != null && lytOTP.getText().toString().trim().length() != 6) {
//                    AppUtils.showToastMessage(NewLoginActivity.this, getString(R.string.valid_otp));
//                    mProgressbarOtp.setVisibility(View.GONE);
//                }
            }
        });

        mUpdateNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextName.getText() != null && editTextName.getText().toString().trim().length() > 1) {
                    mUpdateNameProgressbar.setVisibility(View.VISIBLE);
                    updateName(editTextName.getText().toString().trim());
                }else {
                    AppUtils.showToastMessage(NewLoginActivity.this, getString(R.string.Enter_name));
                    mUpdateNameProgressbar.setVisibility(View.GONE);
                }
            }
        });

        mResendOTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputCodeNull();
               // InputCodeFalse();
                mProgressbarOtp.setVisibility(View.VISIBLE);
                mResendOTPButton.setEnabled(false);
                resendVerificationCode("+91" + mEditTextMobile.getText().toString(), mResendToken);

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
                mLinearLayoutProgressBar.setVisibility(View.GONE);
                loginButton.setEnabled(true);
                mEditTextMobile.setEnabled(true);
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    AppUtils.showToastMessage(NewLoginActivity.this, "invalid phone number.");
                    // inputEditTextPhone.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    AppUtils.showToastMessage(NewLoginActivity.this, "iQuota exceeded.");
                    loginButton.setEnabled(true);
                    mEditTextMobile.setEnabled(true);
                    mProgressbarOtp.setVisibility(View.GONE);
                   /* Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();*/
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                try {
                    WhichLyt = 1;
                    Log.e(TAG, "onCodeSent:" + verificationId);
                    mLinearLayoutProgressBar.setVisibility(View.GONE);
                    mVerificationId = verificationId;
                    mResendToken = token;
                  //  lytOTP.setVisibility(View.VISIBLE);
                    mOtpEnterLayout.setVisibility(View.VISIBLE);
                    //  mEditTextMobile.setVisibility(View.INVISIBLE);
                    mLoginLayout.setVisibility(View.VISIBLE);
                    // mEditTextMobile.startAnimation(RightSwipe2);
                    mLoginLayout.startAnimation(RightSwipe2);
                    //  lytOTP.startAnimation(RightSwipe);
                    mOtpEnterLayout.startAnimation(RightSwipe);
                    String OTPCode1 =
                            mInputCode1.getText().toString() +
                                    mInputCode2.getText().toString() +
                                    mInputCode3.getText().toString() +
                                    mInputCode4.getText().toString() +
                                    mInputCode5.getText().toString() +
                                    mInputCode6.getText().toString();
                    verifyPhoneNumberWithCode(mVerificationId, OTPCode1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

             /* //  mTextViewOTPSentMessage.setText(getString(R.string.otp_sent)+" "+textInputEditTextPhone.getText().toString());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      //  mButtonResendOTP.setVisibility(View.VISIBLE);
                    }
                }, 60000);
*/
            }
        };

        setupOTPInputs();
    }

    private void requestsmspermission() {
        String smspermission = Manifest.permission.RECEIVE_SMS;
        int grant = ContextCompat.checkSelfPermission(this,smspermission);

        if(grant!= PackageManager.PERMISSION_GRANTED)
        {
            String[] permission_list = new String[1];
                    permission_list[0]=smspermission;

            ActivityCompat.requestPermissions(this,permission_list,1);

        }
    }

//    private void InputCodeFalse(){
//        mInputCode1.setEnabled(false);
//        mInputCode2.setEnabled(false);
//        mInputCode3.setEnabled(false);
//        mInputCode4.setEnabled(false);
//        mInputCode5.setEnabled(false);
//        mInputCode6.setEnabled(false);
//    }

//    private void InputCodeTrue(){
//        mInputCode1.setEnabled(true);
//        mInputCode2.setEnabled(true);
//        mInputCode3.setEnabled(true);
//        mInputCode4.setEnabled(true);
//        mInputCode5.setEnabled(true);
//        mInputCode6.setEnabled(true);
//    }
//
    private void InputCodeNull(){
        mInputCode1.setText(null);
        mInputCode2.setText(null);
        mInputCode3.setText(null);
        mInputCode4.setText(null);
        mInputCode5.setText(null);
        mInputCode6.setText(null);
        mInputCode1.requestFocus();
        mInputCode1.setInputType(android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

    }

    private void updateName(String name) {
        mLinearLayoutProgressBar.setVisibility(View.VISIBLE);
        Call<APIResponseModel> responseModelCall = apiInterface.updateNAme(currentUser.getUid(), mEditTextMobile.getText().toString().trim(), name);
        responseModelCall.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                mLinearLayoutProgressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body().isStatus()) {
                    loginSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                mLinearLayoutProgressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                AppUtils.showToastMessage(NewLoginActivity.this, getString(R.string.some_thing_wrong));
            }
        });
    }



    private void startPhoneNumberVerification(String phoneNumber) {
        mLinearLayoutProgressBar.setVisibility(View.VISIBLE);
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

    FirebaseUser currentUser;

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loginButton.setEnabled(true);
                        mEditTextMobile.setEnabled(true);
                        if (task.isSuccessful()) {

                            Log.e(TAG, "signInWithCredential:success");
                            currentUser = task.getResult().getUser();
                            //  UserProfileChangeRequest update = new UserProfileChangeRequest.Builder().setDisplayName(textInputEditTextName.getText().toString()).build();
                            // currentUser.updateProfile(update);
                            UserDetails userDetails = new UserDetails();
                           /* userDetails.setPhone(textInputEditTextPhone.getText().toString());
                            userDetails.setPwd(textInputEditTextPwd.getText().toString());
                            userDetails.setUserid(currentUser.getUid());
                            userDetails.setName(textInputEditTextName.getText().toString());
                            userDetails.setEmail(textInputEditTextEmail.getText().toString());*/
                            // registerUser(userDetails);

                            //registeredSuccess(userDetails);
                            AppUtils.showToastMessage(NewLoginActivity.this, getString(R.string.your_phone_verified));
                            checkLoginDetails(currentUser.getUid(), "", 5);
                        } else {
                            loginButton.setEnabled(true);
                            mEditTextMobile.setEnabled(true);
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(NewLoginActivity.this, "Invalid Code", Toast.LENGTH_LONG).show();
                                mProgressbarOtp.setVisibility(View.GONE);
                                mLinearLayoutProgressBar.setVisibility(View.GONE);
                                mVerifyOtpButton.setEnabled(true);
                              //  InputCodeTrue();
                                // mVerificationField.setError("Invalid code.");
                            }
                        }
                    }
                });
    }

    private void checkLoginDetails(String id, String pwd, int type) {
        if (AppUtils.checkInternetStatus()) {
            Call<APIResponseModel> apiResponseModelCall = apiInterface.verifyUser(id, pwd, type);
            apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
                @Override
                public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                    WhichLyt = 2;
                    mLinearLayoutProgressBar.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body().isStatus()) {
                        loginSuccess(response.body());
                    } else {
                        //  editTextName.setVisibility(View.VISIBLE);
                        mUpdateName.setVisibility(View.VISIBLE);
                        //  editTextName.startAnimation(RightSwipe);
                        mUpdateName.startAnimation(RightSwipe);
                        if (mOtpEnterLayout.getVisibility() == View.VISIBLE) {
                            // lytOTP.startAnimation(RightSwipe2);
                            mOtpEnterLayout.startAnimation(RightSwipe2);
                        } else if (mLoginLayout.getVisibility() == View.VISIBLE) {
                            // mEditTextMobile.startAnimation(RightSwipe2);
                            mLoginLayout.startAnimation(RightSwipe2);
                        }
                    }
                }

                @Override
                public void onFailure(Call<APIResponseModel> call, Throwable t) {
                    mLinearLayoutProgressBar.setVisibility(View.GONE);
                    AppUtils.showToastMessage(NewLoginActivity.this, getString(R.string.some_thing_wrong));
                }
            });
        } else {
            AppUtils.showToastMessage(NewLoginActivity.this, getString(R.string.no_inetrnet));
        }
    }

    private void loginSuccess(APIResponseModel userDetails) {
        SharedPrefsHelper.getInstance().save(FIRST_TIME, false);
        SharedPrefsHelper.getInstance().save(NAME, userDetails.getmUserDetails().getName());
        if (userDetails.getEmail() != null)
            SharedPrefsHelper.getInstance().save(EMAIL, userDetails.getmUserDetails().getEmail());
        SharedPrefsHelper.getInstance().save(MOBILE, userDetails.getmUserDetails().getPhone());
        if (userDetails.getmUserDetails().getPhone() != null)
            SharedPrefsHelper.getInstance().save(PROFILE_URL, userDetails.getmUserDetails().getImageURL());
        SharedPrefsHelper.getInstance().save(USER_ID, userDetails.getmUserDetails().getId());
        if (userDetails.getmUserDetails().getSelectedDegree() != null) {
            SharedPrefsHelper.getInstance().save(AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE, userDetails.getmUserDetails().getSelectedDegree());
        }
        if (userDetails.getmUserDetails().getInterest() != null) {
            SharedPrefsHelper.getInstance().save(SELECTED_SUBJECT_ASPIRATION, userDetails.getmUserDetails().getInterest());
        }

        if (userDetails.getmUserDetails().getSelectedDegreeName() != null) {
            SharedPrefsHelper.getInstance().save(AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE_NAME, userDetails.getmUserDetails().getSelectedDegreeName());
        }
        if (userDetails.getmUserDetails().getInterestName() != null) {
            SharedPrefsHelper.getInstance().save(SELECTED_SUBJECT_ASPIRATION_NAME, userDetails.getmUserDetails().getInterestName());
        }

        if (userDetails.getmUserDetails().getCollege() != null) {
            SharedPrefsHelper.getInstance().save(COLLEGE_NAME, userDetails.getmUserDetails().getCollege());
        }

        if (userDetails.getmUserDetails().getSelectedDegree() == null && userDetails.getmUserDetails().getInterest() == null) {
            startActivity(new Intent(activity, StarterActivity.class));
        } else if (userDetails.getmUserDetails().getCollege() == null) {
            startActivity(new Intent(activity, CollegeSelectionActivity.class));
        } else if (userDetails.getmUserDetails().getSelectedDegree() != null && userDetails.getmUserDetails().getInterest() == null) {
            startActivity(new Intent(activity, WhatYouWantActivity.class));
        } else if (userDetails.getmUserDetails().getSelectedDegree() == null && userDetails.getmUserDetails().getInterest() != null) {
            startActivity(new Intent(activity, StarterActivity.class));
        } else {
            startActivity(new Intent(activity, SecondMainActivity.class));
        }

        finish();
    }

    private void setupOTPInputs() {
        mInputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /*if(!TextUtils.isEmpty(mInputCode1.getText()))
                {
                    mInputCode1.setSelection(mInputCode1.getText().length());
                }*/
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    mInputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(mInputCode1.getText()))
                {
                    mInputCode1.setSelection(mInputCode1.getText().length());
                }
            }
        });
        mInputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    mInputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(mInputCode2.getText()))
                {
                    mInputCode2.setSelection(mInputCode2.getText().length());
                }
            }
        });
        mInputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    mInputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(mInputCode3.getText()))
                {
                    mInputCode3.setSelection(mInputCode3.getText().length());
                }
            }
        });
        mInputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    mInputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(mInputCode4.getText()))
                {
                    mInputCode4.setSelection(mInputCode4.getText().length());
                }
            }
        });
        mInputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    mInputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(mInputCode5.getText()))
                {
                    mInputCode5.setSelection(mInputCode5.getText().length());
                }
            }
        });
        mInputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    mInputCode1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(mInputCode2.getText()))
                {
                    mInputCode2.setSelection(mInputCode2.getText().length());
                }

            }
        });
        mInputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    mInputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(mInputCode3.getText()))
                {
                    mInputCode3.setSelection(mInputCode3.getText().length());
                }
            }
        });
        mInputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    mInputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(mInputCode4.getText()))
                {
                    mInputCode4.setSelection(mInputCode4.getText().length());
                }
            }
        });
        mInputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    mInputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(mInputCode5.getText()))
                {
                    mInputCode5.setSelection(mInputCode5.getText().length());
                }
            }
        });
        mInputCode6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    mInputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(mInputCode6.getText()))
                {
                    mInputCode6.setSelection(mInputCode6.getText().length());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //registerReceiver
        PackageManager pm  = NewLoginActivity.this.getPackageManager();
        ComponentName componentName = new ComponentName(NewLoginActivity.this, OTP_Receiver.class);
        pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver
        PackageManager pm  = NewLoginActivity.this.getPackageManager();
        ComponentName componentName = new ComponentName(NewLoginActivity.this, OTP_Receiver.class);
        pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}