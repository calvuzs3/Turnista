package com.varonesoft.luca.turnista.utils;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.luca.calendarioturni.database.Events;
import com.varonesoft.luca.turnista.MainActivity;
import com.varonesoft.luca.turnista.R;

import org.joda.time.LocalDate;

@SuppressWarnings("ALL")
public class CelebrationDaysList extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;


    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    //private ArrayList<Mark> mItems;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private CelebrationDaysCursorAdapter mAdapter = null;
    private ListView mList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebration_days_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.celebrationdays_list_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.celebrationdays_list_fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mList = (ListView) findViewById(R.id.celebrationdays_list);
        assert mList != null;
        registerForContextMenu(mList);

        mAdapter = (new CelebrationDaysCursorAdapter(this,
                R.layout.content_celebration_days_list_row, null,
                new String[]{Events.Columns.NAME, Events.Columns.START_DAY},
                new int[]{R.id.celebrationdays_list_name, R.id.celebrationdays_list_date}, 0));

        mList.setAdapter(mAdapter);

        /*if (findViewById(R.id.celebrationdays_list_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }*/

        // Load the content
        getLoaderManager().initLoader(0, null, this);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mTwoPane) {
                    // In two-pane mode, show the detail view in this activity by
                    // adding or replacing the detail fragment using a
                    // fragment transaction.
/*
                    Bundle arguments = new Bundle();
                    arguments.putLong(Fragment_MarksDetail.ARG_ITEM_ID, id);
                    Fragment_MarksDetail fragment = new Fragment_MarksDetail();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.shiftsmark_detail_container, fragment).commit();
*/

                } else {
                    // In single-pane mode, simply start the detail activity
                    // for the selected item ID.
/*                    Intent detailIntent = new Intent(Activity_MarksList.this, Activity_MarksDetail.class);
                    detailIntent.putExtra(Fragment_MarksDetail.ARG_ITEM_ID, id);
                    startActivity(detailIntent);
*/
                }
            }
        });

// Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState
                    .getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_celebration_days_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, MainActivity.class));
            return true;
        }
/*
        if (R.id.insert == item.getItemId()) {
//            // Create a new person.
//            Mark m = new Mark();
//            Uri uri = getContentResolver().insert(Mark.URI, m.getContentValues());
//            assert uri != null;
//            m.id= Long.parseLong(uri.getLastPathSegment());
//            // Open a new fragment with the new id
//            onItemSelected(m.id);
            Intent i = new Intent(Activity_MarksList.this, Activity_MarksDetail.class);
            startActivity(i);
            return true;
        }

*/
        return super.onOptionsItemSelected(item);
    }

    /**
     * If in a fragment this would be a
     * Callback method from {@link } indicating that
     * the item with the given ID was selected.
     */
    //@Override
    public void onItemSelected(long id) {
/*
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(Fragment_MarksDetail.ARG_ITEM_ID, id);
            Fragment_MarksDetail fragment = new Fragment_MarksDetail();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.shiftsmark_detail_container, fragment).commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, Activity_MarksDetail.class);
            detailIntent.putExtra(Fragment_MarksDetail.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
*/
    }
/*
        if (R.id.insert == item.getItemId()) {
//            // Create a new person.
//            Mark m = new Mark();
//            Uri uri = getContentResolver().insert(Mark.URI, m.getContentValues());
//            assert uri != null;
//            m.id= Long.parseLong(uri.getLastPathSegment());
//            // Open a new fragment with the new id
//            onItemSelected(m.id);
            Intent i = new Intent(Activity_MarksList.this, Activity_MarksDetail.class);
            startActivity(i);
            return true;
        }

*/

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.word_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                        .getMenuInfo();
                Uri uri = Uri.parse(Events.URI + "/"
                        + info.id);

                // The loader is already notified of the changes
                getContentResolver().delete(uri, null, null);

                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        mList.setChoiceMode(
                activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
                        : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            mList.setItemChecked(mActivatedPosition, false);
        } else {
            mList.setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                Events.URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        mAdapter.swapCursor(c);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        mAdapter.swapCursor(null);
    }

    private class CelebrationDaysCursorAdapter extends SimpleCursorAdapter {

        /**
         * Constructor the enables auto-requery.
         *
         * @param context
         * @param layout
         * @param c
         * @param from
         * @param to
         * @deprecated This option is discouraged, as it results in Cursor queries
         * being performed on the application's UI thread and thus can cause poor
         * responsiveness or even Application Not Responding errors.  As an alternative,
         * use {@link LoaderManager} with a {@link CursorLoader}.
         */
        public CelebrationDaysCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int idontcare) {
            super(context, layout, c, from, to, idontcare);
        }

        // The newView method is used to inflate a new view and return it,
        // you don't bind any data to the view at this point.
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.content_celebration_days_list_row, parent, false);
        }

        // The bindView method is used to bind all data to a given view
        // such as setting the text on a TextView.
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Find fields to populate in inflated template
            TextView name= (TextView) view.findViewById(R.id.celebrationdays_list_name);
            TextView date= (TextView) view.findViewById(R.id.celebrationdays_list_date);
            // Extract properties from cursor
            String name_string = cursor.getString(cursor.getColumnIndexOrThrow(Events.Columns.NAME));
            long date_long= cursor.getInt(cursor.getColumnIndexOrThrow(Events.Columns.START_DAY));
            LocalDate ld = new LocalDate(date_long);
            // Populate fields with extracted properties
            name.setText(name_string);
            date.setText(ld.toString("dd/MM/yyyy"));
        }
    }
}
