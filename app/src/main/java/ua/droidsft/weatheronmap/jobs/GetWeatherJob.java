package ua.droidsft.weatheronmap.jobs;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.droidsft.weatheronmap.bus.RxBus;
import ua.droidsft.weatheronmap.bus.WeatherEvents;
import ua.droidsft.weatheronmap.model.WeatherData;
import ua.droidsft.weatheronmap.model.WeatherInfo;
import ua.droidsft.weatheronmap.net.QueryBuilder;
import ua.droidsft.weatheronmap.net.RetrofitProvider;

import static ua.droidsft.weatheronmap.bus.WeatherEvents.*;

/**
 * Job for retrieve weather data.
 * Created by Vlad on 19.05.2016.
 */
public class GetWeatherJob extends Job {
    private static final String TAG = "GetWeatherJob";
    private static final AtomicInteger sJobCounter = new AtomicInteger(0);
    private final int mId;
    private final LatLng mLatLng;

    public GetWeatherJob(LatLng latLng) {
        super(new Params(Priority.LOW).groupBy(Groups.MAIN));
        mId = sJobCounter.incrementAndGet();
        mLatLng = latLng;
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {
        if (mId != sJobCounter.get()) {
            // Other jobs has been added after this, so cancel this.
            return;
        }
        getWeather(mLatLng);
    }

    private void getWeather(final LatLng latLng) {
        RetrofitProvider.get().getWeatherApi()
                .loadWeather(QueryBuilder.getQuery(latLng.latitude, latLng.longitude))
                .enqueue(new Callback<WeatherData>() {
                    @Override
                    public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().main != null) {
                            WeatherData data = response.body();
                            Log.d(TAG, "onResponse: " + data.getName());
                            WeatherInfo info = new WeatherInfo(data);
                            RxBus.bus().send(new WeatherEvents.SuccessEvent(info, latLng));
                        } else {
                            RxBus.bus().send(new FailEvent(WeatherEvents.UNHANDLED_MSG));
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherData> call, Throwable t) {
                        if (t != null && t.getMessage() != null) {
                            RxBus.bus().send(new FailEvent(t.getMessage()));
                        } else {
                            RxBus.bus().send(new FailEvent(WeatherEvents.UNHANDLED_MSG));
                        }
                    }
                });
    }

    @Override
    protected void onCancel() {
        RxBus.bus().send(new FailEvent(WeatherEvents.UNHANDLED_MSG));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
