package edu.umn.robotcontrol.robotwhisky;

import android.location.Location;
import edu.umn.robotcontrol.domain.RobotPosition;

/*
 * android.location is not available on a non-android platform.  Use this
 * class to easily convert between the JSON-serializable version and the
 * Android version.
 */
public class PositionFacade {

	public static RobotPosition locToRobotPos(Location loc) {
		RobotPosition pos = new RobotPosition();
		pos.setAccuracy(loc.getAccuracy());
		pos.setAltitude(loc.getAltitude());
		pos.setBearing(loc.getBearing());
		pos.setLatitude(loc.getLatitude());
		pos.setLongitude(loc.getLongitude());
		pos.setProvider(loc.getProvider());
		pos.setSpeed(loc.getSpeed());
		pos.setValidityTime(loc.getTime());

		return pos;
	}

	public static Location robotPosToLoc(RobotPosition pos) {
		Location loc = new Location(pos.getProvider());
		
		loc.setAccuracy(pos.getAccuracy());
		loc.setAltitude(pos.getAltitude());
		loc.setBearing(pos.getBearing());
		loc.setLatitude(pos.getLatitude());
		loc.setLongitude(pos.getLongitude());
		loc.setProvider(pos.getProvider());
		loc.setSpeed(pos.getSpeed());
		loc.setTime(pos.getValidityTime());
		
		return loc;
	}
}
