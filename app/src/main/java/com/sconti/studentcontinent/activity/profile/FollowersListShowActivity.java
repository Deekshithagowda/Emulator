package com.sconti.studentcontinent.activity.profile;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.profile.ui.main.SectionsPagerAdapter;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.NAME;

public class FollowersListShowActivity extends BaseActivity {
    private String name;
    private ActionBar mActionBar;
    private TextView mUserNameOfFollowersList;
    private LinearLayout mCancelButton;
    private Button mCancleButton;
    private boolean ISFROM_OTHER_PROF;

    @Override
    protected int getContentView() {
        return R.layout.activity_followers_list_show;
    }
    //comment

    @Override
    protected void initView() {
        name = SharedPrefsHelper.getInstance().get(NAME);
        mActionBar = getSupportActionBar();
        initData();
        //   initToolbar(name);

        mUserNameOfFollowersList = findViewById(R.id.UserNameOfFollowersList);
        mCancelButton = findViewById(R.id.cancel_button);
        mCancleButton = findViewById(R.id.CancleButton);

        String userID = getIntent().getStringExtra("USER_ID");
        String userName = getIntent().getStringExtra("USER_NAME");
        String PageID = getIntent().getStringExtra("ID");
        ISFROM_OTHER_PROF = getIntent().getBooleanExtra("ISFROM_OTHER_PROF", false);


        mUserNameOfFollowersList.setText(userName);
        initToolbar(userName);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), userID, ISFROM_OTHER_PROF);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        if(PageID.equals("0")){
            viewPager.setCurrentItem(0);
        } else if(PageID.equals("1")){
            viewPager.setCurrentItem(1);
        }else {
            viewPager.setCurrentItem(0);
        }
        TabLayout tabs = findViewById(R.id.tabs);

        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mCancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    private void initToolbar(String title) {
         mToolbar.setTitle(title);
       // mToolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
       // mToolbar.setTitleTextAppearance(this, R.style.RobotoBoldTextAppearance);
    }


    private Toolbar mToolbar;

    @Override
    protected void initData() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
       onBackPressed();
        return true;
    }


    @Override
    protected void initListener() {

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

    }

    protected ApiInterface apiInterface;

    private void setUpNetwork() {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }



}