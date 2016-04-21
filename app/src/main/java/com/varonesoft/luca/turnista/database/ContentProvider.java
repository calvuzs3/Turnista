package com.varonesoft.luca.turnista.database;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.varonesoft.luca.turnista.database.models.Creator;
import com.varonesoft.luca.turnista.database.models.DAO;
import com.varonesoft.luca.turnista.database.models.Events;
import com.varonesoft.luca.turnista.database.models.Marks;
import com.varonesoft.luca.turnista.database.models.Scheme;

import java.util.ArrayList;

/**
 * Created by luca on 13/04/16.
 */
public class ContentProvider extends android.content.ContentProvider {

    // TAG
    private static String TAG = "ContentProvider";

    // MUST
    public static final String AUTHORITY = "com.varonesoft.luca.turnista.contentprovider";
    public static final String SCHEME = "content://";
    public static final String BASE_CONTENT_TYPE = "vnd.android.cursor.item/vnd.calendarioturni.";
    public static final String BASE_TABLE_NAME = "tbl_";

    // UriMatcher
    private static final UriMatcher mURIMatcher;

    // Static declarations
    // add Uris of the datamodels
    static {
        mURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        Marks.addMatcherUris(mURIMatcher);
        Events.addMatcherUris(mURIMatcher);
        Creator.addMatcherUris(mURIMatcher);
        Scheme.addMatcherUris(mURIMatcher);
        //Celebrations.addMatcherUris(mURIMatcher);
    }

    // EMPTY CONSTRUCTOR (prevents the instance of this class)
    public ContentProvider() {
    }

    @Override
    public String getType(Uri uri) {
        switch (mURIMatcher.match(uri)) {
            case Marks.BASEITEMCODE:
            case Marks.BASEURICODE:
                return Marks.CONTENT_TYPE;

            case Creator.BASEITEMCODE:
            case Creator.BASEURICODE:
                return Creator.CONTENT_TYPE;

            case Scheme.BASEITEMCODE:
            case Scheme.BASEURICODE:
                return Scheme.CONTENT_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
                //return null;
        }
    }


    /*
     * onCreate()
     */
    @Override
    public boolean onCreate() {
        return true;
    }



    /*
     * Create
     *
     * Marks, Creator, Scheme
     */
    @Override
    public synchronized Uri insert(Uri uri, ContentValues values) {

        Uri result = null;

        SQLiteDatabase sqlDB = OpenHelper.getInstance(getContext()).getWritableDatabase();

        sqlDB.beginTransaction();
        try {
            final DAO item;

            switch (mURIMatcher.match(uri)) {
                case Marks.BASEURICODE:
                    item = new Marks(values);
                    break;
                case Creator.BASEURICODE:
                    item = new Creator(values);
                    break;
                case Scheme.BASEURICODE:
                    item = new Scheme(values);
                    break;

                default:
                    throw new IllegalArgumentException("Insertion failed. URI: " + uri);
            }

            result = item.insert(getContext(), sqlDB);
            sqlDB.setTransactionSuccessful();

        } catch (IllegalArgumentException e) {
            Log.d(TAG, e.getMessage());
        } finally {
            sqlDB.endTransaction();
        }

        if (result != null) {
            DAO.notifyProviderOnChange(getContext(), uri);
        }
        return result;
    }

