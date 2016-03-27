package fr.kocal.android.iut_mini_projet.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fr.kocal.android.iut_mini_projet.contracts.EarthquakeContract.EarthquakeEntry;

/**
 * Helper pour gérer la BDD Earthquake
 * https://developer.android.com/training/basics/data-storage/databases.html
 */
public class EarthquakeDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Earthquake.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + EarthquakeEntry.TABLE_NAME + " (" +
                    EarthquakeEntry._ID + " INTEGER PRIMARY KEY," +
                    EarthquakeEntry.COLUMN_NAME_ID + TEXT_TYPE + COMMA_SEP +
                    EarthquakeEntry.COLUMN_NAME_FAVORITE + INTEGER_TYPE + " DEFAULT 0" +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + EarthquakeEntry.TABLE_NAME;

    public EarthquakeDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
