package ua.droidsft.weatheronmap.model;

import com.google.gson.annotations.Expose;

/**
 * Model for GSON.
 * Created by Vlad on 18.05.2016.
 */
public class Main {
    @Expose
    public String temp;
    @Expose
    public String humidity;

    public String getTemp() {
        return temp;
    }

    public String getHumidity() {
        return humidity;
    }
}
