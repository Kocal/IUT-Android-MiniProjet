package fr.kocal.android.iut_mini_projet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import fr.kocal.android.iut_mini_projet.AlertLevel;
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
        View view = convertView;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.listview_row_earthquake, null);
            viewHolder = new EarthquakeViewHolder();
        } else {
            convertView.setTag(viewHolder);
            view = convertView;
        }

        viewHolder.mPlace = (TextView) view.findViewById(R.id.place);
        viewHolder.mDate = (TextView) view.findViewById(R.id.date);
        viewHolder.mMagnitude = (TextView) view.findViewById(R.id.magnitude);
        viewHolder.mFavorite = (ToggleButton) view.findViewById(R.id.buttonFavorite);

        Earthquake earthquake = getItem(position);

        // Formatage de la date
        Date date = new Date(earthquake.getTime());
        DateFormat dateFormat = DateFormat.getDateInstance();
        String dateString = dateFormat.format(date);

        // Formatage de la magnitude
        String magnitudeString = String.format(getContext().getString(R.string.magnitude), earthquake.getMagnitude());

        viewHolder.mPlace.setText(earthquake.getPlace());
        viewHolder.mDate.setText(dateString);
        viewHolder.mMagnitude.setText(magnitudeString);
        view.setBackgroundResource(earthquake.getAlertLevel().getColorId());
        viewHolder.mFavorite.setChecked(false);

        return view;
    }
}
