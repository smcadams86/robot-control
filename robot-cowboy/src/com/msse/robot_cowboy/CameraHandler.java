package com.msse.robot_cowboy;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class CameraHandler implements PictureCallback {
	
	private static final String TAG = CameraHandler.class.getSimpleName();

	private final Context context;

	public CameraHandler(Context context) {
		this.context = context;
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("mm-dd-yyyy-hhmmss");
		String date = dateFormat.format(new Date());
		String photoFile = "Picture_" + date + ".jpg";

		try {
			FileOutputStream fos = context.openFileOutput(photoFile, Context.MODE_PRIVATE);
			fos.write(data);
			fos.close();
			File pictureFile = new File(photoFile);
			
			Toast.makeText(context, "New Image saved:" + photoFile,
					Toast.LENGTH_LONG).show();
			
			PhotoQueue.getInstance().add(pictureFile.getPath());
		} catch (Exception error) {
			Log.d(TAG, "File" + photoFile
					+ "not saved: " + error.getMessage());
			Toast.makeText(context, "Image could not be saved.",
					Toast.LENGTH_LONG).show();
		}
	}
}