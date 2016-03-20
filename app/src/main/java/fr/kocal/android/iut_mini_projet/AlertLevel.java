package fr.kocal.android.iut_mini_projet;

/**
 * Created by kocal on 20/03/16.
 */
public enum AlertLevel {
    GREEN(R.color.alertGreen),
    YELLOW(R.color.alertYellow),
    RED(R.color.alertRed);

    private int colorId;

    AlertLevel(int colorId) {
        this.colorId = colorId;
    }

    public static AlertLevel getLevel(String level) {
        switch (level) {
            case "green":
                return AlertLevel.GREEN;
            case "yellow":
                return AlertLevel.YELLOW;
            case "red":
                return AlertLevel.YELLOW;
            default:
                return null;
        }
    }

    public int getColorId() {
        return this.colorId;
    }
}
