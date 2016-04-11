package com.varonesoft.luca.turnista.utils;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.varonesoft.luca.turnista.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CelebrationDaysListFragment extends Fragment {

    public CelebrationDaysListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_celebration_days_list, container, false);
    }
}
