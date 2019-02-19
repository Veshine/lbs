package com.example.acer.lbsgereja;

/**
 * Created by acer on 2017.
 */


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Halamandetailberita extends Activity{
    public ImageLoader imageLoader;
    {
        imageLoader = new ImageLoader(null);
    }

    JSONArray string_json = null;
    String idberita;
    private ProgressDialog pDialog;
    JSONParser2 jsonParser = new JSONParser2();
    public static final String TAG_ID = "id";
    public static final String TAG_JUDUL = "judul";
    public static final String TAG_ISI = "isi";
    public static final String TAG_GAMBAR = "gambar";
    private static final String url_detail_berita =
            "http://lbsgereja.esy.es/android/beritadetailcoba.php";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halamanberitadetail);

        Intent i = getIntent();
        idberita = i.getStringExtra(TAG_ID);
        Toast.makeText(getApplicationContext(),
                "id berita = " + idberita,
                Toast.LENGTH_SHORT).show();
        new AmbilDetailBerita().execute();

    }
    class AmbilDetailBerita extends AsyncTask<String, String,
            String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Halamandetailberita.this);
            pDialog.setMessage("Menampilkan Data ... !");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... params) {
            try {
                List<NameValuePair> params1 = new
                        ArrayList<NameValuePair>();
                params1.add(new
                        BasicNameValuePair("id_berita",idberita));
                JSONObject json = jsonParser.makeHttpRequest(
                        url_detail_berita, "GET", params1);
                string_json = json.getJSONArray("berita");
                runOnUiThread(new Runnable() {
                    public void run() {
                        ImageView thumb_image = (ImageView)
                                findViewById(R.id.gambardetail);
                        TextView judul = (TextView)
                                findViewById(R.id.judul);
                        //TextView detail = (TextView)
                        findViewById(R.id.detail);
                        TextView isi = (TextView)
                                findViewById(R.id.isi);
                        try {
                            // ambil objek member pertama dari JSON Array
                            JSONObject ar =
                                    string_json.getJSONObject(0);
                            String judul_d = ar.getString(TAG_JUDUL);
                            String isi_d = ar.getString(TAG_ISI);
                            String url_detail_image = ar.getString(TAG_GAMBAR);
                            judul.setText(judul_d);
                            isi.setText(isi_d);


//                            imageLoader.DisplayImage(ar.getString(TAG_GAMBAR),thumb_image);
                            Picasso.with(getApplicationContext())
                                    .load(url_detail_image)
                                    .error(R.drawable.no_image)
                                    .into(thumb_image);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }
    }
}
