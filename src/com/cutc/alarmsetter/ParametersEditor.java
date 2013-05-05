package com.cutc.alarmsetter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.example.preferences.PreferencesDemo;

import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class ParametersEditor extends Activity {

	public static final String TAG = "ContactsAdder";

	private boolean isDefault;
	private CheckBox mUseAsDefCheckbox;
	private EditText mSettingNameEditText;
	private EditText mReadyTimeEditText;
    private EditText mCommuteMethodEditText;
    private EditText mCommuteTimeEditText;
    private Button mSaveSettingButton;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "Activity State: onCreate()");
		
		Context context = MainActivity.getAppContext();
		int condition= getIntent().getIntExtra("isCreateAlarm", 0);
		
		if ( condition == 0 ) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_parameters_editor);
			
			// Obtain handles to UI objects
			isDefault = false;
			mUseAsDefCheckbox  = (CheckBox) findViewById(R.id.use_as_def);
			mSettingNameEditText = (EditText) findViewById(R.id.setting_name_text);
			mReadyTimeEditText = (EditText) findViewById(R.id.ready_time_text);
			mCommuteMethodEditText = (EditText) findViewById(R.id.commute_method_text);
			mCommuteTimeEditText = (EditText) findViewById(R.id.commute_time_text);
			mSaveSettingButton = (Button) findViewById(R.id.save_setting_button);
	
			mUseAsDefCheckbox.setOnClickListener(new View.OnClickListener() {
				 
				  @Override
				  public void onClick(View v) {
					if (((CheckBox) v).isChecked()) {
						isDefault = true;
					}
				  }
				});
			
			mSaveSettingButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                onSaveButtonClicked(isDefault);
	            }
	        });
			
			/*
			 PreferenceManager.setDefaultValues(context.getActivity(),
	                    R.xml.advanced_preferences, false);

	         // Load the preferences from an XML resource
	         addPreferencesFromResource(R.xml.fragmented_preferences);
	        */
			
			//Intent i = new Intent(context, PreferencesDemo.class);
	        //startActivity(i);
			
		} else {
	        
	        GoogleCalendarReader reader = new GoogleCalendarReader(context);
	        Calendar eventCal = reader.getEventTime();
			String location  = reader.getEventLocation();
			
			//TODO:
			// Just one setting for now
			SharedPreferences currentSettings = PreferenceManager.getDefaultSharedPreferences(this);
	        String readyTime = currentSettings.getString("readyTime", "");
	        String commuteMethod = currentSettings.getString("commMeth", "");
	        String commuteTime = currentSettings.getString("commTime", "");
	        
	        //TODO: if commute time not set: set it from location
	        if ( readyTime == "") {
	        	readyTime = "1"; //TODO: default
	        }
	        
	        if ( commuteTime == "" ) { // || getCommuteTime(commuteMethod) == "" ) {
	        	commuteTime = "1"; //TODO: default
	        }
	        
	        //TODO: check this
	        String pattern = "HH:mm";
	        
	        Calendar readyCal = Calendar.getInstance();
	        Date readyDate = null;
			try {
				readyDate = new SimpleDateFormat(pattern).parse(readyTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        readyCal.setTime(readyDate);
	        
	        Calendar commuteCal = Calendar.getInstance();
	        Date commuteDate = null;
			try {
				commuteDate = new SimpleDateFormat(pattern).parse(commuteTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        commuteCal.setTime(commuteDate);
	        
			AlarmCreator.setAlarm(eventCal, commuteCal, readyCal);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.parameters_editor, menu);
		return true;
	}
	
	private void onSaveButtonClicked(boolean isDefault) {
        Log.v(TAG, "Save button clicked");
        createSettingEntry(isDefault);
        finish();
    }
	
	protected void createSettingEntry(boolean isDefault) {
        // Get values from UI
		String settingName = mSettingNameEditText.getText().toString();
        String readyTime = mReadyTimeEditText.getText().toString();
        String commMeth = mCommuteMethodEditText.getText().toString();
        String commTime = mCommuteTimeEditText.getText().toString();

        //TODO:
        // Add to settings list - JUST ONE SETTING FOR NOW
        SharedPreferences currentSettings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEdit = currentSettings.edit();
        mEdit.putString("settingName", settingName); 
        mEdit.putString("readyTime", readyTime); 
        mEdit.putString("commMeth", commMeth); 
        mEdit.putString("commTime", commTime); 
        
        System.out.println(readyTime);
        
        /*
        for(int i=0;i<sKey.size();i++)  
        {
		   mEdit.remove("Status_" + i);
		   mEdit.putString("Status_" + i, sKey.get(i));  
        }
         */
        mEdit.commit();
        
        
        //TODO:
        // Schedule task for 12 am
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		Context context = MainActivity.getAppContext();
		
		Intent intent = new Intent(context, ParametersEditor.class);
		intent.putExtra("isCreateAlarm", 1);
		PendingIntent pendIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		//AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
		                                 AlarmManager.INTERVAL_DAY, pendIntent);
		
		//pendingIntent = PendingIntent.getService(AndroidAlarmService.this, 0, myIntent, 0);

        
        test.printTime();
      
        // Prepare alarm creation request
        //
        // Note: We use RawContacts because this data must be associated with a particular account.
        //       The system will aggregate this with any other data for this contact and create a
        //       corresponding entry in the ContactsContract.Contacts provider for us.
        /*
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, mSelectedAccount.getType())
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, mSelectedAccount.getName())
                .build());
        */
        
	}
	
	/*
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, TAG);
        // Acquire the lock
        wl.acquire();
        // will run every midnight
        System.out.println("Alarm Scheduler Started");
        Intent intentDefault = new Intent(context, myopeinigactivity.class);
        intentDefault.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentDefault);

        wl.release();

    }
    */

}
