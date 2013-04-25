package edu.umn.robotcontrol.robotwhisky;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

public class Home extends Activity {
	private Timer autoUpdate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Uncomment the following line to reset all settings to their default value.
		//PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();
		
		PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
		
		setContentView(R.layout.activity_home);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	public void goToMap() {
	    // Do something in response to button
		Intent intent = new Intent(this, Map.class);
		startActivity(intent);
	}
	
	public void goToSettings() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		long period = Long.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_update_period", "1000"));
		autoUpdate = new Timer();
		autoUpdate.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						updateInterface();
					}
				});
			}
		}, 0, period);
	}
	
	public void onPause() {
		autoUpdate.cancel();
		super.onPause();
	}
	
	private void updateInterface() {
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	            goToSettings();
	            return true;
	        case R.id.action_map:
	            goToMap();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
