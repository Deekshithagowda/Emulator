package com.sconti.studentcontinent.utils.tools;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.sconti.studentcontinent.ContinentApplication;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.VideoImageActivity;

public class PopUpClass {

    PlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    String url,mediaTypes;
    PhotoView photoView;
    final Context context = ContinentApplication.getInstance().getBaseContext();
    public void showPopupWindow(final View view,String mtype,String mediaUrl) {

        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.pop_up_layout, null);

        exoPlayerView = (PlayerView)popupView.findViewById(R.id.videoFullScreenPlayer);
        photoView = (PhotoView)popupView.findViewById(R.id.photo_view);


        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        boolean focusable = true;


        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        dimBehind(popupWindow);
        popupWindow.setOutsideTouchable(true);
        popupWindow.getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if(exoPlayer!=null) {
                    exoPlayer.setPlayWhenReady(false);
                    exoPlayer.getPlaybackState();
                }
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
                if(exoPlayer!=null) {
                    exoPlayer.setPlayWhenReady(false);
                    exoPlayer.getPlaybackState();
                }
            }
        });



        mediaTypes=mtype;
        if(mediaTypes.equals("video")) {
            exoPlayerView.setVisibility(View.VISIBLE);
            photoView.setVisibility(View.INVISIBLE);
            url = mediaUrl;
            try {
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
              //  exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                Uri videoURI = Uri.parse(url);
                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
                exoPlayerView.setPlayer(exoPlayer);
                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(mediaTypes.equals("image"))
        {
            exoPlayerView.setVisibility(View.INVISIBLE);
            photoView.setVisibility(View.VISIBLE);
            url = mediaUrl;
            Glide.with(context).load(url).into(photoView);
        }

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                  //  Toast.makeText(context, "BACK PRESS", Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();
                    if(exoPlayer!=null) {
                        exoPlayer.setPlayWhenReady(false);
                        exoPlayer.getPlaybackState();
                    }
                }

                return true;
            }
        });

       /* pw.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    pw.dismiss();
                }
                return true;
            }
        });*/



    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // Override back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (pw.isShowing()) {
                pw.dismiss();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }*/


   /* public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d("back", "back button pressed");
        }
        return true;
    }*/
    public static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.8f;
        wm.updateViewLayout(container, p);
    }




}

