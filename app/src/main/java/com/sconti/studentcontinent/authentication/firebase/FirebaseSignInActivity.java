package com.sconti.studentcontinent.authentication.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sconti.studentcontinent.ContinentApplication;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.MainActivity;
import com.sconti.studentcontinent.model.UserDataModel;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import java.util.concurrent.TimeUnit;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.EMAIL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.FIRST_TIME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.MOBILE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.PROFILE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.PROFILE_URL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

public class FirebaseSignInActivity extends AppCompatActivity {
    public EditText loginEmailId, logInpasswd;
    Button btnLogIn;
    TextView signup;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String mVerificationId;

    //The edittext to input the code
    private EditText editTextCode;
    private LinearLayout linearLayoutOTP, linearLayoutEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_login);
        firebaseAuth = FirebaseAuth.getInstance();
        loginEmailId = findViewById(R.id.loginEmail);
        logInpasswd = findViewById(R.id.loginpaswd);
        btnLogIn = findViewById(R.id.btnLogIn);
        signup = findViewById(R.id.TVSignIn);
        linearLayoutOTP = findViewById(R.id.linearlayoutMobile);
        linearLayoutEmail = findViewById(R.id.linearlayoutEmail);
        editTextCode = findViewById(R.id.loginOTP);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    SharedPrefsHelper.getInstance().save(FIRST_TIME, false);
                    SharedPrefsHelper.getInstance().save(USER_ID, user.getUid());
                    getUserData(user.getUid());
                    Toast.makeText(FirebaseSignInActivity.this, "User logged in ", Toast.LENGTH_SHORT).show();
                    /*Intent I = new Intent(FirebaseSignInActivity.this, MainActivity.class);
                    startActivity(I);
                    finish();*/
                } else {
                   // Toast.makeText(FirebaseSignInActivity.this, "Login to continue", Toast.LENGTH_SHORT).show();
                }
            }
        };
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(FirebaseSignInActivity.this, FirebaseLoginActivity.class);
                startActivity(I);
                finish();
            }
        });
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = editTextCode.getText().toString().trim();
                if (otp != null && otp.length() > 3) {
                    verifyVerificationCode(otp);
                } else {
                    String userEmail = loginEmailId.getText().toString();
                    boolean isMobile = (userEmail.contains("[outdooricon-zA-Z]+") == false);
                    if (isMobile) {
                        String userPaswd = logInpasswd.getText().toString();
                        if (userEmail.isEmpty()) {
                            loginEmailId.setError("Provide your Email first!");
                            loginEmailId.requestFocus();
                        } else if (userPaswd.isEmpty()) {
                            logInpasswd.setError("Enter Password!");
                            logInpasswd.requestFocus();
                        } else if (userEmail.isEmpty() && userPaswd.isEmpty()) {
                            Toast.makeText(FirebaseSignInActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                        } else if (!(userEmail.isEmpty() && userPaswd.isEmpty())) {
                            firebaseAuth.signInWithEmailAndPassword(userEmail, userPaswd).addOnCompleteListener(FirebaseSignInActivity.this, new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(FirebaseSignInActivity.this, "Not successfull", Toast.LENGTH_SHORT).show();
                                    } else {
                                        SharedPrefsHelper.getInstance().save(FIRST_TIME, false);
                                        SharedPrefsHelper.getInstance().save(USER_ID, firebaseAuth.getUid());
                                        getUserData(firebaseAuth.getUid());
                              /*  startActivity(new Intent(FirebaseSignInActivity.this, MainActivity.class));
                                finish();*/
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(FirebaseSignInActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        linearLayoutEmail.setVisibility(View.GONE);
                        linearLayoutOTP.setVisibility(View.VISIBLE);
                        sendVerificationCode(userEmail);
                    }
                }
            }

        });


    }

    private void getUserData(String uid) {
        DatabaseReference databaseReference = ContinentApplication.getFireBaseRef().child("users").child("profile").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDataModel userDataModel = dataSnapshot.getValue(UserDataModel.class);
                SharedPrefsHelper.getInstance().save(FIRST_TIME, false);
                SharedPrefsHelper.getInstance().save(NAME, userDataModel.getName());
                SharedPrefsHelper.getInstance().save(EMAIL, userDataModel.getEmail());
                SharedPrefsHelper.getInstance().save(MOBILE, userDataModel.getMobile());
                SharedPrefsHelper.getInstance().save(PROFILE_URL, userDataModel.getProfileURL());
                startActivity(new Intent(FirebaseSignInActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                (Activity) TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }


    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(FirebaseSignInActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(FirebaseSignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPrefsHelper.getInstance().save(FIRST_TIME, false);
                            SharedPrefsHelper.getInstance().save(USER_ID, firebaseAuth.getUid());
                            getUserData(firebaseAuth.getUid());

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }

}