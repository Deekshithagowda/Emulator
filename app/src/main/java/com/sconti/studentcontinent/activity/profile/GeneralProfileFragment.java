package com.sconti.studentcontinent.activity.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.othersprofile.OthersProfilePresenter;
import com.sconti.studentcontinent.activity.othersprofile.OthersProfilePresenterImpl;
import com.sconti.studentcontinent.activity.othersprofile.OthersProfileView;
import com.sconti.studentcontinent.activity.outdoor.adapter.OutDoorTagsPrefAdapter;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorPostModel;
import com.sconti.studentcontinent.activity.profile.editProfile.EditProfileActivity;
import com.sconti.studentcontinent.base.BaseFragment;
import com.sconti.studentcontinent.model.FollowModel;
import com.sconti.studentcontinent.model.LikeDataModel;
import com.sconti.studentcontinent.model.NewAPIResponseModel;
import com.sconti.studentcontinent.model.UserDetails;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.COLLEGE_NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_ASPIRATION_NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE_NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;


public class GeneralProfileFragment extends BaseFragment implements OthersProfileView, View.OnClickListener {

    private static final String TAG = "OthersProfileActivity";
    private OthersProfilePresenter mOthersProfilePresenter;
    private String userId , CurrentUserID;
    private TextView mTextViewDegree, mTextViewInterest,  mTextViewFollowing , mTextViewSelectedCollage , mEditProfile;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_general_profile, container, false);
        initView();
        initData();

        setUpNetwork();

        return mView;
    }



    private void initData() {
        Bundle bundle=getArguments();
        userId = bundle.getString("USER_ID");
        String CurrentUserID = SharedPrefsHelper.getInstance().get(USER_ID).toString();
        if(CurrentUserID.equals(userId)){
            mEditProfile.setText(R.string.edit);
            if (SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE_NAME) != null)
                mTextViewDegree.setText(SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE_NAME).toString());
            if (SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_ASPIRATION_NAME) != null)
                mTextViewInterest.setText(SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_ASPIRATION_NAME).toString());
            if (SharedPrefsHelper.getInstance().get(COLLEGE_NAME) != null)
                mTextViewSelectedCollage.setText(SharedPrefsHelper.getInstance().get(COLLEGE_NAME).toString());
        }else {
            mEditProfile.setText(R.string.follow);
            mOthersProfilePresenter = new OthersProfilePresenterImpl(this);
            Log.d(TAG, "initData: logdId:" + userId);
            mOthersProfilePresenter.getProfileData(userId);
        }

    }

    private void initView() {
        mTextViewDegree = mView.findViewById(R.id.textViewSelectedDegree);
        mTextViewInterest = mView.findViewById(R.id.textViewSelectedAspirant);
        mTextViewSelectedCollage = mView.findViewById(R.id.textViewSelectedCollage);
        mEditProfile = mView.findViewById(R.id.EditProfile);

        initListener();
    }





    @Override
    public void updateProfileData(final UserDetails userDetails) {
        if(userDetails != null){
            if (userDetails.getDegree() != null)
                mTextViewDegree.setText(userDetails.getDegree());
            if (userDetails.getInterest() != null)
                mTextViewInterest.setText(userDetails.getInterest());
            if (userDetails.getVillage() != null)
                mTextViewSelectedCollage.setText(userDetails.getVillage());
        }
    }

    @Override
    public void showProgressbar() {
    }

    @Override
    public void hideProgressbar() {
    }

    @Override
    public void updateFollowers(int IamFollowing, int IhaveFollowers, List<FollowModel> followingModel, List<FollowModel> body) {

    }

    @Override
    public void updateAddedFollowers(boolean val) {

    }


    private void initListener() {
//        Bundle bundle1 = getArguments();
//        String userId1 = bundle1.getString("USER_ID");
//        String CurrentUserID = SharedPrefsHelper.getInstance().get(USER_ID).toString();
//        if(CurrentUserID.equals(userId1)){
//            mEditProfile.setText(R.string.edit);
//
//        }else {
//            mEditProfile.setText(R.string.follow);
//
//        }



    }

    protected ApiInterface apiInterface;
    private void setUpNetwork() {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    @Override
    public void onClick(View v) {

    }
}