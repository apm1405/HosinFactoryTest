package com.android.factorytest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
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

public class TestSubCamera extends BaseActivity {
	private static final boolean SAVE_TAKEN_PICTURE = false;

	private SurfaceView mSurfaceView;
	private Camera mCamera;
	private boolean isPreview;
	private File picPath, picFile;
	private String PATH;
	private SurfaceHolder mHolder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	

		mRetest.setText(R.string.take_picture);
		mSurfaceView = (SurfaceView) findViewById(R.id.subsurfaceview);


		subCamera();

	}

	private void subCamera() {
		isPreview = false;
		if (SAVE_TAKEN_PICTURE) {
			picPath = getFilesDir();
			PATH = picPath.getAbsolutePath() + "/subtest.jpg";
			picFile = new File(PATH);
		}
		mHolder = mSurfaceView.getHolder();

		Display display = getWindowManager().getDefaultDisplay();
		mHolder.setFixedSize(display.getWidth(), display.getHeight());
		mHolder.addCallback(new SurfaceCallback());

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub\
		if (SAVE_TAKEN_PICTURE) {
			if (picFile.exists()) {
				picFile.delete();
			}
		}
		super.onDestroy();
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
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			return true;
		default:
			return false;
		}
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
			CameraInfo info = new CameraInfo();
			for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
				Camera.getCameraInfo(i, info);
				if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
					try {
						mCamera = Camera.open(i);
						if (mCamera == null) {
							return;
						}
						cp = mCamera.getParameters();
						cp.setPictureFormat(ImageFormat.JPEG);
						mCamera.setParameters(cp);
						mCamera.setDisplayOrientation(360 - info.orientation);
						mHolder.setFixedSize(mSurfaceView.getWidth(),
								mSurfaceView.getHeight());
						mCamera.setPreviewDisplay(mHolder);
						mCamera.startPreview();
						isPreview = true;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						mCamera = null;
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
				Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);

				camera.stopPreview();

				Matrix matrix1 = new Matrix();
				Matrix matrix2 = new Matrix();
				matrix2.setScale(-1, 1);

				CameraInfo info = new CameraInfo();
				Camera.getCameraInfo(CameraInfo.CAMERA_FACING_FRONT, info);
				// matrix1.setRotate(-90);
				matrix1.setRotate(info.orientation - 360);

				Bitmap bm2 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), matrix1, true);
				Bitmap bm3 = Bitmap.createBitmap(bm2, 0, 0, bm2.getWidth(),
						bm2.getHeight(), matrix2, true);
				if (TestSubCamera.SAVE_TAKEN_PICTURE) {
					stream = new FileOutputStream(PATH);
					bm3.compress(CompressFormat.JPEG, 100, stream);
					stream.close();
					Drawable d = Drawable.createFromPath(PATH);
					mSurfaceView.setBackground(d);
				} else {
					BitmapDrawable bitmapDrawable = new BitmapDrawable(bm3);
					mSurfaceView.setBackground((Drawable) bitmapDrawable);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	private Handler waitHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			mRetest.setEnabled(true);
		}
	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == mRetest){
			mRetest.setEnabled(false);
			waitHandler.sendEmptyMessageDelayed(1, 3000);
			if (isPreview) {
				mCamera.autoFocus(new mAutoFocusCallBack());
				isPreview = false;
				mRetest.setText(R.string.take_picture_again);
			} else {
				mSurfaceView.setBackground(null);
				if (TestSubCamera.SAVE_TAKEN_PICTURE) {
					if (picFile.exists()) {
						picFile.delete();
					}
				}

				mCamera.startPreview();
				isPreview = true;
				mRetest.setText(R.string.take_picture);
			}
			return;
		}
		mRetest.setEnabled(false);

		if (mCamera != null) {
			if (isPreview) {

				mCamera.stopPreview();
				isPreview = false;
			}
			mCamera.release();
			mCamera = null;
		}
		SharedPreferences sp;
		SharedPreferences.Editor editor;
		sp = getSharedPreferences("facotytest", MODE_WORLD_READABLE);
		editor = getSharedPreferences("facotytest", MODE_WORLD_WRITEABLE).edit();
		if (v == mSuccess) {
			editor.putInt("subcamera", 1);
		} else {
			editor.putInt("subcamera", 0);
		}

		editor.commit();
		if (sp.getBoolean("all", false)) {
			Intent intent = new Intent();
			intent.putExtra("position", position+1);
			intent.setClass(this,MMITestData.testItems[position+1]);
			startActivity(intent);
		}

		finish();
	}

	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.test_subcamera);
	}

	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		
	}
}
