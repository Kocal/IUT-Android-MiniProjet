package fr.kocal.android.iut_mini_projet;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONObject;

import java.util.Arrays;

import fr.kocal.android.iut_mini_projet.asyncTasks.AsyncDownloader;
import fr.kocal.android.iut_mini_projet.eventListeners.OnContentDownloaded;

/**
 * Created by kocal on 15/03/16.
 */
public class Earthquake {

    /**
     * Lieu (nom)
     */
    private String place;

    /**
     * Coordonnées
     */
    private Double[] coordinates;

    /**
     * Date
     */
    private long time;

    /**
     * Magnitude
     */
    private double magnitude;

    /**
     * Niveau d'alerte
     */
    private AlertLevel alertLevel;

    private String url;

    private String detailsUrl;
    private JSONObject details;

    /**
     * Retourne la magnitude
     * @return
     */
    public double getMagnitude() {
        return magnitude;
    }

    /**
     * Assigne une nouvelle magnitude
     * @param magnitude
     */
    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    /**
     * Retourne le niveau d'alerte
     * @return
     */
    public AlertLevel getAlertLevel() {
        return alertLevel;
    }

    /**
     * Assigne un nouveau niveau d'alerte
     * @param alertLevel
     */
    public void setAlertLevel(AlertLevel alertLevel) {
        this.alertLevel = alertLevel;
    }

    /**
     * Retourne le lieu
     * @return
     */
    public String getPlace() {
        return place;
    }

    /**
     * Assigne un nouveau lieu
     * @param place
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     * Retourne les coordonnées géographiques
     * @return
     */
    public Double[] getCoordinates() {
        return coordinates;
    }

    /**
     * Assigne de nouvelles coordonnées
     * @param coordinates
     */
    public void setCoordinates(Double[] coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Retourne le timestamp
     * @return
     */
    public long getTime() {
        return time;
    }

    /**
     * Assigne un nouveau timestamp
     * @param time
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Retourne les détails du tremblement
     * @return
     */
    public JSONObject getDetails() {
        return details;
    }

    /**
     * Assigne les détails du tremblement
     * @param details
     */
    public void setDetails(JSONObject details) {
        this.details = details;
    }

    /**
     * TODO: Trouver à quoi ça correspond
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * TODO: Trouver à quoi ça correspond
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Retourne l'URL pour voir les détails du tremblement
     * @return
     */
    public String getDetailsUrl() {
        return detailsUrl;
    }

    /**
     * Assigne une nouvelle URL pour voir les détails du tremblement
     * @param detailsUrl
     */
    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

    @Override
    public String toString() {
        return "Earthquake{" +
                "magnitude=" + magnitude +
                ", alertLevel=" + alertLevel +
                ", place='" + place + '\'' +
                ", coordinates=" + Arrays.toString(coordinates) +
                ", time=" + time +
                ", url='" + url + '\'' +
                ", detailsUrl='" + detailsUrl + '\'' +
                ", details=" + details +
                '}';
    }

    /**
     * Télécharge les détails du tremblement
     */
    public void downloadDetails() {
        new AsyncDownloader<JSONObject>(JSONObject.class, new OnContentDownloaded<JSONObject>() {
            @Override
            public void onDownloaded(Error error, JSONObject jsonObject) {
                Log.v("Kocal", jsonObject.toString());
            }
        }).execute(this.detailsUrl);
    }
}
