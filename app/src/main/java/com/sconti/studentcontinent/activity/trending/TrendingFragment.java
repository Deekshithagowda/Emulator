package com.sconti.studentcontinent.activity.trending;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.alltabs.SecondMainActivity;
import com.sconti.studentcontinent.activity.commentdetails.PostSubCommentActivity;
import com.sconti.studentcontinent.activity.details.DetailsActivity;
import com.sconti.studentcontinent.base.BaseFragment;
import com.sconti.studentcontinent.model.NewAPIResponseModel;
import com.sconti.studentcontinent.model.NotificationDataModel;
import com.sconti.studentcontinent.model.TrendLongList;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;
import com.sconti.studentcontinent.utils.AppUtils;
import com.sconti.studentcontinent.utils.RecyclerItemClickListener;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;
import com.sconti.studentcontinent.utils.tools.ContinentTools;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.COLLEGE_NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_ASPIRATION;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_ASPIRATION_NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE_NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;


public class TrendingFragment extends BaseFragment {
    View rootView;
    protected ApiInterface apiInterface;
    List<TrendLongList> trendLingList;
    public static final String TAG = "TrendingFragment";
    // TextView textView;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    String outdoorcategory, aspirationcategory, knowledgecategory;
    TrendingAdapterClass adapter;
    List<TrendingItemClass> itemClasses;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    int type;
    String st1 = "SELECTED_SUBJECT_KNOWLEDGE_NAME " + SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE_NAME);
    String st2 = "SELECTED_SUBJECT_ASPIRATION " + SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_ASPIRATION);
    String st3 = "COLLEGE_NAME " + SharedPrefsHelper.getInstance().get(COLLEGE_NAME);

    String st4 = "SELECTED_SUBJECT_KNOWLEDGE_NAME " + SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE_NAME);
    String st5 = "SELECTED_SUBJECT_ASPIRATION_NAME " + SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_ASPIRATION_NAME);
    //String st6="SELECTED_SUBJECT_ASPIRATION_NAME "+SharedPrefsHelper.getInstance().get(SeleCA);


    //String st2="SELECTED_SUBJECT_ASPIRATION "+SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_ASPIRATION);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_trending, container, false);
        aspirationcategory = "UPSC";
        knowledgecategory = "CS_IS_IT_ENG";
        outdoorcategory = "Default";

        type = 1;
        // mSelectedSubject =SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE_NAME);;

        initView();
        setUpNetwork();
        // Toast.makeText(mActivity, " "+st1, Toast.LENGTH_SHORT).show();
        Log.i("onCreateView: ", st1 + "\n " + st2 + "\n " + st3 + "\n " + st4 + "\n " + st5);


        mSwipeRefreshLayout = rootView.findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ContinentTools.hasInternetConnection(getContext())) {
                    getTrend(outdoorcategory, aspirationcategory, knowledgecategory);
                }
            }
        });

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (ContinentTools.hasInternetConnection(getContext())) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    getTrend(outdoorcategory, aspirationcategory, knowledgecategory);
                }
            }
        });


        LinearLayout linearLayout = ((SecondMainActivity) getActivity()).mToolbar.findViewById(R.id.Notification_LinearLayout);
        LinearLayout AppIcon = ((SecondMainActivity) getActivity()).mToolbar.findViewById(R.id.App_icon);

        LinearLayout.LayoutParams trending = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        trending.setMargins(0, 0, 70, 0);
        linearLayout.setLayoutParams(trending);

        LinearLayout.LayoutParams trending1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        trending1.setMargins(0, 0, 30, 0);
        AppIcon.setLayoutParams(trending1);


        return rootView;
    }

    private void listner() {
        itemClasses = new ArrayList<>();

        itemClasses.add(new TrendingItemClass(TrendingItemClass.LayoutOne,
                "Knowledge"));
        itemClasses.add(new TrendingItemClass(
                TrendingItemClass.LayoutTwo, trendLingList.get(0).getProfURL(),
                trendLingList.get(0).getDescription(), trendLingList.get(0).getPostedByName()));
        itemClasses.add(new TrendingItemClass(
                TrendingItemClass.LayoutTwo, trendLingList.get(1).getProfURL(),
                trendLingList.get(1).getDescription(), trendLingList.get(1).getPostedByName()));
        itemClasses.add(new TrendingItemClass(
                TrendingItemClass.LayoutThree, trendLingList.get(2).getProfURL(),
                trendLingList.get(2).getDescription(), trendLingList.get(2).getPostedByName()));

        itemClasses.add(new TrendingItemClass(TrendingItemClass.LayoutOne,
                "Aspiration"));
        itemClasses.add(new TrendingItemClass(
                TrendingItemClass.LayoutTwo, trendLingList.get(3).getProfURL(),
                trendLingList.get(3).getDescription(), trendLingList.get(3).getPostedByName()));
        itemClasses.add(new TrendingItemClass(
                TrendingItemClass.LayoutTwo, trendLingList.get(4).getProfURL(),
                trendLingList.get(4).getDescription(), trendLingList.get(4).getPostedByName()));
        itemClasses.add(new TrendingItemClass(
                TrendingItemClass.LayoutThree, trendLingList.get(5).getProfURL(),
                trendLingList.get(5).getDescription(), trendLingList.get(5).getPostedByName()));
        itemClasses.add(new TrendingItemClass(TrendingItemClass.LayoutOne,
                "Outdoor"));
       /* itemClasses.add(new TrendingItemClass(
                TrendingItemClass.LayoutTwo,trendLingList.get(6).getProfURL(),
                trendLingList.get(6).getDescription(), trendLingList.get(6).getPostedByName()));
        itemClasses.add(new TrendingItemClass(
                TrendingItemClass.LayoutTwo,trendLingList.get(7).getProfURL(),
                trendLingList.get(7).getDescription(), trendLingList.get(7).getPostedByName()));
        itemClasses.add(new TrendingItemClass(
                TrendingItemClass.LayoutThree,trendLingList.get(8).getProfURL(),
                trendLingList.get(8).getDescription(), trendLingList.get(8).getPostedByName()));*/
        adapter = new TrendingAdapterClass(getContext(), itemClasses);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (position == 1) {
                            NotificationDataModel notify = new NotificationDataModel();
                            notify.setType(1);
                            notify.setPostID(trendLingList.get(0).getSl_no());
                            Intent intent = new Intent(getContext(), DetailsActivity.class);
                            intent.putExtra(AppUtils.Constants.NOTIFICATION_DATA, notify);
                            intent.putExtra(AppUtils.Constants.NOTIFICATION_DETAILS, true);
                            intent.putExtra("KNOWLEDGE_COMMENT", true);
                            /*  intent.putExtra(AppUtils.Constants.TRENDING_CATEGORY,"isKnowledgeComment");*/
                            getContext().startActivity(intent);
                        } else if (position == 2) {
                            //  Toast.makeText(mActivity, trendLingList.get(2).getPostId(), Toast.LENGTH_SHORT).show();
                            NotificationDataModel notify = new NotificationDataModel();
                            notify.setType(1);
                            notify.setPostID(trendLingList.get(1).getSl_no());
                            Intent intent = new Intent(getContext(), DetailsActivity.class);
                            intent.putExtra(AppUtils.Constants.NOTIFICATION_DATA, notify);
                            intent.putExtra(AppUtils.Constants.NOTIFICATION_DETAILS, true);
                            intent.putExtra("KNOWLEDGE_COMMENT", true);
                            getContext().startActivity(intent);
                        } else if (position == 3) {
                            // Toast.makeText(mActivity, trendLingList.get(3).getPostId(), Toast.LENGTH_SHORT).show();
                            NotificationDataModel notify = new NotificationDataModel();
                            notify.setType(1);
                            notify.setPostID(trendLingList.get(2).getSl_no());
                            Intent intent = new Intent(getContext(), DetailsActivity.class);
                            intent.putExtra(AppUtils.Constants.NOTIFICATION_DATA, notify);
                            intent.putExtra(AppUtils.Constants.NOTIFICATION_DETAILS, true);
                            intent.putExtra("KNOWLEDGE_COMMENT", true);
                            getContext().startActivity(intent);
                        } else if (position == 5) {
                            // Toast.makeText(mActivity, trendLingList.get(4).getPostId(), Toast.LENGTH_SHORT).show();

                            NotificationDataModel notify = new NotificationDataModel();
                            notify.setType(1);
                            notify.setPostID(trendLingList.get(3).getSl_no());
                            Intent intent = new Intent(getContext(), DetailsActivity.class);
                            intent.putExtra(AppUtils.Constants.NOTIFICATION_DATA, notify);
                            intent.putExtra(AppUtils.Constants.NOTIFICATION_DETAILS, true);
                            intent.putExtra("KNOWLEDGE_COMMENT", true);
                            getContext().startActivity(intent);
                           /* NotificationDataModel notify=new NotificationDataModel();
                            notify.setType(3);
                            notify.setPostID(trendLingList.get(3).getSl_no());
                            Intent intent = new Intent(getContext(), DetailsActivity.class);
                            intent.putExtra(AppUtils.Constants.NOTIFICATION_DATA, notify);
                            intent.putExtra(AppUtils.Constants.NOTIFICATION_DETAILS , true);
                            intent.putExtra("ASPIRATION_COMMENT", true);
                            getContext().startActivity(intent);*/
                        } else if (position == 6) {
                            NotificationDataModel notify = new NotificationDataModel();
                            notify.setType(1);
                            notify.setPostID(trendLingList.get(4).getSl_no());
                            Intent intent = new Intent(getContext(), DetailsActivity.class);
                            intent.putExtra(AppUtils.Constants.NOTIFICATION_DATA, notify);
                            intent.putExtra(AppUtils.Constants.NOTIFICATION_DETAILS, true);
                            intent.putExtra("KNOWLEDGE_COMMENT", true);
                            getContext().startActivity(intent);
                           /* NotificationDataModel notify=new NotificationDataModel();
                            notify.setType(3);
                            notify.setPostID(trendLingList.get(4).getSl_no());
                            Intent intent = new Intent(getContext(), DetailsActivity.class);
                            intent.putExtra(AppUtils.Constants.NOTIFICATION_DATA, notify);
                            intent.putExtra(AppUtils.Constants.NOTIFICATION_DETAILS , true);
                            intent.putExtra("ASPIRATION_COMMENT", true);
                            getContext().startActivity(intent);*/
                        } else if (position == 7) {
                            //Toast.makeText(mActivity, trendLingList.get(6).getPostId(), Toast.LENGTH_SHORT).show();
                            NotificationDataModel notify = new NotificationDataModel();
                            notify.setType(1);
                            notify.setPostID(trendLingList.get(5).getSl_no());
                            Intent intent = new Intent(getContext(), DetailsActivity.class);
                            intent.putExtra(AppUtils.Constants.NOTIFICATION_DATA, notify);
                            intent.putExtra(AppUtils.Constants.NOTIFICATION_DETAILS, true);
                            intent.putExtra("KNOWLEDGE_COMMENT", true);
                            getContext().startActivity(intent);
                           /* NotificationDataModel notify=new NotificationDataModel();
                            notify.setType(3);
                            notify.setPostID(trendLingList.get(5).getSl_no());
                            Intent intent = new Intent(getContext(), DetailsActivity.class);
                            intent.putExtra(AppUtils.Constants.NOTIFICATION_DATA, notify);
                            intent.putExtra(AppUtils.Constants.NOTIFICATION_DETAILS , true);
                            intent.putExtra("ASPIRATION_COMMENT", true);
                            getContext().startActivity(intent);*/
                        } else if (position == 9) {
                            Toast.makeText(mActivity, trendLingList.get(7).getPostId(), Toast.LENGTH_SHORT).show();
                        } else if (position == 10) {
                            Toast.makeText(mActivity, trendLingList.get(8).getPostId(), Toast.LENGTH_SHORT).show();
                        } else if (position == 11) {
                            Toast.makeText(mActivity, trendLingList.get(9).getPostId(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
      /*  adapter.setOnItemClickListener(new TrendingAdapterClass.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.d(TAG, "onItemClick position: " + position);
            }

            @Override
            public void onItemLongClick(int position, View v) {
                Log.d(TAG, "onItemLongClick pos = " + position);
            }
        });*/
    }

    private void initView() {
        recyclerView = rootView.findViewById(R.id.recycler_view);
        layoutManager
                = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

    }

    private void setUpNetwork() {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }

    private void getTrend(String knowledgeCategory, String aspirationCategory, String OutdoorCategory) {
        recyclerView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(true);
        Call<NewAPIResponseModel> call = apiInterface.GetTrendings(knowledgeCategory, aspirationCategory, OutdoorCategory);
        call.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                trendLingList = response.body().getTrendLingList();
                if (trendLingList != null && response.isSuccessful() && !trendLingList.isEmpty()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    listner();
                }
            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {

            }
        });
    }


}