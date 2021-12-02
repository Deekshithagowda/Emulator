package com.sconti.studentcontinent.activity.post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.giphy.sdk.ui.GiphyFrescoHandler;
import com.giphy.sdk.ui.views.GiphyDialogFragment;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sconti.studentcontinent.ContinentApplication;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.LikedUserListActivity;
import com.sconti.studentcontinent.activity.alltabs.SecondMainActivity;
import com.sconti.studentcontinent.activity.alltabs.ui.knowledge.KnowledgeFragment;
import com.sconti.studentcontinent.activity.knowledge.fragments.FirstPreferenceFragment;
import com.sconti.studentcontinent.activity.newlogin.RegisterActivity;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorPostModel;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.model.APIResponseModel;
import com.sconti.studentcontinent.model.FirstPreferenceDataModel;
import com.sconti.studentcontinent.model.UserDetails;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;
import com.sconti.studentcontinent.utils.AppUtils;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;
import com.sconti.studentcontinent.utils.tools.ContinentTools;
import com.sconti.studentcontinent.utils.tools.PathUtils;
import com.sconti.studentcontinent.utils.tools.FilePath;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.ASPIRATION_FIRST;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.ASPIRATION_FIRST_GENERAL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.ASPIRATION_FIRST_POST_1;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.COLLEGE_NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.DATA_OF_COMMENT_AND_SLNO;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.DATA_OF_COMMENT_AND_SLNO3;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.KNOWLEDGE_FIRST;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.KNOWLEDGE_FIRST_GENERAL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.KNOWLEDGE_POST_1;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.OUTDOOR_FIRST;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.OUTDOOR_POST_1;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.PROFILE_URL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_ASPIRATION;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.UPDATE_KNOWLEDGE_POST_1;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.ui.GPHContentType;
import com.giphy.sdk.ui.Giphy;
import com.giphy.sdk.ui.GiphyFrescoHandler;
import com.giphy.sdk.ui.views.GiphyDialogFragment;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import okhttp3.OkHttpClient;
import timber.log.Timber;

public class PostActivity extends BaseActivity {
    boolean flag = false;
    private static final String TAG = "PostActivity";
    boolean switches = true;
    final int MAX_WORDS = 500;
    private static final int VIDEO_CAPTURE_18 = 18;
    private final int PICK_IMAGE_REQUEST = 71;

    private final int PICK_IMAGE_REQUEST_2 = 1111;
    private final int PICK_VIDEO_REQUEST_2 = 2222;
    private boolean isVideoSelected;
    OutDoorPostModel outDoorPostModel;
    private AlertDialog mAlertDialogFocusSelection;
    private LinearLayout mLinearLayoutMessage;
    private String keyword;
    String str, ssuid;
    ImageView thumbnail_imageView;
    // private Button delete;

    int PERMISSION_ALL = 1;
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

    private EditText mEditTextTitle, mEditTextDescription, meditTextDetailsCheck;
    private String mImageURL, mVideoURL;
    private TextView mButtonSubmit;
    private ImageView mImageViewAttachment, mImageViewVideoAttachment, mPostImage, mRemovePost, mRemoveVideo;
    private CardView mPostImageCardView, mPostVideoCardView;
    private DatabaseReference databaseReference;
    private String type, mSelectedSubject;
    private ActionBar mActionBar;
    private OutDoorPostModel mOutDoorPostModelData;
    private String Username;
    private TextView mUserNameAddPost;
    private VideoView mPlayerView;
    private LinearLayout mCancelButton;
    private int type2;

    private boolean isEdit;
    private File mediaFile;
    private CircleImageView mImageViewAvatar;
    private String name, profileUrl, BannerUrl;
    private ImageView imageViewGifOpen;
    public GiphyDialogFragment giphyDialogFragment;
    public GiphyFrescoHandler frescoHandler;
    private boolean isComingFromExternalApp = true;

    @Override
    protected int getContentView() {
        return R.layout.activity_post;

    }

