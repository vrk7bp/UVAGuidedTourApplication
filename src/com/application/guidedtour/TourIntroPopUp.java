package com.application.guidedtour;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

public class TourIntroPopUp extends Activity {
	
	private PopupWindow pu;
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);       
		
		LayoutInflater inflater = (LayoutInflater)
				this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popLayout = inflater.inflate(R.layout.tourstartpopup,
				(ViewGroup) findViewById(R.id.tourstartpopuplayout));

		pu = new PopupWindow(popLayout, 500, 500, true);

		setContentView(popLayout.findViewById(R.id.tourstartpopuplayout));  

		//pu.showAsDropDown(popLayout.findViewWithTag("@+id/introtext"));
		
		//pu.showAtLocation(popLayout, Gravity.CENTER, 0, 0);


	}

	public void closePopup(View view) {

        Intent startTour = new Intent(this, GuidedTour.class);
        startActivity(startTour);
        
        System.exit(0);
        
	}

}
