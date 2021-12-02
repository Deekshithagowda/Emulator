package com.sconti.studentcontinent.activity.starterscreen;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DatabaseReference;
import com.sconti.studentcontinent.ContinentApplication;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.alltabs.SecondMainActivity;
import com.sconti.studentcontinent.backendServices.BackgroundService;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.utils.AppUtils;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.COLLEGE_NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_ASPIRATION;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

public class CollegeSelectionActivity extends BaseActivity{
    private static final String TAG = "CollegeSelectionActivity";
    private List<String> degrees;
    private String selectedDegree;
    private String userID;
    private ChipGroup mTagsChipGroup;
    private TextView mTextViewUsername;
    private LinearLayout mLinearLayoutBottom;
    private CircularProgressButton mButtonNext;
    private String selectedCollege;
    private EditText mEditTextCollegeName;
    private ProgressBar mProgressbar;

    @Override
    protected int getContentView() {
        return R.layout.activity_degree_selection;
    }

    @Override
    protected void initView() {
        //  mTagsChipGroup = findViewById(R.id.HashChipGroup);
        mTextViewUsername = findViewById(R.id.username);
        // mListViewLanguage = findViewById(R.id.listViewLanguage);
        mLinearLayoutBottom = findViewById(R.id.linearLayoutBottom);
        mButtonNext = findViewById(R.id.buttonNext);
        mEditTextCollegeName = findViewById(R.id.editTextCollegeName);
        mProgressbar = findViewById(R.id.progressbar);
    }

    @Override
    protected void initData() {
        String name = "Hi, " + SharedPrefsHelper.getInstance().get(AppUtils.Constants.NAME);
     //   mTextViewUsername.setText(name);
        userID = SharedPrefsHelper.getInstance().get(USER_ID);
        //initializeDegreeData();
    }

    @Override
    protected void initListener() {
        mEditTextCollegeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()>2)
                {
                    mButtonNext.setEnabled(true);
                    mButtonNext.setClickable(true);
                }
                else
                {
                    mButtonNext.setEnabled(false);
                    mButtonNext.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressbar.setVisibility(View.VISIBLE);
                selectedCollege = mEditTextCollegeName.getText().toString();
                if(selectedCollege!=null && selectedCollege.length()>0) {
                    SharedPrefsHelper.getInstance().save(COLLEGE_NAME, selectedCollege);
                    BackgroundService.startActionCollege(CollegeSelectionActivity.this);
                    if (SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_ASPIRATION) != null) {
                        startActivity(new Intent(CollegeSelectionActivity.this, SecondMainActivity.class));
                    } else {
                        startActivity(new Intent(CollegeSelectionActivity.this, WhatYouWantActivity.class));
                    }
                    mProgressbar.setVisibility(View.GONE);
                    finish();
                }
                else
                {
                    Toast.makeText(CollegeSelectionActivity.this, "Please Enter College Name", Toast.LENGTH_LONG).show();
                    mProgressbar.setVisibility(View.GONE);
                }
            }
        });
    }
    private void initializeDegreeData() {
        selectedCollege = mEditTextCollegeName.getText().toString();
    }
}