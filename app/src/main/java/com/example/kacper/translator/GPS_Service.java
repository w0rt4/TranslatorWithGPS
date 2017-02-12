package com.example.kacper.translator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.location.Location;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

/**
 * Created by Kacper on 2016-12-26.
 */

@SuppressWarnings("MissingPermission")
public class GPS_Service extends Service{
    private LocationListener locationListener;
    private LocationManager locationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
         locationListener = new LocationListener() {
             @Override
             public void onLocationChanged(Location location) {
                Intent i = new Intent("location_update");
                 i.putExtra("coordinates1", location.getLongitude() );
                 i.putExtra("coordinates2", location.getLatitude());
                 sendBroadcast(i);
             }

             @Override
             public void onStatusChanged(String s, int i, Bundle bundle) {

             }

             @Override
             public void onProviderEnabled(String s) {

             }

             @Override
             public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                 i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 startActivity(i);
             }
         };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //GPS_PROVIDER lub NETWORK_PROVIDER
        //noinspection MissingPermission
        locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER ,3000 ,0 ,locationListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null)
        {

            locationManager.removeUpdates(locationListener);
        }
    }
}
