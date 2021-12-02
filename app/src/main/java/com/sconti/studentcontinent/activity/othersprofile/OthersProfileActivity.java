package com.sconti.studentcontinent.activity.othersprofile;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.details.DetailsActivity;
import com.sconti.studentcontinent.activity.outdoor.adapter.OutDoorTagsPrefAdapter;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorPostModel;
import com.sconti.studentcontinent.activity.post.PostActivity;
import com.sconti.studentcontinent.activity.profile.SectionsProfilePagerAdapter;
import com.sconti.studentcontinent.adapter.FollowingAdapter;
import com.sconti.studentcontinent.activity.profile.FollowersListShowActivity;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.model.APIResponseModel;
import com.sconti.studentcontinent.model.CommentModel;
import com.sconti.studentcontinent.model.FollowModel;
import com.sconti.studentcontinent.model.LikeDataModel;
import com.sconti.studentcontinent.model.NewAPIResponseModel;
import com.sconti.studentcontinent.model.ShareDataModel;
import com.sconti.studentcontinent.model.UserDetails;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;
import com.sconti.studentcontinent.utils.AppUtils;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;
import com.sconti.studentcontinent.utils.tools.ContinentTools;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.ASPIRATION_FIRST;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

public class OthersProfileActivity extends BaseActivity implements OthersProfileView, View.OnClickListener {
    private static final String TAG = "OthersProfileActivity";
    private OthersProfilePresenter mOthersProfilePresenter;
    private String userId , CurrentUserName;
    private CircleImageView mImageViewProfile , mimageViewAvatar;
    private Button mProfileLogout ;
    private TextView mTextViewDegree, mTextViewInterest, mTextViewName, mTextViewFollowing , mTextViewSelectedCollage , navUsername , navUserEmail , mPostCountheader , mfollowersbtnHeader , mfollowingbtnHeader ;
    private RecyclerView mFollowRecyclerView;
    private TextView mFollowProfileing, mFollowProfileers ;
    private LinearLayout  mFollowing , mFollowers , mFollowerList;
    private ProgressBar mProgressBar;
    private TextView mSearchView;
    private ImageView mLogOutUser , mUserChat , mProfileBackground ;
    private List<OutDoorPostModel> TagDataModelList;
    private RecyclerView mRecyclerView;
    private List<FollowModel> mFollowersList;
    OutDoorTagsPrefAdapter PostAdapter;
    private String mSelectedSubject;
    private List<LikeDataModel> likeDatalist;
    private AlertDialog alertDialogMoreOptions;
    private TabLayout tabs;
    private ViewPager viewPager;
    private LinearLayout mBackButton;
    private TextView mFollowProfile;


    private ImageView mOpenDrawable;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    ShimmerFrameLayout shimmerFrameLayout;
    private List<ShareDataModel> shareDataList;
    private List<CommentModel> commentDataList;


    @Override
    protected int getContentView() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void initView() {
        shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmerLayout);
        mImageViewProfile = findViewById(R.id.imageViewProfile);
        mProfileLogout = findViewById(R.id.logout);
        mFollowProfile = findViewById(R.id.EditProfile);
     //   mFollowProfile.setText("Follow");
        mProgressBar = findViewById(R.id.progress_circular);
        mProfileLogout.setVisibility(View.INVISIBLE);
        mTextViewDegree = findViewById(R.id.textViewSelectedDegree);
        mTextViewInterest = findViewById(R.id.textViewSelectedAspirant);
        mTextViewName = findViewById(R.id.textViewName);
        mTextViewFollowing = findViewById(R.id.textViewFollowing);
        mFollowRecyclerView = findViewById(R.id.follow_recycler);
        mFollowProfileing = findViewById(R.id.followingbtn);
        mFollowProfileers = findViewById(R.id.followersbtn);
        mSearchView = findViewById(R.id.SearchView);
       // mLogOutUser = findViewById(R.id.LogOutUser);
        mUserChat = findViewById(R.id.UserChat);
        mTextViewSelectedCollage = findViewById(R.id.textViewSelectedCollage);
        mFollowing = findViewById(R.id.Following);
        mFollowers = findViewById(R.id.Followers);
        mRecyclerView = findViewById(R.id.UserPostList);
        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.view_pager);
        mBackButton = findViewById(R.id.BackButton);
       // mFollowerList = findViewById(R.id.FollowerList);
        mProfileBackground = findViewById(R.id.ProfileBackground);


