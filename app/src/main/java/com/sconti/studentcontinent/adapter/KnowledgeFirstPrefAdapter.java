package com.sconti.studentcontinent.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.sconti.studentcontinent.ContinentApplication;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.YouTubeConfig;
import com.sconti.studentcontinent.model.FirstPreferenceDataModel;
import com.sconti.studentcontinent.model.UserResponseModel;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.LIKE_NODE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SHARE_NODE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_FEEDS;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_RESPONSES;

/**
 * Created by Veerendra Patel on 3/6/19.
 */

public class KnowledgeFirstPrefAdapter extends RecyclerView.Adapter<KnowledgeFirstPrefAdapter.MyViewHolder>  {
    private final String TAG = "KnowledgefAdapter";
    private Context context;
    private boolean isClicked = false;
    private long nbId=0;
    private DecimalFormat formatter;
    private List<FirstPreferenceDataModel> dataModelList;
    private boolean playWhenReady = false;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private SimpleExoPlayer player;
    private SelectItem mListener;
    private MediaController mediaController;
    private String type;

    public KnowledgeFirstPrefAdapter(Context context, List<FirstPreferenceDataModel> dataList, SelectItem selectItem, MediaController media, String type) {
        this.context = context;
        this.dataModelList = dataList;
        this.mListener = selectItem;
        this.type = type;
        this.mediaController = media;
    }
    

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

        public static String extractYTId(String ytUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(ytUrl);
        if(matcher.find()){
            return matcher.group();
        } else {
            return "error";
        }
    }


