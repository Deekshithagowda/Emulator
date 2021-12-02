package com.sconti.studentcontinent.activity.getcategories.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.getcategories.CategoryAdapter;
import com.sconti.studentcontinent.activity.getcategories.GetCategoryActivity;
import com.sconti.studentcontinent.model.CategoryModel;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {
    private static final String TAG = "CategoryFragment";
    private ApiInterface apiInterface;
    private List<CategoryModel> categoryModelList;
    private GridView mGridview;
    private CategoryAdapter adapter;
    private OnFragmentInteractionListener mListener;
    private View mRootView;
    private String categoryValue;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create outdooricon new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle=getArguments();
            categoryValue = bundle.getString("CATEGORY");
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
        getCategoryData();
        return mRootView;
    }

    private void initListener() {
        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               onCategoryClick(categoryModelList.get(i));
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
    private void getCategoryData() {
        Call<List<CategoryModel>> apiInterfaceCategoryModels = apiInterface.getCategoryModels(categoryValue);
        apiInterfaceCategoryModels.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if(response.isSuccessful())
                {
                    categoryModelList = response.body();
                    adapter = new CategoryAdapter(getContext(), categoryModelList);
                    mGridview.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onCategoryClick(CategoryModel uri) {
        if (mListener != null) {
            mListener.onCategorySelected(uri);
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
        void onCategorySelected(CategoryModel uri);
    }
}
