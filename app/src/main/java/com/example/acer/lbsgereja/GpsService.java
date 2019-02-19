package com.example.acer.lbsgereja;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

/**
 * Created Veronika Apriani
     */

public class GpsService extends Service implements LocationListener {
    private final Context _context;
    boolean GPSEnable = false;
    boolean GetLocation = false;
    Location location;
    double lat;
    double lng;
    private static final long MIN_JARAK_GPS_UPDATE = 5;
    private static final long MIN_WAKTU_GPS_UPDATE = 1000 * 60 * 1;
    protected LocationManager LocationBasedService;

    public GpsService(Context context) {
        _context = context;
        getLocation();
    }

    private Location getLocation() {
        try {
            LocationBasedService = (LocationManager) _context.getSystemService(LOCATION_SERVICE);

            GPSEnable = LocationBasedService.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!GPSEnable)
            {

// tidak ada koneksi ke GPS dan Jaringan
            }
            else {
                GetLocation = true;
                if (GPSEnable) {
                    if (location == null){
                        LocationBasedService.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_WAKTU_GPS_UPDATE,MIN_JARAK_GPS_UPDATE, this);
                        if (LocationBasedService != null) {
                            location = LocationBasedService.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                lat = location.getLatitude();
                                lng = location.getLongitude();
                            }}}}
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }



    @Override
    public void onLocationChanged(Location location)
    {
// TODO Auto-generated method stub
    }
    @Override
    public void onProviderDisabled(String provider) {
// TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
// TODO Auto-generated method stub
    }
    @Override

    public void onStatusChanged(String provider, int status, Bundle extras)
    {
// TODO Auto-generated method stub
    }

    @Override
    public IBinder onBind(Intent intent)
    {
// TODO Auto-generated method stub
        return null;
    }

    public double getLatitude()
    {
       if (location != null)
            lat = location.getLatitude();
        return lat;
    }

    public void setLatitude(double lat)
    {
        this.lat = lat;
    }

    public double getLongitude()
    {
        if (location != null)
            lng = location.getLongitude();
        return lng;

    }
    public void setLongitude(double lng)
    {
        this.lng = lng;
    }

    public boolean GetLocation()
    {
        return this.GetLocation;
    }

    public void showSettingAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(_context);
        alertDialog.setTitle("GPS Setting");
        alertDialog.setMessage("GPS tidak aktif. Mau masuk ke setting Menu?");
        alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener()

        {

            @Override

            public void onClick(DialogInterface dialog, int which)

            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
               _context.startActivity(intent);
            }

        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()

        {

            @Override

            public void onClick(DialogInterface dialog, int which)

            {
                dialog.cancel();
            }

        });
        alertDialog.show();
    }



    public void stopUsingGPS()
    {
        if (LocationBasedService != null)
            LocationBasedService.removeUpdates(GpsService.this);
    }
}
