package com.sconti.studentcontinent.activity.commentdetails;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.commentdetails.adapter.CommentsListAdapter;
import com.sconti.studentcontinent.activity.commentdetails.adapter.SubCommentsListAdapter;
import com.sconti.studentcontinent.activity.details.DetailsActivity;
import com.sconti.studentcontinent.activity.othersprofile.OthersProfileActivity;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorPostModel;
import com.sconti.studentcontinent.activity.profile.ProfileFragment;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.model.APIResponseModel;

import com.sconti.studentcontinent.model.CommentModel;
import com.sconti.studentcontinent.model.FirstPreferenceDataModel;
import com.sconti.studentcontinent.model.LikeDataModel;
import com.sconti.studentcontinent.model.LikeModel;
import com.sconti.studentcontinent.model.NewAPIResponseModel;
import com.sconti.studentcontinent.model.UserDetails;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;
import com.sconti.studentcontinent.utils.AppUtils;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.PROFILE_URL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

public class PostSubCommentActivity extends BaseActivity {
    private static final String TAG = "PostSubCommentActivity";
    private FirstPreferenceDataModel preferenceDataModel;
    private EditText mEditTextDetails;
    private TextView mSubmit;
    private Button mCnacleButton;
    private TextView mUserSubCommentNameComment, mTextViewDescriptionSubComment;
    //private int type;
    private String mSelectedSubject;
    private LinearLayout contentView;
    private boolean isKnowledgeComment, isKnowledgeGeneralComment, isAspirationComment, isAspirationGeneralComment;
    private ProgressBar mProgressBar;
    private boolean isOutdoorComment;
    private OutDoorPostModel outDoorPostModel;
    private List<CommentModel> commentModelList;
    private SubCommentsListAdapter mSubCommentsListAdapter;
    private RecyclerView recyclerViewCommentor;
    private ActionBar mActionBar;
    String type1;
    private LinearLayout mCancelButton;
    private List<LikeDataModel> likeDatalist;
    ImageView gifImageButton;
    private String userId;
    public GiphyDialogFragment giphyDialogFragment;
    public GiphyFrescoHandler frescoHandler;
    public CircleImageView avatar;

    @Override
    protected int getContentView() {
        return R.layout.activity_post_sub_comment;
    }

