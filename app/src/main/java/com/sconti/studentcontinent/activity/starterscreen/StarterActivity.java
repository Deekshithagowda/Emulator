package com.sconti.studentcontinent.activity.starterscreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.sconti.studentcontinent.ContinentApplication;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.alltabs.SecondMainActivity;
import com.sconti.studentcontinent.backendServices.BackgroundService;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.model.APIResponseModel;
import com.sconti.studentcontinent.model.DegreeAspirationModel;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;
import com.sconti.studentcontinent.utils.AppUtils;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.COLLEGE_NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_DEGREE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_ASPIRATION;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

public class StarterActivity extends BaseActivity {
    private static final String TAG = "StarterActivity";
    private TextView mTextViewUsername;
    private Spinner spinner;
    private List<String> degrees;
    private DegreeListAdapter mDegreeListAdapter;
    private ListView mListViewLanguage;
    private LinearLayout mLinearLayoutBottom;
    private CircularProgressButton mButtonNext;
    private String selectedDegree, selectedDegreeName;
    private String userID;
    private ProgressBar mProgressbar;

    @Override
    protected int getContentView() {
        return R.layout.activity_starter;
    }

    @Override
    protected void initView() {
        mProgressbar = findViewById(R.id.progressbar);
        mTextViewUsername = findViewById(R.id.username);
        mListViewLanguage = findViewById(R.id.listViewLanguage);
        mLinearLayoutBottom = findViewById(R.id.linearLayoutBottom);
        mButtonNext = findViewById(R.id.buttonNext);

        mButtonNext.setEnabled(false);
        mButtonNext.setClickable(false);
    }

    @Override
    protected void initData() {
        setUpNetwork();
        getDegree();
    }

    @Override
    protected void initListener() {
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefsHelper.getInstance().save(SELECTED_SUBJECT_KNOWLEDGE, selectedDegree);
                BackgroundService.startActionDegree(StarterActivity.this);
                if(SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_ASPIRATION)!=null && SharedPrefsHelper.getInstance().get(COLLEGE_NAME)!=null )
                {
                    startActivity(new Intent(StarterActivity.this, SecondMainActivity.class));
                }
                else if(SharedPrefsHelper.getInstance().get(COLLEGE_NAME)!=null)
                {
                    startActivity(new Intent(StarterActivity.this, CollegeSelectionActivity.class));

                }
                else {
                    startActivity(new Intent(StarterActivity.this, WhatYouWantActivity.class));
                }
                finish();
            }
        });

       /* if(!isRecreated)
        {
            recreate();
            isRecreated = true;
        }*/

        //mListViewLanguage.setSelection(0);

    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public void setItemSelected(View view) {
        try {
            View rowView = view;
            TextView tv = rowView.findViewById(R.id.textViewLanguage);
            tv.setTextColor(Color.WHITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ResourceAsColor")
    public void setItemNormal() {
        for (int i = 0; i < mListViewLanguage.getChildCount(); i++) {
            View v = mListViewLanguage.getChildAt(i);
            TextView txtview = v.findViewById(R.id.textViewLanguage);
            txtview.setTextColor(R.color.PrimaryBlue);
        }
    }
    protected ApiInterface apiInterface;
    private void setUpNetwork()
    {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }

    private List<DegreeAspirationModel> degreeAspirationModelList;
    private void getDegree() {
        mProgressbar.setVisibility(View.VISIBLE);
        Call<APIResponseModel> modelCall = apiInterface.getDegree();
        modelCall.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                mProgressbar.setVisibility(View.GONE);
                if(response.isSuccessful() && response.body().getDegreeAspirationModelList().size()>0)
                {
                    degrees = new ArrayList<>();
                    degreeAspirationModelList = new ArrayList<>();
                    degreeAspirationModelList = response.body().getDegreeAspirationModelList();
                    String name = "Hi, " + SharedPrefsHelper.getInstance().get(AppUtils.Constants.NAME);
                  //  mTextViewUsername.setText(name);
                    userID = SharedPrefsHelper.getInstance().get(USER_ID);
                    for(int i=0;i<degreeAspirationModelList.size(); i++)
                    {
                        degrees.add(degreeAspirationModelList.get(i).getName());
                    }
                    mDegreeListAdapter = new DegreeListAdapter(degrees, StarterActivity.this);
                    mListViewLanguage.setAdapter(mDegreeListAdapter);
                    mListViewLanguage.requestFocusFromTouch();
                    mListViewLanguage.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    mListViewLanguage.setItemChecked(0, true);
                    mListViewLanguage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //selectItem(position);
                            setItemNormal();
                            mListViewLanguage.setSelector(R.color.primaryTextColor);
                            selectedDegree = degreeAspirationModelList.get(i).getId();
                            selectedDegreeName = degreeAspirationModelList.get(i).getName();
                            mButtonNext.setEnabled(true);
                            mButtonNext.setClickable(true);
                            SharedPrefsHelper.getInstance().save(AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE, selectedDegree);
                            SharedPrefsHelper.getInstance().save(AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE_NAME, selectedDegreeName);
                            //    DatabaseReference databaseReference = ContinentApplication.getFireBaseRef().child(AppUtils.Constants.SELECTED_DEGREE).child(userID).child(AppUtils.Constants.SELECTED_DEGREE);
                            //  databaseReference.setValue(selectedDegree);
                            View rowView = view;
                            setItemSelected(rowView);
                        }
                    });

                    setItemNormal();
                    // View rowView = getViewByPosition(0, mListViewLanguage);
                    //setItemSelected(rowView);
                }
            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                mProgressbar.setVisibility(View.GONE);
                AppUtils.showToastMessage(StarterActivity.this, getString(R.string.some_thing_wrong));
            }
        });
    }
}