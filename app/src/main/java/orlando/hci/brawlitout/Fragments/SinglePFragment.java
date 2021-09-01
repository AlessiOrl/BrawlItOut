package orlando.hci.brawlitout.Fragments;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import orlando.hci.brawlitout.Activities.SinglePlayerActivity;
import orlando.hci.brawlitout.R;
import orlando.hci.brawlitout.Utils.DataHandlerSingleton;
import orlando.hci.brawlitout.Utils.Player;

import java.io.IOException;
import java.text.DecimalFormat;

// Add Another Activiy for Movement
public class SinglePFragment extends Fragment {

    private DataHandlerSingleton dataHandler;

    private Button ss_btn;
    private Button r_btn;
    private TextView result;
    private EditText usernameTextE;

    //private String username;
    private Player player;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.dataHandler = DataHandlerSingleton.getInstance(getActivity().getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        setHasOptionsMenu(true);
    }


    @Override
    public void onResume() {
        super.onResume();
        this.player = dataHandler.getCurrentPlayer();
        if (this.player != null) {
            updateView(this.player.getName(), this.player.getTime());
            try {
                saveScore(this.player);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_single, container, false);


        ss_btn = (Button) root.findViewById(R.id.button_start);
        r_btn = (Button) root.findViewById(R.id.button_reset);
        result = (TextView) root.findViewById(R.id.chronometer);
        usernameTextE = (EditText) root.findViewById(R.id.edit_username);


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
        return root;

    }

    private void saveScore(Player player) throws IOException, ClassNotFoundException {
        if (this.player.isSaved() | (player.getName().equals(""))) return;
        dataHandler.add(player);
        this.player.setSaved(true);
    }

    public void startGame() {
        String username = usernameTextE.getText().toString();
        dataHandler.setCurrentPlayer(new Player(username));
        Intent myIntent = new Intent(getActivity(), SinglePlayerActivity.class);
        startActivity(myIntent);
    }

    public void resetGame() {
        updateView("", -1);
        dataHandler.clearCurrentPlayer();
    }

    private void updateView(String username, double time) {
        usernameTextE.setText(username);
        if (time == -1) {
            result.setText("");
        } else {
            String displayTime = new DecimalFormat("#.###").format(time);
            result.setText(displayTime);
        }
    }

}
