package com.sconti.studentcontinent.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.LikedUserListActivity;
import com.sconti.studentcontinent.activity.othersprofile.OthersProfileActivity;
import com.sconti.studentcontinent.activity.othersprofile.OthersProfilePresenter;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorPostModel;
import com.sconti.studentcontinent.model.FollowModel;
import com.sconti.studentcontinent.model.LikeDataModel;
import com.sconti.studentcontinent.model.UserDetails;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

public class userLikeListAdapter extends RecyclerView.Adapter<userLikeListAdapter.MyViewHolder> {
    private Context context;
    private List<UserDetails> userDataModelList , dataModelListFull;
    private OthersProfilePresenter mOthersProfilePresenter;
    private String userId;


    public userLikeListAdapter(Context context, List<UserDetails> userDataModelList) {
        this.context = context;
        this.userDataModelList = userDataModelList;
        dataModelListFull = new ArrayList<>(userDataModelList);
    }

    public userLikeListAdapter(){}



    @NonNull
    @Override
    public userLikeListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlikedlist, parent, false);
        return new userLikeListAdapter.MyViewHolder(itemView);
    }

    public Filter getFilter() {
        return dataModelListFilter;
    }

    private Filter dataModelListFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<UserDetails>  filterList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0 ){
                filterList.addAll(dataModelListFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(UserDetails item : dataModelListFull){
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
            userDataModelList.clear();
            userDataModelList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

        @Override
    public void onBindViewHolder(@NonNull userLikeListAdapter.MyViewHolder holder, int position) {
        final UserDetails dataModel = userDataModelList.get(position);
        holder.userName.setText(capitalizeWord(dataModel.getName()));
        holder.collageName.setText(dataModel.getVillage());
        Glide.with(context).load(dataModel.getImageURL()).placeholder(R.drawable.ic_user_profile).into(holder.mImageViewAvatar);
        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dataModel.getUserId().equalsIgnoreCase(SharedPrefsHelper.getInstance().get(USER_ID).toString())) {
                    Intent intent = new Intent(context, OthersProfileActivity.class);
                    intent.putExtra("USER_ID", dataModel.getUserId());
                    context.startActivity(intent);
                }
            }
        });

        if(dataModel.getUserId().equalsIgnoreCase(SharedPrefsHelper.getInstance().get(USER_ID).toString())){
            holder.mFollowerProfileBtn.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return userDataModelList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView userName , collageName ;
        private Button mFollowerProfileBtn;
        private CircleImageView mImageViewAvatar;

        private int IamFollowing, IhaveFollowers;
        private List<FollowModel> mFollowingModelList, mFollowersModelList;
        public MyViewHolder(@NonNull View view) {
            super(view);

            userName = view.findViewById(R.id.UserLikeListUserName);
            collageName = view.findViewById(R.id.UserLikeListCollageName);
            mFollowerProfileBtn = view.findViewById(R.id.FollowerProfileBtn);
            mImageViewAvatar = view.findViewById(R.id.circleImageViewLikeList);

        }


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


}
