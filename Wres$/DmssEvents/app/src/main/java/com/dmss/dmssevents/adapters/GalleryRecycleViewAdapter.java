package com.dmss.dmssevents.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmss.dmssevents.R;
import com.dmss.dmssevents.common.RecyclerViewHolder;
import com.dmss.dmssevents.interfaces.AdapterCallBack;
import com.dmss.dmssevents.models.PhotoDetailsModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by jaya.krishna on 17-Mar-17.
 */
public class GalleryRecycleViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    ArrayList<PhotoDetailsModel> photoDetailsModels = new ArrayList<PhotoDetailsModel>();
    Context context;
    int selectedPosition;
    AdapterCallBack adapterCallBack;
    int userId;

    public GalleryRecycleViewAdapter(ArrayList<PhotoDetailsModel> photoDetailsModels, Context context, int selectedPosition, int userId) {
        this.photoDetailsModels = photoDetailsModels;
        this.context = context;
        this.selectedPosition = selectedPosition;
        adapterCallBack = (AdapterCallBack) context;
        this.userId = userId;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.gallery_recycle_item, parent, false);
        RecyclerViewHolder listHolder = new RecyclerViewHolder(mainGroup);
        return listHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        //final Data_Model model = arrayList.get(position);
        final RecyclerViewHolder mainHolder = (RecyclerViewHolder) holder;// holder
        mainHolder.galleryRecycleImageView.bringToFront();
        mainHolder.accessImageView.bringToFront();
        if (userId == photoDetailsModels.get(position).getCreatedBy()) {
            mainHolder.accessImageView.setVisibility(View.VISIBLE);
        } else {
            mainHolder.accessImageView.setVisibility(View.GONE);

        }
        if (selectedPosition == position) {
            mainHolder.recycleView.setBackgroundResource(R.color.dmsOrange);
        } else {
            mainHolder.recycleView.setBackgroundResource(R.color.black);
        }
        Picasso.with(context).load(photoDetailsModels.get(position).getImageURL()).into(mainHolder.galleryRecycleImageView, new com.squareup.picasso.Callback() {

            @Override
            public void onSuccess() {
                /// mainHolder.loadingTextView.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                mainHolder.loadingTextView.setVisibility(View.VISIBLE);
                mainHolder.loadingTextView.setText("Error in loading.");

            }

        });
        mainHolder.galleryRecycleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition != position) {
                    selectedPosition = position;
                    adapterCallBack.adapterClickedPosition(position, false);
                    notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != photoDetailsModels ? photoDetailsModels.size() : 0);
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }
}
