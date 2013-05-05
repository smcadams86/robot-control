package com.msse.robot_cowboy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.hardware.Camera;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class CameraPoster extends AsyncTask<String, Void, Void> {
	private final String TAG = CameraPoster.class.getSimpleName();
	private Camera camera;
	private static final int STATUS_OK = 200;
	
	
	public CameraPoster(Context context) {
		
	}

	@Override
	protected Void doInBackground(String... params) {
		if (params.length != 1) {
			Log.e(TAG, "CameraPoster requires one parameters");
			return null;
		}
		
		AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
		HttpPost request = new HttpPost(params[0]);
		try {
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-Type", "application/json");
			Log.e(TAG, "URL: "+params[0]);
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() != STATUS_OK) {
				Log.e(TAG, "Server returned failure");
			}
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "Unsupported Encoding Exception");
		} catch (IOException e) {
			Log.e(TAG, "IOException");
		} finally {
		  client.close();
		}
		return null;
	}

}