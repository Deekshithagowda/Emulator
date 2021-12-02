package com.sconti.studentcontinent.activity.profile.ui.main;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.adapter.FollowerAdapter;
import com.sconti.studentcontinent.base.BaseFragment;
import com.sconti.studentcontinent.model.APIResponseModel;
import com.sconti.studentcontinent.model.FollowModel;
import com.sconti.studentcontinent.model.NewAPIResponseModel;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

public class FollowersFragment extends BaseFragment {
    private static final String TAG = "FollowersFragment";
    private View mView;
    private RecyclerView follow_recycler;
    private List<FollowModel> mFollowersModelList;
    private String userID ;
    private SearchView mSearchView;
    private List<FollowModel> myFollowingList;
    private boolean ISFROM_OTHER_PROF;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.fragment_followerslist, container, false);
        Bundle bundle=getArguments();
        userID = bundle.getString("USER_ID");
        ISFROM_OTHER_PROF = bundle.getBoolean("ISFROM_OTHER_PROF");
        initView();
        initData();
        return mView;
    }

    private void initView() {
        follow_recycler = mView.findViewById(R.id.follow_recycler);
      //  mSearchView= mView.findViewById(R.id.SearchView);
        initListener();
    }
    private void initData() {
    }

    private void initListener() {
        setUpNetwork();
        getMyFollwers();

//        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                if(followAdapter != null){
//                    followAdapter.getFilter().filter(newText);
//                }
//                return true;
//            }
//        });

    }

    private void getMyFollwers() {
        Call<NewAPIResponseModel> listCall = apiInterface.GetFollowers(SharedPrefsHelper.getInstance().get(USER_ID).toString(), "0");
        listCall.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                myFollowingList = response.body().getmFollowList();
                getFollowers();
            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {

            }

        });
    }





    FollowerAdapter followingAdapter;
    FollowerAdapter.SelectItem selectItem = new FollowerAdapter.SelectItem() {
        @Override
        public void followUnfollow(FollowModel dataModel, int type) {
            if(type==2)
            {
                Call<APIResponseModel> call = apiInterface.UnFollow(dataModel.getFollowee_id(), dataModel.getFollower_id());
                call.enqueue(new Callback<APIResponseModel>() {
                    @Override
                    public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {

                    }

                    @Override
                    public void onFailure(Call<APIResponseModel> call, Throwable t) {

                    }
                });
            }
            else
            {
               Call<NewAPIResponseModel> call = apiInterface.AddFollower(dataModel);
               call.enqueue(new Callback<NewAPIResponseModel>() {
                   @Override
                   public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {

                   }

                   @Override
                   public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {

                   }
               });
            }
        }
    };

    private void getFollowers() {
        Call<NewAPIResponseModel> listCall = apiInterface.GetFollowers(userID, "1");
        listCall.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                mFollowersModelList = response.body().getmFollowList();
                if (mFollowersModelList != null && mFollowersModelList.size()>0) {
                    Collections.reverse(mFollowersModelList);
                    followingAdapter = new FollowerAdapter(getContext(), mFollowersModelList, myFollowingList, selectItem, ISFROM_OTHER_PROF);
                    follow_recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    follow_recycler.setAdapter(followingAdapter);
                    followingAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {

            }

        });

    }

    protected ApiInterface apiInterface;
    private void setUpNetwork() {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }

 //   FollowAdapter followAdapter;
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.searchview, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(followingAdapter != null){
                    followingAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

}
