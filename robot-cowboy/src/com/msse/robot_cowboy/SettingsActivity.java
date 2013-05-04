package com.msse.robot_cowboy;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

/**
 */
public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
        	.replace(android.R.id.content, new SettingsFragment())
        	.commit();
    }


    /**
     * This fragment shows the preferences for the first header.
     */
    public static class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
        
        @Override
		public void onResume() {
            super.onResume();
            
            SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);
            onSharedPreferenceChanged(sharedPreferences, "pref_data_source");
            onSharedPreferenceChanged(sharedPreferences, "pref_update_period");
            onSharedPreferenceChanged(sharedPreferences, "pref_camera_period");
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        	Preference preference = findPreference(key);
        	String value = sharedPreferences.getString(key, "");
        	
        	if (preference != null) {
	        	if (preference instanceof ListPreference) {
	        		ListPreference connectionPref = (ListPreference)preference;
	        		
	            	int index = connectionPref.findIndexOfValue(value);
	            	if (index >= 0) {
	            		connectionPref.setSummary(connectionPref.getEntries()[index]);
	            	}
	            	else {
	            		connectionPref.setSummary(null);
	            	}
	        	} else {
	                preference.setSummary(value);
	            }
        	}
        }
    }

}
