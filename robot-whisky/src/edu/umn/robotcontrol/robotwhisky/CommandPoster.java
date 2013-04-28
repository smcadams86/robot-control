package edu.umn.robotcontrol.robotwhisky;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class CommandPoster extends AsyncTask<String, Void, Void> {
	public static final int STATUS_OK = 200;

	@Override
	protected Void doInBackground(String... params) {
		if (params.length != 2) {
			Log.e("CommandPoster", "CommandPoster requires two parameters");
			return null;
		}
		
		AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
		HttpPost request = new HttpPost(params[0]);
		try {
			request.setEntity(new StringEntity(params[1]));
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-Type", "application/json");
			Log.v("CommandPoster", "URL: "+params[0]);
			Log.v("CommandPoster", "JSON: "+params[1]);
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() != STATUS_OK) {
				Log.e("CommandPoster", "Server returned failure");
			}
			client.close();
		} catch (UnsupportedEncodingException e) {
			Log.e("CommandPoster", "Unsupported Encoding Exception");
		} catch (IOException e) {
			Log.e("CommandPoster", "IOException");
		}
		return null;
	}

}
