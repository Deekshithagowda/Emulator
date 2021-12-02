package com.sconti.studentcontinent.customview;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.google.android.exoplayer2.ui.PlayerView;
import com.sconti.studentcontinent.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoPlayerViewHolder extends RecyclerView.ViewHolder {

    FrameLayout media_container;
    TextView title;
    ImageView thumbnail, volumeControl;
    ProgressBar progressBar;
    View parent;
    RequestManager requestManager;
    CircleImageView avatar;
    ImageView mImageViewContent, mImageViewMoreOption;
    TextView mTextViewTitle, mTextViewDescription, mTextViewDateTime, mTextViewViewComments;
    PlayerView playerView;
    LinearLayout mShareCommentLayout;
    ConstraintLayout constraintLayout;
    ConstraintSet constraintSet;
    LinearLayout mLinearLayoutLike, mLinearLayoutComment, mLinearLayoutShare;
    TextView mTextViewLikeCount, mTextViewShareCount, mTextViewCommentCount;

    public VideoPlayerViewHolder(@NonNull View itemView) {
        super(itemView);
        parent = itemView;
        media_container = itemView.findViewById(R.id.media_container);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        title = itemView.findViewById(R.id.textViewTitle);
        progressBar = itemView.findViewById(R.id.progressBar);
        mTextViewDateTime = itemView.findViewById(R.id.textViewDateTime);
        mShareCommentLayout = itemView.findViewById(R.id.linearlayoutLikesCommentShare);
        constraintLayout = itemView.findViewById(R.id.constraintLayoutRoot);
        mLinearLayoutComment = itemView.findViewById(R.id.linearlayoutComment);
        mLinearLayoutLike = itemView.findViewById(R.id.linearlayoutLike);
        mLinearLayoutShare = itemView.findViewById(R.id.linearlayoutShare);
        mTextViewLikeCount = itemView.findViewById(R.id.textViewLikeCount);
        mTextViewShareCount = itemView.findViewById(R.id.textViewShareCount);
        mTextViewCommentCount = itemView.findViewById(R.id.textViewCommentCount);
        mImageViewMoreOption = itemView.findViewById(R.id.more_option);
       // mTextViewViewComments = itemView.findViewById(R.id.textViewViewcomment);
        volumeControl = itemView.findViewById(R.id.volume_control);

    }

    public void onBind(MediaObject mediaObject, RequestManager requestManager) {
        this.requestManager = requestManager;
        parent.setTag(this);
        title.setText(mediaObject.getTitle());
       // this.requestManager.load(mediaObject.getThumbnail()).into(thumbnail);
    }

}
