package com.example.brookesradio;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private boolean playPause;
	private MediaPlayer mediaPlayer;
	private ImageView notStreaming;
	private ImageView streaming;
	private boolean isFirstImage = true;
	private boolean initialStage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		notStreaming = (ImageView) findViewById(R.id.ImageView01);
		streaming = (ImageView) findViewById(R.id.ImageView02);
		streaming.setVisibility(View.GONE);
		
		String url = "http://5.152.208.98:8062/;stream.mp3";
		Button stream_radio = (Button) findViewById(R.id.stream_button);
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//		try {
//			mediaPlayer.setDataSource(url);
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalStateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			mediaPlayer.prepare();
//		} catch (IllegalStateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//mediaPlayer.start();
		//stream_radio.setOnClickListener(pausePlay);
		stream_radio.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isFirstImage) {
					applyRotation(0, -90);
					isFirstImage = !isFirstImage;
				} else {
					applyRotation(0, 90);
					isFirstImage = !isFirstImage;
				}
			}
		});
	}
	
	public void applyRotation(float start, float end) {
		// Find the center of the image
		final float centerX = notStreaming.getWidth() / 2.0f;
		final float centerY = notStreaming.getHeight() / 2.0f;
		
		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Flip3dAnimation rotation = 
				new Flip3dAnimation(start, end, centerX, centerY);
		rotation.setDuration(1000);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(isFirstImage, notStreaming, streaming));
		
		if (isFirstImage){
			notStreaming.startAnimation(rotation);
		} else {
			streaming.startAnimation(rotation);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

//	private OnClickListener pausePlay = new OnClickListener() {
		
		
		
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			if (!playPause) {
//				if (initialStage)
//					new Player()
//							.execute("http://project-tango.org/Projects/TangoBand/Songs/files/01%20La%20Cumparsita.mp3");
//
//				else {
//					if (!mediaPlayer.isPlaying())
//						mediaPlayer.start();
//				}
//				playPause = true;
//			} else {
//				if (mediaPlayer.isPlaying())
//					mediaPlayer.pause();
//				playPause = false;
//			}
//		}
//	};
//
//	class Player extends AsyncTask<String, Void, Boolean> {
//		private ProgressDialog progress;
//
//		@Override
//		protected Boolean doInBackground(String... arg0) {
//			// TODO Auto-generated method stub
//			Boolean prepared;
//			try{
//				mediaPlayer.setDataSource("project-tango.org/Projects/TangoBand/Songs/files/01%20La%20Cumparsita.mp3");
//				
//				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
//
//					@Override
//					public void onCompletion(MediaPlayer mediaPlayer) {
//						// TODO Auto-generated method stub
//						initialStage = true;
//						playPause = false;
//						mediaPlayer.stop();
//						mediaPlayer.reset();
//					}
//			});
//				mediaPlayer.prepare();
//				prepared = true;
//			}
//			catch (IllegalArgumentException e){
//				Log.d("Illegal Argument", e.getMessage());
//				prepared = false;
//				e.printStackTrace();
//			}catch (SecurityException e) {
//				prepared = false;
//				e.printStackTrace();
//			}catch (IllegalStateException e) {
//				prepared = false;
//				e.printStackTrace();
//			}catch (IOException e) {
//				prepared = false;
//	            e.printStackTrace();
//			}
//			return prepared;
//		}
//		
//		@Override
//		protected void onPostExecute(Boolean result){
//			super.onPostExecute(result);
//			if (progress.isShowing()) {
//				progress.cancel();
//			}
//			Log.d("Prepared", "//" + result);
//			mediaPlayer.start();
//			
//			initialStage = false;
//		}
//		
//		public Player() {
//			progress = new ProgressDialog(MainActivity.this);
//		}
//		
//		@Override
//		protected void onPreExecute(){
//			super.onPreExecute();
//			this.progress.setMessage("Buffering...");
//			this.progress.show();
//		}
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		if (mediaPlayer != null) {
//			mediaPlayer.reset();
//			mediaPlayer.release();
//			mediaPlayer = null;
//		}
//	}
}
