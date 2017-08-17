package com.dmss.dmssevents.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dmss.dmssevents.R;
import com.dmss.dmssevents.models.PhotoDetailsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sandeep.kumar on 30-03-2017.
 */
public class EventBannersAdapter extends BaseAdapter {
    Context context;
    ArrayList<PhotoDetailsModel> photoDetailsModelArrayList;

    public EventBannersAdapter(Context context, ArrayList<PhotoDetailsModel> photoDetailsModelArrayList) {
        this.context = context;
        this.photoDetailsModelArrayList=photoDetailsModelArrayList;
    }

    @Override
    public int getCount() {
        return photoDetailsModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return photoDetailsModelArrayList.hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        View rowView = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.banner_image_item, null);
        } else {
            rowView = convertView;
        }
        holder.gridImageView = (ImageView) rowView.findViewById(R.id.gridImageView);
        holder.gridImageView.bringToFront();
        holder.loadingLayout = (RelativeLayout) rowView.findViewById(R.id.loadingBannerLayout);
        holder.loadingTextView = (TextView) rowView.findViewById(R.id.loadingBannerTextView);
        Picasso.with(context).load(photoDetailsModelArrayList.get(position).getImageURL()).into(holder.gridImageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                holder.loadingLayout.setVisibility(View.GONE);
            }
            @Override
            public void onError() {
                holder.loadingLayout.setVisibility(View.VISIBLE);
                holder.loadingTextView.setText("Error in loading.");
            }
        });
        return rowView;
    }
    public class Holder {
        ImageView gridImageView;
        TextView loadingTextView;
        RelativeLayout loadingLayout;
    }
}
