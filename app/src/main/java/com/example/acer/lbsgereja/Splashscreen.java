package com.example.acer.lbsgereja;

/**
 * Created by acer on 2017.
 */

import android.app.Activity;
import android.os.Bundle;

import android.content.Intent;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import static com.example.acer.lbsgereja.R.layout.activity_splashscreen;

public class Splashscreen extends Activity{

    //set Waktu untuk bergeraknya Splash Screen
    private static int LamaTampilSplash = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mulai koding

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //koding

        setContentView(R.layout.activity_splashscreen);

        //koding lagi
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //to do auto generated stub
                Intent apasih = new Intent(Splashscreen.this,Halamanutama.class);
                startActivity(apasih);

                //jeda setelah splashscreen
                this.selesai();
            }

            private void selesai() {
                //auto
            }
        },LamaTampilSplash);

        }
    }