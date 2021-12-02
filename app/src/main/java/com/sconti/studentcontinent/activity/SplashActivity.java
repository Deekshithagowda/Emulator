package com.sconti.studentcontinent.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.WindowManager;

import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.alltabs.SecondMainActivity;
import com.sconti.studentcontinent.activity.appinfo.AppInfoActivity;
import com.sconti.studentcontinent.activity.newlogin.LoginActivity;
import com.sconti.studentcontinent.activity.newlogin.NewLoginActivity;
import com.sconti.studentcontinent.authentication.firebase.FirebaseLoginActivity;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.COLLEGE_NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.FIRST_TIME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.THREE_SECOND;

public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";

    @Override
    protected int getContentView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               nextScreen();
           }
       }, THREE_SECOND);
    }

    @Override
    protected void initListener() {

    }

    private void nextScreen(){
        if(!SharedPrefsHelper.getInstance().get(FIRST_TIME, true) && SharedPrefsHelper.getInstance().get(COLLEGE_NAME) != null)
        {
            startActivity(new Intent(SplashActivity.this, SecondMainActivity.class));
        }
        else{
            startActivity(new Intent(SplashActivity.this, AppInfoActivity.class));
        }
        finish();
    }
}
