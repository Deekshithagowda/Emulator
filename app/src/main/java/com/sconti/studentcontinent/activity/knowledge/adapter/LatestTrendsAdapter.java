package com.sconti.studentcontinent.activity.knowledge.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorPostModel;
import com.sconti.studentcontinent.model.LatestTrendsModel;
import com.sconti.studentcontinent.utils.tools.ImageHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Veerendra Patel on 3/6/19.
 */

public class LatestTrendsAdapter extends RecyclerView.Adapter<LatestTrendsAdapter.MyViewHolder> {
    private final String TAG = "LatestTrendsAdapter";
    private Context context;
    private boolean isClicked = false;
    private long nbId=0;
    private DecimalFormat formatter;
    private List<LatestTrendsModel> dataModelList;
    private boolean playWhenReady = false;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private SimpleExoPlayer player;
    private SelectItem mListener;
    private String type;
    private boolean mExoPlayerFullscreen;
    private int width, height;

    public LatestTrendsAdapter(Context context, List<LatestTrendsModel> dataList) {
        this.context = context;
        this.dataModelList = dataList;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.height = displayMetrics.heightPixels;
         this.width = displayMetrics.widthPixels;
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
        if(position==dataModelList.size()-1)
        {
            if(mListener!=null)
            {
                mListener.reachedLastItem();
            }
        }
        return position;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView avatar;
        public View parent;
        public ImageView mImageViewContent, mImageViewMoreOption;
        public TextView mTextViewTitle, mTextViewDescription, mTextViewDateTime, mTextViewViewComments;
        public PlayerView playerView;
        public LinearLayout mShareCommentLayout;
        public ConstraintLayout constraintLayout;
        public ConstraintSet constraintSet;
        public TextView tag;
        public String url;
        public CardView linearLayoutRoot;
        public LinearLayout mLinearLayoutLike, mLinearLayoutComment, mLinearLayoutShare;
        public TextView mTextViewLikeCount, mTextViewShareCount, mTextViewCommentCount;

        MyViewHolder(View view) {
            super(view);
            parent = view;
        //    mImageViewContent = view.findViewById(R.id.imageView);
            mTextViewTitle = view.findViewById(R.id.textViewTitle);
           // tag = view.findViewById(R.id.tag);
            linearLayoutRoot = view.findViewById(R.id.linearLayoutRoot);
        }
        public void setView(String url2){
            parent.setTag(this);
            url = url2;
        }
    }
    
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.latest_trend_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            final LatestTrendsModel dataModel = dataModelList.get(position);
            holder.mTextViewTitle.setText(dataModel.getTitle());
           // Picasso.with(context).load(dataModel.getImageUrl()).into(holder.mImageViewContent);
           /* Picasso.with(context).load(dataModel.getImageUrl()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    BitmapDrawable background = new BitmapDrawable(ImageHelper.getRoundedCornerBitmap(bitmap, 20));
                    holder.linearLayoutRoot.setBackgroundDrawable(background);

                    //holder.linearLayoutRoot.setMinimumHeight(10);

                  //  holder.mImageViewContent.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bitmap, 20));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });*/
           // holder.linearLayoutRoot.setBa
           // holder.mImageViewContent.setImageBitmap(ImageHelper.getRoundedCornerBitmap(decodedByte, 16));
            //holder.tag.setText(position+"");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onClickItem(int position) {
        if (mListener != null) {
            mListener.selectedPosition(position);
        }
    }
    public interface SelectItem{
        void selectedPosition(int position);
        void openAlert(OutDoorPostModel dataModel);
        void reachedLastItem();
        void clickOnProfile(String id);
    }
}


