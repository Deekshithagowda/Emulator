import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.sconti.studentcontinent.R;

public class VideoFragment extends Fragment {

    View view;
    PlayerView videoFullScreenPlayer;
    SimpleExoPlayer player;

    PlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    String videoURL = "http://blueappsoftware.in/layout_design_android_blog.mp4";
    String url,mediaTypes;
    PhotoView photoView;


    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getContext() fragment
        view = inflater.inflate(R.layout.fragment_video, container, false);
        exoPlayerView = (PlayerView) view.findViewById(R.id.videoFullScreenPlayer);
        photoView = (PhotoView)view.findViewById(R.id.photo_view);

        Bundle mBundle = new Bundle();
        if(mBundle!=null)
        {
            mBundle = getArguments();
            mediaTypes=mBundle.getString("mediaType");

            if(mediaTypes.equals("video")) {
                exoPlayerView.setVisibility(View.VISIBLE);
                photoView.setVisibility(View.INVISIBLE);
                url = mBundle.getString("url");
                try {
                    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                    exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
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
                url = mBundle.getString("url");
                Glide.with(getContext()).load(url).into(photoView);
            }
        }



        //   mp4VideoUri = Uri.parse("http://live.field59.com/wwsb/ngrp:wwsb1_all/playlist.m3u8"); //ABC NEWS



        String ur = "https://www.radiantmediaplayer.com/media/big-buck-bunny-360p.mp4";



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

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

    private void stopPlayer(PlayerView pv, SimpleExoPlayer absPlayer) {
        if(exoPlayer!=null) {
            pv.setPlayer(null);
            absPlayer.release();
            absPlayer = null;
        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (!isVisibleToUser)   // If we are becoming invisible, then...
            {
                //pause or stop video


                if (exoPlayer != null) {

                    exoPlayer.setPlayWhenReady(false);


                }


            }
        }
    }
}