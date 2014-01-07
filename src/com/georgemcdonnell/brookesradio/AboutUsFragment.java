package com.georgemcdonnell.brookesradio;

import com.georgemcdonnell.brookesradio.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class AboutUsFragment extends Fragment {
	WebView schedule_webview;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.about_us_tab, container,
				false);
		Button view_schedule = (Button) rootView
				.findViewById(R.id.view_schedule_button);
		Button get_involved = (Button) rootView.findViewById(R.id.get_involved_button);
		view_schedule.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("https://www.google.com/calendar/embed?mode=week&src=9e8fb720qmpg1n7isg23nafq18%40group.calendar.google.com&color=%23BE6D00&ctz=Europe/London");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
		
		get_involved.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openEmailToSend(new String[]{"11022129@brookes.ac.uk.com"});
				
			}
		});
		
		
		
		return rootView;
	}
	// Opens the email client and sets the recipient
		public void openEmailToSend(String[] recipients) {
			Intent email = new Intent(Intent.ACTION_SEND);
			email.setType("message/rfc822");
			email.putExtra(Intent.EXTRA_EMAIL, recipients);
			try {
				startActivity(Intent.createChooser(email, "Send mail..."));
			} catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(getActivity(), "There are no email clients installed", Toast.LENGTH_SHORT).show();
			}
		}
}
