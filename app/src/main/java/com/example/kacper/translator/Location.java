package com.example.kacper.translator;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.security.Permission;

public class Location extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    String message = "";
    String c1,c2;
    private BroadcastReceiver brodcastReciver;

    @Override
    protected void onResume() {
        super.onResume();
        if(brodcastReciver == null)
        {
            brodcastReciver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    c1=intent.getExtras().get("coordinates1").toString();
                    c2=intent.getExtras().get("coordinates2").toString();
                    if(c1!=null && c2!= null)
                    {
                        disable_track();
                        endactivity();
                    }
                }
            };
        }
        registerReceiver(brodcastReciver,new IntentFilter("location_update"));
    }

    private void endactivity() {
        RetriveLocationTask rl = new RetriveLocationTask();
        //rl.execute("53.1333300", "23.1643300");
        rl.execute(c2,c1);
        while ((rl.response2 == null)) ;
       /* String translation = rl.response1.substring(587);
        translation = translation.substring(0, 2);*/
        Intent intent2 = new Intent();
        intent2.putExtra("Lokalizacja", rl.response2);
        setResult(RESULT_OK, intent2);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(brodcastReciver != null)
        {
            unregisterReceiver(brodcastReciver);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        ((Button) findViewById(R.id.lokalizuj)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView tw = (TextView) findViewById(R.id.textView);
                tw.setVisibility(View.VISIBLE);

                if(!runtime_premmisions());
                {
                    enable_track();
                }

            }
        });


    }

    private void enable_track() {
        Intent i = new Intent(getApplicationContext(),GPS_Service.class);
        startService(i);
    }

    private  void disable_track() {
        Intent i = new Intent(getApplicationContext(),GPS_Service.class);
        stopService(i);
    }

    private Boolean runtime_premmisions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION ,Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==100)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            enable_track();
            }
            else
            {
                runtime_premmisions();
            }
        }
    }
}
