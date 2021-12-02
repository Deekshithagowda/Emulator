package com.sconti.studentcontinent.activity.appinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.sconti.studentcontinent.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;


    public SliderAdapter(Context context) {
        this.context = context;
    }

    public int[] slide_image = {
            R.drawable.group9705,
            R.drawable.ic_group_9658,
            R.drawable.ic_group_9659
    };

    public String[] slide_headlines = {
            "Connect",
            "Aspire",
            "Explore"
    };

    public String[] slide_des = {
            "You can able to connect with different colleges and different universities across India.",
            "start early preparation of your aspirations like IAS , IPS , Placements , GATE and much more.",
            "Go beyond studies explore your hobbies like Travel , photography , painting , music and much more."
    };

    @Override
    public int getCount() {
        return slide_headlines.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object ;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.appinfo_slide, container, false);

        ImageView SlideImageView = view.findViewById(R.id.BgImage);
        TextView text1 = view.findViewById(R.id.Headlines);
        TextView text2 = view.findViewById(R.id.Des);


        SlideImageView.setImageResource(slide_image[position]);
        text1.setText(slide_headlines[position]);
        text2.setText(slide_des[position]);


        container.addView(view);

        return view;


    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((LinearLayout)object);
    }

}
