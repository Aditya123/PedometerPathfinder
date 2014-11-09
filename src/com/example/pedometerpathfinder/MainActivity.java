package com.example.pedometerpathfinder;

import java.util.Timer;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener{
	SensorManager mSensorMan;
	Sensor mStepCounterSensor;
	Sensor mStepDetectorSensor;
	TextView txtView;
    TextView txtView2;
	int steps;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mSensorMan = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mStepCounterSensor = mSensorMan.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
		mStepDetectorSensor = mSensorMan.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
		
		txtView = (TextView)findViewById(R.id.TextView1);
        txtView2 = (TextView)findViewById(R.id.TextView2);
		steps = 0;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		Sensor sensor = event.sensor;
	     float[] values = event.values;
	     int value = -1;

	    
	     if (values.length > 0) {
	        value = (int) values[0];
             steps++;
	     }


	      if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
	        txtView.setText("Step Counter Detected : " + value);
	     } else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
	    	 txtView2.setText("Step Detector Detected : " + value);
	     }
	 }
		 
	protected void onResume() {
		super.onResume();
		
		mSensorMan.registerListener(this, mStepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
		mSensorMan.registerListener(this, mStepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
		
	}
	
	protected void onStop(){
	
	super.onStop();
	mSensorMan.unregisterListener(this, mStepCounterSensor);
	mSensorMan.unregisterListener(this, mStepDetectorSensor);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}
