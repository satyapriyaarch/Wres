package com.dmss.dmssevents.adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
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
import com.dmss.dmssevents.models.AwardsModel;
import com.dmss.dmssevents.models.EventRatingModel;
import com.dmss.dmssevents.models.ParticipantsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sandeep.kumar on 30-03-2017.
 */
public class AwardsAdapter extends BaseAdapter {
    Context context;
    ArrayList<AwardsModel> awardsModelArrayList;
    AdapterCallForRating adapterCallBack;
    AdapterCallBack adapterCallBackForImage;

    public AwardsAdapter(Context context, ArrayList<AwardsModel> awardsModelArrayList) {
        this.context = context;
        this.awardsModelArrayList = awardsModelArrayList;
        adapterCallBack = (AdapterCallForRating) context;
        adapterCallBackForImage = (AdapterCallBack) context;
    }

    @Override
    public int getCount() {
        return awardsModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return awardsModelArrayList.hashCode();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView = convertView;
        AwardsModel awardsModel = awardsModelArrayList.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.rating_item, null);
        } else {
            rowView = convertView;
        }


        /**********Declaration of id's***********/
        holder.textViewEventHeading = (TextView) rowView.findViewById(R.id.textViewEventHeading);
        holder.awardImage = (ImageView) rowView.findViewById(R.id.awardImage);
        holder.textViewVotes = (TextView) rowView.findViewById(R.id.textViewVotes);
        holder.textViewAvgRating = (TextView) rowView.findViewById(R.id.textViewAvgRating);
        holder.textViewAvg = (TextView) rowView.findViewById(R.id.textViewAvg);
        holder.textViewDetail = (TextView) rowView.findViewById(R.id.textViewRate);
        holder.listViewEmployees = (ListView) rowView.findViewById(R.id.listViewEmployees);
        holder.linearLayoutVotes = (LinearLayout) rowView.findViewById(R.id.linearLayoutVotes);
        holder.linearLayoutAvgRating = (LinearLayout) rowView.findViewById(R.id.linearLayoutAvgRating);
        holder.awardShortImageView = (ImageView) rowView.findViewById(R.id.awardShortImageView);
        holder.awardShortImageView.setVisibility(View.VISIBLE);
        holder.awardShortImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterCallBackForImage.adapterClickedPosition(position,false);
            }
        });
        holder.awardImage.setVisibility(View.VISIBLE);
        holder.textViewEventHeading.setText(awardsModel.getAwardName());
        String url = awardsModelArrayList.get(position).getAwardImage();
        if(url != null && url.length()>0){
            Picasso.with(context).load(url).error(R.drawable.profilepic).into(holder.awardShortImageView);
        }


        /***********if condition to check wheather the particular event is rated or not.
         If the event is rated the votes and avg rating with done button will be visible to user or else we are not displaying to user*************/
        if (awardsModelArrayList.get(position).getRewards() !=null&&awardsModelArrayList.get(position).getDescription() !=null) {
            holder.textViewDetail.setText("Details");
            holder.textViewDetail.setBackgroundResource(R.drawable.orange_round_corner_bg);
            holder.linearLayoutVotes.setVisibility(View.GONE);
            holder.linearLayoutAvgRating.setVisibility(View.VISIBLE);
            holder.textViewAvg.setTextSize(TypedValue.COMPLEX_UNIT_PX,14);
            holder.textViewAvgRating.setText(awardsModelArrayList.get(position).getRewards());
            holder.textViewAvg.setText("Award Price");

        }else if(awardsModelArrayList.get(position).getDescription() ==null){
            holder.textViewDetail.setVisibility(View.GONE);
        }
            holder.textViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awardsModelArrayList.get(position).getDescription() !=null) {
                    awardDialog(position);
                }
            }
        });

        holder.listViewEmployees.setAdapter(new EmployeeAwardListAdapter(awardsModelArrayList.get(position).getEmployeeAwardModelArrayList(), context));
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
    private void awardDialog(final int position) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_awards);
        TextView textViewHeading = (TextView) dialog.findViewById(R.id.textViewHeading);
        TextView textViewDescription = (TextView) dialog.findViewById(R.id.textViewDescription);
        TextView textViewCreteria = (TextView) dialog.findViewById(R.id.textViewCreteria);
        textViewHeading.setText(awardsModelArrayList.get(position).getAwardName());
        textViewDescription.setText(awardsModelArrayList.get(position).getDescription());
        textViewCreteria.setText(awardsModelArrayList.get(position).getCriteria());
        dialog.show();
    }

    /**********Holder class for intialising variables************/
    public class Holder {
        TextView textViewEventHeading, textViewDetail, textViewVotes, textViewAvgRating,textViewAvg;
        ListView listViewEmployees;
        LinearLayout linearLayoutVotes, linearLayoutAvgRating;
        ImageView awardImage,awardShortImageView;


    }
}
