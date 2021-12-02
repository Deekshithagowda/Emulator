package com.sconti.studentcontinent.activity.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.base.BaseFragment;

public class SecondPreferenceFragment extends BaseFragment {

    private SecondPreferenceViewModel mViewModel;

    public static SecondPreferenceFragment newInstance() {
        return new SecondPreferenceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.second_preference_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SecondPreferenceViewModel.class);
        // TODO: Use the ViewModel
    }

}
