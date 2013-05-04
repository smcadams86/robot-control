package edu.umn.robotcontrol.robotwhisky;

import java.util.Timer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;

public class Map extends Activity implements LocationSource {
//	static final LatLng QJE = new LatLng(44.752624, -93.227638);
//	static final LatLng MSP = new LatLng(44.890454, -93.230632);
	
	private GoogleMap map;
	private OnLocationChangedListener positionListener;
	private UpdatePosition updater;
	private Timer posUpdateTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_map);
		map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//		map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
//		map.animateCamera(CameraUpdateFactory.zoomTo(10), 500, null);
		map.setLocationSource(this);
		map.setMyLocationEnabled(true);

	}
	
	private void startUpdater() {
		if (posUpdateTimer == null) {
			long period = Long.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_update_period", "1000"));

			updater = new UpdatePosition();
			updater.setPositionListener(positionListener);
			updater.setURL(Utility.buildURL(this) + "/position");
			
			posUpdateTimer = new Timer();
			posUpdateTimer.scheduleAtFixedRate(updater, 0, period);
		}
	}
	
	private void stopUpdater() {
		if (posUpdateTimer != null) {
			posUpdateTimer.cancel();
			posUpdateTimer = null;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		startUpdater();
		
	}
	
	public void onPause() {
		super.onPause();
		stopUpdater();
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		if (updater != null) {
			updater.setPositionListener(listener);
		}
		positionListener = listener;
		startUpdater();
	}

	@Override
	public void deactivate() {
		stopUpdater();
		positionListener = null;
		if (updater != null) {
			updater.setPositionListener(null);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
	
	public void goToSettings() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	            goToSettings();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
