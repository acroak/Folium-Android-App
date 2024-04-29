package com.example.folium_nav.Fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.folium_nav.R;

public class CompassFragment extends Fragment implements SensorEventListener {
    private static final String TAG = "CompassFragment";

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private TextView compassDegrees, compassDirection;
    private ImageView compass;
    private float[] accelerometerReading = new float[3];
    private float[] magnetometerReading = new float[3];
    private float[] rotationMatrix = new float[9];
    private float[] orientationAngles = new float[3];

    public CompassFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if (accelerometer == null) {
            Log.e(TAG, "Accelerometer sensor is not available on this device.");
        }
        if (magnetometer == null) {
            Log.e(TAG, "Magnetometer sensor is not available on this device.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compass, container, false);
        compassDirection = view.findViewById(R.id.compass_direction);
        compassDegrees = view.findViewById(R.id.compass_degree);
        compass = view.findViewById(R.id.compassImg);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (magnetometer != null) {
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.length);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.length);
        }
        updateOrientationAngles();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }

    private void updateOrientationAngles() {
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading);
        SensorManager.getOrientation(rotationMatrix, orientationAngles);
        float degrees = (float) Math.toDegrees(orientationAngles[0]);

        // Adjusting degrees to be in the range of 0 to 360 rather than -180 to 180
        degrees = (degrees + 360) % 360;

        String direction = getDirection(degrees);
        compassDirection.setText(direction);

        // Format the degrees to show only two decimal points
        String degreeStr = String.format("%.2f", degrees);
        if (!degreeStr.isEmpty()) {
            compassDegrees.setText(degreeStr);
        }

        // Round the degree to be easier to use for rotation of image
        int degreeRound = (int) Math.round(degrees);
        // Rotate image by degree
        compass.setRotation(-degreeRound);
    }

    private String getDirection(float degrees) {
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        int index = (int) Math.round(((degrees % 360) / 45));
        if (index < 0) {
            index += 8;
        }
        index %= 8; // Ensure index is within the range [0, 7]
        return directions[index];
    }

}
