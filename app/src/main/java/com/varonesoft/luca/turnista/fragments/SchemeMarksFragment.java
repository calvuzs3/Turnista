package com.varonesoft.luca.turnista.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.varonesoft.luca.turnista.R;
import com.varonesoft.luca.turnista.database.models.Scheme;
import com.varonesoft.luca.turnista.utils.Log;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SchemeMarksFragment extends Fragment {

    // TAG
    private static String TAG = "SchemeMarksFragment";

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";

    // TODO: Customize parameters
    private int mColumnCount = 1;

    // Listener to this implementation
    private OnListFragmentInteractionListener mListener;

    // Adapter
    private MySchemeMarksRecyclerViewAdapter mAdapter;

    // ID of the CursorLoader
    private static int SCHEMEMARKSFRAGMENT_LOADER_ID = 0x00001111;
    LoaderManager.LoaderCallbacks mCallback;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SchemeMarksFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SchemeMarksFragment newInstance(int columnCount) {
        SchemeMarksFragment fragment = new SchemeMarksFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schememarks_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            //registerForContextMenu(mRecyclerView);
            mRecyclerView.setAdapter(mAdapter);
            Log.d(TAG, "Instance of RecyclerView");
        } else {
            // Instance of ListView
            Context context = view.getContext();
            Log.d(TAG,"It's another instance of");
        }
        return view;
    }

/*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        // Inflate Menu from xml resource
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.context_menu_scheme_list, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Toast.makeText(getActivity(), " User selected something ", Toast.LENGTH_LONG).show();
        return false;
    }
*/


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }

        // Now the mListener is defined
        mAdapter = new MySchemeMarksRecyclerViewAdapter(getContext(), mListener);

        mCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
                Uri uri = Scheme.SELECTURI;
                return new CursorLoader(getContext(), uri, null, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
                Log.d(TAG, "onLoadFinished() called");
                mAdapter.swapCursor(c);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                Log.d(TAG, "onLoaderReset() called");
                mAdapter.swapCursor(null);
            }
        };
        refreshLoader();
    }

    public void refreshLoader() {
        getLoaderManager().restartLoader(SCHEMEMARKSFRAGMENT_LOADER_ID, null, mCallback);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(long id, String name);
    }
}
