package com.example.stimpe.parking;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.*;
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

public class MainActivity extends AppCompatActivity {
    //variables for nav drawer
    private String[] mTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private String mCurrentTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing drawer values
        mTitles = getResources().getStringArray(R.array.drawer_content);
        mCurrentTitle = getTitle().toString();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerListView = (ListView) findViewById(R.id.drawerList);
        mDrawerListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mTitles));
        mDrawerListView.setOnItemClickListener(new DrawerItemClickListener());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment==null) {
            fragment = new StructureListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container,fragment)
                    .commit();
        }

        drawerSetup();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    //sync for drawer animation
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //protection against landscape changes etc.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //set up hamburger toggle functions
    private void drawerSetup() {
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.drawer_open,R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("JPL Parking Menu");
                invalidateOptionsMenu();
            }
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mCurrentTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    //listener for tapping items in drawer
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    //function called when item is selected
    private void selectItem (int position) {
        String temp = mTitles[position];
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (temp) {
            case ("About"):
                fragment = new AboutPageFragment();
                break;
            default:
                fragment = new StructureListFragment();
        }

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

        mDrawerListView.setItemChecked(position, true);
        setTitle(mTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerListView);
    }
}
