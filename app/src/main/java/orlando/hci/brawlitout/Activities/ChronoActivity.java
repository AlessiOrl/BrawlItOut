package orlando.hci.brawlitout.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import orlando.hci.brawlitout.R;

public class ChronoActivity extends Activity {
    private long starttime;
    private long endtime;
    private double difference;
    private boolean is_running;
    private Button ss_btn;
    private Button r_btn;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometer);
        Intent i = getIntent();

        ss_btn = (Button) findViewById(R.id.button_start_pause);
        r_btn = (Button) findViewById(R.id.button_reset);
        result = (TextView) findViewById(R.id.chronometer);

        ss_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (is_running)  // condition on which you check whether it's start or stop
                {
                    onClickStop(v);
                } else {
                    onClickStart(v);
                }
            }
        });

        r_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickReset(v);
            }
        });
    }

    private void onClickStart(View view) {
        starttime = System.nanoTime();
        ss_btn.setText(R.string.stop);
        is_running = true;
    }

    private void onClickStop(View view) {
        endtime = System.nanoTime();
        ss_btn.setText(R.string.start);
        is_running = false;
        difference = (endtime - starttime) / 1e3;
        updateTime(difference/ 1e3);
    }

    private void onClickReset(View view) {
        starttime = 0;
        endtime = 0;
        ss_btn.setText(R.string.start);
        result.setText("");
    }

    private void updateTime(double updatedTime) {
        DateFormat format = new SimpleDateFormat("ss.SSS");
        String displayTime = format.format(updatedTime);
        result.setText(displayTime);
    }

}