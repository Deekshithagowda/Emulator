package com.sconti.studentcontinent.activity.outdoor.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.LikedUserListActivity;
import com.sconti.studentcontinent.activity.othersprofile.OthersProfileActivity;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorPostModel;
import com.sconti.studentcontinent.activity.profile.ProfileFragment;
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

import com.sconti.studentcontinent.utils.glidemodule.GlideImageLoader;
import com.sconti.studentcontinent.utils.tools.ContinentTools;
import com.sconti.studentcontinent.utils.tools.CustomPlayerUiController;
import com.sconti.studentcontinent.utils.tools.PopUpClass;
import com.sconti.studentcontinent.utils.tools.TimeAgo;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.hanks.library.bang.SmallBangView;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

/**
 * Created by Veerendra Patel on 3/6/19.
 */

public class OutDoorTagsPrefAdapter extends RecyclerView.Adapter<OutDoorTagsPrefAdapter.MyViewHolder> implements Filterable {
    private final String TAG = "OutDoorTagsPrefAdapter";
    private Context context;
    private boolean isClicked = false;
    private long nbId = 0;
    private DecimalFormat formatter;
    private List<OutDoorPostModel> dataModelList, dataModelListFull;
    private List<UserDetails> userDataModelList;
    private boolean playWhenReady = false;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private SimpleExoPlayer player;
    private SelectItem mListener;
    private MediaController mMediaController;
    private String type;
    private boolean mExoPlayerFullscreen;
    private int mFeatureType;
    private List<FollowModel> mFollowersList;
    private String userId;
    private List<LikeDataModel> mlikeDatalist;
    CustomPlayerUiController customPlayerUiController;
    int lastPosition = -1;
    ProgressBar mProgressBar;
    private List<ShareDataModel> shareDataList;
    private List<CommentModel> commentDataList;

//capitalizeWord

    boolean isAnimated = false;



    public OutDoorTagsPrefAdapter(Context context, List<FollowModel> mFollowersList , List<LikeDataModel> mlikeDatalist, List<CommentModel> commentDataList , List<ShareDataModel> shareDataList , List<OutDoorPostModel> dataList, SelectItem selectItem, MediaController mediaController, int type) {
        this.context = context;
        this.dataModelList = dataList;
        this.mListener = selectItem;
        this.mMediaController = mediaController;
        this.mFeatureType = type;
        dataModelListFull = new ArrayList<>(dataList);
        this.mFollowersList = mFollowersList;
        this.mlikeDatalist = mlikeDatalist;
        this.commentDataList=commentDataList;
        this.shareDataList=shareDataList;
        userId = SharedPrefsHelper.getInstance().get(USER_ID);
    }



    @Override   // TODO : udaya add
    public Filter getFilter() {
        return dataModelListFilter;
    }

