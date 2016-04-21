package com.example.luca.calendarioturni.utilities.datamodels;

/**
 * Created by luca on 13/04/16.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.luca.calendarioturni.R;
import com.example.luca.calendarioturni.utilities.CalendarUtilities;

/**
 * Represents a separator in the ListView
 *
 * @author brianreber
 */
public class Separator extends ListItem {

    /**
     * Creates a new Separator for the ListView with the given title
     *
     * @param title
     */
    public Separator(String title) {
        this.title = title;
    }

    /* (non-Javadoc)
     * @see org.reber.agenda.ListItem#getType()
     */
    @Override
    public ItemType getType() {
        return ItemType.SEPARATOR;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(ListItem another) {
        return 0;
    }

    /* (non-Javadoc)
     * @see org.reber.agenda.ListItem#getLayout(android.content.Context, android.view.ViewGroup)
     */
    @Override
    public LinearLayout getLayout(Context ctx, ViewGroup parent, CalendarUtilities util) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(ctx).inflate(R.layout.separator, parent, false);

        TextView label  = (TextView) v.findViewById(R.id.label);
        label.setText(getTitle());

        return v;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return title;
    }

}
