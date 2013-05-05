package com.cutc.alarmsetter;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.os.Bundle;
import android.provider.AlarmClock;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;

public class Temp extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temp);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
   
	@SuppressLint("NewApi")
	public static void setAlarm (Calendar eventTime, Calendar commuteTime, Calendar readyTime) {
		// import android.provider.AlarmClock;
		AlarmClock alarm = new AlarmClock();
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(System.currentTimeMillis());
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		
		int ehour = commuteTime.get(Calendar.HOUR_OF_DAY) + readyTime.get(Calendar.HOUR_OF_DAY);
		int eMin  = commuteTime.get(Calendar.MINUTE) + readyTime.get(Calendar.MINUTE);
		
		Context context = MainActivity.getAppContext();
		 
		//TODO: ask noura: logic behind first hour and minute
		Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
		i.putExtra(AlarmClock.EXTRA_HOUR, hour + eventTime.get(Calendar.HOUR_OF_DAY) - ehour);
		i.putExtra(AlarmClock.EXTRA_MINUTES, minute + eventTime.get(Calendar.MINUTE) - eMin);
		i.putExtra(AlarmClock.EXTRA_MESSAGE, "Alarm Setter App");
		i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
		context.startActivity(i);
	}

}