    private Filter dataModelListFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<OutDoorPostModel> filterList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filterList.addAll(dataModelListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (OutDoorPostModel item : dataModelListFull) {
                    if (item.getDescription().toLowerCase().contains(filterPattern) || item.getPostedByName().toLowerCase().contains(filterPattern)) {
                        filterList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filterList;

            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataModelList.clear();
            dataModelList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == dataModelList.size() - 1) {
            if (mListener != null) {
                mListener.reachedLastItem();
            }
        }
        return position;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView avatar , mcircleShareProfileImage;
        public View parent;
        public ImageView mImageViewContent, mShowTheFollowDot , mShareIcon;

        public TextView mTextViewTitle, mTextViewTitle2, mTextViewDateTime, mTextViewViewComments, mLinearLayoutFollowUnfollow, mShardUserName;
        private ReadMoreTextView mTextViewDescription;
        public PlayerView playerView;
        public LinearLayout mShareCommentLayout, mlinearLayout2, mLinearLayoutShareShow;
        public ConstraintLayout constraintLayout;
        public ConstraintSet constraintSet;
        public SimpleExoPlayer simpleExoPlayer;
        public TextView tag;
        public String url;
        public LinearLayout mLinearLayoutLike, mLinearLayoutComment, mLinearLayoutShare;
        public TextView mTextViewLikeCount, mTextViewShareCount, mTextViewCommentCount;
        private ImageView mImageViewFullScreen;
        private ImageView mFullScreenIcon;
        private Dialog mFullScreenDialog;
        private boolean mExoPlayerFullscreen = false;
        private String YoutubeUrl = "https://www.youtube.com/watch?v=";
        protected RelativeLayout relativeLayoutOverYouTubeThumbnailView;
        protected ImageView playButton;
        private LinearLayout linearLayout7, mImageViewMoreOption, mUserNameLinearLayout;
        private LinearLayout mlinearlayoutMedia;
        YouTubePlayerView youTubePlayerView;
        public ImageView videoThumbNail;
        private SmallBangView smallBangView;

        private TextView mShowTheFollowText;
       // private TextView mShowTheFollowText;

        private ImageView mCommentImage,mShareImage;


        LottieAnimationView lottieAnimationView;


        ImageView black_heart;

        MyViewHolder(View view) {
            super(view);
            parent = view;
            videoThumbNail = view.findViewById(R.id.videothumbnail);
            avatar = view.findViewById(R.id.circleImageViewProfile);
            mImageViewFullScreen = view.findViewById(R.id.fullscreen);
            mImageViewContent = view.findViewById(R.id.imageViewContent);
            mTextViewDescription = view.findViewById(R.id.textViewContent);
            mTextViewTitle = view.findViewById(R.id.textViewTitle);
            mTextViewTitle2 = view.findViewById(R.id.textViewTitle2);
            mTextViewDateTime = view.findViewById(R.id.textViewDateTime);
            playerView = view.findViewById(R.id.video_view);
            mShareCommentLayout = view.findViewById(R.id.linearlayoutLikesCommentShare);
            constraintLayout = view.findViewById(R.id.constraintLayoutRoot);
            mLinearLayoutComment = view.findViewById(R.id.linearlayoutComment);
            mLinearLayoutLike = view.findViewById(R.id.linearlayoutLike);
            mLinearLayoutShare = view.findViewById(R.id.linearlayoutShare);
            mTextViewLikeCount = view.findViewById(R.id.textViewLikeCount);
            mTextViewShareCount = view.findViewById(R.id.textViewShareCount);
            mTextViewCommentCount = view.findViewById(R.id.textViewCommentCount);
            mImageViewMoreOption = view.findViewById(R.id.more_option);
            mUserNameLinearLayout = view.findViewById(R.id.UserNameLinearLayout);
            mShareIcon = view.findViewById(R.id.ShareIcon);
            mcircleShareProfileImage = view.findViewById(R.id.circleShareProfileImage);
            //Like
            smallBangView = view.findViewById(R.id.imageViewAnimation);
            // mTextViewViewComments = view.findViewById(R.id.textViewViewcomment);
            youTubePlayerView = view.findViewById(R.id.youtube_player_view);
            mShowTheFollowText = view.findViewById(R.id.FollowButton);
            //   linearLayout7 = view.findViewById(R.id.more_option);
            //mShowTheFollowDot = view.findViewById(R.id.showTheFollowDot);
            // lottieAnimationView = view.findViewById(R.id.animation_view);
            //black_heart=view.findViewById(R.id.black_heart);

            //  imageView = view.findViewById(R.id.imageViewAnimation);
            mlinearLayout2 = view.findViewById(R.id.NameANdTimeLinearLayout);

            mLinearLayoutShareShow = view.findViewById(R.id.LinearLayoutShareShow);
            mlinearlayoutMedia = view.findViewById(R.id.linearlayoutMedia);
            mCommentImage=view.findViewById(R.id.comment);
            mShareImage=view.findViewById(R.id.share);

            mProgressBar=view.findViewById(R.id.progressLoader);

            mTextViewTitle2.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            mTextViewTitle2.setMarqueeRepeatLimit(-1);
            mTextViewTitle2.setSingleLine(true);
            mTextViewTitle2.setSelected(true);

            tag = view.findViewById(R.id.tag);


        }

        public void setView(String url2) {
            parent.setTag(this);
            url = url2;
        }

    }
    //  dialog.dismiss();

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.first_preference_row_item, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {

            final OutDoorPostModel dataModel = dataModelList.get(position);
            final String ID = AppUtils.extractYTId(dataModel.getDescription());


            if(dataModel.getPostOwnerName() != null){
                holder.mTextViewTitle.setText(capitalizeWord(dataModel.getPostedByName()));
                holder.mTextViewTitle2.setText(" shared " + capitalizeWord(dataModel.getPostOwnerName()) + " post");
                holder.mShareIcon.setVisibility(VISIBLE);
                holder.mcircleShareProfileImage.setVisibility(VISIBLE);
                Glide.with(context).load(dataModel.getOriginalUserPrfURL()).placeholder(R.drawable.ic_user_profile).into(holder.mcircleShareProfileImage);
                Glide.with(context).load(dataModel.getPrfurl()).placeholder(R.drawable.ic_user_profile).into(holder.avatar);

            }else {
                holder.mTextViewTitle.setText(capitalizeWord(dataModel.getPostedByName()));
                holder.mTextViewTitle2.setText("@" + capitalizeWord(dataModel.getPostedByCollege()));
                holder.mShareIcon.setVisibility(GONE);
                holder.mcircleShareProfileImage.setVisibility(GONE);
                Glide.with(context).load(dataModel.getPrfurl()).placeholder(R.drawable.ic_user_profile).into(holder.avatar);
            }

            if (holder.getAdapterPosition() > lastPosition /*||holder.getAdapterPosition()>lastPosition*/) {
                Animation animation = AnimationUtils.loadAnimation(context,
                        (position > lastPosition) ? R.anim.push_left_in
                                : R.anim.push_right_in);
                holder.itemView.startAnimation(animation);
                lastPosition = position;

            }

            if(mlikeDatalist!=null)
            {
                boolean isLiked = false;
                for (int i = 0; i < mlikeDatalist.size(); i++)
                {
                    //mlikeDatalist.get(i).getLikeByID().equalsIgnoreCase(userID/dataModel.getPostedById()/)
                    if (mlikeDatalist.get(i).getPostID().equalsIgnoreCase(dataModel.getPostId())) {
                        // holder.mShowTheFollowText.setText("Unfollow");
                        if(Integer.parseInt(mlikeDatalist.get(i).getLikeCount())>0)
                        {
                            holder.smallBangView.setSelected(true);
                            isLiked = true;
                            break;
                        }
                    }
                }
                dataModel.setLiked(isLiked);
            }


            if(commentDataList!=null)
            {
               // Collections.reverse(commentDataList);
                for (int i =0;i<commentDataList.size();i++)
                {
                    if(commentDataList.get(i).getId().equalsIgnoreCase(dataModel.getSl_no()+""))
                    {
                       holder.mCommentImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_chat_bubble));
                       break;
                    }
                }
            }
            if(shareDataList!=null)
            {
                // Collections.reverse(commentDataList);
                for (int i =0;i<shareDataList.size();i++)
                {
                    if(shareDataList.get(i).getPostId().equalsIgnoreCase(dataModel.getPostId()))
                    {
                        holder.mShareImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_share_blue));
                        break;
                    }
                }
            }
            holder.mcircleShareProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if (dataModel.getUserIDofOriginalPost().equals(userId)) {
                        intent = new Intent(context, ProfileFragment.class);
                    } else {
                        intent = new Intent(context, OthersProfileActivity.class);
                    }
                    intent.putExtra("USER_ID", dataModel.getUserIDofOriginalPost());
                    context.startActivity(intent);
                }
            });
            holder.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if (dataModel.getPostedById().equals(userId)) {
                        intent = new Intent(context, ProfileFragment.class);
                    } else {
                        intent = new Intent(context, OthersProfileActivity.class);
                    }
                    intent.putExtra("USER_ID", dataModel.getPostedById());
                    context.startActivity(intent);
                }
            });

            if (dataModel.getMediaURL() != null && !dataModel.getMediaURL().isEmpty() && dataModel.getMediaType() == 1) {


                holder.mImageViewContent.setVisibility(VISIBLE);
                holder.mlinearlayoutMedia.setVisibility(VISIBLE);
                holder.youTubePlayerView.setVisibility(GONE);
                mProgressBar.setVisibility(INVISIBLE);
                /*Picasso.with(context)
                        .load(dataModel.getMediaURL())
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                Bitmap   resized = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.8), (int)(bitmap.getHeight()*0.8), true);
                                holder.mImageViewContent.setImageBitmap(resized);

                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });*/
               // Bitmap bit=getBitmapFromURL(dataModel.getMediaURL());
              //  Bitmap   resized = Bitmap.createScaledBitmap(bit,(int)(bit.getWidth()*0.8), (int)(bit.getHeight()*0.8), true);
              //  holder.mImageViewContent.setImageBitmap(resized);
                //Log.d(TAG, "scale: width:"+bit.getWidth()+"Height "+bit.getHeight());

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .priority(Priority.HIGH);

                new GlideImageLoader(holder.mImageViewContent,
                        mProgressBar).load(dataModel.getMediaURL(),options);

               /* Glide.with(context)
                        .load(dataModel.getMediaURL())
                       .listener(new RequestListener<Drawable>() {
                           @Override
                           public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                               mProgressBar.setVisibility(GONE);
                               onFinishedLoading(dataModel.getMediaURL());
                               return false;
                           }

                           @Override
                           public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                               mProgressBar.setVisibility(GONE);
                               onFinishedLoading(dataModel.getMediaURL());
                               return false;
                           }
                       })
                        .thumbnail(0.05f)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(holder.mImageViewContent);*/
                //.apply(new RequestOptions().override(0, holder.mImageViewContent.getMaxHeight()))
                //.placeholder(R.drawable.paperplane)
                //.apply(new RequestOptions().override(0,300 /*holder.mImageViewContent.getMaxHeight()*/))


                // holder.mImageViewFullScreen.setVisibility(View.GONE);

            } else {
                holder.mImageViewContent.setVisibility(GONE);
                if (dataModel.getMediaURL() != null && dataModel.getMediaType() == 3) {
                    mProgressBar.setVisibility(GONE);
                    holder.playerView.setVisibility(VISIBLE);
                    holder.mlinearlayoutMedia.setVisibility(VISIBLE);
                    holder.youTubePlayerView.setVisibility(GONE);
                    holder.setView(dataModel.getMediaURL());

                    // holder.mImageViewFullScreen.setVisibility(View.VISIBLE);
                    initializePlayer(holder.playerView, holder.simpleExoPlayer, dataModel.getMediaURL());
                    mMediaController.videoPosition(position);
               //     Toast.makeText(context, " "+holder.simpleExoPlayer.getVideoFormat().width, Toast.LENGTH_SHORT).show();
                  //  Log.d(TAG, "playVideoAtThis: width "+ holder.simpleExoPlayer.getVideoFormat().width+" height"+ holder.simpleExoPlayer.getVideoFormat().height );

                }
            }

            holder.mImageViewContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.dialog);
                    ImageView imageView = (ImageView)dialog.findViewById(R.id.imageview);
                    Glide.with(context).load(dataModelList.get(position).getMediaURL()).into(imageView);
                    dialog.show();*/
                    //Toast.makeText(context, "Image", Toast.LENGTH_SHORT).show();
                   /* Bundle bundle = new Bundle();
                    bundle.putString("url",dataModelList.get(position).getMediaURL());
                    bundle.putString("mediaType","image");
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new VideoFragment();
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.contentFrame, myFragment).addToBackStack(null).commit();
*/

                    /*Intent in = new Intent(context, VideoImageActivity.class);
                    in.putExtra("url",dataModelList.get(position).getMediaURL());
                    in.putExtra("mediaType","image");
                    context.startActivity(in);*/

                    PopUpClass popUpClass = new PopUpClass();
                    popUpClass.showPopupWindow(view,"image",dataModelList.get(position).getMediaURL());

                }
            });

            holder.playerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context, "Video", Toast.LENGTH_SHORT).show();
                    /*Bundle bundle = new Bundle();
                    bundle.putString("url",dataModelList.get(position).getMediaURL());
                    bundle.putString("mediaType","video");
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new VideoFragment();
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.contentFrame, myFragment).addToBackStack(null).commit();*/

                   /* Intent in = new Intent(context, VideoImageActivity.class);
                    in.putExtra("url",dataModelList.get(position).getMediaURL());
                    in.putExtra("mediaType","video");
                    context.startActivity(in);*/

                    PopUpClass popUpClass = new PopUpClass();
                    popUpClass.showPopupWindow(view,"video",dataModelList.get(position).getMediaURL());
                }
            });

