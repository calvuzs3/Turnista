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

/**
 * Created by luca on 13/04/16.
 */
public class Scheme extends DAO {

    // TAG
    private static String TAG = "DB Scheme";

    // Path
    public static final String PATH = "scheme";

    // DATABASE TABLE
    public static final String TABLE_NAME = ContentProvider.BASE_TABLE_NAME + PATH;

    // CONTENT TYPE
    public static final String CONTENT_TYPE = ContentProvider.BASE_CONTENT_TYPE + PATH;

    // BASE CODES
    public static final int BASEURICODE = 414001;
    public static final int BASEITEMCODE = 414011;
    public static final int SELECTURICODE = 414021;

    // Uri
    public static final Uri URI = Uri.withAppendedPath(
            Uri.parse(ContentProvider.SCHEME + ContentProvider.AUTHORITY), PATH);
    public static final Uri SELECTURI = Uri.withAppendedPath(
            Uri.parse(ContentProvider.SCHEME + ContentProvider.AUTHORITY), PATH + "marks");

    // Add to the matcherUris thi codes
    public static void addMatcherUris(UriMatcher sURIMatcher) {
        sURIMatcher.addURI(ContentProvider.AUTHORITY, PATH, BASEURICODE);
        sURIMatcher.addURI(ContentProvider.AUTHORITY, PATH + "/#", BASEITEMCODE);
        sURIMatcher.addURI(ContentProvider.AUTHORITY, PATH + "marks", SELECTURICODE);
    }

    // Inner Class Columns
    public static class Columns implements BaseColumns {

        // This Class cannot be instantiated
        private Columns() {
        }

        // Table FIELDS
        public static final String DATE = "s_date";
        public static final String CREATOR_ID = "s_id_creator";
        public static final String MARK_ID = "s_id_mark";

        // Table Fields as Array
        public static final String[] FIELDS = {_ID, DATE, CREATOR_ID, MARK_ID};

        // Table Fields as Array
        public static final String[] JOIN_FIELDS = {Marks.Columns.NAME, Marks.Columns.DESC};
    } // end-of-class

    // Database creation SQL statement
    public static final String DATABASE_CREATE = new StringBuilder("CREATE TABLE ")
            .append(TABLE_NAME).append("(")
            .append(Columns._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
            .append(Columns.DATE).append(" INTEGER NOT NULL, ")
            .append(Columns.CREATOR_ID).append(" INTEGER NOT NULL, ")
            .append(Columns.MARK_ID).append(" INTEGER NOT NULL, ")
            .append("FOREIGN KEY (").append(Columns.CREATOR_ID).append(") REFERENCES ")
            .append(Creator.TABLE_NAME).append(" (")
            .append(Creator.Columns._ID).append (") ON DELETE CASCADE, ")
            .append("FOREIGN KEY (").append(Columns.MARK_ID).append(") REFERENCES ")
            .append(Marks.TABLE_NAME).append(" (")
            .append(Marks.Columns._ID).append(") ON DELETE CASCADE);")
            .toString();

    public static final String SELECT_STATEMENT = new StringBuilder("SELECT ")
            .append(arrayToCommaString(joinArrays(
                    prefixDottedArray(TABLE_NAME, Columns.FIELDS),
                    prefixDottedArray(Marks.TABLE_NAME, Columns.JOIN_FIELDS))))
            .append(" FROM ")
            .append(TABLE_NAME)
            .append(" JOIN ")
            .append(Marks.TABLE_NAME)
            .append(" ON (")
            .append(TABLE_NAME).append(".").append(Columns.MARK_ID).append("=")
            .append(Marks.TABLE_NAME).append(".").append(Marks.Columns._ID)
            .append(");")
            .toString();

    // fields
    public Long date;
    public Long creator_id;
    public Long mark_id;
    public String mark_name;
    public String mark_desc;

    // CONTRUCTOR
    public Scheme(final Cursor c) {
        this._id = c.getLong(0);
        this.date = c.getLong(1);
        this.creator_id = c.getLong(2);
        this.mark_id = c.getLong(3);
        this.mark_name = c.getString(4);
        this.mark_desc = c.isNull(5)?null:c.getString(5);
    }

    public Scheme(final Uri uri, final ContentValues values) {
        this(Long.parseLong(uri.getLastPathSegment()), values);
    }

    public Scheme(final long id, final ContentValues values) {
        this(values);
        this._id = id;
    }

    public Scheme(final ContentValues values) {
        date = values.getAsLong(Columns.DATE);
        creator_id = values.getAsLong(Columns.CREATOR_ID);
        mark_id = values.getAsLong(Columns.MARK_ID);
        //mark_name = values.getAsString(Marks.Columns.NAME);
        //mark_desc = values.getAsString(Marks.Columns.DESC);
        Log.d(TAG, String.format("cv: date=%s c=%s m=%s", date, creator_id, mark_id));
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

    /*
     * Returns the path
     */
    @Override
    protected String getPath() {
        return PATH;
    }

    /*
     * Returns the content type
     */
    @Override
    public String getContentType() {
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
     * Dont init() it because it assumes values are inserted into the database
     */
    private static void init(SQLiteDatabase database) {
        // Empty
    }
}
