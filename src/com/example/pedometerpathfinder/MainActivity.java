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
    Sensor mAccelerometer;
    Sensor mMagnetoMeter;

    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;


    TextView txtView;
    TextView txtView2;
    TextView txtView3;
	int steps;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mSensorMan = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mStepCounterSensor = mSensorMan.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
		mStepDetectorSensor = mSensorMan.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        mAccelerometer = mSensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetoMeter = mSensorMan.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		txtView = (TextView)findViewById(R.id.TextView1);
        txtView2 = (TextView)findViewById(R.id.TextView2);
        txtView3 = (TextView)findViewById(R.id.TextView3);
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
	        txtView.setText("Step Counter Detected : " + steps);
	     } else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
	    	 txtView2.setText("Step Detector Detected : " + value);
	     }

        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetoMeter) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;
            if(azimuthInDegress > 337.5 && azimuthInDegress < 22.5) {
                 //Assign north 0
            }
            else if (azimuthInDegress >=22.5 && azimuthInDegress < 67.5) {
                //Assign northEast 1

            }
            else if(azimuthInDegress >= 67.5 && azimuthInDegress < 112.5) {
                //Assign East 2
            }
            else if( azimuthInDegress >= 112.5 && azimuthInDegress < 157.5) {
                //Assign southEast 3
            }
            else if( azimuthInDegress >= 157.5 && azimuthInDegress <  202.5) {
                //Assign south 4
            }
            else if( azimuthInDegress >= 202.5 && azimuthInDegress < 247.5) {
                //Assign SouthWest 5
            }
            else if( azimuthInDegress >= 247.5 && azimuthInDegress < 292.5) {
                //Assign WEst 6
            }
            else if( azimuthInDegress >= 292.5 && azimuthInDegress < 337.5) {
                //Assign NorthWest 7
            }

            mCurrentDegree = -azimuthInDegress;
        }



	 }
		 
	protected void onResume() {
		super.onResume();
		
		mSensorMan.registerListener(this, mStepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
		mSensorMan.registerListener(this, mStepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST);

        mSensorMan.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorMan.registerListener(this, mMagnetoMeter, SensorManager.SENSOR_DELAY_GAME);

	}
	
	protected void onStop(){
	
	super.onStop();
	mSensorMan.unregisterListener(this, mStepCounterSensor);
	mSensorMan.unregisterListener(this, mStepDetectorSensor);

    mSensorMan.unregisterListener(this, mAccelerometer);
    mSensorMan.unregisterListener(this, mMagnetoMeter);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}
