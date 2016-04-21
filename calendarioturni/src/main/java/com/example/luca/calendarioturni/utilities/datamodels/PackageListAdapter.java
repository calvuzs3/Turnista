package com.example.luca.calendarioturni.utilities.datamodels;

/**
 * Created by luca on 13/04/16.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luca.calendarioturni.R;

/**
 * A specialized ListAdapter that allows us to use a specific view in the list.
 * In this case, we are able to show the app name, package name, and icon in the list.
 *
 * @author brianreber
 */
public class PackageListAdapter extends ArrayAdapter<PackageInfoWrapper> {

    public PackageListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId, getAppList(context));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.packages, null);
        }

        // Get the necessary views
        TextView appName  = (TextView) v.findViewById(R.id.app_name);
        TextView packageName  = (TextView) v.findViewById(R.id.package_name);
        ImageView iv = (ImageView) v.findViewById(R.id.package_icon);

        // Set the data in the views with the appropriate data
        appName.setText(getItem(position).appName);
        packageName.setText(getItem(position).packageName);
        iv.setImageDrawable(getItem(position).icon);

        appName.setTextColor(getContext().getResources().getColor(R.color.white));
        packageName.setTextColor(getContext().getResources().getColor(R.color.white));

        return v;
    }

    /**
     * Gets a list of installed apps that could be a calendar app.
     *
     * @return
     * A list of apps that could be calendar apps
     */
    private static List<PackageInfoWrapper> getAppList(final Context ctx) {
        List<PackageInfoWrapper> packages = new ArrayList<PackageInfoWrapper>();
        PackageManager packageManager = ctx.getPackageManager();

        packages.add(new PackageInfoWrapper(ctx.getResources().getText(R.string.word_none), "", ctx.getResources().getDrawable(R.drawable.none)));

        for (PackageInfo rs : packageManager.getInstalledPackages(0)) {
            String packageNameLC = rs.packageName.toLowerCase();
            if ((packageNameLC.contains("cal") || packageNameLC.contains("agenda")) && !packageNameLC.contains("calc")) {
                packages.add(new PackageInfoWrapper(rs.applicationInfo.loadLabel(packageManager),
                        rs.packageName, rs.applicationInfo.loadIcon(packageManager)));
            }
        }
        Collections.sort(packages, new Comparator<PackageInfoWrapper>() {
            @Override
            public int compare(PackageInfoWrapper object1,
                               PackageInfoWrapper object2) {
                if (object1.appName.toString().equals(ctx.getResources().getText(R.string.word_none))) {
                    return -1;
                } else if (object2.appName.toString().equals(ctx.getResources().getText(R.string.word_none))) {
                    return 1;
                }

                return object1.appName.toString().compareTo(object2.appName.toString());
            }
        });
        return packages;
    }
}