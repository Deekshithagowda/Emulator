package com.sconti.studentcontinent.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.AspirationActivity;
import com.sconti.studentcontinent.activity.GameActivity;
import com.sconti.studentcontinent.activity.alltabs.SecondMainActivity;
import com.sconti.studentcontinent.activity.aspiration.AspirationTabActivity;
import com.sconti.studentcontinent.activity.games.GamesTabActivity;
import com.sconti.studentcontinent.activity.knowledge.KnowledgeTabActivity;
import com.sconti.studentcontinent.activity.OutdoorActivity;
import com.sconti.studentcontinent.activity.outdoor.OutDoorTabActivity;
import com.sconti.studentcontinent.base.BaseFragment;

public class HomeFragment extends BaseFragment {

    private HomeViewModel homeViewModel;
    private View root;
    private Button mOutDoorButton;
    private Button mGameButton;
    private Button mAspirationButton;
    private Button mKnowledgeButton;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home_main, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
              //  textView.setText(s);
            }
        });
        initViews();
        return root;
    }

    private void initViews(){
        mOutDoorButton = root.findViewById(R.id.outDoor);
        mOutDoorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, OutDoorTabActivity.class));
            }
        });

        mAspirationButton = root.findViewById(R.id.aspiration);
        mAspirationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, AspirationTabActivity.class));
            }
        });

        mGameButton = root.findViewById(R.id.gamesButton);
        mGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, GamesTabActivity.class));
            }
        });

        mKnowledgeButton = root.findViewById(R.id.knowledge);
        mKnowledgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, KnowledgeTabActivity.class));
            }
        });
    }
}