package com.example.gpsappananthschiff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
LocationManager locationManager;
TextView lat, lon, address, distance1, timetext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timetext=findViewById(R.id.textView4);
        lat = findViewById(R.id.textView2);
        lon=findViewById(R.id.textView);
        distance1=findViewById(R.id.textView3);
        address=findViewById(R.id.addy);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);//asks user for location permission
            return;
        }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, this);

        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, this);

    }
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED )
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, this);
                break;
            default:
                break;
        }
    }
        private Location LastLocation;
        private long time = SystemClock.elapsedRealtime();
        private float distance;
        @Override
        public void onLocationChanged(@NonNull Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Log.d("TAG",String.valueOf(longitude));
            Log.d("TAG",String.valueOf(latitude));
            lat.setText("lat "+String.valueOf(latitude));
            lon.setText("long "+String.valueOf(longitude));

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addressList;
            try {
                addressList=geocoder.getFromLocation(latitude,longitude,1);
                String addy = addressList.get(0).getAddressLine(0);
                address.setText(addy);

            } catch (IOException e) {
                e.printStackTrace();
            }
            float x=0;
            if(LastLocation!=null) {
                x=location.distanceTo(LastLocation);
                distance +=x;
                distance*=0.000621371;
                distance =(float)(Math.round(distance*100.0)/100.0);
                distance1.setText(String.valueOf(distance)+" miles");
              //  distance= Float.parseFloat(distance1.getText().toString());
                long eTime = SystemClock.elapsedRealtime() - time;
                double elapsedTime = (double) eTime / 1000.0;
                timetext.setText(String.valueOf(elapsedTime) + " seconds");
            }

            LastLocation=location;
            distance+=x;

        }



}