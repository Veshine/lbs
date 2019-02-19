package com.example.acer.lbsgereja;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
import java.util.jar.JarException;

/**
 * Created by acer on 2017.
 */

public class Halamanpencarian extends ListActivity {

    EditText editSearch;

    private static final String TAG_GEREJA = "gereja";

    private static final String TAG_NAMA = "nama_gereja";
    private static final String TAG_ID = "id_gereja";
    private static final String TAG_ALAMAT = "alamat";

    //web service
    //url untuk melakukan get, parameter name dikosongkan untuk nantinya diisi dengan keywords tertentu
    private static String url= "http://lbsgereja.esy.es/android/pencarian.php?name=";

    //urlget digunakan untuk url full yang dipanggil , url+keywords
    private static String urlget= "";
    static boolean a=false;
    JSONArray gereja = null;

    //deklarasi progressdialog
    ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> gerejaList = new ArrayList<HashMap<String, String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencarian);
    }

    public void searchgrj(View view)
    {
        editSearch = (EditText) findViewById(R.id.edit1);
        //Mengambil keywords, dijadikan string
        String src = editSearch.getText().toString();
        urlget=url+src;
        //Log.e("a",urlget);
        gerejaList.clear();
        new JSONParse().execute();
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {

        /**
        @Override
        //Menampilkan progress dialog
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(Halamanpencarian.this);
            pDialog.setMessage("Menampilkan Data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        */

        @Override
        protected JSONObject doInBackground(String... args)
        {
            //Membuat JSON Parser instance
            JSONParser jParser = new JSONParser();

            //mengambil JSON String dari urlget, url+keywords
            JSONObject json = jParser.AmbilJson(urlget);
            if(json==null)
            {
                a=false;
            }
            else a=true;
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            if(a==true)
            {
                try{
                    Log.e("status",a+"");
                    //mengambil array gereja
                    gereja = json.getJSONArray(TAG_GEREJA);

                    //loop pada gereja
                    for(int i=0; i<gereja.length();i++)
                    {
                        JSONObject a = gereja.getJSONObject(i);

                        //simpan di variable
                        String name = a.getString(TAG_NAMA);
                        String alamat = a.getString(TAG_ALAMAT);
                        String id = a.getString(TAG_ID);

                        Log.e("name",name);

                        //buat hashmap baru untuk store String
                        HashMap<String, String> map = new HashMap<String, String>();
                        //map.put(TAG_TOILET_ID, toilet_id);
                        map.put(TAG_NAMA, name);
                        map.put(TAG_ALAMAT, alamat);
                        map.put(TAG_ID, id);

                        gerejaList.add(map);

                        //ProgressDialog dihilangkan jika sudah selesai mengambil data
                        //pDialog.dismiss();
                        tampilkandata();
                    }
                }catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(getApplicationContext(), "error getting data", Toast.LENGTH_SHORT).show();
                //pDialog.dismiss();
                Log.e("status",a+"");}
        }
    }

    public void tampilkandata()
    {
        //membuat ListView dari data JSON yang ada
        ListAdapter adapter = new SimpleAdapter(this, gerejaList,
                R.layout.activity_cari,
                new String[]{TAG_NAMA, TAG_ALAMAT, TAG_ID},
                new int[]{R.id.namagrj, R.id.alamatgrj, R.id.idgrj});
        setListAdapter(adapter);

        // selecting single ListView item
        ListView lv=getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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



