package com.georgemcdonnell.brookesradio;

import java.io.IOException;

import com.georgemcdonnell.brookesradio.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

public class RadioFragment extends Fragment {
	private MediaPlayer mediaPlayer;
	private ImageView notStreaming;
	private ImageView streaming;
	private boolean isFirstImage = true;
	String url = "http://5.152.208.98:8062/;stream.mp3";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.radio_tab, container, false);
		notStreaming = (ImageView) rootView.findViewById(R.id.ImageView01);
		streaming = (ImageView) rootView.findViewById(R.id.ImageView02);
		streaming.setVisibility(View.GONE);
		final Button stream_radio = (Button) rootView
				.findViewById(R.id.stream_button);
		
		stream_radio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PhoneStateListener listener = new PhoneStateListener() {
					@Override
					public void onCallStateChanged(int state, String incomingNumber){
						if (state == TelephonyManager.CALL_STATE_RINGING) {
							mediaPlayer.pause();
						} else if(state == TelephonyManager.CALL_STATE_IDLE) {
							mediaPlayer.start();
							if (mediaPlayer.isPlaying()) {
								Log.i("radio", "online");
								
							} else {
								stream_radio.setText(R.string.stream_radio);
								stream_radio.setTextColor(Color.parseColor("#4FB4FF"));
								// If the radio is not online an error is displayed
								new AlertDialog.Builder(getActivity())
								.setTitle(R.string.connection_error)
								.setMessage(R.string.error_radio_not_online)
								.setPositiveButton("OK", new DialogInterface.OnClickListener(){
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								})
								.show();
								
							}
						} else if(state == TelephonyManager.CALL_STATE_OFFHOOK){
							mediaPlayer.pause();
						}
						super.onCallStateChanged(state, incomingNumber);
					}
				};
				if (isFirstImage) {
					stream_radio.setText("    Stop     ");
					stream_radio.setTextColor(Color.RED);
					mediaPlayer = new MediaPlayer();
					mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					
					try {
						mediaPlayer.setDataSource(url);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						mediaPlayer.prepare();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					TelephonyManager mgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
					if (mgr != null) {
						mgr.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
					}
					applyRotation(0, -90);
					isFirstImage = !isFirstImage;
				}
					
				 else if (!isFirstImage){
					 stream_radio.setText(R.string.stream_radio);
					 stream_radio.setTextColor(Color.parseColor("#4FB4FF"));
					 mediaPlayer.pause();
					 if (mediaPlayer != null) {
							mediaPlayer.reset();
							mediaPlayer.release();
							mediaPlayer = null;
							TelephonyManager mgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
							if(mgr != null) {
								mgr.listen(listener, PhoneStateListener.LISTEN_NONE);
							}
					 }
					 applyRotation(0, 90);
					 isFirstImage = !isFirstImage;
				}
			}
				
		});
		return rootView;
	}

	// stream_radio.setOnClickListener(pausePlay);

	public void applyRotation(float start, float end) {
		// Find the center of the image
		final float centerX = notStreaming.getWidth() / 2.0f;
		final float centerY = notStreaming.getHeight() / 2.0f;

		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Flip3dAnimation rotation = new Flip3dAnimation(start, end,
				centerX, centerY);
		rotation.setDuration(300);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(isFirstImage,
				notStreaming, streaming));
		if (isFirstImage) {
			notStreaming.startAnimation(rotation);
		} else {
			streaming.startAnimation(rotation);
		}
	}
}