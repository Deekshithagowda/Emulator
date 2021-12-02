package com.sconti.studentcontinent.activity.outdoor.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;


import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sconti.studentcontinent.ContinentApplication;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.details.DetailsActivity;
import com.sconti.studentcontinent.activity.knowledge.adapter.LatestTrendsAdapter;
import com.sconti.studentcontinent.activity.othersprofile.OthersProfileActivity;
import com.sconti.studentcontinent.activity.outdoor.adapter.OutDoorTagsPrefAdapter;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorPostModel;
import com.sconti.studentcontinent.activity.post.PostActivity;
import com.sconti.studentcontinent.activity.trending.TrendingActivity;
import com.sconti.studentcontinent.adapter.OutDoorFirstPrefAdapter;
import com.sconti.studentcontinent.base.BaseFragment;
import com.sconti.studentcontinent.model.APIResponseModel;
import com.sconti.studentcontinent.model.CommentModel;
import com.sconti.studentcontinent.model.FirstPreferenceDataModel;
import com.sconti.studentcontinent.model.FollowModel;
import com.sconti.studentcontinent.model.LatestTrendsModel;
import com.sconti.studentcontinent.model.LikeDataModel;
import com.sconti.studentcontinent.model.NewAPIResponseModel;
import com.sconti.studentcontinent.model.ShareDataModel;
import com.sconti.studentcontinent.model.SubjectListModel;
import com.sconti.studentcontinent.model.UserDataModel;
import com.sconti.studentcontinent.model.UserResponseModel;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;
import com.sconti.studentcontinent.utils.AppUtils;
import com.sconti.studentcontinent.utils.RecyclerItemClickListener;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;
import com.sconti.studentcontinent.utils.tools.ContinentTools;
import com.sconti.studentcontinent.utils.tools.FilePath;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.EMAIL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.MOBILE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.OUTDOOR_FIRST;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.PROFILE_URL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_OUTDOOR;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_FEEDS;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_RESPONSES;

public class SecondPreferenceFragment extends BaseFragment implements OutDoorFirstPrefAdapter.SelectItem{
    private static final String TAG = "SecondPreferenceFragmen";
    private SecondPreferenceViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private OutDoorFirstPrefAdapter firstPrefAdapter;
    private View mRootView;
    private EditText mEditTextTitle, mEditTextDescription;
    private String mImageURL, mVideoURL;
    private Button mButtonSubmit;
    private ImageView mImageViewAttachment, mImageViewVideoAttachment;
    private DatabaseReference databaseReference;
    private List<FirstPreferenceDataModel> preferenceDataModelList;
    private List<OutDoorPostModel> TagDataModelList;
    private UserDataModel mUserDataModel;
    private String mSelectedSubject;
    private List<SubjectListModel> mSubjectList;
    private LinearLayout mLinearLayoutMessage;
    private View focusSelection;
    private RadioGroup radioGroup;
    private Button mButtonSelection;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int PICK_IMAGE_REQUEST_2 = 1111;
    private final int PICK_VIDEO_REQUEST_2 = 2222;
    private boolean isVideoSelected;
    private List<LikeDataModel> likeDatalist;
    private AlertDialog mAlertDialogFocusSelection;
    private RelativeLayout mRelativeLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mLatestTrendsRecyclerView;
    int PERMISSION_ALL = 1;
    int offset = 20;
    String[] PERMISSIONS = {
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.RECORD_AUDIO,
    };

    private static final int SELECT_FILE = 1100;
    private static final int REQUEST_CAMERA = 1200;
    private static final int SELECT_VIDEO = 1300;
    private static final int REQUEST_CODE_DOC = 1400;
    private static final int RQS_RECORDING = 1500;
    private static final int VIDEO_CAPTURE = 1600;
    private static final int AUDIO_LOCAL = 1700;
    private List<ShareDataModel> shareDataList;
    private List<CommentModel> commentDataList;


    public static SecondPreferenceFragment newInstance() {
        return new SecondPreferenceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.second_preference_fragment, container, false);
        initViews();
        initListener();
        Bundle bundle = getArguments();

