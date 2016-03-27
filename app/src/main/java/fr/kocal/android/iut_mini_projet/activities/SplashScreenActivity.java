package fr.kocal.android.iut_mini_projet.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONObject;

import fr.kocal.android.iut_mini_projet.R;
import fr.kocal.android.iut_mini_projet.asyncTasks.AsyncDownloader;
import fr.kocal.android.iut_mini_projet.eventListeners.OnContentDownloaded;

public class SplashScreenActivity extends AppCompatActivity {

    /**
     * Loader au centre du splash
     */
    ProgressBar mProgressBar;

    /**
     * Affichage des messages d'informations à l'utilisateur sur ce qu'il se passe
     */
    TextView mLoaderMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        setBackgroundColor();
        initProgressBar();
        initLoaderMessage();

        // On check l'état de la connexion internet de l'utilisateur toutes les 3 secondes
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (hasInternetConnection()) {
                    mLoaderMessage.setText(getString(R.string.splashMessageGotConnection));
                    fetchDatas();
                } else {
                    Toast.makeText(SplashScreenActivity.this, getString(R.string.splashMessageNoConnection), Toast.LENGTH_SHORT).show();
                    h.postDelayed(this, 3000);
                }
            }
        }, 0);
    }

    /**
     * Initialise le loader
     */
    private void initProgressBar() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        // Couleur blanche pour le loader
        mProgressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
    }

    /**
     * Change le background de la barre de notifications
     */
    private void setBackgroundColor() {
        getWindow().getDecorView().setBackgroundColor(getColor(R.color.colorPrimaryDark));
    }

    /**
     * Initialise le message du loader
     */
    private void initLoaderMessage() {
        mLoaderMessage = (TextView) findViewById(R.id.loaderMessage);
        mLoaderMessage.setText(getString(R.string.splashMessageCheckConnectivity));
    }

    /**
     * Vérifie la connectivité à internet
     *
     * @return boolean
     */
    private boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Récupère les données JSON à partir de l'API
     */
    private void fetchDatas() {
        mLoaderMessage.setText(getString(R.string.splashMessageFetchLastEarthquakes));

        new AsyncDownloader<>(JSONObject.class, new OnContentDownloaded<JSONObject>() {
            @Override
            public void onDownloaded(Error error, JSONObject jsonObject) {
                if (error != null) {
                    Toast.makeText(SplashScreenActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (jsonObject == null) {
                    mLoaderMessage.setText(getString(R.string.splashMessageFetchLastEarthquakesError));
                    return;
                }

                launchMainActivity(jsonObject);
            }
        }).execute("http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_week.geojson");
    }

    /**
     * Lance la MainActivity en lui passant le JSON récupéré via l'API
     *
     * @param jsonObject
     */
    public void launchMainActivity(final JSONObject jsonObject) {
        final Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);

        mLoaderMessage.setText(getString(R.string.splashMessageFetchLastEarthquakesSuccess));
        mLoaderMessage.setText(getString(R.string.splashMessageMapCreation));

        // On initialise une map vide
        SupportMapFragment fMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fMap.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mLoaderMessage.setText(getString(R.string.splashMessageMapCreationSuccess));

                i.putExtra("JSON", jsonObject.toString());
                startActivity(i);
                finish();
            }
        });
    }
}
