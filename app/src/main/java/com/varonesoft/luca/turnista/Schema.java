package com.varonesoft.luca.turnista;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.varonesoft.luca.turnista.database.OpenHelper;
import com.varonesoft.luca.turnista.database.models.Creator;
import com.varonesoft.luca.turnista.database.models.Marks;
import com.varonesoft.luca.turnista.database.models.Scheme;
import com.varonesoft.luca.turnista.fragments.SchemeMarksFragment;
import com.varonesoft.luca.turnista.utils.DateTimeFormatter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Schema extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
    SchemeMarksFragment.OnListFragmentInteractionListener {

    // IDs of the CursorLoaders
    private int CREATOR_LOADER_ID = 0x01000000;
    private int SCHEME_LOADER_ID  = 0x10000000;

    // private Members
    private Creator mCreator;
    private long mMarkId = -1;

    // private Fields
    private EditText mTextViewDate;
    private ImageButton mButtonChooseDate;
    private Spinner mSpinner;
    private ImageButton mButtonAddFromSpinner;
    private SimpleCursorAdapter mSpinnerAdapter;
    private Calendar mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schema);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            // TODO gestire l'aggiunta di items
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get references to the Views
        mTextViewDate = (EditText )findViewById(R.id.schema_edit_startdate);
        mButtonChooseDate = (ImageButton )findViewById(R.id.schema_button_choosedate);
        mSpinner = (Spinner) findViewById(R.id.schema_spinner);
        mButtonAddFromSpinner = (ImageButton )findViewById(R.id.schema_button_add);

        // First get a Creator
        getCreator(this);

mButtonChooseDate.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        // Set CalendarDateTime Picker
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                Schema.this, 2016, 0, 1
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");

    }
});

        // Lets try this out

        // get a cursor from the database with an "_id" field
        // it's a work around for the time beeing
        // we should close the cursor
        Cursor c = getContentResolver().query(Marks.URI, null, null, null, null);

        // make an adapter from the cursor
        String[] from = new String[]{Marks.Columns.NAME};
        int[] to = new int[]{android.R.id.text1};
        mSpinnerAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_dropdown_item, c, from, to);

        // set layout for activated adapter
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // set adapter
        mSpinner.setAdapter(mSpinnerAdapter);

        // set spinner listener to display the selected item id
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO make the button (add) enabled
                // we need a creator id
                // and store the value into the db
                mMarkId = id;

                Toast.makeText(getApplicationContext(), "Selected [ID=" + id + "] added", Toast.LENGTH_LONG).show();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // TODO make the button (ADD) disabled
            }
        });

        mButtonAddFromSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((mMarkId<1) || (mDate == null)) return;
                ContentValues cv = new ContentValues();
                cv.put(Scheme.Columns.DATE, mDate.getTimeInMillis());
                cv.put(Scheme.Columns.CREATOR_ID, mCreator._id);
                cv.put(Scheme.Columns.MARK_ID, mMarkId);
                Scheme s = new Scheme(cv);
                s.save(getApplicationContext());

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schema, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            /*
             * Add an item to the schema table
             */
            // TODO genera turni
            return true;
        }
        if (id == R.id.action_delete_all) {
            /*
             * Destroy the list in the scema table
             */
            // TODO Dialog warning
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /*
     * Crea un Cursore per recuperare info per il Creatore
     */
    private void getCreator(Activity a) {
        getLoaderManager().restartLoader(CREATOR_LOADER_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {

            @Override
            public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
                return new CursorLoader( Schema.this, Creator.URI, null, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> arg0, Cursor c) {
                if (c!=null) {
                    if (c.moveToFirst()) {
                        mCreator = new Creator(c);
                        fillViews();
                    }
                } else {
                    // NOData
                }
                // Don't need it anymore
                getLoaderManager().destroyLoader(CREATOR_LOADER_ID);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> arg0) {
            }
        });
    }

    /*
     * Riempie le Views
     */
    private void fillViews() {
        // Set the date
        if (mCreator.name!= null && mCreator.start_date!=null)
            mTextViewDate.setText(DateTimeFormatter.getDate(mCreator.start_date));
    }

    /**
     * @param view        The view associated with this listener.
     * @param year        The year that was set.
     * @param monthOfYear The month that was set (0-11) for compatibility
     *                    with {@link Calendar}.
     * @param dayOfMonth  The day of the month that was set.
     */
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mDate = GregorianCalendar.getInstance();
        mDate.set(Calendar.YEAR, year);
        mDate.set(Calendar.MONTH, monthOfYear);
        mDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        this.mCreator.start_date=mDate.getTimeInMillis();
        fillViews();
    }

    /*
     * Implementation of the interface
     *
     * this way we comunicate with the list fragment
     */
    @Override
    public void onListFragmentInteraction(long id, String name) {
        // No methods till now
        Snackbar.make(null, "id="+id+" name="+name, Snackbar.LENGTH_SHORT)
                .setAction("ACTION", null)
                .show();
        return;
    }
}