        setUpNetwork();
        initData();
        mSwipeRefreshLayout = mRootView.findViewById(R.id.swipeLayout2);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLikeListByUserID();
                // getData(filterModel);
                // getTags();
              //  getLatestTrendsData();
                getCategory(mSelectedSubject , 1 , 5 , offset);
            }
        });

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                getLikeListByUserID();
                // getData(filterModel);
                //getTags();
               // getLatestTrendsData();
                getCategory(mSelectedSubject , 1 , 5 , offset);
            }
        });

        if (mSelectedSubject != null)
            focusSelection.setVisibility(View.GONE);
            //getData();

            //focusSelection.setVisibility(View.VISIBLE);
            mLinearLayoutMessage.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
         //   getSubjectList();

        return mRootView;
    }


    private void getLikeListByUserID() {
        String userID = SharedPrefsHelper.getInstance().get(USER_ID);
        Log.d(TAG, "IDD" + userID);
        Call<NewAPIResponseModel> call = apiInterface.GetLikeListByUserID(userID, 5);
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


//    private void getSubjectList() {
//        databaseReference = ContinentApplication.getFireBaseRef();
//        databaseReference = databaseReference.child(SUBJECT_LIST_OUTDOOR);
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mSubjectList = new ArrayList<>();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    mSubjectList.add(snapshot.getValue(SubjectListModel.class));
//                    preferenceDataModelList = new ArrayList<>();
//                    if(snapshot.getValue(SubjectListModel.class)!=null && !snapshot.getValue(SubjectListModel.class).getKey().equalsIgnoreCase(mSelectedSubject))
//                    {
//                        getData(snapshot.getValue(SubjectListModel.class).getKey());
//                    }
//                }
//               // focusSelectionAlertList(mSubjectList);
//                // mProgressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void focusSelectionAlertList(final List<SubjectListModel> mSubjectList) {
        if (mActivity != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            LayoutInflater inflater = mActivity.getLayoutInflater();
            final View view = inflater.inflate(R.layout.focus_select_lyt, null);
            builder.setView(view);
            radioGroup = view.findViewById(R.id.radiogroup);
            for (int i = 0; i < mSubjectList.size(); i++) {
                RadioButton rdbtn = new RadioButton(mActivity);
                rdbtn.setId(View.generateViewId());
               /* if(i==0)
                {
                    rdbtn.setChecked(true);
                    mSelectedSubject = mSubjectList.get(i).getKey();
                    SharedPrefsHelper.getInstance().save(SELECTED_SUBJECT,mSelectedSubject);
                }*/
                rdbtn.setText(mSubjectList.get(i).getName());
                radioGroup.addView(rdbtn);
            }

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int idRadioButtonChosen = radioGroup.getCheckedRadioButtonId();
                    if (idRadioButtonChosen > 0) {
                        RadioButton radioButtonChosen = view.findViewById(idRadioButtonChosen);
                        mSelectedSubject = mSubjectList.get(idRadioButtonChosen - 1).getKey();
                        SharedPrefsHelper.getInstance().save(SELECTED_SUBJECT_OUTDOOR, mSelectedSubject);
                    } else {
                        Toast.makeText(mActivity, "Select Subject", Toast.LENGTH_LONG).show();
                    }
                }
            });

            mButtonSelection = view.findViewById(R.id.buttonSelected);
            mButtonSelection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // focusSelection.setVisibility(View.GONE);
                    mAlertDialogFocusSelection.dismiss();
                    //getData();
                }
            });
            mAlertDialogFocusSelection = builder.create();
            mAlertDialogFocusSelection.show();
        }
    }



    private void initData() {
        mSelectedSubject = SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_OUTDOOR, null);
        mUserDataModel = new UserDataModel();
        mUserDataModel.setEmail(SharedPrefsHelper.getInstance().get(EMAIL, ""));
        mUserDataModel.setMobile(SharedPrefsHelper.getInstance().get(MOBILE, ""));
        mUserDataModel.setName(SharedPrefsHelper.getInstance().get(NAME, ""));
    }

    private OutDoorFirstPrefAdapter.SelectItem selectItemInterface = new OutDoorFirstPrefAdapter.SelectItem() {
        @Override
        public void selectedPosition(int position) {
            Intent intent = new Intent(getContext(), DetailsActivity.class);
            intent.putExtra("DATA", preferenceDataModelList.get(position));
            intent.putExtra("TYPE", OUTDOOR_FIRST);
            intent.putExtra("SUBJECT", mSelectedSubject);
            mActivity.startActivity(intent);
        }


        @Override
        public void openAlert(FirstPreferenceDataModel dataModel) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
            LayoutInflater inflater = mActivity.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.more_alert, null);
            TextView report, copyLink, share, mute, onNotification, unfollow;
            report = dialogView.findViewById(R.id.Report);
            copyLink = dialogView.findViewById(R.id.copy_link);
            share = dialogView.findViewById(R.id.share_to);
            mute = dialogView.findViewById(R.id.mute);
            onNotification = dialogView.findViewById(R.id.turn_on_post_noti);
            unfollow = dialogView.findViewById(R.id.unfollow);

            report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mActivity, "Report", Toast.LENGTH_LONG).show();
                }
            });

            alertBuilder.setView(dialogView);
            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
        }

        @Override
        public void reachedLastItem() {
            getMoreData();
        }
    };

    private FirstPreferenceDataModel lastItemInQuery;

