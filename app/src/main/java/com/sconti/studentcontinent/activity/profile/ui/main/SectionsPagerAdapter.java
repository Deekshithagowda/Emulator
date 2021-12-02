package com.sconti.studentcontinent.activity.profile.ui.main;

import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sconti.studentcontinent.R;


/**
 * A [FragmentPagerAdapter] that returns outdooricon fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;
    private String userID;
    private boolean ISFROM_OTHER_PROF;

    public SectionsPagerAdapter(Context context, FragmentManager fm, String userID, boolean ISFROM_OTHER_PROF) {
        super(fm);
        mContext = context;
        this.userID = userID;
        this.ISFROM_OTHER_PROF = ISFROM_OTHER_PROF;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();

        switch (position){
            case  0:
                fragment = new FollowersFragment();
                bundle.putString("USER_ID", userID);
                bundle.putBoolean("ISFROM_OTHER_PROF", ISFROM_OTHER_PROF);
                fragment.setArguments(bundle);
                break;
            case  1:
                fragment = new FollowingFragment();
                bundle.putString("USER_ID", userID);
                bundle.putBoolean("ISFROM_OTHER_PROF", ISFROM_OTHER_PROF);
                fragment.setArguments(bundle);
                break;

        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}