package com.example.stimpe.parking;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.*;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements StructureListFragment.OnFragmentInteractionListener, predictionPage.OnFragmentInteractionListener
                                                                , socialMap.OnMapFragmentInteractionListener{
    public static final int PAGE_COUNT = 3;
    public String received_lot = null;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new customPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);

    }

    //custom adapter for the viewpager to use
    private class customPagerAdapter extends FragmentPagerAdapter {

        public customPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //getitem will return each new fragment
        @Override
        public Fragment getItem(int pos) {
            switch(pos) {
                case 0: return new StructureListFragment();
                case 1: return new predictionPage();
                case 2: return new socialMap();
                default: return new StructureListFragment();
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

    }

    /*  method override for onbackpressed. May not be neccessary with the use of onkeydown
    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, false);
        } else {
            finish();
        }
    }*/

    //override for onkeydown so that backbutton will return to last screen
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
            return true;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void sendDataToMap(String selected_parking) {
        socialMap map_destination;
        Log.d("interface", "trying to communicate");
        map_destination = (socialMap) getSupportFragmentManager().findFragmentByTag(
                "android:switcher:" + R.id.viewPager + ":" + 2
        );
        if (map_destination != null) {
            Log.d("interface", "fragment already instantiated, communicating...");
            received_lot = selected_parking;
            viewPager.setCurrentItem(2, true);
        }else {
            Log.d("interface", "fragment not instantiated, instatiating...");
            map_destination = (socialMap) viewPager.getAdapter().instantiateItem(viewPager, 2);
            received_lot = selected_parking;
            viewPager.setCurrentItem(2, true);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //use for prediction page
    }

    @Override
    public String getParkingAreaZoom() {
        return received_lot;
    }
}
