package com.sconti.studentcontinent.activity.getcategories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.model.CategoryModel;
import com.sconti.studentcontinent.model.LevelOneModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LevelOneAdapter extends BaseAdapter {
    private Context mContext;
    private List<LevelOneModel> categoryModelList;

    public LevelOneAdapter(Context c, List<LevelOneModel> categoryModelList ) {
        mContext = c;
        this.categoryModelList = categoryModelList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return categoryModelList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            CircleImageView imageView = (CircleImageView) grid.findViewById(R.id.grid_image);
            textView.setText(categoryModelList.get(position).getName());
            Picasso.with(mContext).load(categoryModelList.get(position).getIcon()).into(imageView);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}