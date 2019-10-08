package com.haunchen.mysql_test;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity implements AsyncTaskResult<String> {
    static WelcomeActivity instance;
    static LineChart lineChart;
    Description description;

    static Spinner spinner;
    static String tables[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        MyContext.setContext(this);
        instance = this;
        lineChart = findViewById(R.id.chart_line);
        lineChart.getViewPortHandler().getMatrixTouch().postScale(5f, 1f);
        description = new Description();

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GetDatasAsyncTask getDatasAsyncTask = new GetDatasAsyncTask();
                getDatasAsyncTask.connectionResult = WelcomeActivity.this;
                getDatasAsyncTask.execute(tables[position]);

                description.setText(tables[position]);
                description.setTextSize(16f);
                description.setTextColor(Color.RED);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new GetTablesAsyncTask().execute();

        /*LineChart lineChart = findViewById(R.id.chart_line);
        lineChart.setDrawBorders(true);
        List<Entry> entryList = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            entryList.add(new Entry(i, (float) (Math.random()) * 80));
        }

        LineDataSet lineDataSet = new LineDataSet(entryList, "測試");

        LineData data = new LineData(lineDataSet);
        lineChart.setData(data);*/

        //Bundle bundle = getIntent().getExtras();

        //TextView tv = findViewById(R.id.textView);

        //tv.setText("Welcome, " + bundle.getString("account"));
    }

    @Override
    public void taskFinish(String result) {
        try {
            final JSONArray jsonArray = new JSONArray(result);
            lineChart.clear();
            lineChart.setDescription(description);

            lineChart.setDrawBorders(true);
            lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            lineChart.getXAxis().setTextSize(15f);
            lineChart.getXAxis().setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    try {
                        return jsonArray.getJSONObject((int) value).getString("datetime").split(" ")[1];
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    return super.getFormattedValue(value);
                }

            });

            List<Entry> entryList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                entryList.add(new Entry(i + 1, Integer.parseInt(jsonArray.getJSONObject(i).getString("num"))));
            }

            LineDataSet lineDataSet = new LineDataSet(entryList, "測試");

            LineData data = new LineData(lineDataSet);
            lineChart.setData(data);
            lineChart.invalidate();

            Log.e("taskFinish", jsonArray.toString());
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
