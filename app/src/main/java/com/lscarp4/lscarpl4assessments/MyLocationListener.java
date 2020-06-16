package com.lscarp4.lscarpl4assessments;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

public class MyLocationListener implements LocationListener {

    public void onLocationChanged(Location loc) {
        String message = String.format(
                "New Location \n Longitude: %1$s \n Latitude: %2$s",
                loc.getLongitude(), loc.getLatitude()
        );

    }
    public void onProviderDisabled(String arg0) {

    }
    public void onProviderEnabled(String provider) {

    }
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}