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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

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
    private String username;
    private boolean isMulti;

    private static final String TAG = "SinglePlayerActivity";
    private Float xTotal = (float) 0;
    private Float yTotal = (float) 0;
    private Float zTotal = (float) 0;
    private SensorManager sensorManager;
    private Sensor accelerometer, gyroscope;
    private List<Float> xHistory = new ArrayList<>();
    private List<Float> yHistory = new ArrayList<>();
    private List<Float> zHistory = new ArrayList<>();
    private Integer gameState = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_player_activity);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        imageButton = (GifImageButton) findViewById(R.id.image_button);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);


        imageButton.setClickable(false);
        try {
            dataHandler = DataHandlerSingleton.getInstance(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopGame();
                imageButtonEndState();
                try {
                    saveScore();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                finish();
            }
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
            xTotal = xHistory.stream().reduce((float) 0, (a, b) -> a + b);
            yTotal = yHistory.stream().reduce((float) 0, (a, b) -> a + b);
            zTotal = zHistory.stream().reduce((float) 0, (a, b) -> a + b);
        }
        if (gameState == 1 && (yTotal > 800 || yTotal < -800) && (zTotal < -100 || zTotal > 100)) {
            clearMovementHistory();
            gameState = 2;

            imageButtonStartState();
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            starttime = System.nanoTime();

        } else if (gameState == 2 && (xTotal < -220 || xTotal > 230 || zTotal < -220 || zTotal > 230)) {
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
        imageButton.setImageResource(R.drawable.green);
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
