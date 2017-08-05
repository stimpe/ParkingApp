package com.example.stimpe.parking;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class socialMap extends Fragment implements OnMapReadyCallback {
    MapView gMapView;
    private View view;
    private GoogleMap mMap = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        try {
            view = inflater.inflate(R.layout.maps, container, false);
        }catch(InflateException e) {
            Log.d("map", "map exists already, cannot inflate");
        }

        SupportMapFragment mapFrag = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        //gMapView = (MapView) view.findViewById(R.id.social_map_view);
        //gMapView.getMapAsync(this);

        //gMapView.onCreate(getArguments());
        return view;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    @Override
    public void onResume() {
        super.onResume();

        if (gMapView != null)
            gMapView.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (gMapView != null)
            gMapView.onDestroy();
    }
    /*
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FragmentManager fragMng = getActivity().getSupportFragmentManager();
        Fragment frag = (fragMng.findFragmentById(R.id.map));
        FragmentTransaction fragTrans = fragMng.beginTransaction();
        fragTrans.remove(frag);
        fragTrans.commit();
    }*/

    @Override
    public void onStart() {
        super.onStart();

        if (gMapView != null)
            gMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (gMapView != null)
            gMapView.onStop();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (gMapView != null)
            gMapView.onSaveInstanceState(outState);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
