package com.example.thetibetanalphabetapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class RatioFrameLayout extends FrameLayout {

	public RatioFrameLayout(Context context) {
		super(context);
		init(context);
	}
	
	public RatioFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public RatioFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context) {
		
	}
	
	private double getRatio() {
		String prefix = "ratio:";
		String dsc = getContentDescription().toString();
		if (!dsc.startsWith(prefix))
			return 1;
		
		String ratioStr = dsc.substring(prefix.length());
		try {
			return Double.parseDouble(ratioStr);
		} catch(Exception e) {
			return 1;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mode = MeasureSpec.getMode(widthMeasureSpec);
		int size = MeasureSpec.getSize(widthMeasureSpec);
		int spec = MeasureSpec.makeMeasureSpec((int) (size / getRatio()), mode);
		
		super.onMeasure(widthMeasureSpec, spec);
	}
}
