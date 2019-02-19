package com.example.acer.lbsgereja;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by acer on 2017.
 */

public class Halamanjadwal extends Activity {
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    AlertDialogManager alert = new AlertDialogManager();

    JSONArray gereja = null;

    ListView listView;
    public String nm, kode;
    ListAdapter adapter;

    ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal);

        listView = (ListView) findViewById(R.id.daftargereja);

        Bundle bundle = getIntent().getExtras();
        nm = bundle.getString("nmgrj");
        kode = bundle.getString("idgrj");

        cekInternet();

        //new AmbilData().execute();
    }

    public class AmbilData extends AsyncTask<String, String, String> {
        ArrayList<HashMap<String, String>> data_map = new ArrayList<HashMap<String, String>>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Halamanjadwal.this);
            pDialog.setMessage("Menampilkan Data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            String link_url = "http://lbsgereja.esy.es/android/daftar_jadwal.php?kode=" + kode;
            //String link_url = "http://lbsgereja.esy.es/android/a.php";

            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.AmbilJson(link_url);

            try {
                gereja = json.getJSONArray("jadwal");

                for(int i = 0; i < gereja.length(); i++){
                    JSONObject ar = gereja.getJSONObject(i);

                    HashMap<String, String> map = new HashMap<String, String>();

                    String nmjdw = ar.getString("nama").trim();
                    String desjdw = ar.getString("deskripsi").trim();
                    String hrwktjdw = ar.getString("hari")+" | "+ar.getString("waktu");

                    map.put("nama", nmjdw);
                    map.put("deskripsi", desjdw);
                    map.put("hari", hrwktjdw);

                    data_map.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            TextView judul=(TextView) findViewById(R.id.namagereja);
            judul.setText(nm);

            runOnUiThread(new Runnable() {
                public void run() {
                    adapter=new SimpleAdapter(getApplicationContext(),
                            data_map,R.layout.activity_daftarjadwal,
                            new String[] {"deskripsi", "nama", "hari"},
                            new int[] { R.id.desjdw, R.id.namajdw, R.id.hariwaktujdw});
                    listView.setAdapter(adapter);

                }
            });

        }
    }

    public void cekInternet() {
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {

            new AmbilData().execute();

        } else {

            alert.showAlertDialog(Halamanjadwal.this, "Peringatan",
                    "Maaf, Silahkan cek koneksi internet.",
                    false);
        }
    }
}