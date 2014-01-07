package com.georgemcdonnell.brookesradio;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.georgemcdonnell.brookesradio.R;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.FacebookDialog.PendingCall;
import com.georgemcdonnell.adapter.TabsPagerAdapter;

public class MainActivity extends FragmentActivity implements TabListener {
	private ViewPager viewPager;
	private UiLifecycleHelper uiHelper;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	private String[] tabTitles = { "Radio", "About Us" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Session.StatusCallback callback = new Session.StatusCallback() {

			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
				onSessionStateChange(session, state, exception);

			}
		};

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initialisation of view pager interface
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

		});
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tabName : tabTitles) {
			actionBar.addTab(actionBar.newTab().setText(tabName)
					.setTabListener(this));
		}
		// stream_radio.setOnClickListener(pausePlay);
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			Log.i("Activity", "Logged in...");
		} else if (state.isClosed()) {
			Log.i("Acitivity", "Logged out...");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		uiHelper.onActivityResult(requestCode, resultCode, data,
				new FacebookDialog.Callback() {

					@Override
					public void onError(PendingCall pendingCall,
							Exception error, Bundle data) {
						Log.e("Activity",
								String.format("Error: %s", error.toString()));
					}

					@Override
					public void onComplete(PendingCall pendingCall, Bundle data) {
						Log.i("Activity", "Success!");

					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_email_studio:
			openEmailToSend(new String[] { "djs@brookesradio.com" });
			return true;
		case R.id.action_facebook_page:
			openFacebookIntent();
			return true;
		case R.id.action_twitter_page:
			openTwitterIntent();
		case R.id.action_facebook_share:
			facebookShare();
		case R.id.action_twitter_share:
			// File imagePath = this.getFileStreamPath();
			share("com.twitter.android");
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	// Checks if there is an internet connection available
	public void internetReachable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			Log.i("Main activity", "Internet available");
		} else {
			new AlertDialog.Builder(this)
			.setTitle(R.string.connection_error)
			.setMessage(R.string.error_no_internet)
			.setPositiveButton("OK", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			})
			.show();
		}
	}

	// Opens the email client and sets the recipient
	public void openEmailToSend(String[] recipients) {
		Intent email = new Intent(Intent.ACTION_SEND);
		email.setType("message/rfc822");
		email.putExtra(Intent.EXTRA_EMAIL, recipients);
		try {
			startActivity(Intent.createChooser(email, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(MainActivity.this,
					"There are no email clients installed", Toast.LENGTH_SHORT)
					.show();
		}
	}

	// Opens the facebook page in app if available or browser if not available
	public void openFacebookIntent() {
		try {
			Intent facebook = new Intent(Intent.ACTION_VIEW,
					Uri.parse("fb://profile/293287194023031"));
			startActivity(facebook);
		} catch (Exception e) {
			startActivity(new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("https://www.facebook.com/brookesradio?red=ts&fref=ts)")));
		}
	}

	// Opens the twitter page in app if available or browser if not available
	public void openTwitterIntent() {
		try {
			Intent twitter = new Intent(Intent.ACTION_VIEW,
					Uri.parse("twitter://user?screen_name=BrookesRadio"));
			startActivity(twitter);
		} catch (Exception e) {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.twitter.com/BrookesRadio")));
		}
	}
	
	// Opens a facebook share dialog
	// Provides authentication for user via app id and token
	// Processes call back to app once the post is complete or cancelled
	public void facebookShare() {
		if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
				FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(
					this).setLink("http://brookesradio.com")
					.setName(getString(R.string.app_name))
					.setCaption(getString(R.string.dialog_caption))
					.setPicture("http://gdurl.com/ujvq")
					.setDescription(getString(R.string.description_text))
					.build();
			uiHelper.trackPendingDialogCall(shareDialog.present());
		}
	}

	// Creates a twitter intent
	public void share(String nameApp) {
		try {
			List<Intent> targetedShareIntents = new ArrayList<Intent>();
			Intent share = new Intent(android.content.Intent.ACTION_SEND);
			share.setType("image/png");
			List<ResolveInfo> resInfo = getPackageManager()
					.queryIntentActivities(share, 0);
			if (!resInfo.isEmpty()) {
				for (ResolveInfo info : resInfo) {
					Intent targetedShare = new Intent(
							android.content.Intent.ACTION_SEND);
					targetedShare.setType("image/png");
					if (info.activityInfo.packageName.toLowerCase().contains(
							nameApp)
							|| info.activityInfo.name.toLowerCase().contains(
									nameApp)) {
						// targetedShare.putExtra(Intent.EXTRA_SUBJECT, "test");
						targetedShare.putExtra(Intent.EXTRA_TEXT,
								"Listening to @Brookes Radio :)");
						targetedShare.setPackage(info.activityInfo.packageName);
						targetedShareIntents.add(targetedShare);
					}
				}
				Intent chooserIntent = Intent.createChooser(
						targetedShareIntents.remove(0), "Select app to share");
				chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
						targetedShareIntents.toArray(new Parcelable[] {}));
				startActivity(chooserIntent);

			}
		} catch (Exception e) {
			Log.v("VM",
					"Exception while sending image on" + nameApp + ""
							+ e.getMessage());
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}
}
