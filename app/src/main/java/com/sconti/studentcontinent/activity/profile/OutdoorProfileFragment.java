package com.sconti.studentcontinent.activity.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sconti.studentcontinent.ContinentApplication;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.alltabs.SecondMainActivity;
import com.sconti.studentcontinent.activity.details.DetailsActivity;
import com.sconti.studentcontinent.activity.knowledge.adapter.LatestTrendsAdapter;
import com.sconti.studentcontinent.activity.knowledge.fragments.FirstPreferenceViewModel;
import com.sconti.studentcontinent.activity.othersprofile.OthersProfileActivity;
import com.sconti.studentcontinent.activity.outdoor.adapter.OutDoorTagsPrefAdapter;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorPostModel;
import com.sconti.studentcontinent.activity.post.PostActivity;
import com.sconti.studentcontinent.adapter.KnowledgeFirstPrefAdapter;
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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.provider.MediaStore.Video.Thumbnails.MINI_KIND;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.DATA_OF_COMMENT_AND_SLNO;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.DELETE_COMMENT_COUNT;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.EMAIL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.KNOWLEDGE_FIRST;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.KNOWLEDGE_POST_1;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.MOBILE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.PROFILE_URL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.UPDATE_KNOWLEDGE_POST_1;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_FEEDS;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_RESPONSES;

public class OutdoorProfileFragment extends BaseFragment implements KnowledgeFirstPrefAdapter.SelectItem {
    private static final String TAG = "FirstPreferenceFragment";
    private FirebaseCrashlytics crashlytics;
    private FirstPreferenceViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private KnowledgeFirstPrefAdapter firstPrefAdapter;
    private View mRootView;
    private EditText mEditTextTitle, mEditTextDescription;
    private String mImageURL, mVideoURL;
    private Button mButtonSubmit;
    private String userID;
    private ImageView mImageViewAttachment, mImageViewVideoAttachment;
    private DatabaseReference databaseReference;
    private List<FirstPreferenceDataModel> preferenceDataModelList;
    private UserDataModel mUserDataModel;
    private static String mSelectedSubject;
    private List<SubjectListModel> mSubjectList;
    private LinearLayout mLinearLayoutMessage;
    private View focusSelection;
    private RadioGroup radioGroup;
    private Button mButtonSelection;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int PICK_IMAGE_REQUEST_2 = 1111;
    private final int PICK_VIDEO_REQUEST_2 = 2222;
    private boolean isVideoSelected;
    private AlertDialog mAlertDialogFocusSelection;
    private RelativeLayout mRelativeLayout;
    private String keyword;
    private CardView mPostCardView;
    int PERMISSION_ALL = 1;
    int offset = 0;
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
    private SwipeRefreshLayout mSwipeRefreshLayout;
    List<OutDoorPostModel> TagDataModelList;
    List<OutDoorPostModel> TrendingKnowledgeModel;
    private AlertDialog alertDialogMoreOptions;
    private RecyclerView mLatestTrendsRecyclerView;
    private List<LikeDataModel> likeDatalist;

    private List<FollowModel> mFollowersList;

    private Parcelable recyclerViewState;
    ContinentTools likes;
    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayoutManager linearLayoutManager;
    private FirebaseAnalytics mFirebaseAnalytics;
    private boolean isTendingData;

    private List<ShareDataModel> shareDataList;
    private List<CommentModel> commentDataList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView= inflater.inflate(R.layout.fragment_outdoor_profile, container, false);
        mSelectedSubject="All";
        initViews();
        initListener();
        setUpNetwork();
        initData();
        TagDataModelList = new ArrayList<>();

        shimmerFrameLayout.startShimmer();

