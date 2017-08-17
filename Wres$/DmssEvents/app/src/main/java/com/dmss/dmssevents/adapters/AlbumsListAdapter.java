package com.dmss.dmssevents.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmss.dmssevents.R;
import com.dmss.dmssevents.interfaces.AdapterCallBack;
import com.dmss.dmssevents.models.AlbumDetailsModel;
import com.dmss.dmssevents.models.EventsDetailsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jaya.krishna on 14-Mar-17.
 */
public class AlbumsListAdapter extends BaseAdapter {
    Context context;
    ArrayList<AlbumDetailsModel> albumDetailsModels;
    AdapterCallBack adapterCallBack;

    public AlbumsListAdapter(Context context, ArrayList<AlbumDetailsModel> albumDetailsModels) {
        this.context = context;
        this.albumDetailsModels = albumDetailsModels;
        this.adapterCallBack = (AdapterCallBack) context;
    }

    @Override
    public int getCount() {
        return albumDetailsModels.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return albumDetailsModels.hashCode();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AlbumDetailsModel albumDetailsModel = albumDetailsModels.get(position);
        final Holder holder = new Holder();
        View rowView = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.album_list_item, null);
        } else {
            rowView = convertView;
        }
        holder.albumHeadingTextView = (TextView) rowView.findViewById(R.id.albumHeadingTextView);
        holder.albumCoverPhotoErrorTextView = (TextView) rowView.findViewById(R.id.albumCoverPhotoErrorTextView);
        holder.addPhotoImageView = (ImageView) rowView.findViewById(R.id.addPhotoImageView);
        holder.albumCoverPhotoImageView = (ImageView) rowView.findViewById(R.id.albumCoverPhotoImageView);

        holder.albumHeadingTextView.setText(albumDetailsModel.getAlbumName());

        holder.addPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterCallBack.adapterClickedPosition(position, true);
            }
        });

        holder.albumCoverPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterCallBack.adapterClickedPosition(position, false);
            }
        });
        Picasso.with(context).load(albumDetailsModel.getAlbumCoverPhotoUrl()).into(holder.albumCoverPhotoImageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                if (holder.albumCoverPhotoErrorTextView != null) {
                    holder.albumCoverPhotoErrorTextView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {
                holder.albumCoverPhotoErrorTextView.setVisibility(View.VISIBLE);
                holder.albumCoverPhotoImageView.setVisibility(View.GONE);
                holder.albumCoverPhotoErrorTextView.setText("Error occurred while loading image.");
            }

        });

        return rowView;
    }

    public class Holder {
        TextView albumHeadingTextView, albumCoverPhotoErrorTextView;
        ImageView addPhotoImageView, albumCoverPhotoImageView;
    }
}