package ua.droidsft.weatheronmap.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import ua.droidsft.weatheronmap.App;
import ua.droidsft.weatheronmap.R;
import ua.droidsft.weatheronmap.databinding.WeatherInfoBinding;
import ua.droidsft.weatheronmap.model.WeatherInfo;

/**
 * InfoWindowAdapter for displaying weather.
 * Created by Vlad on 19.05.2016.
 */
public class WeatherWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private static final String TAG = "WeatherWindowAdapter";

    private WeatherInfoBinding mBinding;

    public WeatherWindowAdapter() {
        LayoutInflater inflater = LayoutInflater.from(App.getApp());
        mBinding = WeatherInfoBinding.inflate(inflater);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return mBinding.getRoot();
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    public void showWeather(WeatherInfo info, final Marker marker) {

//        mBinding.setInfo(info); // Does not work with Map's InfoWindow?

        mBinding.tvName.setText(info.getName());
        mBinding.tvMain.setText(info.getMain());
        mBinding.tvDesc.setText(info.getDescription());
        mBinding.tvTemperature.setText(info.getTemperature());
        mBinding.tvHumidity.setText(info.getHumidity());
        mBinding.tvWindSpeed.setText(info.getWindSpeed());

        Glide.with(App.getApp())
                .load(info.getIconUrl())
                .dontAnimate()
                .placeholder(R.drawable.ic_no_icon)
                .error(R.drawable.ic_no_icon)
                .into(new GlideDrawableImageViewTarget(mBinding.weatherImage) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        super.setResource(resource);
                        if (marker != null && marker.isInfoWindowShown()) {
                            marker.showInfoWindow();
                        }
                    }
                });

    }
}
