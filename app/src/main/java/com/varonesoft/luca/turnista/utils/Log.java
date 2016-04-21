package com.varonesoft.luca.turnista.utils;

/**
 * Created by luca on 13/04/16.
 */
public class Log {

    // Default TAG
    private final static String TAG = "Turnista";

    // Debug
    private final static boolean DEBUG = true;

    public static void d( String msg) {
        if (DEBUG)
            d( TAG, msg);
    }

    public static void d( String tag, String msg) {
        if (DEBUG)
            android.util.Log.d(tag, msg);
    }
}
