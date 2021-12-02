package com.sconti.studentcontinent.activity.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sconti.studentcontinent.ContinentApplication;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.adapter.KnowledgeFirstPrefAdapter;
import com.sconti.studentcontinent.base.BaseFragment;
import com.sconti.studentcontinent.model.FirstPreferenceDataModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FirstPreferenceFragment extends BaseFragment {

    private FirstPreferenceViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private KnowledgeFirstPrefAdapter firstPrefAdapter;
    private View mRootView;
    private EditText mEditTextTitle, mEditTextDescription;
    private String mImageURL, mVideoURL;
    private Button mButtonSubmit;
    private ImageView mImageViewAttachment;
    private DatabaseReference databaseReference;
    private List<FirstPreferenceDataModel> preferenceDataModelList;

    public static FirstPreferenceFragment newInstance() {
        return new FirstPreferenceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mRootView =  inflater.inflate(R.layout.first_preference_fragment, container, false);
        initViews();
        initListener();
        initData();
        getData();
        return mRootView;
    }

    private void initData() {

    }

    private KnowledgeFirstPrefAdapter.SelectItem selectItemInterface;

    private void getData() {
        databaseReference = ContinentApplication.getFireBaseRef();
        databaseReference = databaseReference.child("knowledge_first");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                preferenceDataModelList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    preferenceDataModelList.add(snapshot.getValue(FirstPreferenceDataModel.class));
                }

                Collections.reverse(preferenceDataModelList);
                firstPrefAdapter = new KnowledgeFirstPrefAdapter(mActivity, preferenceDataModelList, selectItemInterface, null,"");
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
                mRecyclerView.setAdapter(firstPrefAdapter);
               // mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initListener() {
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });
    }

    private void initViews() {
        mEditTextTitle = mRootView.findViewById(R.id.editTextTitle);
        mEditTextDescription = mRootView.findViewById(R.id.editTextDetails);
        mButtonSubmit = mRootView.findViewById(R.id.submitButton);
        mImageViewAttachment = mRootView.findViewById(R.id.imageViewAttach);
        mRecyclerView = mRootView.findViewById(R.id.recycler_view);

    }
    private void submitData()
    {
        FirstPreferenceDataModel firstPreferenceDataModel;
        firstPreferenceDataModel = new FirstPreferenceDataModel();
        firstPreferenceDataModel.setTitle(mEditTextTitle.getText().toString());
        firstPreferenceDataModel.setDescription(mEditTextDescription.getText().toString());
        firstPreferenceDataModel.setDatetime(System.currentTimeMillis());
        firstPreferenceDataModel.setImageURL(mImageURL);
        firstPreferenceDataModel.setVideoURL(mVideoURL);
        databaseReference = ContinentApplication.getFireBaseRef().child("knowledge_first");
        databaseReference.push().setValue(firstPreferenceDataModel);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FirstPreferenceViewModel.class);
    }

}
