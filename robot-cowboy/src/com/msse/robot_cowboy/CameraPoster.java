package com.msse.robot_cowboy;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class CameraPoster extends AsyncTask<String, Void, Void> {
	private final String TAG = CameraPoster.class.getSimpleName();
	private static final int STATUS_OK = 200;
	private Context context;

	public CameraPoster(Context context) {
		this.context = context;
	}

	@Override
	protected Void doInBackground(String... params) {
		if (params.length != 1) {
			Log.e(TAG, "CameraPoster requires one parameter");
			return null;
		}

		PhotoQueue queue = PhotoQueue.getInstance();
		while (queue.peek() != null) {
			String filename = queue.poll();

			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(params[0]);

			try {
				Log.e(TAG, "URL: " + params[0]);
				
				FileInputStream fis = context.openFileInput(filename);
				Bitmap photo = BitmapFactory.decodeStream(fis);
				fis.close();

				if (photo != null) {
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
					byte[] photoByteArray = stream.toByteArray();

					// Write the photo to the REST endpoint
					request.setEntity(new ByteArrayEntity(photoByteArray));
					HttpResponse response = client.execute(request);
					HttpEntity entity = response.getEntity();
					InputStream inStream = entity.getContent();
					inStream.close();

					if (response.getStatusLine().getStatusCode() != STATUS_OK) {
						Log.e(TAG, "Server returned failure");
						Log.e(TAG, response.getStatusLine().getReasonPhrase());
					}
				} else {
					Log.v(TAG, "Could not decode file [" + filename + "]");
				}
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, "Unsupported Encoding Exception");
			} catch (IOException e) {
				Log.e(TAG, "IOException");
			}
		}
		return null;
	}
}