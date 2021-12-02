package com.sconti.studentcontinent.activity.appinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.SplashActivity;
import com.sconti.studentcontinent.activity.newlogin.NewLoginActivity;
import com.sconti.studentcontinent.base.BaseActivity;

public class AppInfoActivity extends BaseActivity {

    private ViewPager mViewPager;
    private LinearLayout mBottomLayout;

    private SliderAdapter sliderAdapter;
    private int mCurrentPageNumber;
    private TextView[] mDots;

    private Button mNextBtn , mBackBtn , mSkip , mGotIt;


    @Override
    protected int getContentView() {
        return R.layout.activity_app_info;
    }


    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.ViewPager);
        mBottomLayout = findViewById(R.id.bottomLayout);
        mNextBtn = findViewById(R.id.next);
        mBackBtn = findViewById(R.id.Pervioes);
        mSkip = findViewById(R.id.Skip);
        mGotIt = findViewById(R.id.GotIt);
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        sliderAdapter = new SliderAdapter(this);

        mViewPager.setAdapter(sliderAdapter);


        addDots(0);
        mViewPager.addOnPageChangeListener(viewLister);

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(mCurrentPageNumber + 1);
            }
        });

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(mCurrentPageNumber - 1);
                mNextBtn.setVisibility(View.VISIBLE);
                mSkip.setVisibility(View.VISIBLE);
                mGotIt.setVisibility(View.GONE);
            }
        });

        mSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppInfoActivity.this, NewLoginActivity.class));
                finish();
            }
        });

        mGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppInfoActivity.this, NewLoginActivity.class));
                finish();
            }
        });


    }

    public void addDots(int position){

        mDots = new TextView[3];

        for(int i=0; i< mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setTextSize(35);
            mBottomLayout.addView(mDots[i]);
        }

    }


    ViewPager.OnPageChangeListener viewLister = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {
            addDots(i);

            mCurrentPageNumber = i;

            if(i == 0){

                mNextBtn.setEnabled(true);
                mBackBtn.setVisibility(View.GONE);
                mNextBtn.setVisibility(View.VISIBLE);
                mGotIt.setVisibility(View.GONE);
                mSkip.setVisibility(View.VISIBLE);
                mNextBtn.setText("Next");

            }else if(i == mDots.length - 1){
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);
                mNextBtn.setVisibility(View.GONE);
                mGotIt.setVisibility(View.GONE);
                mSkip.setVisibility(View.VISIBLE);

                mBackBtn.setText("Previous");


                mSkip.setVisibility(View.GONE);
                mGotIt.setVisibility(View.VISIBLE);

            }else {
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);
                mNextBtn.setVisibility(View.VISIBLE);
                mGotIt.setVisibility(View.GONE);
                mSkip.setVisibility(View.VISIBLE);

                mBackBtn.setText("Previous");
                mNextBtn.setText("Next");

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}