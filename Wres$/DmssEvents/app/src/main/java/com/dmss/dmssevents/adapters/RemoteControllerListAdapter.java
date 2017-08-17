package com.dmss.dmssevents.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dmss.dmssevents.R;
import com.dmss.dmssevents.interfaces.AdapterCallForRating;
import com.dmss.dmssevents.interfaces.RemoteControllerForEnableDisable;
import com.dmss.dmssevents.models.EventRatingModel;
import com.dmss.dmssevents.models.ParticipantsModel;

import java.util.ArrayList;

/**
 * Created by sandeep.kumar on 13-04-2017.
 */
public class RemoteControllerListAdapter extends BaseAdapter {
    Context context;
    ArrayList<EventRatingModel> eventRatingModelArrayList;
    int buttonClickId=0;
    RemoteControllerForEnableDisable adapterCallBack;
    public RemoteControllerListAdapter(Context context, ArrayList<EventRatingModel> eventRatingModelArrayList) {
        this.context = context;
        this.eventRatingModelArrayList = eventRatingModelArrayList;
        adapterCallBack = (RemoteControllerForEnableDisable) context;
    }
    @Override
    public int getCount() {
        return eventRatingModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return eventRatingModelArrayList.hashCode();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.remote_listitem, null);
        } else {
            rowView = convertView;
        }



        /**********Declaration of id's***********/
        holder.textViewDescription = (TextView) rowView.findViewById(R.id.textViewDescription);
        holder.textViewEnable = (TextView) rowView.findViewById(R.id.textViewEnable);
        holder.textViewDisable = (TextView) rowView.findViewById(R.id.textViewDisable);
        holder.textViewStatus = (ImageView) rowView.findViewById(R.id.textViewStatus);
        holder.textViewDescription.setText(eventRatingModelArrayList.get(position).getTPEventDescription());
        holder.textViewEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickId = 1;

                adapterCallBack.adapterClickedPosition(buttonClickId,eventRatingModelArrayList.get(position).getId(), position);
            }
        });
        holder.textViewDisable.setVisibility(View.GONE);
        holder.textViewDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //buttonClickId=2;
                //adapterCallBack.adapterClickedPosition(buttonClickId, eventRatingModelArrayList.get(position).getId(),position);
            }
        });
       // if( eventRatingModelArrayList.get(position).getEnabledDisabldValue()==1){
        if( eventRatingModelArrayList.get(position).isActive()==true){
            //holder.textViewStatus.setVisibility(View.VISIBLE);
            holder.textViewEnable.setText("Enabled");
            //holder.textViewStatus.setImageResource(R.drawable.acceptedcolored);
            holder.textViewEnable.setClickable(false);
            holder.textViewEnable.setBackgroundResource(R.drawable.grey_round_corner_bg);
        }/*else if(eventRatingModelArrayList.get(position).getEnabledDisabldValue()==2){
            holder.textViewStatus.setVisibility(View.VISIBLE);
            holder.textViewStatus.setText("Disabled");
        }*/else if(eventRatingModelArrayList.get(position).isActive()==false){
            holder.textViewEnable.setClickable(true);
            holder.textViewEnable.setText("Enable");
            holder.textViewEnable.setBackgroundResource(R.drawable.orange_round_corner_bg);
            //holder.textViewStatus.setVisibility(View.GONE);
        }
        return rowView;
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }
    /**********Holder class for intialising variables************/
    public class Holder {
        TextView textViewDescription, textViewEnable, textViewDisable;
        ImageView textViewStatus;


    }
}