package ua.droidsft.weatheronmap.model;

import com.google.gson.annotations.Expose;

/**
 * Model for GSON.
 * Created by Vlad on 19.05.2016.
 */
public class Wind {
    @Expose
    public String speed;

    public String getSpeed() {
        return speed;
    }
}
