package com.svmindlogic.tiffin.tiffinmap;

import android.app.Service;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationService implements LocationListener {

    private static final String TAG = "LocationService";
    // flag for GPS status
    private boolean isGPSEnabled = false;

    // flag for network status
    private boolean isNetworkEnabled = false;

    // flag for GPS status
    public boolean canGetLocation = false;

    private Location location; // location
    private double latitude; // latitude
    private double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1 * 10 * 1000; // 10 seconds

    // Declaring a Location Manager
    protected LocationManager locationManager;
    private Context c;

    public LocationService(Context service) {
        this.c = service;
        locationManager = (LocationManager) c.getSystemService(Service.LOCATION_SERVICE);
        getLocation();
    }

    public void getLocation() {

        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isNetworkEnabled == false && isGPSEnabled == false) {
            // no provider is enabled
            // dashboard_bottpm_backgroud get location of network
            Log.i(TAG, "No provider is enabled");
            /*location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            } else {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }

            }*/

        } else {
            // anyone provider is enabled

            canGetLocation = true;
            if (isGPSEnabled) {
                // GPS is enabled
                Log.i(TAG, "GPS is enabled");
                location = null;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        this.latitude = location.getLatitude();
                        this.longitude = location.getLongitude();
                        Log.i(TAG, "latitude: " + latitude + " longitude: " + longitude);
                    }
                }

            } else {
                // network is enabled
                Log.i(TAG, "NETWORK is enabled");
                location = null;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        this.latitude = location.getLatitude();
                        this.longitude = location.getLongitude();
                        Log.i(TAG, "latitude: " + latitude + " longitude: " + longitude);
                    }
                }

            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        Log.i(TAG, "onLocationChanged:-latitude: " + latitude + " longitude: " + longitude);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        if (provider == LocationManager.GPS_PROVIDER)
            this.isGPSEnabled = true;
        if (provider == LocationManager.NETWORK_PROVIDER)
            this.isNetworkEnabled = true;

    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider == LocationManager.GPS_PROVIDER)
            this.isGPSEnabled = false;
        if (provider == LocationManager.NETWORK_PROVIDER)
            this.isNetworkEnabled = false;

    }

    public double getLongitude() {


        return longitude;

    }

    public double getLatitude() {


        return latitude;

    }

    public void stopUsingGPS() {
        if (locationManager != null)
            locationManager.removeUpdates(this);
    }
}
