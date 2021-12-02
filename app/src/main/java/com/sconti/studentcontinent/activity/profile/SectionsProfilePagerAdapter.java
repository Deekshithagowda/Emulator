package com.sconti.studentcontinent.activity.profile;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.knowledge.fragments.FirstPreferenceFragment;

public class SectionsProfilePagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.BioData, R.string.knowledge , R.string.aspiration, R.string.outdoor};
    private final Context mContext;
    private String userID;

    public SectionsProfilePagerAdapter(@NonNull FragmentManager fm, Context mContext, String userID) {
        super(fm);
        this.mContext = mContext;
        this.userID = userID;
    }

    public SectionsProfilePagerAdapter(@NonNull FragmentManager fm, int behavior, Context mContext, String userID) {
        super(fm, behavior);
        this.mContext = mContext;
        this.userID = userID;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();

        switch (position){
            case  0:
                fragment = new GeneralProfileFragment();
                bundle.putString("USER_ID", userID);
                fragment.setArguments(bundle);
                break;
            case  1:
                fragment = new KnowledgeProfileFragment();
                bundle.putString("USER_ID", userID);
                fragment.setArguments(bundle);
                break;
            case  2:
                fragment = new AspirationProfileFragment();
                bundle.putString("USER_ID", userID);
                fragment.setArguments(bundle);
                break;
            case  3:
                fragment = new OutdoorProfileFragment();
                bundle.putString("USER_ID", userID);
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
        return 4;
    }
}