    @Override
    protected void initView() {
        recyclerViewCommentor = findViewById(R.id.recycler_sub_viewComments);
        mEditTextDetails = findViewById(R.id.editTextSubComment);
        mSubmit = findViewById(R.id.AddSubComment);
        avatar = findViewById(R.id.imageViewSubCommentProfile);
        mUserSubCommentNameComment = findViewById(R.id.UserSubCommentNameComment);
        mTextViewDescriptionSubComment = findViewById(R.id.textViewDescriptionSubComment);
        contentView = findViewById(R.id.SubComment);
        mProgressBar = findViewById(R.id.progressbar);
        mCancelButton = findViewById(R.id.cancel_button);
        mActionBar = getSupportActionBar();
        userId = SharedPrefsHelper.getInstance().get(USER_ID);
        gifImageButton=findViewById(R.id.gif_image);
        mCnacleButton = findViewById(R.id.CnacleButton);
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

    CommentModel commentModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        setUpNetwork();

        Intent intent = getIntent();
        commentModel = new CommentModel();

        //type = getIntent().getIntExtra("TYPE", 1);
        //type1=getIntent().getParcelableExtra("TYPE");
        // Log.d(TAG, "initData: "+type+" "+commentModel);

        if (intent != null) {
            type1 = intent.getStringExtra("TYPE");
           // Toast.makeText(this, " "+type1, Toast.LENGTH_SHORT).show();
            commentModel = getIntent().getParcelableExtra("DATA");
            GetSubCommentLikeListByUserID(Integer.parseInt(type1));
            commentModel.setId(commentModel.getSl_no());

        }


      //  mEditTextDetails.setHint("You replay on " + commentModel.getCommentedByName() + " Comment");
       // Toast.makeText(this, commentModel.getSl_no() + " " + type1, Toast.LENGTH_SHORT).show();

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (commentModel.getCommentedById().equals(userId)) {
                    intent = new Intent(context, ProfileFragment.class);
                } else {
                    intent = new Intent(context, OthersProfileActivity.class);
                }
                intent.putExtra("USER_ID", commentModel.getCommentedById());
                startActivity(intent);

            }
        });
        mUserSubCommentNameComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (commentModel.getCommentedById().equals(userId)) {
                    intent = new Intent(context, ProfileFragment.class);
                } else {
                    intent = new Intent(context, OthersProfileActivity.class);
                }
                intent.putExtra("USER_ID", commentModel.getCommentedById());
                startActivity(intent);
            }
        });

        mCnacleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mUserSubCommentNameComment.setText(capitalizeWord(commentModel.getCommentedByName()));
         Glide.with(context).load(commentModel.getCommentorURL()).placeholder(R.drawable.ic_user_profile).into(avatar);

        if(!TextUtils.isEmpty(commentModel.getCommentDesc().trim())) {
            String regex = "(https|http)://(media.giphy.com/media/)[a-zA-Z0-9]+(/giphy.gif)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(commentModel.getCommentDesc());
            if (matcher.find()) {
                //  System.out.println(matcher.group());
                Log.i(TAG, "GIF found: " + matcher.group());
                String finals = commentModel.getCommentDesc().replace(matcher.group(), "");
                 mTextViewDescriptionSubComment.setText(capitalizeWordDes(finals.trim()));
            } else {
                mTextViewDescriptionSubComment.setText(capitalizeWordDes(commentModel.getCommentDesc().trim()));
            }
        }
     //   mTextViewDescriptionSubComment.setText(commentModel.getCommentDesc());


        isOutdoorComment = getIntent().getExtras().getBoolean("OUTDOOR_COMMENT", false);
        isKnowledgeComment = getIntent().getExtras().getBoolean("KNOWLEDGE_COMMENT", false);
        isKnowledgeGeneralComment = getIntent().getExtras().getBoolean("KNOWLEDGE_GENERAL_COMMENT", false);
        isAspirationComment = getIntent().getExtras().getBoolean("ASPIRATION_COMMENT", false);
        isAspirationGeneralComment = getIntent().getExtras().getBoolean("ASPIRATION_GENERAL_COMMENT", false);

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
            //  preferenceDataModel = getIntent().getParcelableExtra("DATA");
        }
        mSelectedSubject = getIntent().getStringExtra("SUBJECT");
        //types = getIntent().getStringExtra("TYPE");
        if (mActionBar != null) {
            if (preferenceDataModel != null) {
                mActionBar.setTitle(preferenceDataModel.getDescription());
            } else {
//                mActionBar.setTitle(outDoorPostModel.getDescription());
            }
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (preferenceDataModel != null) {
            if (preferenceDataModel.getDescription() != null && preferenceDataModel.getDescription().trim().length() > 0) {
                //   mTextViewDescription.setText(preferenceDataModel.getDescription());
            }
            if (preferenceDataModel.getImageURL() != null) {
//                mImageViewPost.setVisibility(View.VISIBLE);
//                loadImage(preferenceDataModel.getImageURL(), mImageViewPost);
            } else if (preferenceDataModel.getVideoURL() != null) {
//                playerView.setVisibility(View.VISIBLE);
//                mImageViewPost.setVisibility(View.GONE);
//                initializePlayer(playerView, player, preferenceDataModel.getVideoURL());
            }

            if (preferenceDataModel.getNumShares() != 0)
                //  mTextViewShareCount.setText(""+preferenceDataModel.getNumShares());
                if (preferenceDataModel.getNumLikes() != 0) {
                    //  mTextViewLikeCount.setText(""+preferenceDataModel.getNumLikes());
                }
            if (preferenceDataModel.getNumComments() != 0) {
                //   mTextViewCommentCount.setText(""+preferenceDataModel.getNumComments());
            }
            String uid = SharedPrefsHelper.getInstance().get(USER_ID);
            if (preferenceDataModel.getPostedBy().equalsIgnoreCase(uid)) {
                //    mLinearLayoutDeletePost.setVisibility(View.VISIBLE);
            }
            commentModel.setDescription(preferenceDataModel.getDescription());
            //  commentModel.setId(preferenceDataModel.getId());
            commentModel.setKey(preferenceDataModel.getKey());
            commentModel.setImageURL(preferenceDataModel.getImageURL());
            commentModel.setVideoURL(preferenceDataModel.getVideoURL());
            commentModel.setNumComments(preferenceDataModel.getNumComments());
            commentModel.setNumLikes(preferenceDataModel.getNumLikes());
            commentModel.setNumShares(preferenceDataModel.getNumShares());
            commentModel.setPostedBy(preferenceDataModel.getPostedBy());
            commentModel.setTitle(preferenceDataModel.getTitle());

            ////slno
            commentModel.setSl_no(preferenceDataModel.getSl_no() + "");
        } else {
//            if(outDoorPostModel.getDescription()!=null && outDoorPostModel.getDescription().trim().length()>0){
//               // mTextViewDescription.setText(outDoorPostModel.getDescription());
//            }
//            if(outDoorPostModel.getMediaURL()!=null && !outDoorPostModel.getMediaURL().isEmpty() && outDoorPostModel.getMediaType()==1)
//            {
////                mImageViewPost.setVisibility(View.VISIBLE);
////                loadImage(outDoorPostModel.getMediaURL(), mImageViewPost);
//            }
//            else if(outDoorPostModel.getMediaURL()!=null && !outDoorPostModel.getMediaURL().isEmpty()) {
////                playerView.setVisibility(View.VISIBLE);
////                mImageViewPost.setVisibility(View.GONE);
////                initializePlayer(playerView, player, outDoorPostModel.getMediaURL());
//            }
//            if(outDoorPostModel.getNumShares()!=0)
//               // mTextViewShareCount.setText(""+outDoorPostModel.getNumShares());
//            if(outDoorPostModel.getNumLikes()!=0)
//            {
//             //   mTextViewLikeCount.setText(""+outDoorPostModel.getNumLikes());
//            }
//            if(outDoorPostModel.getNumComments()!=0)
//            {
//              //  mTextViewCommentCount.setText(""+outDoorPostModel.getNumComments());
//            }
//            String uid = SharedPrefsHelper.getInstance().get(USER_ID);
//            if(outDoorPostModel.getPostedById().equalsIgnoreCase(uid)){
//             //   mLinearLayoutDeletePost.setVisibility(View.VISIBLE);
//            }

//            commentModel.setDescription(outDoorPostModel.getDescription());
//            commentModel.setId(outDoorPostModel.getSl_no()+"");
//            // commentModel.setKey(preferenceDataModel.getKey());
//            commentModel.setImageURL(outDoorPostModel.getMediaURL());
//            commentModel.setVideoURL(outDoorPostModel.getMediaURL());
//            commentModel.setNumComments(outDoorPostModel.getNumComments());
//            commentModel.setNumLikes(outDoorPostModel.getNumLikes());
//            commentModel.setNumShares(outDoorPostModel.getNumShares());
//            commentModel.setPostedBy(outDoorPostModel.getPostedById());
//            commentModel.setTitle(outDoorPostModel.getTitle());
        }
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
//        height = metrics.heightPixels;
//        width = metrics.widthPixels;
        setUpNetwork();
        if (type1 != null) {

            getOutdoorComments(commentModel.getId(), Integer.parseInt(type1));
            GetSubCommentLikeListByUserID(Integer.parseInt(type1));

          //  Toast.makeText(this, commentModel.getId()+" "+type1, Toast.LENGTH_SHORT).show();

        } else {
            type1 = "1";
            GetSubCommentLikeListByUserID(Integer.parseInt(type1));
            getOutdoorComments(commentModel.getId(), Integer.parseInt(type1));
        }
        Log.d(TAG, "initData:1 " + commentModel.getId());


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
                        String[] split=media.getUrl().split("-|/");
                        String gif_url = "https://media.giphy.com/media/" + split[split.length - 1] +"/giphy.gif";
                        Log.d(TAG, "onGifSelected URL: " + gif_url);
                        Log.d(TAG, "onGifSelected: "+media.getUrl());
                        Log.d(TAG, "onGifSelected: Original"+media.getImages().getOriginal());
                        Log.d(TAG, "onGifSelected: S:"+media.getImages().getOriginal());
                        Log.d(TAG, "onGifSelected: "+media.getBitlyGifUrl());

                        final Dialog dialog = new Dialog(PostSubCommentActivity.this); // Context, this, etc.
                        dialog.setContentView(R.layout.dialog_demo);
                        dialog.setCancelable(false);
                        ImageView imageView =dialog.findViewById(R.id.gif_image);
                        Button ok=dialog.findViewById(R.id.ok);
                        Button cancel=dialog.findViewById(R.id.cancel);
                        ProgressBar progress =dialog.findViewById(R.id.progress);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                              //  Toast.makeText(PostSubCommentActivity.this, " "+gif_url, Toast.LENGTH_SHORT).show();
                               /* if(!TextUtils.isEmpty(mEditTextDetails.getText().toString().trim()))*//*{*/
                                String title = mEditTextDetails.getText().toString().trim()+gif_url;
                                CommentModel subcoment = new CommentModel();
                                String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
                                subcoment.setCommentDesc(title);
                                subcoment.setDescription(title);
                                subcoment.setDatetime(System.currentTimeMillis());
                                String uid = SharedPrefsHelper.getInstance().get(USER_ID);
                                subcoment.setCommentedById(uid);
                                subcoment.setId(commentModel.getId());
                                subcoment.setSl_no(commentModel.getSl_no());
                                subcoment.setPostedBy("");
                                subcoment.setNumComments(commentModel.getNumComments());
                                subcoment.setTitle("");
                                subcoment.setVideoURL("");
                                String name = SharedPrefsHelper.getInstance().get(NAME);
                                subcoment.setCommentedByName(name);
                                if (prfurl != null)
                                    subcoment.setCommentorURL(prfurl);

                                mProgressBar.setVisibility(View.VISIBLE);
                                Call<APIResponseModel> call = apiInterface.AddSubComment(subcoment, Integer.parseInt(type1));
                                call.enqueue(new Callback<APIResponseModel>() {
                                    @Override
                                    public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                                        mProgressBar.setVisibility(View.GONE);
                                        if (response.body().isStatus() && response.body() != null) {

                                            GetSubCommentLikeListByUserID(Integer.parseInt(type1));
                                            getOutdoorComments(commentModel.getId(), Integer.parseInt(type1));

                                            mEditTextDetails.clearFocus();
                                            mSubCommentsListAdapter.notifyDataSetChanged();

                                        } else {
                                            Toast.makeText(PostSubCommentActivity.this, "Failed to add Comment", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<APIResponseModel> call, Throwable t) {
                                        mProgressBar.setVisibility(View.GONE);
                                        AppUtils.showToastMessage(PostSubCommentActivity.this, getString(R.string.some_thing_wrong));
                                    }
                                });

                                dialog.dismiss();
                                Arrays.fill(split, null);
                           /* }*/
                               /* else{
                                    Toast.makeText(PostSubCommentActivity.this, "write some message", Toast.LENGTH_SHORT).show();
                                }*/
                        }});
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Arrays.fill(split, null);
                            }
                        });
                        Glide.with(PostSubCommentActivity.this)
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


