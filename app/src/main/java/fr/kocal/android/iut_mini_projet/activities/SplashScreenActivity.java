package fr.kocal.android.iut_mini_projet.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import fr.kocal.android.iut_mini_projet.R;
import fr.kocal.android.iut_mini_projet.asyncTasks.AsyncDownloader;
import fr.kocal.android.iut_mini_projet.eventListeners.OnContentDownloaded;

public class SplashScreenActivity extends AppCompatActivity {

    ProgressBar mProgressBar;
    TextView mLoaderMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        initProgressBar();

        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (hasInternetConnection()) {
                    mLoaderMessage.setText(getString(R.string.splashMessageGotConnection));
                    fetchDatas();
                } else {
                    Toast.makeText(SplashScreenActivity.this, getString(R.string.splashMessageNoConnection), Toast.LENGTH_SHORT).show();
                    h.postDelayed(this, 2000);
                }
            }
        }, 1000);
    }

    private void initProgressBar() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mLoaderMessage = (TextView) findViewById(R.id.loaderMessage);

        // On veut une couleur blanche pour le loader
        mProgressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);

        // Et couleur sombre pour la barre de notifications
        getWindow().getDecorView().setBackgroundColor(getColor(R.color.colorPrimaryDark));

        mLoaderMessage.setText(getString(R.string.splashMessageCheckConnectivity));
    }

    private boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void fetchDatas() {
        mLoaderMessage.setText(getString(R.string.splashMessageFetchLastEarthquakes));

        new AsyncDownloader<>(JSONObject.class, new OnContentDownloaded<JSONObject>() {
            @Override
            public void onDownloaded(JSONObject jsonObject) {
                if (jsonObject != null) {
                    launchMainActivity(jsonObject);
                    return;
                }

                mLoaderMessage.setText(getString(R.string.splashMessageFetchLastEarthquakesError));
            }
        }).execute("http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_week.geojson");
    }

    public void launchMainActivity(JSONObject jsonObject) {
        Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);

        mLoaderMessage.setText(getString(R.string.splashMessageFetchLastEarthquakesSuccess));
        i.putExtra("JSON", jsonObject.toString());
        startActivity(i);
        finish();
    }
}
