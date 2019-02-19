package com.example.acer.lbsgereja.helper;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Created on   : 11/12/2017
 * Developed by : Hendrawan Adi Wijaya
 * Github       : https://github.com/andevindo
 * Website      : http://www.andevindo.com
 */

public class MapDrawer {

    public static void draw(GoogleMap googleMap, List<LatLng> location){
        googleMap.clear();
        googleMap.addPolyline(new PolylineOptions()
                .addAll(location)
                .width(12)
                .color(Color.parseColor("#05b1fb"))//Google maps blue color
                .geodesic(true));
        drawMarker(googleMap, location.get(location.size()-1), location.get(0));
    }

    private static void drawMarker(GoogleMap googleMap, LatLng awal, LatLng tujuan){
        googleMap.addMarker(new MarkerOptions().position(awal).title("Posisi Awal"));
        googleMap.addMarker(new MarkerOptions().position(tujuan).title("Posisi Tujuan"));
    }

}
