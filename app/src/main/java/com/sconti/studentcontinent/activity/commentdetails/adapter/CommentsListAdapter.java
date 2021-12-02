package com.sconti.studentcontinent.activity.commentdetails.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
import com.sconti.studentcontinent.activity.commentdetails.PostSubCommentActivity;
import com.sconti.studentcontinent.activity.othersprofile.OthersProfileActivity;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorPostModel;
import com.sconti.studentcontinent.activity.profile.ProfileFragment;
import com.sconti.studentcontinent.model.CommentModel;
import com.sconti.studentcontinent.model.FirstPreferenceDataModel;
import com.sconti.studentcontinent.model.LikeDataModel;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;
import com.sconti.studentcontinent.utils.tools.TimeAgo;
import com.squareup.picasso.Picasso;


import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import xyz.hanks.library.bang.SmallBangView;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

/**
 * Created by Veerendra Patel on 3/6/19.
 */

public class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.MyViewHolder> {
    private FirstPreferenceDataModel preferenceDataModel;
    private final String TAG = "KnowledgefAdapter";
    private Context context;
    private boolean isClicked = false;
    private long nbId = 0;
    private DecimalFormat formatter;
    private List<CommentModel> dataModelList;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private SimpleExoPlayer player;
    private SelectItem mListener;
    private String type, mSelectedSubject;
    private String userID;
    List<LikeDataModel> likeDatalist;

    // SmallBangView smallBangView;


    TextView commenterName;


