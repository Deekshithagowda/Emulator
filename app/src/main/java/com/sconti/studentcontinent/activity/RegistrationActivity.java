package com.sconti.studentcontinent.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.sconti.studentcontinent.ContinentApplication;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.model.UserDataModel;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.EMAIL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.FIRST_TIME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.MOBILE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.NAME;

public class RegistrationActivity extends BaseActivity {
    private static final String TAG = "RegistrationActivity";

    private ActionBar mActionBar;
    private Button mButtonSubmit;
    private DatabaseReference databaseReference;
    private TextInputEditText textInputEditTextName, textInputEditTextPhone, textInputEditTextEmail;

    @Override
    protected int getContentView() {
        return R.layout.activity_registration;
    }

    @Override
    protected void initView() {
        mActionBar = getSupportActionBar();
        textInputEditTextEmail = findViewById(R.id.et_email);
        textInputEditTextName = findViewById(R.id.et_name);
        textInputEditTextPhone = findViewById(R.id.et_phone);
        mButtonSubmit = findViewById(R.id.btn_register);
    }

    @Override
    protected void initData() {
        if(mActionBar!=null)
        {
            mActionBar.setTitle(getString(R.string.registration));
        }
    }

    @Override
    protected void initListener() {
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitDetails();
            }
        });
    }
    private void submitDetails() {
        try {
            if(textInputEditTextName.getText()==null || textInputEditTextName.getText().toString().trim().length()<3)
            {
                textInputEditTextName.setError(getString(R.string.name_required));
                return;
            }
            if(textInputEditTextPhone.getText()==null || textInputEditTextPhone.getText().toString().trim().length()!=10)
            {
                textInputEditTextPhone.setError(getString(R.string.phone_correct));
                return;
            }

           /* if(textInputEditTextEmail.getText()!=null) {
                if (!(Patterns.EMAIL_ADDRESS.matcher(textInputEditTextEmail.getText()).matches())) {
                    return;
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        UserDataModel userDetails = new UserDataModel();
        userDetails.setEmail(textInputEditTextEmail.getText().toString());
        userDetails.setMobile(textInputEditTextPhone.getText().toString());
        userDetails.setName(textInputEditTextName.getText().toString());
        databaseReference = ContinentApplication.getFireBaseRef();
        databaseReference = databaseReference.child("users").child("profile")/*.child(course)*/;
        databaseReference.child(textInputEditTextPhone.getText().toString()).setValue(userDetails);
        SharedPrefsHelper.getInstance().save(FIRST_TIME, false);
        SharedPrefsHelper.getInstance().save(NAME,textInputEditTextName.getText().toString());
        SharedPrefsHelper.getInstance().save(EMAIL, textInputEditTextEmail.getText().toString());
        SharedPrefsHelper.getInstance().save(MOBILE, textInputEditTextPhone.getText().toString());
        startActivity(new Intent(activity, MainActivity.class));
        finish();
    }
}
