package fr.kocal.android.iut_mini_projet;

/**
 * Représente le niveau d'alerte d'un earthquake
 */
public enum AlertLevel {
    GREEN(R.color.alertGreen),
    YELLOW(R.color.alertYellow),
    RED(R.color.alertRed),
    NO_COLOR(R.color.alertNone);

    /**
     * Couleur associée au level
     */
    private int colorId;

    AlertLevel(int colorId) {
        this.colorId = colorId;
    }

    /**
     * Retourne la couleur correspondant au niveau d'alerte
     * @return
     */
    public int getColorId() {
        return this.colorId;
    }

    /**
     * Retourne la couleur au level correspondant au niveau d'alerte passé en paramètre
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
