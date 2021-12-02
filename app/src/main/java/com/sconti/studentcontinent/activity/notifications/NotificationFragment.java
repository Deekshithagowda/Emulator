package com.sconti.studentcontinent.activity.notifications;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.details.DetailsActivity;
import com.sconti.studentcontinent.activity.notifications.adapter.CommentsNotificationsAdapter;
import com.sconti.studentcontinent.activity.notifications.model.OutDoorPostModel;
import com.sconti.studentcontinent.activity.notifications.model.UserNotificationsModel;
import com.sconti.studentcontinent.activity.outdoor.adapter.OutDoorTagsPrefAdapter;
import com.sconti.studentcontinent.base.BaseFragment;
import com.sconti.studentcontinent.model.NewAPIResponseModel;
import com.sconti.studentcontinent.model.NotificationDataModel;
import com.sconti.studentcontinent.model.NotificationReadModel;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;
import com.sconti.studentcontinent.utils.AppUtils;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;


public class NotificationFragment extends BaseFragment {
    private static final String TAG = "NotificationFragment";
    private OnFragmentInteractionListener mListener;
    private View mView;
    private RecyclerView mRecyclerView;
    private RelativeLayout mProgressBar;
    private List<NotificationDataModel> notificationDataModelList;
    private List<NotificationReadModel> notificationReadModelsList;
    private TextView textViewNoData;
    private LinearLayout mCancelButton;
    private Button mCancleButton;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public NotificationFragment() {
        // Required empty public constructor
    }


    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
       /* args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
           /* mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_notification, container, false);
        initViews();
        getData();

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        return mView;
    }

    private CommentsNotificationsAdapter.SelectItem selectItem = new CommentsNotificationsAdapter.SelectItem() {
        @Override
        public void selectedPosition(int position) {

        }

        @Override
        public void openAlert(OutDoorPostModel dataModel) {

        }

        @Override
        public void reachedLastItem() {

        }
    };

    CommentsNotificationsAdapter commentsNotificationsAdapter;

    private void getData() {
        setUpNetwork();
        String UserID = SharedPrefsHelper.getInstance().get(USER_ID);
        //   mProgressBar.setVisibility(View.VISIBLE);
        Call<NewAPIResponseModel> userNotificationsModelCall = apiInterface.GetNotificationsByID(UserID, 1);
        userNotificationsModelCall.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                //     mProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                notificationDataModelList = new ArrayList<>();
                notificationDataModelList = response.body().getmNotificationDataList();
                if (notificationDataModelList.size() > 0) {
                    Collections.reverse(notificationDataModelList);
                    commentsNotificationsAdapter = new CommentsNotificationsAdapter(mActivity, notificationDataModelList, selectItem);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
                    mRecyclerView.setAdapter(commentsNotificationsAdapter);
                    mRecyclerView.setVisibility(View.VISIBLE);

                    notificationReadModelsList = new ArrayList<>();
                    for(NotificationReadModel notificationDataModel : notificationReadModelsList){
                        String NotID = notificationDataModel.getNotificationID();
                        Call<NewAPIResponseModel> userNotificationsModelCall = apiInterface.MakeNotificationRead(notificationDataModel.getType(), 1, NotID);
                        userNotificationsModelCall.enqueue(new Callback<NewAPIResponseModel>() {
                            @Override
                            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                                if (response.isSuccessful() && response.body().isStatus()) {

                                }
                            }

                            @Override
                            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                                //    mProgressBar.setVisibility(View.GONE);
                            }
                        });
                    }


//                    textViewNoData.setVisibility(View.GONE);
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    //       textViewNoData.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                //    mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void ReadAllData() {
        setUpNetwork();
        NotificationDataModel dataModel = new NotificationDataModel();


    }

    private void initViews() {
        mRecyclerView = mView.findViewById(R.id.recycler_view);
        mCancelButton = mView.findViewById(R.id.cancel_button);
        //   mProgressBar = mView.findViewById(R.id.progress_bar);
        //   textViewNoData = mView.findViewById(R.id.no_data);
        mSwipeRefreshLayout = mView.findViewById(R.id.swipeLayout);
        mCancleButton = mView.findViewById(R.id.CancleButton);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });

        mCancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                getData();
            }
        });
    }

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    protected ApiInterface apiInterface;

    private void setUpNetwork() {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
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
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        ReadAllData();


    }
}
