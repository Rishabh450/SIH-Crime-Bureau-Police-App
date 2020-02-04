package com.sih.policeapp.Services;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sih.policeapp.Broadcasters.Restarter;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyService extends Service implements SensorEventListener {
    SupportMapFragment mapFragment;
    GoogleMap mMap;
    Switch patrol;
    Intent mServiceIntent;
    Location lastKnown;
    SensorManager sensorManager;
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
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        vehicle= FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        if(vehicle.contains(" "))
        vehicle=vehicle.substring(0,vehicle.indexOf(' '));

        final DatabaseReference myref= FirebaseDatabase.getInstance().getReference().child("Time").child(vehicle).child("Location");
        final DatabaseReference myref1= FirebaseDatabase.getInstance().getReference().child("Beats").child(vehicle);
        Map<String,String> mp=new HashMap<>();
        lastKnown=getLastKnownLocation();
        mp.put("Location",lastKnown.getLatitude()+"||"+lastKnown.getLongitude());
        mp.put("Emergency","No");



        myref1.setValue(mp);
        mp.clear();

        final Handler handler = new Handler();

      final Runnable r = new Runnable() {
            public void run() {
                // tv.append("Hello World");
               myref.setValue(System.currentTimeMillis());


                handler.postDelayed(this, 2000);
            }
        };

        handler.postDelayed(r, 2000);
       LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("locationchangednow","yess");
                Toast.makeText(MyService.this,"Location changed",Toast.LENGTH_LONG);
                myref1.child("Location"). setValue(location.getLatitude()+"||"+location.getLongitude());



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


       startForeground(10,new Notification());
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
    int index=0;
    String TAG ="Hey there";
    @Override
    public void onSensorChanged(SensorEvent foEvent) {

        if (foEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            double loX = foEvent.values[0];
            double loY = foEvent.values[1];
            double loZ = foEvent.values[2];

            double loAccelerationReader = Math.sqrt(Math.pow(loX, 2)
                    + Math.pow(loY, 2)
                    + Math.pow(loZ, 2));
            long mlPreviousTime = System.currentTimeMillis();
            Log.i(TAG, "loX : " + loX + " loY : " + loY + " loZ : " + loZ);
            boolean moIsMin=false;
            if (loAccelerationReader <= 6.0) {
                moIsMin = true;
                Log.i(TAG, "min");
            }

            int i=0;
            boolean moIsMax = false;
            if (moIsMin) {
                i++;
                Log.i(TAG, " loAcceleration : " + loAccelerationReader);
                if (loAccelerationReader >= 30) {
                    long llCurrentTime = System.currentTimeMillis();
                    long llTimeDiff = llCurrentTime - mlPreviousTime;
                    Log.i(TAG, "loTime :" + llTimeDiff);
                    if (llTimeDiff >= 10) {
                        moIsMax = true;
                        Log.i(TAG, "max");
                    }
                }

            }
            if (foEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                loX = foEvent.values[0];
                loY = foEvent.values[1];
                loZ = foEvent.values[2];

                loAccelerationReader = Math.sqrt(Math.pow(loX, 2)
                        + Math.pow(loY, 2)
                        + Math.pow(loZ, 2));

                DecimalFormat precision = new DecimalFormat("0.00");
                double ldAccRound = Double.parseDouble(precision.format(loAccelerationReader));

                if (ldAccRound > 0.3d && ldAccRound < 0.5d) {
                    //Do your stuff
                    Toast.makeText(this, "FALL DETECTED", Toast.LENGTH_LONG).show();
                }
            }

            if (moIsMin && moIsMax) {
                Log.i(TAG, "loX : " + loX + " loY : " + loY + " loZ : " + loZ);
                Log.i(TAG, "FALL DETECTED!!!!!");
                Toast.makeText(this, "FALL DETECTED!!!!!", Toast.LENGTH_LONG).show();
                i = 0;
                moIsMin = false;
                moIsMax = false;
            }

            if (i > 5) {
                i = 0;
                moIsMin = false;
                moIsMax = false;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}