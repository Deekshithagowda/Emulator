package com.sconti.studentcontinent.activity.profile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.adapter.ProfileUserListAdapter;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.model.APIResponseModel;
import com.sconti.studentcontinent.model.UserDetails;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;
import com.sconti.studentcontinent.utils.AppUtils;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileSearchActivity extends BaseActivity  {
    private List<UserDetails> userDetailsList;
    private RecyclerView mUserProfileList;
    private String UserNames;
    private LinearLayout mCancelButton;
    private ActionBar mActionBar;

    @Override
    protected int getContentView() {



        return R.layout.activity_user_profile_search;
    }

    @Override
    protected void initView() {
        setUpNetwork();
        mActionBar = getSupportActionBar();
        initData();
        getUser("");
        initToolbar("Search");
        mUserProfileList = findViewById(R.id.UserProfileList);

    }

    ProfileUserListAdapter UserListAdapter;
    private void getUser(String name) {
        Call<APIResponseModel> UserCall = apiInterface.GetUser(name);
        UserCall.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                userDetailsList = response.body().getUsertable();
                if (userDetailsList != null && userDetailsList.size()>0) {
                    mUserProfileList.setVisibility(View.VISIBLE);
                    Collections.reverse(userDetailsList);
                    UserListAdapter = new ProfileUserListAdapter(UserProfileSearchActivity.this,userDetailsList);
                    mUserProfileList.setLayoutManager(new LinearLayoutManager(UserProfileSearchActivity.this, LinearLayoutManager.VERTICAL, false));
                    mUserProfileList.setAdapter(UserListAdapter);
                }else {
                    UserListAdapter = new ProfileUserListAdapter(UserProfileSearchActivity.this,userDetailsList);
                    mUserProfileList.setLayoutManager(new LinearLayoutManager(UserProfileSearchActivity.this, LinearLayoutManager.VERTICAL, false));
                    mUserProfileList.setAdapter(UserListAdapter);
                    UserListAdapter.notifyDataSetChanged();
                    AppUtils.showToastMessage(UserProfileSearchActivity.this,getString(R.string.no_user_data));
                }
            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                AppUtils.showToastMessage(UserProfileSearchActivity.this,getString(R.string.some_thing_wrong));
            }
        });

    }


    private void initToolbar(String title) {
        mToolbar.setTitle(title);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        mToolbar.setTitleTextAppearance(this, R.style.RobotoBoldTextAppearance);
    }


    private Toolbar mToolbar;
    @Override
    protected void initData() {
        mToolbar = findViewById(R.id.toolbar);
        mCancelButton = findViewById(R.id.cancel_button);
        setSupportActionBar(mToolbar);

     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     //   getSupportActionBar().setDisplayShowHomeEnabled(true);


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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        UserProfileSearchActivity.this.getMenuInflater().inflate(R.menu.searchview, menu);
        SearchManager searchManager = (SearchManager)UserProfileSearchActivity.this.getSystemService(Context.SEARCH_SERVICE);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(UserProfileSearchActivity.this.getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                getUser(newText);
                return true;
            }
        });
        return true;
    }

    protected ApiInterface apiInterface;

    private void setUpNetwork() {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }

}