package com.sconti.studentcontinent.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.othersprofile.OthersProfileActivity;
import com.sconti.studentcontinent.activity.othersprofile.OthersProfilePresenter;
import com.sconti.studentcontinent.activity.profile.ProfileFragment;
import com.sconti.studentcontinent.model.FollowModel;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

/**
 * Created by Veerendra Patel on 3/6/19.
 */

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.MyViewHolder> {
    private final String TAG = "OutDoorTagsPrefAdapter";
    private Context context;
    private boolean isClicked = false;
    private long nbId=0;
    private DecimalFormat formatter;
    private List<FollowModel> mFollowersModelList , mCommonList, myFollowingList;
    private boolean playWhenReady = false;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private SimpleExoPlayer player;
    private String type;
    private boolean mExoPlayerFullscreen;
    private OthersProfilePresenter mOthersProfilePresenter;
    private String userId;
    private TextView mShowFollowUnFollow;
    private boolean isOtherProfile = true;


    public FollowerAdapter(Context context, List<FollowModel> mFollowersModelList, List<FollowModel> myFollowingList, SelectItem selectItem, boolean isOtherProfile) {
        this.context = context;
        this.mFollowersModelList = mFollowersModelList;
        this.mCommonList = new ArrayList<>(mFollowersModelList);
        this.myFollowingList = myFollowingList;
        this.mListener = selectItem;
        this.isOtherProfile = isOtherProfile;
        this.mCommonList.retainAll(myFollowingList);

    }

    @Override
    public int getItemCount() {
        return mFollowersModelList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView avatar;
        public View parent;
        public ImageView mImageViewContent, mImageViewMoreOption;
        public TextView mTextViewTitle, mTextViewDescription, mTextViewDateTime, mTextViewViewComments , userName , collageName;;
        public PlayerView playerView;
        public LinearLayout mShareCommentLayout;
        public ConstraintLayout constraintLayout;
        public ConstraintSet constraintSet;
        public SimpleExoPlayer simpleExoPlayer;
        public TextView tag;
        public String url;
        public LinearLayout mLinearLayoutLike, mLinearLayoutComment, mLinearLayoutShare;
        public TextView mTextViewLikeCount, mTextViewShareCount, mTextViewCommentCount;
        private ImageView mImageViewFullScreen;
        private ImageView mFullScreenIcon;
        private Dialog mFullScreenDialog;
        public Button mShowFollowUnFollow;
        private OthersProfilePresenter mOthersProfilePresenter;
        private String userId;
        private boolean mExoPlayerFullscreen = false;

        MyViewHolder(View view) {
            super(view);
            parent = view;
           // mTextViewTitle = view.findViewById(R.id.textViewFollowName);
            userName = view.findViewById(R.id.UserFollowListUserName);
            collageName = view.findViewById(R.id.UserFollowListCollageName);
            mShowFollowUnFollow = view.findViewById(R.id.ShowFollowUnFollow);
        }
        public void setView(String url2){
            parent.setTag(this);
            url = url2;
        }

        private int IamFollowing, IhaveFollowers;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
            //holder.mShowFollowUnFollow.setText(context.getString(R.string.unfollow));
                final FollowModel dataModel = mFollowersModelList.get(position);
                if(dataModel.getName()!=null)
                {
                    boolean isFollowing = false;
                    holder.userName.setText(capitalizeWord(dataModel.getName()));
                    holder.collageName.setText(dataModel.getVillage());
                    holder.userName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(dataModel.getUserId().equalsIgnoreCase(SharedPrefsHelper.getInstance().get(USER_ID).toString())) {
                                Intent intent = new Intent(context, ProfileFragment.class);
                                intent.putExtra("USER_ID", dataModel.getUserId());
                                context.startActivity(intent);
                            }else {
                                Intent intent = new Intent(context, OthersProfileActivity.class);
                                intent.putExtra("USER_ID", dataModel.getUserId());
                                context.startActivity(intent);
                            }
                        }
                    });

                        for(int j = 0; j<mCommonList.size(); j++ ) {
                            if (mCommonList.get(j).getUserId().equalsIgnoreCase(dataModel.getUserId())) {
                                isFollowing = true;
                                break;
                            }
                    }

                }
      /*  if(this.isOtherProfile)
        {
            holder.mShowFollowUnFollow.setVisibility(View.GONE);
        }
        else
        {
            holder.mShowFollowUnFollow.setVisibility(View.VISIBLE);
        }*/

    }




    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        super.onViewRecycled(holder);
        Log.d(TAG, "onViewRecycled: fromtagadapter");
    }

    public Filter getFilter() {
        return mFollowersModelListFilter;
    }
    private SelectItem mListener;

    private Filter mFollowersModelListFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<FollowModel>  filterList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0 ){
                filterList.addAll(mFollowersModelList);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(FollowModel item : mFollowersModelList){
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filterList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filterList;
            return  results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mFollowersModelList.clear();
            mFollowersModelList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
    public interface SelectItem{
        void followUnfollow(FollowModel dataModel, int type);
    }

    protected ApiInterface apiInterface;
    private void setUpNetwork() {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }

    public static String capitalizeWord(String str){
        String words[]=str.split("\\s");
        String capitalizeWord="";
        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeWord+=first.toUpperCase()+afterfirst+" ";
        }
        return capitalizeWord.trim();
    }

    private static void getSimilarList(List<FollowModel> followList, List<FollowModel> followerList) {
        List<FollowModel> finalList = new ArrayList<>();
        if (followList.size() >= followerList.size() && (!followList.isEmpty() && !followerList.isEmpty())) {
            for (int i = 0; i < followList.size(); i++) {
                for (FollowModel follow : followerList) {
                    if (followList.get(i).getUserId().equals(follow.getUserId())) {
                        finalList.add(followList.get(i));
                        System.out.println("Follow");
                        System.out.println("Position " + i + " id " + followList.get(i).getUserId() + " Name " + followList.get(i).getName());
                    }
                }
            }
            //return finalList;
        }
        else if(followList.size()<followerList.size() && (!followList.isEmpty() && !followerList.isEmpty()))
        {
            for (FollowModel followe : followerList) {
                for (int j = 0; j < followList.size(); j++) {
                    if (followe.getUserId().equals(followList.get(j).getUserId())) {
                        finalList.add(followList.get(j));
                        System.out.println("Un Follow");
                        System.out.println("Position B " + j + " id " + followList.get(j).getUserId() + " Name " + followList.get(j).getName());
                    }
                }
            }
            //  return finalList;
        }
        //  return finalList;

    }

}


