package fr.kocal.android.iut_mini_projet.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

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

    Earthquake earthquake = null;
    String sLocalisation, sMagnitude, sDate;

    private TextView mLocalisation, mMagnitude, mDate;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

        earthquake = (Earthquake) getIntent().getSerializableExtra("earthquake");

        initToolbar();
        initValues();
        initMaps();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(earthquake.getPlace());
    }

    private void initMaps() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Double[] coordinates = earthquake.getCoordinates();

        LatLng place = new LatLng(coordinates[1], coordinates[0]);
        mMap.addMarker(new MarkerOptions().position(place).title("Tremblement de terre"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, mMap.getMinZoomLevel()));

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mMap.animateCamera(CameraUpdateFactory.zoomTo(6));
            }
        }, 200);
    }

    private void initValues() {
        mLocalisation = (TextView) findViewById(R.id.localisation);
        mMagnitude = (TextView) findViewById(R.id.magnitude);
        mDate = (TextView) findViewById(R.id.date);

        // Formatage des coordonnées
        Double[] coordinates = earthquake.getCoordinates();
        sLocalisation = String.format(getString(R.string.coordinates),
                coordinates[0], coordinates[1]);

        // Formatage de la magnitude
        sMagnitude = String.format(getString(R.string.magnitude), earthquake.getMagnitude());

        // Formatage de la date
        Date date = new Date(earthquake.getTime());
        DateFormat dateFormat = DateFormat.getDateInstance();
        sDate = dateFormat.format(date);

        // Fill
        mLocalisation.setText(sLocalisation);
        mMagnitude.setText(sMagnitude);
        mDate.setText(sDate);
    }
}
