package fr.kocal.android.iut_mini_projet;

import java.io.Serializable;

/**
 * Représente un Earthquake
 */
public class Earthquake implements Serializable {

    /**
     * Id
     */
    private String id;

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
     * Se trouve dans les favoris ou pas
     */
    private boolean inFavorite;

    /**
     * Niveau d'alerte
     */
    private AlertLevel alertLevel;

    /**
     * URL de "présentation" sur le site officiel de l'earthquake
     */
    private String url;

    /**
     * URL des détails en JSON de l'earthquake
     */
    private String detailsUrl;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    /**
     * Retourne la magnitude
     *
     * @return
     */
    public double getMagnitude() {
        return magnitude;
    }

    /**
     * Assigne une nouvelle magnitude
     *
     * @param magnitude
     */
    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    /**
     * Retourne le niveau d'alerte
     *
     * @return
     */
    public AlertLevel getAlertLevel() {
        return alertLevel;
    }

    /**
     * Assigne un nouveau niveau d'alerte
     *
     * @param alertLevel
     */
    public void setAlertLevel(AlertLevel alertLevel) {
        this.alertLevel = alertLevel;
    }

    /**
     * Retourne le lieu
     *
     * @return
     */
    public String getPlace() {
        return place;
    }

    /**
     * Assigne un nouveau lieu
     *
     * @param place
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     * Retourne les coordonnées géographiques
     *
     * @return
     */
    public Double[] getCoordinates() {
        return coordinates;
    }

    /**
     * Assigne de nouvelles coordonnées
     *
     * @param coordinates
     */
    public void setCoordinates(Double[] coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Retourne le timestamp
     *
     * @return
     */
    public long getTime() {
        return time;
    }

    /**
     * Assigne un nouveau timestamp
     *
     * @param time
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     *  Retourne l'URL présentant l'Earthquake sur le site officiel
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * Assigne une nouvelle URL afin de présenter l'Earthquake
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Retourne l'URL pour voir les détails du tremblement
     *
     * @return
     */
    public String getDetailsUrl() {
        return detailsUrl;
    }

    /**
     * Assigne une nouvelle URL pour voir les détails du tremblement
     *
     * @param detailsUrl
     */
    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

    /**
     * Met oui ou non le tremblement de terre en favoris
     *
     * @param inFavorite
     */
    public void setInFavorite(boolean inFavorite) {
        this.inFavorite = inFavorite;
    }

    /**
     * Retourne true ou false selon si le tremblement terre est en
     *
     * @return
     */
    public boolean isInFavorite() {
        return inFavorite;
    }
}
