package com.sconti.studentcontinent.activity.commentdetails.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.othersprofile.OthersProfileActivity;
import com.sconti.studentcontinent.activity.profile.ProfileFragment;
import com.sconti.studentcontinent.model.CommentModel;
import com.sconti.studentcontinent.model.LikeDataModel;
import com.sconti.studentcontinent.model.UserDetails;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;
import com.sconti.studentcontinent.utils.tools.TimeAgo;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import xyz.hanks.library.bang.SmallBangView;

import static android.view.View.GONE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

public class SubCommentsListAdapter  extends RecyclerView.Adapter<SubCommentsListAdapter.MyViewHolder> {
    private final String TAG = "KnowledgefAdapter";
    private Context context;
    private boolean isClicked = false;
    private long nbId=0;
    private DecimalFormat formatter;
    private List<CommentModel> dataModelList;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private SimpleExoPlayer player;
    private SubCommentsListAdapter.SelectItem mListener;
    private String type;
    private String userID;
    private List<LikeDataModel> likeDatalist;



    public SubCommentsListAdapter(Context context, List<CommentModel> dataList,  List<LikeDataModel> likeDatalist,SelectItem selectItem, String type) {
        this.context = context;
        this.dataModelList = dataList;
        this.mListener = selectItem;
        this.type = type;
        this.likeDatalist=likeDatalist;
        userID = SharedPrefsHelper.getInstance().get(USER_ID);
    }


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
        return position;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView avatar;
        private TextView mTextViewName, mTextViewDescription, mTextViewDateTime , mTextViewCommentReply;
        private PlayerView playerView;
        private ImageView mTextViewDelete;
       // private ImageView mImageViewDelete;
        private LinearLayout mShareCommentLayout , mCommentDesLinearLayout;
        private ConstraintLayout constraintLayout;
        private ConstraintSet constraintSet;
        private LinearLayout mLinearLayoutLike, mLinearLayoutComment, mLinearLayoutShare;
        private TextView mTextViewLikeCount, mTextViewShareCount, mTextViewCommentCount;
        private SmallBangView smallBangView;
        private TextView mtextViewReplySubLike,mTextViewCommentSubReply;
        ConstraintLayout mConstraintLayout;
        CardView mGifCardView;
        ImageView mGifImageView;

