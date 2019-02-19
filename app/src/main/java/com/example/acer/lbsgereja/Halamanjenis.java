package com.example.acer.lbsgereja;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Veronika Apriani
 */

public class Halamanjenis extends ListActivity {
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    AlertDialogManager alert = new AlertDialogManager();

    static String nm_jns = "nama_jenis";
    static String id_jns = "id_jenis";
    JSONArray gereja = null;

    private ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> data_map = new ArrayList<HashMap<String, String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jenis);

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

        cekInternet();

        //new AmbilData().execute();
    }

    class AmbilData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Halamanjenis.this);
            pDialog.setMessage("Menampilkan Data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            String link_url = "http://lbsgereja.esy.es/android/daftar_jenis.php";
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.AmbilJson(link_url);

            try {
                gereja = json.getJSONArray("jenis");

                for(int i = 0; i < gereja.length(); i++){
                    JSONObject ar = gereja.getJSONObject(i);

                    HashMap<String, String> map = new HashMap<String, String>();

                    String nmjns = ar.getString("nama_jenis");
                    String idjns = ar.getString("id_jenis");

                    map.put(nm_jns, nmjns);
                    map.put(id_jns, idjns);

                    data_map.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            Halamanjenis.this, data_map,
                            R.layout.activity_daftarjenis, new String[] { nm_jns, id_jns}, new int[] {R.id.namajns, R.id.idjns});
                    setListAdapter(adapter);

                    //tambahan Klik dan tampil ke halaman selanjutnya
                    ListView lv = getListView();
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // TODO Auto-generated method stub
                            String idjns = ((TextView) view.findViewById(R.id.idjns)).getText().toString();
                            String nmjns = ((TextView) view.findViewById(R.id.namajns)).getText().toString();
                            Intent x = new Intent(getApplicationContext(), Halamangerejajenis.class);   //ubah disini
                            x.putExtra("idj", idjns);
                            x.putExtra("namaj", nmjns);
                            startActivity(x);
                        }
                    });
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

            alert.showAlertDialog(Halamanjenis.this, "Peringatan",
                    "Maaf, Silahkan cek koneksi internet.",
                    false);
        }
    }
}




