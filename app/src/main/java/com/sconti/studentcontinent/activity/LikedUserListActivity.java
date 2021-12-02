package com.sconti.studentcontinent.activity;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.adapter.userLikeListAdapter;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.model.NewAPIResponseModel;
import com.sconti.studentcontinent.model.UserDetails;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;
import com.sconti.studentcontinent.utils.AppUtils;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikedUserListActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
   // private List<UserDetails> userDetailsList;
    private List<UserDetails> LikeDataList;
    private String POSTID;
    private int TYPE;
    private LinearLayout mCancelButton;
    private ActionBar mActionBar;


    @Override
    protected int getContentView() {
        return R.layout.activity_liked_user_list;
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recycler_LikeList);
        mActionBar = getSupportActionBar();
        initData();
        initToolbar("");

    }

    private void initToolbar(String title) {
        mToolbar.setTitle(title);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        mToolbar.setTitleTextAppearance(this, R.style.RobotoBoldTextAppearance);
    }

    public Toolbar mToolbar;

    @Override
    protected void initData() {
        POSTID = getIntent().getStringExtra("POSTID");
        TYPE = getIntent().getIntExtra("TYPE",0);
        mToolbar = findViewById(R.id.toolbar);
        mCancelButton = findViewById(R.id.cancel_button);
        setSupportActionBar(mToolbar);

    }

    @Override
    protected void initListener() {
        setUpNetwork();
        if(POSTID!=null) {
            getUser(POSTID, TYPE);
        }

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private com.sconti.studentcontinent.adapter.userLikeListAdapter userLikeListAdapter;
    private void getUser(String postId , int type) {
        Call<NewAPIResponseModel> UserCall = apiInterface.GetLike(postId , type);
        UserCall.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                LikeDataList =  response.body().getUsertable();
                if (LikeDataList != null && LikeDataList.size()>0) {
                    Collections.reverse(LikeDataList);
                    userLikeListAdapter = new userLikeListAdapter(LikedUserListActivity.this,LikeDataList);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(LikedUserListActivity.this, LinearLayoutManager.VERTICAL, false));
                    mRecyclerView.setAdapter(userLikeListAdapter);
                }
            }
            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                AppUtils.showToastMessage(LikedUserListActivity.this,getString(R.string.some_thing_wrong));
                Log.d("12345", "onFailure: " + toString().trim());
            }
        });

    }

    protected ApiInterface apiInterface;
    private void setUpNetwork() {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        LikedUserListActivity.this.getMenuInflater().inflate(R.menu.searchview, menu);
        SearchManager searchManager = (SearchManager) getApplication().getSystemService(Context.SEARCH_SERVICE);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(LikedUserListActivity.this.getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(userLikeListAdapter != null){
                    userLikeListAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



}