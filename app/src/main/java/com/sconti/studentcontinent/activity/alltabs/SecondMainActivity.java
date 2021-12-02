package com.sconti.studentcontinent.activity.alltabs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.SplashActivity;
import com.sconti.studentcontinent.activity.alltabs.ui.aspiration.AspirationFragment;
import com.sconti.studentcontinent.activity.alltabs.ui.games.GamesFragment;
import com.sconti.studentcontinent.activity.alltabs.ui.knowledge.KnowledgeFragment;
import com.sconti.studentcontinent.activity.alltabs.ui.outdoor.OutdoorFragment;
import com.sconti.studentcontinent.activity.appinfo.AppInfoActivity;
import com.sconti.studentcontinent.activity.newlogin.LoginActivity;
import com.sconti.studentcontinent.activity.newlogin.NewLoginActivity;
import com.sconti.studentcontinent.activity.notifications.NotificationFragment;
import com.sconti.studentcontinent.activity.notifications.adapter.CommentsNotificationsAdapter;
import com.sconti.studentcontinent.activity.othersprofile.OthersProfileActivity;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorPostModel;
import com.sconti.studentcontinent.activity.profile.ProfileFragment;
import com.sconti.studentcontinent.activity.profile.UserProfileSearchActivity;
import com.sconti.studentcontinent.activity.trending.TrendingFragment;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.model.APIResponseModel;
import com.sconti.studentcontinent.model.FollowModel;
import com.sconti.studentcontinent.model.NewAPIResponseModel;
import com.sconti.studentcontinent.model.NotificationDataModel;
import com.sconti.studentcontinent.model.notify.Data;
import com.sconti.studentcontinent.model.notify.Notification;
import com.sconti.studentcontinent.model.notify.SendNotificationModel;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static com.sconti.studentcontinent.utils.AppUtils.Constants.COLLEGE_NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.PROFILE_URL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

public class SecondMainActivity extends BaseActivity implements
        NotificationFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener,
        ProfileFragment.OnFragmentInteractionListener {
    private static final String TAG = "KnowledgeTabActivity";
    private ActionBar mActionBar;
    private FloatingActionButton floatingActionButton;
    private String mSelectedSubject;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    List<OutDoorPostModel> TagDataModelList;
    private TextView navUsername, navUserEmail, mPostCountheader, mfollowersbtnHeader, mfollowingbtnHeader;
    private CircleImageView mimageViewAvatar;
    private String name, profileUrl;
    private LinearLayout mheaderLinerLayout;
    private ImageView mReloadData;
    private LinearLayout mOpenDrawable , mNotification_LinearLayout , mApp_icon;
    private BottomNavigationView navigation;
    private FrameLayout frameLayout;
    private ImageView mNotificationFragment;
    private RelativeLayout mNotification;
    private TextView mNotificationBadge;
    Deque<Integer> integerDeque = new ArrayDeque<>(6);
    private List<NotificationDataModel> notificationDataModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    public void calledFromFragment(String tag)
//    {
//        if(tag.equals(KnowledgeFragment.newInstance())){
//
//        }
//
//    }

    @Override
    protected int getContentView() {
        return R.layout.activity_second_main;
    }

    @Override
    protected void initView() {
        mActionBar = getSupportActionBar();
        initData();
        initToolbar("");

        SecondMainActivity.this.getSupportActionBar().show();

    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initListener() {

        navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
        TagDataModelList = new ArrayList<>();

        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        mOpenDrawable = findViewById(R.id.OpenDrawable);
        mApp_icon = findViewById(R.id.App_icon);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
        navUsername = headerView.findViewById(R.id.textViewName);
        navUserEmail = headerView.findViewById(R.id.textViewEmail);
        mimageViewAvatar = headerView.findViewById(R.id.imageViewAvatar);
        mPostCountheader = headerView.findViewById(R.id.PostCountheader);
        mfollowersbtnHeader = headerView.findViewById(R.id.followersbtnHeader);
        mfollowingbtnHeader = headerView.findViewById(R.id.followingbtnHeader);
        mheaderLinerLayout = headerView.findViewById(R.id.headerLinerLayout);
        mReloadData = findViewById(R.id.ReloadData);
       // mNotification = findViewById(R.id.Notification);
        mNotification_LinearLayout = findViewById(R.id.Notification_LinearLayout);
        frameLayout = findViewById(R.id.contentFrame);
        mNotificationFragment = findViewById(R.id.notification_Fragment);
        mNotificationBadge = findViewById(R.id.NotificationBadge);



        //  mNotificationBadge.setNumber(8);


        name = SharedPrefsHelper.getInstance().get(NAME);
        profileUrl = SharedPrefsHelper.getInstance().get(PROFILE_URL);
        if (name != null)
            navUsername.setText(capitalizeWord(name));
        if (profileUrl != null && !profileUrl.isEmpty())
            Glide.with(SecondMainActivity.this).load(profileUrl).placeholder(R.drawable.ic_user_profile).into(mimageViewAvatar);
        if (SharedPrefsHelper.getInstance().get(COLLEGE_NAME) != null)
            navUserEmail.setText(capitalizeWord(SharedPrefsHelper.getInstance().get(COLLEGE_NAME).toString()));

        getFollowers();
        getNotificationData();

//        mReloadData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirstPreferenceFragment fragment = new FirstPreferenceFragment();
//                fragment.iconClick();
//            }
//        });

//        MaterialShapeDrawable bottomBarBackground = (MaterialShapeDrawable) navigation.getBackground();
//        bottomBarBackground.setShapeAppearanceModel(
//                bottomBarBackground.getShapeAppearanceModel()
//                        .toBuilder()
//                        .setTopRightCorner(CornerFamily.ROUNDED,100)
//                        .setTopLeftCorner(CornerFamily.ROUNDED,100)
//                        .build());

        mheaderLinerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile();
            }
        });

        mOpenDrawable.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RtlHardcoded")
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        mNotificationFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .add(android.R.id.content, new NotificationFragment()).addToBackStack(null).commit();
            }
        });

