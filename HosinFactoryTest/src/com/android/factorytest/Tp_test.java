package com.android.factorytest;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;

public class Tp_test extends BaseActivity {
	public static final boolean DEBUG = false;
	private static final String TAG = "Tp_test";
	public static final int REC_SIZE = 80;
	public static int mRecNo = 0;
	private MySurfaceView mSurfaceView = null;
	private boolean mIsRunning = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		

		

	}

	private class MySurfaceView extends SurfaceView implements
			SurfaceHolder.Callback, Runnable {

		private final static String TAG = "MySurfaceView";
		
		private SurfaceHolder mHolder;
		private Paint mPaint;//add by messi
		private Paint mPaint1;
		private Paint mPaint2;
		private Paint mPaint3;
		private Paint mPaint4;
		private Paint mPaint5;//add by messi
		private Paint mPaint6;//add by messi
		private Paint mPaint7;
		private Paint mPaint8;
		private float delta;
		
		private float k1,k2;//斜率
		private PointF point1,point2,point3,point4,point5,point6,point7,point8;
		private Path path1,path2;
		private Path mPath;
		private Path mTempPath;
		private ArrayList<Path> mPaths;
		private SquareFlag mSquareFlag = new SquareFlag();
		private boolean mIsFirst = true;
		private Thread mThread;//开启一条线程用于绘图
		private Thread caculateThread;//用于计算并判断是否该退出
		private Point mTempPoint = new Point();
		private ArrayList<Rect> mTempRect = new ArrayList<Rect>();
		private ArrayList<PointF> mTempPoints = new 	ArrayList<PointF>();//add by messi
		private ArrayList<PointF> mYellowRegionPoints = new ArrayList<PointF>();//add by messi
		private ArrayList<Float> mYellowSpace = new ArrayList<Float>();
		private ArrayList<PointF> mBlueRegionPoints = new ArrayList<PointF>();//add by messi
		private ArrayList<Float> mBlueSpace = new ArrayList<Float>();
		private boolean isTpTestPassOver = true;//用于判断isTpTestPass()方法时候结束
 		public MySurfaceView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			mHolder = this.getHolder();
			mHolder.addCallback(this);

			mPaths = new ArrayList<Path>();
			mPath = new Path();
			mPaint = new Paint();
			mPaint.setColor(Color.WHITE);
			mPaint.setStyle(Paint.Style.FILL);
			
			mPaint1 = new Paint();
			mPaint1.setColor(Color.BLACK);
			mPaint1.setStyle(Paint.Style.STROKE);
			mPaint1.setXfermode(new PorterDuffXfermode(Mode.XOR));
			mPaint1.setStrokeWidth(1);
			mPaint1.setAntiAlias(true);
			
			mPaint2 = new Paint();
			mPaint2.setColor(Color.GRAY);
			mPaint2.setStyle(Paint.Style.FILL);
			mPaint2.setStrokeCap(Paint.Cap.ROUND);
			mPaint2.setAntiAlias(true);

			mPaint3 = new Paint();
			mPaint3.setColor(Color.RED);
			mPaint3.setStyle(Paint.Style.STROKE);
			mPaint3.setStrokeWidth(1);
			mPaint3.setStrokeCap(Paint.Cap.ROUND);
			mPaint3.setAntiAlias(true);
			
			mPaint4 = new Paint();
			mPaint4.setColor(Color.BLUE);
			mPaint4.setStyle(Paint.Style.STROKE);
			mPaint4.setStrokeWidth(4);
			mPaint4.setStrokeCap(Paint.Cap.ROUND);
			mPaint4.setAntiAlias(true);

			mPaint5 = new Paint();
			mPaint5.setColor(getResources().getColor(R.color.ctp_yellow));
			mPaint5.setStyle(Paint.Style.STROKE);
			mPaint5.setStrokeWidth(2);
			mPaint5.setStrokeCap(Paint.Cap.ROUND);
			mPaint5.setAntiAlias(true);
			
			mPaint6 = new Paint();
			mPaint6.setColor(getResources().getColor(R.color.ctp_light_blue));
			mPaint6.setStyle(Paint.Style.STROKE);
			mPaint6.setStrokeWidth(2);
			mPaint6.setStrokeCap(Paint.Cap.ROUND);
			mPaint6.setAntiAlias(true);
			
			mPaint7 = new Paint();
			mPaint7.setColor(getResources().getColor(R.color.ctp_yellow));
			mPaint7.setStyle(Paint.Style.FILL);
		//	mPaint7.setXfermode( new PorterDuffXfermode(Mode.SRC_OVER));
		//	mPaint7.setStrokeWidth(2);
			mPaint7.setStrokeCap(Paint.Cap.ROUND);
			mPaint7.setAntiAlias(true);
			
			mPaint8 = new Paint();
			mPaint8.setColor(getResources().getColor(R.color.ctp_light_blue));
			mPaint8.setStyle(Paint.Style.FILL);
		//	mPaint8.setXfermode( new PorterDuffXfermode(Mode.SRC_OVER));
		//	mPaint8.setStrokeWidth(2);
			mPaint8.setStrokeCap(Paint.Cap.ROUND);
			mPaint8.setAntiAlias(true);
			getWindowManager().getDefaultDisplay().getSize(mTempPoint);
			k1 = (float)(Tp_test.REC_SIZE-mTempPoint.x)/(float)(mTempPoint.y-Tp_test.REC_SIZE);
			k2 = -k1;
			caculatePoint();
		}
 		/**
 		 * 根据黄色区域里面的点求要绘制的path的四个边界点
 		 * @param p
 		 * @return
 		 */
 		private PointF[]  caculateYellowPoint(PointF p){//k为斜率 
 		
 			float temp =0;
 			PointF[] points = new PointF[4];
 			float px = p.x;
 			float py = p.y;
 			temp =(point2.y - point1.y)*(point2.y-point1.y)+(point2.x - point1.x)*(point2.x - point1.x);
 			delta = (float)Math.sqrt(temp)/2;
 			PointF p1 = new PointF();
 			p1.x = (Tp_test.REC_SIZE+k1*px-py-delta*(float)(Math.sqrt(k1*k1+1)))*k1/(k1*k1+1);
 			temp = p1.x;
 			p1.y = -temp/k1+Tp_test.REC_SIZE;
 			points[0] = p1;
 			
 			PointF p2 = new PointF();
 			p2.x = (Tp_test.REC_SIZE/k1+k1*px-py-delta*(float)(Math.sqrt(k1*k1+1)))*k1/(k1*k1+1);
 			temp = p2.x;
 			p2.y = -temp/k1+Tp_test.REC_SIZE/k1;
 			points[1] = p2;
 			
 			PointF p3 = new PointF();
 			p3.x = (Tp_test.REC_SIZE/k1+k1*px-py+delta*(float)(Math.sqrt(k1*k1+1)))*k1/(k1*k1+1);
 			temp = p3.x;
 			p3.y = -temp/k1+Tp_test.REC_SIZE/k1;
 			points[2] = p3;
 			
 			PointF p4 = new PointF();
 			p4.x = (Tp_test.REC_SIZE+k1*px-py+delta*(float)(Math.sqrt(k1*k1+1)))*k1/(k1*k1+1);
 			temp = p4.x;
 			p4.y = -temp/k1+Tp_test.REC_SIZE;
 			points[3] = p4;
 			
 		
 			return points;
 		}
 		/**
 		 * 根据蓝色区域里面的点求要绘制的path的四个边界点
 		 * @param p
 		 * @return
 		 */
 		private PointF[] caculateBluePoint(PointF p){
 			float temp =0;
 			PointF[] points = new PointF[4];
 			float px = p.x;
 			float py = p.y;
 			temp =(point2.y - point1.y)*(point2.y-point1.y)+(point2.x - point1.x)*(point2.x - point1.x);
 			delta = (float)Math.sqrt(temp)/2;
 			PointF p1 = new PointF();
 			p1.x = (k2*px+mTempPoint.y-py-Tp_test.REC_SIZE - delta*(float)(Math.sqrt(k2*k2+1)))*k2/(k2*k2+1);
 			temp = p1.x;
 			p1.y = -temp/k2+mTempPoint.y -Tp_test.REC_SIZE;
 			points[0] = p1;
 			
 			PointF p2 = new PointF();
 			p2.x = (k2*px-py+Tp_test.REC_SIZE+mTempPoint.x/k2 - delta*(float)(Math.sqrt(k2*k2+1)))*k2/(k2*k2+1);
 			temp = p2.x;
 			p2.y = -temp/k2+Tp_test.REC_SIZE+mTempPoint.x/k2;
 			points[1] = p2;
 			
 			PointF p3 = new PointF();
 			p3.x = (k2*px-py+Tp_test.REC_SIZE+mTempPoint.x/k2 +delta*(float)(Math.sqrt(k2*k2+1)))*k2/(k2*k2+1);
 			temp = p3.x;
 			p3.y = -temp/k2+Tp_test.REC_SIZE+mTempPoint.x/k2;
 			points[2] = p3;
 			
 			PointF p4 = new PointF();
 			p4.x = (k2*px+mTempPoint.y-py-Tp_test.REC_SIZE + delta*(float)(Math.sqrt(k2*k2+1)))*k2/(k2*k2+1);
 			temp = p4.x;
 			p4.y = -temp/k2+mTempPoint.y -Tp_test.REC_SIZE;
 			points[3] = p4;
 			
 		
 			return points;
 		}
 		private void caculatePoint(){
 			float temp =0;
 			k1 = (float)(Tp_test.REC_SIZE-mTempPoint.x)/(float)(mTempPoint.y-Tp_test.REC_SIZE);
			k2 = -k1;
			point1 = new PointF();
			point1.x = (k1*k1*Tp_test.REC_SIZE)/(k1*k1+1);
			point1.y =  Tp_test.REC_SIZE-(k1*k1*Tp_test.REC_SIZE)/((k1*k1+1)*k1);
			
			point2 = new PointF();
			point2.x =(k1*k1-k1+1)*Tp_test.REC_SIZE/(k1*k1+1);
			point2.y = (k1*k1-k1+1)*Tp_test.REC_SIZE*k1/(k1*k1+1)-k1*Tp_test.REC_SIZE+Tp_test.REC_SIZE;
			
			point3 = new PointF();
			point3.x = (Tp_test.REC_SIZE +(mTempPoint.x-Tp_test.REC_SIZE)*k1+Tp_test.REC_SIZE-mTempPoint.y)*k1/(k1*k1+1);
			temp = point3.x;
			point3.y = -temp/k1+Tp_test.REC_SIZE;
			
			point4 = new PointF();
			point4.x = (Tp_test.REC_SIZE/k1 +(mTempPoint.x-Tp_test.REC_SIZE)*k1+Tp_test.REC_SIZE-mTempPoint.y)*k1/(k1*k1+1);
			temp = point4.x;
			point4.y = -temp/k1+Tp_test.REC_SIZE/k1;
			
			point5 = new PointF();
			point5.x = (mTempPoint.y - Tp_test.REC_SIZE + k2*(mTempPoint.x-Tp_test.REC_SIZE)-Tp_test.REC_SIZE)*k2/(k2*k2+1);
			temp = point5.x;
			point5.y = k2*temp+Tp_test.REC_SIZE-k2*(mTempPoint.x-Tp_test.REC_SIZE);
			
			point6 = new PointF();
			point6.x = (mTempPoint.y+Tp_test.REC_SIZE/k2 -Tp_test.REC_SIZE+k2*(mTempPoint.x-Tp_test.REC_SIZE))*k2/(k2*k2+1);
			temp = point4.x;
			point6.y = -temp/k2+Tp_test.REC_SIZE/k2+mTempPoint.y;
			
			
			point7 = new PointF();
			point7.x = (mTempPoint.y - Tp_test.REC_SIZE +Tp_test.REC_SIZE*k2+Tp_test.REC_SIZE-mTempPoint.y)*k2/(k2*k2+1);
			temp = point7.x;
			point7.y = k2*temp+mTempPoint.y-Tp_test.REC_SIZE-Tp_test.REC_SIZE*k2;
			
			point8 = new PointF();
			point8.x = (Tp_test.REC_SIZE+Tp_test.REC_SIZE*k2+Tp_test.REC_SIZE/k2)*k2/(k2*k2+1);
			temp = point8.x;
			point8.y = k2*temp+mTempPoint.y-Tp_test.REC_SIZE-Tp_test.REC_SIZE*k2;
 		}
 		/**
 		 * 计算p到直线p1p2的距离
 		 */
 		private float pointToLine(PointF p1 ,PointF p2, PointF p){
 			float space = 0;
 			float a,b,c;
 			a = lineSpace(p1, p2);//线段的长度
 			b = lineSpace(p, p1);//点到p1的长度
 			c = lineSpace(p, p2);//点到p2的长度
 		     float  d= (a + b + c) / 2;// 半周长  
 		   
            double s = Math.sqrt(d * (d - a) * (d - b) * (d - c));// 海伦公式求面积  
   
            space = (float) (2 * s / a);// 返回点到线的距离（利用三角形面积公式求高）  
 			return space;
 		}
 		/**
 		 * 计算两点的距离
 		 * @param p1
 		 * @param p2
 		 * @return
 		 */
 		 private float lineSpace(PointF p1 ,PointF p2) {  
 			  
 	           float lineLength = 0;  
 	           
 	           lineLength = (float)Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y)* (p1.y - p2.y));
 	  
 	  
 	           return lineLength;
 		 }
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			float x = event.getX();
			float y = event.getY();
			PointF pf =  new PointF(x, y);
			mTempPoints.add(pf);
			if(isInPathYellow(pf)){
				mYellowRegionPoints.add(pf);
				mYellowSpace.add(pointToLine(point1, point2, pf));
				Collections.sort(mYellowSpace);
			}
			if(isInPathBlue(pf)){
				mBlueRegionPoints.add(pf);
				mBlueSpace.add(pointToLine(point5, point6, pf));
				Collections.sort(mBlueSpace);
			}
			int left = 0;
			int top = 0;
			int right = 0;
			int bottom = 0;
			
			for (int i = 0; i < mSquareFlag.mRectList.size(); i++) {
				left = mSquareFlag.mRectList.get(i).left;
				top = mSquareFlag.mRectList.get(i).top;
				right = mSquareFlag.mRectList.get(i).right;
				bottom = mSquareFlag.mRectList.get(i).bottom;
				if (x <= right && x >= left && y <= bottom && y >= top) {
					mSquareFlag.mFlag[i] = true;
				}
			}

			
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				
				mPath.moveTo(x, y);
				break;

			case MotionEvent.ACTION_MOVE:
				mPath.lineTo(x, y);
				break;

			case MotionEvent.ACTION_UP:
				Path tempPath = new Path(mPath);
				mPaths.add(tempPath);
				
				if (isTpTestPass()&&isYellowRegionPast()&&isBlueRegionPast()) {
							mIsRunning = false;
							over(1);
							Tp_test.this.finish();
				
				}

				break;
			}
			return true;
		}
		private boolean isYellowRegionPast(){
			isTpTestPassOver = false;
			ArrayList<Float> temList = new ArrayList<Float>();
			for(int i=0;i<mYellowSpace.size();i++){
				temList.add(mYellowSpace.get(i));
			}
			PointF endPoint = new PointF(mTempPoint.x-Tp_test.REC_SIZE,mTempPoint.y-Tp_test.REC_SIZE);
			if(temList.size() == 0){
				isTpTestPassOver = true;
				return false;
			}
			if(temList.get(0)>2*delta||temList.get(temList.size()-1)<pointToLine(point1,point2,endPoint)-2*delta){
				isTpTestPassOver = true;
				return false;
			}
			
			for(int i=0;i<temList.size()-1;i++) {
			//	Log.i("messi","temList: "+temList.get(i));
				if(temList.get(i)+2*delta < temList.get(i+1)){
					isTpTestPassOver = true;
					return false;
				}
			}
			isTpTestPassOver = true;
			
			return true;
		}
		private boolean isBlueRegionPast(){
			isTpTestPassOver = false;
			ArrayList<Float> temList = new ArrayList<Float>();
			for(int i=0;i<mBlueSpace.size();i++){
				temList.add(mBlueSpace.get(i));
			}
			PointF endPoint = new PointF(mTempPoint.x-Tp_test.REC_SIZE,mTempPoint.y-Tp_test.REC_SIZE);
			if(temList.size() == 0){
				isTpTestPassOver = true;
				return false;
			}
			if(temList.get(0)>2*delta||temList.get(temList.size()-1)<pointToLine(point1,point2,endPoint)-2*delta){
				isTpTestPassOver = true;
				return false;
			}
			
			for(int i=0;i<temList.size()-1;i++) {
			//	Log.i("messi","temList: "+temList.get(i));
				if(temList.get(i)+2*delta < temList.get(i+1)){
					isTpTestPassOver = true;
					return false;
				}
			}
			isTpTestPassOver = true;
			
			return true;
		}
		private boolean isTpTestPass() {
			for (int i = 0; i < mSquareFlag.mRectList.size(); i++) {
				if (mSquareFlag.mFlag[i] == true) {
					continue;
				} else {
					isTpTestPassOver = true;
					return false;
				}
			}
		return	true;
		
		}
		private boolean isInPathBlue(PointF p){
			float x = p.x;
			float y = p.y;
			if(y>-x/k2+mTempPoint.y-Tp_test.REC_SIZE&&y<-x/k2+mTempPoint.y + Tp_test.REC_SIZE/k2){
				return true;
			}
			
			return false;
		}
		private boolean isInPathYellow(PointF p){
			float x = p.x;
			float y = p.y;
			if(y>-x/k1+Tp_test.REC_SIZE/k1&&y<-x/k1+Tp_test.REC_SIZE){
				return true;
			}
			
			return false;
		}
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			if (DEBUG)
				Log.d(TAG, "suxy surfaceCreated(), mRecNo = " + Tp_test.mRecNo);

			// getSize(), old API. @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
			getWindowManager().getDefaultDisplay().getSize(mTempPoint);

			mIsRunning = true;
			if (mTempRect.size() == 0) { // suxy add this conditions
				for (int i = 0; i < Tp_test.mRecNo; i++) {
					mTempRect.add(new Rect());
				}
			}

			if (mThread == null) {
				mThread = new Thread(this);
				mThread.start();
			}
			
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (mIsRunning && !Thread.interrupted()) {
				synchronized (mHolder) {
					Canvas canvas = null;
					try {
						canvas = mHolder.lockCanvas();
						if (canvas != null) {
							drawing(canvas);
							
						}
					} finally {
						if (canvas != null) {
							mHolder.unlockCanvasAndPost(canvas);
						}
					}
				}
			}
			if (DEBUG)
				Log.d(TAG, "suxy run(end)");
		}
		
		private void drawing(Canvas c) {
			
			int counter = 0;
			int sideLength = Tp_test.REC_SIZE;
			// T70 p.x=540,p.y=960,n=75=27+48 square's width is 20
			// V90 p.x=720,p.y=1280,n=100=36+64 square's width is 20
			c.drawRect(0,0,mTempPoint.x, mTempPoint.y,mPaint);
			
			for(int i=0;i<mYellowRegionPoints.size();i++){
				PointF p= 	mYellowRegionPoints.get(i);
				
					
					PointF[] pointFs = new PointF[4];
					pointFs =	caculateYellowPoint(p);
					Path path = new Path();
					path.moveTo(pointFs[0].x, pointFs[0].y);
					path.lineTo(pointFs[1].x, pointFs[1].y);
					path.lineTo(pointFs[2].x, pointFs[2].y);
					path.lineTo(pointFs[3].x, pointFs[3].y);
					c.drawPath(path, mPaint7);//画黄色的填充区域
				
				
			}
			for(int i=0;i<mBlueRegionPoints.size();i++){
				PointF p= 	mBlueRegionPoints.get(i);
				
					
					PointF[] pointFs = new PointF[4];
					pointFs =	caculateBluePoint(p);
					Path path = new Path();
					path.moveTo(pointFs[0].x, pointFs[0].y);
					path.lineTo(pointFs[1].x, pointFs[1].y);
					path.lineTo(pointFs[2].x, pointFs[2].y);
					path.lineTo(pointFs[3].x, pointFs[3].y);
					c.drawPath(path, mPaint8);//画黄色的填充区域
				
				
			}
			
			for (int a = 0; a <= mTempPoint.x - sideLength; a += sideLength) {// top
				mTempRect.get(counter).left = a;
				mTempRect.get(counter).top = 0;
				mTempRect.get(counter).right = a + sideLength;
				mTempRect.get(counter).bottom = sideLength;
				c.drawRect(mTempRect.get(counter), mPaint1);
				c.drawRect(mTempRect.get(counter), mPaint);
				if (mSquareFlag.mFlag[counter] == true) {
				
					c.drawRect(mTempRect.get(counter), mPaint2);

				}
				if (mIsFirst) {
					mSquareFlag.mRectList.add(mTempRect.get(counter));
				}
				counter++;
			}

			for (int a = 0; a <= mTempPoint.x - sideLength; a += sideLength) {// bottom
				mTempRect.get(counter).left = a;
				mTempRect.get(counter).top = mTempPoint.y - sideLength;
				mTempRect.get(counter).right = a + sideLength;
				mTempRect.get(counter).bottom = mTempPoint.y;
				c.drawRect(mTempRect.get(counter), mPaint1);
				c.drawRect(mTempRect.get(counter), mPaint);
				if (mSquareFlag.mFlag[counter] == true) {
					c.drawRect(mTempRect.get(counter), mPaint2);
				} 
				if (mIsFirst) {
					mSquareFlag.mRectList.add(mTempRect.get(counter));
				}
				counter++;
			}
			for (int a = 0; a <= mTempPoint.y - sideLength; a += sideLength) {// left
				mTempRect.get(counter).left = 0;
				mTempRect.get(counter).top = a;
				mTempRect.get(counter).right = sideLength;
				mTempRect.get(counter).bottom = a + sideLength;
				c.drawRect(mTempRect.get(counter), mPaint1);
				c.drawRect(mTempRect.get(counter), mPaint);
				if (mSquareFlag.mFlag[counter] == true) {
					c.drawRect(mTempRect.get(counter), mPaint2);
				} 
			
				if (mIsFirst) {
					mSquareFlag.mRectList.add(mTempRect.get(counter));
				}
				counter++;
			}

			for (int a = 0; a <= mTempPoint.y - sideLength; a += sideLength) {// right
				mTempRect.get(counter).left = mTempPoint.x - sideLength;
				mTempRect.get(counter).top = a;
				mTempRect.get(counter).right = mTempPoint.x;
				mTempRect.get(counter).bottom = a + sideLength;
				c.drawRect(mTempRect.get(counter), mPaint1);
				c.drawRect(mTempRect.get(counter), mPaint);
				if (mSquareFlag.mFlag[counter] == true) {
					c.drawRect(mTempRect.get(counter), mPaint2);
				} 
				
				if (mIsFirst) {
					mSquareFlag.mRectList.add(mTempRect.get(counter));
				}
				counter++;
			}
			c.drawLine(0, sideLength,mTempPoint.x-sideLength, mTempPoint.y,  mPaint5);
			c.drawLine(sideLength, 0,mTempPoint.x, mTempPoint.y-sideLength,  mPaint5);
			c.drawLine(mTempPoint.x-sideLength, 0,0, mTempPoint.y-sideLength,  mPaint6);
			c.drawLine(mTempPoint.x, sideLength,sideLength, mTempPoint.y,  mPaint6);
			
		//	c.drawLine(0, sideLength,mTempPoint.x-sideLength, mTempPoint.y,  mPaint5);
		//	c.restore();
			//画黄色区域
			path1 = new Path();
			path1.moveTo(point1.x, point1.y);
			path1.lineTo(point2.x, point2.y);
			path1.lineTo(point4.x, point4.y);
			path1.lineTo(point3.x, point3.y);
			path1.lineTo(point1.x, point1.y);
			c.drawPath(path1, mPaint5);
			//画蓝色区域
			 path2 = new Path();
			path2.moveTo(point5.x, point5.y);
			path2.lineTo(point6.x, point6.y);
			path2.lineTo(point8.x, point8.y);
			path2.lineTo(point7.x, point7.y);
			path2.lineTo(point5.x, point5.y);
			c.drawPath(path2, mPaint6);
		
		

		
		
			
			//画点
			for(int i=0;i<mTempPoints.size();i++){
				PointF p= 	mTempPoints.get(i);
				
				c.drawPoint(p.x, p.y, mPaint4);
			}
			// for few cases, can NOT draw current path. avoid it.
			// c.drawPath(mPath, mPaint3);
			mTempPath = null;
			if (!mPath.isEmpty()) {
				mTempPath = new Path(mPath);
			}
			if (mTempPath != null) {
				c.drawPath(mTempPath, mPaint3);
			}

			mIsFirst = false;
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			if (DEBUG)
				Log.d(TAG, "suxy surfaceDestroyed()");

			// clean mPaths
			for (int i = 0; i < mPaths.size(); i++) {
				mPaths.get(i).reset();
			}

			// end thread
			if (mThread != null) {
				mThread.interrupt();
				mThread = null;
			}

			if (mIsRunning == true) {
				mIsRunning = false;
			}
		}
	}

	private class SquareFlag {
		public ArrayList<Rect> mRectList = new ArrayList<Rect>();
		public boolean[] mFlag = new boolean[Tp_test.mRecNo];
	}

	private void over(int isOK) {
		SharedPreferences sp;
		SharedPreferences.Editor editor;
		final String TEST = "facotytest";

		sp = getSharedPreferences(TEST, MODE_WORLD_READABLE);
		editor = getSharedPreferences(TEST, MODE_WORLD_READABLE).edit();

		editor.putInt("tp", isOK);

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
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		mIsRunning = false;
		over(0);
		super.onBackPressed();
	}

	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		final Point point = new Point();
		getWindowManager().getDefaultDisplay().getSize(point); // getSize(), old
		// API.
		// @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
		mRecNo = (point.x / REC_SIZE) * 2 + (point.y / REC_SIZE) * 4 - 2;
		// if (DEBUG) Log.d("MySurfaceView", "suxy mRecNo = "+mRecNo);
		mSurfaceView = new MySurfaceView(this);
		LinearLayout layout = new LinearLayout(this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);
		layout.addView(mSurfaceView, params);
		View  view = getLayoutInflater().inflate(R.layout.buttons, null);
		layout.addView(view);
		setContentView(layout);
		mSurfaceView.setSystemUiVisibility(0x00002000);
	}

	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		
		
	}
}
