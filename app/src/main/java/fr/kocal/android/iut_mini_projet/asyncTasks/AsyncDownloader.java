package fr.kocal.android.iut_mini_projet.asyncTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import fr.kocal.android.iut_mini_projet.eventListeners.OnContentDownloaded;

/**
 * TODO: Doc
 * À la base j'avais deux classes AsyncImageDownloader et AsyncJsonDownloader,
 * ainsi que deux listeners OnImageDownloaded et OnJsonDownloaded.
 * Avec la duplication de code et de fichiers, c'était ultra chiant et galère
 * de créer un Async${TYPE}Downloader et un On${TYPE}Downloaded pour chaque type.
 * J'ai donc créé une classe AsyncDownloader qui utilise les templates de Java afin de
 * gérer plusieurs types/classes facilement (comme String, JSONObject, ...).
 * <p>
 * Created by kocal on 20/03/16.
 */
public class AsyncDownloader<T> extends AsyncTask<String, Integer, T> {

    /**
     * TODO: Doc
     */
    private final Class<T> type;

    /**
     * TODO: Doc
     */
    private final OnContentDownloaded onContentDownloaded;

    /**
     * TODO: Doc
     *
     * @param type                Type de données à renvoyer (null, String, JSONObject, BitMap)
     * @param onContentDownloaded Event
     */
    public AsyncDownloader(Class<T> type, OnContentDownloaded onContentDownloaded) {
        this.type = type;
        this.onContentDownloaded = onContentDownloaded;
    }

    /**
     * TODO: DOC
     *
     * @param params
     * @return
     */
    @Override
    protected T doInBackground(String... params) {
        T output = null;
        URL url = null;
        HttpURLConnection urlConnection = null;
        InputStream is = null;

        // On check l'URL
        try {
            url = new URL(params[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (url == null) {
            Log.e("AsyncDownloader", "L'URL n'a pas un format valide");
            return null;
        }

        // On ouvre la connexion
        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e("AsyncDownloader", "Le code de retour n'est pas valide");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (urlConnection == null) {
            Log.e("AsyncDownloader", "Impossible d'ouvrir une connexion HTTP quand l'URL est : " + url.toString());
            return null;
        }

        // On lit le flux
        try {
            is = urlConnection.getInputStream();
            output = this.readStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return output;
    }

    /**
     * TODO: Doc
     *
     * @param is
     * @return
     */
    public T readStream(InputStream is) {
        switch (this.type.getSimpleName()) {
            case "JSONObject":
                return this.readJsonStream(is);
            case "BitMap":
                return this.readImageStream(is);
            case "String":
            default:
                return this.readPlainStream(is);
        }
    }

    /**
     * TODO: Doc
     *
     * @param is
     * @return
     */
    public T readPlainStream(InputStream is) {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuilder buffer = new StringBuilder();
        String line;

        try {
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }

            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (T) buffer.toString();
    }

    /**
     * TODO: Doc
     *
     * @param is
     * @return
     */
    public T readImageStream(InputStream is) {
        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (T) bitmap;
    }

    /**
     * TODO: Doc
     *
     * @param is
     * @return
     */
    public T readJsonStream(InputStream is) {
        String streamContent = (String) this.readPlainStream(is);
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(streamContent);
            return (T) jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * TODO: Doc
     *
     * @param o
     */
    @Override
    protected void onPostExecute(T o) {
        this.onContentDownloaded.onDownloaded(o);
    }
}
