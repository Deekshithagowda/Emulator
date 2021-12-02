package com.sconti.studentcontinent.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.othersprofile.OthersProfileActivity;
import com.sconti.studentcontinent.activity.profile.ProfileFragment;
import com.sconti.studentcontinent.activity.ui.main.FirstPreferenceFragment;
import com.sconti.studentcontinent.model.UserDetails;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sconti.studentcontinent.activity.commentdetails.adapter.SubCommentsListAdapter.capitalizeWordDes;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

public class ProfileUserListAdapter extends RecyclerView.Adapter<ProfileUserListAdapter.MyViewHolder> {
    private Context context;
    private List<UserDetails> userDataModelList , userDetailsListFilter;

    public ProfileUserListAdapter(Context context, List<UserDetails> userDataModelList) {
        this.context = context;
        this.userDataModelList = userDataModelList;
        userDetailsListFilter = new ArrayList<>(userDataModelList);
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
        private TextView userName , collageName;
        private CircleImageView mCircleImageView;
        private LinearLayout mLinearLayoutUserListImage;
        MyViewHolder(@NonNull View view) {
            super(view);
            userName = view.findViewById(R.id.UserListUserName);
            collageName = view.findViewById(R.id.UserListCollageName);
            mCircleImageView = view.findViewById(R.id.circleImageViewProfile);
            mLinearLayoutUserListImage = view.findViewById(R.id.LinearLayoutUserListImage);

        }

    }

    @Override
    public ProfileUserListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlistprofile, parent, false);
        return new ProfileUserListAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ProfileUserListAdapter.MyViewHolder holder, int position) {
        final UserDetails dataModel = userDataModelList.get(position);



            if(dataModel.getName().length() > 0 ){
                holder.userName.setText(capitalizeWord(dataModel.getName()));
                holder.collageName.setText(capitalizeWord(dataModel.getVillage()));
            }


            Picasso.with(context)
                    .load(dataModel.getImageURL())
                    .placeholder(R.drawable.ic_user_profile)
                    .into(holder.mCircleImageView);


        holder.mLinearLayoutUserListImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dataModel.getUserId().equalsIgnoreCase(SharedPrefsHelper.getInstance().get(USER_ID).toString())) {
                    Intent intent = new Intent(context, OthersProfileActivity.class);
                    intent.putExtra("USER_ID", dataModel.getUserId());
                    context.startActivity(intent);
                }else {
                    Intent intent = new Intent(context, ProfileFragment.class);
                    intent.putExtra("USER_ID", dataModel.getUserId());
                    context.startActivity(intent);
                }
            }
        });

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
