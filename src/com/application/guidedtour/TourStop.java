package com.application.guidedtour;

import android.location.Location;

public class TourStop {
	
	private double latitude;
	private double longitude;
	private String history;
	
	public TourStop(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.history = null;
	}

	
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}
	
	public Location toLocation() {
		
		Location loc = new Location("Stop 1");
		
		loc.setLatitude(this.getLatitude());
		
		loc.setLongitude(this.getLongitude());
		
		return loc;
		
		
	}
	
}
