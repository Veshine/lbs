package com.example.acer.lbsgereja;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by acer on 2017.
 */

public class Halamanberita extends Activity {

    private ProgressDialog pDialog;
    JSONParser2 jParser = new JSONParser2();
    ArrayList<HashMap<String, String>> DaftarBerita = new
            ArrayList<HashMap<String, String>>();
    private static String url_berita = "http://lbsgereja.esy.es/android/beritacoba2.php";
    public static final String TAG_ID = "id";
    public static final String TAG_JUDUL = "judul";
    public static final String TAG_GAMBAR = "gambar";
    JSONArray string_json = null;
    ListView list;
    LazyAdapter adapter;

    //Menangkap Intent dari Halaman sebelumnya
    public String nm,kode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halamanberita);

        //Deklarasi Intent
        Bundle bundle = getIntent().getExtras();
        nm=bundle.getString("nmgrj");
        kode=bundle.getString("idgrj");
        //Deklarasi Intent

        DaftarBerita = new ArrayList<HashMap<String, String>>();
        new AmbilData().execute();
        list = (ListView) findViewById(R.id.list);

        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map = DaftarBerita.get(position);
                // Starting new intent
                Intent in = new Intent(getApplicationContext(), Halamandetailberita.class);
                in.putExtra(TAG_ID, map.get(TAG_ID));
                in.putExtra(TAG_GAMBAR, map.get(TAG_GAMBAR));
                startActivity(in);
            }
        });
    }
    public void SetListViewAdapter(ArrayList<HashMap<String,
            String>> berita) {
        adapter = new LazyAdapter(this, berita);
        list.setAdapter(adapter);
    }
    class AmbilData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Halamanberita.this);
            pDialog.setMessage("Menampilkan Data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new
                    ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("id_gereja", kode));

            JSONObject json = jParser.makeHttpRequest(url_berita,
                    "GET", params);
            Log.i("Ini nilai json ", ">" + json);
            try {
                string_json = json.getJSONArray("berita");
                for (int i = 0; i < string_json.length(); i++) {
                    JSONObject c = string_json.getJSONObject(i);
                    String id_berita = c.getString(TAG_ID);
                    String judul = c.getString(TAG_JUDUL);
                    String link_image = c.getString(TAG_GAMBAR);
                    HashMap<String, String> map = new HashMap<String,
                            String>();
                    map.put(TAG_ID, id_berita);
                    map.put(TAG_JUDUL, judul);
                    map.put(TAG_GAMBAR, link_image);
                    DaftarBerita.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            //Intent
            TextView judul=(TextView) findViewById(R.id.namagereja);
            judul.setText(nm);
            //Intent

            runOnUiThread(new Runnable() {
                public void run() {
                    SetListViewAdapter(DaftarBerita);
                }
            });
        }
    }
}