package fr.kocal.android.iut_mini_projet.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import fr.kocal.android.iut_mini_projet.Earthquake;
import fr.kocal.android.iut_mini_projet.R;

public class ShowOnMaps extends AppCompatActivity implements OnMapReadyCallback {

    ArrayList<Earthquake> earthquakes;

    /**
     * Map Google Maps ;-))
     */
    private SupportMapFragment fMap;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_on_maps);

        initToolbar();
        earthquakes = (ArrayList<Earthquake>) getIntent().getSerializableExtra("earthquakes");
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
        getSupportActionBar().setTitle(getString(R.string.earthquakes));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initMaps() {
        fMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fMap.getView().setVisibility(View.INVISIBLE);
        fMap.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fMap.getView().setVisibility(View.VISIBLE);

        for (Earthquake earthquake : earthquakes) {
            Double[] coordinates = earthquake.getCoordinates();
            LatLng place = new LatLng(coordinates[1], coordinates[0]);
            mMap.addMarker(new MarkerOptions().position(place).title("Tremblement de terre"));
        }
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