//        frameLayout.setOnTouchListener(new OnSwipeTouchListener(SecondMainActivity.this) {
//            public void onSwipeTop() {
//                Toast.makeText(SecondMainActivity.this, "top", Toast.LENGTH_SHORT).show();
//            }
//            public void onSwipeRight() {
//
//                // todo function to change fragments something like
//                getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(
//                                R.anim.slide_in,  // enter
//                                R.anim.slide_out // exit
//                        )
//                        .replace(R.id.fragmentContainer,
//                                selectedFragment).commit();
//
//                Toast.makeText(MyActivity.this, "right", Toast.LENGTH_SHORT).show();
//            }
//            public void onSwipeLeft() {
//                Toast.makeText(MyActivity.this, "left", Toast.LENGTH_SHORT).show();
//            }
//            public void onSwipeBottom() {
//                Toast.makeText(MyActivity.this, "bottom", Toast.LENGTH_SHORT).show();
//            }
//
//        });

        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });


    }

    private void Profile() {
        Intent intent = new Intent(context, ProfileFragment.class);
        intent.putExtra("USER_ID", userID);
        startActivity(intent);
        // TODO : Udaya Profile
    }


    public void iconClick() {

    }

    private void getNotificationData() {
        setUpNetwork();
        String UserID = SharedPrefsHelper.getInstance().get(USER_ID);
        Call<NewAPIResponseModel> userNotificationsModelCall = apiInterface.GetNotificationsByID(UserID , 1);
        userNotificationsModelCall.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                notificationDataModelList = new ArrayList<>();
                notificationDataModelList = response.body().getmNotificationDataList();
                if(notificationDataModelList.size()>0) {
                    mNotificationBadge.setVisibility(View.VISIBLE);
                    mNotificationBadge.setText(notificationDataModelList.size() + "");
                }else {
                    mNotificationBadge.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {

            }

        });
    }

    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.Profile:
                    Profile();
                    break;
