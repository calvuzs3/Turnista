package com.varonesoft.luca.turnista.database.models;

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

/**
 * Created by luca on 13/04/16.
 */
public class Marks extends DAO {

    // TAG
    private static String TAG = "DB Marks";

    // PATH
    public static final String PATH = "marks";

    // CONTENT TYPE
    public static final String CONTENT_TYPE = ContentProvider.BASE_CONTENT_TYPE + PATH;

    // DATABASE TABLE
    public static final String TABLE_NAME = ContentProvider.BASE_TABLE_NAME + PATH;

    // BASE CODES
    public static final int BASEURICODE = 4001;
    public static final int BASEITEMCODE = 4011;

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
        public static final String DESC = "m_desc";
        public static final String EXTRA = "m_extra";
        public static final String COLOR= "m_color";
        public static final String UPDATED = "m_updated";

        // Table Fields as Array
        public static final String[] FIELDS = { _ID, NAME, DESC, EXTRA, COLOR, UPDATED};
    } // end-of-class

    // Database creation SQL statement
    public static final String DATABASE_CREATE = new StringBuilder("CREATE TABLE ")
            .append(TABLE_NAME).append("(")
            .append(Columns._ID)             .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
            .append(Columns.NAME)            .append(" TEXT NOT NULL, ")
            .append(Columns.DESC)            .append(" TEXT DEFAULT NULL, ")
            .append(Columns.EXTRA)           .append(" TEXT DEFAULT NULL, ")
            .append(Columns.COLOR )          .append(" INTEGER DEFAULT NULL, ")
            .append(Columns.UPDATED)         .append(" INTEGER DEFAULT NULL);")
            .toString();

    // fields
    public String name = null;
    public String desc = null;
    public String extra = null;
    public Long color = null;
    public Long updated = null;

    // CONTRUCTOR
    public Marks (final Cursor c) {
        this._id = c.getLong(0);
        this.name = c.getString(1);
        this.desc = c.isNull(2) ? null : c.getString(2);
        this.extra = c.isNull(3) ? null : c.getString(3);
        this.color = c.isNull(4) ? null : c.getLong(4);
        this.updated = c.isNull(5) ? null : c.getLong(5);
    }

    public Marks(final Uri uri, final ContentValues values) {
        this(Long.parseLong(uri.getLastPathSegment()), values);
    }

    public Marks(final long id, final ContentValues values) {
        this(values);
        this._id = id;
    }

    public Marks(final ContentValues values) {
        name = values.getAsString(Columns.NAME);
        desc= values.getAsString(Columns.DESC);
        extra= values.getAsString(Columns.EXTRA);
        color = values.getAsLong(Columns.COLOR);
        updated = values.getAsLong(Columns.UPDATED);
    }

    @Override
    public ContentValues getContent() {
        final ContentValues values = new ContentValues();
        // Note that ID is NOT included here
        // TODO check null values
        values.put(Columns.NAME, name);
        values.put(Columns.DESC, (desc == null) ? "" : desc); // e.g.
        values.put(Columns.EXTRA, extra);
        values.put(Columns.COLOR, color);
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
     */
    public static void init(SQLiteDatabase database) {
        final String[] names = new String[] {
                "M",
                "P",
                "N",
                "R",
                "RR",
                "F",
                "CT",
                "FS" };
        final String[] descs = new String[] {
                "Mattina",
                "Pomeriggio",
                "Notte",
                "Riposo",
                "Riposo Retribuito",
                "Ferie",
                "Cambio Turno",
                "Fermata Straordinaria" };

        final ContentValues values=new ContentValues();
        final Calendar cal = GregorianCalendar.getInstance();

        for (int i=0; i< names.length; i++) {
            values.clear();
            values.put(Columns.NAME, names[i]);
            values.put(Columns.DESC, descs[i]);
            values.put(Columns.UPDATED, Calendar.getInstance().getTimeInMillis());
            database.insert(TABLE_NAME, null, values);
            Log.d(TAG, String.format("init() [name=%s]", names[i]));
        }
    }
}
