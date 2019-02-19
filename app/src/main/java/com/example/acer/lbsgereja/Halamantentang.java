package com.example.acer.lbsgereja;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by acer on 2017.
 */

public class Halamantentang extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);


        TextView jdl = (TextView) findViewById(R.id.judul);
        TextView os = (TextView) findViewById(R.id.osnya);
        TextView def = (TextView) findViewById(R.id.keterangan);
        TextView crt = (TextView) findViewById(R.id.pembuat);
        TextView s1=(TextView) findViewById(R.id.sub1);
        TextView s2=(TextView) findViewById(R.id.sub2);
        TextView s3=(TextView) findViewById(R.id.sub3);

        String p1 = "Location Based Service Gedung Gereja di Kota Pontianak";

        String sb1 = "Sistem Operasi   \t:\t";
        String p2 = "Minimal Android versi 4.0";

        String sb2 = "Keterangan \t\t\t\t: \t";
        String p3 = "Merupakan aplikasi pencarian jalur terpendek menuju gereja di Kota Pontianak " +
                "menggunakan algoritma Floyd warshall).\n" +
                "Pengguna hanya dapat menggunakan aplikasi jika mode internet aktif dan gps aktif";

        String sb3 = "Pembuat \t\t\t\t\t:\t";
        String p4 = "Veronika Apriani\nD03112032\nTeknik Informatika\nFakultas Teknik\nUniversitas Tanjungpura\n2018";

        jdl.setText(String.valueOf(p1));
        s1.setText(String.valueOf(sb1));
        os.setText(String.valueOf(p2));
        s2.setText(String.valueOf(sb2));
        def.setText(String.valueOf(p3));
        s3.setText(String.valueOf(sb3));
        crt.setText(String.valueOf(p4));
        }
}
