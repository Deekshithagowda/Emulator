package com.sconti.studentcontinent.activity.details;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.ui.GPHContentType;
import com.giphy.sdk.ui.Giphy;
import com.giphy.sdk.ui.GiphyFrescoHandler;
import com.giphy.sdk.ui.views.GiphyDialogFragment;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.sconti.studentcontinent.AddUser;
import com.sconti.studentcontinent.ContinentApplication;
import com.sconti.studentcontinent.CustomListAdapter;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.commentdetails.CommentDetailsActivity;
import com.sconti.studentcontinent.activity.commentdetails.PostSubCommentActivity;
import com.sconti.studentcontinent.activity.commentdetails.adapter.CommentsListAdapter;
import com.sconti.studentcontinent.activity.knowledge.fragments.FirstPreferenceFragment;
import com.sconti.studentcontinent.activity.othersprofile.OthersProfileActivity;
import com.sconti.studentcontinent.activity.outdoor.adapter.OutDoorTagsPrefAdapter;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorPostModel;
import com.sconti.studentcontinent.activity.post.PostActivity;
import com.sconti.studentcontinent.activity.profile.ProfileFragment;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.model.APIResponseModel;
import com.sconti.studentcontinent.model.CommentModel;
import com.sconti.studentcontinent.model.FirstPreferenceDataModel;
import com.sconti.studentcontinent.model.LikeDataModel;
import com.sconti.studentcontinent.model.LikeModel;
import com.sconti.studentcontinent.model.NewAPIResponseModel;
import com.sconti.studentcontinent.model.NotificationDataModel;
import com.sconti.studentcontinent.model.UserDetails;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;
import com.sconti.studentcontinent.utils.AppUtils;
import com.sconti.studentcontinent.utils.RecyclerItemClickListener;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;
import com.sconti.studentcontinent.utils.tools.ContinentTools;
import com.sconti.studentcontinent.utils.tools.CustomPlayerUiController;
import com.sconti.studentcontinent.utils.tools.PopUpClass;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.COMMENT_NODE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.DATA_OF_COMMENT_AND_SLNO;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.DATA_OF_COMMENT_AND_SLNO2;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.DATA_OF_COMMENT_AND_SLNO3;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.DATA_OF_COMMENT_AND_SLNO4;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.DATA_OF_COMMENT_AND_SLNO5;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.DELETE_COMMENT_COUNT;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.KNOWLEDGE_FIRST;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.LIKE_NODE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.MOBILE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.PROFILE_URL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_COMMENTS;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

public class DetailsActivity extends BaseActivity {
    private static final String TAG = "DetailsActivity";
    private FirstPreferenceDataModel preferenceDataModel;
    private ActionBar mActionBar;
    private TextView mTextViewDescription;
    private TextView mImageViewSend;
    private String userId;
    private int totalcommentcount;
    ////
    private EditText mEditTextComment;
    private LinearLayout mCancelButton;
    private int intCount = 0, initialStringLength = 0;
    private String strText = "";
    private ConstraintLayout contentView;
    private LinearLayout mLinearlayoutLikesCommentShare;
    private TextView mTextViewCommentCount, mTextViewShareCount, mTextViewLikeCount, mUserNameComment;
    private DatabaseReference databaseReference;
    private String type, mSelectedSubject ;
    private ImageView mImageViewComment, mImageViewPost;
    private LinearLayout mLinearLayoutComments;
    private SimpleExoPlayer player;
    private PlayerView playerView;
    private Button mCnacleButton;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private ImageView mImageViewPostDelete;
    private LinearLayout mLinearLayoutDeletePost;
    private boolean isOutdoorComment;
    private OutDoorPostModel outDoorPostModel;
    private NotificationDataModel notificationDataModel;
    //  private String TrendingCategory;
    private ProgressBar mProgressBar;
    private boolean isKnowledgeComment, isKnowledgeGeneralComment, isAspirationComment, isAspirationGeneralComment;
    long add = 0;
    private CommentsListAdapter mCommentsListAdapter;
    private List<CommentModel> commentModelList;
    // private List<UserDetails> usertable;
    private RecyclerView recyclerViewCommentor;
    boolean isAdapterSet = true;
    private boolean isNotificationData;
    int cid;
    private List<LikeDataModel> likeDatalist;
    CustomListAdapter adapter;
    int deleteCommentCount = 0;

    List<UserDetails> userDetails = new ArrayList<>();
    Set<UserDetails> newuserDetails = new LinkedHashSet<>();
    ImageView gifImageButton;
    public CircleImageView avatar;
    public GiphyDialogFragment giphyDialogFragment;
    public GiphyFrescoHandler frescoHandler;
    // private SwipeRefreshLayout mSwipeRefreshLayout;
    CardView mCardViewContent;
    ImageView mCardViewContentImageView;
    PlayerView mCardViewContentVideoView;
    SimpleExoPlayer simpleExoPlayer;
    long mNotificationSlNo;
    TextView titleView;
    int Postdetailsname , Comments;
    YouTubePlayerView youTubePlayerView;


    @Override
    protected int getContentView() {
        return R.layout.activity_details;
    }

