package com.sconti.studentcontinent.activity.alltabs.ui.knowledge;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.alltabs.ui.dashboard.DashboardFragment;
import com.sconti.studentcontinent.activity.alltabs.ui.notifications.NotificationsFragment;
import com.sconti.studentcontinent.activity.knowledge.fragments.FirstPreferenceFragment;
import com.sconti.studentcontinent.activity.knowledge.fragments.SecondPreferenceFragment;
import com.sconti.studentcontinent.activity.outdoor.adapter.OutDoorTagsPrefAdapter;
import com.sconti.studentcontinent.model.NewAPIResponseModel;
import com.sconti.studentcontinent.model.UserDetails;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_ASPIRATION;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_ASPIRATION_NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE_NAME;

public class KnowledgeFragment extends Fragment {

    private KnowledgeViewModel mViewModel;

    public static KnowledgeFragment newInstance() {
        return new KnowledgeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View  root =  inflater.inflate(R.layout.knowledge_fragment, container, false);
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
        String subjectSelected = SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString("SELECTED", SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE).toString());
        FirstPreferenceFragment preferenceFragment = new FirstPreferenceFragment();
        preferenceFragment.setArguments(bundle);

        adapter.addFragment(preferenceFragment, subjectSelected);





//        String subjectAspirant = SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_ASPIRATION);
//
//        Bundle bundle2 = new Bundle();
//        SecondPreferenceFragment preferenceFragment2 = new SecondPreferenceFragment();
//        bundle2.putString("SELECTED", subjectAspirant);
//        preferenceFragment2.setArguments(bundle2);
//        adapter.addFragment(preferenceFragment2, getString(R.string.general_knowledge));


        /*Bundle bundle3 = new Bundle();
        FirstPreferenceFragment preferenceFragment3 = new FirstPreferenceFragment();
        bundle3.putString("SELECTED", "tc");
        preferenceFragment3.setArguments(bundle3);
        adapter.addFragment(preferenceFragment3, "Telecom");*/
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