package com.example.acer.lbsgereja;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by Veronika Apriani
 */

public class Halamankategori extends ListActivity {

    //Tampilkan List
    static final String[] KATEGORI = {"Kecamatan", "Jenis Gereja"};

    //Nampilkan List
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);

        //Tampilkan List
        ListAdapter listAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, KATEGORI);
        setListAdapter(listAdapter);
    }

    //Klik Isi List dan menampilkan Activity lain
    @Override
    public void onListItemClick(ListView parent, View v, int position, long id) {
        super.onListItemClick(parent, v, position, id);

        Object detail = this.getListAdapter().getItem(position);
        String tampil = detail.toString();
        Intent i = null;
        if (tampil == "Kecamatan") {
            i = new Intent(Halamankategori.this, Halamankecamatan.class);
            startActivity(i);
        } else if (tampil == "Jenis Gereja") {
            i = new Intent(Halamankategori.this, Halamanjenis.class);
            startActivity(i);
        }
    }
}



