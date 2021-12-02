package com.sconti.studentcontinent.activity.trending;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.details.DetailsActivity;
import com.sconti.studentcontinent.activity.othersprofile.OthersProfileActivity;
import com.sconti.studentcontinent.activity.outdoor.adapter.OutDoorTagsPrefAdapter;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorPostModel;
import com.sconti.studentcontinent.activity.post.PostActivity;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.model.APIResponseModel;
import com.sconti.studentcontinent.model.CommentModel;
import com.sconti.studentcontinent.model.FollowModel;
import com.sconti.studentcontinent.model.LikeDataModel;
import com.sconti.studentcontinent.model.NewAPIResponseModel;
import com.sconti.studentcontinent.model.ShareDataModel;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;
import com.sconti.studentcontinent.utils.AppUtils;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.ASPIRATION_FIRST;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

public class TrendingActivity extends BaseActivity {

    private String mSelectedSubject;
    private View focusSelection;
    private RelativeLayout mRelativeLayout;
    private RecyclerView mRecyclerView;
    private RelativeLayout mProgressBar;
    private AlertDialog alertDialogMoreOptions;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<OutDoorPostModel> TagDataModelList;
    private FloatingActionButton floatingActionButton;
    private Menu menu;
    int offset = 20;
    private List<LikeDataModel> likeDatalist;
    private List<ShareDataModel> shareDataList;
    private List<CommentModel> commentDataList;


