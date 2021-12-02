package com.sconti.studentcontinent.activity.outdoor;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.aspiration.AspirationTabActivity;
import com.sconti.studentcontinent.activity.games.GamesTabActivity;
import com.sconti.studentcontinent.activity.knowledge.KnowledgeTabActivity;
import com.sconti.studentcontinent.activity.outdoor.adapter.OutDoorSectionsPagerAdapter;
import com.sconti.studentcontinent.activity.post.PostActivity;
import com.sconti.studentcontinent.activity.ui.main.SectionsPagerAdapter;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.ASPIRATION_FIRST;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.OUTDOOR_FIRST;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_ASPIRATION;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_OUTDOOR;

public class OutDoorTabActivity extends BaseActivity {
    private static final String TAG = "AspirationTabActivity";
    private ActionBar mActionBar;
    private String mSelectedSubject;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_game_new_tab_outdoor;
    }

    @Override
    protected void initView() {
        mActionBar = getSupportActionBar();
        OutDoorSectionsPagerAdapter sectionsPagerAdapter = new OutDoorSectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        floatingActionButton = findViewById(R.id.fabAddPost);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                mSelectedSubject = SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_OUTDOOR, null);
                Intent intent = new Intent(OutDoorTabActivity.this, PostActivity.class);
                intent.putExtra("TYPE", OUTDOOR_FIRST);
                intent.putExtra("FAV", mSelectedSubject);
                startActivity(intent);
            }
        });
        tabs.setupWithViewPager(viewPager);
        initToolbar();
      /*
       FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        BottomNavigationView navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_outdoor);

   /*     final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.htab_collapse_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.htab_appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Title");
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");//careful there should outdooricon space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });*/
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(OutDoorTabActivity.this, KnowledgeTabActivity.class));
                    overridePendingTransition(R.anim.left_right, R.anim.right_left);
                    finish();
                    return true;
                case R.id.navigation_outdoor:
                    // toolbar.setTitle("My Gifts");
                    return true;
                case R.id.navigation_games:
                    startActivity(new Intent(OutDoorTabActivity.this, GamesTabActivity.class));
                    overridePendingTransition(R.anim.enter, R.anim.exit);

                    finish();
                    return true;
                case R.id.navigation_aspiration:
                    startActivity(new Intent(OutDoorTabActivity.this, AspirationTabActivity.class));
                    overridePendingTransition(R.anim.enter, R.anim.exit);

                    finish();
                    return true;
            }
            return false;
        }
    };

    private void initToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(getString(R.string.outdoor));
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }


    @Override
    protected void initData() {
        if(mActionBar!=null){
            mActionBar.setTitle(R.string.games);
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setShowHideAnimationEnabled(true);
        }
    }

    @Override
    protected void initListener() {

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
}