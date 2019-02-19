package com.example.acer.lbsgereja;

/**
 * Created by acer on 2017.
 */

        import android.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentStatePagerAdapter;

public class Pagetab extends FragmentStatePagerAdapter {
    int indi;
    public Pagetab(FragmentManager fm, int tabCount) {
        super(fm);
        indi= tabCount;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Tabinfo1();
            case 1:
                return new Tabinfo2();
            case 2:
                return new Tabinfo3();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return indi;
    }
}

