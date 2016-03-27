package fr.kocal.android.iut_mini_projet.contracts;

import android.provider.BaseColumns;

/**
 * Model de la table "earthquake"
 * https://developer.android.com/training/basics/data-storage/databases.html
 */
public class EarthquakeContract {
    public EarthquakeContract() {}

    public static abstract class EarthquakeEntry implements BaseColumns {
        public static final String TABLE_NAME = "earthquake";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_FAVORITE = "favorite";
    }
}
