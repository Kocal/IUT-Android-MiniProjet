package fr.kocal.android.iut_mini_projet.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import fr.kocal.android.iut_mini_projet.Earthquake;
import fr.kocal.android.iut_mini_projet.R;
import fr.kocal.android.iut_mini_projet.viewHolders.EarthquakeViewHolder;

/**
 * Created by kocal on 15/03/16.
 */
public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    EarthquakeViewHolder viewHolder = null;

    public EarthquakeAdapter(Context context, List<Earthquake> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        EarthquakeViewHolder viewHolder = new EarthquakeViewHolder();

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = inflater.inflate(R.layout.listview_row_earthquake, parent, false);

            viewHolder.mPlace = (TextView) row.findViewById(R.id.place);
            viewHolder.mDate = (TextView) row.findViewById(R.id.date);
            viewHolder.mMagnitude = (TextView) row.findViewById(R.id.magnitude);
            viewHolder.mAlertLevel = (ImageView) row.findViewById(R.id.alertLevel);
            viewHolder.mFavorite = (ToggleButton) row.findViewById(R.id.buttonFavorite);
            row.setTag(viewHolder);
        } else {
            viewHolder = (EarthquakeViewHolder) row.getTag();
        }

        final Earthquake earthquake = getItem(position);
        final EarthquakeViewHolder finalViewHolder = viewHolder;

        if(earthquake != null) {
            // Formatage de la date
            Date date = new Date(earthquake.getTime());
            DateFormat dateFormat = DateFormat.getDateInstance();
            String dateString = dateFormat.format(date);

            // Formatage de la magnitude
            String magnitudeString = String.format(getContext().getString(R.string.magnitude), earthquake.getMagnitude());

            viewHolder.mPlace.setText(earthquake.getPlace());
            viewHolder.mDate.setText(dateString);
            viewHolder.mMagnitude.setText(magnitudeString);
            viewHolder.mAlertLevel.getDrawable().setColorFilter(getContext().getColor(earthquake.getAlertLevel().getColorId()), PorterDuff.Mode.MULTIPLY);
            viewHolder.mFavorite.setChecked(earthquake.isInFavorite());
            viewHolder.mFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean inFavorite = earthquake.isInFavorite();
                    finalViewHolder.mFavorite.setChecked(!inFavorite);
                    earthquake.setInFavorite(!inFavorite);
                    Log.v("Kocal", "EQ : " + earthquake.getPlace());
                }
            });
        }

        return row;
    }
}