    @Override
    protected void initView() {
        recyclerViewCommentor = findViewById(R.id.recycler_viewComments);
        // mSwipeRefreshLayout=findViewById(R.id.mSwipeRefreshComment);
        contentView = findViewById(R.id.rootView);
        mActionBar = getSupportActionBar();
        mProgressBar = findViewById(R.id.progressbar);
        avatar = findViewById(R.id.imageViewProfile);
        mTextViewDescription = findViewById(R.id.textViewDescription);
        mImageViewPost = findViewById(R.id.imageViewPost);
        mImageViewSend = findViewById(R.id.imageViewSend);
        mEditTextComment = findViewById(R.id.editTextComment);
        mLinearlayoutLikesCommentShare = findViewById(R.id.linearlayoutLikesCommentShare);
        mTextViewCommentCount = findViewById(R.id.textViewCommentCount);
        mTextViewLikeCount = findViewById(R.id.textViewLikeCount);
        mTextViewShareCount = findViewById(R.id.textViewShareCount);
        mLinearLayoutComments = findViewById(R.id.linearlayoutComment);
        playerView = findViewById(R.id.video_view);
        mImageViewPostDelete = findViewById(R.id.imageViewPostDelete);
        mLinearLayoutDeletePost = findViewById(R.id.linearlayoutDelete);
        mUserNameComment = findViewById(R.id.UserNameComment);
        mCancelButton = findViewById(R.id.cancel_button);
        gifImageButton = findViewById(R.id.gif_image);
        mCnacleButton = findViewById(R.id.CnacleButton);
        //cardview
        mCardViewContent = findViewById(R.id.cardViewContent);
        mCardViewContentImageView = findViewById(R.id.cardViewContentImageView);
        mCardViewContentVideoView = findViewById(R.id.cardViewContentVideoView);
        titleView = findViewById(R.id.title);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        userId = SharedPrefsHelper.getInstance().get(USER_ID);

        recyclerViewCommentor.setLayoutManager(new LinearLayoutManager(DetailsActivity.this, LinearLayoutManager.VERTICAL, false));
        adapter = new CustomListAdapter(DetailsActivity.this,
                R.layout.user_info, userDetails);//userDetails= AddUser.getallUser()
//        mEditTextComment.setAdapter(adapter);
//        mEditTextComment.setThreshold(1);

       /* mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(ContinentTools.hasInternetConnection(getApplicationContext()))
                {
                    getOutdoorComments(outDoorPostModel.getSl_no() + "");
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (ContinentTools.hasInternetConnection(getApplicationContext())) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    getOutdoorComments(outDoorPostModel.getSl_no() + "");
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }

                }
            }
        });*/

        frescoHandler = new GiphyFrescoHandler() {
            @Override
            public void handle(OkHttpClient.@NotNull Builder builder) {

            }

            @Override
            public void handle(ImagePipelineConfig.@NotNull Builder builder) {

            }
        };
        Giphy.INSTANCE.configure(this, AppUtils.Constants.GIF_KEY, true, frescoHandler);

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        setUpNetwork();
        //getOutdoorComments(outDoorPostModel.getSl_no() + "");

        //getOutdoorComments(outDoorPostModel.getSl_no() + "");

        isNotificationData = getIntent().getExtras().getBoolean(AppUtils.Constants.NOTIFICATION_DETAILS, false);
        Comments =  getIntent().getExtras().getInt("Comments");

        if(Comments == 1){
            titleView.setText("Comments");
        }else {
            titleView.setText("Post details");
        }



        //  Glide.with(context).load(outDoorPostModel.getPrfurl()).placeholder(R.drawable.ic_user_profile).into(avatar);

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (outDoorPostModel.getPostedById().equals(userId)) {
                    intent = new Intent(context, ProfileFragment.class);
                } else {
                    intent = new Intent(context, OthersProfileActivity.class);
                }
                intent.putExtra("USER_ID", outDoorPostModel.getPostedById());
                startActivity(intent);

            }
        });
        mUserNameComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (outDoorPostModel.getPostedById().equals(userId)) {
                    intent = new Intent(context, ProfileFragment.class);
                } else {
                    intent = new Intent(context, OthersProfileActivity.class);
                }
                intent.putExtra("USER_ID", outDoorPostModel.getPostedById());
                startActivity(intent);
            }
        });

        mCnacleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (isNotificationData) {
            notificationDataModel = getIntent().getParcelableExtra(AppUtils.Constants.NOTIFICATION_DATA);
            //  TrendingCategory=getIntent().getStringExtra(AppUtils.Constants.TRENDING_CATEGORY);
            cid = notificationDataModel.getType();
            isOutdoorComment = getIntent().getExtras().getBoolean("OUTDOOR_COMMENT", false);
            isKnowledgeComment = getIntent().getExtras().getBoolean("KNOWLEDGE_COMMENT", false);
            isKnowledgeGeneralComment = getIntent().getExtras().getBoolean("KNOWLEDGE_GENERAL_COMMENT", false);
            isAspirationComment = getIntent().getExtras().getBoolean("ASPIRATION_COMMENT", false);
            isAspirationGeneralComment = getIntent().getExtras().getBoolean("ASPIRATION_GENERAL_COMMENT", false);
            getNotificationCommentsDetails(notificationDataModel.getPostID() + "", notificationDataModel.getType());
        } else {
            isOutdoorComment = getIntent().getExtras().getBoolean("OUTDOOR_COMMENT", false);
            isKnowledgeComment = getIntent().getExtras().getBoolean("KNOWLEDGE_COMMENT", false);
            isKnowledgeGeneralComment = getIntent().getExtras().getBoolean("KNOWLEDGE_GENERAL_COMMENT", false);
            isAspirationComment = getIntent().getExtras().getBoolean("ASPIRATION_COMMENT", false);
            isAspirationGeneralComment = getIntent().getExtras().getBoolean("ASPIRATION_GENERAL_COMMENT", false);
            // Toast.makeText(this, "Recyclerview", Toast.LENGTH_SHORT).show();

            if (isOutdoorComment) {
                outDoorPostModel = getIntent().getParcelableExtra("DATA");

            } else if (isAspirationComment) {
                outDoorPostModel = getIntent().getParcelableExtra("DATA");

            } else if (isAspirationGeneralComment) {
                outDoorPostModel = getIntent().getParcelableExtra("DATA");

            } else if (isKnowledgeComment) {
                outDoorPostModel = getIntent().getParcelableExtra("DATA");

            } else if (isKnowledgeGeneralComment) {
                outDoorPostModel = getIntent().getParcelableExtra("DATA");

            } else {
                //preferenceDataModel = getIntent().getParcelableExtra("DATA");
                outDoorPostModel = getIntent().getParcelableExtra("DATA");

            }


            mSelectedSubject = getIntent().getStringExtra("SUBJECT");
            type = getIntent().getStringExtra("TYPE");
            if (outDoorPostModel.getMediaType() == 1 && !TextUtils.isEmpty(outDoorPostModel.getMediaURL())) {
                // Toast.makeText(this, "Image", Toast.LENGTH_SHORT).show();
                mCardViewContent.setVisibility(VISIBLE);
                mCardViewContentImageView.setVisibility(VISIBLE);
                mCardViewContentVideoView.setVisibility(GONE);
                youTubePlayerView.setVisibility(GONE);
                Glide.with(DetailsActivity.this).load(outDoorPostModel.getMediaURL()).into(mCardViewContentImageView);
            } else if (outDoorPostModel.getMediaType() == 3 && !TextUtils.isEmpty(outDoorPostModel.getMediaURL())) {
                mCardViewContent.setVisibility(VISIBLE);
                mCardViewContentVideoView.setVisibility(VISIBLE);
                mCardViewContentImageView.setVisibility(GONE);
                youTubePlayerView.setVisibility(GONE);
                Uri uri = Uri.parse(outDoorPostModel.getMediaURL());
                try {
                    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                    simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
                    DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");

                    ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                    MediaSource mediaSource = new ExtractorMediaSource(uri, dataSourceFactory,
                            extractorsFactory, null, null);
                    mCardViewContentVideoView.setPlayer(simpleExoPlayer);
                    simpleExoPlayer.prepare(mediaSource);
                    simpleExoPlayer.setPlayWhenReady(true);
                    simpleExoPlayer.setVolume(0f);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.d(TAG, "initData:youtube " + outDoorPostModel.getDescription());
                String id = ContinentTools.extractYTId(outDoorPostModel.getDescription());
                if (ContinentTools.is_word(outDoorPostModel.getDescription().trim()) && !TextUtils.isEmpty(id)) {
                    mCardViewContent.setVisibility(VISIBLE);
                    youTubePlayerView.setVisibility(VISIBLE);
                    youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            CustomPlayerUiController customPlayerUiController = new CustomPlayerUiController(DetailsActivity.this, youTubePlayer, youTubePlayerView);
                            youTubePlayer.addListener(customPlayerUiController);
                            youTubePlayerView.addFullScreenListener(customPlayerUiController);
                            youTubePlayer.loadVideo(id, 0f);
                            youTubePlayer.pause();
                        }
                    });

                } else {
                    mCardViewContent.setVisibility(GONE);
                    mCardViewContentVideoView.setVisibility(GONE);
                    mCardViewContentImageView.setVisibility(GONE);
                    youTubePlayerView.setVisibility(GONE);
                }
            }
            getOutdoorComments(outDoorPostModel.getSl_no() + "");
            // GetCommentLikeListByUserID(Integer.parseInt(type));
            mCardViewContentImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopUpClass popUpClass = new PopUpClass();
                    popUpClass.showPopupWindow(view, "image", outDoorPostModel.getMediaURL());
                }
            });
            mCardViewContentVideoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopUpClass popUpClass = new PopUpClass();
                    popUpClass.showPopupWindow(view, "video", outDoorPostModel.getMediaURL());
                }
            });


            if (mActionBar != null) {
                if (preferenceDataModel != null) {
                    // TODO :  mActionBar.setTitle(outDoorPostModel.getDescription()); udaya change to   mActionBar.setTitle("Comments");

                    mActionBar.setTitle("Comments");

                } else {
                    // TODO :  mActionBar.setTitle(outDoorPostModel.getDescription()); udaya change to   mActionBar.setTitle("Comments");

                    mActionBar.setTitle("Comments");

                }
                mActionBar.setDisplayShowHomeEnabled(true);
                mActionBar.setDisplayHomeAsUpEnabled(true);
            }
            if (preferenceDataModel != null) {
                if (preferenceDataModel.getDescription() != null && preferenceDataModel.getDescription().trim().length() > 0) {
                    mTextViewDescription.setText(preferenceDataModel.getDescription());
                }
                if (preferenceDataModel.getImageURL() != null) {
                    // mImageViewPost.setVisibility(View.VISIBLE);
                    //  loadImage(preferenceDataModel.getImageURL(), mImageViewPost);
                } else if (preferenceDataModel.getVideoURL() != null) {
                    //  playerView.setVisibility(View.VISIBLE);
                    mImageViewPost.setVisibility(View.GONE);
                    // initializePlayer(playerView, player, preferenceDataModel.getVideoURL());
                }

                if (preferenceDataModel != null) {
                    if (preferenceDataModel.getDescription() != null && preferenceDataModel.getDescription().trim().length() > 0) { // TODO : udaya add
                        mEditTextComment.setText(preferenceDataModel.getDescription());
                    }
                    if (preferenceDataModel.getImageURL() != null) {
                        // mImageViewPost.setVisibility(View.VISIBLE);
                        //  loadImage(preferenceDataModel.getImageURL(), mImageViewPost);
                    } else if (preferenceDataModel.getVideoURL() != null) {
                        //  playerView.setVisibility(View.VISIBLE);
                        mImageViewPost.setVisibility(View.GONE);
                        // initializePlayer(playerView, player, preferenceDataModel.getVideoURL());
                    }
                }

                if (preferenceDataModel.getNumShares() != 0)
                    mTextViewShareCount.setText("" + preferenceDataModel.getNumShares());
                if (preferenceDataModel.getNumLikes() != 0) {
                    mTextViewLikeCount.setText("" + preferenceDataModel.getNumLikes());
                }
                if (preferenceDataModel.getNumComments() != 0) {
                    mTextViewCommentCount.setText("" + preferenceDataModel.getNumComments());
                }
                String uid = SharedPrefsHelper.getInstance().get(USER_ID);
                if (preferenceDataModel.getPostedBy().equalsIgnoreCase(uid)) {
                    mLinearLayoutDeletePost.setVisibility(View.VISIBLE);
                }
                commentModel.setDescription(preferenceDataModel.getDescription());
                commentModel.setId(preferenceDataModel.getId());
                commentModel.setKey(preferenceDataModel.getKey());
                commentModel.setImageURL(preferenceDataModel.getImageURL());
                commentModel.setVideoURL(preferenceDataModel.getVideoURL());
                commentModel.setNumComments(preferenceDataModel.getNumComments());

                commentModel.setNumLikes(preferenceDataModel.getNumLikes());
                commentModel.setNumShares(preferenceDataModel.getNumShares());
                commentModel.setPostedBy(preferenceDataModel.getPostedBy());
                commentModel.setTitle(preferenceDataModel.getTitle());
                add = preferenceDataModel.getNumComments();
            } else {
                SetCommentData(outDoorPostModel);
            }
        }

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
//        height = metrics.heightPixels;
//        width = metrics.widthPixels;


//        mEditTextComment.setTokenizer(new MultiAutoCompleteTextView.Tokenizer() {
//
//            @Override
//            public CharSequence terminateToken(CharSequence text) {
//                int i = text.length();
//
//                while ((i > 0 && text.charAt(i - 1) == ' ' )) {
//                    i--;
//                }
//
//                if (i > 0 && text.charAt(i - 1) == ' ') {
//                    return text;
//
//                } else {
//                    if (text instanceof Spanned) {
//                        SpannableString sp = new SpannableString(text + " ");
//                        TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, sp, 0);
//                        return sp;
//                    } else {
//                        return text + " ";
//                    }
//                }
//            }
//
//            @Override
//            public int findTokenStart(CharSequence text, int cursor) {
//                int i = cursor;
//
//                while (i > 0 && text.charAt(i - 1) != '@') {
//                    i--;
//                }
//
//                if (i < 1 || text.charAt(i - 1) != '@') {
//                    return cursor;
//                }
//
//                return i;
//            }
//
//            @Override
//            public int findTokenEnd(CharSequence text, int cursor) {
//                int i = cursor;
//                int len = text.length();
//
//                while (i < len) {
//                    if (text.charAt(i) == ' ') {
//                        return i;
//
//                    }
//                    else {
//                        i++;
//                    }
//                }
//
//                return len;
//            }
//        });
//        mEditTextComment.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void afterTextChanged(Editable s) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void onTextChanged(CharSequence text, int start, int before, int count) {
//                String strET = mEditTextComment.getText().toString();
//                String[] str = strET.split(" ");
//                int cnt = 0;
//                if (text.length() != initialStringLength && text.length() != 0) {
//                    if (!strET.substring(strET.length() - 1).equals(" ")) {
//                        initialStringLength = text.length();
//                        cnt = intCount;
//
//                        for (int i = 0; i < str.length; i++)
//                            if (str[i].startsWith("@") && str[i].length() == 1) {
//                                strText = strText + " " + "<font color='#000000'>" + str[i] + "</font>";
//                            } else if (str[i].startsWith("@") /*&& str[i].contains()*/) {
//                                strText = strText + " " + "<font color='#34B7F1'>" + str[i] + "</font>";
//                                userDetails.clear();
//                                userDetails.addAll(AddUser.getallUser(removeFirstChar(str[i])));
//                                adapter = new CustomListAdapter(DetailsActivity.this,
//                                        R.layout.user_info,userDetails);
//                            //    mEditTextComment.setAdapter(adapter);
//                                adapter.notifyDataSetChanged();
//                            } else {
//                                strText = strText + " " + str[i];
//                            }
//                    }
//                }
//                if (intCount == cnt) {
//                    intCount = str.length;
//                    mEditTextComment.setText(Html.fromHtml(strText));
//                    mEditTextComment.setSelection(mEditTextComment.getText().toString().length());
//                }
//                else {
//                    strText = "";
//                }
//            }
//        });
//
//    }