    @Override
    public int getItemViewType(int position) {
        if(position==dataModelList.size()-1)
        {
            if(mListener!=null)
            {
                mListener.reachedLastItem();
            }
        }
        return position;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder  {
        private CircleImageView avatar;
        private ImageView mImageViewContent, mImageViewMoreOption;
        private TextView mTextViewTitle, mTextViewDescription, mTextViewDateTime, mTextViewViewComments;
        public PlayerView playerView;
        private LinearLayout mShareCommentLayout;
        private ConstraintLayout constraintLayout;
        private ConstraintSet constraintSet;
        private LinearLayout mLinearLayoutLike, mLinearLayoutComment, mLinearLayoutShare;
        private TextView mTextViewLikeCount, mTextViewShareCount, mTextViewCommentCount , mMyTextVisbale;
        public View parent;
        public String url;
      //  private YouTubePlayerSupportFragment youTubePlayerFragment;
        private String YoutubeUrl = "https://www.youtube.com/watch?v=";
       // protected RelativeLayout relativeLayoutOverYouTubeThumbnailView;
      //  YouTubeThumbnailView youTubeThumbnailView;
        protected ImageView playButton;



        MyViewHolder(View view) {
            super(view);
            parent = view;
            avatar = view.findViewById(R.id.circleImageViewProfile);
            mImageViewContent = view.findViewById(R.id.imageViewContent);
            mTextViewDescription = view.findViewById(R.id.textViewContent);



            mTextViewTitle = view.findViewById(R.id.textViewTitle);
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
           // mTextViewViewComments = view.findViewById(R.id.textViewViewcomment);

          /*  relativeLayoutOverYouTubeThumbnailView = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
            youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail);
            playButton=(ImageView)itemView.findViewById(R.id.btnYoutube_player);*/

        }

        public void setView(String url2){
            parent.setTag(this);
            url = url2;
        }

    }


    
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.first_preference_row_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            final FirstPreferenceDataModel dataModel = dataModelList.get(position);

         /*   holder.playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: ");
                    Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) context, "AIzaSyBfpuBtwtxPvLUd0dICK0RG0pcEMOTCj6I", "Lm3OOsqTlKs");
                    context.startActivity(intent);
                }
            });

          */
           // Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) context, "AIzaSyBfpuBtwtxPvLUd0dICK0RG0pcEMOTCj6I", "Lm3OOsqTlKs");
        //    context.startActivity(intent);
            if(dataModel.getPostedByName()!=null)
            {
                holder.mTextViewTitle.setText(dataModel.getPostedByName());
            }
            if(dataModel.getPostedByCollege()!=null)
            {
                holder.mTextViewTitle.setText(dataModel.getPostedByName()+" "+ dataModel.getPostedByCollege());
            }
            holder.mTextViewDescription.setText(dataModel.getDescription());

            if(dataModel.getDatetime()!=0)
            {
                CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(dataModel.getDatetime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                holder.mTextViewDateTime.setText(timeAgo);
            }
            String prfurl =  dataModel.getPosterUrl();
            if(prfurl!=null && !prfurl.isEmpty())
            {
                Picasso.Builder builder = new Picasso.Builder(context);
                builder.listener(new Picasso.Listener()
                {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                    {
                        exception.printStackTrace();
                    }
                });
                builder.build().load(prfurl).into(holder.avatar);
              //  Glide.with(context).load(prfurl).into(holder.avatar);
            }

            if(dataModel.getImageURL()!=null)
            {
                //Picasso.with(context).load(dataModel.getImageURL()).into(holder.mImageViewContent);
                Glide.with(context).load(dataModel.getImageURL()).into(holder.mImageViewContent);
                holder.mImageViewContent.setVisibility(View.VISIBLE);
            }
            else if(dataModel.getVideoURL()!=null && !dataModel.getVideoURL().isEmpty()){
                holder.playerView.setVisibility(View.VISIBLE);
               // initializePlayer(holder.playerView, player, dataModel.getVideoURL());
                holder.setView(dataModel.getVideoURL());
            }

            if(dataModel.getNumComments()!=0)
                holder.mTextViewCommentCount.setText(""+dataModel.getNumComments());



            if(dataModel.getNumLikes()!=0)
                holder.mTextViewLikeCount.setText(""+dataModel.getNumLikes());

            if(dataModel.getNumShares()!=0)
                holder.mTextViewShareCount.setText(""+dataModel.getNumShares());


            holder.mLinearLayoutComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickItem(holder.getAdapterPosition());
                }
            });
            holder.mLinearLayoutLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!dataModel.isLiked())
                    {
                        updateLikesCount(holder.getAdapterPosition(), true);
                    }
                    else
                    {
                        updateLikesCount(holder.getAdapterPosition(), false);
                    }
                }
            });

            holder.mLinearLayoutShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateSharesCount(holder.getAdapterPosition());
                }
            });

            holder.mImageViewMoreOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null)
                        mListener.openAlert(dataModel);
                }
            });
            if(dataModel.getNumComments()>=1)
            {
                holder.mTextViewViewComments.setText("View "+ dataModel.getNumComments()+" Comments");
                holder.mTextViewViewComments.setVisibility(View.VISIBLE);
            }
            else{
                holder.mTextViewViewComments.setVisibility(View.GONE);
            }
            holder.mTextViewViewComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickItem(holder.getAdapterPosition());
                }
            });



        }catch (Exception e){
            e.printStackTrace();
        }




    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        super.onViewRecycled(holder);
       // releasePlayer(player);
    }

    private void initializePlayer(PlayerView playerView, SimpleExoPlayer player, String url) {
       if(mediaController!=null)
       {
           mediaController.playVideo(playerView, player, url);
       }
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(context, "exoplayer-codelab");
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
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
    public interface SelectItem{
        void selectedPosition(int position);
        void openAlert(FirstPreferenceDataModel dataModel);
        void reachedLastItem();
    }
    public interface MediaController{
        void playVideo(PlayerView playerView, SimpleExoPlayer player, String url);
    }


    public void updateLikesCount(int position, final boolean isLiked)
    {
      String mSelectedSubject = SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE, null);
        DatabaseReference databaseReference = ContinentApplication.getFireBaseRef().child(type).child(mSelectedSubject);
        databaseReference = databaseReference.child(dataModelList.get(position).getKey()).child(LIKE_NODE);
        databaseReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                if(isLiked) {
                    if (currentData.getValue() == null) {
                        currentData.setValue(1);
                    } else {
                        currentData.setValue((Long) currentData.getValue() + 1);
                    }
                }
                else {
                    if(currentData.getValue() != null)
                    currentData.setValue((Long) currentData.getValue() - 1);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });

        String userId = SharedPrefsHelper.getInstance().get(USER_ID, null);

        DatabaseReference databaseReference1 = ContinentApplication.getFireBaseRef().child(type).child(USER_RESPONSES).child(USER_FEEDS).child(mSelectedSubject).child(userId).child(dataModelList.get(position).getId());
        UserResponseModel userResponseModel = new UserResponseModel();
        userResponseModel.setId(dataModelList.get(position).getId());
        //userResponseModel.setCommented();
        userResponseModel.setLiked(isLiked);
        databaseReference1.setValue(userResponseModel);

    }
    public void updateSharesCount(int position)
    {
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
        });
    }

    private void openMoreAlert(FirstPreferenceDataModel dataModel){

    }

}


