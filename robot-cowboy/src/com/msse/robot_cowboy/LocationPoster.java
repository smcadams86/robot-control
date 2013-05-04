package com.msse.robot_cowboy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class LocationPoster extends AsyncTask<String, Void, Void> {
	public static final int STATUS_OK = 200;

	@Override
	protected Void doInBackground(String... params) {
		if (params.length != 2) {
			Log.e("LocationPoster", "LocationPoster requires two parameters");
			return null;
		}
		
		AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
		HttpPost request = new HttpPost(params[0]);
		try {
			request.setEntity(new StringEntity(params[1]));
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-Type", "application/json");
			Log.e("LocationPoster", "URL: "+params[0]);
			Log.e("LocationPoster", "JSON: "+params[1]);
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() != STATUS_OK) {
				Log.e("LocationPoster", "Server returned failure");
			}
			client.close();
		} catch (UnsupportedEncodingException e) {
			Log.e("LocationPoster", "Unsupported Encoding Exception");
		} catch (IOException e) {
			Log.e("LocationPoster", "IOException");
		}
		return null;
	}

}
