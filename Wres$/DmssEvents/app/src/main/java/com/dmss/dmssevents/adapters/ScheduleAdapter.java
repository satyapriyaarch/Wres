package com.dmss.dmssevents.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dmss.dmssevents.R;
import com.dmss.dmssevents.ScheduleActivity;
import com.dmss.dmssevents.models.ScheduleModel;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sandeep.kumar on 28-03-2017.
 */
public class ScheduleAdapter extends BaseAdapter {
    Context context;
    ArrayList<ScheduleModel> scheduleModelArrayList;

    public ScheduleAdapter(Context context, ArrayList<ScheduleModel> scheduleModelArrayList) {
        this.context = context;
        this.scheduleModelArrayList = scheduleModelArrayList;
    }

    @Override
    public int getCount() {
        return scheduleModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return scheduleModelArrayList.hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            rowView = inflater.inflate(R.layout.schedule_item, null);
        } else {
            rowView = convertView;
        }
        holder.textViewDate = (TextView) rowView.findViewById(R.id.textViewDate);
        holder.titleTextView = (TextView) rowView.findViewById(R.id.titleTextView);
        holder.imageViewTimer = (ImageView) rowView.findViewById(R.id.imageViewTimer);
        holder.textViewTime = (TextView) rowView.findViewById(R.id.textViewTime);
        holder.titleTextView.setText(scheduleModelArrayList.get(position).getScheduleItem());
        holder.backgroundLayout = (LinearLayout) rowView.findViewById(R.id.backgroundLayout);
        holder.descriptionTextView = (TextView) rowView.findViewById(R.id.descriptionTextView);
        holder.descriptionTextView.setText(scheduleModelArrayList.get(position).getDescription());


        String startTime = scheduleModelArrayList.get(position).getStartDateTime();
        String endTime = scheduleModelArrayList.get(position).getEndDateTime();
        String[] startDateTimeArray, endDateTimeArray;
        String startDate, timeDisplay;
        try {
            startDateTimeArray = startTime.split("\\s+");
            endDateTimeArray = endTime.split("\\s+");

            DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("dd MMMM");
            Date date = originalFormat.parse(startDateTimeArray[0]);
            startDate = targetFormat.format(date);
            String test1 = startDateTimeArray[1] + " " + startDateTimeArray[2];
            String test2 = endDateTimeArray[1] + " " + endDateTimeArray[2];
            if (test1.equalsIgnoreCase(test2)) {
                timeDisplay = test1;
            } else {
                timeDisplay = test1 + " - " + test2;
            }

        } catch (Exception e) {
            startDate = "";
            timeDisplay = " ";
        }


        holder.textViewDate.setText(startDate);

        // String[] str_arrayEndDate = .split("T");
        // String endDate = str_arrayEndDate[0];
        // String endTime = str_arrayEndDate[1];
        holder.textViewTime.setText(timeDisplay);


        String color = scheduleModelArrayList.get(position).getColorCode();
        holder.backgroundLayout.setBackgroundColor(Color.parseColor(color));
        holder.imageViewTimer.setBackgroundResource(R.drawable.circle_background);

        GradientDrawable drawable = (GradientDrawable) holder.imageViewTimer.getBackground();
        drawable.setColor(Color.parseColor(color));
        holder.imageViewTimer.setImageResource(R.drawable.circular_clock);
        //holder.imageViewTimer.setBackgroundColor(Color.parseColor(color));


        return rowView;
    }

    public class Holder {
        TextView textViewDate, titleTextView, textViewTime,descriptionTextView;
        ImageView imageViewTimer;
        LinearLayout backgroundLayout;

    }
}
