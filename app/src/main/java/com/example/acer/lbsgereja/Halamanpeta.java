package com.example.acer.lbsgereja;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by acer on 2017.
 */

public class Halamanpeta extends FragmentActivity implements OnMapReadyCallback {

    MapFragment mapFragment;
    GoogleMap gMap;
    MarkerOptions markerOptions = new MarkerOptions();
    CameraPosition cameraPosition;
    LatLng center, latLng;
    String title;
    String snippet;
    double currentlat, currentlng;

    public static final String TITLE = "nama_gereja";
    public static final String SNIPPET = "id_gereja";
    public static final String ID = "alamat";
    public static final String LAT = "lattitude";
    public static final String LNG = "longitude";

    private String url = "http://lbsgereja.esy.es/android/markerpeta.php";

    String tag_json_obj = "json_obj_req";

    //Dekrasikan tombol
    Button daftargrj, grjsekitar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peta);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //deklarasikan tombol jadwal
        daftargrj = (Button) findViewById(R.id.tomboldaftargrj);
        daftargrj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), Halamankategori.class);
                startActivity(i);
            }
        });

        //deklarasikan tombol peta gereja sekitar
        grjsekitar = (Button) findViewById(R.id.tombolgrjsekitar);
        grjsekitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), Halamangerejasekitar.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        // Mengarahkan ke pusat Pontianak (UNTAN)
        center = new LatLng(-0.041592, 109.336548);
        cameraPosition = new CameraPosition.Builder().target(center).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        getMarkers();

    }

    private void addMarker(LatLng latlng, final String title, final String snippet) {
        markerOptions.position(latlng);
        markerOptions.title(title);
        markerOptions.snippet(snippet);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));

        gMap.addMarker(markerOptions);

        gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();

                //Mengirim id ke halaman selanjutnya saat marker di klik
                Intent intent = new Intent(Halamanpeta.this, Halamaninformasigereja.class);
                String replace_string_first = marker.getSnippet().replace(" ", "_");
                intent.putExtra("idg", replace_string_first);
                    startActivity(intent);
                //Mengirim id ke halaman selanjutnya saat marker di klik
            }
        });

    }

    // Fungsi get JSON marker
    private void getMarkers() {
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String getObject = jObj.getString("markerpeta");
                    JSONArray jsonArray = new JSONArray(getObject);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        title = jsonObject.getString(TITLE);
                        snippet = jsonObject.getString(SNIPPET) +("--")+ jsonObject.getString(ID);
                        latLng = new LatLng(Double.parseDouble(jsonObject.getString(LAT)),
                                Double.parseDouble(jsonObject.getString(LNG)));

                        // Menambah data marker untuk di tampilkan ke google map
                        addMarker(latLng, title, snippet);
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
                Toast.makeText(Halamanpeta.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Appcontroller.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

}
