package com.dmss.dmssevents.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dmss.dmssevents.GalleryActivity;
import com.dmss.dmssevents.R;
import com.dmss.dmssevents.common.DmsSharedPreferences;
import com.dmss.dmssevents.common.RoundedCornersTransform;
import com.dmss.dmssevents.models.AlbumsModel;
import com.dmss.dmssevents.models.PhotoDetailsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sandeep.kumar on 21-03-2017.
 */
public class AlbumGridAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<AlbumsModel> albumsModels;
    int viewSize;

    public AlbumGridAdapter(Context mContext, ArrayList<AlbumsModel> albumsModels, int viewSize) {
        this.mContext = mContext;
        this.albumsModels = albumsModels;
        this.viewSize = viewSize;
    }

    @Override
    public int getCount() {
        return albumsModels.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return albumsModels.hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlbumsModel albumsModel = albumsModels.get(position);
        final Holder holder = new Holder();
        View rowView = convertView;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.album_grid_item, null);
        } else {
            rowView = convertView;
        }
        holder.eventNameTextView = (TextView) rowView.findViewById(R.id.eventNameTextView);
        holder.photosCountTextView = (TextView) rowView.findViewById(R.id.photosCountTextView);
        holder.albumCoverPhotoImageView = (ImageView) rowView.findViewById(R.id.albumCoverPhotoImageView);
        holder.accessImageView = (ImageView) rowView.findViewById(R.id.accessImageView);
        if(albumsModel.getCreatedBy() == DmsSharedPreferences.getUserDetails(mContext).getId()){
            holder.accessImageView.setVisibility(View.VISIBLE);
        }else{
            holder.accessImageView.setVisibility(View.GONE);
        }

        holder.albumGridItemView = (LinearLayout) rowView.findViewById(R.id.albumGridItemView);
        holder.loadingLayout = (RelativeLayout) rowView.findViewById(R.id.loadingLayout);
        holder.loadingTextView = (TextView) rowView.findViewById(R.id.loadingTextView);
        int paddingPixel = 15;
        float density = mContext.getResources().getDisplayMetrics().density;
        int paddingDp = (int)(paddingPixel * density);
        LinearLayout.LayoutParams gridParams = new LinearLayout.LayoutParams(viewSize-paddingDp, viewSize-paddingDp);
        holder.albumGridItemView.setLayoutParams(gridParams);

        holder.eventNameTextView.setText(albumsModel.getAlbumName());
        holder.photosCountTextView.setText(Integer.toString(albumsModel.getPhotosCount()) + " photos");
            try {
                if(albumsModel.getAlbumImageList().size()>0) {
                    String imageUrls = albumsModel.getAlbumImageList().get(albumsModel.getAlbumImageList().size() - 1).getImageURL();
                    Picasso.with(mContext).load(imageUrls).into(holder.albumCoverPhotoImageView, new com.squareup.picasso.Callback() {
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
                }
            }catch (Exception ex){
                ex.printStackTrace();
        }
       /* Glide.with(mContext)
                .load("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTHULo4q-aEMni2MLHdTclBwGzjCXZITaPWNP7B53PU_XjVf9Fw")
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .into(holder.albumCoverPhotoImageView);*/
        return rowView;
    }
    public class Holder {
        TextView eventNameTextView, photosCountTextView,loadingTextView;
        LinearLayout albumGridItemView;
        RelativeLayout loadingLayout;
        ImageView albumCoverPhotoImageView,accessImageView;

    }
}

