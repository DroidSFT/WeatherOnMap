package ua.droidsft.weatheronmap.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.droidsft.weatheronmap.Constants;

/**
 * Provider of Retrofit2 instance.
 * Created by Vlad on 19.05.2016.
 */
public class RetrofitProvider {
    private static RetrofitProvider sRetrofitProvider;
    private static WeatherApi sWeatherApi;

    public static RetrofitProvider get() {
        if (sRetrofitProvider == null) {
            sRetrofitProvider = new RetrofitProvider();
        }
        return sRetrofitProvider;
    }

    private RetrofitProvider() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        sWeatherApi = retrofit.create(WeatherApi.class);
    }

    public WeatherApi getWeatherApi() {
        return sWeatherApi;
    }
}
