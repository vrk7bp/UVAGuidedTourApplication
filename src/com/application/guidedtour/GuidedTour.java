package com.application.guidedtour;

import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class GuidedTour extends Activity {

	private PopupWindow pu;
	private PopupWindow pu2;
	
	private Handler mHandler = new Handler();
	private Long mStartTime;
	private String mStartTimeString;
	private String mTimeLabel;

	private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1; // in Meters   //for GPS updates
	private static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000; // in Milliseconds

	private static final String PROX_ALERT_INTENT = 
			"Firstdroid.Tutorial.Gps.ProximityAlert";

	private ArrayList<TourStop> mPositions;

	private LocationManager locationManager;

	Intent intent = new Intent(PROX_ALERT_INTENT);

	PendingIntent pI0;
	PendingIntent pI1;
	PendingIntent pI2;
	PendingIntent pI3;
	PendingIntent pI4;

	Bundle bundle = new Bundle();

	private float distance = 0; //distance from location after you've moved
	private float lastDistance; //distance from location before you've moved (distance)
	private float finalDistance; //distance from starting location to 1st stop, and 1st stop to 2nd, etc.
	private ArrayList<Float> distanceList = new ArrayList<Float>();

	boolean coldest; //ranks of hot and coldness		
	boolean colder;
	boolean cold;
	boolean warm;
	boolean warmer;
	boolean hot;

	float firstInterval; //distance range linked to each hot and cold statement
	float secondInterval;
	float thirdInterval;
	float fourthInterval;
	float fifthInterval;
	float sixthInterval;
	
	protected View mainLayout;
	protected View popupLayout1;
	protected View popupLayout2;
	protected View popupLayout3;
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);       

		setContentView(R.layout.guidedtour);
		
		LayoutInflater inflater = (LayoutInflater)
				this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mainLayout = inflater.inflate(R.layout.guidedtour,
				(ViewGroup) findViewById(R.id.tourlayout));
		popupLayout1 = inflater.inflate(R.layout.tourstop1,
				(ViewGroup) findViewById(R.id.tourstop1layout));
		popupLayout2 = inflater.inflate(R.layout.tourstop2,
				(ViewGroup) findViewById(R.id.tourstop2layout));

		/* Use the LocationManager class to obtain GPS locations */
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener mlocListener = new MyLocationListener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1000, mlocListener);


		//This takes in the time that the phone has been on when the activity starts.
		mStartTime = SystemClock.uptimeMillis();
		
		
		//((TextView)layout.findViewById(R.id.timer)).setText(mStartTime.getLong);
		
	}
	private void saveProximityAlertPoint() {
		Location location = 
				locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location==null) { //If we don't know where we are right now
			Toast.makeText(this, "No last known location. Aborting...", 
					Toast.LENGTH_LONG).show();
			return;
		}

		createPositions();
		//        registerIntents();
	}

	//	private void registerIntents() {
	//
	//		for(int i = 0; i < mPositions.size(); i++) {
	//			 
	//    		TourStop tourStop = mPositions.get(i);
	//    		setProximityAlert(tourStop.getLatitude(), 
	//    				tourStop.getLongitude(), i + 1, i);
	//    	}
	//    }

    private void createPositions() {
        mPositions = new ArrayList<TourStop>();
        mPositions.add(new TourStop(38.035315, -78.511477));	//Old Dorms
        mPositions.add(new TourStop(38.033498, -78.509599));	//Thornton Hall
        mPositions.add(new TourStop(38.037605, -78.502904));	//Rugby Road
        mPositions.add(new TourStop(38.032231,-78.514599));		//Scott Stadium
        mPositions.add(new TourStop(38.034876,-78.500361));		//The Corner
        mPositions.add(new TourStop(38.035332,-78.503537));		//The Rotunda
 
    }


    private void getDistance() { //makes a list of the distances between starting point to location to location etc.

        float dist = 0;
        Location location = 
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        while (location==null) { //If we don't know where we are right now
            Toast.makeText(this, "No last known location. Aborting...", 
                    Toast.LENGTH_LONG).show();
            location = 
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        createPositions();

        dist = location.distanceTo(mPositions.get(0).toLocation());
        distanceList.add(dist);

        dist = mPositions.get(0).toLocation().distanceTo(mPositions.get(1).toLocation());
        distanceList.add(dist);
        dist = mPositions.get(1).toLocation().distanceTo(mPositions.get(2).toLocation());
        distanceList.add(dist);
        dist = mPositions.get(2).toLocation().distanceTo(mPositions.get(3).toLocation());
        distanceList.add(dist);
        dist = mPositions.get(3).toLocation().distanceTo(mPositions.get(4).toLocation());
        distanceList.add(dist);
   
    }
	
	public void closePopup(View v) {
		
		pu.dismiss();
				
	}
	
	public void createPopup(int i) {
		
		if(i == 0) {
			pu = new PopupWindow(popupLayout1, 200, 200, true);
		}
		if (i == 1) {
			pu2 = new PopupWindow(popupLayout2, 200, 200, true);
		}
		
	}

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			final long start = mStartTime;
			//Compares the current start time with the start of the activity.
			long millis = SystemClock.uptimeMillis() - start;

			//Converts the milli seconds to the necessary units
			int seconds = (int) (millis / 1000);
			int minutes = seconds / 60;
			seconds     = seconds % 60;

			//Creates a String Statement that takes into account formatting.
			if (seconds < 10) {
				mTimeLabel = "It has been " + minutes + " minutes. \n" + "It has been :0" + seconds + " seconds.";
			} else {
				mTimeLabel = "It has been " + minutes + " minutes. \n" + "It has been " + seconds + " seconds.";          
			}
			//		     
			//		       mHandler.postAtTime(this,
			//		               start + (((minutes * 60) + seconds + 1) * 1000));
		}
	};

	/* Class My Location Listener */

	public class MyLocationListener implements LocationListener {

		public Location destination;
		public int itr = 0;
//		public float firstInterval;
//		public float secondInterval;
//		public float thirdInterval;
//		public float fourthInterval;
//		public float fifthInterval;
		private boolean firstTime = true;
		String text = "history";
		String text2 = "On to the next tour stop";

		public void onLocationChanged(Location currentLoc) {	        	

        	String s = "first time = " + firstTime;
        	Toast.makeText(GuidedTour.this, s, Toast.LENGTH_LONG).show();
        	
        	
            if(firstTime) {
            	getDistance();
                finalDistance = distanceList.get(itr);
                destination = mPositions.get(itr).toLocation();        
                //distance = currentLoc.distanceTo(destination);

                firstInterval = (finalDistance / 6) - 7;
                secondInterval = 2 * (finalDistance / 6);
                thirdInterval = 3 * (finalDistance / 6);
                fourthInterval = 4 * (finalDistance / 6);
                fifthInterval = 5 * (finalDistance / 6);
                sixthInterval = finalDistance;

            }

			if (distance - lastDistance > 0) { //and this dist

				//call method to make text box that is "coldest"
				mainLayout.setBackgroundColor(Color.BLUE);
				Toast.makeText(GuidedTour.this, "Absolutely ice cold. Turn back!", Toast.LENGTH_LONG).show();

			}

			if (fifthInterval < distance && distance < sixthInterval) {

				//call method to make text box that is "colder"
				mainLayout.setBackgroundColor(Color.argb(255, 132, 112, 255));
				Toast.makeText(GuidedTour.this, "Getting cold", Toast.LENGTH_LONG).show();

			}

			if (fourthInterval < distance && distance < fifthInterval) {

				//call method to make text box that is "cold"
				mainLayout.setBackgroundColor(Color.argb(255, 135, 206, 255));
				Toast.makeText(GuidedTour.this, "Getting a little chilly", Toast.LENGTH_LONG).show();

			}

			if (thirdInterval < distance && distance < fourthInterval) {

				//call method to make text box that is "warm"
				mainLayout.setBackgroundColor(Color.argb(255, 255, 165, 0));
				Toast.makeText(GuidedTour.this, "Just warm", Toast.LENGTH_LONG).show();

			}

			if (secondInterval < distance && distance < thirdInterval) {

				//call method to make text box that is "warmer"
				mainLayout.setBackgroundColor(Color.argb(255, 255, 69, 0));
				Toast.makeText(GuidedTour.this, "Getting Warmer!", Toast.LENGTH_LONG).show();

			}

			if (distance < secondInterval) {


				//call method to make text box that is "hot"
				mainLayout.setBackgroundColor(Color.RED);
				Toast.makeText(GuidedTour.this, "Red Hot!!!", Toast.LENGTH_LONG).show();
			}
			

			if(distance <= 7) {

//				Toast.makeText(GuidedTour.this, text, Toast.LENGTH_LONG).show();
//				Toast.makeText(GuidedTour.this, text2, Toast.LENGTH_LONG).show();
				
				createPopup(itr);

                itr++;

                finalDistance = distanceList.get(itr);
                destination = mPositions.get(itr).toLocation();        
                //distance = currentLoc.distanceTo(destination);

                firstInterval = finalDistance / 6;
                secondInterval = 2 * (finalDistance / 6);
                thirdInterval = 3 * (finalDistance / 6);
                fourthInterval = 4 * (finalDistance / 6);
                fifthInterval = 5 * (finalDistance / 6);
                sixthInterval = finalDistance;   	

			}




		}
		public void onStatusChanged(String s, int i, Bundle b) {            
		}
		public void onProviderDisabled(String s) {
		}
		public void onProviderEnabled(String s) {            
		}
	}

}/* End of UseGps Activity */