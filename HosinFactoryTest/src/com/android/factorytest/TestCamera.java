package com.android.factorytest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class TestCamera extends BaseActivity implements AutoFocusCallback, OnTouchListener {
	private static final boolean SAVE_TAKEN_PICTURE = false;

	private SurfaceView mSurfaceView;
	private Camera mCamera;
	private File picPath, picFile;
	private String PATH;
	private CameraInfo mCameraInfo;
	 private ImageView focusView;
	 private RelativeLayout layout;
	 private int currentX,currentY;
	 private RelativeLayout.LayoutParams params;
	 private Parameters param;
	 private boolean isSilent = true;
	 private boolean isPreview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mCameraInfo = new CameraInfo();
		Camera.getCameraInfo(0, mCameraInfo);
		mRetest.setText(R.string.take_picture);
		layout = (RelativeLayout) findViewById(R.id.my_layout);
		mSurfaceView = (SurfaceView) findViewById(R.id.msurfaceview);
		focusView = (ImageView) findViewById(R.id.focus_image);
		mSurfaceView.setOnClickListener(this);
		mSurfaceView.setOnTouchListener(this);
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
	
	private final class SurfaceCallback implements SurfaceHolder.Callback {

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub

			for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
				Camera.getCameraInfo(i, mCameraInfo);
				if (mCameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
					mCamera = Camera.open(i);
					if (mCamera == null) {
						return;
					}

					param = mCamera.getParameters();
					/*List<String> focusModes = cp.getSupportedFocusModes();
					if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
					{
						cp.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
					}*/
					param.setPictureFormat(ImageFormat.JPEG);
					mCamera.setParameters(param);
					try {
						
						mCamera.setDisplayOrientation(mCameraInfo.orientation);
						mCamera.setPreviewDisplay(mSurfaceView.getHolder());
						mCamera.startPreview();
						isPreview = true;
						animateIn();
						mCamera.autoFocus(TestCamera.this);
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

	private final class TakePicture implements PictureCallback {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				// TODO Auto-generated method stub
				FileOutputStream stream = null;

				Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);

				camera.stopPreview();

				Matrix matrix1 = new Matrix();
				matrix1.setRotate(90);
				Bitmap bm2 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), matrix1, true);
				// doing the following action for what??
				// Matrix matrix2 = new Matrix();
				// matrix2.setScale(1, 1);
				// Bitmap bm3 = Bitmap.createBitmap(bm2, 0, 0, bm2.getWidth(),
				// bm2.getHeight(), matrix2, true);

				if (TestCamera.SAVE_TAKEN_PICTURE) {
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
	  private void updateViewPositon(){
		  params =  (LayoutParams) focusView.getLayoutParams();
		  
		  if(isSilent){
			  params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE); 
		  }
		  else{
			  params.removeRule(RelativeLayout.CENTER_IN_PARENT); 
		  }
		   params.leftMargin = currentX - focusView.getLayoutParams().width;
		   params.topMargin = currentY-focusView.getLayoutParams().height;
		   if(params.leftMargin<0)
			   params.leftMargin = 0;
		   if(params.topMargin<0)
			   params.topMargin = 0;
		   layout.updateViewLayout(focusView, params);
	   }
	 private void animateIn(){
		   updateViewPositon();
		   focusView.setBackgroundResource(R.drawable.ic_focus_focusing);
		  focusView.setVisibility(View.VISIBLE);
		  Animation animation = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f,0.5f,0.5f);
		  animation.setDuration(500);
		  animation.setFillAfter(true);
		  focusView.startAnimation(animation);
		 
	   }
	   private void animateOut(){
		  Animation animation = new ScaleAnimation(1.5f, 1.0f, 1.5f, 1.0f,0.5f,0.5f);
		  animation.setDuration(500);
		  animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				 focusView.setVisibility(View.GONE);
			}
		});
		  focusView.startAnimation(animation);
		 
	   }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == mSurfaceView){
			isSilent = false;
			if(mCamera!=null&&isPreview){
				animateIn();
				param.setFlashMode(Parameters.FLASH_MODE_TORCH);
				mCamera.setParameters(param);
				mCamera.autoFocus(this);
			}
			return ;
		}
		if (v == mRetest) {
			mRetest.setEnabled(false);
			waitHandler.sendEmptyMessageDelayed(1, 3000);
			if (isPreview) {
				isPreview = false;
				mRetest.setText(R.string.take_picture_again);
				mCamera.takePicture(null, null, new TakePicture());
			} 
			else {
				mSurfaceView.setBackground(null);
				if (TestCamera.SAVE_TAKEN_PICTURE) {
					if (picFile.exists()) {
						picFile.delete();
					}
				}
				isPreview = true;
				mCamera.startPreview();
				isSilent = true;
				animateIn();
				mCamera.autoFocus(this);
				mRetest.setText(R.string.take_picture);
				
			}
			return ;
		}
		else{
			mRetest.setEnabled(false);
			SharedPreferences sp;
			SharedPreferences.Editor editor;
			if (mCamera != null) {
				if (isPreview) {
	
					mCamera.stopPreview();
					isPreview = false;
				}
				mCamera.release();
				mCamera = null;
			}
	
			sp = getSharedPreferences("facotytest", MODE_WORLD_READABLE);
			editor = getSharedPreferences("facotytest", MODE_WORLD_WRITEABLE)
					.edit();
			if (v == mSuccess) {
				editor.putInt("backcamera", 1);
	
			} else if (v == mFailed) {
				editor.putInt("backcamera", 0);
	
			}
			editor.commit();
			if (sp.getBoolean("all", false)) {
				Intent intent = new Intent();
				intent.putExtra("position", position+1);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setClass(this,MMITestData.testItems[position+1]);
				startActivity(intent);
			}
	
			finish();
		}

	}

	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.test_camera);
	}

	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAutoFocus(boolean success, Camera camera) {
		// TODO Auto-generated method stub
		param.setFlashMode(Parameters.FLASH_MODE_OFF);
		camera.setParameters(param);
		if(success){
			if(!isSilent){
				MediaPlayer mediaPlayer;  
				mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.focus_complete);  
				mediaPlayer.start();
			}
			focusView.setBackgroundResource(R.drawable.ic_focus_focused);
		}
		else{
			focusView.setBackgroundResource(R.drawable.ic_focus_failed);
		}
		animateOut();
		}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		   currentX = (int) event.getX();
		   currentY = (int) event.getY();
			Log.i("messi","onTouch->currentX: "+currentX+" currentY: "+currentY);
			return false;
	}
}