        MyViewHolder(View view) {
            super(view);
            avatar = view.findViewById(R.id.imageViewSubProfile);
            mTextViewName = view.findViewById(R.id.ommentorSubName);
            mTextViewDescription = view.findViewById(R.id.commentorSubDesc);
            mTextViewDateTime = view.findViewById(R.id.commentSubTime);
            mTextViewDelete = view.findViewById(R.id.imageViewSubDelete);
            smallBangView=view.findViewById(R.id.comment_sub_like);
            mtextViewReplySubLike=view.findViewById(R.id.textViewReplySubLike);
            mConstraintLayout=view.findViewById(R.id.box_item);
            mGifCardView=view.findViewById(R.id.gifCardViewSub);
            mGifImageView=view.findViewById(R.id.gifImageViewSub);
            mCommentDesLinearLayout = view.findViewById(R.id.CommentDesLinearLayout);
          //  mTextViewCommentSubReply=view.findViewById(R.id.textViewCommentSubReply);
           // mTextViewCommentReply = view.findViewById(R.id.textViewCommentReplySubComment);




        }
    }

    @Override
    public SubCommentsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_replay, parent, false);
        return new SubCommentsListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        try {
            final CommentModel dataModel = dataModelList.get(position);
            if (likeDatalist != null) {
                for (int i = 0; i < likeDatalist.size(); i++) {
                    if (likeDatalist.get(i).getPostID().equalsIgnoreCase(dataModel.getSl_no())) {
                        if (Integer.parseInt(likeDatalist.get(i).getLikeCount()) > 0) {
                            holder.smallBangView.setSelected(true);
                        }
                    }
                }
            }
            if(dataModel.getCommentDesc().length() > 0 ) {
                holder.mTextViewDescription.setVisibility(View.VISIBLE);
                holder.mTextViewDescription.setText(capitalizeWordDes(dataModel.getCommentDesc().trim()));
                String regex = "(https|http)://(media.giphy.com/media/)[a-zA-Z0-9]+(/giphy.gif)";
                Pattern pattern=Pattern.compile(regex);
                Matcher matcher=pattern.matcher(dataModel.getCommentDesc());
                if(matcher.find())
                {

                    holder.mGifCardView.setVisibility(View.VISIBLE);
                    holder.mGifImageView.setVisibility(View.VISIBLE);
                    Glide.with(context).load(matcher.group()).into(holder.mGifImageView);
                    //  System.out.println(matcher.group());
                    Log.i(TAG, "GIF found: "+matcher.group());
                    String finals=dataModel.getCommentDesc().replace(matcher.group(),"");
                    // System.out.println(dataModel.getDescription().trim());
                    holder.mTextViewDescription.setText(capitalizeWordDes(finals.trim()));
                }
                else
                {
                    holder.mGifCardView.setVisibility(View.GONE);
                    holder.mGifImageView.setVisibility(View.GONE);
                    holder.mTextViewDescription.setText(capitalizeWordDes(dataModel.getCommentDesc().trim()));
                }

            }else {
                holder.mTextViewDescription.setVisibility(GONE);
            }
          //  holder.mTextViewDescription.setText(dataModel.getCommentDesc());
            /*holder.mConstraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, " "+position, Toast.LENGTH_SHORT).show();
                }
            });*/
            if (dataModel.getCommentedById().equalsIgnoreCase(userID)) {
                holder.mTextViewDelete.setVisibility(View.VISIBLE);
            } else {
                holder.mTextViewDelete.setVisibility(View.GONE);
            }
            if(dataModel.getCreated_on()!=null)
            {
                /*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                TimeZone zone=TimeZone.getTimeZone("UTC");
                dateFormat.setTimeZone(zone);
                Date pasTime = dateFormat.parse(dataModel.getCreated_on());*/
               // CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(pasTime.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
               // holder.mTextViewDateTime.setText(timeAgo);

                holder.mTextViewDateTime.setText(TimeAgo.covertStringTimeToText(dataModel.getCreated_on()));

            }
            if(dataModel.getNumLikes()>0)
            {
                holder.mtextViewReplySubLike.setText(dataModel.getNumLikes()+"");
            }
            else
                {
                    holder.mtextViewReplySubLike.setVisibility(GONE);
                }
            if(dataModel.getCommentedByName()!=null) {
                holder.mTextViewName.setText(capitalizeWord(dataModel.getCommentedByName()));
            }

            if(dataModel.getNumComments()>0)
            {
                if(holder.mTextViewCommentCount!=null)
                {
                    holder.mTextViewCommentCount.setText(dataModel.getNumComments()+"");
                }
            }
            else {
                if(holder.mTextViewCommentCount!=null) {
                    holder.mTextViewCommentCount.setVisibility(GONE);
                }
            }
          /*  if(likeDatalist!=null) {
              //  boolean isLiked = false;
                for (int i = 0; i < likeDatalist.size(); i++) {

                    if (likeDatalist.get(i).getPostID().equals(dataModel.getSl_no()))
                    {
                        if(Integer.parseInt(likeDatalist.get(i).getLikeCount())>0) {
                            holder.smallBangView.setSelected(true);
                            //isLiked = true;
                            //break;
                        }
                    }
                }
               // dataModel.setLiked(isLiked);
            }*/

            holder.mTextViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if (dataModel.getCommentedById().equals(userID)) {
                        intent = new Intent(context, ProfileFragment.class);
                    } else {
                        intent = new Intent(context, OthersProfileActivity.class);
                    }
                    intent.putExtra("USER_ID", dataModel.getCommentedById());
                    context.startActivity(intent);
                }
            });

            holder.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if (dataModel.getCommentedById().equals(userID)) {
                        intent = new Intent(context, ProfileFragment.class);
                    } else {
                        intent = new Intent(context, OthersProfileActivity.class);
                    }
                    intent.putExtra("USER_ID", dataModel.getCommentedById());
                    context.startActivity(intent);
                }
            });

            holder.mTextViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickItemPosition(dataModel.getSl_no(),dataModel.getNumComments()+"");
                }
            });
            holder.smallBangView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.smallBangView.isSelected())
                    {
                        updateLikesCount(dataModel,false);
                        holder.smallBangView.setSelected(false);
                    }
                    else
                    {
                        updateLikesCount(dataModel, true);
                        holder.smallBangView.setSelected(true);
                        holder.smallBangView.likeAnimation();
                    }
                }
            });

            String prfurl =  dataModel.getCommentorURL();
            if(prfurl!=null) {
                Picasso.with(context).load(prfurl).into(holder.avatar);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onViewRecycled(@NonNull SubCommentsListAdapter.MyViewHolder holder) {
        super.onViewRecycled(holder);
        releasePlayer(player);
    }

    private void initializePlayer(PlayerView playerView, SimpleExoPlayer player, String url) {
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
    public void onClickItemPosition(String position,String commentCount) {
        if (mListener != null) {
            mListener.deleteSelectedItemPosition(position,commentCount);
        }
    }
    public void onClickItem(CommentModel commentModel) {
        if (mListener != null) {
            mListener.selectedPosition(commentModel);
        }
    }
    public void updateLikesCount (CommentModel dataModel,final boolean isLiked){
        if (mListener != null) {
            mListener.updateCommentLikeCount(dataModel,isLiked);
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


    public interface SelectItem{
        void selectedPosition(CommentModel commentModel);
        void deleteSelectedItemPosition(String position,String commentCount);
        void updateCommentLikeCount(CommentModel dataModel, boolean isLiked);
    }

}
