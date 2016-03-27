package fr.kocal.android.iut_mini_projet.eventListeners;

/**
 * Lire AsyncDownloader.java
 */
public abstract class OnContentDownloaded<T> {
    public abstract void onDownloaded(Error error, T o);
}
