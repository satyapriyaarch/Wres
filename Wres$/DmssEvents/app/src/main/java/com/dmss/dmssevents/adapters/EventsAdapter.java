package com.dmss.dmssevents.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmss.dmssevents.AwardsActivity;
import com.dmss.dmssevents.EventAlbumsList;
import com.dmss.dmssevents.EventBanners;
import com.dmss.dmssevents.GalleryActivity;
import com.dmss.dmssevents.R;
import com.dmss.dmssevents.RatingActivity;
import com.dmss.dmssevents.ScheduleActivity;
import com.dmss.dmssevents.common.DmsEventsAppController;
import com.dmss.dmssevents.models.EventsDetailsModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by sandeep.kumar on 13-03-2017.
 */
public class EventsAdapter extends BaseAdapter {
    Context context;
    ArrayList<EventsDetailsModel> eventsDetailsModels;
    private Handler handler;
    private Runnable runnable;
    DmsEventsAppController controller;

    public EventsAdapter(Context context, ArrayList<EventsDetailsModel> eventsDetailsModels,DmsEventsAppController controller) {
        this.context = context;
        this.eventsDetailsModels = eventsDetailsModels;
        this.controller =controller;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
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
        holder.textViewDaysToGo = (TextView) rowView.findViewById(R.id.textViewDaysToGo);
        holder.textViewHours = (TextView) rowView.findViewById(R.id.textViewHours);
        holder.textViewMinutes = (TextView) rowView.findViewById(R.id.textViewMinutes);
        holder.textViewSeconds = (TextView) rowView.findViewById(R.id.textViewSeconds);
        holder.eventEdtTextView = (TextView) rowView.findViewById(R.id.eventEdtTextView);
        holder.buttonGallery = (Button) rowView.findViewById(R.id.buttonGallery);
        holder.buttonScheduler = (Button) rowView.findViewById(R.id.buttonScheduler);
        holder.buttonRating = (Button) rowView.findViewById(R.id.buttonRating);
        holder.buttonAwards = (Button) rowView.findViewById(R.id.buttonAwards);
        holder.timerLinearLayout = (LinearLayout) rowView.findViewById(R.id.timerLinearLayout);
        holder.linearLayoutDays = (LinearLayout) rowView.findViewById(R.id.linearLayoutDays);
        holder.daysTextView = (TextView) rowView.findViewById(R.id.daysTextView);
        holder.hoursTextView = (TextView) rowView.findViewById(R.id.hoursTextView);
        String[] str_array = eventsDetailsModels.get(position).getLaunchDate().split("T");
        String stringDate = str_array[0];
        final String stringTime = str_array[1];
        countDownStart(holder.textViewDaysToGo, holder.textViewHours, holder.textViewMinutes,
                holder.textViewSeconds, stringDate,holder.timerLinearLayout,holder.eventEdtTextView,holder.linearLayoutDays,
                holder.linearLayoutHours,holder.linearLayoutMinutes,holder.daysTextView,holder.hoursTextView);
        //holder.txtViewDate.setText(stringDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date tempDate= null;
        try {
            tempDate = simpleDateFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        holder.txtViewDate.setText(outputDateFormat.format(tempDate));
        holder.imgViewListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventBanners.class);
                context.startActivity(intent);
                // startGalleryActivity();
            }
        });
        holder.buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.setSelectedEvent(eventsDetailsModels.get(position));
                Intent intent = new Intent(context, EventAlbumsList.class);
                context.startActivity(intent);
                // startGalleryActivity();
            }
        });

        holder.buttonScheduler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,ScheduleActivity.class);
                context.startActivity(i);
            }
        });
        holder.buttonRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,RatingActivity.class);
                context.startActivity(i);
            }
        });
        holder.buttonAwards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,AwardsActivity.class);
                context.startActivity(i);
            }
        });
        return rowView;
    }
    public void countDownStart(final TextView textViewDaysToGo,final TextView textViewHours,final TextView textViewMinutes,
                               final TextView textViewSeconds, final String stringDate,final LinearLayout timerLinearLayout,
                               final TextView eventEdtTextView,final LinearLayout linearLayoutDays,final LinearLayout linearLayoutHours,final LinearLayout linearLayoutMinutes,
                               final TextView daysTextView,final TextView hoursTextView) {

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd");
                    // Here Set your Event Date
                    Date eventDate = dateFormat.parse(stringDate);
                    Date currentDate = new Date();
                    if (!currentDate.after(eventDate)) {
                        long diff = eventDate.getTime()
                                - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        /*if(days==0){
                            linearLayoutDays.setVisibility(View.GONE);
                        }else{
                            textViewDaysToGo.setText(""+ String.format("%02d", days));
                        }
                        if(hours==0){
                            linearLayoutHours.setVisibility(View.GONE);
                        }else{
                            textViewHours.setText("" + String.format("%02d", hours));
                        }
                        if(days==0&&hours==0&&minutes==0){
                            linearLayoutMinutes.setVisibility(View.GONE);
                        }else{
                            textViewMinutes.setText("" + String.format("%02d", minutes));
                        }*/
                        if(days<02){
                            daysTextView.setText("Day");
                        }else{
                            daysTextView.setText("Days");
                        }
                        if(hours<02){
                            hoursTextView.setText("Hour");
                        }else{
                            hoursTextView.setText("Hours");
                        }
                        textViewDaysToGo.setText(""+ String.format("%02d", days));
                        textViewHours.setText("" + String.format("%02d", hours));
                        textViewMinutes.setText("" + String.format("%02d", minutes));
                        textViewSeconds.setText("" + String.format("%02d", seconds));
                    } else {
                     /*   linearLayout1.setVisibility(View.VISIBLE);
                        linearLayout2.setVisibility(View.GONE);
                        tvEvent.setText("Android Event Start");*/
                        timerLinearLayout.setVisibility(View.GONE);
                        eventEdtTextView.setVisibility(View.VISIBLE);
                        eventEdtTextView.setText("Event Started!");
                        handler.removeCallbacks(runnable);
                        // handler.removeMessages(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    // //////////////COUNT DOWN END/////////////////////////
  /*  private void startGalleryActivity() {
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
    }*/

    public class Holder {
        TextView txtViewDate,textViewDaysToGo ,textViewHours,textViewMinutes,textViewSeconds,eventEdtTextView,hoursTextView,daysTextView;
        LinearLayout lineaLyListItem,timerLinearLayout,linearLayoutDays,linearLayoutHours,linearLayoutMinutes;
        ImageView imgViewListItem;
        Button buttonGallery,buttonRating,buttonScheduler,buttonAwards;

    }
}