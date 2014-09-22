package com.example.thetibetanalphabetapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class DrawCancasView extends ImageView {

	public DrawCancasView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public DrawCancasView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public DrawCancasView(Context context) {
		super(context);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		
		mPaint = new Paint();
		mPaint.setColor(0xFF000000);
		mPaint.setStrokeWidth(20);
		mPaint.setAntiAlias(true);
		mPaint.setStrokeCap(Cap.ROUND);
		
		if (mBitmap == null || mBitmap.getWidth() != width || mBitmap.getHeight() != height)
			mBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		setImageBitmap(mBitmap);
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private Bitmap mBitmap;
	private Paint mPaint;
	private boolean mHasStart;
	private PointF mStart = new PointF();
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			mHasStart = true;
			mStart.x = event.getX();
			mStart.y = event.getY();
			break;
			
		case MotionEvent.ACTION_MOVE:
			if (mHasStart) {
				Canvas canvas = new Canvas(mBitmap);
				canvas.drawLine(mStart.x, mStart.y, event.getX(), event.getY(), mPaint);
				mStart.x = event.getX();
				mStart.y = event.getY();
				setImageBitmap(mBitmap);
			}
			break;
			
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			
			mHasStart = false;
			break;
		}
		
		return true;
	}
	
	public void clear() {
		mBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Config.ARGB_8888);
		setImageBitmap(mBitmap);
	}
}
