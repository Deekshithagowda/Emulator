package com.sconti.studentcontinent.activity.othersprofile;


import android.util.Log;

import com.sconti.studentcontinent.model.APIResponseModel;
import com.sconti.studentcontinent.model.FollowModel;
import com.sconti.studentcontinent.model.NewAPIResponseModel;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OthersProfilePresenterImpl implements OthersProfilePresenter{
    private static final String TAG = "OthersProfilePresenterI";
    private OthersProfileView mOthersProfileView;
    protected ApiInterface apiInterface;
    private int iamFollowing = 0;
    private int IHaveFollowers = 0;
    private List<FollowModel> followersModel;
    private List<FollowModel> followingModel;

    public OthersProfilePresenterImpl(OthersProfileView othersProfileView)
    {
        this.mOthersProfileView = othersProfileView;
        setUpNetwork();
    }




    public OthersProfilePresenterImpl() {
    }

    @Override
    public void getProfileData(String userdId) {
        Call<NewAPIResponseModel> userDetailsCall = apiInterface.GetOthersProfile(userdId);
        mOthersProfileView.showProgressbar();
        userDetailsCall.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                mOthersProfileView.hideProgressbar();
                if(response.isSuccessful() && response.body()!=null &&response.body().isStatus()) {
                    mOthersProfileView.updateProfileData(response.body().getmUserDetails());
                }
            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
                mOthersProfileView.hideProgressbar();
            }
        });
    }

    @Override
    public void addFollower(FollowModel followModel) {
        Call<NewAPIResponseModel> apiResponseModelCall = apiInterface.AddFollower(followModel);
        mOthersProfileView.showProgressbar();
        apiResponseModelCall.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                mOthersProfileView.hideProgressbar();
                Log.d(TAG, "onResponse: added");
                if(response.isSuccessful() && response.body().isStatus())
                {
                    mOthersProfileView.updateAddedFollowers(response.body().isStatus());
                }
            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                Log.d(TAG, "onFailure: for adding followers");
                mOthersProfileView.hideProgressbar();
            }
        });
    }

    @Override
    public void UnFollower(FollowModel followModel) {
        Call<APIResponseModel> apiResponseModelCall = apiInterface.UnFollow(followModel.getFollowee_id(), followModel.getFollower_id());
        mOthersProfileView.showProgressbar();
        apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                mOthersProfileView.hideProgressbar();
                if(response.isSuccessful() && response.body().isStatus())
                {
                    mOthersProfileView.updateAddedFollowers(response.body().isStatus());
                }
            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                mOthersProfileView.hideProgressbar();
            }

        });
    }

    @Override
    public void getFollowers(final String id) {
        Call<NewAPIResponseModel> listCall = apiInterface.GetFollowers(id, "0" );
        mOthersProfileView.showProgressbar();
        listCall.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                iamFollowing = response.body().getmFollowList().size();
                followingModel = response.body().getmFollowList();
                Call<NewAPIResponseModel> listCall2 = apiInterface.GetFollowers(id, "1" );
                listCall2.enqueue(new Callback<NewAPIResponseModel>() {
                    @Override

                    public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                        mOthersProfileView.hideProgressbar();
                        IHaveFollowers = response.body().getmFollowList().size();
                        mOthersProfileView.updateFollowers(iamFollowing, IHaveFollowers, followingModel, response.body().getmFollowList() );
                    }

                    @Override
                    public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                        Log.d(TAG, "onFailure: ");
                        mOthersProfileView.hideProgressbar();
                    }
                });
            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
                mOthersProfileView.hideProgressbar();
            }
        });
    }

    private void setUpNetwork() {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }
}
