package com.varonesoft.luca.turnista.fragments;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.varonesoft.luca.turnista.R;
import com.varonesoft.luca.turnista.database.models.Marks;
import com.varonesoft.luca.turnista.database.models.Scheme;
import com.varonesoft.luca.turnista.fragments.SchemeMarksFragment.OnListFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Scheme} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 *
 * TODO implement long clicks to delete items
 * TODO implement long clicks to move up or down
 */
public class MySchemeMarksRecyclerViewAdapter extends RecyclerView.Adapter<MySchemeMarksRecyclerViewAdapter.ViewHolder> {

    // The listener, the activity to attach to
    private final OnListFragmentInteractionListener mListener;

    // Holds the cursor
    private Cursor mCursor;

    // Holds the Context
    final Context mContext;

    /*
     * Contructor
     */
    public MySchemeMarksRecyclerViewAdapter(Context c, OnListFragmentInteractionListener listener) {
        mContext = c;
        mListener = listener;
    }

    /*
     * Loads the layout with the recyclerview's row
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_schememarks, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        holder.onBind(mCursor);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(
                            holder.mScheme._id, holder.mScheme.mark_name);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        } else {
            return mCursor.getCount();
        }
    }
    public void swapCursor(final Cursor cursor) {
        this.mCursor = cursor;
        notifyDataSetChanged();
    }

    /*
     * ViewHolder
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        //public long id = -1;
        public Scheme mScheme;

        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        void onBind(final Cursor cursor) {
            mScheme = new Scheme(cursor);
            //id = mScheme._id;
            mIdView.setText(mScheme.mark_name);
            mContentView.setText(mScheme.mark_desc);
        }

        @Override
        public String toString() {
            return super.toString() + " '"
                    + mIdView.getText() + " - "
                    + mContentView.getText() + "'";
        }
    }
}
