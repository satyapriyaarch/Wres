package com.dmss.dmssevents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.dmss.dmssevents.common.DmsSharedPreferences;

/**
 * Created by sandeep.kumar on 14-03-2017.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (DmsSharedPreferences.isUserLoggedIn(SplashActivity.this)) {
                        boolean status = DmsSharedPreferences.isOwnerLoggedIn(SplashActivity.this);
                        Intent intent;
                        if (status) {
                            intent = new Intent(SplashActivity.this, RemoteControlActivity.class);
                        } else {
                            intent = new Intent(SplashActivity.this, EventsList.class);
                        }
                        startActivity(intent);
                        finish();

                    } else {
                        Intent intent = new Intent(SplashActivity.this, MailValidationActivity.class);
                        startActivity(intent);
                    }
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
