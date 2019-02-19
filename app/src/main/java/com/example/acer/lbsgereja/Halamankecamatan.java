package com.example.acer.lbsgereja;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
 * Created by Veronika Apriani
 */

public class Halamankecamatan extends ListActivity {

    // mengecek keaktifan internet
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    AlertDialogManager alert = new AlertDialogManager();

    static String nm_kec = "nama_kecamatan";
    static String id_kec = "id_kecamatan";
    JSONArray gereja = null;

    private ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> data_map = new ArrayList<HashMap<String, String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kecamatan);

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
            pDialog = new ProgressDialog(Halamankecamatan.this);
            pDialog.setMessage("Menampilkan Data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            String link_url = "http://lbsgereja.esy.es/android/daftar_kecamatan.php";
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.AmbilJson(link_url);

            try {
                gereja = json.getJSONArray("kecamatan");

                for(int i = 0; i < gereja.length(); i++){
                    JSONObject c = gereja.getJSONObject(i);

                    HashMap<String, String> map = new HashMap<String, String>();

                    String nmkec = c.getString("nama_kecamatan");
                    String idkec = c.getString("id_kecamatan");

                    map.put(nm_kec, nmkec);
                    map.put(id_kec, idkec);

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
                            Halamankecamatan.this, data_map,
                            R.layout.activity_daftarkecamatan, new String[] { nm_kec, id_kec}, new int[] {R.id.namakec, R.id.kodekec});
                    setListAdapter(adapter);

                    //tambahan Klik dan tampil ke halaman selanjutnya
                    ListView lv = getListView();
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // TODO Auto-generated method stub
                            String idkec = ((TextView) view.findViewById(R.id.kodekec)).getText().toString();
                            String nmkec = ((TextView) view.findViewById(R.id.namakec)).getText().toString();
                            Intent x = new Intent(getApplicationContext(), Halamangerejakecamatan.class);
                            x.putExtra("idk", idkec);
                            x.putExtra("namak", nmkec);
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

            alert.showAlertDialog(Halamankecamatan.this, "Peringatan",
                    "Maaf, Silahkan cek koneksi internet.",
                    false);
        }
    }



}