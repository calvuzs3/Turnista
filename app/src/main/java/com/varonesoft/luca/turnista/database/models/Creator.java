package com.varonesoft.luca.turnista.database.models;

/**
 * Created by luca on 13/04/16.
 */
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.varonesoft.luca.turnista.database.ContentProvider;
import com.varonesoft.luca.turnista.utils.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.varonesoft.luca.turnista.utils.DateTimeFormatter.getDate;
import static java.lang.Math.*;

public class Creator extends DAO {

    // TAG
    private static String TAG = "DB Creator";

    // PATH
    public static final String PATH="creator";

    // DATABASE TABLE
    public static final String TABLE_NAME = ContentProvider.BASE_TABLE_NAME + PATH;

    // CONTENT TYPE
    public static final String CONTENT_TYPE = ContentProvider.BASE_CONTENT_TYPE + PATH;

    // BASE CODES
    public static final int BASEURICODE = 14001;
    public static final int BASEITEMCODE = 14011;

    // Uri
    public static final Uri URI = Uri.withAppendedPath(
            Uri.parse(ContentProvider.SCHEME + ContentProvider.AUTHORITY), PATH);

    // Add to the matcherUris thi codes
    public static void addMatcherUris(UriMatcher sURIMatcher) {
        sURIMatcher.addURI(ContentProvider.AUTHORITY, PATH, BASEURICODE);
        sURIMatcher.addURI(ContentProvider.AUTHORITY, PATH + "/#", BASEITEMCODE);
    }

    // Inner Class Columns
    public static class Columns implements BaseColumns {

        // This Class cannot be instantiated
        private Columns() {
        }

        // Table FIELDS
        public static final String NAME = "m_name";
        public static final String START_DATE = "m_startdate";
        public static final String NUM_REPETITIONS = "m_numrepetitions";
        public static final String UPDATED = "m_updated";

        // Table Fields as Array
        public static final String[] FIELDS = { _ID, NAME, START_DATE, NUM_REPETITIONS, UPDATED};
    } // end-of-class

    // Database creation SQL statement
    public static final String DATABASE_CREATE = new StringBuilder("CREATE TABLE ")
            .append(TABLE_NAME)              .append(" (")
            .append(Columns._ID)             .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
            .append(Columns.NAME)            .append(" TEXT NOT NULL, ")
            .append(Columns.START_DATE)      .append(" INTEGER NOT NULL, ")
            .append(Columns.NUM_REPETITIONS) .append(" INTEGER NOT NULL, ")
            .append(Columns.UPDATED)         .append(" INTEGER NOT NULL);")
            .toString();

    // fields
    public String name = null;
    public Long start_date= null;
    public Long num_repetitions = null;
    public Long updated = null;

    // CONTRUCTOR
    public Creator (final Cursor c) {
        this._id = c.getLong(0);
        this.name = c.getString(1);
        this.start_date = c.getLong(2);
        this.num_repetitions = c.getLong(3);
        this.updated = c.getLong(4);
    }

    public Creator(final Uri uri, final ContentValues values) {
        this(Long.parseLong(uri.getLastPathSegment()), values);
    }

    public Creator(final long id, final ContentValues values) {
        this(values);
        this._id = id;
    }

    public Creator(final ContentValues values) {
        name = values.getAsString(Columns.NAME);
        start_date= values.getAsLong(Columns.START_DATE);
        num_repetitions= values.getAsLong(Columns.NUM_REPETITIONS);
        updated = values.getAsLong(Columns.UPDATED);
    }

    @Override
    public ContentValues getContent() {
        final ContentValues values = new ContentValues();
        // Note that ID is NOT included here
        values.put(Columns.NAME, name);
        values.put(Columns.START_DATE, start_date);
        values.put(Columns.NUM_REPETITIONS, num_repetitions);
        values.put(Columns.UPDATED, updated);
        return values;
    }

    /*
     * Returns the table name
     */
    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getPath() {
        return PATH;
    }

    /*
     * Returns the content type
     */
    @Override
    public String getContentType() {
        // It is public so.. why?
        return CONTENT_TYPE;
    }

    /**
     * Convenience method for normal operations. Updates "updated" field.
     *
     * Returns number of db-rows affected. Fail if < 1
     *
     * @param context
     */
    @Override
    public int save(Context context) {
        return save(context, Calendar.getInstance().getTimeInMillis());
    }

    public int save(Context context, final long update_time) {
        int result = 0;
        updated = update_time;
        if (_id < 1) {
            final Uri uri = context.getContentResolver().insert(getBaseUri(), getContent());
            if (uri != null) {
                _id = Long.parseLong(uri.getLastPathSegment());
                result++;
            }
        } else {
            result += context.getContentResolver().update(getUri(),
                    getContent(), null, null);
        }
        return result;
    }

    /* Init
     * put some default values into it
     * Dont init() it because it assumes values are inserted into the database
     */
    public static void init(SQLiteDatabase database) {
        final ContentValues values=new ContentValues();
        final Calendar cal = GregorianCalendar.getInstance();
        cal.set(2016, 0, 1);

            values.clear();
            values.put(Columns.NAME, "No1");
            values.put(Columns.START_DATE, cal.getTimeInMillis());
            values.put(Columns.NUM_REPETITIONS, (int)(floor(365 / 18)+1));
            values.put(Columns.UPDATED, Calendar.getInstance().getTimeInMillis());
            database.insert(TABLE_NAME, null, values);
            Log.d(TAG, String.format("init() [start=%s] [repetirions=1]", getDate(cal.getTimeInMillis())));
    }
}
