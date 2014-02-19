package com.application.guidedtour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeMenu extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);       
        
        setContentView(R.layout.main);
        
    
    }
	
    public void closeSystem(View view) {
    	
    	System.exit(0);
    	
    }
    
    public void startGuidedTour(View view) {
    	
        Intent startTour = new Intent(this, TourIntroPopUp.class);
        startActivity(startTour);
 	
    }

}