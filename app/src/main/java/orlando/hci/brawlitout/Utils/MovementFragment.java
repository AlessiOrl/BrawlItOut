package orlando.hci.brawlitout.Utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class MovementFragment extends Fragment implements SensorEventListener {

    private static final String TAG = "MainActivity";
    private Float xTotal = (float)0;
    private Float yTotal = (float)0;
    private Float zTotal = (float)0;
    private SensorManager sensorManager;
    private Sensor accelerometer,gyroscope;
    private List<Float> xHistory = new ArrayList<Float>();
    private List<Float> yHistory = new ArrayList<Float>();
    private List<Float> zHistory = new ArrayList<Float>();
    private Integer gameState = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        startGame();


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            Log.d(TAG, "X: "+ event.values[0] +
                    " Y: "+ event.values[1] +
                    " Z: "+ event.values[2] +
                    " gameState " + gameState);
            if (xHistory.size() > 200) xHistory.remove(0);
            xHistory.add(event.values[0]);
            if (yHistory.size() > 200) yHistory.remove(0);
            yHistory.add(event.values[1]);
            if (zHistory.size() > 200) zHistory.remove(0);
            zHistory.add(event.values[2]);
            xTotal = xHistory.stream().reduce((float)0, (a,b) -> a+b);
            yTotal = yHistory.stream().reduce((float)0, (a,b) -> a+b);
            zTotal = zHistory.stream().reduce((float)0, (a,b) -> a+b);
        }
        if (gameState == 1 && (yTotal > 1300 || yTotal < -1300) && (zTotal < -100 || zTotal > 100)){
            stopGame();
            resetGame();
            gameState = 2;
        }
        else if (gameState == 2 && (xTotal < -220 || xTotal > 230  || zTotal < -220 || zTotal > 230)){

            stopGame();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void startGame() {
        gameState++;
        sensorManager.registerListener(MovementFragment.this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopGame() {
        gameState++;
        sensorManager.unregisterListener(MovementFragment.this);
        Log.d(TAG, "final summed values are  X: "+ xTotal +
                " Y: "+ yTotal +
                " Z: "+ zTotal);
    }

    public void resetGame() {
        xHistory.clear();
        yHistory.clear();
        zHistory.clear();
        gameState=0;
    }
}
