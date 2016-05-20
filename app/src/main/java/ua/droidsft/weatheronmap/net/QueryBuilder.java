package ua.droidsft.weatheronmap.net;

import java.util.HashMap;
import java.util.Map;

import ua.droidsft.weatheronmap.Constants;


/**
 * Helper for building queries for Retrofit2.
 * Created by Vlad on 19.05.2016.
 */
public class QueryBuilder {
    private static final String UNITS = "metric";

    public static Map<String, String> getQuery(double lat, double lon) {
        Map<String, String> map = new HashMap<>();

        map.put("lat", String.valueOf(lat));
        map.put("lon", String.valueOf(lon));
        map.put("units", UNITS);
        map.put("appid", Constants.WEATHER_API_KEY);

        return map;
    }
}
