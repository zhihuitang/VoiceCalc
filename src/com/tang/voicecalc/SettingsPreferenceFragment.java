package com.tang.voicecalc;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsPreferenceFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

	private ListPreference	soundSetupListPreference;
	private String 			soundSetupString	= "sound_setup_list_preference";
	
	public SettingsPreferenceFragment() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.preference.PreferenceFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		SharedPreferences sp;
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		
		soundSetupListPreference	= (ListPreference)findPreference(soundSetupString);

		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		sp.registerOnSharedPreferenceChangeListener(this);
		
		

	}

	/* (non-Javadoc)
	 * @see android.preference.PreferenceFragment#onStart()
	 */
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		soundSetupListPreference.setSummary(soundSetupListPreference.getEntry());
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		if(key.equals(soundSetupString)){
			CharSequence	entry	= soundSetupListPreference.getEntry();
			soundSetupListPreference.setSummary(entry);
		}
	}

}
