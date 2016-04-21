package com.example.luca.calendarioturni.fragments;

/**
 * Created by luca on 13/04/16.
 */
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.luca.calendarioturni.R;

/**
 * This is the Activity that gets loaded when the user clicks on the app icon,
 * just like they would do to open any other application.
 *
 * @author brianreber
 */
public class PreferencesFragment extends PreferenceFragment {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.appsettings);
    }

}