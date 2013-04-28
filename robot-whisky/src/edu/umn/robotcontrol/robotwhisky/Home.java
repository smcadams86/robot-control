package edu.umn.robotcontrol.robotwhisky;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import edu.umn.robotcontrol.domain.RobotCommand;

import com.google.gson.Gson;

public class Home extends Activity {
	private Timer autoUpdate;
	private Camera cameras[];
	
	private String buildURL() {
		String dataSource = PreferenceManager.getDefaultSharedPreferences(this).getString("pref_data_source", "");
		return "http://" + dataSource + "/control";
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Uncomment the following line to reset all settings to their default value.
		//PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();
		
		PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
		
		setContentView(R.layout.activity_home);
		
		
		cameras = new Camera[2];
		cameras[0] = new Camera(
				(ImageView)findViewById(R.id.img_front),
				(ProgressBar)findViewById(R.id.front_load),
				buildURL() + "/photo" + "?width=%width%&height=%height%");
		cameras[0].update();
		cameras[1] = new Camera(
				(ImageView)findViewById(R.id.img_back),
				(ProgressBar)findViewById(R.id.back_load),
				buildURL() + "/photo" + "?width=%width%&height=%height%");
		cameras[1].update();
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
				// If the last image is done loading, toggle a new one
				runOnUiThread(new Runnable() {
					public void run() {
						for (Camera camera : cameras) {
							camera.update();
						}
					}
				});
			}
		}, 0, period);
	}
	
	public void onPause() {
		autoUpdate.cancel();
		super.onPause();
	}
	
	public void onClick(View v) {
		RobotCommand cmd = new RobotCommand();
		
		switch(v.getId()) {
		case R.id.stop:
			cmd.setComponent(RobotCommand.MOVE_STOP);
			break;
		case R.id.forward:
			cmd.setComponent(RobotCommand.MOVE_FORWARD);
			break;
		case R.id.reverse:
			cmd.setComponent(RobotCommand.MOVE_REVERSE);
			break;
		case R.id.left:
			cmd.setComponent(RobotCommand.MOVE_LEFT);
			break;
		case R.id.right:
			cmd.setComponent(RobotCommand.MOVE_RIGHT);
			break;
		default:
			Log.e("Home", "Unknown command: " + v.getId());
			return;
		}
		
		cmd.setIssueTime(System.currentTimeMillis());
		cmd.setValue(100);
		
		Gson gson = new Gson();
		String json = gson.toJson(cmd);
		
		CommandPoster post = new CommandPoster();
		post.execute(buildURL()+"/command", json);
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