//        mSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                submitData();
//            }
//        });

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


        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSubmit.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSubmit.setClickable(true);
                    }
                }, 5000);
                if (mEditTextDetails.getText() != null && !TextUtils.isEmpty(mEditTextDetails.getText()) && mEditTextDetails.getText().toString().trim().length() > 0) {
                    /*
                    if(isKnowledgeComment)
                    {
                        String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
                        commentModel.setCommentDesc(mEditTextDetails.getText().toString());
                        commentModel.setDatetime(System.currentTimeMillis());
                        String uid = SharedPrefsHelper.getInstance().get(USER_ID);
                        commentModel.setCommentedById(uid);
                        String name = SharedPrefsHelper.getInstance().get(NAME);
                        commentModel.setCommentedByName(name);
                        if(prfurl!=null)
                            commentModel.setCommentorURL(prfurl);
                        mProgressBar.setVisibility(View.VISIBLE);
                        Call<APIResponseModel> apiResponseModelCall = apiInterface.AddSubComment(commentModel, 1);
                        apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
                            @Override
                            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                                mProgressBar.setVisibility(View.GONE);
                                if(response.body()!=null && response.body().isStatus())
                                {
                                    //  finish();
                                    mEditTextDetails.setText("");
                                    getOutdoorComments(outDoorPostModel.getSl_no() + "");
                                }
                                else
                                {
                                    AppUtils.showToastMessage(PostSubCommentActivity.this, getString(R.string.some_thing_wrong));
                                }
                            }

                            @Override
                            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                                mProgressBar.setVisibility(View.GONE);
                                AppUtils.showToastMessage(PostSubCommentActivity.this, getString(R.string.some_thing_wrong));
                            }
                        });
                    }

                    else  if(isAspirationComment){
                        String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
                        commentModel.setCommentDesc(mEditTextDetails.getText().toString());
                        commentModel.setDatetime(System.currentTimeMillis());
                        String uid = SharedPrefsHelper.getInstance().get(USER_ID);
                        commentModel.setCommentedById(uid);
                        String name = SharedPrefsHelper.getInstance().get(NAME);
                        commentModel.setCommentedByName(name);
                        if(prfurl!=null)
                            commentModel.setCommentorURL(prfurl);
                        mProgressBar.setVisibility(View.VISIBLE);
                        Call<APIResponseModel> apiResponseModelCall = apiInterface.AddSubComment(commentModel, 3);
                        apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
                            @Override
                            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                                mProgressBar.setVisibility(View.GONE);
                                if(response.body()!=null && response.body().isStatus())
                                {
                                    finish();
                                }
                                else
                                {
                                    AppUtils.showToastMessage(PostSubCommentActivity.this, getString(R.string.some_thing_wrong));
                                }
                            }

                            @Override
                            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                                mProgressBar.setVisibility(View.GONE);
                                AppUtils.showToastMessage(PostSubCommentActivity.this, getString(R.string.some_thing_wrong));
                            }
                        });
                    }

                    else  if(isAspirationGeneralComment){
                        String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
                        commentModel.setCommentDesc(mEditTextDetails.getText().toString());
                        commentModel.setDatetime(System.currentTimeMillis());
                        String uid = SharedPrefsHelper.getInstance().get(USER_ID);
                        commentModel.setCommentedById(uid);
                        String name = SharedPrefsHelper.getInstance().get(NAME);
                        commentModel.setCommentedByName(name);
                        if(prfurl!=null)
                            commentModel.setCommentorURL(prfurl);
                        mProgressBar.setVisibility(View.VISIBLE);
                        Call<APIResponseModel> apiResponseModelCall = apiInterface.AddSubComment(commentModel, 4);
                        apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
                            @Override
                            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                                mProgressBar.setVisibility(View.GONE);
                                if(response.body()!=null && response.body().isStatus())
                                {
                                    finish();
                                }
                                else
                                {
                                    AppUtils.showToastMessage(PostSubCommentActivity.this, getString(R.string.some_thing_wrong));
                                }
                            }

                            @Override
                            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                                mProgressBar.setVisibility(View.GONE);
                                AppUtils.showToastMessage(PostSubCommentActivity.this, getString(R.string.some_thing_wrong));
                            }
                        });
                    }

                    else  if(isKnowledgeGeneralComment){
                        String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
                        commentModel.setCommentDesc(mEditTextDetails.getText().toString());
                        commentModel.setDatetime(System.currentTimeMillis());
                        String uid = SharedPrefsHelper.getInstance().get(USER_ID);
                        commentModel.setCommentedById(uid);
                        String name = SharedPrefsHelper.getInstance().get(NAME);
                        commentModel.setCommentedByName(name);
                        if(prfurl!=null)
                            commentModel.setCommentorURL(prfurl);
                        mProgressBar.setVisibility(View.VISIBLE);
                        Call<APIResponseModel> apiResponseModelCall = apiInterface.AddSubComment(commentModel, 2);
                        apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
                            @Override
                            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                                mProgressBar.setVisibility(View.GONE);
                                if(response.body()!=null && response.body().isStatus())
                                {
                                    finish();
                                }
                                else
                                {
                                    AppUtils.showToastMessage(PostSubCommentActivity.this, getString(R.string.some_thing_wrong));
                                }
                            }

                            @Override
                            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                                mProgressBar.setVisibility(View.GONE);
                                AppUtils.showToastMessage(PostSubCommentActivity.this, getString(R.string.some_thing_wrong));
                            }
                        });
                    }

                    else if(isOutdoorComment)
                    {
                        String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
                        commentModel.setCommentDesc(mEditTextDetails.getText().toString());
                        commentModel.setDatetime(System.currentTimeMillis());
                        String uid = SharedPrefsHelper.getInstance().get(USER_ID);
                        commentModel.setCommentedById(uid);
                        String name = SharedPrefsHelper.getInstance().get(NAME);
                        commentModel.setCommentedByName(name);
                        if(prfurl!=null)
                            commentModel.setCommentorURL(prfurl);
                        mProgressBar.setVisibility(View.VISIBLE);
                        Call<APIResponseModel> apiResponseModelCall = apiInterface.AddSubComment(commentModel, 2);
                        apiResponseModelCall.enqueue(new Callback<APIResponseModel>() {
                            @Override
                            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                                mProgressBar.setVisibility(View.GONE);
                                if(response.body()!=null && response.body().isStatus())
                                {
                                    finish();
                                }
                                else
                                {
                                    AppUtils.showToastMessage(PostSubCommentActivity.this, getString(R.string.some_thing_wrong));
                                }
                            }

                            @Override
                            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                                mProgressBar.setVisibility(View.GONE);
                                AppUtils.showToastMessage(PostSubCommentActivity.this, getString(R.string.some_thing_wrong));
                            }
                        });
                    }
                    */
                    submitData();
                } else {
                    Toast.makeText(PostSubCommentActivity.this, "Write Comment", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

//    int height, width;
//    boolean isKeyboardShowing = false;
//
//    void onKeyboardVisibilityChanged(boolean opened) {
//        if (opened) {
//            mSubmit.setVisibility(View.VISIBLE);
//            //   mLinearlayoutLikesCommentShare.setVisibility(View.GONE);
//            mEditTextDetails.getLayoutParams().width = width - 100;
//        } else {
//            mSubmit.setVisibility(View.GONE);
//            //    mLinearlayoutLikesCommentShare.setVisibility(View.VISIBLE);
//            mEditTextDetails.getLayoutParams().width = MATCH_PARENT;
//
//        }
//    }

    private void submitData() {
        String title = mEditTextDetails.getText().toString().trim();
        CommentModel subcoment = new CommentModel();
        String prfurl = SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
        subcoment.setCommentDesc(title);
        subcoment.setDescription(title);
        subcoment.setDatetime(System.currentTimeMillis());
        String uid = SharedPrefsHelper.getInstance().get(USER_ID);
        subcoment.setCommentedById(uid);
        subcoment.setId(commentModel.getId());
        subcoment.setSl_no(commentModel.getSl_no());
        subcoment.setPostedBy("");
        subcoment.setNumComments(commentModel.getNumComments());
        subcoment.setTitle("");
        subcoment.setVideoURL("");
        String name = SharedPrefsHelper.getInstance().get(NAME);
        subcoment.setCommentedByName(name);
        if (prfurl != null)
            subcoment.setCommentorURL(prfurl);

        mProgressBar.setVisibility(View.VISIBLE);
        Call<APIResponseModel> call = apiInterface.AddSubComment(subcoment, Integer.parseInt(type1));
        call.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                mProgressBar.setVisibility(View.GONE);
                if (response.body().isStatus() && response.body() != null) {

                    GetSubCommentLikeListByUserID(Integer.parseInt(type1));
                    getOutdoorComments(commentModel.getId(), Integer.parseInt(type1));

                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    mSubCommentsListAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(PostSubCommentActivity.this, "Failed to add Comment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                AppUtils.showToastMessage(PostSubCommentActivity.this, getString(R.string.some_thing_wrong));
            }
        });
    }


    protected ApiInterface apiInterface;

    private void setUpNetwork() {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }

    private void getOutdoorComments(String postId, final int type1) {
        setUpNetwork();
        Call<NewAPIResponseModel> call = null;
        if (type1 == 1) {
            GetSubCommentLikeListByUserID(1);
            call = apiInterface.GetSubCommentsByPostId(postId, 1);

        } else if (type1 == 2) {
            GetSubCommentLikeListByUserID(2);
            call = apiInterface.GetSubCommentsByPostId(postId, 2);

        } else if (type1 == 3) {
            GetSubCommentLikeListByUserID(3);
            call = apiInterface.GetSubCommentsByPostId(postId, 3);

        } else if (type1 == 4) {
            GetSubCommentLikeListByUserID(4);
            call = apiInterface.GetSubCommentsByPostId(postId, 4);

        } else if (type1 == 5) {
            GetSubCommentLikeListByUserID(5);
            call = apiInterface.GetSubCommentsByPostId(postId, 5);

        }
        call.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                commentModelList = response.body().getCommentModel();
                if (commentModelList != null) {
                    mSubCommentsListAdapter = new SubCommentsListAdapter(PostSubCommentActivity.this, commentModelList, likeDatalist, selectItemInterface, type1 + "");
                    recyclerViewCommentor.setLayoutManager(new LinearLayoutManager(PostSubCommentActivity.this, LinearLayoutManager.VERTICAL, false));
                    recyclerViewCommentor.setAdapter(mSubCommentsListAdapter);
                    mSubCommentsListAdapter.notifyDataSetChanged();
                    mEditTextDetails.setText("");
                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                    );

                }
            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });

    }

    private SubCommentsListAdapter.SelectItem selectItemInterface = new SubCommentsListAdapter.SelectItem() {
        @Override
        public void selectedPosition(CommentModel commentModel) {
            //updateCommentCount(commentModel);
            // deleteComment(commentModel);


        }

        @Override
        public void deleteSelectedItemPosition(final String position,final String commentCount) {
            new AlertDialog.Builder(PostSubCommentActivity.this)
                    .setMessage("Delete Comment")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            DeleteSubComment(position, commentCount,type1);
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
            commonUpdateCommentLikeCount(dataModel, mSubCommentsListAdapter, apiInterface, commentModelList, type, isLiked);
        }



        /*@Override
        public void deleteItem(CommentModel commentModel) {

        }*/
    };

    private void DeleteSubComment(final String slno, String commentCount,String type) {
        Call<NewAPIResponseModel> call = apiInterface.DeleteSubComment(slno, commentModel.getSl_no(),commentModel.getNumComments(),Integer.parseInt(type));

        call.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(retrofit2.Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                if (response.isSuccessful() && response.body().isStatus()) {
                    for (int i = 0; i < commentModelList.size(); i++) {
                        if (commentModelList.get(i).getSl_no().equalsIgnoreCase(slno)) {
                            commentModelList.remove(i);
                            mSubCommentsListAdapter.notifyItemRemoved(i);
                            break;
                        }
                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<NewAPIResponseModel> call, Throwable t) {

                Toast.makeText(PostSubCommentActivity.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void commonUpdateCommentLikeCount(final CommentModel dataModel, final SubCommentsListAdapter PostAdapter, ApiInterface apiInterface, final List<CommentModel> TagDataModelList/*commentModelList*/, int type, boolean isLiked) {
        final String uid = SharedPrefsHelper.getInstance().get(USER_ID, null);
        final LikeModel likeModel = new LikeModel();
        likeModel.setLikeByID(uid);
        if (isLiked) {
            likeModel.setLikeCount(dataModel.getNumLikes() + "");
            likeModel.setPostId(dataModel.getSl_no());
            Call<NewAPIResponseModel> call = apiInterface.AddSubLike(likeModel, type);
            call.enqueue(new Callback<NewAPIResponseModel>() {
                @Override
                public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                    if (response.body().isStatus()) {
                        //   Log.i("DAta0",TagDataModelList.toString());
                        if (TagDataModelList != null) {
                            for (int i = 0; i < TagDataModelList.size(); i++) {
                                if (TagDataModelList.get(i).getSl_no().equalsIgnoreCase(dataModel.getSl_no())) {
                                    TagDataModelList.get(i).setNumLikes(TagDataModelList.get(i).getNumLikes() + 1);
                                    LikeDataModel likeDataModel = new LikeDataModel();
                                    likeDataModel.setLikeByID(uid);
                                    likeDataModel.setLikeCount((dataModel.getNumLikes() + 1) + "");
                                    likeDataModel.setPostID(dataModel.getSl_no());
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
            Call<NewAPIResponseModel> call = apiInterface.DisSubLike(likeModel, type);
            call.enqueue(new Callback<NewAPIResponseModel>() {
                @Override
                public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                    for (int i = 0; i < TagDataModelList.size(); i++) {
                        if (TagDataModelList.get(i).getSl_no().equalsIgnoreCase(dataModel.getSl_no())) {
                            if (TagDataModelList.get(i).getNumLikes() > 0) {
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Integer.parseInt(type1)!=0 &&  commentModel.getId()!=null) {
            GetSubCommentLikeListByUserID(Integer.parseInt(type1));
            getOutdoorComments(commentModel.getId(), Integer.parseInt(type1));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpNetwork();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        if(Integer.parseInt(type1)!=0 &&  commentModel.getId()!=null) {
            GetSubCommentLikeListByUserID(Integer.parseInt(type1));
            getOutdoorComments(commentModel.getId(), Integer.parseInt(type1));
        }
    }
    private void GetSubCommentLikeListByUserID(int type) {

        String userID = SharedPrefsHelper.getInstance().get(USER_ID);
        Log.d(TAG, "IDD" + userID);

        Call<NewAPIResponseModel> Call = apiInterface.GetSubCommentLikeListByUserID(userID, type);
        Call.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(retrofit2.Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                if (response.isSuccessful() && response.body().getUsertable() != null) {
                    likeDatalist = response.body().getLikeDatalist();
                    if (likeDatalist != null) {
                        if (mSubCommentsListAdapter != null) {
                            mSubCommentsListAdapter.notifyDataSetChanged();
                            mSubCommentsListAdapter = new SubCommentsListAdapter(PostSubCommentActivity.this, commentModelList, likeDatalist, selectItemInterface, type1 + "");
                            recyclerViewCommentor.setAdapter(mSubCommentsListAdapter);
                            mSubCommentsListAdapter.notifyDataSetChanged();
                        }
                    }
                    else{
                        Log.d(TAG, "onResponse: Empty like List");
                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<NewAPIResponseModel> call, Throwable t) {

            }
        });
    }
}