package edu.umn.robotcontrol.robotwhisky;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class UpdateCamera extends AsyncTask<String, Void, Bitmap> {
	public static final int STATUS_OK = 200;
	
	private ImageView destination;
	private ProgressBar progress;
	
	public UpdateCamera(ImageView dest, ProgressBar prog) {
		destination = dest;
		progress = prog;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (progress != null) {
			progress.setVisibility(View.VISIBLE);
		}
	}
	
	protected Bitmap doInBackground(String... url) {
		if (url.length != 1) {
			Log.e("UpdateCamera", "Only one URL can be loaded at a time");
			return null;
		}
		if (destination == null) {
			Log.e("UpdateCamera", "Destination image is null");
			return null;
		}
		
		Bitmap result = null;
		AndroidHttpClient client = AndroidHttpClient.newInstance("Android", null);
		HttpUriRequest request = new HttpGet(url[0]);
		
		try {
			HttpResponse response = client.execute(request);
			result = BitmapFactory.decodeStream(response.getEntity().getContent());
			if (response.getStatusLine().getStatusCode() != STATUS_OK) {
				Log.e("CommandPoster", "Failed to execute command");
			}
		}
		catch (IOException e) {
			Log.e("UpdateCamera", "Error refreshing image.");
		}
		catch (OutOfMemoryError e) {
			Log.e("UpdateCamera", "Out of Memory");
		}
		client.close();
		
		return result;
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		if (progress != null) {
			progress.setVisibility(View.INVISIBLE);
		}
		
		if (result != null && destination != null) {
			// Free the bitmap data, because unlike all of the rest of Java, this
			// data needs to be manually freed.
			if (destination.getDrawable() != null) {
				BitmapDrawable bd = ((BitmapDrawable)destination.getDrawable());
				if (bd.getBitmap() != null) {
					bd.getBitmap().recycle();
				}
				// Also, apparently the view doesn't actually free it's data when
				// you call recycle.  Set the image to NULL here to force it to
				// be GC'd.
				destination.setImageBitmap(null);
			}
			
			destination.setImageBitmap(result);
		}
	}
}
