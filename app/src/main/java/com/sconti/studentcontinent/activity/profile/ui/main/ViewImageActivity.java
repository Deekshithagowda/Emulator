package com.sconti.studentcontinent.activity.profile.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.sconti.studentcontinent.R;
import com.squareup.picasso.Picasso;

public class ViewImageActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView title;
    private String my_title, imgPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        imageView = findViewById(R.id.imageView);
        title = findViewById(R.id.title);

        imgPath = getIntent().getExtras().getString("image");
        my_title = getIntent().getExtras().getString("title");

        if (!TextUtils.isEmpty(my_title)) {
            title.setText(my_title);
        }

        if (!TextUtils.isEmpty(imgPath)) {

            ViewCompat.setTransitionName(imageView, imgPath);
            Picasso.with(this).load(imgPath)
                    .placeholder(R.drawable.ic_user_profile)
                    .into(imageView);

        } else {
            ViewCompat.setTransitionName(imageView, "NotAvailable");
            Picasso.with(this).load(R.drawable.ic_user_profile)
                    .placeholder(R.drawable.ic_user_profile)
                    .into(imageView);
        }

    }
    }
