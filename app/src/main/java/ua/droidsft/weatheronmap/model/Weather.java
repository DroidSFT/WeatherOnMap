package ua.droidsft.weatheronmap.model;

import com.google.gson.annotations.Expose;

import ua.droidsft.weatheronmap.Constants;

/**
 * Model for GSON.
 * Created by Vlad on 19.05.2016.
 */
public class Weather {

    @Expose
    public String main;
    @Expose
    public String description;
    @Expose
    public String icon;

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    public String getIconUrl() {
        if (icon == null || icon.equals("")) return null;
        else return Constants.IMAGE_BASE_URL + icon + ".png";
    }
}