//                case R.id.Message:
//                    Toast.makeText(SecondMainActivity.this, "Need Add ", Toast.LENGTH_SHORT).show();
//                    break;
                case R.id.SearchUser:
                    Intent intent = new Intent(SecondMainActivity.this, UserProfileSearchActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_share:
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, " Hi , download this app. it is has amazing \n\n" +
                            " Student Continent Network of Young Minds  \n\n " + " https://play.google.com/apps/internaltest/4699837930120847039");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Student Continent");
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    break;
                case R.id.LogOut:

                    SharedPrefsHelper.getInstance().clearAllData();
                    startActivity(new Intent(SecondMainActivity.this, NewLoginActivity.class));
                    finish();
                    // startActivity(new Intent(mActivity, UserProfileSearchActivity.class));
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
    };


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); //
            //fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
            Fragment curFrag = fragmentManager.getPrimaryNavigationFragment();
            switch (item.getItemId()) {
                case R.id.navigation_home:

//                    navigation.getMenu().setGroupCheckable(0, true, true);
//                    navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_SELECTED);
                    Log.d(TAG, "onNavigationItemSelected: ");
                    if (curFrag != null) {
                        fragmentTransaction.detach(curFrag);
                    }
                    Fragment fragment = fragmentManager.findFragmentByTag("ABOUT");
                    if (fragment == null) {
                        fragment = new KnowledgeFragment();
                        fragmentTransaction.add(R.id.contentFrame, fragment, "ABOUT");
                    } else {
                        fragmentTransaction.attach(fragment);
                    }
                    fragmentTransaction.setPrimaryNavigationFragment(fragment);
                    fragmentTransaction.setReorderingAllowed(true);
                    fragmentTransaction.commit();
                    // setupToolBar(SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE_NAME).toString());
                    // initToolbar(SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE_NAME).toString());
                    return true;

                case R.id.navigation_outdoor:
//                    navigation.getMenu().setGroupCheckable(0, true, true);
//                    navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_SELECTED);
                    if (curFrag != null) {
                        fragmentTransaction.detach(curFrag);
                    }
                    fragment = fragmentManager.findFragmentByTag("CURRENT");
                    if (fragment == null) {
                        fragment = new OutdoorFragment();
                        fragmentTransaction.add(R.id.contentFrame, fragment, "CURRENT");
                    } else {
                        fragmentTransaction.attach(fragment);
                    }
                    fragmentTransaction.setPrimaryNavigationFragment(fragment);
                    fragmentTransaction.setReorderingAllowed(true);
                    fragmentTransaction.commit();
                    // setupToolBar(getString(R.string.outdoor));
                    // initToolbar(getString(R.string.outdoor));
                    // mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_games:
//                    navigation.getMenu().setGroupCheckable(0, true, true);
//                    navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_SELECTED);
                    if (curFrag != null) {
                        fragmentTransaction.detach(curFrag);
                    }
                    fragment = fragmentManager.findFragmentByTag("JOB");
                    if (fragment == null) {
                        fragment = new GamesFragment();
                        fragmentTransaction.add(R.id.contentFrame, fragment, "JOB");
                    } else {
                        fragmentTransaction.attach(fragment);
                    }
                    fragmentTransaction.setPrimaryNavigationFragment(fragment);
                    fragmentTransaction.setReorderingAllowed(true);
                    fragmentTransaction.commit();
                    //setupToolBar(getString(R.string.games));
                    // initToolbar(getString(R.string.games));
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.navigation_aspiration:
//                    navigation.getMenu().setGroupCheckable(0, true, true);
//                    navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_SELECTED);
                    if (curFrag != null) {
                        fragmentTransaction.detach(curFrag);
                    }
                    fragment = fragmentManager.findFragmentByTag("dfs");
                    if (fragment == null) {
                        fragment = new AspirationFragment();
                        fragmentTransaction.add(R.id.contentFrame, fragment, "dfs");
                    } else {
                        fragmentTransaction.attach(fragment);
                    }
                    fragmentTransaction.setPrimaryNavigationFragment(fragment);
                    fragmentTransaction.setReorderingAllowed(true);
                    fragmentTransaction.commit();
                    // setupToolBar(SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_ASPIRATION_NAME).toString());
                    // initToolbar(SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_ASPIRATION_NAME).toString());
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;

             /*   case R.id.navigation_new_notification:
//                    navigation.getMenu().setGroupCheckable(0, true, true);
//                    navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_SELECTED);
                    if (curFrag != null) {
                        fragmentTransaction.detach(curFrag);
                    }
                    fragment = fragmentManager.findFragmentByTag("notifications");
                    if (fragment == null) {
                        fragment = new NotificationFragment();
                        fragmentTransaction.add(R.id.contentFrame, fragment, "notifications");
                    } else {
                        fragmentTransaction.attach(fragment);
                    }
                    fragmentTransaction.setPrimaryNavigationFragment(fragment);
                    fragmentTransaction.setReorderingAllowed(true);
                    fragmentTransaction.commit();
                   // setupToolBar(getString(R.string.title_notifications));
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;

              */

                case R.id.navigation_trending:
//                    navigation.getMenu().setGroupCheckable(0, true, true);
//                    navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_SELECTED);
                    if (curFrag != null) {
                        fragmentTransaction.detach(curFrag);
                    }
                    fragment = fragmentManager.findFragmentByTag("trending");
                    if (fragment == null) {
                        fragment = new TrendingFragment();
                        fragmentTransaction.add(R.id.contentFrame, fragment, "trending");
                    } else {
                        fragmentTransaction.attach(fragment);
                    }
                    fragmentTransaction.setPrimaryNavigationFragment(fragment);
                    fragmentTransaction.setReorderingAllowed(true);
                    fragmentTransaction.commit();
                    //  setupToolBar(getString(R.string.trending));
                    // initToolbar(getString(R.string.trending));
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
            }

            return false;


        }
    };


    String userID = SharedPrefsHelper.getInstance().get(USER_ID).toString();
    private List<FollowModel> mFollowingModelList, mFollowersModelList;

    public void getFollowers() {
        setUpNetwork();
        Call<NewAPIResponseModel> listCall = apiInterface.GetFollowers(userID, "0");
        listCall.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                mFollowingModelList = response.body().getmFollowList();
                Call<NewAPIResponseModel> listCall2 = apiInterface.GetFollowers(userID, "1");
                listCall2.enqueue(new Callback<NewAPIResponseModel>() {
                    @Override
                    public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                        mFollowersModelList = response.body().getmFollowList();
                        mfollowingbtnHeader.setText(mFollowingModelList.size() + "");
                        mfollowersbtnHeader.setText(mFollowersModelList.size() + "");
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


//    @Override
//    public void onBackPressed() {
//       onBackPressed();
//    }

    private void initToolbar(String title) {
        mToolbar.setTitle(title);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        mToolbar.setTitleTextAppearance(this, R.style.RobotoBoldTextAppearance);
    }


    public Toolbar mToolbar;

    @Override
    protected void initData() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        updateFcmToken();
    }

//    private void setupToolBar(String text) {
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mToolbar.setTitle(text);
//
//        mToolbar.setSelected(true);
//        setSupportActionBar(mToolbar);
//
//      //  Toolbar toolbar=findViewById(R.id.toolbar);
//
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//    }


    private void updateFcmToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        final String token = task.getResult().getToken();
                        if (SharedPrefsHelper.getInstance().get("TOKEN") == null || !token.equalsIgnoreCase(SharedPrefsHelper.getInstance().get("TOKEN").toString())) {
                            Log.d(TAG, "onComplete: " + token);
                            setUpNetwork();
                            String phone = SharedPrefsHelper.getInstance().get(USER_ID);
                            Call<APIResponseModel> apiResponseModelCall = apiInterface.updateFcm(phone, token);
                            apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
                                @Override
                                public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                                    if (response.isSuccessful() && response.body().getPhone() != null) {
                                        SharedPrefsHelper.getInstance().save("TOKEN", token);
                                        Log.d(TAG, "onResponse updated token: " + token);
                                        SendNotificationModel sendNotificationModel = new SendNotificationModel();
                                        Notification notification = new Notification();
                                        notification.setTitle("Notification Title ");
                                        notification.setBody("Notification Body");
                                        Data data = new Data();
                                        data.setTitle("Data Title");
                                        data.setBody("Data Body");
                                        sendNotificationModel.setData(data);
                                        sendNotificationModel.setNotification(notification);
                                        sendNotificationModel.setCollapse_key("type_a");
                                        String[] ids = new String[]{token};
                                        sendNotificationModel.setRegistration_ids(ids);
                                        Gson gson = new Gson();
                                        Log.d(TAG, "submitData json: " + gson.toJson(sendNotificationModel));
                                    }
                                }

                                @Override
                                public void onFailure(Call<APIResponseModel> call, Throwable t) {

                                }
                            });
                        }
                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        //  Log.d(TAG, msg);
                        // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
    private ApiInterface apiInterface;

    private void setUpNetwork() {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


//    @Override
//    public void onBackPressed() {
//        Toast.makeText(this, "Tap to exit", Toast.LENGTH_SHORT).show();
//    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment curFrag = fragmentManager.getPrimaryNavigationFragment();
        switch (item.getItemId()) {
            case R.id.navigation_home:
//                navigation.getMenu().setGroupCheckable(0, true, true);
//                navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_SELECTED);
                Log.d(TAG, "onNavigationItemSelected: ");
                if (curFrag != null) {
                    fragmentTransaction.detach(curFrag);
                }
                Fragment fragment = fragmentManager.findFragmentByTag("ABOUT");
                if (fragment == null) {
                    fragment = new KnowledgeFragment();
                    fragmentTransaction.add(R.id.contentFrame, fragment, "ABOUT");
                } else {
                    fragmentTransaction.attach(fragment);
                }
                fragmentTransaction.setPrimaryNavigationFragment(fragment);
                fragmentTransaction.setReorderingAllowed(true);
                fragmentTransaction.commitNowAllowingStateLoss();
                // initToolbar(SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE_NAME).toString());
                break;

        }

        return true;
    }

    @Override
    public void onBackPressed() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        int seletedItemId = bottomNavigationView.getSelectedItemId();
        if (R.id.navigation_home != seletedItemId) {
            setHomeItem(SecondMainActivity.this);
        } else {
            super.onBackPressed();
        }

        // initToolbar(SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE_NAME).toString());
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

    public static void setHomeItem(Activity activity) {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                activity.findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPrefsHelper.getInstance().get(COLLEGE_NAME) == null){
            startActivity(new Intent(SecondMainActivity.this, LoginActivity.class));
        }

    }
}