//    private void getData(String subject) {
//        mRelativeLayout.setVisibility(View.VISIBLE);
//        // mLinearLayoutMessage.setVisibility(View.VISIBLE);
//        databaseReference = ContinentApplication.getFireBaseRef();
//        Query imagesQuery = databaseReference.child(OUTDOOR_FIRST).child(subject).orderByKey().limitToLast(AppUtils.Constants.QUERY_LIMIT);
//        databaseReference = databaseReference.child(OUTDOOR_FIRST).child(subject);
//        imagesQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    preferenceDataModelList.add(snapshot.getValue(FirstPreferenceDataModel.class));
//                }
//                getLikeDetails();
//
//                mRecyclerView.setVisibility(View.VISIBLE);
//                Collections.reverse(preferenceDataModelList);
//                if (preferenceDataModelList.size() == AppUtils.Constants.QUERY_LIMIT) {
//                    lastItemInQuery = preferenceDataModelList.remove(preferenceDataModelList.size() - 1);
//                }
//
//                firstPrefAdapter = new OutDoorFirstPrefAdapter(mActivity, preferenceDataModelList, selectItemInterface, OUTDOOR_FIRST);
//                mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
//                mRecyclerView.setAdapter(firstPrefAdapter);
//                mRelativeLayout.setVisibility(View.GONE);
//
//                // mProgressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                mRelativeLayout.setVisibility(View.GONE);
//            }
//        });
//    }

    private boolean isLoadingMoreData;
    private List<FirstPreferenceDataModel> moreDataList;

    private void getMoreData() {
        if (!isLoadingMoreData && lastItemInQuery != null) {
            mRelativeLayout.setVisibility(View.VISIBLE);
            // mLinearLayoutMessage.setVisibility(View.VISIBLE);
            Log.d(TAG, "getMoreData: ");
            databaseReference = ContinentApplication.getFireBaseRef();
            String mLastKey = lastItemInQuery.getKey();
            isLoadingMoreData = true;
            Query imagesQuery = databaseReference.child(OUTDOOR_FIRST).child(mSelectedSubject).orderByKey().endAt(mLastKey).limitToLast(AppUtils.Constants.QUERY_LIMIT);
            //  databaseReference = databaseReference.child(OUTDOOR_FIRST).child(mSelectedSubject);
            imagesQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //Collections.reverse(preferenceDataModelList);
                    moreDataList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        moreDataList.add(snapshot.getValue(FirstPreferenceDataModel.class));
                        //preferenceDataModelList.add(preferenceDataModelList.size(),snapshot.getValue(FirstPreferenceDataModel.class));
                    }
                    getLikeDetails();

                    mRecyclerView.setVisibility(View.VISIBLE);
                    Collections.reverse(moreDataList);
                    if (moreDataList.size() == AppUtils.Constants.QUERY_LIMIT) {
                        lastItemInQuery = moreDataList.remove(moreDataList.size() - 1);
                    } else {
                        lastItemInQuery = null;
                    }
                    preferenceDataModelList.addAll(moreDataList);

                    firstPrefAdapter = new OutDoorFirstPrefAdapter(mActivity, preferenceDataModelList, selectItemInterface, OUTDOOR_FIRST);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
                    mRecyclerView.setAdapter(firstPrefAdapter);
                    isLoadingMoreData = false;
                    mRelativeLayout.setVisibility(View.GONE);


                    // mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    mRelativeLayout.setVisibility(View.GONE);

                }
            });
        }
    }

    private void getLikeDetails() {
        String uid = SharedPrefsHelper.getInstance().get(USER_ID, null);
        if(uid!=null && mSelectedSubject!=null) {
            DatabaseReference databaseReference1 = ContinentApplication.getFireBaseRef().child(OUTDOOR_FIRST).child(USER_RESPONSES).child(USER_FEEDS).child(mSelectedSubject).child(uid);
            databaseReference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<UserResponseModel> userResponseModelList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        userResponseModelList.add(snapshot.getValue(UserResponseModel.class));
                    }
                    if (userResponseModelList != null && userResponseModelList.size() > 0) {
                        for (int i = 0; i < userResponseModelList.size(); i++) {
                            for (int j = 0; j < preferenceDataModelList.size(); j++) {
                                if (userResponseModelList.get(i).getId().equalsIgnoreCase(preferenceDataModelList.get(j).getId())) {
                                    preferenceDataModelList.get(j).setCommented(userResponseModelList.get(i).isCommented());
                                    preferenceDataModelList.get(j).setLiked(userResponseModelList.get(i).isLiked());
                                    preferenceDataModelList.get(j).setShared(userResponseModelList.get(i).isShared());
                                }
                            }
                        }
                    }
                    firstPrefAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void initListener() {
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String desc = mEditTextDescription.getText().toString().trim();
                if (desc != null && desc.length() > 2) {
                    submitData();
                } else {
                    Toast.makeText(getContext(), "Please write description", Toast.LENGTH_LONG).show();
                }
            }
        });
        mImageViewAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isVideoSelected = false;
                addPhoto();
            }
        });

        mImageViewVideoAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isVideoSelected = true;
                addVideo();
            }
        });
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getContext(), TrendingActivity.class);
                        intent.putExtra("DATA", "ALL");
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    private FloatingActionButton floatingActionButton;
    @SuppressLint("RestrictedApi")
    private void initViews() {
        focusSelection = mRootView.findViewById(R.id.focus_select_view);
        mLinearLayoutMessage = mRootView.findViewById(R.id.linearlayoutEditMessage);
        mEditTextTitle = mRootView.findViewById(R.id.editTextTitle);
        mEditTextDescription = mRootView.findViewById(R.id.editTextDetails);
        mButtonSubmit = mRootView.findViewById(R.id.submitButton);
        mImageViewAttachment = mRootView.findViewById(R.id.imageViewAttach);
        mImageViewVideoAttachment = mRootView.findViewById(R.id.videoAttach);
        mRecyclerView = mRootView.findViewById(R.id.recycler_view);
        mLatestTrendsRecyclerView = mRootView.findViewById(R.id.recycler_latest2);
        mRelativeLayout = mRootView.findViewById(R.id.progress_bar);
        floatingActionButton = mRootView.findViewById(R.id.fabAddPost);
        floatingActionButton.setVisibility(View.GONE);
        /*
        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Click action
               // mSelectedSubject = SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_OUTDOOR, null);
                Intent intent = new Intent(mActivity, PostActivity.class);
                intent.putExtra("TYPE", OUTDOOR_FIRST);
                intent.putExtra("FAV", mSelectedSubject);
                startActivity(intent);
            }
        });
*/
    }

    private void submitData() {
        String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
        FirstPreferenceDataModel firstPreferenceDataModel;
        firstPreferenceDataModel = new FirstPreferenceDataModel();
        firstPreferenceDataModel.setTitle(mEditTextTitle.getText().toString());
        firstPreferenceDataModel.setDescription(mEditTextDescription.getText().toString());
        firstPreferenceDataModel.setDatetime(System.currentTimeMillis());
        firstPreferenceDataModel.setImageURL(mImageURL);
        firstPreferenceDataModel.setVideoURL(mVideoURL);
        String uid = SharedPrefsHelper.getInstance().get(USER_ID, null);
        firstPreferenceDataModel.setPostedBy(uid);
        databaseReference = ContinentApplication.getFireBaseRef().child(OUTDOOR_FIRST).child(mSelectedSubject);
        String key = databaseReference.push().getKey();
        firstPreferenceDataModel.setId(key);
        firstPreferenceDataModel.setKey(key);
        firstPreferenceDataModel.setPosterUrl(prfurl);
        databaseReference.child(key).setValue(firstPreferenceDataModel);
        mEditTextDescription.setText("");
    }

    List<LatestTrendsModel> trendsModelList = new ArrayList<>();
    public void onResume() {
        super.onResume();
       // getLatestTrendsData();
    }
    private void getLatestTrendsData()
    {
        Call<List<LatestTrendsModel>> listCall = apiInterface.GetTrends("2");
        listCall.enqueue(new Callback<List<LatestTrendsModel>>() {
            @Override
            public void onResponse(Call<List<LatestTrendsModel>> call, Response<List<LatestTrendsModel>> response) {
                mLatestTrendsRecyclerView.setVisibility(View.VISIBLE);
                trendsModelList = response.body();
                LatestTrendsAdapter firstPrefAdapter = new LatestTrendsAdapter(mActivity, response.body());
                mLatestTrendsRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
                mLatestTrendsRecyclerView.setAdapter(firstPrefAdapter);
             //   getCategory("All");
            }

            @Override
            public void onFailure(Call<List<LatestTrendsModel>> call, Throwable t) {

            }
        });

        mLatestTrendsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), mLatestTrendsRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "onItemClick: "+trendsModelList.get(position).getTitle());
                if(!trendsModelList.get(position).getTitle().equalsIgnoreCase("flower"))
                {
                //    getCategory(trendsModelList.get(position).getKeywords());
                }
                else
                {
                   // getCategory("All");
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SecondPreferenceViewModel.class);
    }

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri filePath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null)
            filePath = data.getData();
        // imageViewUploaded.setImageURI(filePath);
        // imageViewUploaded.setVisibility(View.VISIBLE);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            // imageViewUploaded.setImageURI(filePath);
            // imageViewUploaded.setVisibility(View.VISIBLE);
            //   if(filePath!=null)
            //  uploadImage();
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST_2) {
                uploadImage(filePath);
            } else if (requestCode == PICK_VIDEO_REQUEST_2) {
                uploadImage(filePath);
            } else if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data, "IMAGE");
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
            else if (requestCode == SELECT_VIDEO)
                onSelectFromGalleryResult(data, "VIDEO");
            else if (requestCode == REQUEST_CODE_DOC) {
                onSelectFromGalleryResult(data, "ALL");
            } else if (requestCode == AUDIO_LOCAL) {
                onSelectFromGalleryResult(data, "AUDIO");
            } else if (requestCode == RQS_RECORDING) {
                String result = data.getStringExtra("result");
                if (result != null) {
                    // uploadImageToAWS(new File(result), "AUDIO");
                } else {

                }
            } else if (requestCode == VIDEO_CAPTURE) {
                Log.d(TAG, "onActivityResult: ");
                //onSelectFromGalleryResult(data, "VIDEO");
                //  isVideoCaptured = true;
                uploadImage(Uri.fromFile(mediaFile));
                // uploadImageToAWS(mediaFile, "VIDEO");
            }
        }
    }

    private void uploadImage(Uri filePath) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images").child(OUTDOOR_FIRST).child(mSelectedSubject).child(UUID.randomUUID().toString());
            //UploadTask uploadTask =
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;

                                    {
                                        String url = downloadUrl.toString();
                                        if (isVideoSelected) {
                                            mVideoURL = url;
                                        } else {
                                            mImageURL = url;
                                        }

                                        /*if(url.contains(".jpg") || url.contains(".jpeg") || url.contains(".png"))
                                        {
                                            mImageURL = url;
                                        }
                                        else if(url.contains(".mp4") ) {
                                            mVideoURL = url;
                                        }*/

                                        //  textViewURL.setText(downloadUrl.toString());
                                    }
                                    //imageViewUploaded.setVisibility(View.VISIBLE);
                                }
                            });

                            Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });

        }
    }

    private void addPhoto() {
        if (!ContinentTools.hasPermissions(mActivity, PERMISSIONS)) {
            ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_ALL);
        }
        selectImage();
    }

    private void addVideo() {
        if (!ContinentTools.hasPermissions(mActivity, PERMISSIONS)) {
            ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_ALL);
        }
        selectVideo();
    }

    private void addDocument() {
        if (!ContinentTools.hasPermissions(mActivity, PERMISSIONS)) {
            ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_ALL);
        }
        //   getDocument();
    }

    private void addAudio() {
        if (!ContinentTools.hasPermissions(mActivity, PERMISSIONS)) {
            ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_ALL);
        }
        selectAudio();
    }

    private String userChoosenTask;

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Select Attachment!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean gallery = ContinentTools.checkPermission(mActivity);
                boolean camera = ContinentTools.checkPermissionCamera(mActivity);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (camera)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (gallery) {
                        chooseImage();
                        //galleryIntent();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_2);
    }

    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST_2);
    }

    private void selectVideo() {
        final CharSequence[] items = {"Capture Video", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Select Attachment!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean gallery = ContinentTools.checkPermission(mActivity);
                boolean camera = ContinentTools.checkPermissionCamera(mActivity);
                if (items[item].equals("Capture Video")) {
                    userChoosenTask = "Capture Video";
                    if (hasCamera()) {
                        videoCaptureIntent();
                    } else {
                        ContinentTools.showToast("No Camera Found");
                    }
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (gallery) {
                        chooseVideo();
                        //videoFromGalleryIntent();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectAudio() {
        final CharSequence[] items = {"Record Audio", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Select Attachment!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean gallery = ContinentTools.checkPermission(mActivity);
                boolean camera = ContinentTools.checkPermissionCamera(mActivity);
                if (items[item].equals("Record Audio")) {
                    userChoosenTask = "Record Audio";
                    audioIntent();
                    //recordingAlert();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    AudioLocalIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private boolean hasCamera() {
        if (mActivity.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY)) {
            return true;
        } else {
            return false;
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void videoFromGalleryIntent() {
        if (!ContinentTools.checkInternetStatus()) {
            ContinentTools.showToast("No Internet connected");
            //return;
        }
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra("return-data", true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivityForResult(Intent.createChooser(intent, "Select outdooricon Video "), SELECT_VIDEO);
    }

    private File mediaFile;

    private void videoCaptureIntent() {
        if (!ContinentTools.checkInternetStatus()) {
            ContinentTools.showToast("No Internet connected");
            // return;
        }
        mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp4");
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Uri videoUri = Uri.fromFile(mediaFile);
        /*if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            videoUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", mediaFile);
        }*/
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        //intent.putExtra(EXTRA_VIDEO_QUALITY, 0);
        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            startActivityForResult(intent, VIDEO_CAPTURE);
        }
    }

    private void audioIntent() {
        if (!ContinentTools.checkInternetStatus()) {
            ContinentTools.showToast("No Internet connected");
            //  return;
        }
        //Intent intent = new Intent(mActivity, AudioRecordActivity.class);
        //startActivityForResult(intent, RQS_RECORDING);
    }

    private void AudioLocalIntent() {
        if (!ContinentTools.checkInternetStatus()) {
            ContinentTools.showToast("No Internet connected");
            // return;
        }
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload, AUDIO_LOCAL);
    }

    private void onSelectFromGalleryResult(Intent data, String mediaType) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Uri path = data.getData();
        File original;
        if (path != null) {
            if (path.toString().contains("com.google.android.apps.photos")) {
                Log.d(TAG, "From android photos ");
                String filePath = FilePath.getPathFromInputStreamUri(mActivity, path);

                original = new File(filePath);
                String extension_file = original.getAbsolutePath().substring(original.getAbsolutePath().lastIndexOf("."));
                if (extension_file.equalsIgnoreCase(".jpg") || extension_file.equalsIgnoreCase(".jpeg") || extension_file.equalsIgnoreCase(".png")) {
                    crop_ImageAndUpload(original, extension_file, mediaType);
                } else {
                    //uploadImageToAWS(new File(filePath), mediaType);
                    uploadImage(Uri.fromFile(original));
                }
                //OustSdkTools.showToast("can't select attachment from google photos app");
                //return;
            } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                Log.d(TAG, "from SDK more than Kitkat");
                String filePath = getRealPathFromUri(mActivity, path);
                if (filePath != null) {
                    original = new File(filePath);
                    uploadImage(Uri.parse(filePath));
                  /*  String extension_file = original.getAbsolutePath().substring(original.getAbsolutePath().lastIndexOf("."));
                    if(extension_file.equalsIgnoreCase(".jpg") || extension_file.equalsIgnoreCase(".jpeg") || extension_file.equalsIgnoreCase(".png")) {
                        crop_ImageAndUpload(original,extension_file,mediaType);
                    }else {
                        uploadImage(Uri.parse(filePath));
                        //uploadImageToAWS(new File(filePath), mediaType);
                    }*/
                } else {
                    ContinentTools.showToast("unable to get file");
                }
            } else {

                String[] proj = {MediaStore.Images.Media.DATA};
                String result = null;

                CursorLoader cursorLoader = new CursorLoader(
                        mActivity,
                        path, proj, null, null, null);
                Cursor cursor = cursorLoader.loadInBackground();

                if (cursor != null) {
                    int column_index =
                            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    result = cursor.getString(column_index);
                    if (result != null) {
                        uploadImage(Uri.parse(result));
                        //uploadImageToAWS(new File(result), mediaType);
                    }
                }
            }
        }
    }

    private void storeImage(Bitmap image, File pictureFile) {
        //File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    public void crop_ImageAndUpload(File original, String extension_file, String mediaType) {
        try {
            //change the filepath
            Bitmap d = new BitmapDrawable(mActivity.getResources(), original.getPath()).getBitmap();
            int nh = (int) (d.getHeight() * (512.0 / d.getWidth()));
            Bitmap bitmap_new = Bitmap.createScaledBitmap(d, 512, nh, true);
            Log.d(TAG, "original:" + d.getByteCount() + " -- duplicate:" + bitmap_new.getByteCount());
            //Log.d(TAG, "Bitmap width:" + bitmap_new.getWidth() + " -- height:" + bitmap_new.getHeight());

            File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + "" + extension_file);
            // storeImage(bitmap_new, destination);

            Log.d(TAG, "file size  duplicate:" + destination.length() + " -- Original:" + original.length());
            // uploadImageToAWS(destination, mediaType);
            uploadImage(Uri.fromFile(original));
        } catch (Exception e) {
            e.printStackTrace();
            uploadImage(Uri.fromFile(original));
            //  uploadImageToAWS(original, mediaType);
            //Toast.makeText(this,"Couldn't able to load the image. Please try again.",Toast.LENGTH_LONG).show();
        }
    }

    public static String getRealPathFromUri(Context context, final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (destination != null) {
            String extension_file = destination.getAbsolutePath().substring(destination.getAbsolutePath().lastIndexOf("."));
            if (extension_file.equalsIgnoreCase(".jpg") || extension_file.equalsIgnoreCase(".jpeg") || extension_file.equalsIgnoreCase(".png")) {
                crop_ImageAndUpload(destination, extension_file, "IMAGE");
            } else {
                uploadImage(Uri.fromFile(destination));
                // uploadImageToAWS(destination, "IMAGE");
            }

        }
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {


        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private AlertDialog alertDialogMoreOptions;  // TODO : udaya add
    private OutDoorTagsPrefAdapter.SelectItem selectItemInterface3 = new OutDoorTagsPrefAdapter.SelectItem() {
        @Override
        public void selectedPosition(int position) {
            Intent intent = new Intent(getContext(), DetailsActivity.class);
            intent.putExtra("DATA", TagDataModelList.get(position));
            intent.putExtra("TYPE", OUTDOOR_FIRST);
            intent.putExtra("OUTDOOR_COMMENT", true);
            intent.putExtra("SUBJECT", mSelectedSubject);
            mActivity.startActivity(intent);
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
          //  playVideoAtThis();
        }

        @Override
        public void openAlert(final OutDoorPostModel dataModel) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
            LayoutInflater inflater = mActivity.getLayoutInflater();
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
                    Intent intent = new Intent(mActivity, PostActivity.class);
                    intent.putExtra("OUTDOOR_DATA", dataModel);
                    intent.putExtra("TYPE", OUTDOOR_FIRST);
                    intent.putExtra("SUBJECT", mSelectedSubject);
                    intent.putExtra("IS_EDIT", true);
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
                    if(alertDialogMoreOptions!=null)
                    {
                        alertDialogMoreOptions.dismiss();
                    }
                }
            });

            alertBuilder.setView(dialogView);
            alertDialogMoreOptions = alertBuilder.create();
            alertDialogMoreOptions.show();
        }

        @Override
        public void reachedLastItem() {
            getMoreData();
        }



        @Override
        public void clickOnProfile(String id) {
            if(!id.equalsIgnoreCase(SharedPrefsHelper.getInstance().get(USER_ID).toString())) {
                Intent intent = new Intent(mActivity, OthersProfileActivity.class);
                intent.putExtra("USER_ID", id);
                startActivity(intent);
            }
        }

        @Override
        public void updateLikeCount(final OutDoorPostModel dataModel, boolean isLiked) {
            ContinentTools likes = new ContinentTools();
            likes.commonUpdateLikeCount(dataModel, PostAdapter, apiInterface, TagDataModelList, 6, isLiked);
        }
    };

    private void updateShare(ShareDataModel shareDataModel) {
      //  mProgressBar.setVisibility(View.VISIBLE);
        Call<APIResponseModel> call = apiInterface.AddShare(shareDataModel, 6);
        call.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
             //   mProgressBar.setVisibility(View.GONE);
                if(response.body().isStatus()) {
                    AppUtils.showToastMessage(mActivity, mActivity.getString(R.string.shared_success));
                    getCategory(mSelectedSubject , 1 , 5 , offset);
                }
                else
                {
                    AppUtils.showToastMessage(mActivity, mActivity.getString(R.string.some_thing_wrong));
                }
            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {
              //  mProgressBar.setVisibility(View.GONE);
                AppUtils.showToastMessage(mActivity, mActivity.getString(R.string.some_thing_wrong));
            }
        });
    }


    private void deletePost(String postId) {
        mRelativeLayout.setVisibility(View.VISIBLE);
        Call<List<APIResponseModel>> hashTagCall = apiInterface.deletePost(postId, 6);
        hashTagCall.enqueue(new Callback<List<APIResponseModel>>() {
            @Override
            public void onResponse(Call<List<APIResponseModel>> call, Response<List<APIResponseModel>> response) {
                mRelativeLayout.setVisibility(View.GONE);
                AppUtils.showToastMessage(mActivity, mActivity.getString(R.string.post_deleted_success));
                alertDialogMoreOptions.dismiss();
            }

            @Override
            public void onFailure(Call<List<APIResponseModel>> call, Throwable t) {
                mRelativeLayout.setVisibility(View.GONE);
            }
        });

    }

    private HashMap<String, SimpleExoPlayer> playersList = new HashMap<>();
    private HashMap<String, SimpleExoPlayer> playingList = new HashMap<>();
    private int videoPosition;
    private OutDoorTagsPrefAdapter.MediaController mediaController = new OutDoorTagsPrefAdapter.MediaController() {
        @Override
        public void playVideo(PlayerView playerView, SimpleExoPlayer player, final String url) {
            playersList.put(url, player);
        }

        @Override
        public void releaseVideo(SimpleExoPlayer player) {
            //releasePlayer(player);
        }

        @Override
        public void videoPosition(int videoPosition2) {
            videoPosition = videoPosition2;
        }
    };


    OutDoorTagsPrefAdapter PostAdapter;
    private List<FollowModel> mFollowersList;

    private void getCategory(String category , int type , int FeatureType , int offset) {
        mRecyclerView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(true);
        Call<NewAPIResponseModel> hashTagCall = apiInterface.GetKnowledgePost(category , type , FeatureType , offset);
        hashTagCall.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                mRelativeLayout.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                if(response.body().isStatus()){
                    TagDataModelList = response.body().getOutDoorModel();
                    if (TagDataModelList != null) {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        Collections.reverse(TagDataModelList);
                        PostAdapter = new OutDoorTagsPrefAdapter(mActivity, mFollowersList, likeDatalist,commentDataList,shareDataList , TagDataModelList, selectItemInterface3, mediaController, 6);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
                        mRecyclerView.setAdapter(PostAdapter);
                        mRelativeLayout.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                mRelativeLayout.setVisibility(View.GONE);
            }
        });

    }


    @Override
    public void selectedPosition(int position) {
        Log.d(TAG, "selectedPosition: " + position);
    }

    @Override
    public void openAlert(FirstPreferenceDataModel dataModel) {

    }

    @Override
    public void reachedLastItem() {

    }

    protected ApiInterface apiInterface;

    private void setUpNetwork() {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }

        @Override
    public void onPrepareOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.searchview, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(PostAdapter != null){
                    PostAdapter.getFilter().filter(newText);
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