//        mOpenDrawable = findViewById(R.id.OpenDrawable);
//
//        mDrawerLayout = findViewById(R.id.drawer_layout);
//        navigationView =  findViewById(R.id.nav_view);
//        View headerView = navigationView.getHeaderView(0);
//        navUsername =  headerView.findViewById(R.id.textViewName);
//        navUserEmail = headerView.findViewById(R.id.textViewEmail);
//        mimageViewAvatar = headerView.findViewById(R.id.imageViewAvatar);
//        mPostCountheader = headerView.findViewById(R.id.PostCountheader);
//        mfollowersbtnHeader = headerView.findViewById(R.id.followersbtnHeader);
//        mfollowingbtnHeader = headerView.findViewById(R.id.followingbtnHeader);
//
//
//        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);

    }


//    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
//            = new NavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.knowledge:
//                    Toast.makeText(OthersProfileActivity.this, "Other Profile", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//            mDrawerLayout.closeDrawer(GravityCompat.START);
//            return true;
//        }
//    };/


    @Override
    protected void initData() {
        userId = getIntent().getStringExtra("USER_ID");
        mOthersProfilePresenter = new OthersProfilePresenterImpl(this);
        Log.d(TAG, "initData: logdId:" + userId);
        mOthersProfilePresenter.getProfileData(userId);
        mOthersProfilePresenter.getFollowers(userId);

        shimmerFrameLayout.startShimmer();

        setUpNetwork();
        getFollowersList();
        getAllPost(userId);
    }

    private void getLikeListByUserID() {
        String userID = SharedPrefsHelper.getInstance().get(USER_ID);
        Log.d(TAG, "IDD" + userID);
        Call<NewAPIResponseModel> call = apiInterface.GetLikeListByUserID(userID, 3);
        call.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                likeDatalist = response.body().getLikeDatalist();
                //Log.d(TAG, "onResponse: Model"+likeDataModels.get(1).getPostID());
                if (likeDatalist != null) {
                    if (PostAdapter != null) {
                        PostAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {

            }
        });

    }



    @Override
    protected void initListener() {

        mFollowProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (!userId.equalsIgnoreCase(SharedPrefsHelper.getInstance().get(USER_ID).toString()))
                {
                    FollowModel followModel = new FollowModel();
                    followModel.setFollowee_id(userId);
                    followModel.setFollower_id(SharedPrefsHelper.getInstance().get(USER_ID).toString());
                    if(mFollowProfile.getText().toString().equalsIgnoreCase("Follow"))
                    {
                        mOthersProfilePresenter.addFollower(followModel);
                    }
                    else if(mFollowProfile.getText().toString().equalsIgnoreCase("Un Follow"))
                    {
                        mOthersProfilePresenter.UnFollower(followModel);
                    }
                }
            }
        });




        SectionsProfilePagerAdapter sectionsPagerAdapter = new SectionsProfilePagerAdapter(getSupportFragmentManager(), this, userId);
        viewPager.setAdapter(sectionsPagerAdapter);


        tabs.setupWithViewPager(viewPager);


        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        mFollowers.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mFollowingModelList != null) {
//                    followDisplay(mFollowingModelList);
//                    Intent intent = new Intent(OthersProfileActivity.this, FollowersListShowActivity.class);
//                    intent.putExtra("USER_ID", userId);
//                    intent.putExtra("USER_NAME", "UserName");
//                    intent.putExtra("ID", 0);
//                    startActivity(intent);
//                }
//
//            }
//        });
//
//        mFollowing.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mFollowingModelList != null) {
//                    followDisplay(mFollowersModelList);
//                    Intent intent = new Intent(OthersProfileActivity.this, FollowersListShowActivity.class);
//                    intent.putExtra("USER_ID", userId);
//                    intent.putExtra("USER_NAME", "UserName");
//                    intent.putExtra("ID", 1);
//                    startActivity(intent);
//                }
//            }
//        });


//        mOpenDrawable.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("RtlHardcoded")
//            @Override
//            public void onClick(View v) {
//                mDrawerLayout.openDrawer(Gravity.LEFT);
//            }
//        });


//        mSearchView.setVisibility(View.GONE);
//        mLogOutUser.setVisibility(View.GONE);

    }

    @Override
    public void updateProfileData(final UserDetails userDetails) {
        if(userDetails != null){
            if (userDetails.getImageURL() != null && userDetails.getImageURL().length() > 0)
                Picasso.with(this).load(userDetails.getImageURL()).placeholder(R.drawable.ic_user_profile).into(mImageViewProfile);
//            if (userDetails.getB() != null && userDetails.getImageURL().length() > 0)
//                Picasso.with(this).load(userDetails.getImageURL()).placeholder(R.drawable.ic_user_profile).into(mImageViewProfile);
            if (userDetails.getDegree() != null)
                mTextViewDegree.setText(userDetails.getDegree());
            if (userDetails.getInterest() != null)
                mTextViewInterest.setText(userDetails.getInterest());
            if (userDetails.getName() != null)
                mTextViewName.setText(capitalizeWord(userDetails.getName()));
            if (userDetails.getVillage() != null)
                mTextViewSelectedCollage.setText(userDetails.getVillage());
            CurrentUserName = userDetails.getName();
            mFollowing.setOnClickListener(this);
            mFollowers.setOnClickListener(this);
        }




    }

    @Override
    public void showProgressbar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        mProgressBar.setVisibility(View.INVISIBLE);

    }

    private int IamFollowing, IhaveFollowers;
    private List<FollowModel> mFollowingModelList, mFollowersModelList;

    @Override
    public void updateFollowers(int IamFollowing, int IhaveFollowers, List<FollowModel> followingModel, List<FollowModel> body) {
        this.IamFollowing = IamFollowing;
        this.IhaveFollowers = IhaveFollowers;
        this.mFollowersModelList = body;
        this.mFollowingModelList = followingModel;
        String msg = "I am following " + IamFollowing + " people and have " + IhaveFollowers + " followers";
        mTextViewFollowing.setText(msg);
        if (body.size() == 0) {
            mFollowProfile.setVisibility(View.VISIBLE);
        }
        mFollowProfileing.setText(followingModel.size()+ "");
        mFollowProfileers.setText(body.size()+ "");
      //  mfollowersbtnHeader.setText(body.size()+ "");
      //  mfollowingbtnHeader.setText(followingModel.size()+ "");
        followDisplay(mFollowingModelList);

        Log.d(TAG, "Current userID: "+SharedPrefsHelper.getInstance().get(USER_ID).toString());
        Log.d(TAG, "followerID: "+userId);

        if(SharedPrefsHelper.getInstance().get(USER_ID).toString().equals(userId)){
            mFollowProfile.setText("Edit profile");
        }else {
            mFollowProfile.setText("Follow");
            for (int i = 0; i < body.size(); i++)
            {
                if (SharedPrefsHelper.getInstance().get(USER_ID).toString().equalsIgnoreCase(body.get(i).getFollower_id()))
                {
                    mFollowProfile.setText("Un Follow");
                    mFollowProfile.setVisibility(View.VISIBLE);
                    break;
                }
                else
                {
                    mFollowProfile.setVisibility(View.VISIBLE);
                }
            }
        }



    }
    FollowingAdapter.SelectItem selectItem = new FollowingAdapter.SelectItem() {
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
    public void followDisplay(List<FollowModel> mFollowingModelList) {
        FollowingAdapter firstPrefAdapter = new FollowingAdapter(OthersProfileActivity.this, mFollowingModelList, null, selectItem, true);
        mFollowRecyclerView.setLayoutManager(new LinearLayoutManager(OthersProfileActivity.this, LinearLayoutManager.VERTICAL, false));
        mFollowRecyclerView.setAdapter(firstPrefAdapter);
    }

    @Override
    public void updateAddedFollowers(boolean val) {
        if (val) {
           // mFollowProfile.setText("Un Follow");
            mOthersProfilePresenter.getFollowers(userId);

         /*   //IamFollowing++;
            IhaveFollowers++;
            String msg = "I am following " + IamFollowing + " people and have " + IhaveFollowers + " followers";
            mTextViewFollowing.setText(msg);
            mFollowProfile.setText("Your are already following");
            mFollowProfile.setEnabled(false);
            mFollowProfile.setClickable(false);
            mFollowProfile.setVisibility(View.VISIBLE);*/
        }
    }

    private void getAllPost(String id) {
        mRecyclerView.setVisibility(View.GONE);
        Call<NewAPIResponseModel> hashTagCall = apiInterface.GetAllUserPostByUserID(id,1,20);
        hashTagCall.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                shimmerFrameLayout.startShimmer();
                //  mProgressBar.setVisibility(View.GONE);
                if(response.body().isStatus()){
                    TagDataModelList = response.body().getKnowledgeModel();
                    if (TagDataModelList != null) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        Collections.reverse(TagDataModelList);
                        getFollowersList();
                        PostAdapter = new OutDoorTagsPrefAdapter(OthersProfileActivity.this, mFollowersList, likeDatalist,commentDataList,shareDataList , TagDataModelList, selectItemInterface2, mediaController, 3);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(OthersProfileActivity.this, LinearLayoutManager.VERTICAL, false));
                        mRecyclerView.setAdapter(PostAdapter);
                        PostAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
            }
        });

    }

    private OutDoorTagsPrefAdapter.SelectItem selectItemInterface2 = new OutDoorTagsPrefAdapter.SelectItem() {
        @Override
        public void selectedPosition(int position) {
            Intent intent = new Intent(OthersProfileActivity.this, DetailsActivity.class);
            intent.putExtra("DATA", TagDataModelList.get(position));
            intent.putExtra("TYPE", ASPIRATION_FIRST);
            intent.putExtra("ASPIRATION_COMMENT", true);
            intent.putExtra("SUBJECT", mSelectedSubject);
            OthersProfileActivity.this.startActivity(intent);
        }

        @Override
        public void sharePost(OutDoorPostModel dataModel) {
            ShareDataModel shareDataModel = new ShareDataModel();
            shareDataModel.setCategory(dataModel.getCategory());
            shareDataModel.setPostId(dataModel.getPostId());
            shareDataModel.setShareCount(dataModel.getNumShares()+"");
            shareDataModel.setShareByID(SharedPrefsHelper.getInstance().get(USER_ID).toString());
            updateShare(shareDataModel);
        }
//        @Override
//        public void updateLikeCount(final OutDoorPostModel dataModel) {
//
//            final String uid = SharedPrefsHelper.getInstance().get(USER_ID, null);
//
//            final LikeModel likeModel = new LikeModel();
//            likeModel.setLikeByID(uid);
//            List<LikeDataModel> ListLikeData = dataModel.getLikesData();
//            if(likeModel != null){
//                boolean isLike = false;
//                boolean isBreak = false;
//                if(ListLikeData!=null)
//                    for(int i=0 ; i<ListLikeData.size(); i++){
//                        if(uid.equalsIgnoreCase(ListLikeData.get(i).getLikeByID())){
//                            isLike = true;
//                            break;
//                        }
//                    }
//
//                if(!isLike){
//                    likeModel.setLikeCount(dataModel.getNumLikes() + "");
//                    likeModel.setPostId(dataModel.getPostId());
//                    Call<NewAPIResponseModel> call = apiInterface.addLike(likeModel , 3);
//                    call.enqueue(new Callback<NewAPIResponseModel>() {
//                        @Override
//                        public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
//                            assert response.body() != null;
//                            Log.d("TAG", "onResponse: " + response.body().toString());
//                            if(response.body().isStatus())
//                            {
//                                for (int i = 0; i < TagDataModelList.size(); i++) {
//                                    if (TagDataModelList.get(i).getPostId().equalsIgnoreCase(dataModel.getPostId())) {
//                                        TagDataModelList.get(i).setNumLikes(TagDataModelList.get(i).getNumLikes() + 1);
//                                        LikeDataModel likeDataModel = new LikeDataModel();
//                                        likeDataModel.setLikeByID(uid);
//                                        likeDataModel.setLikeCount((dataModel.getNumLikes()+1)+"");
//                                        likeDataModel.setPostID(dataModel.getPostId());
//
//                                        PostAdapter.notifyDataSetChanged();
//
//                                        if(TagDataModelList.get(i).getLikesData() == null){
//
//                                            List<LikeDataModel> list = new ArrayList<>();
//                                            list.add(likeDataModel);
//                                            TagDataModelList.get(i).setLikesData(list);
//
//                                        }else {
//
//                                            TagDataModelList.get(i).getLikesData().add(likeDataModel);
//
//                                        }
//                                        break;
//                                    }
//                                    // mRecyclerView.stopScroll();
//                                    PostAdapter.notifyDataSetChanged();
//                                    //  Toast.makeText(OthersProfileActivity.this, "  PostAdapter.notifyDataSetChanged() 2", Toast.LENGTH_LONG).show();
//                                }
//                            }
//
//                        }
//                        @Override
//                        public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
//                            Log.d("TAG", "onFailure: " + t.getLocalizedMessage());
//                        }
//                    });
//                }
//                else {
//                    for(int i=0; i<TagDataModelList.size(); i++){
//                        if(isBreak){
//                            break;
//                        }
//                        if(TagDataModelList.get(i).getPostId().equalsIgnoreCase(dataModel.getPostId())){
//                            if(TagDataModelList.get(i).getNumLikes() > 0 ){
//                                TagDataModelList.get(i).setNumLikes(TagDataModelList.get(i).getNumLikes() - 1);
//                                for(int j=0; j<TagDataModelList.get(i).getLikesData().size(); j++){
//                                    if(TagDataModelList.get(i).getLikesData().get(j).getPostID().equalsIgnoreCase(dataModel.getPostId())){
//                                        TagDataModelList.get(i).getLikesData().remove(j);
//                                        isBreak = true;
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    //  PostAdapter = new OutDoorTagsPrefAdapter(OthersProfileActivity.this, mFollowersList, TagDataModelList, selectItemInterface2, mediaController, 3);
//                    //  mRecyclerView.setLayoutManager(new LinearLayoutManager(OthersProfileActivity.this, LinearLayoutManager.VERTICAL, false));
//                    //  mRecyclerView.setAdapter(PostAdapter);
//                    PostAdapter.notifyDataSetChanged();
//                    isBreak = false;
//                }
//
//            }
//
////            String uid = SharedPrefsHelper.getInstance().get(USER_ID, null);  // TODO : uday raj uncomment add like new code
////
////            LikeModel likeModel = new LikeModel();
////            likeModel.setLikeByID(uid);
////            likeModel.setLikeCount(dataModel.getNumLikes()+"");
////            likeModel.setPostId(dataModel.getPostId());
////            Call<APIResponseModel>call = apiInterface.aspirationAddLike(likeModel);
////            call.enqueue(new Callback<APIResponseModel>() {
////                @Override
////                public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
////                    Log.d(TAG, "onResponse: "+response.body().toString());
////                    if(response.body().isStatus()){
////                        for( int  i=0; i<TagDataModelList.size(); i++){
////                            if(TagDataModelList.get(i).getPostId().equalsIgnoreCase(dataModel.getPostId())){
////                                TagDataModelList.get(i).setNumLikes(TagDataModelList.get(i).getNumLikes() + 1);
////                                break;
////                            }
////                        }
////                        PostAdapter.notifyDataSetChanged();
////                    }
////
////                }
////                @Override
////                public void onFailure(Call<APIResponseModel> call, Throwable t) {
////                    Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
////                }
////            });
//
//
//        }

        @Override
        public void followUnfollow(FollowModel dataModel, int type) {
            if(type==2)
            {
                Call<APIResponseModel> call = apiInterface.UnFollow(dataModel.getFollowee_id(), dataModel.getFollower_id());
                call.enqueue(new Callback<APIResponseModel>() {
                    @Override
                    public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                        if(response.body().isStatus()){
                            ContinentTools.showToast("unfollowed");
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponseModel> call, Throwable t) {
                        ContinentTools.showToast(getString(R.string.some_thing_wrong));
                    }
                });
            }
            else
            {
                Call<NewAPIResponseModel> call = apiInterface.AddFollower(dataModel);
                call.enqueue(new Callback<NewAPIResponseModel>() {
                    @Override
                    public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                        if(response.body().isStatus()){
                            ContinentTools.showToast("following");
                        }
                    }

                    @Override
                    public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                        ContinentTools.showToast(getString(R.string.some_thing_wrong));

                    }
                });
            }
        }

        @Override
        public void startVideo(ImageView videoThumbNail) {
         //   playVideoAtThis();
        }

        @Override
        public void openAlert(final OutDoorPostModel dataModel) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(OthersProfileActivity.this);
            LayoutInflater inflater = OthersProfileActivity.this.getLayoutInflater();
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(OthersProfileActivity.this
                    , R.style.BottomSheetDialogThem);
            View bottomSheetView = LayoutInflater.from(OthersProfileActivity.this)
                    .inflate(R.layout.outdoor_more_alert,
                            (CardView) findViewById(R.id.BottomSheetDialogUserDost));

            View dialogView = inflater.inflate(R.layout.outdoor_more_alert, null);
            TextView report, copyLink, share, delete, onNotification, edit;
            report = bottomSheetView.findViewById(R.id.Report);
            copyLink = bottomSheetView.findViewById(R.id.copy_link);
            share = bottomSheetView.findViewById(R.id.share_to);
            delete = bottomSheetView.findViewById(R.id.delete);
            onNotification = bottomSheetView.findViewById(R.id.turn_on_post_noti);
            edit = bottomSheetView.findViewById(R.id.edit);

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
                    Intent intent = new Intent(OthersProfileActivity.this, PostActivity.class);
                    intent.putExtra("OUTDOOR_DATA", dataModel);
                    intent.putExtra("TYPE", ASPIRATION_FIRST);
                    intent.putExtra("SUBJECT", mSelectedSubject);
                    intent.putExtra("IS_EDIT", true);
                    startActivity(intent);
                    alertDialogMoreOptions.dismiss();
                    alertDialogMoreOptions = null;
                }
            });

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareDataModel shareDataModel = new ShareDataModel();
                    shareDataModel.setCategory(dataModel.getCategory());
                    shareDataModel.setPostId(dataModel.getPostId());
                    shareDataModel.setShareCount(dataModel.getNumShares()+"");
                    shareDataModel.setShareByID(SharedPrefsHelper.getInstance().get(USER_ID).toString());
                    updateShare(shareDataModel);
                    bottomSheetDialog.dismiss();
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    deletePost(dataModel.getPostId());
                                    Toast.makeText(OthersProfileActivity.this, getString(R.string.post_deleted_success), Toast.LENGTH_LONG).show();
                                    bottomSheetDialog.dismiss();
                                    dialog.dismiss();
                                    if(alertDialogMoreOptions!=null)
                                    {
                                        bottomSheetDialog.dismiss();
                                        alertDialogMoreOptions.dismiss();
                                    }
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    if(alertDialogMoreOptions!=null)
                                    {
                                        bottomSheetDialog.dismiss();
                                        alertDialogMoreOptions.dismiss();
                                    }
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(OthersProfileActivity.this);
                    builder.setMessage(R.string.DeleteToast).setPositiveButton("YES", dialogClickListener)
                            .setNegativeButton("NO", dialogClickListener).show();
                }
            });

