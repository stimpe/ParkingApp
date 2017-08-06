package com.example.stimpe.parking;

import android.app.Activity;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by stimpe on 7/26/2017.
 */

public class StructureListFragment extends Fragment {
    Button mParkingLot1Value;
    Button mParkingLot2Value;
    Button mParkingLot3Value;
    final Handler handler = new Handler();
    Timer timer;
    TimerTask doAsynchronousTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_structure_list,container,false);
        assignVariables(view);
        /*timer = new Timer();
        doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {
                            getNowHTTPS();
                        }catch (Exception e) {
                            Toast toast = Toast.makeText(getActivity(),"Failed to obtain data! Please restart app.",Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 60000);*/
        //getNowHTTPS();
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
                        Log.d("now_data", "Success");   //check android monitor logcat for debug log statement
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
                        Log.d("now_data", String.valueOf(test_connect.getResponseCode()));
                        test_connect.disconnect();
                    }
                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void setParkingValues(final JSONArray jsonArray) {
        JSONObject jsonObject;
        Resources res;
        //must be final to allow access with inner class (runOnUiThread)
        final String[] temp = new String[jsonArray.length()];
        Activity activity;


        res = getResources();
        activity = getActivity();
        try {

            jsonObject = jsonArray.getJSONObject(0);//Set each textview value
            temp[0] = String.format(res.getString(R.string.parking_lot_1_text), jsonObject.getInt("spaces_left"));
            jsonObject = jsonArray.getJSONObject(1);
            temp[1] = String.format(res.getString(R.string.parking_lot_2_text), jsonObject.getInt("spaces_left"));
            jsonObject = jsonArray.getJSONObject(2);
            temp[2] = String.format(res.getString(R.string.parking_lot_3_text), jsonObject.getInt("spaces_left"));
        }catch(Exception e) {
            e.printStackTrace();
        }
        //throws exception if not on ui thread
        activity.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    mParkingLot1Value.setText(temp[0]);
                    mParkingLot2Value.setText(temp[1]);
                    mParkingLot3Value.setText(temp[2]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //onPause eliminate timerTask to prevent running in background
    @Override
    public void onPause() {
        timer.cancel();
        timer.purge();
        super.onPause();
    }

    //onResume create timerTask
    //onResume runs both on initial start and when app brought to foreground
    @Override
    public void onResume() {
        timer = new Timer();
        doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {
                            getNowHTTPS();
                        }catch (Exception e) {
                            Toast toast = Toast.makeText(getActivity(),"Failed to obtain data! Please restart app.",Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 60000);
        super.onResume();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
