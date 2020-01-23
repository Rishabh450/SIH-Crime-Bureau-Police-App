package com.sih.policeapp.Services;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sih.policeapp.Broadcasters.Restarter;

import java.util.List;

public class MyService extends Service {
    SupportMapFragment mapFragment;
    GoogleMap mMap;
    Switch patrol;
    Intent mServiceIntent;
    Location lastKnown;
    String vehicle="rishabhKaGaadi";

    @Override
    public IBinder onBind(Intent intent) {
        // This won't be a bound service, so simply return null
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() +2000, restartServicePI);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
       // stoptimertask();


        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
    }

    @Override
    public void onCreate() {
        // This will be called when your Service is created for the first time
        // Just do any operations you need in this method.
        Log.d("serviceStared","gun");

        final DatabaseReference myref= FirebaseDatabase.getInstance().getReference().child("Time").child(vehicle).child("Location");
        final DatabaseReference myref1= FirebaseDatabase.getInstance().getReference().child("Beats").child(vehicle).child("Location");


        lastKnown=getLastKnownLocation();
        myref1.setValue(lastKnown.getLatitude()+"||"+lastKnown.getLongitude());

        //final Handler handler = new Handler();

      /*  final Runnable r = new Runnable() {
            public void run() {
                // tv.append("Hello World");
               myref.setValue(System.currentTimeMillis());


                handler.postDelayed(this, 2000);
            }
        };

        handler.postDelayed(r, 2000);*/
       LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("locationchangednow","yess");
                Toast.makeText(MyService.this,"Location changed",Toast.LENGTH_LONG);
                myref1.setValue(location.getLatitude()+"||"+location.getLongitude());



            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        startForeground(21,new Notification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();

        return START_STICKY;
    }
    private Location getLastKnownLocation() {
        LocationManager mLocationManager;
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.


                }
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}