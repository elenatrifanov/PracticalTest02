package ro.pub.cs.systems.eim.practical2test;

import androidx.annotation.NonNull;

public class WeatherForecastInformation {

    private final String updated;
    private final String code;
    private final String rate;
    private final String description;

    public WeatherForecastInformation(String updated, String code, String rate, String description) {
        this.updated = updated;
        this.code = code;
        this.rate = rate;
        this.description = description;
    }

    public String getUpdated() {
        return updated;
    }

    public String getCode() {
        return code;
    }

    public String getRate() {
        return rate;
    }

    public String getDescription() {
        return description;
    }

    @NonNull
    @Override
    public String toString() {
        return "Info{" + "updated='" + updated + '\'' + ", code='" + code + '\'' + ", description='"
                + description + '\'' + ", rate='" + rate + '}';
    }
}
