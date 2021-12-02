package com.sconti.studentcontinent.activity.alltabs.ui.games;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.alltabs.ui.dashboard.DashboardFragment;
import com.sconti.studentcontinent.activity.alltabs.ui.notifications.NotificationsFragment;
import com.sconti.studentcontinent.activity.games.fragments.FirstPreferenceFragment;

import java.util.ArrayList;
import java.util.List;

public class GamesFragment extends Fragment {

    private GamesViewModel mViewModel;

    public static GamesFragment newInstance() {
        return new GamesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root  = inflater.inflate(R.layout.games_fragment, container, false);
        ViewPager viewPager = (ViewPager) root.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) root.findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);


        return root;

    }


    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {


        Adapter adapter = new Adapter(getChildFragmentManager());
        //String subjectSelected = SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE);
        Bundle bundle = new Bundle();
        bundle.putString("SELECTED", "cs");
        FirstPreferenceFragment preferenceFragment = new FirstPreferenceFragment();
        preferenceFragment.setArguments(bundle);
        adapter.addFragment(preferenceFragment, "For You");

        Bundle bundle2 = new Bundle();
        FirstPreferenceFragment preferenceFragment2 = new FirstPreferenceFragment();
        bundle2.putString("SELECTED", "mech");
        preferenceFragment2.setArguments(bundle2);
        adapter.addFragment(preferenceFragment2, "Mechanical");


        Bundle bundle3 = new Bundle();
        FirstPreferenceFragment preferenceFragment3 = new FirstPreferenceFragment();
        bundle3.putString("SELECTED", "tc");
        preferenceFragment3.setArguments(bundle3);
        adapter.addFragment(preferenceFragment3, "Telecom");
        // adapter.addFragment(new MonthFixturesFragment(), "Month");
        // adapter.addFragment(new AllFixturesFragment(), "Month");
        // adapter.addFragment(new MyTeamsFixturesFragment(), "My Teams");
        viewPager.setAdapter(adapter);



    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



}