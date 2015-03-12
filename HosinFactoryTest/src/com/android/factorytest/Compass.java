package com.android.factorytest;



import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

public class Compass extends BaseActivity implements SensorEventListener{
	
	 public static final String TAG = "MSensor";
	    float[] accelermeter_value;
	    Sensor accelerometerSensor;
	    private CompassView mPointer;
	    SensorManager mSensorManager;
	    private TextView mTextview_data;
	    
	    
	    Sensor magneticSensor;
	    float[] magntiude_value;
	    protected final Handler mHandler = new Handler();  
	    
	    //加速计数据。
		private float[] mGData = new float[3];
		//磁力计数据。
		private float[] mMData = new float[3];
		//旋转矩阵(rotation matrix)，用于计算手机自身相对于地面坐标系的旋转角度。比如手机现在是横着还是竖着的。
		//反映的是手机的立体摆放位置。这个数据最好用陀螺仪来测量，没有陀螺仪的话，借助于加速计和重力加速度也可以，只是精度不够强罢了。
		private float[] mR = new float[16];
		//倾斜矩阵(inclination matrix)，用于计算手机方位。比如手机的头部现在朝南还是朝北。是针对一个平面上的东南西北四个方向而言的。
		private float[] mI = new float[16];
		//存放最终的旋转角度数据
		private float[] mOrientation = new float[3];
		private float yaw = 0.0f;
		private AccelerateInterpolator mInterpolator;// 动画从开始到结束，变化率是一个加速的过程,就是一个动画速率
		private float mDirection;// 当前浮点方向  
	    private float mTargetDirection;// 目标浮点方向  
	    private final float MAX_ROATE_DEGREE = 1.0f;// 最多旋转一周，即360°  
	    private boolean mStopDrawing;// 是否停止指南针旋转的标志位  
	    private boolean upeast , upwest,faceeast ,facewest =false;

		
	    public Compass() {
	        mSensorManager = null;
	        magneticSensor = null;
	        accelerometerSensor = null;
	        
	    }
	    
	@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
	        super.onCreate(savedInstanceState);
	       
	        mTextview_data = (TextView)findViewById(R.id.xyz);
	        mPointer = (CompassView)findViewById(R.id.compass_image);
	        mPointer.setImageResource(R.drawable.compass);
	       