    /*
     * Wrapper for deleting items
     */
    synchronized
    private int safeDeleteItem(final SQLiteDatabase db,
                               final String tableName, final Uri uri, final String selection,
                               final String[] selectionArgs) {
        db.beginTransaction();
        int result = 0;
        try {
            result += db.delete(
                    tableName,
                    DAO.whereIdIs(selection),
                    DAO.joinArrays(selectionArgs,
                            new String[]{uri.getLastPathSegment()}));
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return result;
    }

    /*
     * Delete
     *
     * Marks, Creator, Scheme
     */
    @Override
    synchronized public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqlDB = OpenHelper.getInstance(getContext()).getWritableDatabase();
        int result = 0;
        String id;

        switch (mURIMatcher.match(uri)) {
            case Marks.BASEITEMCODE:
                result += safeDeleteItem(sqlDB, Marks.TABLE_NAME, uri, selection, selectionArgs);
                break;
            case Marks.BASEURICODE:
                result += sqlDB.delete(Marks.TABLE_NAME, selection, selectionArgs);
                break;

            case Creator.BASEITEMCODE:
                result += safeDeleteItem(sqlDB, Creator.TABLE_NAME, uri, selection, selectionArgs);
                break;
            case Creator.BASEURICODE:
                result += sqlDB.delete(Creator.TABLE_NAME, selection, selectionArgs);
                break;

            case Scheme.BASEITEMCODE:
                result += safeDeleteItem(sqlDB, Scheme.TABLE_NAME, uri, selection, selectionArgs);
                break;
            case Scheme.BASEURICODE:
                result += sqlDB.delete(Scheme.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        if (result > 0) {
            DAO.notifyProviderOnChange(getContext(), uri);
        }
        return result;
    }

    /*
     * Query
     *
     * Marks, Creator, Scheme
     */
    @Override
    synchronized public Cursor query(Uri uri, String[] projection, String selection,
                                     String[] selectionArgs, String sortOrder) {
        // We could use queryBuilder
        SQLiteDatabase sqlDB = OpenHelper.getInstance(getContext()).getReadableDatabase();

        Cursor result = null;
        final long id;

        // check if the caller has requested a column which does not exists
        //checkColumns(uri, projection);
        switch (mURIMatcher.match(uri)) {
            case Marks.BASEURICODE:
                result = sqlDB.query(Marks.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                result.setNotificationUri(getContext().getContentResolver(),
                        Marks.URI);
                break;
            case Marks.BASEITEMCODE:
                id = Long.parseLong(uri.getLastPathSegment());
                result = sqlDB.query(Marks.TABLE_NAME, projection,
                        Marks.whereIdIs(selection),
                        Marks.joinArrays(selectionArgs, new String[]{String.valueOf(id)}),
                        null, null, sortOrder);
                result.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case Creator.BASEURICODE:
                result = sqlDB.query(Creator.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                result.setNotificationUri(getContext().getContentResolver(),
                        Creator.URI);
                break;
            case Creator.BASEITEMCODE:
                id = Long.parseLong(uri.getLastPathSegment());
                result = sqlDB.query(Creator.TABLE_NAME, projection,
                        Creator.whereIdIs(selection),
                        Creator.joinArrays(selectionArgs, new String[]{String.valueOf(id)}),
                        null, null, sortOrder);
                result.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case Scheme.SELECTURICODE:
                result = sqlDB.rawQuery(Scheme.SELECT_STATEMENT, selectionArgs);
                result.setNotificationUri(getContext().getContentResolver(), Scheme.URI);
                break;
            case Scheme.BASEURICODE:
                result = sqlDB.query(Scheme.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                result.setNotificationUri(getContext().getContentResolver(), Scheme.URI);
                break;
            case Scheme.BASEITEMCODE:
                id = Long.parseLong(uri.getLastPathSegment());
                result = sqlDB.query(Scheme.TABLE_NAME, projection,
                        Creator.whereIdIs(selection),
                        Creator.joinArrays(selectionArgs, new String[]{String.valueOf(id)}),
                        null, null, sortOrder);
                result.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        return result;
    }

    /*
     * Update
     *
     * Marks, Creator, Scheme
     */
    @Override
    synchronized public int update(Uri uri, ContentValues values, String selection,
                                   String[] selectionArgs) {
        //
        final SQLiteDatabase sqlDB = OpenHelper.getInstance(getContext()).getWritableDatabase();
        final Long id;
        final ArrayList<Uri> updateUris = new ArrayList<Uri>();
        int result = 0;

        sqlDB.beginTransaction();
        try {
            switch (mURIMatcher.match(uri)) {
                case Marks.BASEURICODE:
                    updateUris.add(Marks.URI);
                    result = sqlDB.update(Marks.TABLE_NAME,
                            values, selection, selectionArgs);
                    break;
                case Marks.BASEITEMCODE:
                    updateUris.add(Marks.URI);
                    id = Long.parseLong(uri.getLastPathSegment());
                    result = sqlDB.update(Marks.TABLE_NAME,
                            values, Marks.whereIdIs(selection),
                            Marks.whereIdArg(id, selectionArgs));
                    break;

                case Creator.BASEURICODE:
                    updateUris.add(Creator.URI);
                    result = sqlDB.update(Creator.TABLE_NAME,
                            values, selection, selectionArgs);
                    break;
                case Creator.BASEITEMCODE:
                    updateUris.add(Creator.URI);
                    id = Long.parseLong(uri.getLastPathSegment());
                    result = sqlDB.update(Creator.TABLE_NAME,
                            values, Creator.whereIdIs(selection),
                            Creator.whereIdArg(id, selectionArgs));
                    break;

                case Scheme.BASEURICODE:
                    updateUris.add(Scheme.URI);
                    result = sqlDB.update(Scheme.TABLE_NAME,
                            values, selection, selectionArgs);
                    break;
                case Scheme.BASEITEMCODE:
                    updateUris.add(Scheme.URI);
                    id = Long.parseLong(uri.getLastPathSegment());
                    result = sqlDB.update(Scheme.TABLE_NAME,
                            values, Scheme.whereIdIs(selection),
                            Scheme.whereIdArg(id, selectionArgs));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri.toString());
            }
            if (result >= 0)
                sqlDB.setTransactionSuccessful();
        } finally {
            sqlDB.endTransaction();
        }
        if (result > 0)
            for (Uri u : updateUris) {
                DAO.notifyProviderOnChange(getContext(), u);
            }
        return result;
    }

    /*
     * Destroy the database
     *
     * just in case we need to recreate it from scratch
     */
    public void destroyDatabase() {
        OpenHelper sqlDB = OpenHelper.getInstance(getContext());
        SQLiteDatabase db = OpenHelper.getInstance(getContext()).getWritableDatabase();
        sqlDB.onCreate(db);
        Log.d(TAG, "Database destroyed, recreated.");
    }
} // End of Provider
