package com.example.acer.lbsgereja;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by acer on 2017.
 */

public class Halamaninformasigereja extends Activity {
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    AlertDialogManager alert = new AlertDialogManager();

    ProgressDialog pDialog;
    //String status = "1";

    JSONArray college = null;
    public String idgrj;

    //Dekrasikan tombol
    Button jadwal, berita, rute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasigereja);

        Bundle b = getIntent().getExtras();
        idgrj = b.getString("idg");
        cekInternet();

        // Deklarasikan Tombol yang akan digunakan
        //Buttonkembalimenuutama
        Button btn_kembaliawal = (Button) findViewById(R.id.btn_awal);
        btn_kembaliawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //menampilkan halaman kategori
                Intent i = new Intent(getApplication(), Halamanutama.class);
                startActivity(i);
            }
        });

        //deklarasikan tombol jadwal
        jadwal = (Button) findViewById(R.id.tomboljadwal);
        jadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), Halamanjadwal.class);
                startActivity(i);
            }
        });
        //deklarasikan tombol jadwal

        //deklarasi tombol berita
        berita = (Button) findViewById(R.id.tombolberita);
        berita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent b = new Intent(getApplicationContext(), Halamanberita.class);
                startActivity(b);
            }
        });
        //deklarasi tombol berita

        //deklarasikan tombol rute
         rute = (Button) findViewById(R.id.tombolrute);
         rute.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
        Intent c = new Intent(getApplicationContext(), Halamanrute.class);
        startActivity(c);
        }
        });
        //deklarasikan tombol rute

    }

    public class AmbilData extends AsyncTask<String, String, String> {

        ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

        String nmgereja, almgereja, kapgereja, desgereja, idsim, latit, longit, imamgrj, telp, gbr ;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Halamaninformasigereja.this);
            pDialog.setMessage("Menampilkan Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            String url;
            url = "http://lbsgereja.esy.es/android/a.php?kodegrj="+idgrj;
            //url = "http://lbsgereja.esy.es/android/a.php";

            JSONParser jParser = new JSONParser();

            JSONObject json = jParser.AmbilJson(url);
            try {
                college = json.getJSONArray("gereja");

                //     String success = json.getString("success");

                //   if (success.equals("1")) {

                for (int i = 0; i < college.length(); i++) {
                    JSONObject ar = college.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();

                    nmgereja = ar.getString("nama_gereja").trim();
                    almgereja = ar.getString("alamat").trim();
                    kapgereja = ar.getString("kapasitas").trim();
                    desgereja = ar.getString("deskripsi").trim();
                    idsim = ar.getString("id_simpul").trim();
                    latit = ar.getString("lattitude").trim();
                    longit = ar.getString("longitude").trim();
                    imamgrj = ar.getString("imam").trim();
                    telp = ar.getString("telepon").trim();
                    gbr = ar.getString("gambar").trim();


                    map.put("nama_gereja", nmgereja);
                    map.put("alamat", almgereja);
                    map.put("kapasitas", kapgereja);
                    map.put("deskripsi", desgereja);
                    map.put("id_simpul", idsim);
                    map.put("lattitude", latit);
                    map.put("longitude", longit);
                    map.put("imam", imamgrj);
                    map.put("telepon", telp);
                    map.put("gambar", gbr);

                }
                //       } else {

                //         pDialog.dismiss();
                //      status = "0";

                //      }

            } catch (JSONException e) {

                pDialog.dismiss();

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pDialog.dismiss();

            /**
             if (status.equals("0")) {
             Toast.makeText(getApplicationContext(), "data tidak ada",
             Toast.LENGTH_SHORT).show();

             }
             */

            TextView nama = (TextView) findViewById(R.id.namagereja);
            TextView alamat = (TextView) findViewById(R.id.alamatgereja);
            TextView kapasitas = (TextView) findViewById(R.id.kapasitas);
            TextView deskripsi = (TextView) findViewById(R.id.deskripsi);
            TextView simpul= (TextView) findViewById(R.id.idsim);
            TextView lattitude = (TextView) findViewById(R.id.la);
            TextView longitude = (TextView) findViewById(R.id.lo);
            TextView imam = (TextView) findViewById(R.id.imam);
            TextView tel = (TextView) findViewById(R.id.telepon);
            ImageView foto = (ImageView) findViewById(R.id.image);
            TextView id= (TextView) findViewById(R.id.id);

            if(gbr.isEmpty()) {
                Picasso.with(getApplicationContext())
                        .load("http://lbsgereja.esy.es/gambar/a.jpg")
                        .placeholder(R.drawable.load)
                        .into(foto);
            }
            else {
                Picasso.with(getApplicationContext())
                        .load(gbr)
                        .error(R.drawable.exit)
                        .placeholder(R.drawable.load)
                        .into(foto);
            }

            nama.setText(nmgereja);
            alamat.setText(almgereja);
            kapasitas.setText(kapgereja);
            deskripsi.setText(desgereja);
            simpul.setText(idsim);
            lattitude.setText(latit);
            longitude.setText(longit);
            imam.setText(imamgrj);
            tel.setText(telp);
            id.setText(idgrj);


            rute.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent  c = new Intent(getApplicationContext(), Halamanrute.class);
            String simp = ((TextView) findViewById(R.id.idsim)).getText().toString();
            String na = ((TextView) findViewById(R.id.namagereja)).getText().toString();
            String lal = ((TextView) findViewById(R.id.la)).getText().toString();
            String lol = ((TextView) findViewById(R.id.lo)).getText().toString();
            c.putExtra("namagereja", na);
            c.putExtra("namasimpul", simp);
            c.putExtra("latitude", lal);
            c.putExtra("longitude", lol);
            startActivity(c);
            }
            });

            //Mendapatkan String untuk halaman selanjutnya
            //Jadwal
            jadwal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent i= new Intent(getApplicationContext(), Halamanjadwal.class);
                    String nm = ((TextView) findViewById(R.id.namagereja)).getText().toString();
                    String id = ((TextView) findViewById(R.id.id)).getText().toString();

                    i.putExtra("nmgrj", nm);
                    i.putExtra("idgrj", id);

                    startActivity(i);
                }
            });

            //HalamanBerita
            berita.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent b= new Intent(getApplicationContext(), Halamanberita.class);
                    String nmb = ((TextView) findViewById(R.id.namagereja)).getText().toString();
                    String idb = ((TextView) findViewById(R.id.id)).getText().toString();

                    b.putExtra("nmgrj", nmb);
                    b.putExtra("idgrj", idb);

                    startActivity(b);
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

            alert.showAlertDialog(Halamaninformasigereja.this, "Peringatan",
                    "Maaf, Silahkan cek koneksi internet.",
                    false);
        }
    }
}