	        mInterpolator = new AccelerateInterpolator();// 实例化加速动画对象  
	        mDirection = 0.0f;// 初始化起始方向  
	        mTargetDirection = 0.0f;// 初始化目标方向  
	        mStopDrawing = true;  
	        initsensor();
	        
	        
	        mSuccess.setEnabled(false);
	        setRetestBtnVisible(false);
		}
	
	
    private void initsensor() {
        Log.i("MSensor", "initsensor");
        mSensorManager = (SensorManager)getSystemService("sensor");
        magneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
	
	
    public void onPause() {
        Log.i("MSensor", "onPause");
        super.onPause();
        mStopDrawing = true;  
        mSensorManager.unregisterListener(this, magneticSensor);
        mSensorManager.unregisterListener(this, accelerometerSensor);
    }
    
    public void onResume() {
        Log.i("MSensor", "onResume");
        super.onResume();
        mSensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        mStopDrawing = false;  
        mHandler.postDelayed(mCompassViewUpdater, 20);// 20毫秒执行一次更新指南针图片旋转  
    }

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
			int type = event.sensor.getType();    
			if (type == Sensor.TYPE_ACCELEROMETER) {       
				for (int i = 0; i < 3; i++)     {
					mGData[i] = event.values[i];
					Log.d(TAG, "mGData[i] = " + mGData[i]);
				}
				
				// 判断通过按钮的状态
	            if(event.values[SensorManager.DATA_Z]>9){
	            	if(yaw >170){
	                	upeast = true;
	                	Log.d("shihongwei", "upeast = true;");
	                }if(yaw<-170){
	                	upwest =true;
	                	Log.d("shihongwei", "upwest =true;");
	                }
				}
	            if(event.values[SensorManager.DATA_Z]<2){
	            	if(yaw >170){
	                	faceeast = true;
	                	Log.d("shihongwei", "faceeast =true;");
	                }
	            	if(yaw<-170){
	                	facewest =true;
	                	Log.d("shihongwei", "facewest =true;");
	                }
				}
	            
	            if(upeast && upwest && faceeast && facewest){
	            	Log.d("shihongwei", "okButton.setEnabled(true);");
	            	mSuccess.setEnabled(true);
	            }
				
			} 
			else if (type == Sensor.TYPE_MAGNETIC_FIELD) {      
				for (int i = 0; i < 3; i++)     
					mMData[i] = event.values[i];
				
			} else {        
						// we should not be here.       
						return;      
			}      
			//这一步的目的是获取mR和mI两个矩阵。 
			if(!SensorManager.getRotationMatrix(mR, mI, mGData, mMData)){
				Log.d(TAG, "failure");
				return ;
			}else{
				for (int i = 0; i < 16; i++)     {
					Log.d(TAG, "mR[i] = " + mR[i]);
				}
			}
			//用矩阵mR获取手机的旋转角度。     
			SensorManager.getOrientation(mR, mOrientation);
			//用矩阵mI获取手机在东南西北四个方向上的夹角。   
			float incl = SensorManager.getInclination(mI);      
			
			final float rad2deg = (float)(180.0f / Math.PI);      
			// 正北转角 绕Z轴旋转角度
			yaw =  (mOrientation[0] * rad2deg);
			// 顶尾翘角 绕X轴旋转角度
			float pitch = (mOrientation[1] * rad2deg);
			// 左右转角 绕Y轴旋转角度
			float roll = (mOrientation[2] * rad2deg);
			
			float dincl = (incl * rad2deg);
			
			Log.d("Compass", "yaw: " + yaw +
					"  pitch: " + pitch +
					"  roll: " + roll + 
					"  incl: " + dincl);
			
			mTextview_data.setText("X degree =" + yaw + "\nY degree = " + pitch + "\nZ degree = " + roll );
			//add
			float direction = yaw * -1.0f;  
            mTargetDirection = normalizeDegree(direction);// 赋值给全局变量，让指南针旋转  
            
            
}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		 Log.i("MSensor", "onAccuracyChanged");

	}
	
	 // 这个是更新指南针旋转的线程，handler的灵活使用，每20毫秒检测方向变化值，对应更新指南针旋转  
    protected Runnable mCompassViewUpdater = new Runnable() {  
        @Override  
        public void run() {  
            if (mPointer != null && !mStopDrawing) {  
                if (mDirection != mTargetDirection) {  
  
                    // calculate the short routine  
                    float to = mTargetDirection;  
                    if (to - mDirection > 180) {  
                        to -= 360;  
                    } else if (to - mDirection < -180) {  
                        to += 360;  
                    }  
  
                    // limit the max speed to MAX_ROTATE_DEGREE  
                    float distance = to - mDirection;  
                    if (Math.abs(distance) > MAX_ROATE_DEGREE) {  
                        distance = distance > 0 ? MAX_ROATE_DEGREE  
                                : (-1.0f * MAX_ROATE_DEGREE);  
                    }  
  
                    // need to slow down if the distance is short  
                    mDirection = normalizeDegree(mDirection  
                            + ((to - mDirection) * mInterpolator  
                                    .getInterpolation(Math.abs(distance) > MAX_ROATE_DEGREE ? 0.4f  
                                            : 0.3f)));// 用了一个加速动画去旋转图片，很细致  
                    mPointer.updateDirection(mDirection);// 更新指南针旋转  
                }  
               
                mHandler.postDelayed(mCompassViewUpdater, 20);// 20毫米后重新执行自己，比定时器好  
            }  
        }  
    };  
    
    // 调整方向传感器获取的值  
    private float normalizeDegree(float degree) {  
        return (degree + 720) % 360;  
    }  


	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		 setContentView(R.layout.compass);
	}

	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		
	}
	
}
