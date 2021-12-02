package com.sconti.studentcontinent.activity.starterscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.sconti.studentcontinent.ContinentApplication;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.MainActivity;
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
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_ASPIRANT;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_DEGREE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_ASPIRATION;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_ASPIRATION_NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

public class WhatYouWantActivity extends BaseActivity {
    private static final String TAG = "WhatYouWantActivity";
    private TextView mTextViewUsername;
    private Spinner spinner;
    private List<String> degrees;
    private DegreeListAdapter mDegreeListAdapter;
    private ListView mListViewLanguage;
    private CircularProgressButton mButtonNext;
    private String selectedAspirant, selectedAspirantName;
    private ProgressBar mProgressbar;


    @Override
    protected int getContentView() {
        return R.layout.activity_what_you_want;
    }

    @Override
    protected void initView() {
        mProgressbar = findViewById(R.id.progressbar);
        mTextViewUsername = findViewById(R.id.username);
        mListViewLanguage = findViewById(R.id.listViewLanguage);
        mButtonNext = findViewById(R.id.buttonNext);
        mButtonNext.setClickable(false);
        mButtonNext.setEnabled(false);
    }

    @Override
    protected void initData() {
        String name = "Hi, " + SharedPrefsHelper.getInstance().get(AppUtils.Constants.NAME);
       // mTextViewUsername.setText(name);
        setUpNetwork();
        getAspiration();
    }

    @Override
    protected void initListener() {
        degrees = new ArrayList<>();
        degrees.add("IPS");
        degrees.add("IAS");
        degrees.add("IFS");

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefsHelper.getInstance().save(SELECTED_SUBJECT_ASPIRATION, selectedAspirant);
                BackgroundService.startActionAspiration(WhatYouWantActivity.this);
                if(SharedPrefsHelper.getInstance().get(COLLEGE_NAME)==null)
                {
                    startActivity(new Intent(WhatYouWantActivity.this, CollegeSelectionActivity.class));
                }
                else {
                    startActivity(new Intent(WhatYouWantActivity.this, SecondMainActivity.class));
                }
                finish();
            }
        });

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
    private void getAspiration() {
        mProgressbar.setVisibility(View.VISIBLE);
        Call<APIResponseModel> modelCall = apiInterface.getAspiration();
        modelCall.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                mProgressbar.setVisibility(View.GONE);
                if(response.isSuccessful() && response.body().getDegreeAspirationModelList().size()>0)
                {
                    degrees = new ArrayList<>();
                    degreeAspirationModelList = new ArrayList<>();
                    degreeAspirationModelList = response.body().getDegreeAspirationModelList();
                    for(int i=0;i<response.body().getDegreeAspirationModelList().size(); i++)
                    {
                        degrees.add(response.body().getDegreeAspirationModelList().get(i).getName());
                    }
                    mDegreeListAdapter = new DegreeListAdapter(degrees, WhatYouWantActivity.this);
                    mListViewLanguage.setAdapter(mDegreeListAdapter);
                    mListViewLanguage.requestFocusFromTouch();
                    mListViewLanguage.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    mListViewLanguage.setItemChecked(0, true);
                    mListViewLanguage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            setItemNormal();
                            View rowView = view;
                            mListViewLanguage.setSelector(R.color.primaryTextColor);
                            selectedAspirant = degreeAspirationModelList.get(i).getId();
                            selectedAspirantName = degreeAspirationModelList.get(i).getName();
                            mButtonNext.setEnabled(true);
                            mButtonNext.setClickable(true);
                            String userId = SharedPrefsHelper.getInstance().get(USER_ID);
                            SharedPrefsHelper.getInstance().save(SELECTED_SUBJECT_ASPIRATION, selectedAspirant);
                            SharedPrefsHelper.getInstance().save(SELECTED_SUBJECT_ASPIRATION_NAME, selectedAspirantName);
                            // DatabaseReference databaseReference = ContinentApplication.getFireBaseRef().child(SELECTED_SUBJECT_ASPIRATION).child(userId).child(SELECTED_SUBJECT_ASPIRATION);
                            //  databaseReference.setValue(selectedAspirant);
                            setItemSelected(rowView);
                        }
                    });

                    setItemNormal();
                    //  View rowView = getViewByPosition(0, mListViewLanguage);
                    //  setItemSelected(rowView);
                }

            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                mProgressbar.setVisibility(View.GONE);
                AppUtils.showToastMessage(WhatYouWantActivity.this, getString(R.string.some_thing_wrong));
            }
        });
    }
}