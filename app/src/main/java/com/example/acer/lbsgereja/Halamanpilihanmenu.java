package com.example.acer.lbsgereja;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by acer on 2017.
 */

public class Halamanpilihanmenu extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);
        //apa? help layout??
        textView.setText("Help Layout");
        setContentView(textView);
    }
}