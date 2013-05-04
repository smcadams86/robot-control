package com.msse.robot_cowboy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import edu.umn.robotcontrol.domain.RobotCommand;

public class CommandPoller extends AsyncTask<String, Void, RobotCommand> {
	private final String TAG = CommandPoller.class.getSimpleName();
	
	public static final int STATUS_OK = 200;
	private MainActivity mainActivity;
	
	public CommandPoller(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	@Override
	protected RobotCommand doInBackground(String... params) {
		Log.v(TAG, "Polling server for commands...");
		
		if (params.length != 1) {
			Log.e(TAG, "CommandPoller requires one parameter");
			return null;
		}
		RobotCommand command = null;
		AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
		HttpGet request = new HttpGet(params[0]);
		try {
//			request.setEntity(new StringEntity(params[1]));
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-Type", "application/json");
			Log.v(TAG, "URL: "+params[0]);
			HttpResponse response = client.execute(request);
			if (response.getEntity() != null) {
				String json = getStringFromInputStream(response.getEntity().getContent());
				Log.v(TAG, json);
				if (response.getStatusLine().getStatusCode() != STATUS_OK) {
					Log.e(TAG, "Server returned failure");
				}
				else {
					Gson gson = new Gson();
					command = gson.fromJson(json, RobotCommand.class); 
				}
			}
			client.close();
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "Unsupported Encoding Exception");
		} catch (IOException e) {
			Log.e(TAG, "IOException");
		}
		return command;
	}
	
	protected void onPostExecute(RobotCommand command) {
        Log.v(TAG, "Polling Complete : " + command);
        mainActivity.executeCommand(command);
    }
	

	// convert InputStream to String
	private String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	
}
