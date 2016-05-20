package ua.droidsft.weatheronmap.ui;

import com.google.android.gms.maps.model.LatLng;

import ua.droidsft.weatheronmap.model.WeatherInfo;

/**
 * View interface.
 * Created by Vlad on 19.05.2016.
 */
public interface WeatherView {

    void showWeather(WeatherInfo info, LatLng latLng);

    void showLocation(LatLng latLng);

    void showError();

    void showNoPermissions();

    void clearMap();

}
