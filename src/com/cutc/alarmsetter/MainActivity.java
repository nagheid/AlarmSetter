package com.cutc.alarmsetter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {

	private static Context context;

	public static final String TAG = "AlarmSetter";

    private Button mAddSettingButton;
    private ListView mSettingsList;
    //private boolean mShowInvisible;
    //private CheckBox mShowInvisibleControl;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        MainActivity.context = getApplicationContext();
        
        // Obtain handles to UI objects
        mAddSettingButton = (Button) findViewById(R.id.add_setting_button);
        mSettingsList = (ListView) findViewById(R.id.settings_list);
        
        // Register handler for UI elements
        mAddSettingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "mAddAccountButton clicked");
                launchSettingAdder();
            }
        });
     
        // Populate the settings list
        //populateSettingsList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
   
    protected void launchSettingAdder() {
        Intent i = new Intent(this, ParametersEditor.class);
        startActivity(i);
    }
    
    public static Context getAppContext() {
        return MainActivity.context;
    }
    
}
