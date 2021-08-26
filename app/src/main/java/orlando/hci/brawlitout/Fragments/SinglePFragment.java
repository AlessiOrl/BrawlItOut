package orlando.hci.brawlitout.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import orlando.hci.brawlitout.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

public class SinglePFragment extends Fragment {


    private long starttime;
    private long endtime;
    private double difference;
    private boolean is_running;
    private Button ss_btn;
    private Button r_btn;
    private TextView result;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_single, container, false);


        ss_btn = (Button) root.findViewById(R.id.button_start_pause);
        r_btn = (Button) root.findViewById(R.id.button_reset);
        result = (TextView) root.findViewById(R.id.chronometer);

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

        return root;

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