package com.sconti.studentcontinent.activity.outdoor.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorHashTagModel;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorPostModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OutdoorExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<OutDoorHashTagModel> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<OutDoorPostModel>> _listDataChild;

    public OutdoorExpandableListAdapter(Context context, List<OutDoorHashTagModel> listDataHeader, HashMap<String, List<OutDoorPostModel>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public List<OutDoorPostModel> getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).getId());
       // return this._listDataChild.get(this._listDataHeader.get(groupPosition).getId()).get(0);

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

      //  final String childText = (String) getChild(groupPosition, childPosition);
        List<OutDoorPostModel> outDoorPostModels = new ArrayList<>();
              outDoorPostModels =  (List<OutDoorPostModel>) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.outdoor_item_recyclerview, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.expandedListItem);
        RecyclerView recyclerViewItem = convertView.findViewById(R.id.recycler_view_outdoor_list);
        OutDoorListItemAdapter outDoorListItemAdapter = new OutDoorListItemAdapter(_context, outDoorPostModels);
        recyclerViewItem.setLayoutManager(new LinearLayoutManager(_context, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewItem.setAdapter(outDoorListItemAdapter);
        //txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String header = this._listDataHeader.get(groupPosition).getId();
        int count = this._listDataChild.get(header).size();
       // return this._listDataChild.get(header).size();
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        OutDoorHashTagModel headerTitle = (OutDoorHashTagModel) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.outdoor_hash_tags, null);
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.listTitle);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle.getName());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
