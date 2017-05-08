package ua.droidsft.weatheronmap.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;
import ua.droidsft.weatheronmap.bus.RxBus;
import ua.droidsft.weatheronmap.bus.WeatherEvents.FailEvent;
import ua.droidsft.weatheronmap.bus.WeatherEvents.SuccessEvent;
import ua.droidsft.weatheronmap.jobs.GetWeatherJob;
import ua.droidsft.weatheronmap.ui.WeatherView;

/**
 * Presenter for WeatherView.
 * Created by Vlad on 19.05.2016.
 */
public class WeatherPresenter extends BaseJobPresenter {
    private static final String TAG = "WeatherPresenter";
    private final static int REQUEST_CODE = 100;

    private CompositeSubscription mSubscription;

    private WeatherView mWeatherView;

    private boolean mInProgress = false;

    public WeatherPresenter(WeatherView weatherView) {
        mWeatherView = weatherView;
    }

    public void loadWeather(LatLng latLng) {
        if (mInProgress) return;
        setInProgress(true);
        mWeatherView.clearMap();
        mWeatherView.showLocation(latLng);
        mJobManager.addJobInBackground(new GetWeatherJob(latLng));
    }

    public void findLocation(GoogleApiClient client, Activity activity) {
        if (mInProgress) return;
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);
        // Request permissions if needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);
            return;
        }
        LocationServices.FusedLocationApi
                .requestLocationUpdates(client, request, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mWeatherView.showLocation(latLng);
                        loadWeather(latLng);
                    }
                });

    }

    public void permissionResult(int requestCode, int[] grantResults, GoogleApiClient client, Activity activity) {
        if (requestCode == REQUEST_CODE) {
            // Check for ACCESS_COARSE_LOCATION permission.
            if (grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                mWeatherView.showNoPermissions();
            } else {
                findLocation(client, activity);
            }
        }
    }

    public void rxBusSubscribe() {
        mSubscription = new CompositeSubscription();
        mSubscription.add(RxBus.bus().toObservable()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        setInProgress(false);
                        if (o instanceof SuccessEvent) {
                            mWeatherView.showWeather(((SuccessEvent) o).getWeatherInfo(), ((SuccessEvent) o).getLatLng());
                        } else if (o instanceof FailEvent) {
                            mWeatherView.showError();
                        }
                    }
                }));
    }

    public void rxBusUnSubscribe() {
        mSubscription.clear();
    }

    private void setInProgress(boolean inProgress) {
        mInProgress = inProgress;
        mWeatherView.setInProgress(mInProgress);
    }

}