    @Override
    protected void initView() {
        Log.d(TAG, "initView: ");
        mLinearLayoutMessage = findViewById(R.id.linearlayoutEditMessage);
        mEditTextTitle = findViewById(R.id.editTextTitle);
        mEditTextDescription = findViewById(R.id.editTextDetails);
        mEditTextDescription.setMovementMethod(LinkMovementMethod.getInstance());
        mEditTextDescription.setAutoLinkMask(Linkify.WEB_URLS);
        mButtonSubmit = findViewById(R.id.submitButton);
        mImageViewAttachment = findViewById(R.id.imageViewAttach);
        mImageViewVideoAttachment = findViewById(R.id.videoAttach);
        mPostImage = findViewById(R.id.PostImage);
        mPostImageCardView = findViewById(R.id.PostImageCardView);
        mUserNameAddPost = findViewById(R.id.UserNameAddPost);
        mPlayerView = findViewById(R.id.PostVideo);
        mPostVideoCardView = findViewById(R.id.PostVideoCardView);
        mCancelButton = findViewById(R.id.cancel_button);
        //delete=findViewById(R.id.delete);
        mRemovePost = findViewById(R.id.RemoveImageAddPost);
        thumbnail_imageView = findViewById(R.id.thumbnail);
        mRemoveVideo = findViewById(R.id.RemoveVideoPost);
        imageViewGifOpen = findViewById(R.id.gifopen);
        mImageViewAvatar = findViewById(R.id.imageViewProfile);
        // mRemoveVideo.setVisibility(GONE);


        //   meditTextDetailsCheck = findViewById(R.id.editTextDetailsCheck);


        outDoorPostModel = new OutDoorPostModel();

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

    @Override
    protected void initData() {
        isComingFromExternalApp = getIntent().getBooleanExtra(AppUtils.Constants.IS_COMING_FROM_EXT_APP, true);
        mActionBar = getSupportActionBar();
        profileUrl = SharedPrefsHelper.getInstance().get(PROFILE_URL);
        if (mActionBar != null) {
            mActionBar.setTitle("Post");
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (profileUrl != null && !profileUrl.isEmpty())
            Glide.with(context).load(profileUrl).placeholder(R.drawable.ic_user_profile).into(mImageViewAvatar);
        isEdit = getIntent().getBooleanExtra("IS_EDIT", false);
        type = getIntent().getStringExtra("TYPE");
        keyword = getIntent().getStringExtra("KEYWORD");
        mSelectedSubject = getIntent().getStringExtra("FAV");

        if (mSelectedSubject == null)
            mSelectedSubject = getIntent().getStringExtra("SUBJECT");
        mOutDoorPostModelData = getIntent().getParcelableExtra("OUTDOOR_DATA");
        if (mOutDoorPostModelData != null) {
            mEditTextDescription.setText(mOutDoorPostModelData.getDescription());
            mImageURL = mOutDoorPostModelData.getMediaURL();

            String uid = SharedPrefsHelper.getInstance().get(USER_ID, null);
            String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
            String name = SharedPrefsHelper.getInstance().get(NAME, null);
        }



          /*  Intent intent = getIntent();
            String action = intent.getAction();
            String type = intent.getType();
            if ("android.intent.action.SEND".equals(action) && type != null && "text/plain".equals(type)) {
                Log.println(Log.ASSERT, "shareablTextExtra", intent.getStringExtra("android.intent.extra.TEXT"));
                mEditTextDescription.setText(intent.getStringExtra("android.intent.extra.TEXT"));
            }*/

        Username = SharedPrefsHelper.getInstance().get(NAME);
        if (Username != null) {
            mUserNameAddPost.setText(capitalizeWord(Username));
        }
        try {
            if (type.equalsIgnoreCase(KNOWLEDGE_FIRST)) {
                type2 = 1;
            } else if (type.equalsIgnoreCase(ASPIRATION_FIRST)) {
                type2 = 3;
            } else if (type.equalsIgnoreCase(KNOWLEDGE_FIRST_GENERAL)) {
                type2 = 2;
            } else if (type.equalsIgnoreCase(ASPIRATION_FIRST_GENERAL)) {
                type2 = 4;
            } else {
                type2 = 5;
            }
        } catch (Exception e) {

        }

    }

    @Override
    protected void initListener() {

        imageViewGifOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImageURL != null) {
                    new AlertDialog.Builder(PostActivity.this)
                            .setTitle(R.string.info)
                            .setMessage(R.string.discard_post)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    GIF();

                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                } else {
                    GIF();
                }
            }


        });
        thumbnail_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thumbnail_imageView.setVisibility(GONE);
                thumbnail_imageView.setImageResource(0);
                mPlayerView.setVisibility(VISIBLE);
                mPlayerView.start();
            }
        });

        //  mRemovePost.setColorFilter(ContextCompat.getColor(this, R.color.red));


        mEditTextDescription.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // Nothing
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String[] words = s.toString().split(" "); // Get all words
                if (words.length > MAX_WORDS) {
                    // Trim words to length MAX_WORDS
                    // Join words into a String
                    //Toast.makeText(PostActivity.this, "Words", Toast.LENGTH_SHORT).show();
                    mEditTextDescription.setText(mEditTextDescription.getText().toString());
                }
            }
        });
        mRemoveVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPostImage.getDrawable() != null) {
                    new AlertDialog.Builder(PostActivity.this)
                            .setTitle(R.string.info)
                            .setMessage(R.string.do_you_want_to_delete)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    isVideoSelected = false;
                                    mVideoURL = null;
                                    mImageURL = null;
                                    mPostImage.setImageResource(0);
                                    mRemovePost.setVisibility(GONE);
                                    mPostImageCardView.setVisibility(GONE);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();

                } else if (mPlayerView.getVisibility() == VISIBLE) {

                    new AlertDialog.Builder(PostActivity.this)
                            .setTitle(R.string.info)
                            .setMessage(R.string.do_you_want_to_delete)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    isVideoSelected = false;
                                    mVideoURL = null;
                                    mImageURL = null;
                                    mRemoveVideo.setVisibility(GONE);
                                    mPlayerView.stopPlayback();
                                    mPlayerView.clearAnimation();
                                    mPlayerView.suspend();
                                    mPlayerView.setVideoURI(null);
                                    mPlayerView.setVisibility(GONE);
                                    mPlayerView.setBackgroundColor(getResources().getColor(R.color.white));
                                    mPlayerView.setBackgroundColor(Color.TRANSPARENT);
                                    mPostVideoCardView.setVisibility(GONE);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();


                }

            }
        });


        mRemovePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPostImage.getDrawable() != null) {
                    new AlertDialog.Builder(PostActivity.this)
                            .setTitle(R.string.info)
                            .setMessage(R.string.do_you_want_to_delete)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    isVideoSelected = false;
                                    mVideoURL = null;
                                    mImageURL = null;
                                    mPostImage.setImageResource(0);
                                    mRemovePost.setVisibility(GONE);
                                    mPostImageCardView.setVisibility(GONE);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();

                } else if (mPlayerView.getVisibility() == VISIBLE) {

                    new AlertDialog.Builder(PostActivity.this)
                            .setTitle(R.string.info)
                            .setMessage(R.string.do_you_want_to_delete)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    isVideoSelected = false;
                                    mVideoURL = null;
                                    mImageURL = null;
                                    mRemovePost.setVisibility(GONE);
                                    mPlayerView.stopPlayback();
                                    mPlayerView.clearAnimation();
                                    mPlayerView.suspend();
                                    mPlayerView.setVideoURI(null);
                                    mPlayerView.setVisibility(GONE);
                                    mPlayerView.setBackgroundColor(getResources().getColor(R.color.white));
                                    mPlayerView.setBackgroundColor(Color.TRANSPARENT);
                                    mPostVideoCardView.setVisibility(GONE);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();


                }
            }
        });

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonSubmit.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mButtonSubmit.setClickable(true);
                    }
                }, 5000);

            //     Toast.makeText(PostActivity.this, "Post Activity", Toast.LENGTH_SHORT).show();
                final String ID = AppUtils.extractYTId(mEditTextDescription.getText().toString());
                String desc = mEditTextDescription.getText().toString().trim();
