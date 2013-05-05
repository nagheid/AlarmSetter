package com.cutc.alarmsetter;

import java.util.*;
import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

public class GoogleCalendarReader  {

	private static Calendar startDate = Calendar.getInstance();
	private static Calendar endDate = Calendar.getInstance();
	private static String location = new String();
	
	public GoogleCalendarReader( Context context ) {
		GoogleCalendarReader.readCalendarEvent(context);
	}
	
	public static void readCalendarEvent(Context context) {
	    Cursor cursor = context.getContentResolver()
	            .query(
	                    Uri.parse("content://com.android.calendar/events"),
	                    new String[] { "calendar_id", "title", "description",
	                            "dtstart", "dtend", "eventLocation" }, null,
	                    null, null);
	    cursor.moveToFirst();
	    if ( cursor.getCount() != 0 ) {
	    	startDate = getTime(Long.parseLong(cursor.getString(3)));
		    endDate = getTime(Long.parseLong(cursor.getString(4)));
		    location = cursor.getString(5);
	    } //else do nothing
	    
	}

	private static Calendar getTime(long milliSeconds) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(milliSeconds);
	    
	    return calendar;
	}
	
	public static Calendar getEventTime ()	{	return startDate;	}
	
	public static String getEventLocation ()	{	return location;	}

}