package fr.kocal.android.iut_mini_projet.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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
     * Menu de l'ActionBar, on modifiera l'icone d'un des items
     */
    Menu menu;
    Drawable iconMore;
    Drawable iconLess;

    /**
     * Earthquake à afficher
     */
    Earthquake earthquake = null;

    /**
     * Infos
     */
    String sLocalisation, sMagnitude, sDate, sUrl;

    /**
     * UI elements
     */
    private CardView mDetails;
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

    private void initValues() {
        mDetails = (CardView) findViewById(R.id.card_details);
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

        mDetails.setVisibility(View.INVISIBLE);
        mDetails.setAlpha(0f);
        mDetails.setTranslationY(-mDetails.getHeight());
    }

    private void hideDetails() {
        Log.v("Kocal", "hideDetails()");

        mDetails.animate().alpha(0f).translationY(-mDetails.getHeight()).setDuration(200).withEndAction(new Runnable() {
            @Override
            public void run() {
                mDetails.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void showDetails() {
        Log.v("Kocal", "showDetails()");
        mDetails.setVisibility(View.VISIBLE);
        mDetails.animate().alpha(1f).translationY(0).setDuration(200);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Double[] coordinates = earthquake.getCoordinates();
        LatLng place = new LatLng(coordinates[1], coordinates[0]);

        mMap.addMarker(new MarkerOptions().position(place));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, mMap.getMinZoomLevel()));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(4));

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).run();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_earthquake, menu);
        this.menu = menu;
        iconMore = getResources().getDrawable(R.drawable.ic_expand_more, null);
        iconLess = getResources().getDrawable(R.drawable.ic_expand_less, null);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_show_details:
                if (mDetails.getVisibility() == View.INVISIBLE) {
                    menu.getItem(0).setIcon(iconLess);
                    showDetails();
                } else {
                    menu.getItem(0).setIcon(iconMore);
                    hideDetails();
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
