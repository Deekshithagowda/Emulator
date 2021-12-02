package com.sconti.studentcontinent.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;

public abstract class BaseActivity extends AppCompatActivity {

    protected static final String TAG = BaseActivity.class.getName();

    public Context context;
    public Activity activity;
    private String currentLanguage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        activity = this;
        setContentView(getContentView());
        initView();
        initData();
        initListener();
        setUpNetwork();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected abstract int getContentView();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected ApiInterface apiInterface;
    private void setUpNetwork()
    {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }
}
