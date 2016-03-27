package fr.kocal.android.iut_mini_projet.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
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
import fr.kocal.android.iut_mini_projet.contracts.EarthquakeContract.EarthquakeEntry;
import fr.kocal.android.iut_mini_projet.viewHolders.EarthquakeViewHolder;

/**
 * Created by kocal on 15/03/16.
 */
public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    EarthquakeViewHolder viewHolder = null;

    SQLiteDatabase dbReadable;

    public EarthquakeAdapter(Context context, List<Earthquake> objects, SQLiteDatabase dbReadable) {
        super(context, 0, objects);
        this.dbReadable = dbReadable;
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

        if (earthquake != null) {
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

                    earthquake.setInFavorite(!inFavorite);
                    finalViewHolder.mFavorite.setChecked(!inFavorite);

                    ContentValues values = new ContentValues();
                    values.put(EarthquakeEntry.COLUMN_NAME_FAVORITE, (inFavorite ? 0 : 1));

                    dbReadable.update(EarthquakeEntry.TABLE_NAME,
                            values,
                            EarthquakeEntry.COLUMN_NAME_ID + " = ?",
                            new String[]{earthquake.getId()}
                    );
                }
            });
        }

        return row;
    }
}
