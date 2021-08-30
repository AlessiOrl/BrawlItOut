package orlando.hci.brawlitout.Fragments;

import android.content.Context;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import orlando.hci.brawlitout.R;
import pl.droidsonroids.gif.GifImageButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SinglePFragment extends Fragment implements SensorEventListener {


    private long starttime;
    private long endtime;
    private double difference;
    private Button ss_btn;
    private Button r_btn;
    private GifImageButton imageButton;
    private TextView result;

    private static final String TAG = "MainActivity";
    private Float xTotal = (float) 0;
    private Float yTotal = (float) 0;
    private Float zTotal = (float) 0;
    private SensorManager sensorManager;
    private Sensor accelerometer, gyroscope;
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
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_single, container, false);


        ss_btn = (Button) root.findViewById(R.id.button_start);
        r_btn = (Button) root.findViewById(R.id.button_reset);
        result = (TextView) root.findViewById(R.id.chronometer);
        imageButton = (GifImageButton) root.findViewById(R.id.image_button);

        RelativeLayout rl = (RelativeLayout) root.findViewById(R.id.single_relative);

        ss_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startGame();
            }
        });

        r_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetGame();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopGame();
                imageButtonEndState();
            }
        });

        return root;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSensorChanged(SensorEvent event) {
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

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
        if (gameState == 1 && (yTotal > 1100 || yTotal < -1100) && (zTotal < -100 || zTotal > 100)) {
            clearMovementHistory();
            gameState = 2;

            imageButtonStartState();
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            starttime = System.nanoTime();

        } else if (gameState == 2 && (xTotal < -220 || xTotal > 230 || zTotal < -220 || zTotal > 230)) {
            imageButtonStopState();
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            sensorManager.unregisterListener(SinglePFragment.this);

        }
    }

    private void iamgeButtonReadyState() {
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


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void startGame() {
        gameState++;
        sensorManager.registerListener(SinglePFragment.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        ss_btn.setVisibility(View.INVISIBLE);
        iamgeButtonReadyState();
    }

    public void stopGame() {
        gameState++;
        Log.d(TAG, "final summed values are  X: " + xTotal +
                " Y: " + yTotal +
                " Z: " + zTotal);
        endtime = System.nanoTime();

        difference = (endtime - starttime) / 1e3;
        updateTime(difference / 1e3);
    }

    public void clearMovementHistory() {
        xHistory.clear();
        yHistory.clear();
        zHistory.clear();
    }

    public void resetGame() {
        ss_btn.setVisibility(View.VISIBLE);
        clearMovementHistory();
        sensorManager.unregisterListener(SinglePFragment.this);
        gameState = 0;
        starttime = 0;
        endtime = 0;
        updateTime(-1);
    }

    private void updateTime(double updatedTime) {
        if (updatedTime == -1) {
            result.setText("");
        } else {
            DateFormat format = new SimpleDateFormat("ss.SSS");
            String displayTime = format.format(updatedTime);
            result.setText(displayTime);
        }
    }

}
