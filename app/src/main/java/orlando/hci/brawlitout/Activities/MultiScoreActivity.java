package orlando.hci.brawlitout.Activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;

import orlando.hci.brawlitout.Adapters.ScoreboardAdapter;
import orlando.hci.brawlitout.R;
import orlando.hci.brawlitout.Utils.DataHandlerSingleton;

public class MultiScoreActivity extends AppCompatActivity {
    private DataHandlerSingleton dataHandler;

    private RecyclerView recyclerView;
    private Button close_btn;
    ScoreboardAdapter adapter;


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
        setAdapter();

        close_btn.setOnClickListener(v -> {
            dataHandler.setIsmultirunning(false);
            dataHandler.setshowScore(false);
            dataHandler.clearMultiplayerList();
            finish();
        });
    }


    private void setAdapter() {
        adapter = new ScoreboardAdapter(dataHandler.getMultiplayers());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}
