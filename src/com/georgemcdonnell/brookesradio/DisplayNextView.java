package com.georgemcdonnell.brookesradio;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public final class DisplayNextView implements AnimationListener {
	private boolean mCurrentView;
	ImageView image1;
	ImageView image2;
	
	public DisplayNextView(boolean currentView, ImageView image1, ImageView image2){
		mCurrentView = currentView;
		this.image1 = image1;
		this.image2 = image2;
	}
	
	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		image1.post(new SwapViews(mCurrentView, image1, image2));
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}

}
