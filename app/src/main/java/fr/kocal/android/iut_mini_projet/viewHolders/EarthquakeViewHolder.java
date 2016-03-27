package fr.kocal.android.iut_mini_projet.viewHolders;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * ViewHolder des Earthquakes pour l'EarthquakeAdapter
 */
public class EarthquakeViewHolder {
    public TextView mPlace;
    public TextView mDate;
    public TextView mMagnitude;
    public ImageView mAlertLevel;
    public ToggleButton mFavorite;
}
