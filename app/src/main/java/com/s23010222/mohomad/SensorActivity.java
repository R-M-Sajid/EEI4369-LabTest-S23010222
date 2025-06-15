package com.s23010222.mohomad;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor tempSensor;
    private TextView tvTemperature, tvStatus;
    private ImageView flameIcon;
    private ConstraintLayout rootLayout;
    private MediaPlayer mediaPlayer;
    private final float threshold = 22f;
    private boolean hasPlayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        tvTemperature = findViewById(R.id.tvTemperature);
        tvStatus = findViewById(R.id.tvStatus);
        flameIcon = findViewById(R.id.playingFlame);
        rootLayout = findViewById(R.id.rootLayout);

        findViewById(R.id.btnSimulate).setOnClickListener(v -> simulateTemperature(threshold + 5));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.alert);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tempSensor != null) {
            sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (tempSensor != null) {
            sensorManager.unregisterListener(this);
        }
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float temp = event.values[0];
        tvTemperature.setText("Temperature: " + temp + " °C");

        if (temp > threshold && !hasPlayed) {
            mediaPlayer.start();
            hasPlayed = true;
            flameIcon.setVisibility(View.VISIBLE);
            tvStatus.setVisibility(View.VISIBLE);
            rootLayout.setBackgroundColor(Color.parseColor("#FFF3E0"));
            Toast.makeText(this, "Temperature exceeded " + threshold + "°C! Playing audio.", Toast.LENGTH_SHORT).show();
        } else if (temp <= threshold) {
            hasPlayed = false;
            flameIcon.setVisibility(View.GONE);
            tvStatus.setVisibility(View.GONE);
            rootLayout.setBackgroundResource(R.drawable.bg_gradient);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private void simulateTemperature(float temp) {
        tvTemperature.setText("Temperature: " + temp + " °C (Simulated)");
        if (temp > threshold && !hasPlayed) {
            mediaPlayer.start();
            hasPlayed = true;
            flameIcon.setVisibility(View.VISIBLE);
            tvStatus.setVisibility(View.VISIBLE);
            rootLayout.setBackgroundColor(Color.parseColor("#FFF3E0"));
            Toast.makeText(this, "Temperature exceeded " + threshold + "°C! Playing audio.", Toast.LENGTH_SHORT).show();
        } else if (temp <= threshold) {
            hasPlayed = false;
            flameIcon.setVisibility(View.GONE);
            tvStatus.setVisibility(View.GONE);
            rootLayout.setBackgroundResource(R.drawable.bg_gradient);
        }
    }
}
