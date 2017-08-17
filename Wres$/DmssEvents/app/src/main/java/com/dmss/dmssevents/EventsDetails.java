package com.dmss.dmssevents;

import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dmss.dmssevents.common.DmsEventsBaseActivity;

/**
 * EventsDetails.java -
 *
 * @author Jaya Krishna, sandeepkumar
 * @version 1.0
 * @see DmsEventsBaseActivity
 * @since 12-03-2017.
 */
public class EventsDetails extends DmsEventsBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_details);
    }

    @Override
    public void initializeUIElements() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void actionBarSettings() {

    }

}
