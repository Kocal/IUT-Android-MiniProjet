package fr.kocal.android.iut_mini_projet.asyncTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

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
 * À la base j'avais deux classes AsyncImageDownloader et AsyncJsonDownloader,
 * ainsi que deux listeners OnImageDownloaded et OnJsonDownloaded.
 * Avec la duplication de code et de fichiers, c'était ultra chiant et galère
 * de créer un Async${TYPE}Downloader et un On${TYPE}Downloaded pour chaque type.
 * J'ai donc créé une classe AsyncDownloader qui utilise les templates de Java afin de
 * gérer plusieurs types/classes facilement (comme String, JSONObject, ...).
 */
public class AsyncDownloader<T> extends AsyncTask<String, Integer, T> {

    /**
     * Type de ressource à télécharger
     */
    private final Class<T> type;

    /**
     * Callback qui sera exécuté quand le téléchargement sera terminé
     */
    private final OnContentDownloaded onContentDownloaded;

    /**
     * Message d'erreur
     */
    private String errorMessage;

    /**
     * @param type                Classe Java correspondant au type de données à renvoyer (String, JSONObject, BitMap)
     * @param onContentDownloaded Callback exécuté à la fin du téléchargement
     */
    public AsyncDownloader(Class<T> type, OnContentDownloaded onContentDownloaded) {
        this.type = type;
        this.onContentDownloaded = onContentDownloaded;
    }

    /**
     * Télécharge la ressource sur internet
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
            errorMessage = "L'URL n'a pas un format valide";
            return null;
        }

        // On ouvre la connexion
        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                errorMessage = "Le code de retour n'est pas valide : " + urlConnection.getResponseCode();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            errorMessage = "Impossible d'ouvrir une connexion HTTP quand l'URL est : " + url.toString();
            return null;
        }

        // On lit le flux
        try {
            is = urlConnection.getInputStream();
            output = this.readStream(is);
        } catch (IOException e) {
            e.printStackTrace();
            errorMessage = "Impossible de lire le flux";
            return null;
        } finally {
            urlConnection.disconnect();
        }

        return output;
    }

    /**
     * Lit de contenu d'un InputStream
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
     * Lit le flux d'un InputStream de type texte
     *
     * @param is
     * @return
     */
    public T readPlainStream(InputStream is) {
        BufferedReader in = new BufferedReader(new InputStreamReader(is), 16 * 1024);
        StringBuilder buffer = new StringBuilder();
        String line;

        try {
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }

            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            errorMessage = "Erreur pendant la lecture du flux";
            return null;
        }

        return (T) buffer.toString();
    }

    /**
     * Lit le contenu d'un InputStream de type Image
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
            errorMessage = "Erreur pendant la lecture du flux";
            return null;
        }

        return (T) bitmap;
    }

    /**
     * Lit le flux d'un InputStream de type texte et le convertit en JSONObject
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
     * Exécute le callback et envoie possiblement une instance de Error ou pas
     *
     * @param o
     */
    @Override
    protected void onPostExecute(T o) {
        this.onContentDownloaded.onDownloaded(
                (errorMessage != null && !errorMessage.isEmpty() ? new Error(errorMessage) : null),
                o);
    }
}
