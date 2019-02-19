package com.example.acer.lbsgereja.filter;

import android.app.AlertDialog;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.acer.lbsgereja.AlertDialogManager;
import com.example.acer.lbsgereja.ConnectionDetector;
import com.example.acer.lbsgereja.GpsService;
import com.example.acer.lbsgereja.Halamangerejasekitar;
import com.example.acer.lbsgereja.Halamaninformasigereja;
import com.example.acer.lbsgereja.Halamankategori;
import com.example.acer.lbsgereja.Halamanpencarian;
import com.example.acer.lbsgereja.Halamanutama;
import com.example.acer.lbsgereja.JSONParser3;
import com.example.acer.lbsgereja.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Veronika Apriani
 */

public class Ptkbarat extends FragmentActivity implements LocationListener, OnMapReadyCallback {
    private GoogleMap GMap;
    ProgressDialog pDialog;

    GpsService gps;

    JSONArray college1 = null;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;

    double currentlat, currentlng;

    AlertDialogManager alert = new AlertDialogManager();

    HashMap<String, String> maps;

    private LocationManager mLocationManager = null;
    private Marker mCurrentPosition = null;

    //NAVIGATION SLIDE
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petasekitar);

        //NAVIGATION SLIDE
        // Menginisiasi Toolbar dan mensetting sebagai actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar); //hidden

        // Menginisiasi  NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Memeriksa apakah item tersebut dalam keadaan dicek  atau tidak,
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                //Menutup  drawer item klik
                drawerLayout.closeDrawers();
                //Memeriksa untuk melihat item yang akan dilklik dan melalukan aksi
                switch (menuItem.getItemId()){
                    // pilihan menu item navigasi akan menampilkan pesan toast klik kalian bisa menggantinya
                    //dengan intent activity
                    case R.id.navigation1:  //Halamanutama
                        Intent ia=new Intent(getApplicationContext(),Halamanutama.class);
                        startActivity(ia);
                        Toast.makeText(getApplicationContext(), "Beranda Telah Dipilih", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation2:  //Halamankategori
                        Intent ic=new Intent(getApplicationContext(),Halamankategori.class);
                        startActivity(ic);
                        Toast.makeText(getApplicationContext(),"Daftar Telah Dipilih",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation11: //Halamanpencarian
                        Intent ik=new Intent(getApplicationContext(),Halamanpencarian.class);
                        startActivity(ik);
                        Toast.makeText(getApplicationContext(),"Pencarian Telah Dipilih",Toast.LENGTH_SHORT).show();
                        return true;

//Lihat Lokasi Berdasarkan:
                    case R.id.navigation3: //KecKota
                        Intent intentd=new Intent(getApplicationContext(),Ptkkota.class);
                        startActivity(intentd);
                        Toast.makeText(getApplicationContext(),"Pontianak Kota",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation4: //KecSelatan
                        Intent intente=new Intent(getApplicationContext(),Ptkselatan.class);
                        startActivity(intente);
                        Toast.makeText(getApplicationContext(),"Pontianak Selatan",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation5: //KecUtara
                        Intent intentf=new Intent(getApplicationContext(),Ptkutara.class);
                        startActivity(intentf);
                        Toast.makeText(getApplicationContext(),"Pontianak Utara",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation6: //KecBarat
                        Intent intentg=new Intent(getApplicationContext(),Ptkbarat.class);
                        startActivity(intentg);
                        Toast.makeText(getApplicationContext(),"Pontianak Barat",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation7: //KecTimur
                        Intent intenth=new Intent(getApplicationContext(),Ptktimur.class);
                        startActivity(intenth);
                        Toast.makeText(getApplicationContext(),"Pontianak Timur",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation8: //KecSelatan
                        Intent intenti=new Intent(getApplicationContext(),Ptktenggara.class);
                        startActivity(intenti);
                        Toast.makeText(getApplicationContext(),"Pontianak Tenggara",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation9: //JenisKatolik
                        Intent intentj=new Intent(getApplicationContext(),Katolik.class);
                        startActivity(intentj);
                        Toast.makeText(getApplicationContext(),"Gereja Katolik",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation10: //JenisKristen
                        Intent intentk=new Intent(getApplicationContext(),Kristen.class);
                        startActivity(intentk);
                        Toast.makeText(getApplicationContext(),"Gereja Kristen",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation12: //Semua Lokasi
                        Intent intentl=new Intent(getApplicationContext(),Halamangerejasekitar.class);
                        startActivity(intentl);
                        Toast.makeText(getApplicationContext(),"Semua Lokasi",Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(),"Kesalahan Terjadi ",Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });

        // Menginisasi Drawer Layout dan ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                // Kode di sini akan merespons setelah drawer menutup disini kita biarkan kosong
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                //  Kode di sini akan merespons setelah drawer terbuka disini kita biarkan kosong
                super.onDrawerOpened(drawerView);
            }
        };

        //Mensetting actionbarToggle untuk drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //memanggil synstate
        actionBarDrawerToggle.syncState();

        //NAVIGATION SLIDE


        try {
            initializeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // new AmbilData().execute();

        gps = new GpsService(Ptkbarat.this);
        if (gps.GetLocation())
        {
            double lat = gps.getLatitude();
            double lng = gps.getLongitude();

            new AmbilData().execute(String.valueOf(lat), String.valueOf(lng));
        }
        else
        {
            gps.showSettingAlert();
        }
    }

    private void initializeMap() {
        if (GMap == null) {
            SupportMapFragment supportMapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapsekitar));
            supportMapFragment.getMapAsync(this);
        }
    }

    public void CekGPS() {
        try {
            // Get the location manager
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("info");
                builder.setMessage("GPS tidak aktif. Mau masuk ke setting Menu?");
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
            LatLng lokasi = new LatLng(currentlat, currentlng);

            GMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lokasi, 12));
            GMap.addMarker(new MarkerOptions()
                    .position(lokasi)
                    .title("Posisi Anda")
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_user)));
            CircleOptions mOptions = new CircleOptions()
                    .center(new LatLng(currentlat, currentlng)).radius(1500)
                    .strokeColor(0x110000FF).strokeWidth(1).fillColor(0x110000FF);
            GMap.addCircle(mOptions);
            if (mCurrentPosition != null)
                mCurrentPosition.remove();

        }

    }

    @Override
    public void onLocationChanged(Location location) {
        currentlat=location.getLatitude();
        currentlng=location.getLongitude();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GMap = googleMap;
        CekGPS();
    }

    @Override
    public void onBackPressed() {
    }

    //menampilkan marker sekitar user
    public class AmbilData extends AsyncTask<String, String, String> {

        ArrayList<HashMap<String, String>> dataList1 = new ArrayList<HashMap<String, String>>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Ptkbarat.this);
            pDialog.setMessage("Menampilkan Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {

            //
            String lat = args[0];
            String lng = args[1];
            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("lat", lat));
            param.add(new BasicNameValuePair("lng",lng));

            String url;
            url = "http://lbsgereja.esy.es/android/grjnear-ptkbarat.php";

            JSONParser3 jParser = new JSONParser3 ();
            JSONObject json = jParser.makeHttpRequest(url,"GET",param);

            try {
                college1 = json.getJSONArray("dis");

                Log.e("error", json.getString("success"));

                for (int i = 0; i <= college1.length(); i++) {

                    JSONObject c = college1.getJSONObject(i);

                    maps = new HashMap<String, String>();

                    String id_2 = c.getString("id_gereja").trim();
                    String nama_2 = c.getString("nama_gereja").trim();
                    String alm_2 = c.getString("alamat").trim();
                    String id_1 = c.getString("id_simpul").trim();
                    String dis_1 = c.getString("jarak").trim();
                    String lat1 = c.getString("lattitude").trim();
                    String lon1 = c.getString("longitude").trim();

                    maps.put("id_gereja", id_2);
                    maps.put("nama_gereja", nama_2);
                    maps.put("alamat", alm_2);
                    maps.put("id_simpul", id_1);
                    maps.put("jarak", dis_1);
                    maps.put("lattitude", lat1);
                    maps.put("longitude", lon1);
                    dataList1.add(maps);

                }

            } catch (JSONException e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pDialog.dismiss();

            for (int x = 0; x < dataList1.size(); x = x + 1) {
                double lat = Double.valueOf(dataList1.get(x).get(
                        "lattitude"));
                double lon = Double.valueOf(dataList1.get(x).get(
                        "longitude"));
                LatLng skt = new LatLng(lat, lon);
                String namam = dataList1.get(x).get("nama_gereja");
                String idg = dataList1.get(x).get("id_gereja");
                String jrkg = dataList1.get(x).get("jarak");
                GMap.addMarker(new MarkerOptions()
                        .position(skt)
                        .title(namam)
                        .snippet(idg + "-" + " jarak: " + jrkg + " km")
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.marker)));
            }

            GMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    //Mengirim id ke halaman selanjutnya saat marker di klik
                    Intent intent = new Intent(Ptkbarat.this, Halamaninformasigereja.class);
                    String replace_string_first = marker.getSnippet().replace(" ", "_");
                    intent.putExtra("idg", replace_string_first);
                    startActivity(intent);
                    //Mengirim id ke halaman selanjutnya saat marker di klik
                }
            });

        }
    }


}


