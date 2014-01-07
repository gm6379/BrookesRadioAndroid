package com.georgemcdonnell.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.georgemcdonnell.brookesradio.AboutUsFragment;
import com.georgemcdonnell.brookesradio.RadioFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Radio fragment activity
			return new RadioFragment();
		case 1:
			// About us fragment activity
			return new AboutUsFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}

}
