package com.example.luca.calendarioturni.utilities.datamodels;

/**
 * Created by luca on 13/04/16.
 */
import android.graphics.drawable.Drawable;
/**
 * A small helper class that allows us to store an app's
 * name, package name and icon for use by the Spinner.
 *
 * @author brianreber
 */
public class PackageInfoWrapper {
    public CharSequence appName;
    public String packageName;
    public Drawable icon;

    public PackageInfoWrapper(CharSequence appName, String packageName, Drawable icon) {
        this.appName = appName;
        this.packageName = packageName;
        this.icon = icon;
    }

    public String toString() {
        return appName.toString();
    }
}
