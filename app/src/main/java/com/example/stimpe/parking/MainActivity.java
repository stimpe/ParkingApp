package com.example.stimpe.parking;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.*;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.Arrays;

//TODO: add viewPager to connect all fragments
public class MainActivity extends AppCompatActivity implements predictionPage.OnFragmentInteractionListener, socialMap.OnFragmentInteractionListener, StructureListFragment.OnFragmentInteractionListener{
    public static final int PAGE_COUNT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new customPagerAdapter(getSupportFragmentManager()));
        /*
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new StructureListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }*/
    }

    private class customPagerAdapter extends FragmentPagerAdapter {

        public customPagerAdapter(FragmentManager fm) {
            super(fm);
        }

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

    @Override
    public void onFragmentInteraction(Uri uri) {
        //TODO: any contents needed here?
    }
}
