package com.example.stimpe.parking;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by stimpe on 7/26/2017.
 */

public class StructureListFragment extends Fragment {
    TextView mParkingLot1Value;
    TextView mParkingLot2Value;
    TextView mParkingLot3Value;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_structure_list,container,false);
        assignVariables(view);
        DataGetter gettheData = new DataGetter();
        gettheData.frag = this;
        gettheData.execute();
        return view;
    }
    private void assignVariables(View view) {
        mParkingLot1Value = (TextView) view.findViewById(R.id.parking_lot_1_value);
        mParkingLot2Value = (TextView) view.findViewById(R.id.parking_lot_2_value);
        mParkingLot3Value = (TextView) view.findViewById(R.id.parking_lot_3_value);
    }
}
