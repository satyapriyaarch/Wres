package com.dmss.dmssevents.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dmss.dmssevents.R;
import com.dmss.dmssevents.interfaces.AdapterCallBack;
import com.dmss.dmssevents.interfaces.AdapterCallForRating;
import com.dmss.dmssevents.models.EventRatingModel;
import com.dmss.dmssevents.models.ParticipantsModel;

import java.util.ArrayList;

/**
 * Created by sandeep.kumar on 30-03-2017.
 */
public class RatingAdapter extends BaseAdapter {
    Context context;
    android.support.v7.app.ActionBar actionBar;
    ArrayList<EventRatingModel> eventRatingModelArrayList;
    AdapterCallForRating adapterCallBack;

    public RatingAdapter(Context context, ArrayList<EventRatingModel> eventRatingModelArrayList) {
        this.context = context;
        this.eventRatingModelArrayList = eventRatingModelArrayList;
        adapterCallBack = (AdapterCallForRating) context;
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
            rowView = inflater.inflate(R.layout.rating_item, null);
        } else {
            rowView = convertView;
        }


        /**********Declaration of id's***********/
        holder.textViewEventHeading = (TextView) rowView.findViewById(R.id.textViewEventHeading);
        holder.textViewVotes = (TextView) rowView.findViewById(R.id.textViewVotes);
        holder.textViewAvgRating = (TextView) rowView.findViewById(R.id.textViewAvgRating);
        holder.textViewRate = (TextView) rowView.findViewById(R.id.textViewRate);
        holder.descTextView = (TextView) rowView.findViewById(R.id.descTextView);
        holder.listViewEmployees = (ListView) rowView.findViewById(R.id.listViewEmployees);
        holder.linearLayoutVotes = (LinearLayout) rowView.findViewById(R.id.linearLayoutVotes);
        holder.linearLayoutAvgRating = (LinearLayout) rowView.findViewById(R.id.linearLayoutAvgRating);
        holder.textViewEventHeading.setText(eventRatingModelArrayList.get(position).getTPEventName());
        holder.descTextView.setText(eventRatingModelArrayList.get(position).getTPEventDescription());

        /***********if condition to check wheather the particular event is rated or not.
         If the event is rated the votes and avg rating with done button will be visible to user or else we are not displaying to user*************/
        if (eventRatingModelArrayList.get(position).isRated() == true) {
            holder.textViewRate.setText("Done");
            holder.textViewRate.setBackgroundResource(R.drawable.green_round_corner_bg);
            holder.linearLayoutVotes.setVisibility(View.VISIBLE);
            holder.linearLayoutAvgRating.setVisibility(View.VISIBLE);
            holder.textViewVotes.setText(Integer.toString(eventRatingModelArrayList.get(position).getTotalVote()));
            holder.textViewAvgRating.setText(Integer.toString(eventRatingModelArrayList.get(position).getRateAvarage())+"%");
        } else if (eventRatingModelArrayList.get(position).isRated() == false) {
            holder.textViewRate.setText("Rate");
            holder.textViewRate.setBackgroundResource(R.drawable.orange_round_corner_bg);
            holder.linearLayoutVotes.setVisibility(View.GONE);
            holder.linearLayoutAvgRating.setVisibility(View.GONE);
        }
        holder.textViewRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventRatingModelArrayList.get(position).isRated() == false) {
                    ratingDialog(position);
                } else if (eventRatingModelArrayList.get(position).isRated() == true) {
                    Toast.makeText(context, "You have already rated this event", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ArrayList<ParticipantsModel> participantsModels = eventRatingModelArrayList.get(position).getArrayParticipantsModels();
        holder.listViewEmployees.setAdapter(new EmployeeEventsAdapter(participantsModels, context));
        setListViewHeightBasedOnItems(holder.listViewEmployees);

        /*******This if condition is for we are adding listview inside listview so in normal
         * case this will not work for this setNestedScrollingEnabled method will work if we make that true*********/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.listViewEmployees.setNestedScrollingEnabled(true);

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


    /*****
     * Dialog box for submitting rating and we are calling when the particular event is not
     * rating then we are displaying this dialog box or else we sre displaying a toast
     ***********/
    private void ratingDialog(final int position) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.rating_dialog);
        TextView textViewHeading = (TextView) dialog.findViewById(R.id.textViewHeading);
        TextView textViewSubmit = (TextView) dialog.findViewById(R.id.textViewSubmit);
        TextView textViewCancel = (TextView) dialog.findViewById(R.id.textViewCancel);
        final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
        ratingBar.setStepSize(1);

        textViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rating1 = String.valueOf(ratingBar.getRating());
                int rating = (int) ratingBar.getRating();
                if(rating>0) {
                    adapterCallBack.adapterClickedPosition(rating, eventRatingModelArrayList.get(position).getId());
                    dialog.dismiss();
                }else{
                    Toast.makeText(context,"Kindly rate the event before submitting",Toast.LENGTH_SHORT).show();
                    dialog.show();
                }

            }
        });
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**********Holder class for intialising variables************/
    public class Holder {
        TextView textViewEventHeading, textViewRate, textViewVotes, textViewAvgRating,descTextView;
        ListView listViewEmployees;
        LinearLayout linearLayoutVotes, linearLayoutAvgRating;


    }
}