//    int height, width;
//
//    private void loadImage(String url, ImageView imageView) {
//        Picasso.with(DetailsActivity.this).load(url).into(imageView);
//    }
//
//    boolean isKeyboardShowing = false;

//    void onKeyboardVisibilityChanged(boolean opened) {
//        if (opened) {
//          //  mImageViewSend.setVisibility(View.VISIBLE);
//            //   mLinearlayoutLikesCommentShare.setVisibility(View.GONE);
//            mEditTextComment.getLayoutParams().width = width - 100;
//        } else {
//          //  mImageViewSend.setVisibility(View.GONE);
//            //    mLinearlayoutLikesCommentShare.setVisibility(View.VISIBLE);
//            mEditTextComment.getLayoutParams().width = MATCH_PARENT;
//
//        }
    }

    CommentModel commentModel = new CommentModel();

    @Override
    protected void initListener() {

        gifImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(DetailsActivity.this, "Image", Toast.LENGTH_SHORT).show();
                giphyDialogFragment = new GiphyDialogFragment();
                giphyDialogFragment.setGifSelectionListener(new GiphyDialogFragment.GifSelectionListener() {
                    @Override
                    public void onGifSelected(@NotNull Media media, @org.jetbrains.annotations.Nullable String s, @NotNull GPHContentType gphContentType) {
                        String[] split = media.getUrl().split("-|/");
                        String gif_url = "https://media.giphy.com/media/" + split[split.length - 1] + "/giphy.gif";
                        Log.d(TAG, "onGifSelected URL: " + gif_url);
                        Log.d(TAG, "onGifSelected: " + media.getUrl());
                        Log.d(TAG, "onGifSelected: OriginalOriginal:" + media.getImages().getOriginal());
                        Log.d(TAG, "onGifSelected: S:" + s);
                        Log.d(TAG, "onGifSelected: " + media.getBitlyGifUrl());

                        final Dialog dialog = new Dialog(DetailsActivity.this); // Context, this, etc.
                        dialog.setContentView(R.layout.dialog_demo);
                        dialog.setCancelable(false);
                        ImageView imageView = dialog.findViewById(R.id.gif_image);
                        Button ok = dialog.findViewById(R.id.ok);
                        Button cancel = dialog.findViewById(R.id.cancel);
                        ProgressBar progress = dialog.findViewById(R.id.progress);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (imageView.getDrawable() != null) {
                                    mImageViewSend.setClickable(false);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mImageViewSend.setClickable(true);
                                        }
                                    }, 5000);


                                        if (isKnowledgeComment) {

                                            String postId = outDoorPostModel.getSl_no() + "";
                                            Call<NewAPIResponseModel> call123 = null;
                                            if (isNotificationData) {
                                                call123 = apiInterface.GetCommentsByPostId(postId, notificationDataModel.getType());
                                            } else {
                                                if (isKnowledgeComment) {
                                                    cid = 1;
                                                    GetCommentLikeListByUserID(1);
                                                    call123 = apiInterface.GetCommentsByPostId(postId, 1);

                                                } else if (isKnowledgeGeneralComment) {
                                                    cid = 2;
                                                    GetCommentLikeListByUserID(2);
                                                    call123 = apiInterface.GetCommentsByPostId(postId, 2);

                                                } else if (isAspirationComment) {
                                                    cid = 3;
                                                    GetCommentLikeListByUserID(3);
                                                    call123 = apiInterface.GetCommentsByPostId(postId, 3);

                                                } else if (isAspirationGeneralComment) {
                                                    cid = 4;
                                                    GetCommentLikeListByUserID(4);
                                                    call123 = apiInterface.GetCommentsByPostId(postId, 4);

                                                } else if (isOutdoorComment) {
                                                    cid = 5;
                                                    GetCommentLikeListByUserID(5);
                                                    call123 = apiInterface.GetCommentsByPostId(postId, 5);

                                                }
                                            }
                                            call123.enqueue(new Callback<NewAPIResponseModel>() {
                                                @Override
                                                public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                                                    if (response.isSuccessful()) {
                                                        int totalcommentcount = response.body().getCommentModel().size();
                                                        Log.d(TAG, "onResponse: Knowledge count " + totalcommentcount);
                                                        String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
                                                        commentModel.setCommentDesc(mEditTextComment.getText().toString().trim() + gif_url);
                                                        commentModel.setDatetime(System.currentTimeMillis());
                                                        String uid = SharedPrefsHelper.getInstance().get(USER_ID);
                                                        commentModel.setCommentedById(uid);
                                                        String name = SharedPrefsHelper.getInstance().get(NAME);
                                                        commentModel.setCommentedByName(name);
                                                        commentModel.setNumComments(totalcommentcount);
                                                        if (prfurl != null)
                                                            commentModel.setCommentorURL(prfurl);
                                                        mProgressBar.setVisibility(View.VISIBLE);
                                                        Call<APIResponseModel> apiResponseModelCall = apiInterface.AddComment(commentModel, 1);
                                                        apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
                                                            @Override
                                                            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                                                                mProgressBar.setVisibility(View.GONE);
                                                                if (response.body() != null && response.body().isStatus()) {
                                                                    add = add + 1;
                                                                    commentModel.setNumComments(add);
                                                                    SharedPrefsHelper.getInstance().save("numComments", commentModel.getNumComments());
                                                                    SharedPreferences.Editor editor = context.getSharedPreferences(DATA_OF_COMMENT_AND_SLNO, MODE_PRIVATE).edit();
                                                                    editor.putLong("numComments", commentModel.getNumComments());
                                                                    editor.putLong("id", outDoorPostModel.getSl_no());
                                                                    editor.apply();
                                                                    mEditTextComment.setText("");
                                                                    recyclerViewCommentor.setAdapter(mCommentsListAdapter);
                                                                    recyclerViewCommentor.invalidate();
                                                                    // add=add+1;
                                                                    getOutdoorComments(outDoorPostModel.getSl_no() + "");

                                                                } else {
                                                                    AppUtils.showToastMessage(DetailsActivity.this, getString(R.string.some_thing_wrong));
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                                                                mProgressBar.setVisibility(View.GONE);
                                                                AppUtils.showToastMessage(DetailsActivity.this, getString(R.string.some_thing_wrong));
                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {

                                                }
                                            });


                                        } else if (isAspirationComment) {
                                            String postId = outDoorPostModel.getSl_no() + "";
                                            Call<NewAPIResponseModel> call123 = null;
                                            if (isNotificationData) {
                                                call123 = apiInterface.GetCommentsByPostId(postId, notificationDataModel.getType());
                                            } else {
                                                if (isKnowledgeComment) {
                                                    cid = 1;
                                                    GetCommentLikeListByUserID(1);
                                                    call123 = apiInterface.GetCommentsByPostId(postId, 1);

                                                } else if (isKnowledgeGeneralComment) {
                                                    cid = 2;
                                                    GetCommentLikeListByUserID(2);
                                                    call123 = apiInterface.GetCommentsByPostId(postId, 2);

                                                } else if (isAspirationComment) {
                                                    cid = 3;
                                                    GetCommentLikeListByUserID(3);
                                                    call123 = apiInterface.GetCommentsByPostId(postId, 3);

                                                } else if (isAspirationGeneralComment) {
                                                    cid = 4;
                                                    GetCommentLikeListByUserID(4);
                                                    call123 = apiInterface.GetCommentsByPostId(postId, 4);

                                                } else if (isOutdoorComment) {
                                                    cid = 5;
                                                    GetCommentLikeListByUserID(5);
                                                    call123 = apiInterface.GetCommentsByPostId(postId, 5);

                                                }
                                            }
                                            call123.enqueue(new Callback<NewAPIResponseModel>() {
                                                @Override
                                                public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                                                    if (response.isSuccessful()) {
                                                        int totalcommentcount = response.body().getCommentModel().size();
                                                        Log.d(TAG, "onResponse: Aspiration count " + totalcommentcount);

                                                        String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
                                                        commentModel.setCommentDesc(mEditTextComment.getText().toString().trim() + gif_url);
                                                        commentModel.setDatetime(System.currentTimeMillis());
                                                        String uid = SharedPrefsHelper.getInstance().get(USER_ID);
                                                        commentModel.setCommentedById(uid);
                                                        String name = SharedPrefsHelper.getInstance().get(NAME);
                                                        commentModel.setCommentedByName(name);
                                                        commentModel.setNumComments(totalcommentcount);
                                                        if (prfurl != null)
                                                            commentModel.setCommentorURL(prfurl);
                                                        mProgressBar.setVisibility(View.VISIBLE);
                                                        Call<APIResponseModel> apiResponseModelCall = apiInterface.AddComment(commentModel, 3);
                                                        apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
                                                            @Override
                                                            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                                                                mProgressBar.setVisibility(View.GONE);
                                                                if (response.body() != null && response.body().isStatus()) {
                                                                    add = add + 1;
                                                                    commentModel.setNumComments(add);
                                                                    //  commentModel.setNumComments((commentModel.getNumComments() + 1));
                                                                    SharedPreferences.Editor editor = context.getSharedPreferences(DATA_OF_COMMENT_AND_SLNO3, MODE_PRIVATE).edit();
                                                                    editor.putLong("numComments", commentModel.getNumComments());
                                                                    editor.putLong("id", outDoorPostModel.getSl_no());
                                                                    editor.apply();
                                                                    // Toast.makeText(DetailsActivity.this, outDoorPostModel.getSl_no()+" "+commentModel.getNumComments(), Toast.LENGTH_SHORT).show();
                                                                    mEditTextComment.setText("");
                                                                    recyclerViewCommentor.setAdapter(mCommentsListAdapter);
                                                                    recyclerViewCommentor.invalidate();
                                                                    getOutdoorComments(outDoorPostModel.getSl_no() + "");
                                                                } else {
                                                                    AppUtils.showToastMessage(DetailsActivity.this, getString(R.string.some_thing_wrong));
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                                                                mProgressBar.setVisibility(View.GONE);
                                                                AppUtils.showToastMessage(DetailsActivity.this, getString(R.string.some_thing_wrong));
                                                            }
                                                        });

                                                    }

                                                }

                                                @Override
                                                public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {

                                                }
                                            });


                                        } else if (isAspirationGeneralComment) {
                                            String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
                                            commentModel.setCommentDesc(mEditTextComment.getText().toString().trim() + gif_url);
                                            commentModel.setDatetime(System.currentTimeMillis());
                                            String uid = SharedPrefsHelper.getInstance().get(USER_ID);
                                            commentModel.setCommentedById(uid);
                                            String name = SharedPrefsHelper.getInstance().get(NAME);
                                            commentModel.setCommentedByName(name);
                                            if (prfurl != null)
                                                commentModel.setCommentorURL(prfurl);

                                            mProgressBar.setVisibility(View.VISIBLE);
                                            Call<APIResponseModel> apiResponseModelCall = apiInterface.AddComment(commentModel, 4);
                                            apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
                                                @Override
                                                public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                                                    mProgressBar.setVisibility(View.GONE);
                                                    if (response.body() != null && response.body().isStatus()) {
                                                        commentModel.setNumComments((commentModel.getNumComments() + 1));
                                                        SharedPreferences.Editor editor = context.getSharedPreferences(DATA_OF_COMMENT_AND_SLNO4, MODE_PRIVATE).edit();
                                                        editor.putLong("numComments", commentModel.getNumComments());
                                                        editor.putLong("id", outDoorPostModel.getSl_no());
                                                        editor.apply();
                                                        //Toast.makeText(DetailsActivity.this, outDoorPostModel.getSl_no()+" "+commentModel.getNumComments(), Toast.LENGTH_SHORT).show();
                                                        mEditTextComment.setText("");
                                                        recyclerViewCommentor.setAdapter(mCommentsListAdapter);
                                                        recyclerViewCommentor.invalidate();
                                                        getOutdoorComments(outDoorPostModel.getSl_no() + "");
                                                    } else {
                                                        AppUtils.showToastMessage(DetailsActivity.this, getString(R.string.some_thing_wrong));
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<APIResponseModel> call, Throwable t) {
                                                    mProgressBar.setVisibility(View.GONE);
                                                    AppUtils.showToastMessage(DetailsActivity.this, getString(R.string.some_thing_wrong));
                                                }
                                            });
                                        } else if (isKnowledgeGeneralComment) {
                                            String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
                                            commentModel.setCommentDesc(mEditTextComment.getText().toString().trim() + gif_url);
                                            commentModel.setDatetime(System.currentTimeMillis());
                                            String uid = SharedPrefsHelper.getInstance().get(USER_ID);
                                            commentModel.setCommentedById(uid);
                                            String name = SharedPrefsHelper.getInstance().get(NAME);
                                            commentModel.setCommentedByName(name);
                                            if (prfurl != null)
                                                commentModel.setCommentorURL(prfurl);
                                            mProgressBar.setVisibility(View.VISIBLE);
                                            Call<APIResponseModel> apiResponseModelCall = apiInterface.AddComment(commentModel, 2);
                                            apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
                                                @Override
                                                public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                                                    mProgressBar.setVisibility(View.GONE);
                                                    if (response.body() != null && response.body().isStatus()) {
                                                        commentModel.setNumComments((commentModel.getNumComments() + 1));
                                                        SharedPreferences.Editor editor = context.getSharedPreferences(DATA_OF_COMMENT_AND_SLNO2, MODE_PRIVATE).edit();
                                                        editor.putLong("numComments", commentModel.getNumComments());
                                                        editor.putLong("id", outDoorPostModel.getSl_no());
                                                        editor.apply();
                                                        //  Toast.makeText(DetailsActivity.this, outDoorPostModel.getSl_no()+" "+commentModel.getNumComments(), Toast.LENGTH_SHORT).show();
                                                        mEditTextComment.setText("");
                                                        recyclerViewCommentor.setAdapter(mCommentsListAdapter);
                                                        recyclerViewCommentor.invalidate();
                                                        getOutdoorComments(outDoorPostModel.getSl_no() + "");
                                                    } else {
                                                        AppUtils.showToastMessage(DetailsActivity.this, getString(R.string.some_thing_wrong));
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<APIResponseModel> call, Throwable t) {
                                                    mProgressBar.setVisibility(View.GONE);
                                                    AppUtils.showToastMessage(DetailsActivity.this, getString(R.string.some_thing_wrong));
                                                }
                                            });
                                        }
                                        else if (isOutdoorComment) {
                                            String postId = outDoorPostModel.getSl_no() + "";
                                            Call<NewAPIResponseModel> call123 = null;
                                            if (isNotificationData) {
                                                call123 = apiInterface.GetCommentsByPostId(postId, notificationDataModel.getType());
                                            } else {
                                                if (isKnowledgeComment) {
                                                    cid = 1;
                                                    GetCommentLikeListByUserID(1);
                                                    call123 = apiInterface.GetCommentsByPostId(postId, 1);

                                                } else if (isKnowledgeGeneralComment) {
                                                    cid = 2;
                                                    GetCommentLikeListByUserID(2);
                                                    call123 = apiInterface.GetCommentsByPostId(postId, 2);

                                                } else if (isAspirationComment) {
                                                    cid = 3;
                                                    GetCommentLikeListByUserID(3);
                                                    call123 = apiInterface.GetCommentsByPostId(postId, 3);

                                                } else if (isAspirationGeneralComment) {
                                                    cid = 4;
                                                    GetCommentLikeListByUserID(4);
                                                    call123 = apiInterface.GetCommentsByPostId(postId, 4);

                                                } else if (isOutdoorComment) {
                                                    cid = 5;
                                                    GetCommentLikeListByUserID(5);
                                                    call123 = apiInterface.GetCommentsByPostId(postId, 5);

                                                }
                                            }

                                            call123.enqueue(new Callback<NewAPIResponseModel>() {
                                                @Override
                                                public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                                                    int totalcommentcount = response.body().getCommentModel().size();
                                                    Log.d(TAG, "onResponse: Outdoor count " + totalcommentcount);
                                                    String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
                                                    commentModel.setCommentDesc(mEditTextComment.getText().toString().trim() + gif_url);
                                                    commentModel.setDatetime(System.currentTimeMillis());
                                                    String uid = SharedPrefsHelper.getInstance().get(USER_ID);
                                                    commentModel.setCommentedById(uid);
                                                    String name = SharedPrefsHelper.getInstance().get(NAME);
                                                    commentModel.setCommentedByName(name);
                                                    commentModel.setNumComments(totalcommentcount);
                                                    if (prfurl != null)
                                                        commentModel.setCommentorURL(prfurl);
                                                    mProgressBar.setVisibility(View.VISIBLE);
                                                    Call<APIResponseModel> apiResponseModelCall = apiInterface.AddComment(commentModel, 5);
                                                    apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
                                                        @Override
                                                        public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                                                            mProgressBar.setVisibility(View.GONE);
                                                            if (response.body() != null && response.body().isStatus()) {
                                                                add = add + 1;
                                                                commentModel.setNumComments(add);
                                                                //commentModel.setNumComments((commentModel.getNumComments() + 1));
                                                                SharedPreferences.Editor editor = context.getSharedPreferences(DATA_OF_COMMENT_AND_SLNO5, MODE_PRIVATE).edit();
                                                                editor.putLong("numComments", commentModel.getNumComments());
                                                                editor.putLong("id", outDoorPostModel.getSl_no());
                                                                editor.apply();
                                                                //  Toast.makeText(DetailsActivity.this, outDoorPostModel.getSl_no()+" "+commentModel.getNumComments(), Toast.LENGTH_SHORT).show();
                                                                mEditTextComment.setText("");
                                                                recyclerViewCommentor.setAdapter(mCommentsListAdapter);
                                                                recyclerViewCommentor.invalidate();
                                                                getOutdoorComments(outDoorPostModel.getSl_no() + "");
                                                            } else {
                                                                AppUtils.showToastMessage(DetailsActivity.this, getString(R.string.some_thing_wrong));
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<APIResponseModel> call, Throwable t) {
                                                            mProgressBar.setVisibility(View.GONE);
                                                            AppUtils.showToastMessage(DetailsActivity.this, getString(R.string.some_thing_wrong));
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {

                                                }
                                            });


                                        }

                               /* else {
                                    Toast.makeText(DetailsActivity.this, "Write Comment", Toast.LENGTH_LONG).show();
                                }*/
                                    dialog.dismiss();
                                    Arrays.fill(split, null);
                                }
                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Arrays.fill(split, null);
                            }
                        });
                        Glide.with(DetailsActivity.this)
                                .load(gif_url)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@androidx.annotation.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        progress.setVisibility(GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                        progress.setVisibility(GONE);
                                        return false;
                                    }
                                }).thumbnail(0.05f).diskCacheStrategy(DiskCacheStrategy.DATA)
                                .into(imageView);
                        dialog.show();

                    }

                    @Override
                    public void onDismissed(@NotNull GPHContentType gphContentType) {

                    }

                    @Override
                    public void didSearchTerm(@NotNull String s) {

                    }
                });
                giphyDialogFragment.show(getSupportFragmentManager(), "abc");
            }


        });
        mImageViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageViewSend.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mImageViewSend.setClickable(true);
                    }
                }, 5000);

                if (mEditTextComment.getText() != null && mEditTextComment.getText().toString().trim().length() > 0) {

                    if (isKnowledgeComment) {

                        String postId = outDoorPostModel.getSl_no() + "";
                        Call<NewAPIResponseModel> call123 = null;
                        if (isNotificationData) {
                            call123 = apiInterface.GetCommentsByPostId(postId, notificationDataModel.getType());
                        } else {
                            if (isKnowledgeComment) {
                                cid = 1;
                                GetCommentLikeListByUserID(1);
                                call123 = apiInterface.GetCommentsByPostId(postId, 1);

                            } else if (isKnowledgeGeneralComment) {
                                cid = 2;
                                GetCommentLikeListByUserID(2);
                                call123 = apiInterface.GetCommentsByPostId(postId, 2);

                            } else if (isAspirationComment) {
                                cid = 3;
                                GetCommentLikeListByUserID(3);
                                call123 = apiInterface.GetCommentsByPostId(postId, 3);

                            } else if (isAspirationGeneralComment) {
                                cid = 4;
                                GetCommentLikeListByUserID(4);
                                call123 = apiInterface.GetCommentsByPostId(postId, 4);

                            } else if (isOutdoorComment) {
                                cid = 5;
                                GetCommentLikeListByUserID(5);
                                call123 = apiInterface.GetCommentsByPostId(postId, 5);

                            }
                        }
                        call123.enqueue(new Callback<NewAPIResponseModel>() {
                            @Override
                            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                                if (response.isSuccessful()) {
                                    int totalcommentcount = response.body().getCommentModel().size();
                                    Log.d(TAG, "onResponse: Knowledge count " + totalcommentcount);
                                    String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
                                    commentModel.setCommentDesc(mEditTextComment.getText().toString().trim());
                                    commentModel.setDatetime(System.currentTimeMillis());
                                    String uid = SharedPrefsHelper.getInstance().get(USER_ID);
                                    commentModel.setCommentedById(uid);
                                    String name = SharedPrefsHelper.getInstance().get(NAME);
                                    commentModel.setCommentedByName(name);
                                    commentModel.setNumComments(totalcommentcount);
                                    if (prfurl != null)
                                        commentModel.setCommentorURL(prfurl);
                                    mProgressBar.setVisibility(View.VISIBLE);
                                    Call<APIResponseModel> apiResponseModelCall = apiInterface.AddComment(commentModel, 1);
                                    apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
                                        @Override
                                        public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                                            mProgressBar.setVisibility(View.GONE);
                                            if (response.body() != null && response.body().isStatus()) {
                                                add = add + 1;
                                                commentModel.setNumComments(add);
                                                SharedPrefsHelper.getInstance().save("numComments", commentModel.getNumComments());
                                                SharedPreferences.Editor editor = context.getSharedPreferences(DATA_OF_COMMENT_AND_SLNO, MODE_PRIVATE).edit();
                                                editor.putLong("numComments", commentModel.getNumComments());
                                                editor.putLong("id", outDoorPostModel.getSl_no());
                                                editor.apply();
                                                mEditTextComment.setText("");
                                                recyclerViewCommentor.setAdapter(mCommentsListAdapter);
                                                recyclerViewCommentor.invalidate();
                                                // add=add+1;
                                                getOutdoorComments(outDoorPostModel.getSl_no() + "");

                                            } else {
                                                AppUtils.showToastMessage(DetailsActivity.this, getString(R.string.some_thing_wrong));
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<APIResponseModel> call, Throwable t) {
                                            mProgressBar.setVisibility(View.GONE);
                                            AppUtils.showToastMessage(DetailsActivity.this, getString(R.string.some_thing_wrong));
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {

                            }
                        });


                    } else if (isAspirationComment) {
                        String postId = outDoorPostModel.getSl_no() + "";
                        Call<NewAPIResponseModel> call123 = null;
                        if (isNotificationData) {
                            call123 = apiInterface.GetCommentsByPostId(postId, notificationDataModel.getType());
                        } else {
                            if (isKnowledgeComment) {
                                cid = 1;
                                GetCommentLikeListByUserID(1);
                                call123 = apiInterface.GetCommentsByPostId(postId, 1);

                            } else if (isKnowledgeGeneralComment) {
                                cid = 2;
                                GetCommentLikeListByUserID(2);
                                call123 = apiInterface.GetCommentsByPostId(postId, 2);

                            } else if (isAspirationComment) {
                                cid = 3;
                                GetCommentLikeListByUserID(3);
                                call123 = apiInterface.GetCommentsByPostId(postId, 3);

                            } else if (isAspirationGeneralComment) {
                                cid = 4;
                                GetCommentLikeListByUserID(4);
                                call123 = apiInterface.GetCommentsByPostId(postId, 4);

                            } else if (isOutdoorComment) {
                                cid = 5;
                                GetCommentLikeListByUserID(5);
                                call123 = apiInterface.GetCommentsByPostId(postId, 5);

                            }
                        }
                        call123.enqueue(new Callback<NewAPIResponseModel>() {
                            @Override
                            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                                if (response.isSuccessful()) {
                                    int totalcommentcount = response.body().getCommentModel().size();
                                    Log.d(TAG, "onResponse: Aspiration count " + totalcommentcount);

                                    String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
                                    commentModel.setCommentDesc(mEditTextComment.getText().toString().trim());
                                    commentModel.setDatetime(System.currentTimeMillis());
                                    String uid = SharedPrefsHelper.getInstance().get(USER_ID);
                                    commentModel.setCommentedById(uid);
                                    String name = SharedPrefsHelper.getInstance().get(NAME);
                                    commentModel.setCommentedByName(name);
                                    commentModel.setNumComments(totalcommentcount);
                                    if (prfurl != null)
                                        commentModel.setCommentorURL(prfurl);
                                    mProgressBar.setVisibility(View.VISIBLE);
                                    Call<APIResponseModel> apiResponseModelCall = apiInterface.AddComment(commentModel, 3);
                                    apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
                                        @Override
                                        public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                                            mProgressBar.setVisibility(View.GONE);
                                            if (response.body() != null && response.body().isStatus()) {
                                                add = add + 1;
                                                commentModel.setNumComments(add);
                                                //  commentModel.setNumComments((commentModel.getNumComments() + 1));
                                                SharedPreferences.Editor editor = context.getSharedPreferences(DATA_OF_COMMENT_AND_SLNO3, MODE_PRIVATE).edit();
                                                editor.putLong("numComments", commentModel.getNumComments());
                                                editor.putLong("id", outDoorPostModel.getSl_no());
                                                editor.apply();
                                                // Toast.makeText(DetailsActivity.this, outDoorPostModel.getSl_no()+" "+commentModel.getNumComments(), Toast.LENGTH_SHORT).show();
                                                mEditTextComment.setText("");
                                                recyclerViewCommentor.setAdapter(mCommentsListAdapter);
                                                recyclerViewCommentor.invalidate();
                                                getOutdoorComments(outDoorPostModel.getSl_no() + "");
                                            } else {
                                                AppUtils.showToastMessage(DetailsActivity.this, getString(R.string.some_thing_wrong));
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<APIResponseModel> call, Throwable t) {
                                            mProgressBar.setVisibility(View.GONE);
                                            AppUtils.showToastMessage(DetailsActivity.this, getString(R.string.some_thing_wrong));
                                        }
                                    });

                                }

                            }

                            @Override
                            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {

                            }
                        });


                    } else if (isAspirationGeneralComment) {
                        String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
                        commentModel.setCommentDesc(mEditTextComment.getText().toString().trim());
                        commentModel.setDatetime(System.currentTimeMillis());
                        String uid = SharedPrefsHelper.getInstance().get(USER_ID);
                        commentModel.setCommentedById(uid);
                        String name = SharedPrefsHelper.getInstance().get(NAME);
                        commentModel.setCommentedByName(name);
                        if (prfurl != null)
                            commentModel.setCommentorURL(prfurl);

                        mProgressBar.setVisibility(View.VISIBLE);
                        Call<APIResponseModel> apiResponseModelCall = apiInterface.AddComment(commentModel, 4);
                        apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
                            @Override
                            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                                mProgressBar.setVisibility(View.GONE);
                                if (response.body() != null && response.body().isStatus()) {
                                    commentModel.setNumComments((commentModel.getNumComments() + 1));
                                    SharedPreferences.Editor editor = context.getSharedPreferences(DATA_OF_COMMENT_AND_SLNO4, MODE_PRIVATE).edit();
                                    editor.putLong("numComments", commentModel.getNumComments());
                                    editor.putLong("id", outDoorPostModel.getSl_no());
                                    editor.apply();
                                    //Toast.makeText(DetailsActivity.this, outDoorPostModel.getSl_no()+" "+commentModel.getNumComments(), Toast.LENGTH_SHORT).show();
                                    mEditTextComment.setText("");
                                    recyclerViewCommentor.setAdapter(mCommentsListAdapter);
                                    recyclerViewCommentor.invalidate();
                                    getOutdoorComments(outDoorPostModel.getSl_no() + "");
                                } else {
                                    AppUtils.showToastMessage(DetailsActivity.this, getString(R.string.some_thing_wrong));
                                }
                            }

                            @Override
                            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                                mProgressBar.setVisibility(View.GONE);
                                AppUtils.showToastMessage(DetailsActivity.this, getString(R.string.some_thing_wrong));
                            }
                        });
                    } else if (isKnowledgeGeneralComment) {
                        String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
                        commentModel.setCommentDesc(mEditTextComment.getText().toString().trim());
                        commentModel.setDatetime(System.currentTimeMillis());
                        String uid = SharedPrefsHelper.getInstance().get(USER_ID);
                        commentModel.setCommentedById(uid);
                        String name = SharedPrefsHelper.getInstance().get(NAME);
                        commentModel.setCommentedByName(name);
                        if (prfurl != null)
                            commentModel.setCommentorURL(prfurl);
                        mProgressBar.setVisibility(View.VISIBLE);
                        Call<APIResponseModel> apiResponseModelCall = apiInterface.AddComment(commentModel, 2);
                        apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
                            @Override
                            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                                mProgressBar.setVisibility(View.GONE);
                                if (response.body() != null && response.body().isStatus()) {
                                    commentModel.setNumComments((commentModel.getNumComments() + 1));
                                    SharedPreferences.Editor editor = context.getSharedPreferences(DATA_OF_COMMENT_AND_SLNO2, MODE_PRIVATE).edit();
                                    editor.putLong("numComments", commentModel.getNumComments());
                                    editor.putLong("id", outDoorPostModel.getSl_no());
                                    editor.apply();
                                    //  Toast.makeText(DetailsActivity.this, outDoorPostModel.getSl_no()+" "+commentModel.getNumComments(), Toast.LENGTH_SHORT).show();
                                    mEditTextComment.setText("");
                                    recyclerViewCommentor.setAdapter(mCommentsListAdapter);
                                    recyclerViewCommentor.invalidate();
                                    getOutdoorComments(outDoorPostModel.getSl_no() + "");
                                } else {
                                    AppUtils.showToastMessage(DetailsActivity.this, getString(R.string.some_thing_wrong));
                                }
                            }

                            @Override
                            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                                mProgressBar.setVisibility(View.GONE);
                                AppUtils.showToastMessage(DetailsActivity.this, getString(R.string.some_thing_wrong));
                            }
                        });
                    } else if (isOutdoorComment) {
                        String postId = outDoorPostModel.getSl_no() + "";
                        Call<NewAPIResponseModel> call123 = null;
                        if (isNotificationData) {
                            call123 = apiInterface.GetCommentsByPostId(postId, notificationDataModel.getType());
                        } else {
                            if (isKnowledgeComment) {
                                cid = 1;
                                GetCommentLikeListByUserID(1);
                                call123 = apiInterface.GetCommentsByPostId(postId, 1);

                            } else if (isKnowledgeGeneralComment) {
                                cid = 2;
                                GetCommentLikeListByUserID(2);
                                call123 = apiInterface.GetCommentsByPostId(postId, 2);

                            } else if (isAspirationComment) {
                                cid = 3;
                                GetCommentLikeListByUserID(3);
                                call123 = apiInterface.GetCommentsByPostId(postId, 3);

                            } else if (isAspirationGeneralComment) {
                                cid = 4;
                                GetCommentLikeListByUserID(4);
                                call123 = apiInterface.GetCommentsByPostId(postId, 4);

                            } else if (isOutdoorComment) {
                                cid = 5;
                                GetCommentLikeListByUserID(5);
                                call123 = apiInterface.GetCommentsByPostId(postId, 5);

                            }
                        }

                        call123.enqueue(new Callback<NewAPIResponseModel>() {
                            @Override
                            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                                int totalcommentcount = response.body().getCommentModel().size();
                                Log.d(TAG, "onResponse: Outdoor count " + totalcommentcount);
                                String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
                                commentModel.setCommentDesc(mEditTextComment.getText().toString().trim());
                                commentModel.setDatetime(System.currentTimeMillis());
                                String uid = SharedPrefsHelper.getInstance().get(USER_ID);
                                commentModel.setCommentedById(uid);
                                String name = SharedPrefsHelper.getInstance().get(NAME);
                                commentModel.setCommentedByName(name);
                                commentModel.setNumComments(totalcommentcount);
                                if (prfurl != null)
                                    commentModel.setCommentorURL(prfurl);
                                mProgressBar.setVisibility(View.VISIBLE);
                                Call<APIResponseModel> apiResponseModelCall = apiInterface.AddComment(commentModel, 5);
                                apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
                                    @Override
                                    public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                                        mProgressBar.setVisibility(View.GONE);
                                        if (response.body() != null && response.body().isStatus()) {
                                            add = add + 1;
                                            commentModel.setNumComments(add);
                                            //commentModel.setNumComments((commentModel.getNumComments() + 1));
                                            SharedPreferences.Editor editor = context.getSharedPreferences(DATA_OF_COMMENT_AND_SLNO5, MODE_PRIVATE).edit();
                                            editor.putLong("numComments", commentModel.getNumComments());
                                            editor.putLong("id", outDoorPostModel.getSl_no());
                                            editor.apply();
                                            //  Toast.makeText(DetailsActivity.this, outDoorPostModel.getSl_no()+" "+commentModel.getNumComments(), Toast.LENGTH_SHORT).show();
                                            mEditTextComment.setText("");
                                            recyclerViewCommentor.setAdapter(mCommentsListAdapter);
                                            recyclerViewCommentor.invalidate();
                                            getOutdoorComments(outDoorPostModel.getSl_no() + "");
                                        } else {
                                            AppUtils.showToastMessage(DetailsActivity.this, getString(R.string.some_thing_wrong));
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<APIResponseModel> call, Throwable t) {
                                        mProgressBar.setVisibility(View.GONE);
                                        AppUtils.showToastMessage(DetailsActivity.this, getString(R.string.some_thing_wrong));
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {

                            }
                        });


                    }
                } else {
                    Toast.makeText(DetailsActivity.this, "Write Comment", Toast.LENGTH_LONG).show();
                }

            }
        });

        mLinearLayoutComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, CommentDetailsActivity.class);
                intent.putExtra("DATA", commentModel);
                intent.putExtra("TYPE", type);

                if (outDoorPostModel != null && outDoorPostModel.getSl_no() != 0) {
                    intent.putExtra("POST_ID", "" + outDoorPostModel.getSl_no());
                }
                if (isOutdoorComment) {
                    intent.putExtra("IS_OUTDOOR", true);

                } else if (isKnowledgeComment) {
                    intent.putExtra("KNOWLEDGE_COMMENT", true);

                } else if (isKnowledgeGeneralComment) {
                    intent.putExtra("KNOWLEDGE_COMMENT_GENERAL", true);

                } else if (isAspirationComment) {
                    intent.putExtra("ASPIRATION_COMMENT", true);

                } else if (isAspirationGeneralComment) {
                    intent.putExtra("ASPIRATION_COMMENT_GENERAL", true);

                }
                intent.putExtra("SUBJECT", mSelectedSubject);
                startActivity(intent);
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /*mImageViewPostDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePost(outDoorPostModel.getPostId());
            }
        });*/