//                    if(  filePath != null && ID.length() >= YouTubeID ) {
//                        Toast.makeText(PostActivity.this, "sorry", Toast.LENGTH_SHORT).show();
//                    }else {


                if (isComingFromExternalApp) {

                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PostActivity.this
                            , R.style.BottomSheetDialogThem);
                    View bottomSheetView = LayoutInflater.from(PostActivity.this)
                            .inflate(R.layout.outdoor_more_alert,
                                   findViewById(R.id.BottomSheetDialogUserDost));


                    TextView KnowledgePost, AspirationPost, OutdoorPost , report, copyLink, share, delete, onNotification, edit;

                    report = bottomSheetView.findViewById(R.id.Report);
                    copyLink = bottomSheetView.findViewById(R.id.copy_link);
                    share = bottomSheetView.findViewById(R.id.share_to);
                    delete = bottomSheetView.findViewById(R.id.delete);
                    onNotification = bottomSheetView.findViewById(R.id.turn_on_post_noti);
                    edit = bottomSheetView.findViewById(R.id.edit);
                    KnowledgePost = bottomSheetView.findViewById(R.id.KnowledgePost);
                    AspirationPost = bottomSheetView.findViewById(R.id.AspirationPost);
                    OutdoorPost = bottomSheetView.findViewById(R.id.OutdoorPost);

                    report.setVisibility(View.GONE);
                    copyLink.setVisibility(View.GONE);
                    share.setVisibility(View.GONE);
                    delete.setVisibility(View.GONE);
                    onNotification.setVisibility(View.GONE);
                    edit.setVisibility(View.GONE);

                    KnowledgePost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            KnowledgePost.setClickable(false);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    KnowledgePost.setClickable(true);
                                }
                            }, 10000);
                            type = getIntent().getStringExtra("TYPE");
                            if (TextUtils.isEmpty(type)) {
                                type = AppUtils.Constants.KNOWLEDGE_FIRST;
                                mSelectedSubject = SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE, null);
                                type2 = 1;
                                if (type.equalsIgnoreCase(AppUtils.Constants.KNOWLEDGE_FIRST)) {
                                    submitKnowledgePost();
                                }
                            }
                        }
                    });

                    AspirationPost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AspirationPost.setClickable(false);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AspirationPost.setClickable(true);
                                }
                            }, 10000);
                            type = getIntent().getStringExtra("TYPE");
                            if (TextUtils.isEmpty(type)) {
                                type = AppUtils.Constants.ASPIRATION_FIRST;
                                mSelectedSubject = SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_ASPIRATION, null);
                                type2 = 3;
                                if (type.equalsIgnoreCase(AppUtils.Constants.ASPIRATION_FIRST)) {
                                    submitAspirationPost();
                                }
                            }
                        }
                    });


                    OutdoorPost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OutdoorPost.setClickable(false);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    OutdoorPost.setClickable(true);
                                }
                            }, 10000);
                            type = getIntent().getStringExtra("TYPE");
                            if (TextUtils.isEmpty(type)) {
                                type = OUTDOOR_FIRST;
                                type2 = 5;
                                submitOutDoorPost();
                            }
                        }
                    });

                    bottomSheetDialog.setContentView(bottomSheetView);
                    bottomSheetDialog.show();
