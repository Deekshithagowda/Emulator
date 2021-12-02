package com.sconti.studentcontinent.activity.aspiration.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.aspiration.fragments.FirstPreferenceFragment;
import com.sconti.studentcontinent.activity.aspiration.fragments.PlaceholderFragment;
import com.sconti.studentcontinent.activity.aspiration.fragments.SecondPreferenceFragment;


/**
 * A [FragmentPagerAdapter] that returns outdooricon fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class AspirationSectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public AspirationSectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        if(position==0)
            return FirstPreferenceFragment.newInstance();
        // else if(position==1)
         //   return SecondPreferenceFragment.newInstance();*/
        // getItem is called to instantiate the fragment for the given page.
        // Return outdooricon PlaceholderFragment (defined as outdooricon static inner class below).
        return SecondPreferenceFragment.newInstance();
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