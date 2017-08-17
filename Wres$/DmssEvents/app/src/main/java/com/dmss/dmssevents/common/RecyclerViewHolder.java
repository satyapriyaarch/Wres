package com.dmss.dmssevents.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmss.dmssevents.R;

/**
 * Created by jaya.krishna on 17-Mar-17.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    // View holder for gridview recycler view as we used in listview
    public ImageView galleryRecycleImageView,accessImageView;
    public LinearLayout recycleImageLayout;
    public View recycleView;
    public TextView loadingTextView;


    public RecyclerViewHolder(View itemView) {
        super(itemView);
        // Find all views ids

        this.galleryRecycleImageView = (ImageView) itemView
                .findViewById(R.id.galleryRecycleImageView);
        this.accessImageView = (ImageView) itemView
                .findViewById(R.id.accessImageView);
        this.recycleImageLayout = (LinearLayout) itemView
                .findViewById(R.id.recycleImageLayout);
        this.recycleView = (View) itemView
                .findViewById(R.id.recycleView);

        this.loadingTextView = (TextView) itemView
                .findViewById(R.id.loadingTextView);
    }
}
