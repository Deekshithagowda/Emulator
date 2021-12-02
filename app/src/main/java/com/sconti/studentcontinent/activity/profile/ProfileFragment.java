package com.sconti.studentcontinent.activity.profile;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;


import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.details.DetailsActivity;
import com.sconti.studentcontinent.activity.newlogin.NewLoginActivity;
import com.sconti.studentcontinent.activity.othersprofile.OthersProfileActivity;
import com.sconti.studentcontinent.activity.outdoor.adapter.OutDoorTagsPrefAdapter;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorPostModel;
import com.sconti.studentcontinent.activity.post.PostActivity;
import com.sconti.studentcontinent.activity.profile.editProfile.EditProfileActivity;
import com.sconti.studentcontinent.adapter.FollowingAdapter;
import com.sconti.studentcontinent.adapter.ProfileUserListAdapter;
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

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.ASPIRATION_FIRST;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.BANNER_URL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.COLLEGE_NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.KNOWLEDGE_FIRST;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.PROFILE_URL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_ASPIRATION_NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE_NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;


public class ProfileFragment extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener , View.OnClickListener   {

    private static final int RESULT_OK = -1;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private CircleImageView mImageViewAvatar , mimageViewAvatar;
    private TextView mTextViewProfileName , navUsername , navUserEmail , mPostCountheader , mfollowersbtnHeader , mfollowingbtnHeader;
    private LinearLayout  mFollowing , mFollowers ;
    private Button mButtonLogout;
    private String name, profileUrl, BannerUrl;
    private ImageView mImageViewAspirantEdit, mImageViewDegreeEdit, mLinearLayoutBackground , mProfileBackground;
    private TextView mTextViewSelectedDegree, mTextViewSelectedAspirant, mTextViewFollowing, mEditProfile, mTextViewSelectedCollage;
    private Button mButtonFollowList ;
    private RecyclerView mFollowRecyclerView, UserProfileListRecyclerView;
    private TextView mButtonFollowing, mButtonFollowers;
    private List<UserDetails> userDetailsList;
    private LinearLayout mLinearLayoutUserList, mLinearLayoutProfile;
    private TextView mSearchView ;
    private RecyclerView mRecyclerView;
    private boolean issearchView;
    private List<OutDoorPostModel> TagDataModelList;
    private List<LikeDataModel> likeDatalist;
    OutDoorTagsPrefAdapter PostAdapter;
    private List<FollowModel> mFollowersList;
    private String mSelectedSubject;
    private View focusSelection;
    private AlertDialog alertDialogMoreOptions;
    private DrawerLayout mDrawerLayout;
    private ProgressBar mProgressBar;
    private static final int PICK_IMAGE_REQUEST = 777;
    private Uri imageUri;
    private ImageView mOpenDrawable;
    private static final String TAG = "ProfileFragment";
    private NavigationView navigationView;
    ShimmerFrameLayout shimmerFrameLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FragmentManager fragmentManager;
    private  TabLayout tabs;
    private ViewPager viewPager;
    private LinearLayout mBackButton;
    private List<ShareDataModel> shareDataList;
    private List<CommentModel> commentDataList;


    public ProfileFragment() {
        // Required empty public constructor
    }


//    public static ProfileFragment newInstance(String param1, String param2) {
//        ProfileFragment fragment = new ProfileFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }

//    @Override
//    public void onPrepareOptionsMenu(final Menu menu) {  // TODO : im remove the search bar so im uncomment this   udaya
//        ProfileFragment.this,().getMenuInflater().inflate(R.menu.searchview, menu);
//        SearchManager searchManager = (SearchManager) ProfileFragment.this,().getSystemService(Context.SEARCH_SERVICE);
//        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(ProfileFragment.this,().getComponentName()));
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                if(issearchView)
//                getUser(newText);
//                return true;
//            }
//
//        });
//        MenuItem menuItem = menu.findItem(R.id.action_search);
//        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                issearchView = true;
//                mLinearLayoutProfile.setVisibility(View.GONE);
//                mLinearLayoutUserList.setVisibility(View.VISIBLE);
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                mLinearLayoutProfile.setVisibility(View.VISIBLE);
//                mLinearLayoutUserList.setVisibility(View.GONE);
//                userDetailsList = null;
//                issearchView = false;
//                return true;
//            }R
//        });
//    }

