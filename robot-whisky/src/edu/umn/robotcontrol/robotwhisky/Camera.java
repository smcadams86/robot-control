package edu.umn.robotcontrol.robotwhisky;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;


public class Camera {
	private String url;
	private ImageView view;
	private ProgressBar progress;
	private UpdateCamera cameraUpdater;
	
	public Camera(ImageView dest, ProgressBar progress) {
		this.url = "";
		this.progress = progress;
		this.view = dest;
		this.cameraUpdater = null;
	}
	
	public String getURL() {
		return url;
	}
	
	public void setURL(String url) {
		this.url = url;
	}
	
	public void update() {
		int width = ((View)view.getParent()).getWidth();
		int height = ((View)view.getParent()).getHeight();
		
		if (cameraUpdater == null || cameraUpdater.getStatus() == AsyncTask.Status.FINISHED) {
			cameraUpdater = new UpdateCamera(view, progress);
			String tmpUrl = url.replace("%width%", Integer.toString(width));
			tmpUrl = tmpUrl.replace("%height%", Integer.toString(height));
			Log.v("Camera", "Updating image: " + tmpUrl);
			cameraUpdater.execute(tmpUrl);
		}
	}
}