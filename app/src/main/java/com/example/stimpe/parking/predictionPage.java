package com.example.stimpe.parking;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link predictionPage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link predictionPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class predictionPage extends Fragment {
    Spinner spinner;
    // TODO: Rename parameter arguments if neccessary
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    BarChart chart;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public predictionPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment predictionPage.
     */
    // TODO: Rename and change types and number of parameters
    public static predictionPage newInstance(String param1, String param2) {
        predictionPage fragment = new predictionPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_prediction_page, container, false);
        radioGroup = (RadioGroup) view.findViewById(R.id.days_of_week);
        radioGroup.check(R.id.monday_radio);
        spinner = (Spinner) view.findViewById(R.id.lot_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.lot_spinnerArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectionListener(parent, view, position, id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getPredictHTTPS(1,"Mon");

            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String text = spinner.getSelectedItem().toString();
                selectionListener(group, checkedId, text);
            }

        });
        chart = (BarChart) view.findViewById(R.id.predictChart);
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        spinner.setSelection(0);
        return view;
    }

    public void selectionListener(RadioGroup group, int checkedId, String spinnerText) {
        Log.d("radio", "item selected");
        int lotId;
        radioButton = (RadioButton) group.findViewById(checkedId);
        Log.d("radio", spinnerText);
        switch(spinnerText){    //get lot ids for use with API call
            case "West Lot":
                lotId = 1;
                break;
            case "Parking Structure":
                lotId = 2;
                break;
            case "Visitor Annex":
                lotId = 3;
                break;
            default:
                lotId = 1;
        }
        if (radioButton != null) {
            getPredictHTTPS(lotId, radioButton.getText().toString());
        } else {
            getPredictHTTPS(1,"Mon");
        }
    }

    public void selectionListener(AdapterView<?> parent, View view, int pos, long id) {
        Log.d("spinner", "item selected");
        int selected = radioGroup.getCheckedRadioButtonId();
        Log.d("spinner", String.valueOf(R.id.monday_radio));
        Log.d("spinner", String.valueOf(selected));
        int lotId;
        radioButton = (RadioButton) view.findViewById(selected);
        String lot = parent.getItemAtPosition(pos).toString();
        Log.d("spinner", lot);
        switch(lot){    //get lot ids for use with API call
            case "West Lot":
                lotId = 1;
                break;
            case "Parking Structure":
                lotId = 2;
                break;
            case "Visitor Annex":
                lotId = 3;
                break;
            default:
                lotId = 1;
        }
        if (radioButton != null) {
            getPredictHTTPS(lotId, radioButton.getText().toString());
        } else {
            getPredictHTTPS(1,"Mon");
        }
    }

    //variables must be final to be accessed in inner class
    private void getPredictHTTPS(final int lot, final String day) {
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
                    int day_id;
                    URL API = new URL("https://jparking.jpl.nasa.gov/api/v1/predict.json/?lot=" + String.valueOf(lot));
                    HttpsURLConnection test_connect = (HttpsURLConnection) API.openConnection();
                    test_connect.setRequestProperty("Content-Type", "application/json");
                    test_connect.setRequestProperty("Accept", "application/json");  //ensure only JSON is accepted
                    test_connect.setRequestMethod("GET");
                    if (test_connect.getResponseCode() == 200) {
                        Log.d("predict", "Success");   //check android monitor logcat for debug log statement
                        responseBody = test_connect.getInputStream();
                        responseBodyReader = new BufferedReader(new InputStreamReader(responseBody, "UTF-8"));
                        while ((temp = responseBodyReader.readLine()) != null) {
                            jsonBuild.append(temp); //create string of JSON array
                        }
                        jsonString = jsonBuild.toString();
                        jsonArray = new JSONArray(jsonString);  //Create JSON array from string
                        switch (day) {
                            case "Mon":
                                day_id = 2;
                                break;
                            case "Tues":
                                day_id = 3;
                                break;
                            case "Wed":
                                day_id = 4;
                                break;
                            case "Thurs":
                                day_id = 5;
                                break;
                            case "Fri":
                                day_id = 6;
                                break;
                            default:
                                day_id = 2;
                        }
                        predictDataHandler(jsonArray, day_id);
                        test_connect.disconnect();
                    } else {
                        Log.d("predict", String.valueOf(test_connect.getResponseCode()));
                        test_connect.disconnect();
                    }
                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    public void predictDataHandler(JSONArray jsonArray, int day_id) {
        Log.d("predictChart", "begin chart draw");
        final SparseIntArray dataPoints = new SparseIntArray();
        int lenJsonArray = jsonArray.length();
        JSONObject jsonObject;
        int begin_hr = 6;
        int end_hr = 17;
        List<BarEntry> entries = new ArrayList<BarEntry>();
        final SparseArray<String> axisLabels = new SparseArray<>();
        final BarDataSet barDataSet;
        XAxis xAxis;
        Activity activity = getActivity();

        for (int i=begin_hr; i <= end_hr; i++) {
            if (i > 12) {
                axisLabels.put(i,String.valueOf(i-12) + " pm");
            } else if (i == 12) {
                axisLabels.put(i,String.valueOf(i) + " pm");
            } else {
                axisLabels.put(i, String.valueOf(i) + " am");
            }
        }

        try {
            //json begins at hour 6, ends at hour 17
            for (int i = begin_hr; i <= end_hr; i++) {
                for (int j = 0; j < lenJsonArray; j++) {
                    jsonObject = jsonArray.getJSONObject(j);
                    if (jsonObject.getInt("hour") == i && jsonObject.getInt("day_of_week") == day_id) {
                        dataPoints.append(i, (int) jsonObject.getDouble("total"));
                        break;
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0;i < dataPoints.size();i++) {
            Log.d("key", String.valueOf(dataPoints.keyAt(i)));
            Log.d("value", String.valueOf(dataPoints.valueAt(i)));
            entries.add(new BarEntry(dataPoints.keyAt(i), dataPoints.valueAt(i)));
        }
        barDataSet = new BarDataSet(entries, "Parking Available Over Time");
        barDataSet.setColor(R.color.DarkJpl);
        barDataSet.setDrawValues(false);
        final BarData barData = new BarData(barDataSet);
        xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return axisLabels.get((int) value);
            }
        });
        if (chart.getData() == null) {
            //run on ui thread to prevent thread exception
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    chart.setData(barData);
                    chart.invalidate();
                }
            });
            Log.d("predictChart", "end of chart draw");
        } else {
            //run on ui thread to prevent thread exception
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    ChartData temp = chart.getData();
                    temp.removeDataSet(0);
                    temp.addDataSet(barDataSet);
                    chart.notifyDataSetChanged();
                    chart.invalidate();
                }
            });
            Log.d("predictChart", "end of chart draw");
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
