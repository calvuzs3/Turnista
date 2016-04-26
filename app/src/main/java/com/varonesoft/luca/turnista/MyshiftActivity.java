package com.varonesoft.luca.turnista;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.varonesoft.luca.turnista.database.models.Scheme;
import com.varonesoft.luca.turnista.database.models.shift.PerMonthShift;
import com.varonesoft.luca.turnista.utils.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MyshiftActivity extends AppCompatActivity {


    private PerMonthShift mPerMonthShift;
    private SchemeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myshift);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //mList = (ListView )findViewById(R.id.list);
        mPerMonthShift = PerMonthShift.getInstance();
        mAdapter = new SchemeAdapter(getApplicationContext(), R.layout.content_myshift_list_row,
                mPerMonthShift.getShifts(getApplicationContext()));

        ListView mList = (ListView )findViewById(R.id.list);
        assert mList != null;
        assert mAdapter != null;
        mList.setAdapter(mAdapter);
        refresh();

        Button b_left = (Button) findViewById(R.id.myshift_left);
        Button b_righ = (Button) findViewById(R.id.myshift_right);

        b_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sub a month
                mPerMonthShift.subMonth();
                refresh();
            }
        });

        b_righ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add a month
                mPerMonthShift.addMonth();
                refresh();
            }
        });
    }

    private void refresh() {
//        if (mAdapter.items == null)
//            mAdapter.items = new ArrayList<>();
//        else
//            mAdapter.items.clear();
        mAdapter.items = mPerMonthShift.getShifts(getApplicationContext());
        mAdapter.notifyDataSetChanged();
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(Calendar.YEAR, mPerMonthShift.mYear);
        cal.set(Calendar.MONTH, mPerMonthShift.mMonth-1);
        Locale locale = Locale.getDefault();

        TextView v = (TextView) findViewById(R.id.myshift_month);
        assert v != null;
        v.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, locale) + " - "
                + mPerMonthShift.mYear);
    }


    public class SchemeAdapter extends ArrayAdapter<Scheme> {

        // Members private
        private String TAG = "SchemeAdapter";
        protected ArrayList<Scheme> items;

        public SchemeAdapter(Context context, int textViewResourceId, ArrayList<Scheme> items) {
            super(context, textViewResourceId, items);
            this.items = items;
            Log.d(TAG, "Creator");
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.content_myshift_list_row, null);
            }
            Scheme o = items.get(position);
            if (o != null) {
                TextView tday = (TextView) v.findViewById(R.id.tday);
                TextView twday = (TextView) v.findViewById(R.id.twday);
                TextView t1 = (TextView) v.findViewById(R.id.tt1);

                Calendar cal = GregorianCalendar.getInstance();
                cal.setTimeInMillis(o.date);
                Locale locale = Locale.getDefault();

                if (tday != null) tday.setText("" + cal.get(Calendar.DAY_OF_MONTH));
                if (twday != null)
                    twday.setText("" + cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale));
                if (t1 != null) t1.setText("" + o.mark_name + " - " + o.mark_desc);

                // RED color for Sunday
                Resources resources = getContext().getResources();
                if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
                    assert twday != null;
                    twday.setTextColor(resources.getColor(R.color.colorAccent));
                } else {
                    assert twday != null;
                    twday.setTextColor(resources.getColor(R.color.colorPrimaryDark));
                }
            }

            return v;
        }

    } // Adapter

} // EOF