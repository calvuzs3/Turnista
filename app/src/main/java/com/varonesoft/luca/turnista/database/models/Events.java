package com.varonesoft.luca.turnista.database.models;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.varonesoft.luca.turnista.database.ContentProvider;

/**
 * Created by luca on 13/04/16.
 */
public class Events extends DAO {

    // TAG
    private static String TAG = "DB DayEvents";

    // PATH
    public static final String PATH = "events";

    // DATABASE TABLE
    public static final String TABLE_NAME = ContentProvider.BASE_TABLE_NAME + PATH;

    // CONTENT TYPE
    public static final String CONTENT_TYPE = ContentProvider.BASE_CONTENT_TYPE + PATH;

    // BASE CODES
    public static final int BASEURICODE = 414001;
    public static final int BASEITEMCODE = 414011;

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
        public static final String DATE = "e_date";
        public static final String CREATOR_ID = "e_id_creator";
        public static final String MARK_ID = "e_id_mark";
        public static final String UPDATED = "e_updated";

        // Table Fields as Array
        public static final String[] FIELDS = {_ID, CREATOR_ID, MARK_ID, UPDATED};
    } // end-of-class

    // Database creation SQL statement
    public static final String DATABASE_CREATE = new StringBuilder("CREATE TABLE ")
            .append(TABLE_NAME).append("(")
            .append(Columns._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
            .append(Columns.DATE).append(" INTEGER NOT NULL, ")
            .append(Columns.CREATOR_ID).append(" INTEGER NOT NULL, ")
            .append(Columns.MARK_ID).append(" INTEGER NOT NULL, ")
            .append(Columns.UPDATED).append(" INTEGER NOT NULL);")
            .toString();

    // fields
    public Long date;
    public Long creator_id;
    public Long mark_id;
    public Long updated;

    // CONTRUCTOR
    public Events(final Cursor c) {
        this._id = c.getLong(0);
        this.date = c.getLong(1);
        this.creator_id = c.getLong(2);
        this.mark_id = c.getLong(3);
        this.updated = c.getLong(4);
    }

    public Events(final Uri uri, final ContentValues values) {
        this(Long.parseLong(uri.getLastPathSegment()), values);
    }

    public Events(final long id, final ContentValues values) {
        this(values);
        this._id = id;
    }

    public Events(final ContentValues values) {
        date = values.getAsLong(Columns.DATE);
        creator_id = values.getAsLong(Columns.CREATOR_ID);
        mark_id = values.getAsLong(Columns.MARK_ID);
        updated=values.getAsLong(Columns.UPDATED);
    }

    @Override
    public ContentValues getContent() {
        final ContentValues values = new ContentValues();
        // Note that ID is NOT included here
        values.put(Columns.DATE, date);
        values.put(Columns.CREATOR_ID, creator_id);
        values.put(Columns.MARK_ID, mark_id);
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
     * <p/>
     * Returns number of db-rows affected. Fail if < 1
     *
     * @param context
     */
    @Override
    public int save(Context context) {
        int result = 0;
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
     * Dont init() it
     */
    private static void init(SQLiteDatabase database) {
        // Empty
    }
}
