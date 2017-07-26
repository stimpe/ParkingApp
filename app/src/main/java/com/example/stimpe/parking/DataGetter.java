package com.example.stimpe.parking;

import android.os.AsyncTask;
import android.view.View;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by stimpe on 7/26/2017.
 */

public class DataGetter extends AsyncTask<String,Void,String> {
    public StructureListFragment frag;

    @Override
    protected String doInBackground(String... params) {
        getData();
        return new String("result");
    }

    private void getData() {
        HttpURLConnection http;
        try {
            URL url = new URL("https://jparking.jpl.nasa.gov/api/v1/now.api/?format=json");
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                frag.mParkingLot1Value.setText("good job team");
            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