    ProfileUserListAdapter UserListAdapter;

    private void getUser(String name) {
        Call<APIResponseModel> UserCall = apiInterface.GetUser(name);
        UserCall.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                userDetailsList = response.body().getUsertable();
                if (userDetailsList != null && userDetailsList.size() > 0) {
                    mLinearLayoutUserList.setVisibility(View.VISIBLE);
                    mLinearLayoutProfile.setVisibility(View.GONE);
                    UserProfileListRecyclerView.setVisibility(View.VISIBLE);
                    Collections.reverse(userDetailsList);
                    UserListAdapter = new ProfileUserListAdapter(ProfileFragment.this, userDetailsList);
                    UserProfileListRecyclerView.setLayoutManager(new LinearLayoutManager(ProfileFragment.this, LinearLayoutManager.VERTICAL, false));
                    UserProfileListRecyclerView.setAdapter(UserListAdapter);
                } else {
                    UserListAdapter = new ProfileUserListAdapter(ProfileFragment.this, userDetailsList);
                    UserProfileListRecyclerView.setLayoutManager(new LinearLayoutManager(ProfileFragment.this, LinearLayoutManager.VERTICAL, false));
                    UserProfileListRecyclerView.setAdapter(UserListAdapter);
                    mLinearLayoutProfile.setVisibility(View.VISIBLE);
                    mLinearLayoutUserList.setVisibility(View.GONE);
                    UserListAdapter.notifyDataSetChanged();
                    AppUtils.showToastMessage(ProfileFragment.this, getString(R.string.no_user_data));
                }
            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                AppUtils.showToastMessage(ProfileFragment.this, getString(R.string.some_thing_wrong));
                Log.d("12345", "onFailure: " + toString().trim());
            }
        });

    }


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//        if (getArguments() != null) {
//            // mParam1 = getArguments().getString(ARG_PARAM1);
//            // mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        mView = inflater.inflate(R.layout.fragment_profile, container, false);
//        setHasOptionsMenu(true);
//        initView();
//        initData();
//
//        shimmerFrameLayout.startShimmer();
//
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            mSelectedSubject = bundle.getString("SELECTED");
//            mSelectedSubject = "All";
//        }
//        mSelectedSubject = "All";
//
//        setUpNetwork();
//        initData();
//        getFollowers();
//
//
//
//
////        mSwipeRefreshLayout = findViewById(R.id.swipeLayout);
////        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
////            @Override
////            public void onRefresh() {
////                getAllPost(userID);
////
////            }
////        });
////
////
////
////        mSwipeRefreshLayout.post(new Runnable() {
////            @Override
////            public void run() {
////                mSwipeRefreshLayout.setRefreshing(true);
////                getAllPost(userID);
////
////            }
////        });
////
////        FragmentManager fm = getFragmentManager();
////        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
////            @Override
////            public void onBackStackChanged() {
////                if(getFragmentManager().getBackStackEntryCount() == 0);
////            }
////        });
//
//
//
//    //  ((AppCompatActivity) ProfileFragment.this,()).getSupportActionBar().hide();
//        return mView;
//    }




    @Override
    public void onResume() {
        super.onResume();
      //  initView();
       // initData();
    }


    @Override
    public void onStart() {
        super.onStart();
        String userID = SharedPrefsHelper.getInstance().get(USER_ID).toString();
      //  getAllPost(userID);
     //   getLikeListByUserID();
    }

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

    @Override
    protected int getContentView() {
        return R.layout.fragment_profile;
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

    protected void initData() {
        name = SharedPrefsHelper.getInstance().get(NAME);
        profileUrl = SharedPrefsHelper.getInstance().get(PROFILE_URL);
        BannerUrl = SharedPrefsHelper.getInstance().get(BANNER_URL);
        if (name != null)
            mTextViewProfileName.setText(capitalizeWord(name));
//            navUsername.setText(name);
        if (profileUrl != null && !profileUrl.isEmpty())
            Glide.with(ProfileFragment.this).load(profileUrl).placeholder(R.drawable.ic_user_profile).into(mImageViewAvatar);
          //  Glide.with(ProfileFragment.this,).load(profileUrl).into(mimageViewAvatar);
//        if (BannerUrl != null && !BannerUrl.isEmpty())
//            Glide.with(ProfileFragment.this).load(BannerUrl).into(mProfileBackground);
        if (SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE_NAME) != null)
            mTextViewSelectedDegree.setText(SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE_NAME).toString());
        if (SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_ASPIRATION_NAME) != null)
            mTextViewSelectedAspirant.setText(SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_ASPIRATION_NAME).toString());
        if (SharedPrefsHelper.getInstance().get(COLLEGE_NAME) != null)
            mTextViewSelectedCollage.setText(SharedPrefsHelper.getInstance().get(COLLEGE_NAME).toString());
           // navUserEmail.setText(SharedPrefsHelper.getInstance().get(COLLEGE_NAME).toString());

        setUpNetwork();
        getFollowers();

    }





    @SuppressLint("SetTextI18n")
    protected void initView() {
        shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmerLayout);
        mFollowRecyclerView = findViewById(R.id.follow_recycler);
        UserProfileListRecyclerView = findViewById(R.id.UserProfileList);
        mButtonFollowers = findViewById(R.id.followersbtn);
        mButtonFollowing = findViewById(R.id.followingbtn);
        mImageViewAvatar = findViewById(R.id.imageViewProfile);
        mTextViewProfileName = findViewById(R.id.textViewName);
        mButtonLogout = findViewById(R.id.logout);
        mTextViewSelectedAspirant = findViewById(R.id.textViewSelectedAspirant);
        mTextViewSelectedDegree = findViewById(R.id.textViewSelectedDegree);
        mImageViewAspirantEdit = findViewById(R.id.imageViewAspirantEdit);
        mImageViewDegreeEdit = findViewById(R.id.imageViewDegreeEdit);
        mButtonFollowList = findViewById(R.id.ButtonFollowList);
        mTextViewFollowing = findViewById(R.id.textViewFollowing);
        mLinearLayoutProfile = findViewById(R.id.linearLayoutProfile);
        mEditProfile = findViewById(R.id.EditProfile);
        mSearchView = findViewById(R.id.SearchView);
        mFollowing = findViewById(R.id.Following);
        mFollowers = findViewById(R.id.Followers);
        mTextViewSelectedCollage = findViewById(R.id.textViewSelectedCollage);
        mRecyclerView = findViewById(R.id.UserPostList);
        @SuppressLint("UseCompatLoadingForDrawables") Drawable bitmap = getResources().getDrawable(R.drawable.ic_user_profile);
        mImageViewAvatar.setImageDrawable(bitmap);
        mProgressBar = findViewById(R.id.ProgressBar);
        mLinearLayoutBackground = findViewById(R.id.linearLayoutBackground);
        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.view_pager);
        mBackButton = findViewById(R.id.BackButton);
        mProfileBackground = findViewById(R.id.ProfileBackground);




        mEditProfile.setText("Edit profile");
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


        initListener();

    }





