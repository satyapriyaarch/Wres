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
import android.widget.TextView;

import com.dmss.dmssevents.GalleryActivity;
import com.dmss.dmssevents.R;
import com.dmss.dmssevents.models.EventsDetailsModel;

import java.util.ArrayList;

/**
 * Created by sandeep.kumar on 13-03-2017.
 */
public class EventAlbumsAdapter extends BaseAdapter {
    Context context;
    ArrayList<EventsDetailsModel> eventsDetailsModels;

    public EventAlbumsAdapter(Context context, ArrayList<EventsDetailsModel> eventsDetailsModels) {
        this.context = context;
        this.eventsDetailsModels = eventsDetailsModels;
    }

    @Override
    public int getCount() {
        return eventsDetailsModels.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return eventsDetailsModels.hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.events_list_item, null);
        } else {
            rowView = convertView;
        }
        holder.lineaLyListItem = (LinearLayout) rowView.findViewById(R.id.lineaLyListItem);
        holder.imgViewListItem = (ImageView) rowView.findViewById(R.id.imgViewListItem);
        holder.txtViewDate = (TextView) rowView.findViewById(R.id.txtViewDate);
       // holder.btnPhotos = (Button) rowView.findViewById(R.id.btnPhotos);
        //holder.btnDetails = (Button) rowView.findViewById(R.id.btnDetails);
        holder.txtViewDate.setText(eventsDetailsModels.get(position).getEventDate().toString());
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
startGalleryActivity();
            }
        });

        return rowView;
    }

    private void startGalleryActivity() {
            ArrayList<String> images = new ArrayList<String>();
            images.add("http://imgh.us/milestone_1.png");
            images.add("http://imgh.us/ustav.png");
            images.add("http://imgh.us/loogo.png");
            images.add("http://imgh.us/milestone_1.png");
            images.add("http://imgh.us/ustav.png");
            images.add("http://imgh.us/loogo.png");
            images.add("http://imgh.us/milestone_1.png");
            images.add("http://imgh.us/ustav.png");
            images.add("http://imgh.us/loogo.png");
            Intent intent = new Intent(context, GalleryActivity.class);
            intent.putStringArrayListExtra(GalleryActivity.EXTRA_NAME, images);

            context.startActivity(intent);
    }

    public class Holder {
        TextView txtViewDate ;
        LinearLayout lineaLyListItem;
        ImageView imgViewListItem;
        Button btnPhotos,btnDetails;

    }
}