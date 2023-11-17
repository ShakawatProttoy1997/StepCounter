package com.example.step_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
  private SensorManager sensorManager = null;
  private Sensor step_sensor;
  private TextView step;
  private int total_Steps = 0;
  private int pre_Steps = 0;
  private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        step = findViewById(R.id.step);
        progressBar = findViewById(R.id.progressBar);
        resetSteps();;
        load();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        step_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }
    protected void onResume(){
        super.onResume();;
        if(step_sensor == null) Toast.makeText(this, "this device has no sensor", Toast.LENGTH_LONG).show();
        else sensorManager.registerListener(this,step_sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }
   protected void onPause(){
        super.onPause();;
        sensorManager.unregisterListener(this);
   }
    @Override
    public void onSensorChanged(SensorEvent event) {
             if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
                 total_Steps = (int) event.values[0];
                 int currentSteps = total_Steps - pre_Steps;
                 step.setText(String.valueOf(currentSteps));
                 progressBar.setProgress(currentSteps);
             }
    }
    private void resetSteps(){
        step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "long press to reset..", Toast.LENGTH_LONG).show();
            }
        });
        step.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                pre_Steps = total_Steps;
                step.setText("0");
                progressBar.setProgress(0);
                save();
                return true;
          }
        });
    }

    private void save() {
        SharedPreferences sharedPreferences = getSharedPreferences("my preperance", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key-1",String.valueOf(pre_Steps));
        editor.apply();
    }

    private void load(){
        SharedPreferences sharedPreferences = getSharedPreferences("my prefer",Context.MODE_PRIVATE);
        int savedData = (int)sharedPreferences.getFloat("key-1",0f);
        pre_Steps = savedData;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}