package edu.umn.robotcontrol.robotwhisky;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Map extends Activity {
	static final LatLng QJE = new LatLng(44.752624, -93.227638);
	static final LatLng MSP = new LatLng(44.890454, -93.230632);
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		map.addMarker(new MarkerOptions().position(MSP).title("MSP ASR-9"));
		map.addMarker(new MarkerOptions().position(QJE).title("QJE CARSR").snippet("QJE LRR").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(QJE, 15));
		map.animateCamera(CameraUpdateFactory.zoomTo(10), 500, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

}
