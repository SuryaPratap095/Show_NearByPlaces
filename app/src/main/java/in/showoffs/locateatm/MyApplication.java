package in.showoffs.locateatm;

import android.app.Application;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Administrator on 10/13/2015.
 */
public class MyApplication extends Application {


    Marker marker;
    LatLng latLng;
    GoogleMap googleMap;

    public MyApplication() {
    }

    public MyApplication(Marker marker, LatLng latLng, GoogleMap googleMap) {
        this.marker = marker;
        this.latLng = latLng;
        this.googleMap = googleMap;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
}