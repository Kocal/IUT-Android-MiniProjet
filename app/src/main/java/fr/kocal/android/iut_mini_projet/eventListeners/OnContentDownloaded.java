package fr.kocal.android.iut_mini_projet.eventListeners;

/**
 * Created by kocal on 20/03/16.
 * <p>
 * Lire AsyncDownloader.java
 */
public abstract class OnContentDownloaded<T> {
    public abstract void onDownloaded(T o);
}
