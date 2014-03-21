package com.hyrt.cei.ui.phonestudy.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class GGridView extends GridView {
	
	public GGridView(Context context) {
		super(context);
	}

	public GGridView(Context context, AttributeSet attributeset) {
		super(context, attributeset);
	}

	public GGridView(Context context, AttributeSet attributeset, int i) {
		super(context, attributeset, i);
	}

	public void setAdapter(BaseAdapter baseAdapter,int currentShowNum) {
		super.setAdapter(baseAdapter);
		if (baseAdapter.getCount() > 0) {
			setNumColumns(baseAdapter.getCount());
			DisplayMetrics displaymetrics = new DisplayMetrics();
			((Activity) getContext()).getWindowManager().getDefaultDisplay()
					.getMetrics(displaymetrics);
			int itemWidth = displaymetrics.widthPixels/currentShowNum;
			setLayoutParams(new android.widget.LinearLayout.LayoutParams(itemWidth * baseAdapter.getCount(),LayoutParams.FILL_PARENT));
		}
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
