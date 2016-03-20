package fr.kocal.android.iut_mini_projet;

/**
 * Created by kocal on 20/03/16.
 */
public enum AlertLevel {
    green(R.color.alertGreen),
    yellow(R.color.alertYellow),
    red(R.color.alertRed);

    private int colorId;

    AlertLevel(int colorId) {
        this.colorId = colorId;
    }

    public static AlertLevel normalize(String level) {
        switch (level) {
            case "green":
                return AlertLevel.green;
            case "yellow":
                return AlertLevel.yellow;
            case "red":
                return AlertLevel.red;
            default:
                return null;
        }
    }

    public int getColorId() {
        return this.colorId;
    }
}
