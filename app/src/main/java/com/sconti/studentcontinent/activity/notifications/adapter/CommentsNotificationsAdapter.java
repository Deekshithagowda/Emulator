package com.sconti.studentcontinent.activity.notifications.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.sconti.studentcontinent.activity.details.DetailsActivity;
import com.sconti.studentcontinent.activity.notifications.model.OutDoorPostModel;
import com.sconti.studentcontinent.activity.notifications.model.UserNotificationsModel;
import com.sconti.studentcontinent.activity.post.PostActivity;
import com.sconti.studentcontinent.model.APIResponseModel;
import com.sconti.studentcontinent.model.NewAPIResponseModel;
import com.sconti.studentcontinent.model.NotificationDataModel;
import com.sconti.studentcontinent.model.notify.Data;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;
import com.sconti.studentcontinent.utils.AppUtils;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.ASPIRATION_FIRST;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.ASPIRATION_FIRST_GENERAL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.KNOWLEDGE_FIRST;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.KNOWLEDGE_FIRST_GENERAL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.OUTDOOR_FIRST;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

/**
 * Created by Veerendra Patel on 3/6/19.
 */

public class CommentsNotificationsAdapter extends RecyclerView.Adapter<CommentsNotificationsAdapter.MyViewHolder> {
    private final String TAG = "CommentsNotifications";
    private Context context;
    private boolean isClicked = false;
    private long nbId = 0;
    private DecimalFormat formatter;
    private List<NotificationDataModel> dataModelList;
    private boolean playWhenReady = false;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private SimpleExoPlayer player;
    private SelectItem mListener;

    public CommentsNotificationsAdapter(Context context, List<NotificationDataModel> dataList, SelectItem selectItem) {
        this.context = context;
        this.dataModelList = dataList;
        this.mListener = selectItem;
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
        if (position == dataModelList.size() - 1) {
            if (mListener != null) {
                mListener.reachedLastItem();
            }
        }
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView mCommentUserImage;
        // private LinearLayout mnotificationCardView;
        private LinearLayout mNotificationLinerLayout;
        private ImageView mImageViewContent, mImageViewMoreOption;
        private TextView mTextViewTitle, mTextViewDescription, mTextViewDateTime, mTextViewViewComments, mCommentTime;
        private PlayerView playerView;
        private LinearLayout mShareCommentLayout;
        private ConstraintLayout constraintLayout;
        private ConstraintSet constraintSet;
        private LinearLayout mLinearLayoutLike, mLinearLayoutComment, mLinearLayoutShare;
        private TextView mTextViewLikeCount, mTextViewShareCount, mTextViewCommentCount;


        MyViewHolder(View view) {
            super(view);

            mTextViewDescription = view.findViewById(R.id.textViewData);
            mCommentUserImage = view.findViewById(R.id.CommentUserImage);
            mCommentTime = view.findViewById(R.id.CommentTime);
            mNotificationLinerLayout = view.findViewById(R.id.notificationLinerLayout);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            final NotificationDataModel dataModel = dataModelList.get(position);
            if (dataModel != null) {


//                if(dataModel.getIsRead() == 0){
//                    holder.mNotificationLinerLayout.setCardBackgroundColor(Color.GREEN);
//                }else {
//                    holder.mNotificationLinerLayout.setCardBackgroundColor(Color.WHITE);
//                }


//
//                String myDate = dataModel.getCreated_on();
//                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//                Date date = sdf.parse(myDate);
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(date);
//                Log.d(TAG, "onBindViewHolder1: "+Math.abs(calendar.getTimeInMillis()));
//                CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(calendar.getTimeInMillis(), System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS);
//

                if (dataModel.getCreated_on() != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    TimeZone zone = TimeZone.getTimeZone("UTC");
                    dateFormat.setTimeZone(zone);
                    Date pasTime = dateFormat.parse(dataModel.getCreated_on());

                    CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(pasTime.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

                    holder.mCommentTime.setText(timeAgo);
                    Log.d(TAG, dataModelList.get(position).getCreated_on() + " onBindViewHolder1: " + timeAgo);

                }


                if (dataModel.getType() == 1) {
                    String str = "<font color='black'> " + capitalizeWord(dataModel.getName()) + " </font>" + "\n" + "Commented on your post ";
                    holder.mTextViewDescription.setText(Html.fromHtml(str));
                    holder.mCommentUserImage.setImageResource(R.drawable.ic_comment_notification_ico);
                    //  String str = "<font color='black'> " + dataModel.getName() + " </font>" + "\n" + "commented on your post " + "<font color='black'> \"" + dataModel.getDescription() + "\". </font> ";
                }
                if (dataModel.getType() == 2) {
                    String str = "<font color='black'> " + capitalizeWord(dataModel.getName()) + " </font>" + "\n" + "liked your post ";
                    holder.mTextViewDescription.setText(Html.fromHtml(str));
                    holder.mCommentUserImage.setImageResource(R.drawable.ic_like_icon_notification);
                }

                if (dataModel.getType() == 3) {
                    String str = "<font color='black'> " + capitalizeWord(dataModel.getName()) + " </font>" + "\n" + "Shared your post ";
                    holder.mTextViewDescription.setText(Html.fromHtml(str));
                    holder.mCommentUserImage.setImageResource(R.drawable.ic_share_icon_notification);
                }


                // Glide.with(context).load(dataModel.getProfileURL()).into(holder.mCommentUserImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dataModel.getIsDeleted() == 0) {
                            setUpNetwork();
                            String NotID = dataModel.getSl_no();
//                            Call<NewAPIResponseModel> userNotificationsModelCall = apiInterface.UpdateNotificationStatus(dataModel.getType(), 1, NotID);
//                            userNotificationsModelCall.enqueue(new Callback<NewAPIResponseModel>() {
//                                @Override
//                                public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
//                                    if (response.isSuccessful() && response.body().isStatus()) {
                            // holder.mNotificationLinerLayout.setCardBackgroundColor(Color.WHITE);
                            Intent intent = new Intent(context, DetailsActivity.class);
                            intent.putExtra(AppUtils.Constants.NOTIFICATION_DATA, dataModel);
                            intent.putExtra(AppUtils.Constants.NOTIFICATION_DETAILS, true);
                            context.startActivity(intent);
//                                    } else {
//                                        Toast.makeText(context, "" + R.string.already_registered + " 2", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
//                                    Toast.makeText(context, "" + R.string.some_thing_wrong + " 1", Toast.LENGTH_SHORT).show();
//
//                                }
//                            });

                        } else {
                            Toast.makeText(context, "Sorry this post was Delete ", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }

        } catch (
                Exception e) {
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

    public interface SelectItem {
        void selectedPosition(int position);

        void openAlert(OutDoorPostModel dataModel);

        void reachedLastItem();

    }


    public void updateLikesCount(int position, final boolean isLiked) {
     /*
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
        databaseReference1.setValue(userResponseModel);*/

    }

    public void updateSharesCount(int position) {/*
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

    protected ApiInterface apiInterface;

    private void setUpNetwork() {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }


}


