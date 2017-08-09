package com.example.stimpe.parking;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class socialMap extends Fragment implements OnMapReadyCallback {
    MapView gMapView;
    private View view;
    private GoogleMap mMap = null;
    private Context context;
    private PopupWindow permissionPopUp;
    private LinearLayout mLinearLayout;
    private String received_lot = null;
    private OnMapFragmentInteractionListener mListener;

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

        // Create Map starting point and boundaries
        LatLng jpl_map_start = new LatLng(34.20021, -118.174);
        LatLng jpl_northeast_bound = new LatLng(34.205,-118.167);
        LatLng jpl_southwest_bound = new LatLng(34.199,-118.178);
        LatLngBounds jpl_bounds = new LatLngBounds(jpl_southwest_bound, jpl_northeast_bound);

        //set pins for 3 lots
        LatLng west_lot = new LatLng(34.20021,-118.17737);
        LatLng parking_structure = new LatLng(34.19947,-118.16972);
        LatLng visitor_annex = new LatLng(34.19950,-118.17784);
        context = getActivity();
        mMap.addMarker(new MarkerOptions().position(west_lot).title("West Lot"));
        mMap.addMarker(new MarkerOptions().position(parking_structure).title("Parking Structure"));
        mMap.addMarker(new MarkerOptions().position(visitor_annex).title("Visitor Annex"));
        if(context.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        else {
            //dialog box telling user to turn on permission for app
            //map_permission_popup.xml
        }

        //zoom to jpl map
        mMap.setMinZoomPreference(15);
        mMap.setMaxZoomPreference(18);
        mMap.setLatLngBoundsForCameraTarget(jpl_bounds);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(jpl_map_start));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        moveCameraToSelection();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mMap != null) {
            moveCameraToSelection();
        }
        if (gMapView != null)
            gMapView.onResume();
    }

    public void moveCameraToSelection () {
        received_lot = mListener.getParkingAreaZoom();
        if (received_lot != null) {
            switch (received_lot.toLowerCase()) {
                case "west lot":
                    LatLng west = new LatLng(34.20021, -118.174);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(west));
//                    mMap.moveCamera(CameraUpdateFactory.zoomTo(20));
                    Log.d("map", "move to west lot");
                    break;
                case "parking structure":
                    LatLng pStruct = new LatLng(34.19947, -118.16972);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(pStruct));
//                    mMap.moveCamera(CameraUpdateFactory.zoomTo(20));
                    Log.d("map", "move to parking structure");
                    break;
                case "visitor annex":
                    LatLng annex = new LatLng(34.19950, -118.17784);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(annex));
//                    mMap.moveCamera(CameraUpdateFactory.zoomTo(20));
                    Log.d("map", "move to visitor annex");
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        context = null;

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
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnMapFragmentInteractionListener) context;
        }catch(ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnMapFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        context = null;
    }

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

    public interface OnMapFragmentInteractionListener {
        // TODO: Update argument type and name
        String getParkingAreaZoom();
    }
}
