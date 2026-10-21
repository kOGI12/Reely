package com.example.reely;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class FilmingAssistantActivity extends AppCompatActivity
        implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private TextView tvMovementStatus;
    private TextView tvMovementValue;
    private TextView tvFilmingAdvice;
    private TextView tvSensorDetails;
    private TextView tvRawReadings;
    private ProgressBar progressMovement;

    private final float[] gravity = new float[3];

    private long previousTimestamp;
    private long firstTimestamp;
    private long lastUiTimestamp;

    private double smoothedMovement;

    // Initial demonstration thresholds, to be tested and refined.
    private static final double LOW_MOVEMENT_LIMIT = 0.35;
    private static final double HIGH_MOVEMENT_LIMIT = 1.20;

    private static final long WARMUP_NS = 1_000_000_000L;
    private static final long UI_INTERVAL_NS = 100_000_000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_filming_assistant);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.filmingRoot),
                (view, windowInsets) -> {
                    Insets systemBars = windowInsets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    view.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return windowInsets;
                }
        );

        TextView tvFilmingTopic = findViewById(R.id.tvFilmingTopic);
        tvMovementStatus = findViewById(R.id.tvMovementStatus);
        tvMovementValue = findViewById(R.id.tvMovementValue);
        tvFilmingAdvice = findViewById(R.id.tvFilmingAdvice);
        tvSensorDetails = findViewById(R.id.tvSensorDetails);
        tvRawReadings = findViewById(R.id.tvRawReadings);
        progressMovement = findViewById(R.id.progressMovement);

        String topic = getIntent().getStringExtra("TOPIC");

        if (topic == null || topic.trim().isEmpty()) {
            topic = "Your next reel";
        }

        tvFilmingTopic.setText("Preparing to film: " + topic);

        findViewById(R.id.btnBackToPlan).setOnClickListener(
                view -> finish()
        );

        sensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE
        );

        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(
                    Sensor.TYPE_ACCELEROMETER
            );
        }

        if (accelerometer == null) {
            showSensorUnavailable();
        } else {
            tvSensorDetails.setText(
                    "Accelerometer: " + accelerometer.getName()
                            + "\nRaw readings include gravity."
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        resetMeasurements();

        if (sensorManager == null || accelerometer == null) {
            showSensorUnavailable();
            return;
        }

        tvMovementStatus.setText("Waiting for readings");
        tvMovementStatus.setTextColor(Color.parseColor("#C4B5FD"));
        tvFilmingAdvice.setText(
                "Keep the phone still briefly while readings settle."
        );

        boolean registered = sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_GAME
        );

        if (!registered) {
            tvMovementStatus.setText("Sensor could not start");
            tvFilmingAdvice.setText(
                    "Return to your plan and try opening this screen again."
            );
        }
    }

    @Override
    protected void onPause() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }

        super.onPause();
    }

    private void resetMeasurements() {
        previousTimestamp = 0;
        firstTimestamp = 0;
        lastUiTimestamp = 0;
        smoothedMovement = 0;

        gravity[0] = 0;
        gravity[1] = 0;
        gravity[2] = 0;

        progressMovement.setProgress(0);
        tvMovementValue.setText("Estimated movement: -- m/s²");
        tvRawReadings.setText("X: --    Y: --    Z: -- m/s²");
    }

    private void showSensorUnavailable() {
        tvMovementStatus.setText("Accelerometer unavailable");
        tvMovementStatus.setTextColor(Color.parseColor("#FFCC80"));

        tvSensorDetails.setText(
                "This device does not provide an accelerometer."
        );

        tvFilmingAdvice.setText(
                "You can still use your reel plan. "
                        + "Support your phone on a stable surface before filming."
        );

        progressMovement.setProgress(0);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
            return;
        }

        // Initialise the gravity estimate from the first reading.
        if (previousTimestamp == 0) {
            firstTimestamp = event.timestamp;
            previousTimestamp = event.timestamp;

            System.arraycopy(event.values, 0, gravity, 0, 3);
            return;
        }

        double dt = (event.timestamp - previousTimestamp)
                / 1_000_000_000.0;

        previousTimestamp = event.timestamp;

        if (dt <= 0) {
            return;
        }

        // Low-pass filter estimates gravity.
        // Subtracting that estimate gives approximate movement acceleration.
        double gravityAlpha = 0.5 / (0.5 + dt);

        double squaredMagnitude = 0;

        for (int axis = 0; axis < 3; axis++) {
            gravity[axis] = (float) (
                    gravityAlpha * gravity[axis]
                            + (1 - gravityAlpha) * event.values[axis]
            );

            double linearAcceleration =
                    event.values[axis] - gravity[axis];

            squaredMagnitude +=
                    linearAcceleration * linearAcceleration;
        }

        double movement = Math.sqrt(squaredMagnitude);

        // Smooth the movement value to reduce flickering.
        double smoothingAlpha = Math.exp(-dt / 0.25);

        smoothedMovement =
                smoothingAlpha * smoothedMovement
                        + (1 - smoothingAlpha) * movement;

        // Limit visible updates to approximately ten per second.
        if (event.timestamp - lastUiTimestamp < UI_INTERVAL_NS) {
            return;
        }

        lastUiTimestamp = event.timestamp;

        tvRawReadings.setText(
                String.format(
                        Locale.UK,
                        "X: %.2f   Y: %.2f   Z: %.2f m/s²",
                        event.values[0],
                        event.values[1],
                        event.values[2]
                )
        );

        if (event.timestamp - firstTimestamp < WARMUP_NS) {
            tvMovementStatus.setText("Settling readings...");
            return;
        }

        updateMovementFeedback();
    }

    private void updateMovementFeedback() {
        tvMovementValue.setText(
                String.format(
                        Locale.UK,
                        "Estimated movement: %.2f m/s²",
                        smoothedMovement
                )
        );

        // Visual scale only: 3 m/s² or above fills the bar.
        int progress = (int) Math.round(
                Math.min(smoothedMovement / 3.0, 1.0) * 100
        );

        progressMovement.setProgress(progress);

        if (smoothedMovement < LOW_MOVEMENT_LIMIT) {
            tvMovementStatus.setText("Low movement detected");
            tvMovementStatus.setTextColor(Color.parseColor("#A5D6A7"));

            tvFilmingAdvice.setText(
                    "Your phone is relatively still. "
                            + "Check your framing and lighting before filming."
            );

        } else if (smoothedMovement < HIGH_MOVEMENT_LIMIT) {
            tvMovementStatus.setText("Hold steady");
            tvMovementStatus.setTextColor(Color.parseColor("#FFCC80"));

            tvFilmingAdvice.setText(
                    "Some movement is detected. "
                            + "Adjust your grip or support your phone."
            );

        } else {
            tvMovementStatus.setText("Movement detected");
            tvMovementStatus.setTextColor(Color.parseColor("#EF9A9A"));

            tvFilmingAdvice.setText(
                    "Pause and steady your phone. "
                            + "Try a stable surface or tripod for a fixed shot."
            );
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No additional behaviour is required for this prototype.
    }
}