//        contentView.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//
//                        Rect r = new Rect();
//                        contentView.getWindowVisibleDisplayFrame(r);
//                        int screenHeight = contentView.getRootView().getHeight();
//
//                        // r.bottom is the position above soft keypad or device button.
//                        // if keypad is shown, the r.bottom is smaller than that before.
//                        int keypadHeight = screenHeight - r.bottom;
//
//                        Log.d(TAG, "keypadHeight = " + keypadHeight);
//
//                        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
//                            // keyboard is opened
//                            if (!isKeyboardShowing) {
//                                isKeyboardShowing = true;
//                                onKeyboardVisibilityChanged(true);
//                            }
//                        } else {
//                            // keyboard is closed
//                            if (isKeyboardShowing) {
//                                isKeyboardShowing = false;
//                                onKeyboardVisibilityChanged(false);
//                            }
//                        }
//                    }
//                });

       /* recyclerViewCommentor.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerViewCommentor, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int type = 1;
                if(isKnowledgeComment)
                {
                     type = 1;
                }
                else if(isKnowledgeGeneralComment)
                {
                    type =  2;
                }
                else if(isAspirationComment)
                {
                    type =  3;
                }
                else if(isAspirationGeneralComment)
                {
                    type =  4;
                }
                else if(isOutdoorComment) {
                    type =  5;
                }



                Log.d(TAG, "onItemClick: "+commentModelList.get(position).getCommentedById());
                Intent intent = new Intent(DetailsActivity.this, PostSubCommentActivity.class);
                intent.putExtra("DATA", commentModelList.get(position));
                intent.putExtra("TYPE", type);
               startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));*/
    }

    private void deletePost(String postId) {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<List<APIResponseModel>> hashTagCall = null;
        if (isOutdoorComment) {
            hashTagCall = apiInterface.deletePost(postId, 5);
        } else if (isKnowledgeComment) {
            hashTagCall = apiInterface.deletePost(postId, 1);
        }

        hashTagCall.enqueue(new Callback<List<APIResponseModel>>() {
            @Override
            public void onResponse(Call<List<APIResponseModel>> call, Response<List<APIResponseModel>> response) {
                mProgressBar.setVisibility(View.GONE);
                AppUtils.showToastMessage(DetailsActivity.this, getString(R.string.post_deleted_success));
                // alertDialogMoreOptions.dismiss();
            }

            @Override
            public void onFailure(Call<List<APIResponseModel>> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer(player);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializePlayer(PlayerView playerView, SimpleExoPlayer player2, String url) {
        player = ExoPlayerFactory.newSimpleInstance(context);
        playerView.setPlayer(player);
        Uri uri = Uri.parse(url);
        MediaSource mediaSource = buildMediaSource(uri);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, false, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(context, "exoplayer-codelab");
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }

    private void releasePlayer(SimpleExoPlayer player2) {
        if (player != null) {
            // player.removeListener(playbackStateListener);
            player.release();
            player = null;
        }
    }

    protected ApiInterface apiInterface;

    private void setUpNetwork() {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }

    private void getOutdoorComments(String postId) {
        //   mSwipeRefreshLayout.setRefreshing(true);

        Call<NewAPIResponseModel> call = null;
        if (isNotificationData) {
            call = apiInterface.GetCommentsByPostId(postId, notificationDataModel.getType());
        } else {
            if (isKnowledgeComment) {
                cid = 1;
                GetCommentLikeListByUserID(1);
                call = apiInterface.GetCommentsByPostId(postId, 1);

            } else if (isKnowledgeGeneralComment) {
                cid = 2;
                GetCommentLikeListByUserID(2);
                call = apiInterface.GetCommentsByPostId(postId, 2);

            } else if (isAspirationComment) {
                cid = 3;
                GetCommentLikeListByUserID(3);
                call = apiInterface.GetCommentsByPostId(postId, 3);

            } else if (isAspirationGeneralComment) {
                cid = 4;
                GetCommentLikeListByUserID(4);
                call = apiInterface.GetCommentsByPostId(postId, 4);

            } else if (isOutdoorComment) {
                cid = 5;
                GetCommentLikeListByUserID(5);
                call = apiInterface.GetCommentsByPostId(postId, 5);

            }
        }
        call.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {

                commentModelList = response.body().getCommentModel();
                //  Log.d(TAG, "onResponse:MYCOUNT"+commentModelList.size());
                //   totalcommentcount=commentModelList.size();
                if (commentModelList != null) {

                    // Collections.reverse(commentModelList);
                    //  mSwipeRefreshLayout.setRefreshing(false);
                    mCommentsListAdapter = new CommentsListAdapter(DetailsActivity.this, commentModelList, likeDatalist, selectItemInterface, cid + "");
                    recyclerViewCommentor.setAdapter(mCommentsListAdapter);
                    // Log.d(TAG, "isNotificationData:my data "+commentModelList.get(0).getDescription());
                    //  hideKeyboard(DetailsActivity.this);
                    // mEditTextComment.clearFocus();
                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                    );
                    mCommentsListAdapter.notifyDataSetChanged();
                   /* if(mCommentsListAdapter!=null)
                    {
                        mCommentsListAdapter.notifyDataSetChanged();
                    }*/
                    //  recyclerViewCommentor.invalidate();
                    //Toast.makeText(DetailsActivity.this, "call "+cid, Toast.LENGTH_SHORT).show();


                }

            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });

    }


    private void getNotificationCommentsDetails(String postId, int type) {
        Call<NewAPIResponseModel> call = null;
        call = apiInterface.GetPostDetailsByPostID(postId, type);
        call.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                if (response.body().getKnowledgeModel() != null) {
                    outDoorPostModel = response.body().getKnowledgeModel().get(0);
                    SetCommentData(outDoorPostModel);
                    mNotificationSlNo = outDoorPostModel.getSl_no();
                    getOutdoorComments(outDoorPostModel.getSl_no() + "");
                }

            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                Toast.makeText(DetailsActivity.this, "+t.getLocalizedMessage()", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private CommentsListAdapter.SelectItem selectItemInterface = new CommentsListAdapter.SelectItem() {
        @Override
        public void selectedPosition(CommentModel commentModel) {
            int type = 1;
            if (isKnowledgeComment) {
                type = 1;
            } else if (isKnowledgeGeneralComment) {
                type = 2;
            } else if (isAspirationComment) {
                type = 3;
            } else if (isAspirationGeneralComment) {
                type = 4;
            } else if (isOutdoorComment) {
                type = 5;
            }
            Intent intent = new Intent(DetailsActivity.this, PostSubCommentActivity.class);
            intent.putExtra("DATA", commentModel);
            intent.putExtra("TYPE", type);
            startActivity(intent);
            Log.d(TAG, "selectedPosition: " + type);
            //updateCommentCount(commentModel);
            // deleteComment(commentModel);
        }

        //delete
        @Override
        public void selectedItemPosition(final String position, final String desc, final String numOfComments, final String postBy, CommentModel commentModel) {
            int type = 1;
            if (isKnowledgeComment) {
                type = 1;
            } else if (isKnowledgeGeneralComment) {
                type = 2;
            } else if (isAspirationComment) {
                type = 3;
            } else if (isAspirationGeneralComment) {
                type = 4;
            } else if (isOutdoorComment) {
                type = 5;
            }
            String myDescription;
            final int finalType = type;
            String regex = "(https|http)://(media.giphy.com/media/)[a-zA-Z0-9]+(/giphy.gif)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(desc);
            if (matcher.find()) {
                myDescription = desc.replace(matcher.group(), "");
            } else {
                myDescription = desc;
            }
            new AlertDialog.Builder(DetailsActivity.this)
                    .setTitle("Delete Comment")
                    .setMessage(myDescription)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            DeleteSubComment(position, finalType + "", numOfComments, postBy, commentModel);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setCancelable(true)
                    .show();

        }

        @Override
        public void updateCommentLikeCount(CommentModel dataModel, boolean isLiked) {
            int type = 1;
            if (isKnowledgeComment) {
                type = 1;
            } else if (isKnowledgeGeneralComment) {
                type = 2;
            } else if (isAspirationComment) {
                type = 3;
            } else if (isAspirationGeneralComment) {
                type = 4;
            } else if (isOutdoorComment) {
                type = 5;
            }

            commonUpdateCommentLikeCount(dataModel, mCommentsListAdapter, apiInterface, commentModelList, type, isLiked);
            //  Toast.makeText(DetailsActivity.this, type + " " + dataModel.getSl_no(), Toast.LENGTH_SHORT).show();
            //  ContinentTools likes = new ContinentTools();
            //   likes.commonUpdateLikeCount(dataModel, mCommentsListAdapter, apiInterface, commentModelList, 1, isLiked);

        }

    };


    private void SetCommentData(OutDoorPostModel outDoorPostModel) {
        if (outDoorPostModel.getDescription() != null && outDoorPostModel.getDescription().trim().length() > 0) {
            mTextViewDescription.setText(capitalizeWordDes(outDoorPostModel.getDescription()));
        }
        if (outDoorPostModel.getPostedByName() != null && outDoorPostModel.getPostedByName().trim().length() > 0) { // TODO : udaya add
            //  mEditTextComment.setHint("You Comment on " + outDoorPostModel.getPostedByName() + " Post");
        }
        if (outDoorPostModel.getPostedByName() != null && outDoorPostModel.getPostedByName().trim().length() > 0) { // TODO : udaya add
            mUserNameComment.setText(capitalizeWord(outDoorPostModel.getPostedByName()));
        }
        if (outDoorPostModel.getMediaURL() != null && !outDoorPostModel.getMediaURL().isEmpty() && outDoorPostModel.getMediaType() == 1) {
            // mImageViewPost.setVisibility(View.VISIBLE);
            //  loadImage(outDoorPostModel.getMediaURL(), mImageViewPost);
        } else if (outDoorPostModel.getMediaURL() != null && !outDoorPostModel.getMediaURL().isEmpty()) {
            //  playerView.setVisibility(View.VISIBLE);
            mImageViewPost.setVisibility(View.GONE);
            //   initializePlayer(playerView, player, outDoorPostModel.getMediaURL());
        }
        if (outDoorPostModel.getNumShares() != 0)
            mTextViewShareCount.setText("" + outDoorPostModel.getNumShares());
        if (outDoorPostModel.getNumLikes() != 0) {
            mTextViewLikeCount.setText(outDoorPostModel.getNumLikes() + "");
        }
        if (outDoorPostModel.getNumComments() != 0) {
            mTextViewCommentCount.setText("" + outDoorPostModel.getNumComments());
        }
        String uid = SharedPrefsHelper.getInstance().get(USER_ID);
        if (outDoorPostModel.getPostedById().equalsIgnoreCase(uid)) {
            mLinearLayoutDeletePost.setVisibility(View.VISIBLE);
        }

        commentModel.setDescription(outDoorPostModel.getDescription());
        commentModel.setId(outDoorPostModel.getSl_no() + "");
        // commentModel.setKey(preferenceDataModel.getKey());
        commentModel.setImageURL(outDoorPostModel.getMediaURL());
        commentModel.setVideoURL(outDoorPostModel.getMediaURL());
        commentModel.setNumComments(outDoorPostModel.getNumComments());
        commentModel.setNumLikes(outDoorPostModel.getNumLikes());
        commentModel.setNumShares(outDoorPostModel.getNumShares());
        commentModel.setPostedBy(outDoorPostModel.getPostedById());
        commentModel.setTitle(outDoorPostModel.getTitle());
        add = outDoorPostModel.getNumComments();
    }

    private void GetCommentLikeListByUserID(int type1) {

        String userID = SharedPrefsHelper.getInstance().get(USER_ID);
        Log.d(TAG, "IDD" + userID);

        Call<NewAPIResponseModel> Call = apiInterface.GetCommentLikeListByUserID(userID, type1);
        Call.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(retrofit2.Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                if (response.isSuccessful() && response.body().getLikeDatalist() != null) {
                    likeDatalist = response.body().getLikeDatalist();
                    if (likeDatalist != null) {
                        if (mCommentsListAdapter != null) {
                            mCommentsListAdapter.notifyDataSetChanged();
                        }
                    }
                    //Toast.makeText(DetailsActivity.this, " "+usertable.size(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(retrofit2.Call<NewAPIResponseModel> call, Throwable t) {

            }
        });
    }


    private void DeleteSubComment(final String slno, String type, String numOfComments, String postBy, CommentModel commentModel2) {
        CommentModel commentModel = new CommentModel();
        //  Toast.makeText(this, " "+slno+" "+type+" "+numOfComments, Toast.LENGTH_SHORT).show();
        //getOutdoorComments(outDoorPostModel.getSl_no() + "");
        String postId = outDoorPostModel.getSl_no() + "";

        Call<NewAPIResponseModel> call123 = null;
        if (isNotificationData) {
            call123 = apiInterface.GetCommentsByPostId(postId, notificationDataModel.getType());
        } else {
            if (isKnowledgeComment) {
                cid = 1;
                GetCommentLikeListByUserID(1);
                call123 = apiInterface.GetCommentsByPostId(postId, 1);

            } else if (isKnowledgeGeneralComment) {
                cid = 2;
                GetCommentLikeListByUserID(2);
                call123 = apiInterface.GetCommentsByPostId(postId, 2);

            } else if (isAspirationComment) {
                cid = 3;
                GetCommentLikeListByUserID(3);
                call123 = apiInterface.GetCommentsByPostId(postId, 3);

            } else if (isAspirationGeneralComment) {
                cid = 4;
                GetCommentLikeListByUserID(4);
                call123 = apiInterface.GetCommentsByPostId(postId, 4);

            } else if (isOutdoorComment) {
                cid = 5;
                GetCommentLikeListByUserID(5);
                call123 = apiInterface.GetCommentsByPostId(postId, 5);

            }
        }
        call123.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {

                //   commentModelList = response.body().getCommentModel();
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse:MYCOUNT " + commentModelList.size());
                    int commentcount = response.body().getCommentModel().size();
                    outDoorPostModel.setNumComments(commentcount);
                    Log.d(TAG, "onResponse:setNumComments " + commentcount);

                    Call<NewAPIResponseModel> call23 = apiInterface.DeleteComment(commentModel2.getSl_no(), commentModel2.getId(), commentcount, Integer.parseInt(type));
                    Log.d(TAG, "DeleteSubComment: " + commentcount + " ");

                    call23.enqueue(new Callback<NewAPIResponseModel>() {
                        @Override
                        public void onResponse(retrofit2.Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                            if (response.isSuccessful() && response.body().isStatus()) {
                                for (int i = 0; i < commentModelList.size(); i++) {
                                    if (commentModelList.get(i).getSl_no().equalsIgnoreCase(slno)) {
                                        commentModel.setPost_sl_no(slno);
                                        deleteCommentCount = deleteCommentCount + 1;
                                        commentModel.setCommentCount((Integer.parseInt(numOfComments)) + "");
                                        commentModelList.remove(i);
                                        Toast.makeText(DetailsActivity.this, " " + deleteCommentCount, Toast.LENGTH_SHORT).show();
                                        mCommentsListAdapter.notifyItemRemoved(i);
                                        break;
                                    }
                                }
                            } else {
                                Toast.makeText(DetailsActivity.this, "Failed to Delete", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(retrofit2.Call<NewAPIResponseModel> call, Throwable t) {

                            Toast.makeText(DetailsActivity.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });

        // long finalsum=outDoorPostModel.getNumComments()-1;

    }


    public void commonUpdateCommentLikeCount(final CommentModel dataModel, final CommentsListAdapter PostAdapter, ApiInterface apiInterface, final List<CommentModel> TagDataModelList/*commentModelList*/, int type, boolean isLiked) {


        final String uid = SharedPrefsHelper.getInstance().get(USER_ID, null);

        final LikeModel likeModel = new LikeModel();
        likeModel.setLikeByID(uid);
        if (isLiked) {
            likeModel.setLikeCount(dataModel.getNumLikes() + "");
            likeModel.setPostId(dataModel.getSl_no());
            likeModel.setLikedByName(SharedPrefsHelper.getInstance().get(NAME));
         /*   likeModel.setPostedById(dataModel.getPostedById());
            likeModel.setPostedByName(dataModel.getPostedByName());
            likeModel.setLikedByName(dataModel.getLikedByName());*/
            Call<NewAPIResponseModel> call = apiInterface.AddCommentLike(likeModel, type);
            call.enqueue(new Callback<NewAPIResponseModel>() {
                @Override
                public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                    if (response.body().isStatus()) {
                        if (TagDataModelList != null) {
                            for (int i = 0; i < TagDataModelList.size(); i++) {
                                if (TagDataModelList.get(i).getSl_no().equalsIgnoreCase(dataModel.getSl_no())) {
                                    TagDataModelList.get(i).setNumLikes(TagDataModelList.get(i).getNumLikes() + 1);
                                    LikeDataModel likeDataModel = new LikeDataModel();
                                    likeDataModel.setLikeByID(uid);
                                    likeDataModel.setLikeCount((dataModel.getNumLikes() + 1) + "");
                                    likeDataModel.setPostID(dataModel.getSl_no());
                                    likeModel.setLikedByName(SharedPrefsHelper.getInstance().get(NAME));
                                    PostAdapter.notifyDataSetChanged();
                                    if (TagDataModelList.get(i).getLikesData() == null) {
                                        List<LikeDataModel> list = new ArrayList<>();
                                        list.add(likeDataModel);
                                        TagDataModelList.get(i).setLikesData(list);

                                    } else {

                                        TagDataModelList.get(i).getLikesData().add(likeDataModel);

                                    }
                                    break;
                                }
                                // mRecyclerView.stopScroll();
                                PostAdapter.notifyDataSetChanged();
                                //  Toast.makeText(getContext(), "  PostAdapter.notifyDataSetChanged() 2", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                }

                @Override
                public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                    Log.d("", "onFailure: " + t.getLocalizedMessage());
                }
            });
        } else {
            likeModel.setLikeCount((dataModel.getNumLikes()) + "");
            likeModel.setPostId(dataModel.getSl_no());
            likeModel.setLikedByName(SharedPrefsHelper.getInstance().get(NAME));
            Call<NewAPIResponseModel> call = apiInterface.CommentDisLike(likeModel, type);
            call.enqueue(new Callback<NewAPIResponseModel>() {
                @Override
                public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                    for (int i = 0; i < TagDataModelList.size(); i++) {
                        if (TagDataModelList.get(i).getSl_no().equalsIgnoreCase(dataModel.getSl_no())) {
                            if (TagDataModelList.get(i).getNumLikes() > 0) {
                                TagDataModelList.get(i).setNumLikes(TagDataModelList.get(i).getNumLikes() - 1);

                            }
                            /*else
                                {

                                    PostAdapter.notifyDataSetChanged();
                                }*/
                        }
                    }
                    PostAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {

                }
            });

        }








        /*final String uid = SharedPrefsHelper.getInstance().get(USER_ID, null);

        final LikeModel likeModel = new LikeModel();
        likeModel.setLikeByID(uid);
        if(isLiked)
        {
            likeModel.setLikeCount(dataModel.getNumLikes() + "");
            likeModel.setSl_no(dataModel.getSl_no());
            Call<NewAPIResponseModel> call = apiInterface.AddCommentLike(likeModel , type);
            call.enqueue(new Callback<NewAPIResponseModel>() {
                @Override
                public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                    if (response.body().isStatus()) {
                        if(TagDataModelList!=null) {
                            for (int i = 0; i < TagDataModelList.size(); i++) {
                                if (TagDataModelList.get(i).getSl_no().equalsIgnoreCase(dataModel.getSl_no())) {
                                    TagDataModelList.get(i).setNumLikes(TagDataModelList.get(i).getNumLikes() + 1);
                                    LikeDataModel likeDataModel = new LikeDataModel();
                                    likeDataModel.setLikeByID(uid);
                                    likeDataModel.setLikeCount((dataModel.getNumLikes() + 1) + "");
                                    likeDataModel.setSl_no(dataModel.getSl_no());
                                    PostAdapter.notifyDataSetChanged();
                                    if (TagDataModelList.get(i).getLikesData() == null) {
                                        List<LikeDataModel> list = new ArrayList<>();
                                        list.add(likeDataModel);
                                        TagDataModelList.get(i).setLikesData(list);

                                    } else {

                                        TagDataModelList.get(i).getLikesData().add(likeDataModel);

                                    }
                                    break;
                                }
                                PostAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                }
                @Override
                public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                    Log.d("", "onFailure: " + t.getLocalizedMessage());
                }
            });
        }
        else {
            likeModel.setLikeCount((dataModel.getNumLikes()) + "");
            likeModel.setSl_no(dataModel.getSl_no());
            Call<NewAPIResponseModel> call = apiInterface.CommentDisLike(likeModel , type);
            call.enqueue(new Callback<NewAPIResponseModel>() {
                @Override
                public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                    for(int i=0; i<TagDataModelList.size(); i++){
                        if(TagDataModelList.get(i).getSl_no().equalsIgnoreCase(dataModel.getSl_no())){
                            if(TagDataModelList.get(i).getNumLikes() > 0 ){
                                TagDataModelList.get(i).setNumLikes(TagDataModelList.get(i).getNumLikes() - 1);
                            }
                        }
                    }
                    PostAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {

                }
            });

        }

    }*/

    }

    public static String removeFirstChar(String s) {
        return s.substring(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContinentTools.hasInternetConnection(getApplicationContext())) {
            if (!isNotificationData) {
                getOutdoorComments(outDoorPostModel.getSl_no() + "");
                GetCommentLikeListByUserID(cid);
            } else {
                //Trending
                // getOutdoorComments(outDoorPostModel.getSl_no() + "");
                GetCommentLikeListByUserID(cid);
                if (mNotificationSlNo > 0) {
                    getOutdoorComments(outDoorPostModel.getSl_no() + "");
                }

            }
            if (mCommentsListAdapter != null) {
                mCommentsListAdapter.notifyDataSetChanged();
            }
        } else {
            ContinentTools.showToast("No internet Connection");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
      /*  InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);*/
        //  hideKeyboard(DetailsActivity.this);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        if (ContinentTools.hasInternetConnection(getApplicationContext())) {
            if (!isNotificationData) {
                getOutdoorComments(outDoorPostModel.getSl_no() + "");
                GetCommentLikeListByUserID(cid);
            } else {
                //Trending
                // getOutdoorComments(outDoorPostModel.getSl_no() + "");
                GetCommentLikeListByUserID(cid);
                if (mNotificationSlNo > 0) {
                    getOutdoorComments(outDoorPostModel.getSl_no() + "");
                }

              //  getOutdoorComments(outDoorPostModel.getSl_no() + "");
                GetCommentLikeListByUserID(cid);
            }
            if (mCommentsListAdapter != null) {
                mCommentsListAdapter.notifyDataSetChanged();
            }
        } else {
            ContinentTools.showToast("No internet Connection");
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        SharedPreferences.Editor editor = context.getSharedPreferences(DELETE_COMMENT_COUNT, MODE_PRIVATE).edit();
        editor.putLong("numComments", deleteCommentCount);
        editor.putLong("id", outDoorPostModel.getSl_no());
        editor.apply();
        deleteCommentCount = 0;
        add = 0;

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

    public static String capitalizeWordDes(String name){
        String firstLetter = name.substring(0, 1);
        String remainingLetters = name.substring(1, name.length());
        firstLetter = firstLetter.toUpperCase();
        name = firstLetter + remainingLetters;
        return name;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
