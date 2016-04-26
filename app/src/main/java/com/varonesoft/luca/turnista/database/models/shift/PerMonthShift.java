package com.varonesoft.luca.turnista.database.models.shift;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.varonesoft.luca.turnista.database.OpenHelper;
import com.varonesoft.luca.turnista.database.models.Creator;
import com.varonesoft.luca.turnista.database.models.Scheme;
import com.varonesoft.luca.turnista.utils.Log;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Created by luca on 21/04/16.
 * <p/>
 * Ritorniamo tutti i turni
 * Nella classe si calcolano i turni, dato lo schema
 */
public class PerMonthShift {

    // TAG
    private static String TAG = "PerMonthShift";

    // Istance Reference
    private static PerMonthShift ourInstance = new PerMonthShift();

    public static PerMonthShift getInstance() {
        return ourInstance;
    }

    private PerMonthShift() {
    }

    private long mCreatorID = -1;
    private Creator mCreator = null;
    private ArrayList<Scheme> mSchemeList;
    private int mSchemaSize = -1;

    // Kepp track oh the month
    public int mYear = 2016;
    public int mMonth = 4;


    /*
     * @return Returns an array of shifts
     *
     */
    public ArrayList<Scheme> getShifts(Context context, long creator) {
        if (mCreatorID != creator || mCreator == null || mSchemeList.isEmpty()) {
            mCreatorID = creator;
            mCreator = fetchCreator(context, creator);
            mSchemeList = fetchScheme(context, creator);
            mSchemaSize = mSchemeList.size();
        }

        LocalDate mSchemeStartDay = new LocalDate(mCreator.start_date);
        LocalDate mRequestedStartDate = new LocalDate(mYear, mMonth, 1);

        boolean reqGreater = mRequestedStartDate.isAfter(mSchemeStartDay) || mRequestedStartDate.isEqual(mSchemeStartDay);
        int difference = Days.daysBetween(mSchemeStartDay, mRequestedStartDate).getDays();
        int startoffset = (difference > 0) ? (difference % mSchemaSize) : 0;

        // Sets
        Calendar cal = new GregorianCalendar(mYear,mMonth-1,1);
        //cal.set(mYear, mMonth - 1, 1);
        int daysInMonth = cal.getMaximum(Calendar.DAY_OF_MONTH);

        ArrayList<Scheme> result = new ArrayList<>();

        if (reqGreater || !reqGreater) {
            for (int i = 1; i <= daysInMonth; i++) {
                // Put in the list
                Scheme s = mSchemeList.get(startoffset).clone();
                s.date = cal.getTimeInMillis();

                result.add(s);

                cal.add(Calendar.DAY_OF_MONTH, 1);
                startoffset++;
                if (startoffset == mSchemaSize) startoffset = 0;
            }
        }

        return result;
    }

    public ArrayList<Scheme> getShifts(Context context) {
        return getShifts(context, (long) 1);
    }

    private Creator fetchCreator(final Context context, long creator) {
        Creator result = null;
        SQLiteDatabase db = OpenHelper.getInstance(context).getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + Creator.TABLE_NAME + ";", null);
        if ((c != null) && (c.moveToFirst())) {
            result = new Creator(c);
        } else {
            // NOData
        }

        // Don't need it anymore
        c.close();

        if (result != null) Log.d(TAG, "fetchCreator() " + result.toString());
        else Log.d(TAG, "null creator");
        return result;
    }

    private ArrayList<Scheme> fetchScheme(Context context, long creator) {
        ArrayList<Scheme> result = new ArrayList<>();
        SQLiteDatabase db = OpenHelper.getInstance(context).getReadableDatabase();

        Cursor c = db.rawQuery(Scheme.SELECT_STATEMENT, null);
        if ((c != null) && (c.moveToFirst())) {
            do {
                result.add(new Scheme(c));
            } while (c.moveToNext());
        } else {
            // NOData
        }

        // Don't need it anymore
        c.close();

        if (!result.isEmpty())
            Log.d(TAG, "fetchScheme() size: " + result.size() + "; " + result.toString());
        else Log.d(TAG, "empty scheme");
        return result;
    }


    public void addMonth() {
        this.mMonth++;
        if (mMonth == 13) {
            mMonth = 1;
            mYear++;
        }
    }

    public void subMonth() {
        this.mMonth--;
        if (mMonth == 0) {
            mMonth = 12;
            mYear--;
        }
    }
}
