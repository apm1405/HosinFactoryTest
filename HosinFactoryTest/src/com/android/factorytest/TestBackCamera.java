package com.android.factorytest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

public class TestBackCamera extends Activity {
	private static final boolean SAVE_TAKEN_PICTURE = false;

	private Button camera_ok;
	private Button camera_false;
	private Button takepicture;
	private SurfaceView mSurfaceView;
	private Camera mCamera;
	private boolean isPreview;
	private File picPath, picFile;
	private String PATH;
	private CameraInfo mCameraInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.test_camera);
		
		mCameraInfo = new CameraInfo();
		Camera.getCameraInfo(0, mCameraInfo);

		camera_ok = (Button) findViewById(R.id.success);
		camera_false = (Button) findViewById(R.id.failed);
		takepicture = (Button) findViewById(R.id.retest);
		takepicture.setText(R.string.take_picture);
		mSurfaceView = (SurfaceView) findViewById(R.id.msurfaceview);

		camera_ok.setOnClickListener(mListener);
		camera_false.setOnClickListener(mListener);
		takepicture.setOnClickListener(new takePickture());
		mCamera();

	}

	private void mCamera() {
		isPreview = false;
		if (SAVE_TAKEN_PICTURE) {
			picPath = getFilesDir();
			PATH = picPath.getAbsolutePath() + "/test.jpg";
			picFile = new File(PATH);
		}
		Display display = getWindowManager().getDefaultDisplay();
		mSurfaceView.getHolder().setFixedSize(display.getWidth(),
				display.getHeight());
		mSurfaceView.getHolder().setType(
				SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceView.getHolder().addCallback(new SurfaceCallback());

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (SAVE_TAKEN_PICTURE) {
			if (picFile.exists()) {
				picFile.delete();
				Log.i("cancle", "cancle");
			}
		}
		super.onDestroy();
	}


	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		this.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_HOMEKEY_DISPATCHED);
		super.onAttachedToWindow();
	}

	private final class SurfaceCallback implements SurfaceHolder.Callback {

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub

		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			Camera.Parameters cp;

			for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
				Camera.getCameraInfo(i, mCameraInfo);
				if (mCameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
					mCamera = Camera.open(i);
					if (mCamera == null) {
						return;
					}
					
					cp = mCamera.getParameters();
					cp.setPictureFormat(ImageFormat.JPEG);
					mCamera.setParameters(cp);
					try {
						mCamera.setDisplayOrientation(mCameraInfo.orientation);
						mCamera.setPreviewDisplay(mSurfaceView.getHolder());
						mCamera.startPreview();
						isPreview = true;
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}

				}
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			if (mCamera != null) {
				if (isPreview) {
					mCamera.stopPreview();
					isPreview = false;
				}
				mCamera.release();

			}
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (mCamera != null) {
			if (isPreview) {
				mCamera.stopPreview();
				isPreview = false;
			}
			Log.i("", "pause");

		}
		super.onPause();
	}

	private class takePickture implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			takepicture.setEnabled(false);
			waitHandler.sendEmptyMessageDelayed(1, 3000);
			if (isPreview) {
				mCamera.autoFocus(new mAutoFocusCallBack());
				isPreview = false;
				takepicture.setText(R.string.take_picture_again);
			} else {
				mSurfaceView.setBackground(null);
				if (TestBackCamera.SAVE_TAKEN_PICTURE) {
					if (picFile.exists()) {
						picFile.delete();
					}
				}
				isPreview = true;
				mCamera.startPreview();
				takepicture.setText(R.string.take_picture);
			}
		mCamera.takePicture(null, null, new TakePicture());
		}

		Handler waitHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				takepicture.setEnabled(true);
			}
		};

	}
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			mCamera.takePicture(null, null, new TakePicture());
		}
	};
	private final class mAutoFocusCallBack implements Camera.AutoFocusCallback {

		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			// TODO Auto-generated method stub
			camera.takePicture(null, null, new TakePicture());
		}

	}

	private final class TakePicture implements PictureCallback {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				// TODO Auto-generated method stub
				FileOutputStream stream = null;

				Bitmap bm  = BitmapFactory.decodeByteArray(data, 0, data.length);

			//	camera.stopPreview();

				Matrix matrix1 = new Matrix();
				matrix1.setRotate(90);
				Bitmap bm2 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), matrix1, true);
				// doing the following action for what??
				// Matrix matrix2 = new Matrix();
				// matrix2.setScale(1, 1);
				// Bitmap bm3 = Bitmap.createBitmap(bm2, 0, 0, bm2.getWidth(),
				// bm2.getHeight(), matrix2, true);

				if (TestBackCamera.SAVE_TAKEN_PICTURE) {
					stream = new FileOutputStream(PATH);
					// bm2.compress(CompressFormat.JPEG, 100, stream);
					stream.close();
					Drawable d = Drawable.createFromPath(PATH);
					mSurfaceView.setBackground(d);
				} else {
					BitmapDrawable bitmapDrawable = new BitmapDrawable(bm2);
					mSurfaceView.setBackground((Drawable) bitmapDrawable); // setBackgroundDrawable(Drawable
															// d)
				}
				camera.startPreview();	
				handler.sendEmptyMessage(0);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_HOME:
			return true;
		case KeyEvent.KEYCODE_MENU:
			return true;
		case KeyEvent.KEYCODE_BACK:
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			finish();
			Intent intent = new Intent();
			intent.setClass(TestBackCamera.this, Tp_test.class);
			startActivity(intent);
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			return true;
		default:
			return false;
		}
	}
	private OnClickListener mListener = new OnClickListener() {

		private SharedPreferences sp;
		private SharedPreferences.Editor editor;
		private static final String TEST = "facotytest";

		public void onClick(View v) {
			// TODO Auto-generated method stub
			// add by suxy disable button-tackpicture first
			takepicture.setEnabled(false);

			if (mCamera != null) {
				if (isPreview) {

					mCamera.stopPreview();
					isPreview = false;
				}
				mCamera.release();
				mCamera = null;
			}

			sp = getSharedPreferences(TEST, MODE_WORLD_READABLE);
			editor = getSharedPreferences(TEST, MODE_WORLD_WRITEABLE).edit();
			if (v == camera_ok) {
				editor.putInt("backcamera", 1);
			} else 
			{
				editor.putInt("backcamera", 0);
			}

			editor.commit();
			if (sp.getBoolean("all", false)) {
				Intent intent = new Intent();
				intent.setClass(TestBackCamera.this, TestSubCamera.class);
				startActivity(intent);
			}

			finish();

		}

	};
}