        isTendingData = getArguments().getBoolean("DATA_SENT_BY_TRENDING");
        if (isTendingData) {
            Toast.makeText(mActivity, " " + getArguments().getString("DATA") + " " + getArguments().getInt("TYPE"), Toast.LENGTH_SHORT).show();
            // getCategory(getArguments().getString("DATA"), 1, 1, 0);
            getNotificationCommentsDetails(getArguments().getString("DATA") + "", getArguments().getInt("TYPE"));
        } else {
            Bundle bundle = getArguments();

            if (bundle != null) {
                mSelectedSubject = bundle.getString("SELECTED");
                // mSelectedSubject = "All";  // TODO : udaya add
                Log.d(TAG, "onCreateView:mSelectedSubject "+mSelectedSubject);
            }

            if (mSelectedSubject != null) {
                focusSelection.setVisibility(View.GONE);
            }
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (ContinentTools.hasInternetConnection(getContext())) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        getCommentListByUserID();
                        getShareListByUserID();
                        getFollowersList();
                        getLikeListByUserID();
                        //   getCategory(mSelectedSubject, 1, 1, 0);
                   //     String userID = SharedPrefsHelper.getInstance().get(USER_ID);
                        GetAllUserPostByUserID(userID,5,0);
                        if (PostAdapter != null) {
                            PostAdapter.notifyDataSetChanged();
                        }

                    }
                }
            });
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ContinentTools.hasInternetConnection(getContext())) {
                    getCommentListByUserID();
                    getShareListByUserID();
                    getFollowersList();
                    getLikeListByUserID();
                //    String userID = SharedPrefsHelper.getInstance().get(USER_ID);
                    //  getCategory(mSelectedSubject, 1, 1, 0);
                    GetAllUserPostByUserID(userID,5,0);
                    if (PostAdapter != null) {
                        PostAdapter.notifyDataSetChanged();
                    }
                }


            }
        });


        //   getLatestTrendsData();  // TODO : udaya add
     //   ((AppCompatActivity) getActivity()).getSupportActionBar().show();


        return mRootView;
    }

    protected ApiInterface apiInterface;
    private FloatingActionButton floatingActionButton;

    private void setUpNetwork() {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }

    List<LatestTrendsModel> trendsModelList = new ArrayList<>();


    private void getLikeListByUserID() {
      //  String userID = SharedPrefsHelper.getInstance().get(USER_ID);
        Log.d(TAG, "IDD: " + userID);
        Call<NewAPIResponseModel> call = apiInterface.GetLikeListByUserID(userID, 1);
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
    private void getShareListByUserID()
    {
        String userID = SharedPrefsHelper.getInstance().get(USER_ID);
        Call<NewAPIResponseModel> call = apiInterface.GetShareListByUserID(userID, 5);
        call.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                shareDataList = response.body().getShareDataList();
                //   Log.d(TAG, "onResponse: getShareListByUserID"+shareDataList.get(0).getSl_no());
                if (shareDataList != null) {
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

    private void getCommentListByUserID()
    {
        String userID = SharedPrefsHelper.getInstance().get(USER_ID);
        Log.d(TAG, "My ID "+userID);
        Call<NewAPIResponseModel> call = apiInterface.GetCommentListByUserID(userID, 5);
        call.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                commentDataList = response.body().getCommentDataList();
                //  Log.d(TAG, "onResponse: getCommentListByUserID "+commentDataList.get(0).getSl_no());
                if (commentDataList != null) {
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

    private void getFollowersList() {
     //   String userID = SharedPrefsHelper.getInstance().get(USER_ID);
        Call<NewAPIResponseModel> listCall = apiInterface.GetFollowers(userID, "0");
        listCall.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                mFollowersList = response.body().getmFollowList();
                if (PostAdapter != null) {
                    PostAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                Log.d("TAG", "onFailure: ");
            }
        });
    }

    private void getLatestTrendsData() {
        trendsModelList = new ArrayList<>();
        Call<List<LatestTrendsModel>> listCall = apiInterface.GetTrends("1");
        listCall.enqueue(new Callback<List<LatestTrendsModel>>() {
            @Override
            public void onResponse(Call<List<LatestTrendsModel>> call, Response<List<LatestTrendsModel>> response) {
                mLatestTrendsRecyclerView.setVisibility(View.VISIBLE);
                trendsModelList = response.body();
                LatestTrendsAdapter firstPrefAdapter = new LatestTrendsAdapter(mActivity, response.body());
                mLatestTrendsRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
                mLatestTrendsRecyclerView.setAdapter(firstPrefAdapter);
                //  getCategory("All" , 1);
            }

            @Override
            public void onFailure(Call<List<LatestTrendsModel>> call, Throwable t) {

            }
        });

        mLatestTrendsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), mLatestTrendsRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "onItemClick: " + trendsModelList.get(position).getTitle());
                keyword = trendsModelList.get(position).getKeywords();
                //  getCategory(trendsModelList.get(position).getKeywords() , 2);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }

    OutDoorTagsPrefAdapter PostAdapter;

    private void GetAllUserPostByUserID(String userID, int type, int offset) {
        mRecyclerView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(true);
        Call<NewAPIResponseModel> hashTagCall = apiInterface.GetAllUserPostByUserID(userID, type, offset);
        hashTagCall.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                mSwipeRefreshLayout.setRefreshing(false);
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
                        // Collections.reverse(TagDataModelList);
                        PostAdapter = new OutDoorTagsPrefAdapter(mActivity, mFollowersList, likeDatalist,commentDataList,shareDataList, TagDataModelList, selectItemInterface2, mediaController, 1);
                        linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
                        mRecyclerView.setLayoutManager(linearLayoutManager);
                        //mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
                        mRecyclerView.setAdapter(PostAdapter);

                        PostAdapter.notifyDataSetChanged();
                        //  mRelativeLayout.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


    }


    private void updateCommentCount(String category, int type, int FeatureType) {

        Call<NewAPIResponseModel> hashTagCall = apiInterface.GetKnowledgePost(category, type, FeatureType, offset);
        mRecyclerView.invalidate();
        hashTagCall.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                if (response.body().isStatus()) {
                    List<OutDoorPostModel> TagDataModelList1 = response.body().getKnowledgeModel();
                    if (TagDataModelList1 != null) {
                        if (PostAdapter != null) {
                            Collections.reverse(TagDataModelList1);
                            PostAdapter = new OutDoorTagsPrefAdapter(mActivity, mFollowersList, likeDatalist, commentDataList,shareDataList,TagDataModelList1, selectItemInterface2, mediaController, 1);
                            PostAdapter.notifyDataSetChanged();
                        }
                    }
                }

            }


            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                // mProgressBar.setVisibility(View.GONE);
                //  mSwipeRefreshLayout.setRefreshing(false);
            }
        });



        /*if (response.body().isStatus()) {
            TagDataModelList = response.body().getKnowledgeModel();
            if (TagDataModelList != null) {
                PostAdapter.notifyDataSetChanged();
                CommentModel likeDataModel = new CommentModel();
                likeDataModel.setCommentedById(uid);
                likeDataModel.setNumComments(TagDataModelList21.get(i).getNumComments() + 1);

                if (TagDataModelList.get(i).getNumComments()>0) {

                    List<CommentModel> list = new ArrayList<>();
                    list.add(likeDataModel);
                    //TagDataModelList21.get(i).setNumComments();

                } else {

                    // TagDataModelList.get(i).getNumComments.add(likeDataModel);
                    PostAdapter.notifyDataSetChanged();

                }
                break;
            }
            // mRecyclerView.stopScroll();
            PostAdapter.notifyDataSetChanged();
            //  Toast.makeText(getContext(), "  PostAdapter.notifyDataSetChanged() 2", Toast.LENGTH_LONG).show();
        }*/
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
            Intent intent = new Intent(getContext(), DetailsActivity.class);
            intent.putExtra("DATA", TagDataModelList.get(position));
            intent.putExtra("TYPE", KNOWLEDGE_FIRST);
            intent.putExtra("KNOWLEDGE_COMMENT", true);
            intent.putExtra("SUBJECT", mSelectedSubject);
            mActivity.startActivity(intent);
        }

        @Override
        public void sharePost(OutDoorPostModel dataModel) {
            ShareDataModel shareDataModel = new ShareDataModel();
            shareDataModel.setSharedByName(SharedPrefsHelper.getInstance().get(NAME).toString());
            shareDataModel.setPostedById(dataModel.getPostedById());
            shareDataModel.setPostedByName(dataModel.getPostedByName());
            shareDataModel.setCategory(dataModel.getCategory());
            shareDataModel.setPostId(dataModel.getPostId());
            shareDataModel.setShareCount(dataModel.getNumShares() + "");
            shareDataModel.setShareByID(SharedPrefsHelper.getInstance().get(USER_ID).toString());
            updateShare(shareDataModel);
        }


        @Override
        public void openAlert(final OutDoorPostModel dataModel) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
            LayoutInflater inflater = mActivity.getLayoutInflater();
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext()
                    , R.style.BottomSheetDialogThem);
            View bottomSheetView = LayoutInflater.from(getContext())
                    .inflate(R.layout.outdoor_more_alert,
                            (CardView) mRootView.findViewById(R.id.BottomSheetDialogUserDost));
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
            }
            /*    else if(dataModel.getPo)*/

            else {
                delete.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
            }

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mActivity, PostActivity.class);
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

                    shareDataModel.setPostedById(dataModel.getPostedById());
                    shareDataModel.setPostedByName(dataModel.getPostedByName());
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
                                    deletePost(dataModel.getSl_no() + "");
                                    Toast.makeText(mActivity, getString(R.string.post_deleted_success), Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    for (int i = 0; i < TagDataModelList.size(); i++) {
                                        if (TagDataModelList.get(i).getPostId().equals(dataModel.getPostId())) {
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


                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                Intent intent = new Intent(mActivity, OthersProfileActivity.class);
                intent.putExtra("USER_ID", id);
                startActivity(intent);
            }
        }

        @Override
        public void updateLikeCount(final OutDoorPostModel dataModel, boolean isLiked) {
            ContinentTools likes = new ContinentTools();
            likes.commonUpdateLikeCount(dataModel, PostAdapter, apiInterface, TagDataModelList, 1, isLiked);
        }


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
                       //     String userID = SharedPrefsHelper.getInstance().get(USER_ID);
                            //  getCategory(mSelectedSubject, 1, 1, 0);
                            // GetAllUserPostByUserID(userID,1,0);
                            GetAllUserPostByUserID(userID,5, offset);
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

        }
    };

    private void updateShare(ShareDataModel shareDataModel) {
        mProgressBar.setVisibility(View.VISIBLE);

        // Toast.makeText(mActivity, " "+shareDataModel.getPostedById()+" "+shareDataModel.getPostedByName(), Toast.LENGTH_SHORT).show();
        Call<APIResponseModel> call = apiInterface.AddShare(shareDataModel, 5);
        call.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                mProgressBar.setVisibility(View.GONE);
                if (response.body().isStatus()) {
                    AppUtils.showToastMessage(mActivity, mActivity.getString(R.string.shared_success));
               //     String userID = SharedPrefsHelper.getInstance().get(USER_ID);
                    //  getCategory(mSelectedSubject, 1, 1, 0);
                    // GetAllUserPostByUserID(userID,1,0);
                    GetAllUserPostByUserID(userID,5, offset);
                } else {
                    AppUtils.showToastMessage(mActivity, mActivity.getString(R.string.some_thing_wrong));
                }
            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                AppUtils.showToastMessage(mActivity, mActivity.getString(R.string.some_thing_wrong));
            }
        });
    }

    private RelativeLayout mProgressBar;

    private void deletePost(String postId) {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<List<APIResponseModel>> hashTagCall = apiInterface.deletePost(postId, 1);
        hashTagCall.enqueue(new Callback<List<APIResponseModel>>() {
            @Override
            public void onResponse(Call<List<APIResponseModel>> call, Response<List<APIResponseModel>> response) {
                mProgressBar.setVisibility(View.GONE);
                AppUtils.showToastMessage(mActivity, mActivity.getString(R.string.post_deleted_success));
                alertDialogMoreOptions.dismiss();

            }

            @Override
            public void onFailure(Call<List<APIResponseModel>> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
            }
        });

    }

    private void initData() {
        mSelectedSubject = SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE, null);
        mUserDataModel = new UserDataModel();
        mUserDataModel.setEmail(SharedPrefsHelper.getInstance().get(EMAIL, ""));
        mUserDataModel.setMobile(SharedPrefsHelper.getInstance().get(MOBILE, ""));
        mUserDataModel.setName(SharedPrefsHelper.getInstance().get(NAME, ""));
    }

    private boolean playWhenReady = false;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private SimpleExoPlayer mPlayer;
    private YouTubePlayerView youTubePlayer;


    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (!isVisibleToUser)   // If we are becoming invisible, then...
            {
                Log.d(TAG, "setUserVisibleHint: not isVisibleToUser");
                //pause or stop video
                if (PostAdapter != null) {
                    PostAdapter.stopVideo();
                }

                if (mPlayer != null) {

                    mPlayer.setPlayWhenReady(false);


                }


            }

            if (isVisibleToUser) // If we are becoming visible, then...
            {
                Log.d(TAG, "setUserVisibleHint: VisibleToUser");
                //PostAdapter.stopVideo();
                if (PostAdapter != null) {
                    PostAdapter.stopVideo();
                }
                if (mPlayer != null) {
                    // Toast.makeText(mActivity, "mPlayer!=null", Toast.LENGTH_SHORT).show();
                    // mPlayer.release();

                    mPlayer.setPlayWhenReady(true);


                }


               /* //play your video
                if(mPlayer!=null){
                    mPlayer.release();
                   // youTubePlayer.play();
                }*/
            }
        }
    }

    private void releasePlayer(SimpleExoPlayer player) {
        if (player != null) {
            // player.removeListener(playbackStateListener);
            player.release();
            mPlayer = null;
        }
    }

    private void releaseYouTubePlayer(YouTubePlayer player) {
        if (player != null) {
            // player.removeListener(playbackStateListener);
            player.play();
            youTubePlayer = null;
        }
    }


    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mActivity, "exoplayer-codelab");
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
    }

    private KnowledgeFirstPrefAdapter.SelectItem selectItemInterface = new KnowledgeFirstPrefAdapter.SelectItem() {
        @Override
        public void selectedPosition(int position) {
            Intent intent = new Intent(getContext(), DetailsActivity.class);
            intent.putExtra("DATA", preferenceDataModelList.get(position));
            intent.putExtra("TYPE", KNOWLEDGE_FIRST);
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
            //getMoreData();
        }
    };

    private FirstPreferenceDataModel lastItemInQuery;
    /* private void getData() {
         mRelativeLayout.setVisibility(View.VISIBLE);
        // mLinearLayoutMessage.setVisibility(View.VISIBLE);
         databaseReference = ContinentApplication.getFireBaseRef();
         Query imagesQuery = databaseReference.child(KNOWLEDGE_FIRST).child(mSelectedSubject).orderByKey().limitToLast(AppUtils.Constants.QUERY_LIMIT);
         databaseReference = databaseReference.child(KNOWLEDGE_FIRST).child(mSelectedSubject);
         imagesQuery.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 preferenceDataModelList = new ArrayList<>();
                 for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                     preferenceDataModelList.add(snapshot.getValue(FirstPreferenceDataModel.class));
                 }
                 //getLikeDetails();

                 mRecyclerView.setVisibility(View.VISIBLE);
                 Collections.reverse(preferenceDataModelList);
                 if(preferenceDataModelList.size()==AppUtils.Constants.QUERY_LIMIT)
                 {
                     lastItemInQuery = preferenceDataModelList.remove(preferenceDataModelList.size()-1);
                 }

                 firstPrefAdapter = new KnowledgeFirstPrefAdapter(mActivity, preferenceDataModelList,selectItemInterface, mediaController, KNOWLEDGE_FIRST);
                 mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
                 mRecyclerView.setAdapter(firstPrefAdapter);
                 mRelativeLayout.setVisibility(View.GONE);

                 // mProgressBar.setVisibility(View.GONE);
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
                 mRelativeLayout.setVisibility(View.GONE);
             }
         });
     }
 */
    private boolean isLoadingMoreData;
    private List<FirstPreferenceDataModel> moreDataList;
   /* private void getMoreData() {
        if(!isLoadingMoreData && lastItemInQuery!=null) {
            mRelativeLayout.setVisibility(View.VISIBLE);
            // mLinearLayoutMessage.setVisibility(View.VISIBLE);
            Log.d(TAG, "getMoreData: ");
            databaseReference = ContinentApplication.getFireBaseRef();
            String mLastKey = lastItemInQuery.getKey();
            isLoadingMoreData = true;
            Query imagesQuery = databaseReference.child(KNOWLEDGE_FIRST).child(mSelectedSubject).orderByKey().endAt(mLastKey).limitToLast(AppUtils.Constants.QUERY_LIMIT);
            //  databaseReference = databaseReference.child(KNOWLEDGE_FIRST).child(mSelectedSubject);
            imagesQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //Collections.reverse(preferenceDataModelList);
                    moreDataList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        moreDataList.add(snapshot.getValue(FirstPreferenceDataModel.class));
                        //preferenceDataModelList.add(preferenceDataModelList.size(),snapshot.getValue(FirstPreferenceDataModel.class));
                    }
                    //getLikeDetails();

                    mRecyclerView.setVisibility(View.VISIBLE);
                    Collections.reverse(moreDataList);
                    if (moreDataList.size() == AppUtils.Constants.QUERY_LIMIT) {
                        lastItemInQuery = moreDataList.remove(moreDataList.size() - 1);
                    }
                    else
                    {
                        lastItemInQuery = null;
                    }
                    preferenceDataModelList.addAll(moreDataList);

                    firstPrefAdapter = new KnowledgeFirstPrefAdapter(mActivity, preferenceDataModelList, selectItemInterface, mediaController, KNOWLEDGE_FIRST);
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
*/


    private void getLikeDetails() {
        String uid = SharedPrefsHelper.getInstance().get(USER_ID, null);
        DatabaseReference databaseReference1 = ContinentApplication.getFireBaseRef().child(KNOWLEDGE_FIRST).child(USER_RESPONSES).child(USER_FEEDS).child(mSelectedSubject).child(uid);
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

    private void initListener() {

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d(TAG, "onScrollStateChanged: called.");

                    // There's outdooricon special case when the end of the list has been reached.
                    // Need to handle that with this bit of logic
                    if (!recyclerView.canScrollVertically(1)) {
                        //playVideoAtThis();
                        playVideoAtThis();
                        int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();


                        int last = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                        int page = (last + 1);
                        //Toast.makeText(mActivity, " "+page, Toast.LENGTH_SHORT).show();

                        if (page >= TagDataModelList.size()) {
                            offset = offset + 20;
                      //      String userID = SharedPrefsHelper.getInstance().get(USER_ID);
                            //  getCategory(mSelectedSubject, 1, 1, 0);
                            // GetAllUserPostByUserID(userID,1,0);
                            getCategoryPage(userID, 5,  offset);
                            //Toast.makeText(mActivity, " Index"+index, Toast.LENGTH_SHORT).show();


                            // getCategory(mSelectedSubject, 1, 1,index);
                            //
                        }
                        /*if( (mPlayer.))
                        {

                        }
                            else
                        {
                            if (mPlayer != null) {

                                mPlayer.setPlayWhenReady(false);

                            }
                        }*/
                        // Toast.makeText(mActivity, " Position: "+firstVisiblePosition, Toast.LENGTH_SHORT).show();


                    } else {
                        playVideoAtThis();
                        // int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();
                        //Toast.makeText(mActivity, " Position: 1"+firstVisiblePosition, Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


/*        ImageView logoIcon = ((SecondMainActivity) getActivity()).mToolbar.findViewById(R.id.ReloadData);
        logoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = SharedPrefsHelper.getInstance().get(USER_ID);
                //  getCategory(mSelectedSubject, 1, 1, 0);
                // GetAllUserPostByUserID(userID,1,0);
                GetAllUserPostByUserID(userID,5, offset);
            }
        });*/
        /*
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String desc = mEditTextDescription.getText().toString().trim();
                if(desc!=null && desc.length()>2)
                {
                    submitData();
                }
                else{
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
                        Intent intent = new Intent(getContext(), DetailsActivity.class);
                        intent.putExtra("DATA", preferenceDataModelList.get(position));
                       // startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );*/
    }


    private void getCategoryPage(String userid, int type,  int offset) {
        Call<NewAPIResponseModel> hashTagCall = apiInterface.GetAllUserPostByUserID(userid, type, offset);
        hashTagCall.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                // response.body().getKnowledgeModel()
               /* OutDoorPostModel outDoorPostModel=new OutDoorPostModel();
                outDoorPostModel*/

                TagDataModelList.addAll(response.body().getKnowledgeModel());
                PostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {

            }
        });
    }

    @SuppressLint("CutPasteId")
    private void initViews() {

        shimmerFrameLayout = (ShimmerFrameLayout) mRootView.findViewById(R.id.shimmerLayout);
        mLatestTrendsRecyclerView = mRootView.findViewById(R.id.recycler_latest);
        focusSelection = mRootView.findViewById(R.id.focus_select_view);
        mLinearLayoutMessage = mRootView.findViewById(R.id.linearlayoutEditMessage);
        mEditTextTitle = mRootView.findViewById(R.id.editTextTitle);
        mEditTextDescription = mRootView.findViewById(R.id.editTextDetails);
        mButtonSubmit = mRootView.findViewById(R.id.submitButton);
        mImageViewAttachment = mRootView.findViewById(R.id.imageViewAttach);
        mImageViewVideoAttachment = mRootView.findViewById(R.id.videoAttach);
        mRecyclerView = mRootView.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = mRootView.findViewById(R.id.swipeLayout);

        // mRelativeLayout = mRootView.findViewById(R.id.progress_bar);
        floatingActionButton = mRootView.findViewById(R.id.fabAddPost);

        Bundle bundle=getArguments();
        userID = bundle.getString("USER_ID");

    /*   FirebaseCrashlytics.getInstance().recordException(new Exception("App Name : My first Android non-fatal error"));
       FirebaseCrashlytics.getInstance().log("hi");*/

        // FirebaseCrash.report(new Exception("App Name : My first Android non-fatal error"));


        floatingActionButton.setColorFilter(Color.WHITE);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingActionButton.setClickable(false);
                mSelectedSubject = SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE, null);
                Intent intent = new Intent(mActivity, PostActivity.class);
                intent.putExtra("TYPE", KNOWLEDGE_FIRST);
                intent.putExtra("FAV", mSelectedSubject);
                intent.putExtra("KEYWORD", keyword);
                startActivity(intent);

            }
        });
        mProgressBar = mRootView.findViewById(R.id.progress_bar);


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
        databaseReference = ContinentApplication.getFireBaseRef().child(KNOWLEDGE_FIRST).child(mSelectedSubject);
        String key = databaseReference.push().getKey();
        firstPreferenceDataModel.setId(key);
        firstPreferenceDataModel.setKey(key);
        firstPreferenceDataModel.setPosterUrl(prfurl);
        databaseReference.child(key).setValue(firstPreferenceDataModel);
        mEditTextDescription.setText("");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FirstPreferenceViewModel.class);


    }


    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri filePath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null)
            filePath = data.getData();
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
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

    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
            final AlertDialog testDialog = alert.create();

            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                   /* testDialog.hide();
                    testDialog.dismiss();
                    testDialog.cancel();*/

                } else if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED) {
                    testDialog.setTitle("Info");
                    testDialog.setMessage("Please check internet connection...!");
                    testDialog.show();
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getContext().registerReceiver(networkReceiver, intentFilter);

        try {


        } catch (Exception e) {
            Toast.makeText(mActivity, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (mPlayer != null) {
            mPlayer.stop();
        }
        //   getLatestTrendsData();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (networkReceiver != null) {
            getContext().unregisterReceiver(networkReceiver);
        }
        if (mPlayer != null) {
            releasePlayer(mPlayer);
        }
        if (PostAdapter != null) {

            updateListView();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        floatingActionButton.setClickable(true);
        TagDataModelList.clear();
        offset = 0;
        getLikeListByUserID();
        Boolean KNOWLEDGE_POST_1id = false;

        SharedPreferences prefs2 = mActivity.getSharedPreferences(UPDATE_KNOWLEDGE_POST_1, MODE_PRIVATE);
        String edittext = prefs2.getString("edit", null);//"No name defined" is the default value.
        long postids = prefs2.getLong("postid", 0);

        // Toast.makeText(mActivity, numComments+" "+id, Toast.LENGTH_SHORT).show();

        if (edittext != null && postids > 0) {

            SharedPreferences.Editor editor12 = mActivity.getSharedPreferences(UPDATE_KNOWLEDGE_POST_1, MODE_PRIVATE).edit();
            editor12.putString("edit", null);
            editor12.putLong("id", 0);
            editor12.apply();

            for (int i = 0; i < TagDataModelList.size(); i++) {
                if (TagDataModelList.get(i).getSl_no() == postids) {
                    TagDataModelList.get(i).setDescription(edittext);
                    PostAdapter.notifyDataSetChanged();
                    break;
                }
            }

            SharedPreferences prefs145 = mActivity.getSharedPreferences(UPDATE_KNOWLEDGE_POST_1, MODE_PRIVATE);

            String edittext145 = prefs145.getString("edit", null);//"No name defined" is the default value.
            long postids145 = prefs145.getLong("postid", 0);
        }

        //  SharedPreferences sp = mActivity.getSharedPreferences(KNOWLEDGE_POST_1, 0);

        SharedPreferences sp = mActivity.getSharedPreferences(KNOWLEDGE_POST_1, MODE_PRIVATE);
        boolean cb1 = sp.getBoolean("myBool", false);

        if (cb1) {
        //    String userID = SharedPrefsHelper.getInstance().get(USER_ID);
            //  getCategory(mSelectedSubject, 1, 1, 0);
            // GetAllUserPostByUserID(userID,1,0);
            GetAllUserPostByUserID(userID,5, offset);
        }
        SharedPreferences.Editor editor123 = mActivity.getSharedPreferences(KNOWLEDGE_POST_1, MODE_PRIVATE).edit();
        editor123.putBoolean("myBool", false);
        editor123.apply();


        SharedPreferences prefs = mActivity.getSharedPreferences(DATA_OF_COMMENT_AND_SLNO, MODE_PRIVATE);
        long numComments = prefs.getLong("numComments", 0);//"No name defined" is the default value.
        long id = prefs.getLong("id", 0);

        if (numComments > 0 && id > 0) {

            SharedPreferences.Editor editor = mActivity.getSharedPreferences(DATA_OF_COMMENT_AND_SLNO, MODE_PRIVATE).edit();
            editor.putLong("numComments", 0);
            editor.putLong("id", 0);
            editor.apply();

            for (int i = 0; i < TagDataModelList.size(); i++) {
                if (TagDataModelList.get(i).getSl_no() == id) {
                    TagDataModelList.get(i).setNumComments(numComments);
                    PostAdapter.notifyDataSetChanged();
                    break;


                }
            }

            SharedPreferences prefs1 = mActivity.getSharedPreferences(DATA_OF_COMMENT_AND_SLNO, MODE_PRIVATE);
            long numComments1 = prefs1.getLong("numComments", 0);//"No name defined" is the default value.
            long id1 = prefs1.getLong("id", 0);

        }


        SharedPreferences prefsdelete = mActivity.getSharedPreferences(DELETE_COMMENT_COUNT, MODE_PRIVATE);
        long numCommentsdelete = prefsdelete.getLong("numComments", 0);//"No name defined" is the default value.
        long iddelete = prefsdelete.getLong("id", 0);

        if (numCommentsdelete >0 && iddelete >0) {
            SharedPreferences.Editor editor = mActivity.getSharedPreferences(DELETE_COMMENT_COUNT, MODE_PRIVATE).edit();
            editor.putLong("numComments", 0);
            editor.putLong("id", 0);
            editor.apply();
            for (int i = 0; i < TagDataModelList.size(); i++) {
                if (TagDataModelList.get(i).getSl_no() == iddelete) {
                    TagDataModelList.get(i).setNumComments((TagDataModelList.get(i).getNumComments()-numCommentsdelete));
                    PostAdapter.notifyDataSetChanged();
                    long totalComments=TagDataModelList.get(i).getNumComments();
                    Log.d(TAG, "final count "+"Toast "+TagDataModelList.get(i).getNumComments()+" delete "+numCommentsdelete+" "+(totalComments-numCommentsdelete));
                    Toast.makeText(mActivity, "Toast "+TagDataModelList.get(i).getNumComments()+" delete "+numCommentsdelete+" "+(totalComments-numCommentsdelete), Toast.LENGTH_SHORT).show();
                    break;
                }
            }

            SharedPreferences prefs1 = mActivity.getSharedPreferences(DELETE_COMMENT_COUNT, MODE_PRIVATE);
            long numComments1 = prefs1.getLong("numComments", 0);//"No name defined" is the default value.
            long id1 = prefs1.getLong("id", 0);

        }


    }

    private void post() {

    }


    private void uploadImage(Uri filePath) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images").child(KNOWLEDGE_FIRST).child(mSelectedSubject).child(UUID.randomUUID().toString());
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

    private File mediaFile;


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

    private int videoSurfaceDefaultHeight;
    private int screenDefaultHeight;
    private int playPosition = -1;
    private View viewHolderParent;

    public void playVideoAtThis() {
        //releasePlayer(mPlayer);
        //
        // mPlayer.setVolume(0);

        //  releaseYouTubePlayer(youTubePlayer);

        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        screenDefaultHeight = point.y;
        videoSurfaceDefaultHeight = 500;//400
        int targetPosition;

        int startPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        int endPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastVisibleItemPosition();

        // if there is more than 2 list-items on the screen, set the difference to be 1
        if (endPosition - startPosition > 1) {
            endPosition = startPosition + 1;
        }

        // something is wrong. return.
        if (startPosition < 0 || endPosition < 0) {
            return;
        }

        // if there is more than 1 list-item on the screen
        if (startPosition != endPosition) {
            int startPositionVideoHeight = getVisibleVideoSurfaceHeight(startPosition);
            int endPositionVideoHeight = getVisibleVideoSurfaceHeight(endPosition);
            targetPosition = startPositionVideoHeight > endPositionVideoHeight ? startPosition : endPosition;
        } else {
            targetPosition = startPosition;
        }

        Log.d(TAG, "playVideo: target position: " + targetPosition);

        // video is already playing so return
        if (targetPosition == playPosition) {
            return;
        }
        int currentPosition = targetPosition - ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();

        View child = mRecyclerView.getChildAt(currentPosition);
        if (child == null) {
            return;
        }

        OutDoorTagsPrefAdapter.MyViewHolder holder = (OutDoorTagsPrefAdapter.MyViewHolder) child.getTag();
        if (holder == null) {
            playPosition = -1;
            return;
        }
        viewHolderParent = holder.parent;
        mPlayer = ExoPlayerFactory.newSimpleInstance(getContext());
        holder.playerView.setPlayer(mPlayer);
        final Uri uri = Uri.parse(TagDataModelList.get(targetPosition).getMediaURL());
        MediaSource mediaSource = buildMediaSource(uri);
        mPlayer.setPlayWhenReady(playWhenReady);
        mPlayer.seekTo(currentWindow, playbackPosition);
        mPlayer.prepare(mediaSource, false, false);
       /* mPlayer.setPlayWhenReady(true);
        mPlayer.setVolume(0f);*/
        if (uri != null) {
            mPlayer.setPlayWhenReady(true);
            mPlayer.setVolume(0f);
            holder.playerView.setVisibility(View.VISIBLE);
        } else {
            holder.playerView.setVisibility(View.GONE);
        }


        holder.playerView.setDefaultArtwork(getThumblineImage(TagDataModelList.get(targetPosition).getMediaURL()));

        if (uri != null) {
            holder.playerView.setVisibility(View.VISIBLE);


        } else
            holder.playerView.setVisibility(View.GONE);

    }

    private int getVisibleVideoSurfaceHeight(int playPosition) {
        int at = playPosition - ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        Log.d(TAG, "getVisibleVideoSurfaceHeight: at: " + at);

        View child = mRecyclerView.getChildAt(at);
        if (child == null) {
            return 0;
        }

        int[] location = new int[2];
        child.getLocationInWindow(location);
        Log.d(TAG, "getVisibleVideoSurfaceHeight: " + location[0] + " " + location[1]);

        if (location[1] < 0) {
            return location[1] + videoSurfaceDefaultHeight;
        } else {
            return screenDefaultHeight - location[1];
        }
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
                if (PostAdapter != null) {
                    PostAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.notification_Fragment:
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .add(android.R.id.content, new NotificationFragment()).addToBackStack(null).commit();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void updateListView() {
        PostAdapter.notifyDataSetChanged();
    }

    public static Bitmap getThumblineImage(String videoPath) {
        return ThumbnailUtils.createVideoThumbnail(videoPath, MINI_KIND);
    }

    private void getNotificationCommentsDetails(String postId, int type) {
        Call<NewAPIResponseModel> call = null;
        call = apiInterface.GetPostDetailsByPostID(postId, type);
        call.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                shimmerFrameLayout.startShimmer();
                if (response.body().getKnowledgeModel() != null) {
                    TrendingKnowledgeModel = response.body().getKnowledgeModel();
                    Toast.makeText(getContext(), " "+TrendingKnowledgeModel.size(), Toast.LENGTH_SHORT).show();
                    if (TagDataModelList != null) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        // Collections.reverse(TagDataModelList);
                        PostAdapter = new OutDoorTagsPrefAdapter(mActivity, mFollowersList , likeDatalist ,commentDataList,shareDataList, TrendingKnowledgeModel, selectItemInterface2, mediaController, 1);
                        linearLayoutManager=new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
                        mRecyclerView.setLayoutManager(linearLayoutManager);
                        //mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
                        mRecyclerView.setAdapter(PostAdapter);
                        PostAdapter.notifyDataSetChanged();
                        //  mRelativeLayout.setVisibility(View.GONE);
                    }

                   /*// SetCommentData(outDoorPostModel);
                    getOutdoorComments(outDoorPostModel.getSl_no() + "");*/
                }

            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "+t.getLocalizedMessage()", Toast.LENGTH_SHORT).show();
            }
        });


    }
}