//            alertBuilder.setView(dialogView);
//            alertDialogMoreOptions = alertBuilder.create();
//            alertDialogMoreOptions.show();

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        }

        @Override
        public void reachedLastItem() {
            //getMoreData();
        }


        @Override
        public void clickOnProfile(String id) {
            if(!id.equalsIgnoreCase(SharedPrefsHelper.getInstance().get(USER_ID).toString())) {
                Intent intent = new Intent(OthersProfileActivity.this, OthersProfileActivity.class);
                intent.putExtra("USER_ID", id);
                startActivity(intent);
            }
        }

        @Override
        public void updateLikeCount(OutDoorPostModel dataModel, boolean isLiked) {

        }
    };

    private void updateShare(ShareDataModel shareDataModel) {
        //   mProgressBar.setVisibility(View.VISIBLE);
        Call<APIResponseModel> call = apiInterface.AddShare(shareDataModel, 3);
        call.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                //  mProgressBar.setVisibility(View.GONE);
                if(response.body().isStatus()) {
                    AppUtils.showToastMessage(OthersProfileActivity.this, OthersProfileActivity.this.getString(R.string.shared_success));
                    getAllPost(userId);
                }
                else
                {
                    AppUtils.showToastMessage(OthersProfileActivity.this, OthersProfileActivity.this.getString(R.string.some_thing_wrong));
                }
            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                //  mProgressBar.setVisibility(View.GONE);
                AppUtils.showToastMessage(OthersProfileActivity.this, OthersProfileActivity.this.getString(R.string.some_thing_wrong));
            }
        });
    }

    private void deletePost(String postId) {
        //  mProgressBar.setVisibility(View.VISIBLE);
        Call<List<APIResponseModel>> hashTagCall = apiInterface.deletePost(postId, 3);
        hashTagCall.enqueue(new Callback<List<APIResponseModel>>() {
            @Override
            public void onResponse(Call<List<APIResponseModel>> call, Response<List<APIResponseModel>> response) {
                //  mProgressBar.setVisibility(View.GONE);
                AppUtils.showToastMessage(OthersProfileActivity.this, OthersProfileActivity.this.getString(R.string.post_deleted_success));
                alertDialogMoreOptions.dismiss();
            }

            @Override
            public void onFailure(Call<List<APIResponseModel>> call, Throwable t) {
                //   mProgressBar.setVisibility(View.GONE);
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

    public static String capitalizeWord(String str){
        String words[]=str.split("\\s");
        String capitalizeWord="";
        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeWord+=first.toUpperCase()+afterfirst+" ";
        }
        return capitalizeWord.trim();
    }



    private void getFollowersList()
    {
        String userID = SharedPrefsHelper.getInstance().get(USER_ID);
        Call<NewAPIResponseModel> listCall = apiInterface.GetFollowers(userID, "0" );
        listCall.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                mFollowersList = response.body().getmFollowList();
                if(PostAdapter!=null)
                {
                    PostAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                Log.d("TAG", "onFailure: ");
            }
        });
    }


    protected ApiInterface apiInterface;
    private void setUpNetwork() {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Followers:
                if (mFollowingModelList != null) {
                    followDisplay(mFollowingModelList);
                    Intent intent = new Intent(OthersProfileActivity.this, FollowersListShowActivity.class);
                    intent.putExtra("USER_ID", userId);
                    intent.putExtra("ID", "0");
                    intent.putExtra("ISFROM_OTHER_PROF", true);
                    intent.putExtra("USER_NAME", CurrentUserName);
                    startActivity(intent);
                }

                break;

            case R.id.Following:
                if (mFollowingModelList != null) {
                    followDisplay(mFollowingModelList);
                    Intent intent = new Intent(OthersProfileActivity.this, FollowersListShowActivity.class);
                    intent.putExtra("USER_ID", userId);
                    intent.putExtra("ID", "1");
                    intent.putExtra("ISFROM_OTHER_PROF", true);
                    intent.putExtra("USER_NAME", CurrentUserName);
                    startActivity(intent);
                }


                break;


        }
    }
}