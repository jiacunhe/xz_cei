package com.hyrt.ceiphone.adapter;

import java.util.HashMap;
import java.util.List;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.AsyncImageLoader.ImageCallback;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.ceiphone.R;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class GoodClassAdapter extends BaseAdapter {
	private Activity mContext;
	private List<Courseware> data;
	public HashMap<Integer, Drawable> drawables = new HashMap<Integer, Drawable>();
	private AsyncImageLoader asyncImageLoader;
	private Gallery gallery;
	public GoodClassAdapter(Activity c,List<Courseware> data,Gallery gallery) {
		mContext = c;
		this.data=data;
		this.gallery=gallery;
		asyncImageLoader = ((CeiApplication) c.getApplication()).asyncImageLoader;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position%5);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new ImageView(mContext);
		}
		convertView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		((ImageView)convertView).setScaleType(ImageView.ScaleType.FIT_XY);
		convertView.setTag(data.get(position%5).getBigPath());
		convertView.setBackgroundResource(R.drawable.courseware_big_default_icon);
		if (drawables.containsKey(Integer.valueOf(position%5))
				&& drawables.get(Integer.valueOf(position%5)) != null) {
			convertView.setBackgroundDrawable(drawables.get(Integer.valueOf(position%5)));
		}else{
		   ImageResourse imageResource = new ImageResourse();
		   imageResource.setIconUrl(data.get(position%5).getBigPath());
		   imageResource.setIconId(data.get(position%5).getClassId());
		   imageResource.setIconTime(data.get(position%5).getProTime());
		   System.out.println(123);
		   asyncImageLoader.loadDrawable(imageResource, new ImageCallback() {

			   @Override
			   public void imageLoaded(Drawable imageDrawable, String imageUrl) {
				   ImageView img = (ImageView) gallery.findViewWithTag(data.get(position%5).getBigPath());
				   if (img != null && imageDrawable != null) {
					   img.setBackgroundDrawable(imageDrawable);
					   drawables.put(Integer.valueOf(position%5),imageDrawable);
				   }
			   }
		   });
		 }
		return convertView;
	}

}