//    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
//            = new NavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.knowledge:
//                    Toast.makeText(getContext(), "hiiiiiiiiiii", Toast.LENGTH_SHORT).show();
//                    break;
//                case R.id.LogOut:
//                    SharedPrefsHelper.getInstance().clearAllData();
//                    startActivity(new Intent(ProfileFragment.this,, NewLoginActivity.class));
//                    ProfileFragment.this,.finish();
//
//                    // startActivity(new Intent(ProfileFragment.this,, UserProfileSearchActivity.class));
//                    break;
//            }
//            mDrawerLayout.closeDrawer(GravityCompat.START);
//            return true;
//        }
//    };

    protected void initListener() {



//        mButtonFollowers.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mFollowingModelList != null) {
//                    followDisplay(mFollowersModelList);
//                    Intent intent = new Intent(ProfileFragment.this,(), FollowersListShowActivity.class);
//                    intent.putExtra("USER_ID", userID);
//                    intent.putExtra("USER_NAME", UserName);
//                    startActivity(intent);
//                }
//
//            }
//        });
//
//        mButtonFollowing.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mFollowingModelList != null) {
//                    followDisplay(mFollowingModelList);
//                    Intent intent = new Intent(ProfileFragment.this,(), FollowersListShowActivity.class);
//                    intent.putExtra("USER_ID", userID);
//                    intent.putExtra("USER_NAME", UserName);
//                    startActivity(intent);
//                }
//            }
//        });

        final String UserName = SharedPrefsHelper.getInstance().get(NAME).toString();
        String userID = SharedPrefsHelper.getInstance().get(USER_ID).toString();

//        mFollowers.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mFollowingModelList != null) {
//                    followDisplay(mFollowingModelList);
//                    Intent intent = new Intent(ProfileFragment.this, FollowersListShowActivity.class);
//                    intent.putExtra("USER_ID", userID);
//                    intent.putExtra("USER_NAME", UserName);
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
//                    Intent intent = new Intent(ProfileFragment.this, FollowersListShowActivity.class);
//                    intent.putExtra("USER_ID", userID);
//                    intent.putExtra("USER_NAME", UserName);
//                    intent.putExtra("ID", 1);
//                    startActivity(intent);
//                }
//            }
//        });





        SectionsProfilePagerAdapter sectionsPagerAdapter = new SectionsProfilePagerAdapter(ProfileFragment.this.getSupportFragmentManager(), getApplication(), userID);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);

        mFollowing.setOnClickListener(this);
        mFollowers.setOnClickListener(this);
        mButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefsHelper.getInstance().clearAllData();
                startActivity(new Intent(ProfileFragment.this, NewLoginActivity.class));
                ProfileFragment.this.finish();
            }
        });
        mImageViewDegreeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mImageViewAspirantEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment.this.onBackPressed();
            }
        });

        mButtonFollowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mImageViewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Profile");
                /*mAttacher = new PhotoViewAttacher(mImageViewAvatar);
                mAttacher.update();*/
                //showExpandableImage();

            }
        });

        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ProfileFragment.this, EditProfileActivity.class);
                startActivity(myIntent);
            }
        });


        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileFragment.this, UserProfileSearchActivity.class));
            }
        });

