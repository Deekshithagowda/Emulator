package com.sconti.studentcontinent.customview;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.RequestManager;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorPostModel;

import java.util.ArrayList;

public class VideoPlayerRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MediaObject> mediaObjects;
    private RequestManager requestManager;
    private SelectItem mListener;

    public VideoPlayerRecyclerAdapter(ArrayList<MediaObject> mediaObjects, RequestManager requestManager, SelectItem selectItem) {
        this.mediaObjects = mediaObjects;
        this.requestManager = requestManager;
        this.mListener = selectItem;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VideoPlayerViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_video_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((VideoPlayerViewHolder)viewHolder).onBind(mediaObjects.get(i), requestManager);
    }
    public void onClickItem(int position) {
        if (mListener != null) {
            mListener.selectedPosition(position);
        }
    }
    public interface SelectItem{
        void selectedPosition(int position);
        void openAlert(OutDoorPostModel dataModel);
        void reachedLastItem();
    }
    @Override
    public int getItemCount() {
        return mediaObjects.size();
    }

}
