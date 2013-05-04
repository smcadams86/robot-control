package edu.umn.robotcontrol.domain;

public class RobotPosition implements Comparable<RobotPosition> {
	private float accuracy;
	private double altitude;
	private float bearing;
	private double latitude;
	private double longitude;
	private String provider;
	private float speed;
	private long validityTime;
	
	public RobotPosition() {
		accuracy = 0;
		altitude = 0;
		bearing = 0;
		latitude = 0;
		longitude = 0;
		provider = "null";
		speed = 0;
		validityTime = 0;
	}

	public float getAccuracy() {
		return accuracy;
	}


	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}


	public double getAltitude() {
		return altitude;
	}


	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}


	public float getBearing() {
		return bearing;
	}


	public void setBearing(float bearing) {
		this.bearing = bearing;
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


	public String getProvider() {
		return provider;
	}


	public void setProvider(String provider) {
		this.provider = provider;
	}


	public float getSpeed() {
		return speed;
	}


	public void setSpeed(float speed) {
		this.speed = speed;
	}


	public long getValidityTime() {
		return validityTime;
	}
	
	public void setValidityTime(long validityTime) {
		this.validityTime = validityTime;
	}
	
	public String toString() {
		return "RobotPosition [provider=" + provider + ", lat=" + latitude + ", lon=" + longitude + "]";
	}

	@Override
	public int compareTo(RobotPosition arg0) {
		return Long.valueOf(validityTime).compareTo(Long.valueOf(arg0.getValidityTime()));
	}
}