    @Override
    protected int getContentView() {
        return R.layout.first_preference_fragment;


    }



    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mRelativeLayout = findViewById(R.id.progress_bar);
        mProgressBar =  findViewById(R.id.progress_bar);
        floatingActionButton = findViewById(R.id.fabAddPost);


    }

    @Override
    protected void initData() {
        setUpNetwork();
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void initListener() {
        floatingActionButton.setVisibility(View.GONE);
        initView();
        initData();
        if (mSelectedSubject != null) {
            focusSelection.setVisibility(View.GONE);
        }
        setUpNetwork();
        mSwipeRefreshLayout = findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCategory("All" , 1 , 2 , offset);
            }
        });

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                getCategory("All" , 1 , 2 , offset);
            }
        });

    }


    OutDoorTagsPrefAdapter TrendingPostAdapter;
    private List<FollowModel> mFollowersList;

    private void getCategory(String category , int type , int FeatureType  , int offset) {
        mRecyclerView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(true);
        Call<NewAPIResponseModel> hashTagCall = apiInterface.GetKnowledgePost(category , type , FeatureType , offset);
        hashTagCall.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                TagDataModelList = response.body().getKnowledgeModel();
                if (TagDataModelList != null) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    Collections.reverse(TagDataModelList);
                    TrendingPostAdapter = new OutDoorTagsPrefAdapter(TrendingActivity.this, mFollowersList,  likeDatalist,commentDataList,shareDataList , TagDataModelList, selectItemInterface2, mediaController, 1);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(TrendingActivity.this, LinearLayoutManager.VERTICAL, false));
                    mRecyclerView.setAdapter(TrendingPostAdapter);
                    mRelativeLayout.setVisibility(View.GONE);
                }
            }


            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private OutDoorTagsPrefAdapter.MediaController mediaController = new OutDoorTagsPrefAdapter.MediaController() {
        @Override
        public void playVideo(PlayerView playerView, SimpleExoPlayer player, final String url) {
            //playersList.put(url, player);
        }

        @Override
        public void releaseVideo(SimpleExoPlayer player) {
            //releasePlayer(player);
        }

        @Override
        public void videoPosition(int videoPosition2) {
            //   videoPosition = videoPosition2;
        }
    };

    private OutDoorTagsPrefAdapter.SelectItem selectItemInterface2 = new OutDoorTagsPrefAdapter.SelectItem() {
        @Override
        public void selectedPosition(int position) {
            Intent intent = new Intent(TrendingActivity.this, DetailsActivity.class);
            intent.putExtra("DATA", TagDataModelList.get(position));
            intent.putExtra("TYPE", ASPIRATION_FIRST);
            intent.putExtra("ASPIRATION_COMMENT", true);
            intent.putExtra("SUBJECT", mSelectedSubject);
            startActivity(intent);
        }


        @Override
        public void sharePost(OutDoorPostModel dataModel) {

        }

//        @Override
//        public void updateLikeCount(final OutDoorPostModel dataModel) {
//            String uid = SharedPrefsHelper.getInstance().get(USER_ID, null);
//
//            LikeModel likeModel = new LikeModel();
//            likeModel.setLikeByID(uid);
//            likeModel.setLikeCount(dataModel.getNumLikes()+"");
//            likeModel.setPostId(dataModel.getPostId());
//            Call<NewAPIResponseModel>call = apiInterface.addLike(likeModel, 3);
//            call.enqueue(new Callback<NewAPIResponseModel>() {
//                @Override
//                public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
//                    Log.d(TAG, "onResponse: "+response.body().toString());
//                    if(response.body().isStatus()){
//                        for( int  i=0; i<TagDataModelList.size(); i++){
//                            if(TagDataModelList.get(i).getPostId().equalsIgnoreCase(dataModel.getPostId())){
//                                TagDataModelList.get(i).setNumLikes(TagDataModelList.get(i).getNumLikes() + 1);
//                                break;
//                            }
//                        }
//                        TrendingPostAdapter.notifyDataSetChanged();
//                    }
//
//                }
//                @Override
//                public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
//                    Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
//                }
//            });
//        }

        @Override
        public void followUnfollow(FollowModel dataModel, int type) {

        }

        @Override
        public void startVideo(ImageView videoThumbNail) {
          //  playVideoAtThis();
        }

        @Override
        public void openAlert(final OutDoorPostModel dataModel) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(TrendingActivity.this);
            LayoutInflater inflater = TrendingActivity.this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.outdoor_more_alert, null);
            TextView report, copyLink, share, delete, onNotification, edit;
            report = dialogView.findViewById(R.id.Report);
            copyLink = dialogView.findViewById(R.id.copy_link);
            share = dialogView.findViewById(R.id.share_to);
            delete = dialogView.findViewById(R.id.delete);
            onNotification = dialogView.findViewById(R.id.turn_on_post_noti);
            edit = dialogView.findViewById(R.id.edit);

            String uid = SharedPrefsHelper.getInstance().get(USER_ID, null);

            if (dataModel.getPostedById().equalsIgnoreCase(uid)) {
                delete.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
            } else {
                delete.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
            }

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(TrendingActivity.this, PostActivity.class);
                    intent.putExtra("OUTDOOR_DATA", dataModel);
                    intent.putExtra("TYPE", ASPIRATION_FIRST);
                    intent.putExtra("SUBJECT", mSelectedSubject);
                    startActivity(intent);
                    alertDialogMoreOptions.dismiss();
                    alertDialogMoreOptions = null;
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Toast.makeText(mActivity, "Delete", Toast.LENGTH_LONG).show();
                    deletePost(dataModel.getPostId());
                }
            });

            alertBuilder.setView(dialogView);
            alertDialogMoreOptions = alertBuilder.create();
            alertDialogMoreOptions.show();
        }

        @Override
        public void reachedLastItem() {
            //getMoreData();
        }


        @Override
        public void clickOnProfile(String id) {
            if(!id.equalsIgnoreCase(SharedPrefsHelper.getInstance().get(USER_ID).toString())) {
                Intent intent = new Intent(TrendingActivity.this, OthersProfileActivity.class);
                intent.putExtra("USER_ID", id);
                startActivity(intent);
            }
        }

        @Override
        public void updateLikeCount(OutDoorPostModel dataModel, boolean isLiked) {

        }
    };

    private void deletePost(String postId) {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<List<APIResponseModel>> hashTagCall = apiInterface.deletePost(postId, 1);
        hashTagCall.enqueue(new Callback<List<APIResponseModel>>() {
            @Override
            public void onResponse(Call<List<APIResponseModel>> call, Response<List<APIResponseModel>> response) {
                mProgressBar.setVisibility(View.GONE);
                AppUtils.showToastMessage(TrendingActivity.this, TrendingActivity.this.getString(R.string.post_deleted_success));
                alertDialogMoreOptions.dismiss();
            }

            @Override
            public void onFailure(Call<List<APIResponseModel>> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
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