//        mOpenDrawable.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("RtlHardcoded")
//            @Override
//            public void onClick(View v) {
//                mDrawerLayout.openDrawer(Gravity.LEFT);
//            }
//        });

    }

    protected ApiInterface apiInterface;

    private void setUpNetwork() {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }

    private int iamFollowing = 0;
    private int IHaveFollowers = 0;
    private List<FollowModel> mFollowingModelList, mFollowersModelList;
    private UserDetails userDetails;


    public void getFollowers() {
        setUpNetwork();
        final String userID = SharedPrefsHelper.getInstance().get(USER_ID).toString();
        Call<NewAPIResponseModel> listCall = apiInterface.GetFollowers(userID, "0");
        listCall.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                // iamFollowing = response.body().size();
                mFollowingModelList = response.body().getmFollowList();
                Call<NewAPIResponseModel> listCall2 = apiInterface.GetFollowers(userID, "1");
                listCall2.enqueue(new Callback<NewAPIResponseModel>() {
                    @Override
                    public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                        //  IHaveFollowers = response.body().size();
                        mFollowersModelList = response.body().getmFollowList();
                        String msg = "I am following " + iamFollowing + " people and have " + IHaveFollowers + " followers";
                        mTextViewFollowing.setText(msg);
                        mButtonFollowing.setText(mFollowingModelList.size() + "");
                     //     mfollowingbtnHeader.setText(mFollowingModelList.size() + "");
                     //     mfollowersbtnHeader.setText(mFollowersModelList.size() + "");
                          mButtonFollowers.setText(mFollowersModelList.size() + "");
                        followDisplay(mFollowingModelList);
                    }

                    @Override
                    public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                        Log.d("TAG", "onFailure: ");
                    }
                });
            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                Log.d("TAG", "onFailure: ");
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.knowledge:
                Toast.makeText(ProfileFragment.this, "hiiiiiiiiiiiiiiiiiiiiiiiiiii", Toast.LENGTH_SHORT).show();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Followers:
                if (mFollowingModelList != null) {
                    final String UserName = SharedPrefsHelper.getInstance().get(NAME).toString();
                    String userID = SharedPrefsHelper.getInstance().get(USER_ID).toString();
                    followDisplay(mFollowingModelList);
                    Intent intent = new Intent(ProfileFragment.this, FollowersListShowActivity.class);
                    intent.putExtra("USER_ID", userID);
                    intent.putExtra("ID", "0");
                    intent.putExtra("USER_NAME", UserName);
                    startActivity(intent);
                }

                break;

            case R.id.Following:
                if (mFollowingModelList != null) {
                    final String UserName = SharedPrefsHelper.getInstance().get(NAME).toString();
                    String userID = SharedPrefsHelper.getInstance().get(USER_ID).toString();
                    followDisplay(mFollowingModelList);
                    Intent intent = new Intent(ProfileFragment.this, FollowersListShowActivity.class);
                    intent.putExtra("USER_ID", userID);
                    intent.putExtra("ID", "1");
                    intent.putExtra("USER_NAME", UserName);
                    startActivity(intent);
                }


                break;


        }
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    FollowingAdapter.SelectItem selectItem = new FollowingAdapter.SelectItem() {
        @Override
        public void followUnfollow(FollowModel dataModel, int type) {
            if (type == 2) {
                Call<APIResponseModel> call = apiInterface.UnFollow(dataModel.getFollowee_id(), dataModel.getFollower_id());
                call.enqueue(new Callback<APIResponseModel>() {
                    @Override
                    public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {

                    }

                    @Override
                    public void onFailure(Call<APIResponseModel> call, Throwable t) {

                    }
                });
            } else {
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
        FollowingAdapter firstPrefAdapter = new FollowingAdapter(ProfileFragment.this, mFollowingModelList, null, selectItem, false);
        mFollowRecyclerView.setLayoutManager(new LinearLayoutManager(ProfileFragment.this, LinearLayoutManager.VERTICAL, false));
        mFollowRecyclerView.setAdapter(firstPrefAdapter);
    }

    private void getAllPost(final String id) {
        mRecyclerView.setVisibility(View.GONE);
      //  mSwipeRefreshLayout.setRefreshing(true);
        Call<NewAPIResponseModel> hashTagCall = apiInterface.GetAllUserPostByUserID(id,1,20);
        hashTagCall.enqueue(new Callback<NewAPIResponseModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
              //  mSwipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                shimmerFrameLayout.startShimmer();
                if (response.body().isStatus()) {
                    TagDataModelList = response.body().getKnowledgeModel();
                    if (TagDataModelList != null) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        Collections.sort(TagDataModelList);
                        PostAdapter = new OutDoorTagsPrefAdapter(ProfileFragment.this, mFollowersList, likeDatalist,commentDataList,shareDataList ,  TagDataModelList, selectItemInterface2, mediaController, 3);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(ProfileFragment.this, LinearLayoutManager.VERTICAL, false));
                        mRecyclerView.setAdapter(PostAdapter);
                        PostAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
             //   mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private OutDoorTagsPrefAdapter.SelectItem selectItemInterface2 = new OutDoorTagsPrefAdapter.SelectItem() {
        @Override
        public void selectedPosition(int position) {
            Intent intent = new Intent(ProfileFragment.this, DetailsActivity.class);
            intent.putExtra("DATA", TagDataModelList.get(position));
            intent.putExtra("TYPE", ASPIRATION_FIRST);
            intent.putExtra("ASPIRATION_COMMENT", true);
            intent.putExtra("SUBJECT", mSelectedSubject);
            ProfileFragment.this.startActivity(intent);
        }

        @Override
        public void sharePost(OutDoorPostModel dataModel) {
            ShareDataModel shareDataModel = new ShareDataModel();
            shareDataModel.setCategory(dataModel.getCategory());
            shareDataModel.setPostId(dataModel.getPostId());
            shareDataModel.setShareCount(dataModel.getNumShares() + "");
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
//            if (likeModel != null) {
//                boolean isLike = false;
//                boolean isBreak = false;
//                if (ListLikeData != null)
//                    for (int i = 0; i < ListLikeData.size(); i++) {
//                        if (uid.equalsIgnoreCase(ListLikeData.get(i).getLikeByID())) {
//                            isLike = true;
//                            break;
//                        }
//                    }
//
//                if (!isLike) {
//                    likeModel.setLikeCount(dataModel.getNumLikes() + "");
//                    likeModel.setPostId(dataModel.getPostId());
//                    Call<NewAPIResponseModel> call = apiInterface.addLike(likeModel, 3);
//                    call.enqueue(new Callback<NewAPIResponseModel>() {
//                        @Override
//                        public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
//                            assert response.body() != null;
//                            Log.d("TAG", "onResponse: " + response.body().toString());
//                            if (response.body().isStatus()) {
//                                for (int i = 0; i < TagDataModelList.size(); i++) {
//                                    if (TagDataModelList.get(i).getPostId().equalsIgnoreCase(dataModel.getPostId())) {
//                                        TagDataModelList.get(i).setNumLikes(TagDataModelList.get(i).getNumLikes() + 1);
//                                        LikeDataModel likeDataModel = new LikeDataModel();
//                                        likeDataModel.setLikeByID(uid);
//                                        likeDataModel.setLikeCount((dataModel.getNumLikes() + 1) + "");
//                                        likeDataModel.setPostID(dataModel.getPostId());
//
//                                        PostAdapter.notifyDataSetChanged();
//
//                                        if (TagDataModelList.get(i).getLikesData() == null) {
//
//                                            List<LikeDataModel> list = new ArrayList<>();
//                                            list.add(likeDataModel);
//                                            TagDataModelList.get(i).setLikesData(list);
//
//                                        } else {
//
//                                            TagDataModelList.get(i).getLikesData().add(likeDataModel);
//
//                                        }
//                                        break;
//                                    }
//                                    // mRecyclerView.stopScroll();
//                                    PostAdapter.notifyDataSetChanged();
//                                    //  Toast.makeText(getContext(), "  PostAdapter.notifyDataSetChanged() 2", Toast.LENGTH_LONG).show();
//                                }
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
//                            Log.d("TAG", "onFailure: " + t.getLocalizedMessage());
//                        }
//                    });
//                } else {
//                    for (int i = 0; i < TagDataModelList.size(); i++) {
//                        if (isBreak) {
//                            break;
//                        }
//                        if (TagDataModelList.get(i).getPostId().equalsIgnoreCase(dataModel.getPostId())) {
//                            if (TagDataModelList.get(i).getNumLikes() > 0) {
//                                TagDataModelList.get(i).setNumLikes(TagDataModelList.get(i).getNumLikes() - 1);
//                                for (int j = 0; j < TagDataModelList.get(i).getLikesData().size(); j++) {
//                                    if (TagDataModelList.get(i).getLikesData().get(j).getPostID().equalsIgnoreCase(dataModel.getPostId())) {
//                                        TagDataModelList.get(i).getLikesData().remove(j);
//                                        isBreak = true;
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    //  PostAdapter = new OutDoorTagsPrefAdapter(ProfileFragment.this,, mFollowersList, TagDataModelList, selectItemInterface2, mediaController, 3);
//                    //  mRecyclerView.setLayoutManager(new LinearLayoutManager(ProfileFragment.this,, LinearLayoutManager.VERTICAL, false));
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
            if (type == 2) {
                Call<APIResponseModel> call = apiInterface.UnFollow(dataModel.getFollowee_id(), dataModel.getFollower_id());
                call.enqueue(new Callback<APIResponseModel>() {
                    @Override
                    public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                        if (response.body().isStatus()) {
                            ContinentTools.showToast("unfollowed");
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponseModel> call, Throwable t) {
                        ContinentTools.showToast(getString(R.string.some_thing_wrong));
                    }
                });
            } else {
                Call<NewAPIResponseModel> call = apiInterface.AddFollower(dataModel);
                call.enqueue(new Callback<NewAPIResponseModel>() {
                    @Override
                    public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                        if (response.body().isStatus()) {
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
           // playVideoAtThis();
        }

        @Override
        public void openAlert(final OutDoorPostModel dataModel) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ProfileFragment.this);
            LayoutInflater inflater = ProfileFragment.this.getLayoutInflater();
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ProfileFragment.this
                    , R.style.BottomSheetDialogThem);
            View bottomSheetView = LayoutInflater.from(ProfileFragment.this)
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
                    Intent intent = new Intent(ProfileFragment.this, PostActivity.class);
                    intent.putExtra("OUTDOOR_DATA", dataModel);
                    intent.putExtra("TYPE", KNOWLEDGE_FIRST);
                    intent.putExtra("SUBJECT", mSelectedSubject);
                    intent.putExtra("IS_EDIT", true);
                    startActivity(intent);
                    //  alertDialogMoreOptions.dismiss();
                    bottomSheetDialog.dismiss();
                    alertDialogMoreOptions = null;
                }
            });

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareDataModel shareDataModel = new ShareDataModel();
                    shareDataModel.setCategory(dataModel.getCategory());
                    shareDataModel.setPostId(dataModel.getPostId());
                    shareDataModel.setShareCount(dataModel.getNumShares() + "");
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
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    deletePost(dataModel.getPostId());
                                    Toast.makeText(ProfileFragment.this, getString(R.string.post_deleted_success), Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    for(int i=0;i<TagDataModelList.size();i++)
                                    {
                                        if(TagDataModelList.get(i).getPostId().equals(dataModel.getPostId()))
                                        {
                                            //TagDataModelList.get(i).getPostId()
                                            TagDataModelList.remove(i);
                                            PostAdapter.notifyDataSetChanged();
                                            Log.i(TAG, "onClick:Deleted ");
                                            break;
                                        }
                                    }
                                    // getCategory(mSelectedSubject, 1, 1);

                                    bottomSheetDialog.dismiss();
                                    if (alertDialogMoreOptions != null) {
                                        bottomSheetDialog.dismiss();
                                        alertDialogMoreOptions.dismiss();

                                    }
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    bottomSheetDialog.dismiss();
                                    if (alertDialogMoreOptions != null) {
                                        bottomSheetDialog.dismiss();
                                        //  alertDialogMoreOptions.dismiss();

                                    }
                                    break;
                            }
                        }
                    };


                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileFragment.this);
                    builder.setMessage(R.string.DeleteToast)
                            .setPositiveButton("YES", dialogClickListener)
                            .setNegativeButton("NO", dialogClickListener)
                            .show();
                    ///alertDialogMoreOptions = builder.create();
                    //  alertDialogMoreOptions.show();

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
            if (!id.equalsIgnoreCase(SharedPrefsHelper.getInstance().get(USER_ID).toString())) {
                Intent intent = new Intent(ProfileFragment.this, OthersProfileActivity.class);
                intent.putExtra("USER_ID", id);
                startActivity(intent);
            }
        }

        @Override
        public void updateLikeCount(final OutDoorPostModel dataModel, boolean isLiked) {
//            ContinentTools likes = new ContinentTools();
//            likes.commonUpdateLikeCount(dataModel, PostAdapter, apiInterface, TagDataModelList, 3, isLiked);
        }


    };



    private void updateShare(ShareDataModel shareDataModel) {
        String userID = SharedPrefsHelper.getInstance().get(USER_ID).toString();
        //   mProgressBar.setVisibility(View.VISIBLE);
        Call<APIResponseModel> call = apiInterface.AddShare(shareDataModel, 3);
        call.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                //  mProgressBar.setVisibility(View.GONE);
                if (response.body().isStatus()) {
                    AppUtils.showToastMessage(ProfileFragment.this, ProfileFragment.this.getString(R.string.shared_success));
                //    getAllPost(userID);
                } else {
                    AppUtils.showToastMessage(ProfileFragment.this, ProfileFragment.this.getString(R.string.some_thing_wrong));
                }
            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                //  mProgressBar.setVisibility(View.GONE);
                AppUtils.showToastMessage(ProfileFragment.this, ProfileFragment.this.getString(R.string.some_thing_wrong));
            }
        });
    }

    private void deletePost( String postId) {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<List<APIResponseModel>> hashTagCall = apiInterface.deletePost(postId, 1);
        hashTagCall.enqueue(new Callback<List<APIResponseModel>>() {
            @Override
            public void onResponse(Call<List<APIResponseModel>> call, Response<List<APIResponseModel>> response) {
                mProgressBar.setVisibility(View.GONE);
                AppUtils.showToastMessage(ProfileFragment.this, ProfileFragment.this.getString(R.string.post_deleted_success));
                alertDialogMoreOptions.dismiss();

            }

            @Override
            public void onFailure(Call<List<APIResponseModel>> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
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

    public static void showExpandableImage(/*Context context, String imageUri, String title,View view*/) {
        /*try {

            Intent intent = new Intent(context, ViewImageActivity.class);
            intent.putExtra("image", imageUri);
            intent.putExtra("title", title);

<<<<<<< HEAD
            if (TextUtils.isEmpty(imageUri)) {
                imageUri = getURLForResource(R.drawable.btsestbgimage); // default image in drawable
            }

            Pair<View, String> bodyPair = Pair.create(view, imageUri);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(((Activity)context), bodyPair);

            ActivityCompat.startActivity(context, intent, options.toBundle());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getURLForResource (int resourceId) {
        return Uri.parse("android.resource://com.yourpackage.name/" + resourceId).toString();
    }*/
    }







}
