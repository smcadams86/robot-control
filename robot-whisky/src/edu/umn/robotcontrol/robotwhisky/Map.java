package edu.umn.robotcontrol.robotwhisky;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends Activity implements LocationSource {
	static final LatLng QJE = new LatLng(44.752624, -93.227638);
	static final LatLng MSP = new LatLng(44.890454, -93.230632);
	
	private OnLocationChangedListener positionListener;
	private LatLng loc;
	private GoogleMap map;
	private Timer autoUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		loc = new LatLng(QJE.latitude, QJE.longitude);

		map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		map.addMarker(new MarkerOptions().position(MSP).title("MSP ASR-9"));
		map.addMarker(new MarkerOptions().position(QJE).title("QJE CARSR"));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
		map.animateCamera(CameraUpdateFactory.zoomTo(10), 500, null);
		map.setLocationSource(this);
		map.setMyLocationEnabled(true);
	}
	
	private void startTimer() {
		if (autoUpdate == null && positionListener != null) {
			long period = Long.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_update_period", "1000"));
			autoUpdate = new Timer();
			autoUpdate.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					loc = new LatLng(loc.latitude + (MSP.latitude - QJE.latitude)/60,
							         loc.longitude + (MSP.longitude - QJE.longitude)/60); 
					Location tmp = new Location("");
					tmp.setLatitude(loc.latitude);
					tmp.setLongitude(loc.longitude);
					positionListener.onLocationChanged(tmp);
				}
			}, 0, period);
		}
	}
	
	private void stopTimer() {
		if (autoUpdate != null) {
			autoUpdate.cancel();
			autoUpdate = null;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		startTimer();
		Log.e("Map", "onResume");
	}
	
	public void onPause() {
		Log.e("Map", "onPause");
		stopTimer();
		super.onPause();
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		// TODO Auto-generated method stub
		Log.e("Map", "Activate");
		positionListener = listener;
		startTimer();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		Log.e("Map", "Deactivate");
		stopTimer();
		positionListener = null;
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
