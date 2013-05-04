package edu.umn.robotcontrol.robotwhisky;

import android.content.Context;
import android.preference.PreferenceManager;

public class Utility {
	
	public static String buildURL(Context context) {
		String dataSource = PreferenceManager.getDefaultSharedPreferences(context).getString("pref_data_source", "");
		return "http://" + dataSource + "/control";
	}
}
