package com.example.acer.lbsgereja;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.HashMap;

/**
 * Created by acer on 2017.
 */

public class Halamanutama extends AppCompatActivity {

    //slide
    private SliderLayout sliderLayout;

        //Memunculkan Pesan Ketika Menekan Tombol Exit Pada Android
        public void onBackPressed() {
            new AlertDialog.Builder(this)
                    .setMessage("Apakah Anda akan Keluar dari Aplikasi?")
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Halamanutama.this.finish();
                        }
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halamanutama);

        //SLIDE
                sliderLayout = (SliderLayout) findViewById(R.id.slider);

                // Load Image Dari res/drawable
                HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
                file_maps.put("Gereja GPdI Gajah Mada ",R.drawable.ggpdi);
                file_maps.put("Gereja Isa Almasih",R.drawable.ggia);
                file_maps.put("Gereja Gembala Baik Senghi",R.drawable.ggembalabaik);
                file_maps.put("Gereja Katedral Santo Yosef",R.drawable.gkatedral);
                file_maps.put("Gereja HKBP Eben Ezer",R.drawable.ghkbpeben);

                for(String name : file_maps.keySet()){
                    TextSliderView textSliderView = new TextSliderView(this);
                    // initialize a SliderLayout
                    textSliderView
                            .description(name)
                            .image(file_maps.get(name))
                            .setScaleType(BaseSliderView.ScaleType.Fit);
                    //add your extra information
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle()
                            .putString("extra",name);
                    sliderLayout.addSlider(textSliderView);
                }
                sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                sliderLayout.setCustomAnimation(new DescriptionAnimation());
                sliderLayout.setDuration(4000);
        //SLIDE



    // Deklarasikan Tombol yang akan digunakan
        //Tombol daftar gereja
        Button btn_daftargrj = (Button) findViewById(R.id.btn_daftargrj);

        //Tombol peta gereja
        Button btn_petagrj = (Button) findViewById(R.id.btn_petagrj);

        //Tombol pencarian gereja
        Button btn_carigrj = (Button) findViewById(R.id.btn_carigrj);

        //Tombol peta gereja sekitar
        Button btn_petagrjsekitar = (Button) findViewById(R.id.btn_grjsekitar);

        //Tombol bantuan aplikasi
        Button btn_bantuangrj = (Button) findViewById(R.id.btn_bantuangrj);

        //Tombol tentang aplikasi
        Button btn_tentanggrj = (Button) findViewById(R.id.btn_tentanggrj);

    //Menangkap Klik Event Button
        //Tombol Daftar gereja
        btn_daftargrj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //menampilkan halaman kategori
                Intent i = new Intent(getApplication(), Halamankategori.class);
                startActivity(i);
            }
        });

        //Tombol Peta gereja
        btn_petagrj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //menampilkan halaman peta gereja
                Intent i = new Intent(getApplication(), Halamanpeta.class);
                startActivity(i);
            }
        });


        //Tombol Cari gereja
        btn_carigrj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //menampilkan halaman pencarian
                Intent i = new Intent(getApplication(), Halamanpencarian.class);
                startActivity(i);
            }
        });

        //Tombol Gereja Sekitar
        btn_petagrjsekitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //menampilkan halaman peta gereja sekitar
                Intent i = new Intent(getApplication(), Halamangerejasekitar.class);
                startActivity(i);
            }
        });

                //Tombol Bantuan Aplikasi
        btn_bantuangrj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //menampilkan halaman kategori
                Intent i = new Intent(getApplication(), Halamanbantuan.class);
                startActivity(i);
            }
        });

        //Tombol Tentang Aplikasi
        btn_tentanggrj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //menampilkan halaman kategori
                Intent i = new Intent(getApplication(), Halamantentang.class);
                startActivity(i);
            }
        });
    }

    //Menampilkan Option menu tahap 1, MenuInflater: menu yang sudah dibuat di xml dapat ditampilkan
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pilihan_menu, menu);
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    Intent i = null;
    //Menampilkan menu tahap 2
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itemdaftarkec) {
            i=new Intent(Halamanutama.this, Halamankecamatan.class);
            startActivity(i);
            Toast.makeText(Halamanutama.this, "Daftar Kecamatan", 1000).show();
        } else if (item.getItemId() == R.id.itemdaftarjenis) {
            i=new Intent(Halamanutama.this, Halamanjenis.class);
            startActivity(i);
            Toast.makeText(Halamanutama.this, "Daftar Jenis Gereja", 1000).show();
        } else if (item.getItemId() == R.id.itembantuan) {
            i=new Intent(Halamanutama.this, Halamanbantuan.class);
            startActivity(i);
            Toast.makeText(Halamanutama.this, "Bantuan", 1000).show();
        } else if (item.getItemId() == R.id.itemtentang) {
            i=new Intent(Halamanutama.this, Halamantentang.class);
            startActivity(i);
            Toast.makeText(Halamanutama.this, "Tentang", 1000).show();
        } else if (item.getItemId() == R.id.itemkeluar) {
            //Konfirmasi Keluar Dari Aplikasi
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Apakah Anda akan Keluar dari Aplikasi?")
                    .setCancelable(false)
                    //jika pilih Yes
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }})
                    //jika pilih NO
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();

        }
        return true;
    }
}