//                    String[] category = {"Knowledge", "Aspiration", "Outdoor"};
//                    AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
//                    builder.setTitle("Choose category");
//                    builder.setCancelable(false);
//                    builder.setItems(category, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int i) {
//                            // Toast.makeText(PostActivity.this, " "+i, Toast.LENGTH_SHORT).show();
//                            if (i == 0) {
//                                type = getIntent().getStringExtra("TYPE");
//                                if (TextUtils.isEmpty(type)) {
//                                    type = AppUtils.Constants.KNOWLEDGE_FIRST;
//                                    mSelectedSubject = SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE, null);
//                                    type2 = 1;
//                                    if (type.equalsIgnoreCase(AppUtils.Constants.KNOWLEDGE_FIRST)) {
//                                        submitKnowledgePost();
//                                    }
//                                }
//                            } else if (i == 1) {
//                                type = getIntent().getStringExtra("TYPE");
//                                if (TextUtils.isEmpty(type)) {
//                                    type = AppUtils.Constants.ASPIRATION_FIRST;
//                                    mSelectedSubject = SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_ASPIRATION, null);
//                                    type2 = 3;
//                                    if (type.equalsIgnoreCase(AppUtils.Constants.ASPIRATION_FIRST)) {
//                                        submitAspirationPost();
//                                    }
//                                }
//                            } else if (i == 2) {
//                                type = getIntent().getStringExtra("TYPE");
//                                if (TextUtils.isEmpty(type)) {
//                                    type = OUTDOOR_FIRST;
//                                    type2 = 5;
//                                    submitOutDoorPost();
//                                }
//                            }
//                        }
//                    });
//                    builder.show();
                } else {
                    if ((desc.length() != 0 && !desc.trim().isEmpty()) || (/*mImageURL != null && !mImageURL.isEmpty()*/mPostImage.getDrawable() != null && (desc.length() != 0)) || (mVideoURL != null && !mVideoURL.isEmpty() && (desc.length() != 0))) {
                        if (!isEdit) {
                            if (type.equalsIgnoreCase(AppUtils.Constants.KNOWLEDGE_FIRST)) {
                                submitKnowledgePost();
                            } else if (type.equalsIgnoreCase(AppUtils.Constants.ASPIRATION_FIRST)) {
                                submitAspirationPost();
                            } else if (type.equalsIgnoreCase(AppUtils.Constants.ASPIRATION_FIRST_GENERAL)) {
                                submitAspirationGeneralPost();
                            } else if (type.equalsIgnoreCase(AppUtils.Constants.KNOWLEDGE_FIRST_GENERAL)) {
                                submitKnowledgeGeneralPost();
                            } else if (!type.equalsIgnoreCase(OUTDOOR_FIRST)) {
                                submitData();
                            } else if (mOutDoorPostModelData != null) {
                                editOutDoorPost(mOutDoorPostModelData);
                            } else {
                                submitOutDoorPost();
                            }
                        } else {
                            editOutDoorPost(mOutDoorPostModelData);
                        }
                    } else {
                        Toast.makeText(PostActivity.this, "Please write description", Toast.LENGTH_LONG).show();
                    }
                }


            }

        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mImageViewAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isVideoSelected = false;
                if (mImageURL != null) {
                    //existing image
                    new AlertDialog.Builder(PostActivity.this)
                            .setTitle(R.string.info)
                            .setMessage(R.string.discard_post)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    addPhoto();

                                    switches = false;

                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                } else if (mVideoURL != null) {
                    new AlertDialog.Builder(PostActivity.this)
                            .setTitle(R.string.info)
                            .setMessage(R.string.discard_post)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    addPhoto();
                                    mVideoURL = null;
                                    switches = false;
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                } else if (mImageURL == null || mVideoURL == null) {
                    addPhoto();
                    switches = true;
                }

            }
        });

        mImageViewVideoAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isVideoSelected = true;
                if (mVideoURL != null) {
                    //existing Video
                    new AlertDialog.Builder(PostActivity.this)
                            .setTitle(R.string.info)
                            .setMessage(R.string.discard_post)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    addVideo();
                                    switches = true;
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                } else if (mImageURL != null) {
                    new AlertDialog.Builder(PostActivity.this)
                            .setTitle(R.string.info)
                            .setMessage(R.string.discard_post)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    addVideo();
                                    mImageURL = null;

                                    switches = false;
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                } else if (mVideoURL == null || mImageURL == null) {
                    addVideo();
                    switches = true;
                }


            }
        });

        mEditTextDescription.addTextChangedListener(YouTubeIdCheck);

    }

    TextWatcher YouTubeIdCheck = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            final String ID = AppUtils.extractYTId(mEditTextDescription.getText().toString());
            //String YouTubeLink = mEditTextDescription.getText().toString().trim();

            mImageViewAttachment.setEnabled(ID == null);
            mImageViewVideoAttachment.setEnabled(ID == null);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void editOutDoorPost(OutDoorPostModel outDoorPostModel) {
        outDoorPostModel.setTitle(mEditTextTitle.getText().toString());
        outDoorPostModel.setDescription(mEditTextDescription.getText().toString());
        outDoorPostModel.setMediaURL(mImageURL);

        String uid = SharedPrefsHelper.getInstance().get(USER_ID, null);
        String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
        String name = SharedPrefsHelper.getInstance().get(NAME, null);

        if (isVideoSelected)
            outDoorPostModel.setMediaType(3);
        else
            outDoorPostModel.setMediaType(1);

        outDoorPostModel.setPostedById(uid);
        outDoorPostModel.setUpdated_on(System.currentTimeMillis());
        // outDoorPostModel.setCreated_on(System.currentTimeMillis());
        //  outDoorPostModel.setCategory("game");
        // outDoorPostModel.setCategory_id("dummy");
        outDoorPostModel.setPrfurl(prfurl);
        outDoorPostModel.setPostedByName(name);
        updateOutDoorPost(outDoorPostModel);
        str = mEditTextDescription.getText().toString();


        SharedPreferences.Editor editor = context.getSharedPreferences(UPDATE_KNOWLEDGE_POST_1, MODE_PRIVATE).edit();
        editor.putString("edit", mEditTextDescription.getText().toString());
        editor.putLong("postid", outDoorPostModel.getSl_no());
        editor.apply();

    }

    private void submitAspirationGeneralPost() {
        OutDoorPostModel outDoorPostModel = new OutDoorPostModel();
        outDoorPostModel.setTitle(mEditTextTitle.getText().toString());
        outDoorPostModel.setDescription(mEditTextDescription.getText().toString());
        String uid = SharedPrefsHelper.getInstance().get(USER_ID, null);
        String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
        String name = SharedPrefsHelper.getInstance().get(NAME, null);
        String college = SharedPrefsHelper.getInstance().get(COLLEGE_NAME, null);

        if (isVideoSelected) {
            outDoorPostModel.setMediaType(3);
            outDoorPostModel.setMediaURL(mVideoURL);
        } else {
            outDoorPostModel.setMediaType(1);
            outDoorPostModel.setMediaURL(mImageURL);
        }
        outDoorPostModel.setPostedById(uid);
        outDoorPostModel.setUpdated_on(System.currentTimeMillis());
        outDoorPostModel.setCreated_on(System.currentTimeMillis());
        outDoorPostModel.setCategory("General");
        outDoorPostModel.setCategory_id("General");
        outDoorPostModel.setPrfurl(prfurl);
        outDoorPostModel.setPostedByCollege(college);
        outDoorPostModel.setPostedByName(name);
        callNetworkAPI(outDoorPostModel, type);
    }

    private void submitKnowledgeGeneralPost() {
        OutDoorPostModel outDoorPostModel = new OutDoorPostModel();
        outDoorPostModel.setTitle(mEditTextTitle.getText().toString());
        outDoorPostModel.setDescription(mEditTextDescription.getText().toString());
        String uid = SharedPrefsHelper.getInstance().get(USER_ID, null);
        String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
        String name = SharedPrefsHelper.getInstance().get(NAME, null);
        String college = SharedPrefsHelper.getInstance().get(COLLEGE_NAME, null);

        if (isVideoSelected) {
            outDoorPostModel.setMediaType(3);
            outDoorPostModel.setMediaURL(mVideoURL);
        } else {
            outDoorPostModel.setMediaType(1);
            outDoorPostModel.setMediaURL(mImageURL);
        }

        outDoorPostModel.setPostedById(uid);

        outDoorPostModel.setUpdated_on(System.currentTimeMillis());
        outDoorPostModel.setCreated_on(System.currentTimeMillis());
        outDoorPostModel.setCategory("General");
        outDoorPostModel.setCategory_id("General");
        outDoorPostModel.setPrfurl(prfurl);
        outDoorPostModel.setPostedByCollege(college);
        outDoorPostModel.setPostedByName(name);
        callNetworkAPI(outDoorPostModel, type);
    }

    private void submitKnowledgePost() {
        if (keyword != null) {
            outDoorPostModel.setKeyWord(keyword);
        }
        outDoorPostModel.setTitle(mEditTextTitle.getText().toString());
        outDoorPostModel.setDescription(mEditTextDescription.getText().toString());
        String uid = SharedPrefsHelper.getInstance().get(USER_ID, null);
        String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
        String name = SharedPrefsHelper.getInstance().get(NAME, null);
        String college = SharedPrefsHelper.getInstance().get(COLLEGE_NAME, null);
        if (isVideoSelected) {
            outDoorPostModel.setMediaType(3);
            outDoorPostModel.setMediaURL(mVideoURL);
        } else {
            outDoorPostModel.setMediaType(1);
            outDoorPostModel.setMediaURL(mImageURL);
        }

        outDoorPostModel.setPostedById(uid);
        outDoorPostModel.setUpdated_on(System.currentTimeMillis());
        outDoorPostModel.setCreated_on(System.currentTimeMillis());
        outDoorPostModel.setCategory(mSelectedSubject);
        outDoorPostModel.setCategory_id(mSelectedSubject);
        outDoorPostModel.setPrfurl(prfurl);
        outDoorPostModel.setPostedByCollege(college);
        outDoorPostModel.setPostedByName(name);
        callNetworkAPI(outDoorPostModel, type);
    }

    private void submitAspirationPost() {
        OutDoorPostModel outDoorPostModel = new OutDoorPostModel();
        outDoorPostModel.setTitle(mEditTextTitle.getText().toString());
        outDoorPostModel.setDescription(mEditTextDescription.getText().toString());
        String uid = SharedPrefsHelper.getInstance().get(USER_ID, null);
        String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
        String name = SharedPrefsHelper.getInstance().get(NAME, null);
        String college = SharedPrefsHelper.getInstance().get(COLLEGE_NAME, null);
        if (isVideoSelected) {
            outDoorPostModel.setMediaType(3);
            outDoorPostModel.setMediaURL(mVideoURL);
        } else {
            outDoorPostModel.setMediaType(1);
            outDoorPostModel.setMediaURL(mImageURL);
        }
        outDoorPostModel.setPostedById(uid);
        outDoorPostModel.setUpdated_on(System.currentTimeMillis());
        outDoorPostModel.setCreated_on(System.currentTimeMillis());
        outDoorPostModel.setCategory(mSelectedSubject);
        outDoorPostModel.setCategory_id(mSelectedSubject);
        outDoorPostModel.setPrfurl(prfurl);
        outDoorPostModel.setPostedByCollege(college);
        outDoorPostModel.setPostedByName(name);
        callNetworkAPI(outDoorPostModel, type);
    }

    private void submitOutDoorPost() {
        OutDoorPostModel outDoorPostModel = new OutDoorPostModel();
        outDoorPostModel.setTitle(mEditTextTitle.getText().toString());
        outDoorPostModel.setDescription(mEditTextDescription.getText().toString());
        String uid = SharedPrefsHelper.getInstance().get(USER_ID, null);
        String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
        String name = SharedPrefsHelper.getInstance().get(NAME, null);
        String college = SharedPrefsHelper.getInstance().get(COLLEGE_NAME, null);
        if (isVideoSelected) {
            outDoorPostModel.setMediaType(3);
            outDoorPostModel.setMediaURL(mVideoURL);
        } else {
            outDoorPostModel.setMediaType(1);
            outDoorPostModel.setMediaURL(mImageURL);
        }
        outDoorPostModel.setPostedById(uid);
        outDoorPostModel.setUpdated_on(System.currentTimeMillis());
        outDoorPostModel.setCreated_on(System.currentTimeMillis());
        outDoorPostModel.setCategory("Default");
        outDoorPostModel.setCategory_id("Default_1");
        outDoorPostModel.setPrfurl(prfurl);
        outDoorPostModel.setPostedByCollege(college);
        outDoorPostModel.setPostedByName(name);
        callNetworkAPI(outDoorPostModel, type);
    }

    protected ApiInterface apiInterface;

    private void setUpNetwork() {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }

    private void callNetworkAPI(OutDoorPostModel userDetails, String type) {
        setUpNetwork();
        Call<APIResponseModel> apiResponseModelCall = null;
        if (userDetails.getMediaURL() == null) {
            userDetails.setMediaURL("");
        }
        userDetails.setCreated_on(System.currentTimeMillis());
        userDetails.setUpdated_on(System.currentTimeMillis());
        if (type.equalsIgnoreCase(KNOWLEDGE_FIRST)) {
            SharedPreferences.Editor editor = context.getSharedPreferences(KNOWLEDGE_POST_1, MODE_PRIVATE).edit();
            editor.putBoolean("myBool", true);
            editor.apply();
            apiResponseModelCall = apiInterface.AddPost(userDetails, 1);


        } else if (type.equalsIgnoreCase(ASPIRATION_FIRST)) {
            SharedPreferences.Editor editor = context.getSharedPreferences(ASPIRATION_FIRST_POST_1, MODE_PRIVATE).edit();
            editor.putBoolean("myBool", true);
            editor.apply();
            apiResponseModelCall = apiInterface.AddPost(userDetails, 3);
        } else if (type.equalsIgnoreCase(KNOWLEDGE_FIRST_GENERAL)) {
            apiResponseModelCall = apiInterface.AddPost(userDetails, 2);
        } else if (type.equalsIgnoreCase(ASPIRATION_FIRST_GENERAL)) {
            apiResponseModelCall = apiInterface.AddPost(userDetails, 4);
        } else {
            SharedPreferences.Editor editor = context.getSharedPreferences(OUTDOOR_POST_1, MODE_PRIVATE).edit();
            editor.putBoolean("myBool", true);
            editor.apply();
            apiResponseModelCall = apiInterface.AddPost(userDetails, 5);
        }
        apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                if (response.isSuccessful() && response.body().isStatus()) {
                    APIResponseModel apiResponseModel = new APIResponseModel();
                    finish();

                } else {
                    AppUtils.showToastMessage(PostActivity.this, getString(R.string.already_registered));
                }
            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                AppUtils.showToastMessage(PostActivity.this, getString(R.string.some_thing_wrong));
            }
        });
    }

    private void updateOutDoorPost(OutDoorPostModel userDetails) {
        setUpNetwork();
        try {
            Call<APIResponseModel> apiResponseModelCall = apiInterface.EditPost(userDetails, type2);
            apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
                @Override
                public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                    if (response.isSuccessful() && response.body().isStatus()) {
                        APIResponseModel apiResponseModel = new APIResponseModel();
                        //  Toast.makeText(PostActivity.this, +"", Toast.LENGTH_SHORT).show();

                        finish();
                    } else {
                        AppUtils.showToastMessage(PostActivity.this, getString(R.string.not_able_to_update));
                    }
                }

                @Override
                public void onFailure(Call<APIResponseModel> call, Throwable t) {
                    AppUtils.showToastMessage(PostActivity.this, getString(R.string.some_thing_wrong));
                }
            });
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
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

    private void submitData() {
        String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
        FirstPreferenceDataModel firstPreferenceDataModel;
        firstPreferenceDataModel = new FirstPreferenceDataModel();
        firstPreferenceDataModel.setTitle(mEditTextTitle.getText().toString());
        firstPreferenceDataModel.setDescription(mEditTextDescription.getText().toString());
        firstPreferenceDataModel.setDatetime(System.currentTimeMillis());
        String name = SharedPrefsHelper.getInstance().get(NAME, null);
        firstPreferenceDataModel.setImageURL(mImageURL);
        firstPreferenceDataModel.setVideoURL(mVideoURL);
        firstPreferenceDataModel.setPostedByName(name);
        String college = SharedPrefsHelper.getInstance().get(COLLEGE_NAME, null);
        firstPreferenceDataModel.setPostedByCollege(college);
        String uid = SharedPrefsHelper.getInstance().get(USER_ID, null);
        firstPreferenceDataModel.setPostedBy(uid);
        databaseReference = ContinentApplication.getFireBaseRef().child(type).child(mSelectedSubject);
        String key = databaseReference.push().getKey();
        firstPreferenceDataModel.setId(key);
        firstPreferenceDataModel.setKey(key);
        firstPreferenceDataModel.setPosterUrl(prfurl);
        databaseReference.child(key).setValue(firstPreferenceDataModel);
        mEditTextDescription.setText("");
        finish();
    }

    private void addPhoto() {

        if (!ContinentTools.hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        selectImage();
    }

    private void addVideo() {
        if (!ContinentTools.hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        selectVideo();
    }

    private String userChoosenTask;

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Attachment!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean gallery = ContinentTools.checkPermission(PostActivity.this);
                boolean camera = ContinentTools.checkPermissionCamera(PostActivity.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
        builder.setTitle("Select Attachment!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean gallery = ContinentTools.checkPermission(PostActivity.this);
                boolean camera = ContinentTools.checkPermissionCamera(PostActivity.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
        builder.setTitle("Select Attachment!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean gallery = ContinentTools.checkPermission(PostActivity.this);
                boolean camera = ContinentTools.checkPermissionCamera(PostActivity.this);
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
        if (getPackageManager().hasSystemFeature(
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
            // Toast.makeText(this, "Video capture intent", Toast.LENGTH_SHORT).show();
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


    private void videoCaptureIntent() {
        if (!ContinentTools.hasInternetConnection(this)) {
            ContinentTools.showToast("No Internet connected");
            //   Toast.makeText(this, "Video capture intent 1", Toast.LENGTH_SHORT).show();
            // return;
        }

        /*Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, VIDEO_CAPTURE_18);*/

        File mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/StudentContinent.mp4");
        Intent intent123 = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Uri videoUri = Uri.fromFile(mediaFile);
        intent123.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        long size = 30 * 1024 * 1024;
        intent123.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); //high Quality
        intent123.putExtra(MediaStore.EXTRA_SIZE_LIMIT, size);  //30MB
        intent123.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 120); //2 min value in seconds

        startActivityForResult(intent123, VIDEO_CAPTURE_18);
        //  mPostVideoCardView.setVisibility(VISIBLE);
    /*    mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp4");
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Uri videoUri = Uri.fromFile(mediaFile);
        mPostVideoCardView.setVisibility(VISIBLE);
        mPostImageCardView.setVisibility(GONE);
        thumbnail_imageView.setVisibility(VISIBLE);
        mPlayerView.setVideoURI(videoUri);
       thumbnail_imageView.setImageBitmap(ThumbnailUtils.createVideoThumbnail(PathUtils.getPathFromUri(this,videoUri),
                MediaStore.Video.Thumbnails.MICRO_KIND));
        MediaController mediaController = new MediaController(this);
        mPlayerView.setMediaController(mediaController);
        mediaController.setAnchorView(mPlayerView);
        *//*if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            videoUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", mediaFile);
        }*//*
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        //intent.putExtra(EXTRA_VIDEO_QUALITY, 0);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, VIDEO_CAPTURE);
        }*/
    }


    private void audioIntent() {
        if (!ContinentTools.checkInternetStatus()) {
            ContinentTools.showToast("No Internet connected");
            // Toast.makeText(this, "Video capture intent 2", Toast.LENGTH_SHORT).show();
            //  return;
        }
        //Intent intent = new Intent(mActivity, AudioRecordActivity.class);
        //startActivityForResult(intent, RQS_RECORDING);
    }

    private void AudioLocalIntent() {
        if (!ContinentTools.checkInternetStatus()) {
            ContinentTools.showToast("No Internet connected");
            //  Toast.makeText(this, "Video capture intent 3", Toast.LENGTH_SHORT).show();
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
                String filePath = FilePath.getPathFromInputStreamUri(PostActivity.this, path);

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
                String filePath = getRealPathFromUri(PostActivity.this, path);
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
                        PostActivity.this,
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
            Bitmap d = new BitmapDrawable(getResources(), original.getPath()).getBitmap();
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
        mPostVideoCardView.setVisibility(GONE);
        mPostImageCardView.setVisibility(VISIBLE);
        mRemoveVideo.setVisibility(VISIBLE);
        mRemovePost.setVisibility(VISIBLE);


        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        mPostImage.setImageBitmap(thumbnail);

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

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri filePath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null)
            filePath = data.getData();
//         imageViewUploaded.setImageURI(filePath);
//         imageViewUploaded.setVisibility(View.VISIBLE);

        if (requestCode == PICK_VIDEO_REQUEST_2 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            //Gallery video
            filePath = data.getData();

            uploadImage(filePath);
            //  uploadImage(filePath);
            mPostImageCardView.setVisibility(GONE);
            thumbnail_imageView.setVisibility(VISIBLE);
            mPostVideoCardView.setVisibility(VISIBLE);

            mRemoveVideo.setVisibility(VISIBLE);
            mRemovePost.setVisibility(VISIBLE);
            mPlayerView.setVideoURI(filePath);

            thumbnail_imageView.setImageBitmap(ThumbnailUtils.createVideoThumbnail(PathUtils.getPathFromUri(this, filePath),
                    MediaStore.Video.Thumbnails.MICRO_KIND));


            MediaController mediaController = new MediaController(this);
            mPlayerView.setMediaController(mediaController);
            mediaController.setAnchorView(mPlayerView);
/*
            uploadImage(filePath);
          //  uploadImage(filePath);
            mPostVideoCardView.setVisibility(VISIBLE);
            mPostImageCardView.setVisibility(GONE);
            mPlayerView.setVideoURI(filePath);
            MediaController mediaController = new MediaController(this);
            mPlayerView.setMediaController(mediaController);
            mediaController.setAnchorView(mPlayerView);*/

//              imageViewUploaded.setImageURI(filePath);
//             imageViewUploaded.setVisibility(View.VISIBLE);
//               if(filePath!=null)
//              uploadImage();
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST_2) {
                //Image from gallery
                uploadImage(filePath);
                mPostVideoCardView.setVisibility(GONE);
                mPostImageCardView.setVisibility(VISIBLE);
                mRemovePost.setVisibility(VISIBLE);
                mRemoveVideo.setVisibility(VISIBLE);
                // mRemoveVideo.setVisibility(GONE);


                // mRemovePost.setVisibility(VISIBLE);

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    mPostImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } /*else if (requestCode == PICK_VIDEO_REQUEST_2) {
                uploadImage(filePath);
              //  uploadImage(filePath);
                mPostImageCardView.setVisibility(GONE);
                thumbnail_imageView.setVisibility(VISIBLE);
                mPostVideoCardView.setVisibility(VISIBLE);
                mRemovePost.setVisibility(VISIBLE);
                mPlayerView.setVideoURI(filePath);
                thumbnail_imageView.setImageBitmap(ThumbnailUtils.createVideoThumbnail(PathUtils.getPathFromUri(this,filePath),
                        MediaStore.Video.Thumbnails.MICRO_KIND));
                MediaController mediaController = new MediaController(this);
                mPlayerView.setMediaController(mediaController);
                mediaController.setAnchorView(mPlayerView);
            }*/ else if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data, "IMAGE");
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
            else if (requestCode == SELECT_VIDEO)
                onSelectFromGalleryResult(data, "VIDEO");
            else if (requestCode == REQUEST_CODE_DOC) {
                onSelectFromGalleryResult(data, "ALL");
            } else if (requestCode == AUDIO_LOCAL) {
                onSelectFromGalleryResult(data, "AUDIO");
            } else if (requestCode == VIDEO_CAPTURE_18) {
                if (resultCode == RESULT_OK) {
                   /* Toast.makeText(this, "Video saved to:\n" +
                            data.getData(), Toast.LENGTH_LONG).show();*/
                    //captured video from camera
                    mPostImageCardView.setVisibility(GONE);
                    // mRemovePost.setVisibility(GONE);
                    thumbnail_imageView.setVisibility(VISIBLE);
                    mPostVideoCardView.setVisibility(VISIBLE);
                    mRemovePost.setVisibility(VISIBLE);
                    mRemoveVideo.setVisibility(VISIBLE);

                    // mRemoveVideo.setVisibility(VISIBLE);
                    uploadImage(data.getData());

                    mPlayerView.setVideoURI(data.getData());


                    thumbnail_imageView.setImageBitmap(ThumbnailUtils.createVideoThumbnail(PathUtils.getPathFromUri(this, data.getData()),
                            MediaStore.Video.Thumbnails.MICRO_KIND));

                    MediaController mediaController = new MediaController(this);
                    mPlayerView.setMediaController(mediaController);
                    mediaController.setAnchorView(mPlayerView);


                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Video recording cancelled.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Failed to record video",
                            Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == RQS_RECORDING) {
                String result = data.getStringExtra("result");


                if (result != null) {
                    // uploadImageToAWS(new File(result), "AUDIO");
                } else {

                }
            } else if (requestCode == VIDEO_CAPTURE) {
                Log.d(TAG, "onActivityResult: ");
                uploadImage(Uri.fromFile(mediaFile));
            }
        }
    }

    private void uploadImage(Uri filePath) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if (filePath != null) {
            final StorageReference ref = storageReference.child("images").child(type).child(mSelectedSubject).child(UUID.randomUUID().toString());
            final UploadTask uploadTask = ref.putFile(filePath);
            final ProgressDialog progressDialog = new ProgressDialog(PostActivity.this);
            progressDialog.setTitle(R.string.Uploading);
            progressDialog.setCancelable(false);
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, PostActivity.this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!uploadTask.isComplete()) {
                        Toast.makeText(PostActivity.this, "Upload cancelled", Toast.LENGTH_SHORT).show();
                        uploadTask.cancel();
                        progressDialog.dismiss();
                        progressDialog.cancel();
                        //imageview
                        isVideoSelected = false;
                        mVideoURL = null;
                        mImageURL = null;
                        mPostImage.setImageResource(0);
                        mRemovePost.setVisibility(GONE);
                        mRemoveVideo.setVisibility(GONE);
                        mPostImageCardView.setVisibility(GONE);
                        //video
                        isVideoSelected = false;
                        mVideoURL = null;
                        mImageURL = null;
                        mPostVideoCardView.setVisibility(GONE);
                        mRemovePost.setVisibility(GONE);
                        mRemoveVideo.setVisibility(GONE);
                        mPlayerView.stopPlayback();
                        mPlayerView.clearAnimation();
                        mPlayerView.suspend();
                        mPlayerView.setVideoURI(null);
                        mPlayerView.setVisibility(GONE);
                        mPlayerView.setBackgroundColor(getResources().getColor(R.color.white));
                        mPlayerView.setBackgroundColor(Color.TRANSPARENT);
                    }
                    if (progressDialog.isShowing()/*&& progressDialog!=null*/) {
                        progressDialog.dismiss();
                        progressDialog.cancel();
                    }
                    progressDialog.dismiss();//dismiss dialog

                    progressDialog.dismiss();
                    progressDialog.cancel();
                    if (mPostImage.getDrawable() != null) {
                        if (progressDialog.isShowing()/*||progressDialog!=null*/) {
                            progressDialog.dismiss();
                            progressDialog.hide();
                            progressDialog.cancel();
                        }

                        isVideoSelected = false;
                        mVideoURL = null;
                        mImageURL = null;
                        mPostImage.setImageResource(0);
                        mRemovePost.setVisibility(GONE);
                        mRemoveVideo.setVisibility(GONE);
                        mPostImageCardView.setVisibility(GONE);
                    } else if (mPlayerView.getVisibility() == VISIBLE) {
                        //Toast.makeText(PostActivity.this, "Video", Toast.LENGTH_SHORT).show();
                        if (progressDialog.isShowing()/*||progressDialog!=null*/) {
                            progressDialog.dismiss();
                            progressDialog.hide();
                            progressDialog.cancel();
                        }

                        isVideoSelected = false;
                        mVideoURL = null;
                        mImageURL = null;
                        mPostVideoCardView.setVisibility(GONE);
                        mRemovePost.setVisibility(GONE);
                        mRemoveVideo.setVisibility(GONE);
                        mPlayerView.stopPlayback();
                        mPlayerView.clearAnimation();
                        mPlayerView.suspend();
                        mPlayerView.setVideoURI(null);
                        mPlayerView.setVisibility(GONE);
                        mPlayerView.setBackgroundColor(getResources().getColor(R.color.white));
                        mPlayerView.setBackgroundColor(Color.TRANSPARENT);

                    }


                }
            });
            progressDialog.show();
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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

                    // Toast.makeText(PostActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(PostActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage(PostActivity.this.getString(R.string.Uploading) + " " + (int) progress + "%");

                        }


                    });

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

    private void firstPost() {
        Dialog dialog = new Dialog(PostActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(true);

        ImageView imageView = (ImageView) dialog.findViewById(R.id.profile_image);

        String url = "https://cdn.vox-cdn.com/thumbor/lXm_AmbMRRUJVwsfcEdXd7D8D5Q=/0x0:3000x2000/1200x800/filters:focal(1260x760:1740x1240)/cdn.vox-cdn.com/uploads/chorus_image/image/64062111/1_LGFTlvUlC8iJ5mi-UPF7Uw.0.0.0.jpeg";

        Glide.with(PostActivity.this)
                .load(url)
                .circleCrop()
                .into(imageView);
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mButtonSubmit.setClickable(true);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value1 = extras.getString(Intent.EXTRA_TEXT);
            if (!TextUtils.isEmpty(value1)) {
                mEditTextDescription.setText(value1);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mVideoURL != null || mImageURL != null || !TextUtils.isEmpty(mEditTextDescription.getText())) {
            new AlertDialog.Builder(PostActivity.this)
                    .setTitle(R.string.info)
                    .setMessage(R.string.Do_you_want_to_exit)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();

        } else {
            super.onBackPressed();
        }

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

    private void GIF() {
        giphyDialogFragment = new GiphyDialogFragment();
        giphyDialogFragment.setGifSelectionListener(new GiphyDialogFragment.GifSelectionListener() {
            @Override
            public void onGifSelected(@NotNull Media media, @Nullable String s, @NotNull GPHContentType gphContentType) {

                String[] split = media.getUrl().split("-|/");
                String gif_url = "https://media.giphy.com/media/" + split[split.length - 1] + "/giphy.gif";
                Log.d(TAG, "onGifSelected URL: " + gif_url);
                Log.d(TAG, "onGifSelected: " + media.getUrl());
                //  Log.d()
                //  media.getImages().getOriginal();
                Log.d(TAG, "onGifSelected: S:" + s);
                Log.d(TAG, "onGifSelected: " + media.getBitlyGifUrl());

                final Dialog dialog = new Dialog(PostActivity.this); // Context, this, etc.
                dialog.setContentView(R.layout.dialog_demo);
                dialog.setCancelable(false);
                ImageView imageView123 = dialog.findViewById(R.id.gif_image);
                Button ok = dialog.findViewById(R.id.ok);
                Button cancel = dialog.findViewById(R.id.cancel);
                ProgressBar progress = dialog.findViewById(R.id.progress);
                // ok.setClickable(false);
                Glide.with(PostActivity.this)
                        .load(gif_url).into(imageView123);
                                /*.listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@androidx.annotation.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }
                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        // ok.setClickable(true);
                                        flag=true;
                                        return false;
                                    }
                                }).thumbnail(0.05f).diskCacheStrategy(DiskCacheStrategy.DATA)*/

                dialog.show();
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //ok.setClickable(false);
                        if (imageView123.getDrawable() != null) {
                            mImageURL = gif_url;
                            mPostVideoCardView.setVisibility(GONE);
                            mPostImageCardView.setVisibility(VISIBLE);
                            mRemovePost.setVisibility(VISIBLE);
                            mRemoveVideo.setVisibility(VISIBLE);
                            Glide.with(PostActivity.this).load(mImageURL).into(mPostImage);
                            dialog.dismiss();
                            Arrays.fill(split, null);
                        }

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mImageURL = null;
                        mPostVideoCardView.setVisibility(GONE);
                        mPostImageCardView.setVisibility(GONE);
                        mRemovePost.setVisibility(GONE);
                        mRemoveVideo.setVisibility(GONE);
                        dialog.dismiss();
                        Arrays.fill(split, null);
                    }
                });


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

}