package com.sconti.studentcontinent.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.sconti.studentcontinent.R;

public class YouTubePlayer extends YouTubeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube_player);
    }
}