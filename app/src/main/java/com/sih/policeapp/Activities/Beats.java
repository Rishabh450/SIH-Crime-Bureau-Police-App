package com.sih.policeapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sih.Utils.SendNotification;
import com.sih.policeapp.MainActivity;
import com.sih.policeapp.R;
import com.sih.policeapp.Services.MyService;

import java.util.List;

public class Beats extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener
{
    SupportMapFragment mapFragment;
    GoogleMap mMap;
    Switch patrol;
    Intent mServiceIntent;
    Location lastKnown;
    ImageView emergency;
    String vehicle = "rishabhKaGaadi";

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service status", "Running");
                return true;
            }
        }
        Log.i("Service status", "Not running");
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beats);
        if(FirebaseAuth.getInstance().getCurrentUser()==null) {
            Log.e("ak47", "user null");
            Intent intent = new Intent(Beats.this, Login.class);
            startActivity(intent);
        }
        else{
            Log.e("ak47", "not null");
        vehicle=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
           // vehicle= FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            if(vehicle.contains(" "))
            vehicle=vehicle.substring(0,vehicle.indexOf(' '));}

        patrol = findViewById(R.id.patrol);
        MyService mYourService = new MyService();

        if(isMyServiceRunning(mYourService.getClass()))
            patrol.setChecked(true);
        emergency=findViewById(R.id.emergency);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Beats").child(vehicle).child("Emergency").setValue("Yes");
                DatabaseReference myref=FirebaseDatabase.getInstance().getReference().child("PoliceNotif");
                myref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds :dataSnapshot.getChildren())
                        {
                            String key=ds.getValue(String.class);
                            SendNotification sendNotification=new SendNotification("Under Emergency",vehicle,key);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                DatabaseReference mUserRef=FirebaseDatabase.getInstance().getReference().child("PoliceUser");


            }
        });
        mapFragment.getMapAsync(this);
        patrol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                if (isChecked) {
                   /* Intent servIntent = new Intent("com.sih.policeapp.LONGRUNSERVICE");
                    servIntent.setPackage(Beats.this .getPackageName());

                    startService(servIntent);*/
                    MyService mYourService = new MyService();
                    mServiceIntent = new Intent(Beats.this, mYourService.getClass());
                    if (!isMyServiceRunning(mYourService.getClass())) {
                        startService(mServiceIntent);
                    }
                    else
                        stopService(mServiceIntent);

             /*       final DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("Beats").child(vehicle).child("Location");
                    LocationManager locationManager = (LocationManager) Beats.this.getSystemService(Context.LOCATION_SERVICE);
                    LocationListener locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {

                            myref.setValue(location.getLatitude() + "||" + location.getLongitude());


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

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    Activity#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                lastKnown = getLastKnownLocation();
                            }
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                            if (lastKnown != null) {
                               // lastKnown = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                myref.setValue(lastKnown.getLatitude() + "||" + lastKnown.getLongitude());
                            }


                        }
                    }*/
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
      //  stopService(mServiceIntent);
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("readyyy", "yes");
        DatabaseReference beat = FirebaseDatabase.getInstance().getReference("Beats");
        beat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot vehicle : dataSnapshot.getChildren()) {

                    String location = vehicle.child("Location").getValue(String.class);
                    Log.d("locationfro", location.substring(0, location.indexOf('|')));
                    Log.d("locationfro", location.substring(location.lastIndexOf('|') + 1));
                    double lat = Double.parseDouble(location.substring(0, location.indexOf('|')));
                    double lon = Double.parseDouble(location.substring(location.lastIndexOf('|') + 1));
                    // LatLng sydney = new LatLng( Double.parseDouble(location.substring(0,location.indexOf('|'))) ,Double.parseDouble(location.substring(location.lastIndexOf('|')+1)) );
                    LatLng sydney = new LatLng(lat, lon);

                    final MarkerOptions marker = new MarkerOptions().position(sydney).title(vehicle.getKey());
                    //  Drawable i=;
                    final Bitmap bitmap = drawableToBitmap(getDrawable(R.drawable.car_foreground));
                    final Bitmap bitmap1 = drawableToBitmap(getDrawable(R.drawable.carred_foreground));
                    if(!vehicle.child("Emergency").getValue(String.class).equals("Yes")){
                    marker.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                        Marker m= mMap.addMarker(marker);}
                    else
                    {
                        marker.icon(BitmapDescriptorFactory.fromBitmap(bitmap1));
                        mMap.addMarker(marker);

                    }




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


                mMap.setBuildingsEnabled(true);
                mMap.clear();
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions marker = new MarkerOptions().position(sydney).title("Your current location");
                mMap.addMarker(marker);


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

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        } else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            lastKnown = getLastKnownLocation();

                mMap.clear();
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                LatLng sydney = new LatLng(lastKnown.getLatitude(), lastKnown.getLongitude());
                MarkerOptions marker = new MarkerOptions().position(sydney).title("Your current location");
                mMap.addMarker(marker);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));

        }


        // Add a marker in Sydney and move the camera

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private Location getLastKnownLocation() {
        LocationManager mLocationManager;
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                ActivityCompat.requestPermissions(Beats.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
                ActivityCompat.requestPermissions(Beats.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},0);



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
    public Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;


        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
       String vehicle= marker.getTitle();

        return false;
    }
}
