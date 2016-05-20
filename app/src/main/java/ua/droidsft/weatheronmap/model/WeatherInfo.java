package ua.droidsft.weatheronmap.model;

import ua.droidsft.weatheronmap.App;
import ua.droidsft.weatheronmap.R;

/**
 * Model for displaying on View.
 * Created by Vlad on 19.05.2016.
 */
public class WeatherInfo {
    private final String mName;
    private String mIconUrl;
    private final String mMain;
    private final String mDescription;
    private final String mTemperature;
    private final String mHumidity;
    private final String mWindSpeed;

    public WeatherInfo(WeatherData data) {
        App app = App.getApp();
        String unknown = app.getString(R.string.unknown);
        mName = checkString(data.name, unknown);

        if (data.weather != null && data.weather[0] != null) {
            Weather weather = data.weather[0];
            mIconUrl = weather.getIconUrl();
            mMain = checkString(weather.getMain(), unknown);
            mDescription = checkString(weather.getDescription(), unknown);
        } else {
            mMain = mDescription = unknown;
        }

        Main main = data.main;
        if (main != null) {
            mTemperature = String.format(app.getString(R.string.temperature), checkString(main.getTemp(), unknown));
            mHumidity = String.format(app.getString(R.string.humidity), checkString(main.getHumidity(), unknown));
        } else {
            mTemperature = String.format(app.getString(R.string.temperature), unknown);
            mHumidity = String.format(app.getString(R.string.humidity), unknown);
        }

        Wind wind = data.wind;
        if (wind != null) {
            mWindSpeed = String.format(app.getString(R.string.wind_speed), checkString(wind.getSpeed(), unknown));
        } else {
            mWindSpeed = String.format(app.getString(R.string.wind_speed), unknown);
        }
    }

    private static String checkString(String s, String unknown) {
        if (s == null || s.equals("")) return unknown;
        else return s;
    }

    public String getName() {
        return mName;
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    public String getMain() {
        return mMain;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getTemperature() {
        return mTemperature;
    }

    public String getHumidity() {
        return mHumidity;
    }

    public String getWindSpeed() {
        return mWindSpeed;
    }
}
