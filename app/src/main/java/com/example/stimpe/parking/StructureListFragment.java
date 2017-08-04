package com.example.stimpe.parking;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by stimpe on 7/26/2017.
 */

public class StructureListFragment extends Fragment {
    Button mParkingLot1Value;
    Button mParkingLot2Value;
    Button mParkingLot3Value;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_structure_list,container,false);
        assignVariables(view);
        getNowHTTPS();
        //DataGetter gettheData = new DataGetter();
        //gettheData.frag = this;
        //gettheData.execute();
        return view;
    }
    private void assignVariables(View view) {
        mParkingLot1Value = (Button) view.findViewById(R.id.lot1);
        mParkingLot2Value = (Button) view.findViewById(R.id.lot2);
        mParkingLot3Value = (Button) view.findViewById(R.id.lot3);
    }

    //Addition by: Cristopher Hernandez
    //Date: 7/29/2017
    //Testing https connection
    //This function should get the JSON array from the JPL current parking API
    //It should then set the parking lot values to the values obtained
    private void getNowHTTPS() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream responseBody;
                    BufferedReader responseBodyReader;
                    JSONArray jsonArray;
                    String temp;
                    StringBuilder jsonBuild = new StringBuilder();
                    String jsonString;
                    URL API = new URL("https://jparking.jpl.nasa.gov/api/v1/now/");
                    HttpsURLConnection test_connect = (HttpsURLConnection) API.openConnection();
                    test_connect.setRequestProperty("Content-Type", "application/json");
                    test_connect.setRequestProperty("Accept", "application/json");  //ensure only JSON is accepted
                    test_connect.setRequestMethod("GET");
                    if (test_connect.getResponseCode() == 200) {
                        Log.d("Test", "Success");   //check android monitor logcat for debug log statement
                        responseBody = test_connect.getInputStream();
                        responseBodyReader = new BufferedReader(new InputStreamReader(responseBody, "UTF-8"));
                        while ((temp = responseBodyReader.readLine()) != null) {
                            jsonBuild.append(temp); //create string of JSON array
                        }
                        jsonString = jsonBuild.toString();
                        jsonArray = new JSONArray(jsonString);  //Create JSON array from string
                        setParkingValues(jsonArray);
                        test_connect.disconnect();
                    } else {
                        Log.d("Test", String.valueOf(test_connect.getResponseCode()));
                        test_connect.disconnect();
                    }
                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void setParkingValues(JSONArray jsonArray) {
        JSONObject jsonObject;
        try {
            jsonObject = jsonArray.getJSONObject(0);//Set each textview value
            mParkingLot1Value.setText("West Lot: " + String.valueOf(jsonObject.getInt("spaces_left")));
            jsonObject = jsonArray.getJSONObject(1);
            mParkingLot2Value.setText("Parking Structure: " + String.valueOf(jsonObject.getInt("spaces_left")));
            jsonObject = jsonArray.getJSONObject(2);
            mParkingLot3Value.setText("Visitor Annex" + String.valueOf(jsonObject.getInt("spaces_left")));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
