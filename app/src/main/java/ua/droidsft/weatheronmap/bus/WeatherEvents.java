package ua.droidsft.weatheronmap.bus;

import com.google.android.gms.maps.model.LatLng;

import ua.droidsft.weatheronmap.model.WeatherInfo;

/**
 * Events for RxBus.
 * Created by Vlad on 19.05.2016.
 */
public class WeatherEvents {
    public static final String UNHANDLED_MSG = "UNHANDLED_MSG";

    public static class SuccessEvent {
        private final WeatherInfo mWeatherInfo;
        private final LatLng mLatLng;

        public SuccessEvent(WeatherInfo weatherInfo, LatLng latLng) {
            mWeatherInfo = weatherInfo;
            mLatLng = latLng;
        }

        public WeatherInfo getWeatherInfo() {
            return mWeatherInfo;
        }

        public LatLng getLatLng() {
            return mLatLng;
        }
    }

    public static class FailEvent {
        private final String mErrorMessage;

        public FailEvent(String errorMessage) {
            mErrorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return mErrorMessage;
        }
    }
}
