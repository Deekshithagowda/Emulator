package com.sconti.studentcontinent.activity.starterscreen;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DatabaseReference;
import com.sconti.studentcontinent.ContinentApplication;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.base.BaseFragment;
import com.sconti.studentcontinent.utils.AppUtils;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;


public class CollegeNameFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;
    private View mView;
    private List<String> CollegeName;
    private String selectedCollege;
    private String userID;
    private ChipGroup mTagsChipGroup;


    public CollegeNameFragment() {
        // Required empty public constructor
    }

    public static CollegeNameFragment newInstance(String param1, String param2) {
        CollegeNameFragment fragment = new CollegeNameFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           // mParam1 = getArguments().getString(ARG_PARAM1);
           // mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_college_name, container, false);
        initViews();
        initListener();
       // initializeDegreeData();
        return  mView;
    }

    private void initListener() {

    }
    private void initializeDegreeData() {
        userID = SharedPrefsHelper.getInstance().get(USER_ID);

        CollegeName = new ArrayList<>();
        CollegeName.add("Manipal");
        CollegeName.add("MVJCE");
        CollegeName.add("UVCE");
        if(CollegeName!=null && CollegeName.size()>0)
        {
            for(int i=0; i<CollegeName.size(); i++)
            {
                mTagsChipGroup.setVisibility(View.VISIBLE);
                Chip chip = new Chip(mActivity);
                //style="@style/Widget.MaterialComponents.Chip.Filter"
                ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(mActivity,
                        null,
                        0,
                        R.style.Widget_MaterialComponents_Chip_Filter);
                chip.setChipDrawable(chipDrawable);
                chip.setText(CollegeName.get(i));
                chip.setTextSize(20f);
                mTagsChipGroup.addView(chip);
            }
        }
        mTagsChipGroup.invalidate();
        Chip chip =(Chip)mTagsChipGroup.getChildAt(0);
        chip.setChecked(true);
        mTagsChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                if (chip != null)
                {
                    //getCategory(chip.getText().toString());
                    selectedCollege = chip.getText().toString();
                    SharedPrefsHelper.getInstance().save(AppUtils.Constants.COLLEGE_NAME, selectedCollege);
                    DatabaseReference databaseReference = ContinentApplication.getFireBaseRef().child(AppUtils.Constants.COLLEGE_NAME).child(userID).child(AppUtils.Constants.COLLEGE_NAME);
                 //   databaseReference.setValue(selectedCollege);
                }
            }
        });
    }
    private void initViews() {
        mTagsChipGroup = mView.findViewById(R.id.HashChipGroup);
    }

    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onFragmentCollegeSelcted(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentCollegeSelcted(String collegeName);
    }
}
