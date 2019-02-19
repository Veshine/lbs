package com.example.acer.lbsgereja;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.acer.lbsgereja.floydWarshall.AdjMatrixEdgeWeightedDigraph;
import com.example.acer.lbsgereja.floydWarshall.DirectedEdge;
import com.example.acer.lbsgereja.floydWarshall.FloydWarshall;
import com.example.acer.lbsgereja.helper.MapDrawer;
import com.example.acer.lbsgereja.model.Graph;
import com.example.acer.lbsgereja.model.Node;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

/**
 * Created by acer on 2017.
 */

public class Halamanrute extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleMap googleMap;

    public String indsim, na;

    double latuser, lonuser;

    int h;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_rute);

        /** MENANGKAP INTENT DARI HALAMAN INFORMASI*/
        Bundle b = getIntent().getExtras();
        indsim = b.getString("namasimpul");
        na = b.getString("namagereja");
        Log.d("id_simpul", indsim);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading Data ...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(true);

        h = Integer.parseInt(indsim);
        try {
            initializeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // new AmbilData().execute();
    }

    private void initializeMap() {
        if (googleMap == null) {
            SupportMapFragment supportMapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.rute));
            supportMapFragment.getMapAsync(this);
            CekGPS();
        }
    }

    /** CEK GPS DAN NAMPILKAN KOORDINAR USER*/
    public void CekGPS() {
        try {
            // Get the location manager
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("info");
                builder.setMessage("Apakah anda akan mengaktifkan GPS?");
                builder.setPositiveButton("Ya",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub
                                Intent i = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(i);

                            }
                        });
                builder.setNegativeButton("Tidak",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            }
        } catch (Exception e) {
            // TODO: handle exception

        }
        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getBaseContext());
        if (status != ConnectionResult.SUCCESS) {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
                    requestCode);
            dialog.show();
        } else {
            Criteria criteria = new Criteria();
            LocationManager locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            String provider = locationmanager.getBestProvider(criteria, true);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationmanager.getLastKnownLocation(provider);

            if (location != null) {
                onLocationChanged(location);
            }

            locationmanager.requestLocationUpdates(provider, 500, 0, this);
            LatLng lokasi = new LatLng(latuser, lonuser);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lokasi, 12));
            googleMap.addMarker(new MarkerOptions()
                    .position(lokasi)
                    .title("posisi anda")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_user)));

        }

    }

    //Ambil data graph berdasarkan simpul yang dituju di server
    void loadRuteTerpendek(int idSimpulTujuan){
        JsonRequest jsonRequest = new JsonObjectRequest(GET, "http://lbsgereja.esy.es/android/rute_terpendek.php?id_simpul_tujuan=" + idSimpulTujuan,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mProgressDialog.dismiss();
                        try {
                            int kode = response.getInt("kode");
                            if (kode==1){
                                //Parsing json
                                JSONArray jsonArrayGraph = response.getJSONArray("data");
                                List<Graph> list = new ArrayList<>(jsonArrayGraph.length());
                                int indexTujuanTemp = 0;
                                for (int i = 0; i < jsonArrayGraph.length(); i++) {
                                    JSONObject jsonObject = jsonArrayGraph.getJSONObject(i);
                                    Graph g = new Graph();
                                    g.setId(jsonObject.getInt("id_graph"));
                                    Node nodeSimpulAwal = new Node();
                                    nodeSimpulAwal.setIndexSimpul(jsonObject.getInt("index_simpul_awal"));
                                    String koordinatAwal = jsonObject.getString("koordinat_simpul_awal");
                                    nodeSimpulAwal.setLatitude(Double.parseDouble(koordinatAwal.split(",")[0]));
                                    nodeSimpulAwal.setLongitude(Double.parseDouble(koordinatAwal.split(",")[1]));
                                    g.setSimpulAwal(nodeSimpulAwal);
                                    Node nodeSimpulAkhir = new Node();
                                    int indexTujuan = jsonObject.getInt("index_simpul_akhir");
                                    nodeSimpulAkhir.setIndexSimpul(indexTujuan);
                                    if (indexTujuan>indexTujuanTemp){
                                        indexTujuanTemp = indexTujuan;
                                    }
                                    String koordinatAkhir = jsonObject.getString("koordinat_simpul_akhir");
                                    nodeSimpulAkhir.setLatitude(Double.parseDouble(koordinatAkhir.split(",")[0]));
                                    nodeSimpulAkhir.setLongitude(Double.parseDouble(koordinatAkhir.split(",")[1]));
                                    g.setSimpulAkhir(nodeSimpulAkhir);
                                    g.setJalur(jsonObject.getString("jalur"));
                                    g.setJarak(jsonObject.getDouble("jarak"));
                                    list.add(g);
                                }
                                calculate(list, indexTujuanTemp);
                            }else{
                                Toast.makeText(Halamanrute.this, "Tidak ada data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(Halamanrute.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        mProgressDialog.show();
        Appcontroller.getInstance().addToRequestQueue(jsonRequest);
    }

    //Menghitung rute terpendek dari data yang telah didapat
    void calculate(List<Graph> list, int indexTujuan){
        AdjMatrixEdgeWeightedDigraph G = new AdjMatrixEdgeWeightedDigraph(list.size());
        for (int i = 0; i < list.size(); i++) {
            Graph g = list.get(i);
            G.addEdge(new DirectedEdge(g.getSimpulAwal().getIndexSimpul(), g.getSimpulAkhir().getIndexSimpul(), g.getJarak()));
        }
        FloydWarshall floydWarshall = new FloydWarshall(G);
        List<LatLng> jalur = new ArrayList<>();
        for (DirectedEdge directedEdge : floydWarshall.path(0, indexTujuan)) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getSimpulAwal().getIndexSimpul() == directedEdge.from() && list.get(i).getSimpulAkhir().getIndexSimpul() == directedEdge.to()) {
                    try {
                        JSONArray jsonArrayJalur = new JSONObject(list.get(i).getJalur()).getJSONArray("coordinates");
                        for (int j = jsonArrayJalur.length()-1; j>=0; j--) {
                            JSONArray jsonCoordinate = jsonArrayJalur.getJSONArray(j);
                            jalur.add(new LatLng(jsonCoordinate.getDouble(0), jsonCoordinate.getDouble(1)));
                            Log.d("Coordinate", directedEdge.from() + "->" + directedEdge.to() + jsonCoordinate.getDouble(0) + ":" + jsonCoordinate.getDouble(1));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            //Menampilkan rute pada map dari hasil perhitungan
            MapDrawer.draw(googleMap, jalur);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latuser=location.getLatitude();
        lonuser=location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

/**
    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
 */

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-0.048062497701191635, 109.31911736726761), 19));
        loadRuteTerpendek(h);
    }
}