    public CommentsListAdapter(Context context, List<CommentModel> dataList, List<LikeDataModel> likeDatalist, SelectItem selectItem, String type) {
        this.context = context;
        this.dataModelList = dataList;
        this.likeDatalist = likeDatalist;
        this.mListener = selectItem;
        this.type = type;
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
        private TextView mTextViewName, mTextViewDescription, mTextViewDateTime, mTextViewCommentReply, mTextViewNumberOfLike;
        private EditText mSubCommentReplyEditText;
        private PlayerView playerView;
        private ImageView mImageViewSendEditText;
        private LinearLayout mShareCommentLayout;
        private ConstraintLayout constraintLayout;
        private ConstraintSet constraintSet;
        private LinearLayout mLinearLayoutLike, mLinearLayoutComment, mLinearLayoutShare;
        private TextView mTextViewLikeCount, mTextViewShareCount, mTextViewCommentCount;
        private ImageView mImageViewDelete;
        private LinearLayout mCommentLinearLayoutList, mSubCommentReplyEditTextLinearLayout;
        int height, width;
        private boolean isOutdoorComment;
        private boolean isKnowledgeComment, isKnowledgeGeneralComment, isAspirationComment, isAspirationGeneralComment;
        private OutDoorPostModel outDoorPostModel;
        private List<CommentModel> commentModelList;
        private SubCommentsListAdapter mSubCommentsListAdapter;
        private RecyclerView recyclerViewCommentor;
        private ActionBar mActionBar;
        private ProgressBar mProgressCircular;
        private ProgressBar mProgressBar;
        LinearLayout mCommentBox;
        private SmallBangView mcomment_like;
        private ImageView mReply;
        private LinearLayout mReplyLinearLayout , mCommentUserNameLinerLayout;
        CardView mGifCardView;
        private String userId;
        ImageView mGifImageView;


        MyViewHolder(View view) {
            super(view);
            avatar = view.findViewById(R.id.imageViewProfile);
            mTextViewName = view.findViewById(R.id.commentorName);
            mTextViewDescription = view.findViewById(R.id.commentorDesc);
            mReply = view.findViewById(R.id.reply_txt);

            mImageViewDelete = view.findViewById(R.id.imageViewDelete);
            mImageViewSendEditText = view.findViewById(R.id.imageViewSendEditText);
            mCommentLinearLayoutList = view.findViewById(R.id.CommentLinearLayoutList);
            mSubCommentReplyEditText = view.findViewById(R.id.SubCommentReplyEditText);

            mSubCommentReplyEditTextLinearLayout = view.findViewById(R.id.SubCommentReplyEditTextLinearLayout);
            mProgressCircular = view.findViewById(R.id.ProgressCircular);
            recyclerViewCommentor = view.findViewById(R.id.SubCommentReplyRecyclerView);

            mTextViewNumberOfLike = view.findViewById(R.id.textViewReplyLike);
            mTextViewCommentReply = view.findViewById(R.id.textViewCommentReply);
            mTextViewDateTime = view.findViewById(R.id.commentTime);
            mcomment_like = view.findViewById(R.id.comment_like);
            mCommentBox = view.findViewById(R.id.comment_box);
            commenterName = view.findViewById(R.id.commentorName);
            mCommentUserNameLinerLayout = view.findViewById(R.id.CommentUserNameLinerLayout);
            mReplyLinearLayout = view.findViewById(R.id.replyLinearLayout);
            mGifCardView=view.findViewById(R.id.gifCardView);
            mGifImageView=view.findViewById(R.id.gifImageView);

        }
        

      /*  private CommentsListAdapter.SelectItem selectItemInterface = new CommentsListAdapter.SelectItem() {
            @Override
            public void selectedPosition(CommentModel commentModel) {
                //updateCommentCount(commentModel);
             //  deleteComment(commentModel);
               // Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
            }

        };*/


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            final CommentModel dataModel = dataModelList.get(position);
            Log.d(TAG, "onBindViewHolder: " + dataModel.getCommentDesc());

            if (likeDatalist != null) {
                // boolean isLiked = false;
                for (int i = 0; i < likeDatalist.size(); i++) {

                    if (likeDatalist.get(i).getPostID().equalsIgnoreCase(dataModel.getSl_no())) {
                        if (Integer.parseInt(likeDatalist.get(i).getLikeCount()) > 0) {
                            holder.mcomment_like.setSelected(true);
                            //  Log.d(TAG, "onBindViewHolder:liked data "+likeDatalist.get(i).getPostID()+" == "+dataModelList.get(i).getSl_no());
                            //  break;
                        }

                    }
                   /* else
                            {
                                holder.mcomment_like.setSelected(false);
                            }*/
                }
                // dataModel.setLiked(isLiked);
            }


            if (dataModel.getCommentedById().equalsIgnoreCase(userID)) {
                holder.mImageViewDelete.setVisibility(View.VISIBLE);
            } else {
                holder.mImageViewDelete.setVisibility(View.GONE);
            }


            holder.mcomment_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.mcomment_like.isSelected()) {
                        updateLikesCount(dataModel, false);
                        holder.mcomment_like.setSelected(false);
                    } else {
                        updateLikesCount(dataModel, true);
                        holder.mcomment_like.setSelected(true);
                        holder.mcomment_like.likeAnimation();
                    }
                }
            });

            holder.mCommentUserNameLinerLayout.setOnClickListener(new View.OnClickListener() {
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






            holder.mReplyLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PostSubCommentActivity.class);
                    intent.putExtra("DATA", dataModelList.get(position));
                    intent.putExtra("TYPE", type);
                    Log.d(TAG, "mReplyLinearLayout"+type);
                    context.startActivity(intent);
                }
            });
            holder.mCommentBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* Intent intent = new Intent(context, PostSubCommentActivity.class);
                    intent.putExtra("DATA", dataModelList.get(position));
                    intent.putExtra("TYPE", type);
                    context.startActivity(intent);*/

                    //commentID
                    //  Toast.makeText(context, "Comment "+dataModelList.get(position).getSl_no()+"  "+type, Toast.LENGTH_SHORT).show();
                }
            });

            if(dataModel.getCommentDesc()!=null) {
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

            }
           /* else{
                holder.mTextViewDescription.setVisibility(View.GONE);
            }*/



            if (dataModel.getNumLikes() != 0) {
                holder.mTextViewNumberOfLike.setVisibility(View.VISIBLE);
                holder.mTextViewNumberOfLike.setText(String.valueOf(dataModel.getNumLikes()));
                Log.d(TAG, "onBindViewHolder:like count "+String.valueOf(dataModel.getNumLikes()));

            } else {
                holder.mTextViewNumberOfLike.setVisibility(View.INVISIBLE);
                holder.mcomment_like.setSelected(false);
            }


            if (dataModel.getNumComments() != 0){

                holder.mTextViewCommentReply.setText("View " + dataModel.getNumComments() + " replies");
            }

            String myDate = dataModelList.get(position).getCreated_on();
            holder.mImageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickItemPosition(dataModelList.get(position).getSl_no(), dataModelList.get(position).getCommentDesc(),dataModelList.get(position).getNumComments()+"",dataModelList.get(position).getPostedBy(), dataModelList.get(holder.getAdapterPosition()));
                }
            });


            if (dataModel.getCreated_on() != null) {
               /* SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                TimeZone zone = TimeZone.getTimeZone("UTC");
                dateFormat.setTimeZone(zone);
                Date pasTime = dateFormat.parse(dataModel.getCreated_on());

                CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(pasTime.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
*/
                holder.mTextViewDateTime.setText(TimeAgo.covertStringTimeToText(dataModel.getCreated_on()));
              //  Log.d(TAG, dataModelList.get(position).getCreated_on() + " onBindViewHolder1: " + timeAgo);

            }
            if(holder.mTextViewName!=null)
                holder.mTextViewName.setText(capitalizeWord(dataModel.getCommentedByName()));
            if (dataModel.getCommentedById().equalsIgnoreCase(userID)) {
                holder.mImageViewDelete.setVisibility(View.VISIBLE);
            } else {
                holder.mImageViewDelete.setVisibility(View.GONE);
            }

          /*  holder.mImageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickItem(dataModel);
                }
            });*/
   // String prfurl = dataModel.getCommentorURL();

           /* if (dataModel.getCommentorURL() != null || !dataModel.getCommentorURL().trim().isEmpty())
                Picasso.with(context).load(dataModel.getCommentorURL()).placeholder(R.drawable.ic_user_profile).into(holder.avatar);
*/
            if (!TextUtils.isEmpty(dataModel.getCommentorURL())) {
                Picasso.with(context).load(dataModel.getCommentorURL()).placeholder(R.drawable.ic_user_profile).into(holder.avatar);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
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

    public void onClickItem(CommentModel commentModel) {
        if (mListener != null) {
            mListener.selectedPosition(commentModel);
            //Toast.makeText(context, " Deleted", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickItemPosition(String position, String desc,String numberOfComments,String PostByID, CommentModel commentModel) {
        if (mListener != null) {
            mListener.selectedItemPosition(position, desc,numberOfComments,PostByID, commentModel);
        }
    }

    public void updateLikesCount(CommentModel dataModel, final boolean isLiked) {
        if (mListener != null) {
            mListener.updateCommentLikeCount(dataModel, isLiked);
        }
    }

    public interface SelectItem {
        void selectedPosition(CommentModel commentModel);

        void selectedItemPosition(String position, String description, String numberOfComments, String postBy, CommentModel commentModel);

        void updateCommentLikeCount(CommentModel dataModel, boolean isLiked);

    }


}


