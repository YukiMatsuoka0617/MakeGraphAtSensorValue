package com.example.accelerometergraph;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements SensorEventListener, View.OnClickListener {

    private SensorManager sensorManager;
    private Sensor accel;

    private LineChart mChart;
    private String[] labels = new String[]{
            "accelerationX",
            "accelerationY",
            "accelerationZ"};
    private int[] colors = new int[]{
            Color.RED,
            Color.GREEN,
            Color.BLUE};

    Button buttonStart;
    Button buttonStop;
    Button buttonChange;

    float[] accArray = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        setChart();
        setButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                accArray[0] = sensorEvent.values[0];
                accArray[1] = sensorEvent.values[1];
                accArray[2] = sensorEvent.values[2];
                LineData data = mChart.getLineData();
                if (data != null) {
                    for (int i = 0; i < 3; i++) {
                        ILineDataSet set3 = data.getDataSetByIndex(i);
                        if (set3 == null) {
                            LineDataSet set = new LineDataSet(null, labels[i]);
                            set.setLineWidth(2.0f);
                            set.setColor(colors[i]);
                            set.setDrawCircles(false);
                            set.setDrawValues(false);
                            set3 = set;
                            data.addDataSet(set3);
                        }
                        data.addEntry(new Entry(set3.getEntryCount(), accArray[i]), i);
                        data.notifyDataChanged();
                    }
                    mChart.notifyDataSetChanged();
                    mChart.setVisibleXRangeMaximum(50);
                    mChart.moveViewToX(data.getEntryCount());
                }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    void setButton() {
        buttonStart = findViewById(R.id.button_start);
        buttonStart.setOnClickListener(this);

        buttonStop = findViewById(R.id.button_stop);
        buttonStop.setOnClickListener(this);

        buttonChange = findViewById(R.id.button_change);
        buttonChange.setOnClickListener(this);
    }

    void setChart() {
        mChart = findViewById(R.id.chart);
        mChart.setData(new LineData());
        mChart.getDescription().setEnabled(false);
        mChart.setDrawGridBackground(true);
        mChart.getAxisRight().setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start:
                sensorManager.registerListener(this, accel,
                        SensorManager.SENSOR_DELAY_NORMAL);
                break;
            case R.id.button_stop:
                sensorManager.unregisterListener(this);
                break;
            case R.id.button_change:
        }

    }
}