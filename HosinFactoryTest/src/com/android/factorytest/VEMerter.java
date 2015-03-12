package com.android.factorytest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaRecorder;
import android.util.AttributeSet;
import android.view.View;

public class VEMerter extends View {

	static final float PIVOT_RADIUS = 3.5f;
	static final float PIVOT_Y_OFFSET = 10f;
	static final float SHADOW_OFFSET = 2.0f;
	static final float DROPOFF_STEP = 0.18f;
	static final float SURGE_STEP = 0.35f;
	static final long ANIMATION_INTERVAL = 70;
	Paint mPaint, mShadow;
	float mCurrentAngle;
	MediaRecorder recorder;

	public VEMerter(Context context) {
		super(context);

		// TODO Auto-generated constructor stub
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.WHITE);
		mShadow = new Paint(Paint.ANTI_ALIAS_FLAG);
		mShadow.setColor(Color.argb(60, 0, 0, 0));
		recorder = null;
		mCurrentAngle = 0;
	}

	public VEMerter(Context context, AttributeSet attrs) {
		super(context, attrs);

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.WHITE);
		mShadow = new Paint(Paint.ANTI_ALIAS_FLAG);
		mShadow.setColor(Color.argb(60, 0, 0, 0));
		recorder = null;
		mCurrentAngle = 0;
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
		final float minAngle = (float) Math.PI / 8;
		final float maxAngle = (float) Math.PI * 7 / 8;

		float angle = minAngle;
		if (recorder != null)
			angle += (float) (maxAngle - minAngle) * recorder.getMaxAmplitude()
					/ 32768;

		if (angle > mCurrentAngle)
			mCurrentAngle = angle;
		else
			mCurrentAngle = Math.max(angle, mCurrentAngle - DROPOFF_STEP);

		mCurrentAngle = Math.min(maxAngle, mCurrentAngle);

		float w = getWidth();
		float h = getHeight();
		float pivotX = w / 2;
		float pivotY = h - PIVOT_RADIUS - PIVOT_Y_OFFSET;
		float l = h * 7 / 10;
		// reset mCurrentAngle
		if (recorder == null) {
			mCurrentAngle = (float) Math.PI * 1 / 8;
		}
		float sin = (float) Math.sin(mCurrentAngle);
		float cos = (float) Math.cos(mCurrentAngle);
		float x0 = pivotX - l * cos;
		float y0 = pivotY - l * sin;
		canvas.drawLine(x0 + SHADOW_OFFSET, y0 + SHADOW_OFFSET, pivotX
				+ SHADOW_OFFSET, pivotY + SHADOW_OFFSET, mShadow);
		canvas.drawCircle(pivotX + SHADOW_OFFSET, pivotY + SHADOW_OFFSET,
				PIVOT_RADIUS, mShadow);
		canvas.drawLine(x0, y0, pivotX, pivotY, mPaint);
		canvas.drawCircle(pivotX, pivotY, PIVOT_RADIUS, mPaint);
		postInvalidateDelayed(ANIMATION_INTERVAL);
	}

	public void setRecorder(MediaRecorder mRecorder) {
		// indecator_degree = degree;
		recorder = mRecorder;
		invalidate();
	}

}
