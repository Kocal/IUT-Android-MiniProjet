package fr.kocal.android.iut_mini_projet.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.util.Date;

import fr.kocal.android.iut_mini_projet.Earthquake;
import fr.kocal.android.iut_mini_projet.R;

public class EarthquakeActivity extends AppCompatActivity implements OnMapReadyCallback {

    /**
     * Earthquake à afficher
     */
    Earthquake earthquake = null;

    /**
     * Infos
     */
    String sLocalisation, sMagnitude, sDate, sUrl;

    /**
     * UI elments
     */
    private TextView mLocalisation, mMagnitude, mDate, mUrl;

    /**
     * Map Google Maps ;-))
     */
    private SupportMapFragment fMap;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

        earthquake = (Earthquake) getIntent().getSerializableExtra("earthquake");

        if (earthquake == null) {
            Toast.makeText(EarthquakeActivity.this, "Il y a eu un problème lors de l'affichage du tremblement de terre", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initToolbar();
        initValues();

        // Sinon ça bloque le thread principal, et c'est chiant
        new Thread(new Runnable() {
            @Override
            public void run() {
                initMaps();
            }
        }).run();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(earthquake.getPlace());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initMaps() {
        fMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fMap.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Double[] coordinates = earthquake.getCoordinates();
        LatLng place = new LatLng(coordinates[1], coordinates[0]);

        mMap.addMarker(new MarkerOptions().position(place).title("Tremblement de terre"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, mMap.getMinZoomLevel()));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(4));
    }

    private void initValues() {
        mLocalisation = (TextView) findViewById(R.id.localisation);
        mMagnitude = (TextView) findViewById(R.id.magnitude);
        mDate = (TextView) findViewById(R.id.date);
        mUrl = (TextView) findViewById(R.id.url);

        // Formatage des coordonnées
        Double[] coordinates = earthquake.getCoordinates();
        sLocalisation = String.format(getString(R.string.coordinates),
                coordinates[0], coordinates[1]);

        // Formatage de la magnitude
        sMagnitude = String.format(getString(R.string.magnitude), earthquake.getMagnitude());

        // Formatage de la date
        Date date = new Date(earthquake.getTime());
        DateFormat dateFormat = DateFormat.getDateInstance();
        sDate = String.format(getString(R.string.date), dateFormat.format(date));

        // Url
        sUrl = earthquake.getUrl();

        // Fill
        mLocalisation.setText(sLocalisation);
        mMagnitude.setText(sMagnitude);
        mDate.setText(sDate);
        mUrl.setText(sUrl);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
