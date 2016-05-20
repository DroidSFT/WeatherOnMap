package ua.droidsft.weatheronmap.model;

import com.google.gson.annotations.Expose;

/**
 * Model for GSON.
 * Created by Vlad on 18.05.2016.
 */
public class WeatherData {
    @Expose
    public Weather[] weather;
    @Expose
    public Main main;
    @Expose
    public Wind wind;
    @Expose
    public String name;

    public String getName() {
        return name;
    }
}
