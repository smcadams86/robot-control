package edu.umn.robotcontrol.robotwhisky;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

import edu.umn.robotcontrol.robotwhisky.R;

public class Home extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	/** Called when the user clicks the goToMap button */
	public void goToMap(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, Map.class);
		startActivity(intent);
	}
}
