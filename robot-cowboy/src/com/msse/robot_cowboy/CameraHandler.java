package com.msse.robot_cowboy;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;

public class CameraHandler implements PictureCallback {
	
	private static final String TAG = CameraHandler.class.getSimpleName();
	private final Context context;

	public CameraHandler(Context context) {
		this.context = context;
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		// rotate the Bitmap 90 degrees (counterclockwise)
		matrix.postRotate(-90);

		// recreate the new Bitmap, swap width and height and apply transform
		Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
		                  width, height, matrix, true);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
		byte[] photoByteArray = stream.toByteArray();
		
		PhotoQueue.getInstance().add(photoByteArray);
	}
}