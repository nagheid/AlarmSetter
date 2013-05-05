package com.cutc.alarmsetter;

import android.content.Context;

public class test{
	
	public static void printTime() {
		Context context = MainActivity.getAppContext();
		GoogleCalendarReader reader = new GoogleCalendarReader(context);
		//GoogleCalendarReader.readCalendarEvent(context);
		System.out.println( reader.getEventTime() ); //.toString() );
		System.out.println( reader.getEventLocation() );	
	}
}