package com.sconti.studentcontinent.activity.trending;

import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorPostModel;

import static com.sconti.studentcontinent.activity.trending.TrendingItemClass.LayoutOne;
import static com.sconti.studentcontinent.activity.trending.TrendingItemClass.LayoutThree;
import static com.sconti.studentcontinent.activity.trending.TrendingItemClass.LayoutTwo;


public class TrendingAdapterClass extends RecyclerView.Adapter implements Filterable {

    private List<TrendingItemClass> itemClassList ,  dataModelListFull;
    Context ctx;

    // public constructor for this class
    public TrendingAdapterClass(Context ctx, List<TrendingItemClass> itemClassList) {
        this.ctx=ctx;
        this.itemClassList = itemClassList;
        dataModelListFull = new ArrayList<>(itemClassList);
    }


    @Override
    public int getItemViewType(int position) {
        switch (itemClassList.get(position).getViewType()) {
            case 0:
                return LayoutOne;
            case 1:
                return LayoutTwo;
            case 2:
                return LayoutThree;
            default:
                return -1;
        }
    }

    // Create classes for each layout ViewHolder.

    class LayoutOneViewHolder
            extends RecyclerView.ViewHolder {

        private TextView textview;
        private LinearLayout linearLayout;

        public LayoutOneViewHolder(@NonNull View itemView) {
            super(itemView);

            textview = itemView.findViewById(R.id.category);
            linearLayout = itemView.findViewById(R.id.linearlayout_category);
        }

        // method to set the views that will
        // be used further in onBindViewHolder method.
        private void setView(String text) {

            textview.setText(text);
        }
    }

    // similarly a class for the second layout is also
    // created.

    class LayoutTwoViewHolder
            extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView text_one, text_two;
        private LinearLayout linearLayout;

        public LayoutTwoViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.CommentUserImage);
            text_one = itemView.findViewById(R.id.textViewData);
            text_two = itemView.findViewById(R.id.CommentTime);
            linearLayout = itemView.findViewById(R.id.layout_second);
        }

        private void setViews(String image, String textOne,
                              String textTwo) {
            Glide.with(ctx).load(image).placeholder(R.drawable.ic_user_profile).into(icon);
            text_one.setText(textOne);
            text_two.setText(textTwo);
        }
    }

    class LayoutThreeViewHolder
            extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView text_one, text_two;
        private LinearLayout linearLayout;

        public LayoutThreeViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.CommentUserImage);
            text_one = itemView.findViewById(R.id.textViewData);
            text_two = itemView.findViewById(R.id.CommentTime);
            linearLayout = itemView.findViewById(R.id.notificationLinerLayout);
        }

        private void setViews(String image, String textOne,
                              String textTwo) {
            Glide.with(ctx).load(image).placeholder(R.drawable.ic_user_profile).into(icon);
            text_one.setText(capitalizeWord(textOne));
            text_two.setText(capitalizeWord(textTwo));
        }
    }

    @Override   // TODO : udaya add
    public Filter getFilter() {
        return dataModelListFilter;
    }

    private Filter dataModelListFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<TrendingItemClass> filterList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filterList.addAll(dataModelListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (TrendingItemClass item : dataModelListFull) {
                    if (item.getText_one().toLowerCase().contains(filterPattern)) {
                        filterList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filterList;

            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            itemClassList.clear();
            //itemClassList.addAll((List) results.values);
            notifyDataSetChanged();
        }

    };




    @NonNull
    @Override
    public RecyclerView.ViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType) {
        switch (viewType) {
            case LayoutOne:
                View layoutOne
                        = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.trending_row_1, parent,
                                false);
                return new LayoutOneViewHolder(layoutOne);
            case LayoutTwo:
                View layoutTwo
                        = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.trending_row_2, parent,
                                false);
                return new LayoutTwoViewHolder(layoutTwo);
            case LayoutThree:
                View layoutThree
                        = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.trending_row_3, parent,
                                false);
                return new LayoutThreeViewHolder(layoutThree);
            default:
                return null;
        }
    }

    // In onBindViewHolder, set the Views for each element
    // of the layout using the methods defined in the
    // respective ViewHolder classes.

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

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder,
            final int position) {

        switch (itemClassList.get(position).getViewType()) {
            case LayoutOne:

                String text
                        = itemClassList.get(position).getText();
                ((LayoutOneViewHolder) holder).setView(text);

                // The following code pops a toast message
                // when the item layout is clicked.
                // This message indicates the corresponding
                // layout.
                /*((LayoutOneViewHolder) holder)
                        .linearLayout.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Toast
                                        .makeText(
                                                view.getContext(),
                                                itemClassList.get(position).getText(),
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });*/

                break;

            case LayoutTwo:
                String second_image = itemClassList.get(position).getIcon();
                String text_two_one
                        = itemClassList.get(position).getText_one();
                String text_two_two
                        = itemClassList.get(position).getText_two();
                //Glide.with(ctx).load(image).into(((LayoutTwoViewHolder) holder).
                ((LayoutTwoViewHolder) holder)
                        .setViews(second_image, text_two_one, text_two_two);
               /* ((LayoutTwoViewHolder) holder)
                        .linearLayout.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Toast
                                        .makeText(
                                                view.getContext(),
                                                itemClassList.get(position).getText_two(),
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });*/
                break;
            case LayoutThree:
                String three_image = itemClassList.get(position).getIcon();
                String text_three_one
                        = itemClassList.get(position).getText_one();
                String text_three_two
                        = itemClassList.get(position).getText_two();
                //Glide.with(ctx).load(image).into(((LayoutTwoViewHolder) holder).
                ((LayoutThreeViewHolder) holder)
                        .setViews(three_image, text_three_one, text_three_two);
               /* ((LayoutThreeViewHolder) holder)
                        .linearLayout.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Toast
                                        .makeText(
                                                view.getContext(),
                                                itemClassList.get(position).getText_two(),
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });*/
                break;
            default:
                return;
        }
    }

    // This method returns the count of items present in the
    // RecyclerView at any given time.

    @Override
    public int getItemCount() {
        return itemClassList.size();
    }
}