//            holder.mImageViewContent.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    final Dialog dialog = new Dialog(context);
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setCancelable(true);
//                    dialog.setContentView(R.layout.dialog);
//                    ImageView imageView = (ImageView) dialog.findViewById(R.id.imageview);
//                    Glide.with(context).load(dataModelList.get(position).getMediaURL()).into(imageView);
//                    dialog.show();
//                }
//            });

            holder.mUserNameLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if (dataModel.getPostedById().equals(userId)) {
                        intent = new Intent(context, ProfileFragment.class);
                    } else {
                        intent = new Intent(context, OthersProfileActivity.class);
                    }
                    intent.putExtra("USER_ID", dataModel.getPostedById());
                    context.startActivity(intent);
                }
            });

//            holder.playerView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("url", dataModelList.get(position).getMediaURL());
//                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                    Fragment myFragment = new VideoFragment();
//                    myFragment.setArguments(bundle);
//                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.contentFrame, myFragment).addToBackStack(null).commit();
//
//
//                }
//            });

            holder.youTubePlayerView.setVisibility(GONE);
            if (mFollowersList != null)
                for (int i = 0; i < mFollowersList.size(); i++) {
                    if (mFollowersList.get(i).getFollowee_id().equalsIgnoreCase(dataModel.getPostedById())) {
                        // holder.mShowTheFollowText.setText("Unfollow");

                        holder.mShowTheFollowText.setVisibility(GONE);

                        //TODO : add follow list
                        // holder.mShowTheFollowDot.setVisibility(View.GONE);
                    }
                }

            if (dataModel.getPostedById().equalsIgnoreCase(SharedPrefsHelper.getInstance().get(USER_ID).toString())) {
                holder.mShowTheFollowText.setVisibility(GONE);
                //   holder.mShowTheFollowDot.setVisibility(View.GONE);
            }


            if (dataModel.getDescription() != null && ID != null) {
                holder.mlinearlayoutMedia.setVisibility(VISIBLE);
                holder.youTubePlayerView.setVisibility(VISIBLE);
                holder.mImageViewContent.setVisibility(GONE);
                holder.playerView.setVisibility(GONE);

//                holder.youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//                    @Override
//                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                        youTubePlayer.loadVideo(ID, 0);
//                        youTubePlayer.pause();
//                    }
//                });

                holder.youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        customPlayerUiController = new CustomPlayerUiController(context, youTubePlayer, holder.youTubePlayerView);
                        youTubePlayer.addListener(customPlayerUiController);
                        holder.youTubePlayerView.addFullScreenListener(customPlayerUiController);
                        youTubePlayer.loadVideo(ID, 0f);
                        // youTubePlayer.play();
                        // youTubePlayer.cueVideo(ID,0f);
                        youTubePlayer.pause();
                       // youTubePlayer.setPlayerStyle(com.google.android.youtube.player.YouTubePlayer.PlayerStyle.CHROMELESS);
                       /* holder.youTubePlayerView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(context, "Youtube View", Toast.LENGTH_SHORT).show();

                            }
                        });*/

                    }
                });
                //holder.youTubePlayerView.setPlayerStyle(com.google.android.youtube.player.YouTubePlayer.PlayerStyle.CHROMELESS);
            } else {
                holder.youTubePlayerView.setVisibility(GONE);
            }

            /*holder.youTubePlayerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        Toast.makeText(context, "Youtube Click", Toast.LENGTH_SHORT).show();
                        // Do what you want
                        return true;
                    }
                    return false;
                }
            });*/


            holder.mTextViewLikeCount.setOnClickListener(new View.OnClickListener() { // TODO : udaya add
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LikedUserListActivity.class);
                    intent.putExtra("POSTID", dataModel.getPostId());
                    intent.putExtra("TYPE", mFeatureType);
                    context.startActivity(intent);
                }
            });

            holder.tag.setText(position + "");
            // holder.mTextViewTitle.setText(dataModel.getTitle());






            if (dataModel.getPostedByCollege() != null && dataModel.getPostedByCollege().length() > 0) {


//                if(dataModel.getUserIDofOriginalPost() != null){
//                    holder.mShareIcon.setVisibility(VISIBLE);
//                    holder.mcircleShareProfileImage.setVisibility(VISIBLE);
//                    holder.avatar.setVisibility(VISIBLE);
//
//                }else {
//
//
//                    holder.mShareIcon.setVisibility(GONE);
//                    holder.mcircleShareProfileImage.setVisibility(GONE);
//                    if (dataModel.getPostedByName() != null) {
//                        holder.mTextViewTitle.setText(dataModel.getPostedByName());
//                    }
//                    if (dataModel.getPostedByCollege() != null && dataModel.getPostedByCollege().length() > 0) {
//                        holder.mTextViewTitle.setText(dataModel.getPostedByName());
//                    }
//                    if (dataModel.getPostedByCollege() != null && dataModel.getPostedByCollege().length() > 0) {
//                        holder.mTextViewTitle2.setText("@" + dataModel.getPostedByCollege());
//                    }
            //    }



                holder.mTextViewDescription.setText(capitalizeWordDes(dataModel.getDescription()));

                if (dataModel.getCreated_on() != 0) {

                   // CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(dataModel.getCreated_on(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                    holder.mTextViewDateTime.setText(TimeAgo.timeAgo(new Date(),dataModel.getCreated_on()));
                }

                if (dataModel.getPrfurl() != null) {
                    //Picasso.with(context).load(dataModel.getPrfurl()).resize(0,holder.avatar.getHeight()).into(holder.avatar);
                    //Picasso.with(context).load(dataModel.getPrfurl()).into(holder.avatar);

                }

                if (dataModel.getNumComments() != 0) {
                    holder.mTextViewCommentCount.setText(dataModel.getNumComments() + "");
                    //  holder.mTextViewCommentCount.setVisibility(View.VISIBLE);
                }
               // if (dataModel.getNumLikes() != 0) {
                if(dataModel.getNumLikes()==0)
                {
                    holder.smallBangView.setSelected(false);
                    holder.mTextViewLikeCount.setVisibility(INVISIBLE);
                }else {
                    holder.mTextViewLikeCount.setVisibility(VISIBLE);
                    holder.mTextViewLikeCount.setText(dataModel.getNumLikes() + "");
                }
                    //  holder.mTextViewCommentCount.setVisibility(View.VISIBLE);
             //   }

                if (dataModel.getNumShares() != 0) {
                    holder.mTextViewShareCount.setText(dataModel.getNumShares() + "");
                    //  holder.mTextViewCommentCount.setVisibility(View.VISIBLE);
                }
                holder.smallBangView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.smallBangView.isSelected()) {
                            updateLikesCount(dataModel, false);
                            holder.smallBangView.setSelected(false);
                        }
                        else {
                            updateLikesCount(dataModel, true);
                            holder.smallBangView.setSelected(true);
                            holder.smallBangView.likeAnimation();
                        }

                        /*if (holder.smallBangView.isSelected()) {
                            updateLikesCount(dataModel, false);
                            holder.smallBangView.setSelected(false);
                        } else {
                            // if not selected only

                            updateLikesCount(dataModel, true);
                            holder.smallBangView.setSelected(true);
                            holder.smallBangView.likeAnimation();
                        }*/
                    }
                });


               /* holder.lottieAnimationView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!isAnimated){
                            updateLikesCount(dataModel, true);
                            holder.black_heart.setVisibility(View.INVISIBLE);
                            holder.lottieAnimationView.setSpeed(3f);
                            isAnimated=true;
                            holder.lottieAnimationView.playAnimation();

                        } else {
                            updateLikesCount(dataModel, false);
                            holder.black_heart.setVisibility(View.INVISIBLE);
                            holder.lottieAnimationView.setSpeed(-5F);
                            isAnimated=false;
                            holder.lottieAnimationView.playAnimation();
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    holder.black_heart.setVisibility(View.VISIBLE);
                                }
                            }, 500);

                          *//*  if (!isAnimated){
                                updateLikesCount(dataModel, true);
                                holder.black_heart.setVisibility(View.INVISIBLE);
                                holder.lottieAnimationView.setSpeed(3f);
                                isAnimated=true;
                                holder.lottieAnimationView.playAnimation();

                            } else {
                                updateLikesCount(dataModel, false);
                                holder.black_heart.setVisibility(View.INVISIBLE);
                                holder.lottieAnimationView.setSpeed(-5F);
                                isAnimated=false;
                                holder.lottieAnimationView.playAnimation();
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        holder.black_heart.setVisibility(View.VISIBLE);
                                    }
                                }, 500);*//*



                        }
                    }
                });*/


