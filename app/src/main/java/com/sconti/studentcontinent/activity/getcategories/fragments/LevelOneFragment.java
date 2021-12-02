package com.sconti.studentcontinent.activity.getcategories.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.getcategories.CategoryAdapter;
import com.sconti.studentcontinent.activity.getcategories.LevelOneAdapter;
import com.sconti.studentcontinent.model.CategoryModel;
import com.sconti.studentcontinent.model.LevelOneModel;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LevelOneFragment extends Fragment {
    private static final String TAG = "LevelOneFragment";

    private ApiInterface apiInterface;
    private List<LevelOneModel> categoryModelList;
    private GridView mGridview;
    private LevelOneAdapter adapter;
    private OnFragmentInteractionListener mListener;
    private View mRootView;
    private String mCategoryId;
    public LevelOneFragment() {
        // Required empty public constructor
    }

    public static LevelOneFragment newInstance(String param1, String param2) {
        LevelOneFragment fragment = new LevelOneFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null)
        {
            Bundle bundle = getArguments();
            mCategoryId = bundle.getString("CAT_ID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_category, container, false);
        initViews();
        initListener();
        setUpNetwork();
        getCategoryData(mCategoryId);
        return mRootView;
    }

    private void initListener() {
        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickOnLevelOne(categoryModelList.get(i));
            }
        });
    }

    private void initViews() {
        mGridview = mRootView.findViewById(R.id.gridview);
    }
    private void setUpNetwork()
    {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }
    private void getCategoryData(String mCategoryId) {
        Call<List<LevelOneModel>> apiInterfaceCategoryModels = apiInterface.getLevelOne(mCategoryId);
        apiInterfaceCategoryModels.enqueue(new Callback<List<LevelOneModel>>() {
            @Override
            public void onResponse(Call<List<LevelOneModel>> call, Response<List<LevelOneModel>> response) {
                if(response.isSuccessful())
                {
                    categoryModelList = response.body();
                    adapter = new LevelOneAdapter(getContext(), categoryModelList);
                    mGridview.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<LevelOneModel>> call, Throwable t) {

            }
        });
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void clickOnLevelOne(LevelOneModel uri) {
        if (mListener != null) {
            mListener.onLevelOneSelected(uri);
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
        void onLevelOneSelected(LevelOneModel uri);
    }
}
