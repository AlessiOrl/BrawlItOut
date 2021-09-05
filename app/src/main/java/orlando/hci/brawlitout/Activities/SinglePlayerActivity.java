package orlando.hci.brawlitout.Activities;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import orlando.hci.brawlitout.R;
import orlando.hci.brawlitout.Utils.DataHandlerSingleton;
import pl.droidsonroids.gif.GifImageButton;

public class SinglePlayerActivity extends AppCompatActivity implements SensorEventListener, Window.Callback {

    private DataHandlerSingleton dataHandler;
    private GifImageButton imageButton;
    private long starttime;
    private long endtime;
    private double difference;

    private static final String TAG = "SinglePlayerActivity";
    private Float xTotal = (float) 0;
    private Float yTotal = (float) 0;
    private Float zTotal = (float) 0;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private final List<Float> xHistory = new ArrayList<>();
    private final List<Float> yHistory = new ArrayList<>();
    private final List<Float> zHistory = new ArrayList<>();
    private Integer gameState = 0;

    public SinglePlayerActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_player_activity);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        imageButton = findViewById(R.id.image_button);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);


        imageButton.setClickable(false);
        try {
            dataHandler = DataHandlerSingleton.getInstance(getApplicationContext());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        imageButton.setOnClickListener(v -> {
            stopGame();
            imageButtonEndState();
            try {
                saveScore();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            finish();
        });

        startGame();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSensorChanged(SensorEvent event) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.d(TAG, "X: " + event.values[0] +
                    " Y: " + event.values[1] +
                    " Z: " + event.values[2] +
                    " gameState " + gameState);
            if (xHistory.size() > 200) xHistory.remove(0);
            xHistory.add(event.values[0]);
            if (yHistory.size() > 200) yHistory.remove(0);
            yHistory.add(event.values[1]);
            if (zHistory.size() > 200) zHistory.remove(0);
            zHistory.add(event.values[2]);
            xTotal = xHistory.stream().reduce((float) 0, Float::sum);
            yTotal = yHistory.stream().reduce((float) 0, Float::sum);
            zTotal = zHistory.stream().reduce((float) 0, Float::sum);
        }
        if (gameState == 1 && yTotal < -700 && (zTotal < -100 || zTotal > 100)) {
            clearMovementHistory();
            gameState = 2;

            imageButtonStartState();
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            starttime = System.nanoTime();

        } else if (gameState == 2 && (xTotal < -160 || xTotal > 160 || zTotal < -160 || zTotal > 160)) {
            imageButtonStopState();
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            sensorManager.unregisterListener(this);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void imageButtonReadyState() {
        imageButton.setClickable(false);
        imageButton.setImageResource(R.drawable.down_white);
        imageButton.setVisibility(View.VISIBLE);

    }

    private void imageButtonStartState() {
        imageButton.setClickable(false);
        imageButton.setImageResource(R.drawable.black);
        imageButton.setVisibility(View.VISIBLE);
    }

    private void imageButtonStopState() {
        imageButton.setClickable(true);
        imageButton.setImageResource(R.drawable.green2);
        imageButton.setVisibility(View.VISIBLE);
    }

    private void imageButtonEndState() {
        imageButton.setClickable(false);
        imageButton.setVisibility(View.GONE);
        imageButton.setImageResource(R.drawable.black);
    }

    public void clearMovementHistory() {
        xHistory.clear();
        yHistory.clear();
        zHistory.clear();
    }

    private void saveScore() throws IOException, ClassNotFoundException {
        dataHandler.getCurrentPlayer().setTime(difference);
    }

    private void startGame() {
        gameState++;
        imageButtonReadyState();
    }

    public void stopGame() {
        gameState++;
        Log.d(TAG, "final summed values are  X: " + xTotal +
                " Y: " + yTotal +
                " Z: " + zTotal);
        endtime = System.nanoTime();
        difference = Math.floor(((endtime - starttime) / 1e9) * 1e6) / 1e6;
    }

}