//                holder.mImageViewMoreOption.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (mListener != null)
//                            mListener.openAlert(dataModel);
//                    }
//                });

                if(mlikeDatalist!=null)
                {
                    boolean isLiked = false;
                    for (int i = 0; i < mlikeDatalist.size(); i++)
                    {
                        //mlikeDatalist.get(i).getLikeByID().equalsIgnoreCase(userID/dataModel.getPostedById()/)
                        if (mlikeDatalist.get(i).getPostID().equalsIgnoreCase(dataModel.getPostId())) {
                            // holder.mShowTheFollowText.setText("Unfollow");
                            if(Integer.parseInt(mlikeDatalist.get(i).getLikeCount())>0)
                            {
                                holder.smallBangView.setSelected(true);
                                isLiked = true;
                                break;
                            }
                        }
                    }
                    dataModel.setLiked(isLiked);

                }

                holder.mImageViewMoreOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null)
                            mListener.openAlert(dataModel);
                    }
                });

                holder.mlinearLayout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.clickOnProfile(dataModel.getPostedById());
                        }
                    }
                });

                holder.mShowTheFollowText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setUpNetwork();
                        FollowModel followModel = new FollowModel();
                        followModel.setFollowee_id(dataModel.getPostedById());

                        // holder.mShowTheFollowDot.setVisibility(View.GONE);
                        followModel.setFollower_id(SharedPrefsHelper.getInstance().get(USER_ID).toString());

                        Call<NewAPIResponseModel> call = apiInterface.AddFollower(followModel);
                        call.enqueue(new Callback<NewAPIResponseModel>() {
                            @Override
                            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                                if (response.body().isStatus()) {
                                    ContinentTools.showToast("following");
                                    holder.mShowTheFollowText.setVisibility(GONE);
                                    String userID = SharedPrefsHelper.getInstance().get(USER_ID);
                                    Call<NewAPIResponseModel> listCall = apiInterface.GetFollowers(userID, "0");
                                    listCall.enqueue(new Callback<NewAPIResponseModel>() {
                                        @Override
                                        public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                                            mFollowersList = response.body().getmFollowList();
                                            holder.mShowTheFollowText.setVisibility(GONE);
                                            notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                                            Log.d("TAG", "onFailure: ");
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                                // ContinentTools.showToast(getString(R.string.some_thing_wrong));

                            }
                        });

//                    if (holder.mShowTheFollowText.getText().toString().equalsIgnoreCase("Follow")) {
//
//                         mListener.followUnfollow(followModel, 1);
//                         holder.mShowTheFollowText.setVisibility(View.GONE);
////                        if(followModel.getFollowee_id().equals(dataModel.getPostedById())){
////
////                        }
//                    } else {
//                        holder.mShowTheFollowText.setVisibility(View.VISIBLE);
//                      //  holder.mShowTheFollowDot.setVisibility(View.GONE);
//                    //   mListener.followUnfollow(followModel, 2);
//                    }
                    }
                });


