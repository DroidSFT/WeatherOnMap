package ua.droidsft.weatheronmap.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ua.droidsft.weatheronmap.R;
import ua.droidsft.weatheronmap.model.WeatherInfo;
import ua.droidsft.weatheronmap.presenter.WeatherPresenter;

import static ua.droidsft.weatheronmap.Constants.ZOOM_MAP_DEFAULT;

/**
 * Fragment for Map.
 * Created by Vlad on 18.05.2016.
 */
public class WeatherMapFragment extends SupportMapFragment implements WeatherView {
    public static final String TAG = "WeatherMapFragment";

    private WeatherPresenter mPresenter;

    private WeatherWindowAdapter mAdapter;

    private GoogleApiClient mClient;
    private GoogleMap mMap;

    public static WeatherMapFragment newInstance() {
        return new WeatherMapFragment();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true); // No need to recreate fragment on rotates, etc.

        setHasOptionsMenu(true);
        mPresenter = new WeatherPresenter(this);

        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        getActivity().invalidateOptionsMenu();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                    }
                })
                .build();

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                init();
            }
        });
    }

    private void init() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mPresenter.loadWeather(latLng);
            }
        });
        mAdapter = new WeatherWindowAdapter();
        mMap.setInfoWindowAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().invalidateOptionsMenu();
        mClient.connect();
        mPresenter.rxBusSubscribe();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
        mPresenter.rxBusUnSubscribe();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.map_menu, menu);
        MenuItem locationItem = menu.findItem(R.id.action_locale);
        locationItem.setEnabled(mClient.isConnected());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_locale:
                mPresenter.findLocation(mClient, getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showWeather(WeatherInfo info, LatLng latLng) {
        showLocation(latLng);
        Marker marker = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                .position(latLng));
        mAdapter.showWeather(info, marker);
        marker.showInfoWindow();
    }

    @Override
    public void showLocation(LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_MAP_DEFAULT);
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public void showError() {
        showSnackbar(R.string.error);
    }

    @Override
    public void showNoPermissions() {
        showSnackbar(R.string.no_location_permission);
    }

    private void showSnackbar(int stringId) {
        if (getView() != null) {
            Snackbar.make(getView(), stringId, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void clearMap() {
        mMap.clear();
    }

    public void requestPermissionsResult(int requestCode, @NonNull int[] grantResults) {
        mPresenter.permissionResult(requestCode, grantResults, mClient, getActivity());
    }
}
