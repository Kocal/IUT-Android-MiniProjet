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

    private double magnitude;
    private AlertLevel alertLevel;
    private String place;
    private Double[] coordinates;
    private long time;

    private String url;

    private String detailsUrl;
    private JSONObject details;

    private Bitmap bm;

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public AlertLevel getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(AlertLevel alertLevel) {
        this.alertLevel = alertLevel;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Double[] coordinates) {
        this.coordinates = coordinates;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public JSONObject getDetails() {
        return details;
    }

    public void setDetails(JSONObject details) {
        this.details = details;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

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
                '}';
    }

    public void downloadDetails() {
        new AsyncDownloader<JSONObject>(JSONObject.class, new OnContentDownloaded<JSONObject>() {
            @Override
            public void onDownloaded(JSONObject jsonObject) {
                Log.v("Kocal", jsonObject.toString());
            }
        }).execute(this.detailsUrl);
    }
}
