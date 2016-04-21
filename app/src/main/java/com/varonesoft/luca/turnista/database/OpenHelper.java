package com.varonesoft.luca.turnista.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.varonesoft.luca.turnista.database.models.Creator;
import com.varonesoft.luca.turnista.database.models.Events;
import com.varonesoft.luca.turnista.database.models.Marks;
import com.varonesoft.luca.turnista.database.models.Scheme;
import com.varonesoft.luca.turnista.utils.Log;

/**
 * Created by luca on 13/04/16.
 */
public class OpenHelper extends SQLiteOpenHelper {

    // TAG
    private static String TAG = "OpenHelper";

    // Self reference, it'a singleton
    private static OpenHelper db;

    // Never keep a reference to this, get a reference instead
    public static OpenHelper getInstance(final Context context) {
        if (db == null) {
            db= new OpenHelper(context);
        }
        return db;
    }

    // Database properties
    public static String DATABASE_NAME = "db_turnista.db";
    public static int DATABASE_VERSION = 1;

    // Private constructor
    private OpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
     * This method create the db if it doesn't exists
     *
     * it calls onCreate() eventually.
     */
    @Override
    public void onOpen(SQLiteDatabase database) {
        super.onOpen(database);
        if (!database.isReadOnly()){
            // Enable foreign key constraints
            // This requires android sdk >=16
            database.setForeignKeyConstraintsEnabled(true);
        }
    }

    /*
     * Method is called during creation of the database
     */
    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL("DROP TABLE IF EXISTS " + Events.TABLE_NAME);
        database.execSQL(Events.DATABASE_CREATE);
        database.execSQL("DROP TABLE IF EXISTS " + Creator.TABLE_NAME);
        database.execSQL(Creator.DATABASE_CREATE);
        database.execSQL("DROP TABLE IF EXISTS " + Marks.TABLE_NAME);
        database.execSQL(Marks.DATABASE_CREATE);
        database.execSQL("DROP TABLE IF EXISTS " + Scheme.TABLE_NAME);
        database.execSQL(Scheme.DATABASE_CREATE);

        init(database);
    }

    /*
     * Method is called during an upgrade of the database,
     *
     * e.g. if you increase the database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldV, int newV) {
        // Empty
        Log.d(TAG, "onUpgrade() Nothing to do here now.. You shouldn't see this !");
    }

    /*
     * Private init()
     *
     * Initialize the database with common values
     */
    private static void init(SQLiteDatabase database) {
        database.beginTransaction();
        try {
            Marks.init(database);
            Creator.init(database);
        } catch (Exception e){
            Log.d(TAG, e.getMessage());
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

}
