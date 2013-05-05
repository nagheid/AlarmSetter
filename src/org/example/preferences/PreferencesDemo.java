package org.example.preferences;

import android.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class PreferencesDemo extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);
        
        // Get the app's shared preferences
        SharedPreferences app_preferences = 
        	PreferenceManager.getDefaultSharedPreferences(this);
        
        // Get the value for the run counter
        int readyTime = app_preferences.getInt("readyTime", 0);
        int commMeth = app_preferences.getInt("commMeth", 0);
        int commTime = app_preferences.getInt("commTime", 0);
        
        // Update the TextView
        TextView text = (TextView) findViewById(R.id.edit);
        //text.setText("This app has been started " + counter + " times.");
        
        // Increment the counter
        SharedPreferences.Editor editor = app_preferences.edit();
        editor.putInt("readyTime", readyTime);
        editor.putInt("commMeth", commMeth);
        editor.putInt("commTime", commTime);
        editor.commit();
    }
}