package fr.kocal.android.iut_mini_projet.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.kocal.android.iut_mini_projet.AlertLevel;
import fr.kocal.android.iut_mini_projet.Earthquake;
import fr.kocal.android.iut_mini_projet.R;
import fr.kocal.android.iut_mini_projet.adapters.EarthquakeAdapter;

public class MainActivity extends AppCompatActivity {

    JSONObject json;

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();

        // Inutile puisqu'on gère ça dans le splashscreen, mais c'est au cas où :^)
        if (!fetchJson()) {
            Toast.makeText(MainActivity.this, "Impossible de récupérer le JSON", Toast.LENGTH_SHORT).show();
            return;
        }

        updateToolbarTitle();
        initListView();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void updateToolbarTitle() {
        try {
            // Soit on affiche le titre du JSON
            getSupportActionBar().setTitle(json.getJSONObject("metadata").getString("title"));
        } catch (JSONException e) {
            // Soit le titre par défaut s'il y a eu une erreur
            getSupportActionBar().setTitle(getString(R.string.titleDisplayingEarthquakes));
            e.printStackTrace();
        }
    }

    private boolean fetchJson() {
        try {
            json = new JSONObject(getIntent().getStringExtra("JSON"));
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void initListView() {
        mListView = (ListView) findViewById(R.id.listView);
        ArrayList<Earthquake> earthquakes = extractEarthquakesFromJson();
        EarthquakeAdapter earthquakeAdapter = new EarthquakeAdapter(MainActivity.this, earthquakes);

        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(earthquakeAdapter);
    }

    private ArrayList<Earthquake> extractEarthquakesFromJson() {
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        try {
            JSONArray features = json.getJSONArray("features");

            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = features.getJSONObject(i);
                JSONObject properties = feature.getJSONObject("properties");
                JSONObject geometry = feature.getJSONObject("geometry");

                // On récupère les coordonnées
                JSONArray jsonArrayCoordinates = geometry.getJSONArray("coordinates");
                Double[] coordinates = new Double[]{
                        jsonArrayCoordinates.getDouble(0),
                        jsonArrayCoordinates.getDouble(1),
                        jsonArrayCoordinates.getDouble(2)
                };

                Earthquake earthquake = new Earthquake();
                earthquake.setPlace(properties.getString("place"));
                earthquake.setTime(properties.getLong("time"));
                earthquake.setCoordinates(coordinates);
                earthquake.setMagnitude(properties.getDouble("mag"));
                earthquake.setUrl(properties.getString("url"));
                earthquake.setDetailsUrl(properties.getString("detail"));
                earthquake.setAlertLevel(AlertLevel.getLevel(properties.getString("alert")));
                earthquakes.add(earthquake);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return earthquakes;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
