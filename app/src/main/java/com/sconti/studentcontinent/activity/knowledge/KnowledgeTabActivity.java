package com.sconti.studentcontinent.activity.knowledge;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.MenuItem;
import android.view.View;

import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.aspiration.AspirationTabActivity;
import com.sconti.studentcontinent.activity.games.GamesTabActivity;
import com.sconti.studentcontinent.activity.getcategories.GetCategoryActivity;
import com.sconti.studentcontinent.activity.knowledge.adapter.KnowledgeSectionsPagerAdapter;
import com.sconti.studentcontinent.activity.outdoor.OutDoorTabActivity;
import com.sconti.studentcontinent.activity.post.PostActivity;
import com.sconti.studentcontinent.adapter.KnowledgeFirstPrefAdapter;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.model.FirstPreferenceDataModel;
import com.sconti.studentcontinent.utils.AppUtils;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE;

public class KnowledgeTabActivity extends BaseActivity implements KnowledgeFirstPrefAdapter.SelectItem {
    private static final String TAG = "KnowledgeTabActivity";
    private ActionBar mActionBar;
    private FloatingActionButton floatingActionButton;
    private String mSelectedSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_game_new_knowledgw;
    }

    @Override
    protected void initView() {
        mActionBar = getSupportActionBar();
        mSelectedSubject = SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE, null);
        if(mSelectedSubject!=null)
        {
            startFetchingData();
        }
        else {
           Intent intent = new Intent(KnowledgeTabActivity.this, GetCategoryActivity.class);
            intent.putExtra("CATEGORY", "1");
            startActivity(intent);
        }
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

    private void startFetchingData() {
        KnowledgeSectionsPagerAdapter sectionsPagerAdapter = new KnowledgeSectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        floatingActionButton = findViewById(R.id.fabAddPost);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                mSelectedSubject = SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE, null);
                Intent intent = new Intent(KnowledgeTabActivity.this, PostActivity.class);
                intent.putExtra("TYPE", AppUtils.Constants.KNOWLEDGE_FIRST);
                intent.putExtra("FAV", mSelectedSubject);
                startActivity(intent);
            }
        });
        tabs.setupWithViewPager(viewPager);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //Toast.makeText(KnowledgeTabActivity.this, "Home", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.navigation_outdoor:
                    startActivity(new Intent(KnowledgeTabActivity.this, OutDoorTabActivity.class));
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    finish();
                    return true;
                case R.id.navigation_games:
                    startActivity(new Intent(KnowledgeTabActivity.this, GamesTabActivity.class));
                    //overridePendingTransition(R.anim.left_right, R.anim.right_left);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    finish();
                    return true;
                case R.id.navigation_aspiration:
                    startActivity(new Intent(KnowledgeTabActivity.this, AspirationTabActivity.class));
                    //overridePendingTransition(R.anim.left_right, R.anim.right_left);
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
        setTitle(getString(R.string.knowledge));

    }


    @SuppressLint("RestrictedApi")
    @Override
    protected void initData() {
        if(mActionBar!=null){
            mActionBar.setTitle(R.string.knowledge);
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

    @Override
    public void selectedPosition(int position) {

    }

    @Override
    public void openAlert(FirstPreferenceDataModel dataModel) {

    }

    @Override
    public void reachedLastItem() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSelectedSubject = SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE, null);
        if(mSelectedSubject!=null)
        {
            startFetchingData();
        }
    }
}