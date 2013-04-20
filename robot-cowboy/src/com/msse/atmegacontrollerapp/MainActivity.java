package com.msse.atmegacontrollerapp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class MainActivity extends Activity {

    private final String TAG = MainActivity.class.getSimpleName();

    /**
     * The device currently in use, or {@code null}.
     */
    private UsbSerialDriver mSerialDevice;

    /**
     * The system's USB service.
     */
    private UsbManager mUsbManager;

    private TextView mTitleTextView;
    private TextView mDumpTextView;
    private ScrollView mScrollView;

    private final ExecutorService mExecutor = Executors
            .newSingleThreadExecutor();

    private SerialInputOutputManager mSerialIoManager;

    private final SerialInputOutputManager.Listener mListener = new SerialInputOutputManager.Listener() {

        @Override
        public void onRunError(Exception e) {
            Log.d(TAG, "Runner stopped.");
        }

        @Override
        public void onNewData(final byte[] data) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.this.updateReceivedData(data);
                }
            });
        }
    };

	private Camera camera;

    @Override
    protected void onPause() {
        super.onPause();
        stopIoManager();
        camera.release();
        camera = null;
        if (mSerialDevice != null) {
            try {
                mSerialDevice.close();
            } catch (IOException e) {
                // Ignore.
            }
            mSerialDevice = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera = Camera.open();
        mSerialDevice = UsbSerialProber.acquire(mUsbManager);
        Log.d(TAG, "Resumed, mSerialDevice=" + mSerialDevice);
        if (mSerialDevice == null) {
            mTitleTextView.setText("No serial device.");
        } else {
            try {
                mSerialDevice.open();
            } catch (IOException e) {
                Log.e(TAG, "Error setting up device: " + e.getMessage(), e);
                mTitleTextView.setText("Error opening device: "
                        + e.getMessage());
                try {
                    mSerialDevice.close();
                } catch (IOException e2) {
                    // Ignore.
                }
                mSerialDevice = null;
                return;
            }
            mTitleTextView.setText("Serial device: " + mSerialDevice);
        }
        onDeviceStateChange();
    }

    private void stopIoManager() {
        if (mSerialIoManager != null) {
            Log.i(TAG, "Stopping io manager ..");
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
    }

    private void startIoManager() {
        if (mSerialDevice != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(mSerialDevice,
                    mListener);
            mExecutor.submit(mSerialIoManager);
        }
    }

    private void onDeviceStateChange() {
        stopIoManager();
        startIoManager();
    }

    private void updateReceivedData(byte[] data) {
        final String message = "Read " + data.length + " bytes: \n"
                + HexDump.dumpHexString(data) + "\n\n";
        mDumpTextView.append(message);
        mScrollView.smoothScrollTo(0, mDumpTextView.getBottom());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        mTitleTextView = (TextView) findViewById(R.id.logTitle);
        mDumpTextView = (TextView) findViewById(R.id.logText);
        mScrollView = (ScrollView) findViewById(R.id.logScroller);

        ImageButton forwardBtn = (ImageButton) findViewById(R.id.forward_btn);
        ImageButton leftBtn = (ImageButton) findViewById(R.id.left_btn);
        ImageButton stopBtn = (ImageButton) findViewById(R.id.stop_btn);
        ImageButton rightBtn = (ImageButton) findViewById(R.id.right_btn);
        ImageButton backwardBtn = (ImageButton) findViewById(R.id.backward_btn);
        Button pictureBtn = (Button) findViewById(R.id.photo_btn);

        forwardBtn.setOnClickListener(controlClickListener);
        leftBtn.setOnClickListener(controlClickListener);
        stopBtn.setOnClickListener(controlClickListener);
        rightBtn.setOnClickListener(controlClickListener);
        backwardBtn.setOnClickListener(controlClickListener);
        pictureBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
camera.takePicture(null, null, new Camera.PictureCallback() {
					
					@Override
					public void onPictureTaken(byte[] data, Camera camera) {
						Toast.makeText(getApplicationContext(), "Took Picture, size " + data.length + " bytes", Toast.LENGTH_LONG).show();
						String FILENAME = "image_file";

						try{
							FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
							fos.write(data);
							fos.close();
//							Bitmap myBitmap = BitmapFactory.decodeStream(openFileInput(FILENAME));
//							ImageView myImage = (ImageView) findViewById(R.id.imageview);
//							myImage.setImageBitmap(myBitmap);
						} catch(Exception e){
							Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
						}
					}
				});
			}
        	
        });

    }

    private OnClickListener controlClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            String command = "S";
            switch (v.getId()) {
            case R.id.forward_btn:
                command = "F";
                break;
            case R.id.left_btn:
                command = "L";
                break;
            case R.id.stop_btn:
                command = "S";
                break;
            case R.id.right_btn:
                command = "R";
                break;
            case R.id.backward_btn:
                command = "B";
                break;
            }

            try {
                if (mSerialDevice != null) {
                    mSerialDevice.write(command.getBytes(), 1000);
                }
                else {
                	mDumpTextView.append("\nNo connected Device..");
                    mScrollView.smoothScrollTo(0, mDumpTextView.getBottom());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

}
