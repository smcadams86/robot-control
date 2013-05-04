package edu.umn.robotcontrol.robotwhisky;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import android.location.Location;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.gson.Gson;

import edu.umn.robotcontrol.domain.RobotPosition;

public class UpdatePosition extends TimerTask {
	public static final int STATUS_OK = 200;
	
	private String url;
	private OnLocationChangedListener positionListener;
	
	public UpdatePosition() {
		this.url = "";
		this.positionListener = null;
	}
	
	public String getURL() {
		return url;
	}
	
	public void setURL(String url) {
		this.url = url;
	}
	
	public OnLocationChangedListener getPositionListener() {
		return positionListener;
	}
	
	public void setPositionListener(OnLocationChangedListener positionListener) {
		this.positionListener = positionListener;
	}
	
	@Override
	public void run() {
		// Nothing to do if nothing's listening
		if (positionListener == null) {
			return;
		}
		Log.e("", "Starting");
		
		AndroidHttpClient client = AndroidHttpClient.newInstance("Android", null);
		HttpUriRequest request = new HttpGet(url);
		
		try {
			HttpResponse response = client.execute(request);
			if (response.getEntity() != null && response.getStatusLine() != null) {
				String json = getStringFromInputStream(response.getEntity().getContent());
				if (response.getStatusLine().getStatusCode() != STATUS_OK) {
					Log.e("UpdatePosition", "Failed to execute command");
				}
				else {
					Log.e("UpdatePosition", json);
					RobotPosition pos = new Gson().fromJson(json, RobotPosition.class);
					if (pos != null) {
						Location loc = PositionFacade.robotPosToLoc(pos);
						
						// Just in case we were in the middle of polling the server
						// when the activity was paused, verify that
						// positionListener is still not null.
						if (loc != null && positionListener != null) {
							positionListener.onLocationChanged(loc);
						}
					}
				}
			}
			
		}
		catch (IOException e) {
			Log.e("UpdatePosition", "Error refreshing image.");
		}
		client.close();
	}

	// convert InputStream to String
	private String getStringFromInputStream(InputStream is) {
		if (is != null) {
			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();
			String line;
			try {
				br = new BufferedReader(new InputStreamReader(is));
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
			} catch (IOException e) {
				Log.e("UpdatePosition", "Error updating position.");
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						Log.e("UpdatePosition", "Closing buffered stream failed.");
					}
				}
			}
			return sb.toString();
		}
		return "";
	}
}
