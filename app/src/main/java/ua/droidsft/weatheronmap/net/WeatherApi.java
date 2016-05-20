package ua.droidsft.weatheronmap.net;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import ua.droidsft.weatheronmap.model.WeatherData;

/**
 * API interface for Retrofit2.
 * Created by Vlad on 18.05.2016.
 */
public interface WeatherApi {

    @GET("weather")
    Call<WeatherData> loadWeather(@QueryMap Map<String, String> queries);
}
