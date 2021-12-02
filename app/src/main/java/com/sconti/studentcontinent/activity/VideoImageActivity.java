package com.sconti.studentcontinent.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

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
import com.sconti.studentcontinent.R;

public class VideoImageActivity extends AppCompatActivity {
    View view;
    PlayerView videoFullScreenPlayer;
    SimpleExoPlayer player;

    PlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    String videoURL = "http://blueappsoftware.in/layout_design_android_blog.mp4";
    String url,mediaTypes;
    PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_image);
        exoPlayerView = (PlayerView)findViewById(R.id.videoFullScreenPlayer);
        photoView = (PhotoView)findViewById(R.id.photo_view);
        Intent intent = getIntent();
        if(intent!=null)
        {
            mediaTypes=intent.getStringExtra("mediaType");
            if(mediaTypes.equals("video")) {
                exoPlayerView.setVisibility(View.VISIBLE);
                photoView.setVisibility(View.INVISIBLE);
                url = intent.getStringExtra("url");
                try {
                    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                    exoPlayer = ExoPlayerFactory.newSimpleInstance(VideoImageActivity.this, trackSelector);
                    exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                    Uri videoURI = Uri.parse(url);
                    DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                    ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                    MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
                    exoPlayerView.setPlayer(exoPlayer);
                    exoPlayer.prepare(mediaSource);
                    exoPlayer.setPlayWhenReady(true);
                } catch (Exception e) {
                    // Log.e("MainAcvtivity"," exoplayer error "+ e.toString());
                }
            }
            else if(mediaTypes.equals("image"))
            {
                exoPlayerView.setVisibility(View.INVISIBLE);
                photoView.setVisibility(View.VISIBLE);
                url = intent.getStringExtra("url");
                Glide.with(VideoImageActivity.this).load(url).into(photoView);
            }

        }


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(exoPlayer!=null) {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.getPlaybackState();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(exoPlayer!=null) {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.getPlaybackState();
        }
    }
}