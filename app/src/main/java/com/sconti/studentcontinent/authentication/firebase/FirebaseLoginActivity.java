package com.sconti.studentcontinent.authentication.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.sconti.studentcontinent.ContinentApplication;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.MainActivity;
import com.sconti.studentcontinent.model.UserDataModel;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.EMAIL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.FIRST_TIME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.MOBILE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.PROFILE_URL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

public class FirebaseLoginActivity extends AppCompatActivity {
    private static final String TAG = "FirebaseLoginActivity";
    public EditText emailId, passwd, mEditTextName, mEditTextMobile;
    Button btnSignUp;
    TextView signIn;
    FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private static final String PROFILE = "public_profile";

    private LoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_login2);
        firebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.ETemail);
        passwd = findViewById(R.id.ETpassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        signIn = findViewById(R.id.TVSignIn);
        mEditTextName = findViewById(R.id.ETName);
        mEditTextMobile  = findViewById(R.id.ETMobile);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailID = emailId.getText().toString();
                String paswd = passwd.getText().toString();
                if (emailID.isEmpty()) {
                    emailId.setError("Provide your Email first!");
                    emailId.requestFocus();
                } else if (paswd.isEmpty()) {
                    passwd.setError("Set your password");
                    passwd.requestFocus();
                } else if (emailID.isEmpty() && paswd.isEmpty()) {
                    Toast.makeText(FirebaseLoginActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(emailID.isEmpty() && paswd.isEmpty())) {
                    firebaseAuth.createUserWithEmailAndPassword(emailID, paswd).addOnCompleteListener(FirebaseLoginActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(FirebaseLoginActivity.this.getApplicationContext(),
                                        "SignUp unsuccessful: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                UserDataModel userDetails = new UserDataModel();
                                userDetails.setEmail(emailId.getText().toString());
                                userDetails.setMobile(mEditTextMobile.getText().toString());
                                userDetails.setName(mEditTextName.getText().toString());
                                userDetails.setUserID(firebaseAuth.getUid());
                                SharedPrefsHelper.getInstance().save(USER_ID, firebaseAuth.getUid());
                               saveAndUpdateData(userDetails);
                            }
                        }
                    });
                } else {
                    Toast.makeText(FirebaseLoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(FirebaseLoginActivity.this, FirebaseSignInActivity.class);
                startActivity(I);
                finish();
            }
        });
        
        initFaceBookLogin();
    }

    private void saveAndUpdateData(UserDataModel userDetails) {
        databaseReference = ContinentApplication.getFireBaseRef();
        databaseReference = databaseReference.child("users").child("profile")/*.child(course)*/;
        databaseReference.child(userDetails.getUserID()).setValue(userDetails);
        SharedPrefsHelper.getInstance().save(FIRST_TIME, false);
        SharedPrefsHelper.getInstance().save(NAME, userDetails.getName());
        SharedPrefsHelper.getInstance().save(EMAIL, userDetails.getEmail());
        SharedPrefsHelper.getInstance().save(MOBILE, userDetails.getMobile());
        SharedPrefsHelper.getInstance().save(PROFILE_URL, userDetails.getProfileURL());
        startActivity(new Intent(FirebaseLoginActivity.this, MainActivity.class));
        finish();
    }

    private void initFaceBookLogin() {
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setBackgroundResource(R.drawable.ic_facebook);
        loginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        loginButton.setReadPermissions(Arrays.asList(EMAIL, PROFILE));
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
                                    UserDataModel userDetails = new UserDataModel();
                                    userDetails.setEmail(email);
                                    userDetails.setName(name);
                                    userDetails.setUserID(id);
                                    userDetails.setMobile("");
                                    userDetails.setProfileURL(profile_pic);
                                    SharedPrefsHelper.getInstance().save(USER_ID, id);
                                    saveAndUpdateData(userDetails);
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
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


}