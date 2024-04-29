package com.example.folium_nav.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.folium_nav.R;

public class LightSensorFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private TextView lightLevelTextView, lightNote;
    private Button detectLightButton;

    private ImageView lightLevelImg;

    private boolean isDetectingLight = false;

    public LightSensorFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_light_sensor, container, false);

        lightLevelTextView = view.findViewById(R.id.lightLevelTextView);
        detectLightButton = view.findViewById(R.id.detectLightButton);
        lightNote = view.findViewById(R.id.lightNote);

        lightLevelImg = view.findViewById(R.id.light_level_img);
        lightLevelImg.setImageResource(R.drawable.light_dim);

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        detectLightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDetectingLight = !isDetectingLight;
                if (isDetectingLight) {
                    sensorManager.registerListener(LightSensorFragment.this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
                } else {
                    sensorManager.unregisterListener(LightSensorFragment.this);
                }
            }
        });

        lightNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("How this App measures Light:")
                        .setMessage(
                                "Please consider the purchase of PAR meter for the best care of your plants." +
                                        "\n\nThis application measures the levels of light in Lux." +
                                        "\n\nLux is a measure of illuminance, which is the amount of light that falls on a surface. However, plants use light for photosynthesis, and the effectiveness of light for photosynthesis depends on the number of photons (light particles) rather than just the intensity." +
                                        "\n\nMicromoles (µmol) are a measure of the number of photons, specifically photosynthetically active radiation (PAR), which is more relevant for plant growth than lux."
                        )
                        .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isDetectingLight) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
        // Unregister the sensor listener to avoid memory leaks
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightLevel = event.values[0];
            String lightLevelString = getLightLevelString(lightLevel);
            lightLevelTextView.setText("Light level: \n" + lightLevelString);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void updateLightLevel() {
        float lightLevel = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT).getMaximumRange();
        String lightLevelString = getLightLevelString(lightLevel);
        lightLevelTextView.setText("Light level:\n" + lightLevelString);
    }

    private String getLightLevelString(float lightLevel) {
        if(lightLevel < 49){
            lightLevelImg.setImageResource(R.drawable.light_dark);
            return "Dark\nnot suitable for plants";
        }
        else if (lightLevel < 250 && lightLevel > 50) {
            lightLevelImg.setImageResource(R.drawable.light_dark);
            return "Low Light\n50 - 250 Lux";
        } else if (lightLevel < 1000 && lightLevel > 250) {
            lightLevelImg.setImageResource(R.drawable.light_dim);
            return "Medium Light\n250 - 1000 Lux";
        } else if (lightLevel < 5000 && lightLevel > 1001) {
            lightLevelImg.setImageResource(R.drawable.light_moderate);
            return "Bright Indirect Light\n1000 - 5000 Lux";
        } else {
            lightLevelImg.setImageResource(R.drawable.light_bright);
            return "Bright Direct Light\n5000+ Lux";
        }
    }
}
