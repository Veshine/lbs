package com.example.acer.lbsgereja;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

public class Halamangerejajenis extends AppCompatActivity {

    ProgressDialog pDialog;
    String status = "1";
    JSONArray college = null;
    ListView lve;
    GpsService  gps;
    public String kode, nama;

    private static final String TAG_NAMA = "nama_gereja";
    private static final String TAG_ALMGRJ = "alamat";
    private static final String TAG_JARAK = "jarak";
    private static final String TAG_IDGRJ = "id_gereja";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gereja);

        Bundle bundle = getIntent().getExtras();
        kode = bundle.getString("idj");
        nama = bundle.getString("namaj");

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

        lve = (ListView) findViewById(R.id.daftargereja);

        gps = new GpsService(Halamangerejajenis.this);
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

    public class AmbilData extends AsyncTask<String, String, String> {

        ArrayList<HashMap<String,String>>dataList = new ArrayList<HashMap<String, String>>();
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(Halamangerejajenis.this);
            pDialog.setMessage("Menampilkan Data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args)
        {
            String lat = args[0];
            String lng = args[1];
            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("lat", lat));
            param.add(new BasicNameValuePair("lng",lng));

            param.add(new BasicNameValuePair("id_jenis", kode));

            String url;
            url = "http://lbsgereja.esy.es/android/daftargrjjns2.php"; //bisa

            JSONParser3 jParser = new JSONParser3 ();
            JSONObject json = jParser.makeHttpRequest(url,"GET",param);
            try{
                college = json.getJSONArray("lokasi");

                String success = json.getString("success");

                if(success.equals("1")){
                    for (int i = 0; i <college.length(); i++){
                        JSONObject c = college.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();

                        String nama_gereja = c.getString(TAG_NAMA);
                        String alamat = c.getString(TAG_ALMGRJ);
                        String jarak = c.getString(TAG_JARAK);
                        String id_gereja = c.getString(TAG_IDGRJ);

                        map.put(TAG_NAMA, nama_gereja);
                        map.put(TAG_JARAK, jarak);
                        map.put(TAG_ALMGRJ, alamat);
                        map.put(TAG_IDGRJ, id_gereja);

                        dataList.add(map);
                    }
                }else{
                    pDialog.dismiss();
                    status ="0";
                }
            }catch(JSONException e){
                pDialog.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            pDialog.dismiss();
            if(status.equals("0")){
                Toast.makeText(getApplicationContext(),"data tidak ada", Toast.LENGTH_SHORT).show();
            }

            TextView judul=(TextView) findViewById(R.id.judul);
            judul.setText(nama);

            ListAdapter adapter = new SimpleAdapter(getApplicationContext(),
                    dataList, R.layout.activity_daftargereja, new String[] {TAG_NAMA,TAG_ALMGRJ, TAG_JARAK, TAG_IDGRJ},
                    new int[] {R.id.namagrj,R.id.alamatgrj, R.id.jarakgrj,R.id.idgrj});

            lve.setAdapter(adapter);

            //tambahan Klik dan tampil ke halaman selanjutnya
            lve.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    String idgrj = ((TextView) view.findViewById(R.id.idgrj)).getText().toString();

                    Intent x = new Intent(getApplicationContext(), Halamaninformasigereja.class);

                    x.putExtra("idg", idgrj);

                    startActivity(x);

                }
            });

        }
    }

}
