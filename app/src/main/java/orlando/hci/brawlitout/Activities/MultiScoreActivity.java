package orlando.hci.brawlitout.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import orlando.hci.brawlitout.Adapters.ScoreboardAdapter;
import orlando.hci.brawlitout.R;
import orlando.hci.brawlitout.Utils.DataHandlerSingleton;
import orlando.hci.brawlitout.Utils.Player;

public class MultiScoreActivity extends AppCompatActivity {
    private DataHandlerSingleton dataHandler;

    private RecyclerView recyclerView;
    private Button close_btn;
    ScoreboardAdapter adapter;
    private TextView username_1;
    private TextView username_2;
    private TextView username_3;

    private TextView time_1;
    private TextView time_2;
    private TextView time_3;

    private ImageView image_1;
    private ImageView image_2;
    private ImageView image_3;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            dataHandler = DataHandlerSingleton.getInstance(getApplicationContext());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        setContentView(R.layout.activity_multi_score);
        recyclerView = findViewById(R.id.scoreboardMulti);
        close_btn = findViewById(R.id.close_button);
        username_1 = findViewById(R.id.username_1);
        username_2 = findViewById(R.id.username_2);
        username_3 = findViewById(R.id.username_3);

        time_1 = findViewById(R.id.time_1);
        time_2 = findViewById(R.id.time_2);
        time_3 = findViewById(R.id.time_3);

        image_1 = findViewById(R.id.image_1);
        image_2 = findViewById(R.id.image_2);
        image_3 = findViewById(R.id.image_3);

        setAdapter();

        close_btn.setOnClickListener(v -> {
            dataHandler.setIsmultirunning(false);
            dataHandler.setshowScore(false);
            dataHandler.clearMultiplayerList();
            finish();
        });
    }


    private void setAdapter() {
        dataHandler.getMultiplayers().sort(Player::compareTo);
        List<Player> podium = dataHandler.getMultiplayers().subList(0, Math.min(dataHandler.getMultiplayers().size(), 3));

        for (int i = 0; i < podium.size(); i++) {
            switch (i) {
                case 0:
                    username_1.setVisibility(View.VISIBLE);
                    time_1.setVisibility(View.VISIBLE);
                    image_1.setVisibility(View.VISIBLE);
                    username_1.setText(podium.get(i).getName());
                    time_1.setText(new DecimalFormat("#.###").format(podium.get(i).getTime()));
                    break;
                case 1:
                    username_2.setVisibility(View.VISIBLE);
                    time_2.setVisibility(View.VISIBLE);

                    image_2.setVisibility(View.VISIBLE);
                    username_2.setText(podium.get(i).getName());
                    time_2.setText(new DecimalFormat("#.###").format(podium.get(i).getTime()));
                    break;

                case 2:
                    username_3.setVisibility(View.VISIBLE);
                    time_3.setVisibility(View.VISIBLE);
                    image_3.setVisibility(View.VISIBLE);
                    username_3.setText(podium.get(i).getName());
                    time_3.setText(new DecimalFormat("#.###").format(podium.get(i).getTime()));
                    break;

            }
        }

        if (dataHandler.getMultiplayers().size() > 3) {
            adapter = new ScoreboardAdapter(new ArrayList<>(dataHandler.getMultiplayers().subList(3, dataHandler.getMultiplayers().size())), false);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }
    }
}
