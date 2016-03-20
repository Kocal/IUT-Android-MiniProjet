package fr.kocal.android.iut_mini_projet.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

    MainActivity self = this;

    JSONObject json;

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        self.initToolbar();

        // Inutile puisqu'on gère ça dans le splashscreen, mais c'est au cas où :^)
        if (!self.fetchJson()) {
            Toast.makeText(MainActivity.this, "Impossible de récupérer le JSON", Toast.LENGTH_SHORT).show();
            return;
        }

        self.updateToolbarTitle();
        self.initListView();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void updateToolbarTitle() {
        try {
            // Soit on affiche le titre du JSON
            Log.v("Kocal", self.json.toString());
            getSupportActionBar().setTitle(self.json.getJSONObject("metadata").getString("title"));
        } catch (JSONException e) {
            // Soit le titre par défaut s'il y a eu une erreur
            getSupportActionBar().setTitle(getString(R.string.titleDisplayingEarthquakes));
            e.printStackTrace();
        }
    }

    private boolean fetchJson() {
        try {
            self.json = new JSONObject(getIntent().getStringExtra("JSON"));
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void initListView() {
        self.mListView = (ListView) findViewById(R.id.listView);

        ArrayList<Earthquake> earthquakes = self.extractEarthquakesFromJSON();

        final EarthquakeAdapter earthquakeAdapter = new EarthquakeAdapter(self, earthquakes);
        self.mListView = (ListView) findViewById(R.id.listView);
        self.mListView.setAdapter(earthquakeAdapter);
        earthquakeAdapter.notifyDataSetChanged();
    }

    private ArrayList<Earthquake> extractEarthquakesFromJSON() {

        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        try {
            JSONArray features = self.json.getJSONArray("features");

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

                // On récupère le level
                String alertString = properties.getString("alert");
                AlertLevel alert = AlertLevel.normalize(alertString);

                Earthquake earthquake = new Earthquake();
                earthquake.setDetailsUrl(properties.getString("detail"));

                earthquake.downloadDetails();

                earthquake.setMagnitude(properties.getDouble("mag"));
                earthquake.setPlace(properties.getString("place"));
                earthquake.setTime(properties.getLong("time"));
                earthquake.setUrl(properties.getString("url"));
                earthquake.setCoordinates(coordinates);
                earthquake.setAlertLevel(alert);
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
