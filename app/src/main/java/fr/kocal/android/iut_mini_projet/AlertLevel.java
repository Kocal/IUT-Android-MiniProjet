package fr.kocal.android.iut_mini_projet;

/**
 * Created by kocal on 20/03/16.
 * TOOD: DOC
 */
public enum AlertLevel {
    GREEN(R.color.alertGreen),
    YELLOW(R.color.alertYellow),
    RED(R.color.alertRed),
    NO_COLOR(0);

    /**
     * Couleur associée au level
     */
    private int colorId;

    AlertLevel(int colorId) {
        this.colorId = colorId;
    }

    /**
     * Retourne la couleur associée au level
     *
     * @return
     */
    public int getColorId() {
        return this.colorId;
    }

    /**
     * Retourne la couleur associée au level passé en paramètre
     *
     * @param level
     * @return
     */
    public static AlertLevel getColor(String level) {
        switch (level) {
            case "green":
                return AlertLevel.GREEN;
            case "yellow":
                return AlertLevel.YELLOW;
            case "red":
                return AlertLevel.RED;
            default:
                return AlertLevel.NO_COLOR;
        }
    }
}