//                if (dataModel.getNumComments() >= 1) {
//                    holder.mTextViewViewComments.setText("View " + dataModel.getNumComments() + " Comments");
//                    holder.mTextViewViewComments.setVisibility(View.VISIBLE);
//                } else {
//                    holder.mTextViewViewComments.setVisibility(View.GONE);
//                }
                holder.mLinearLayoutComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        onClickItem(holder.getAdapterPosition());


                    }
                });

                holder.mLinearLayoutShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        share(dataModelList.get(holder.getAdapterPosition()));
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(dataModelList.get(holder.getAdapterPosition()).getMediaType()==3){
            holder.videoThumbNail.setVisibility(VISIBLE);
            holder.playerView.setVisibility(GONE);

        }
        else{
            holder.videoThumbNail.setVisibility(GONE);
        }
        if(dataModelList.get(holder.getAdapterPosition()).getMediaType()==3 && mListener!=null && holder.getAdapterPosition()==0 || holder.getAdapterPosition()==1){
            mListener.startVideo(holder.videoThumbNail);
        }
    }

    public interface FollowUnfollow {
        void followUnfollow(FollowModel dataModel, int type);
    }

    private void initFullscreenDialog(final PlayerView mExoPlayerView, Dialog
            mFullScreenDialog) {

        mFullScreenDialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {

        };
    }


    /*private void openFullscreenDialog(PlayerView mExoPlayerView, Dialog mFullScreenDialog) {

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
       // mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }


    private void closeFullscreenDialog(PlayerView mExoPlayerView, Dialog mFullScreenDialog) {

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
       // mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fullscreen_expand));
    }


    private void initFullscreenButton(PlayerView mExoPlayerView, ImageView mFullScreenButton) {

        PlaybackControlView controlView = mExoPlayerView.findViewById(R.id.exo_controller);
        //mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
       // mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen)
                    openFullscreenDialog(mExoPlayerView, mFullScreenDialog);
                else
                    closeFullscreenDialog();
            }
        });
    }

*/


    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        super.onViewRecycled(holder);
        Log.d(TAG, "onViewRecycled: fromtagadapter");
        if (holder.simpleExoPlayer != null) {
            mMediaController.releaseVideo(holder.simpleExoPlayer);
        }
        //releasePlayer(player);
    }

    private void initializePlayer(PlayerView playerView, SimpleExoPlayer player, String url) {
        mMediaController.playVideo(playerView, player, url);

       /* player = ExoPlayerFactory.newSimpleInstance(context);
        playerView.setPlayer(player);
        Uri uri = Uri.parse(url);
        MediaSource mediaSource = buildMediaSource(uri);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, false, false);*/


    }


    private void getFollowersList() {

    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(context, "exoplayer-codelab");
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }

    public interface MediaController {
        void playVideo(PlayerView playerView, SimpleExoPlayer player, String url);

        void releaseVideo(SimpleExoPlayer player);

        void videoPosition(int videoPosition);
    }

    private void releasePlayer(SimpleExoPlayer player) {
        if (player != null) {
            // player.removeListener(playbackStateListener);
            player.release();
            player = null;
        }
    }

    private class PlaybackStateListener implements Player.EventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady,
                                         int playbackState) {
            String stateString;
            switch (playbackState) {
                case ExoPlayer.STATE_IDLE:
                    stateString = "ExoPlayer.STATE_IDLE      -";
                    break;
                case ExoPlayer.STATE_BUFFERING:
                    stateString = "ExoPlayer.STATE_BUFFERING -";
                    break;
                case ExoPlayer.STATE_READY:
                    stateString = "ExoPlayer.STATE_READY     -";
                    break;
                case ExoPlayer.STATE_ENDED:
                    stateString = "ExoPlayer.STATE_ENDED     -";
                    break;
                default:
                    stateString = "UNKNOWN_STATE             -";
                    break;
            }
            Log.d(TAG, "onPlayerStateChanged:changed state to " + stateString + " playWhenReady: " + playWhenReady);
        }
    }

    public void onClickItem(int position) {
        if (mListener != null) {
            mListener.selectedPosition(position);
        }
    }


    public void share(OutDoorPostModel position) {
        if (mListener != null) {
            mListener.sharePost(position);
        }
    }

    public interface SelectItem {
        void selectedPosition(int position);

        void sharePost(OutDoorPostModel dataModel);

        void openAlert(OutDoorPostModel dataModel);

        void reachedLastItem();

        void clickOnProfile(String id);

        void updateLikeCount(OutDoorPostModel dataModel, boolean isLiked);

        void followUnfollow(FollowModel dataModel, int type);

        void startVideo(ImageView videoThumbNail);
    }


    public void updateLikesCount (OutDoorPostModel dataModel,final boolean isLiked){
        if (mListener != null) {
            mListener.updateLikeCount(dataModel,isLiked);
        }
    }

    public void updateSharesCount(int position) {
        /*
        String mSelectedSubject = SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE, null);
        DatabaseReference databaseReference = ContinentApplication.getFireBaseRef().child(type).child(mSelectedSubject);
        databaseReference = databaseReference.child(dataModelList.get(position).getKey()).child(SHARE_NODE);
        databaseReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                if (currentData.getValue() == null) {
                    currentData.setValue(1);
                } else {
                    currentData.setValue((Long) currentData.getValue() + 1);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });*/
    }

    private void openMoreAlert(OutDoorPostModel dataModel) {

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Log.d(TAG, "onAttachedToRecyclerView: ");
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();

      /*  if (manager instanceof LinearLayoutManager && getItemCount() > 0) {
            final LinearLayoutManager llm = (LinearLayoutManager) manager;
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    Log.d(TAG, "onScrollStateChanged: " + newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visiblePosition = llm.findFirstCompletelyVisibleItemPosition();
                    Log.d(TAG, "onScrolled: visible position:" + visiblePosition);
                    if (visiblePosition > -1) {
                        View v = llm.findViewByPosition(visiblePosition);
                        for (int i = 0; i < dataModelList.size(); i++) {
                            if (i != visiblePosition) {
                                Log.d(TAG, "onScrolled: setting white");
                                v.setBackgroundColor(Color.parseColor("#ffffff"));
                            } else {
                                v.setBackgroundColor(Color.parseColor("#777777"));
                                Log.d(TAG, "onScrolled: setting other");
                            }
                        }
                    }
                }
            });
        }*/
    }

    ApiInterface apiInterface;

    private void setUpNetwork() {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }

    public void stopVideo() {

        if (customPlayerUiController != null) {
            customPlayerUiController.pause();
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

    public static String capitalizeWordDes(String name){
        String firstLetter = name.substring(0, 1);
        String remainingLetters = name.substring(1, name.length());
        firstLetter = firstLetter.toUpperCase();
        name = firstLetter + remainingLetters;
        return name;
    }

    private void onFinishedLoading(String mImageViewURL) {
        if (mProgressBar != null && mImageViewURL != null) {
            mProgressBar.setVisibility(View.GONE);
          //  mImageView.setVisibility(View.VISIBLE);
        }
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    } // Author: silentnuke

}

