package fr.kocal.android.iut_mini_projet.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import fr.kocal.android.iut_mini_projet.Earthquake;
import fr.kocal.android.iut_mini_projet.R;
import fr.kocal.android.iut_mini_projet.contracts.EarthquakeContract.EarthquakeEntry;
import fr.kocal.android.iut_mini_projet.viewHolders.EarthquakeViewHolder;

/**
 * Created by kocal on 15/03/16.
 */
public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public static final CharSequence DISPLAY_ALL = "all";
    public static final CharSequence DISPLAY_ONLY_FAVORITE = "only_favorite";

    EarthquakeViewHolder viewHolder = null;

    // Sauvegarde interne des earthquakes
    private ArrayList<Earthquake> _earthquakes;

    ArrayList<Earthquake> earthquakes;
    SQLiteDatabase dbReadable;

    public EarthquakeAdapter(Context context, ArrayList<Earthquake> objects, SQLiteDatabase dbReadable) {
        super(context, 0, objects);
        this._earthquakes = (ArrayList<Earthquake>) objects.clone();
        this.earthquakes = objects;
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
            viewHolder.mAlertLevel.getDrawable().setColorFilter(ContextCompat.getColor(getContext(), earthquake.getAlertLevel().getColorId()), PorterDuff.Mode.MULTIPLY);
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

    public Filter getFavoriteFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                ArrayList<Earthquake> tmp = new ArrayList<>();
                Earthquake earthquake;

                if (constraint != null && _earthquakes != null) {
                    for (int i = 0; i < _earthquakes.size(); i++) {
                        earthquake = _earthquakes.get(i);

                        if (constraint == DISPLAY_ONLY_FAVORITE) {
                            if (earthquake.isInFavorite()) {
                                tmp.add(earthquake);
                            }
                        } else if (constraint == DISPLAY_ALL) {
                            tmp.add(earthquake);
                        }
                    }
                }

                filterResults.values = tmp;
                filterResults.count = tmp.size();

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                earthquakes.clear();
                earthquakes.addAll((Collection<? extends Earthquake>) results.values);

                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    public void setNewEarthquakes(final ArrayList<Earthquake> eq) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                _earthquakes.clear();
                _earthquakes.addAll((Collection<? extends Earthquake>) eq.clone());
                earthquakes.clear();
                earthquakes.addAll(eq);
                notifyDataSetChanged();
            }
        }).run();
    }
}
