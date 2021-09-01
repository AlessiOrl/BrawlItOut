package orlando.hci.brawlitout.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.io.IOException;
import java.text.DecimalFormat;

import orlando.hci.brawlitout.Activities.SinglePlayerActivity;
import orlando.hci.brawlitout.R;
import orlando.hci.brawlitout.Utils.DataHandlerSingleton;
import orlando.hci.brawlitout.Utils.Player;

// Add Another Activiy for Movement
public class MultiSinglePFragment extends Fragment {

    private DataHandlerSingleton dataHandler;

    private Button ss_btn;
    private TextView result;
    private TextView usernameTextV;


    //private String username;
    private Player player;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            dataHandler = DataHandlerSingleton.getInstance(getActivity().getApplicationContext());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        dataHandler.setCurrentPlayer(dataHandler.nextPlayer());
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
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_multi_single, container, false);


        ss_btn = (Button) root.findViewById(R.id.button_start);
        result = (TextView) root.findViewById(R.id.chronometer);
        usernameTextV = (TextView) root.findViewById(R.id.text_username);

        if (checkEnd()) ss_btn.setText(R.string.next);


        ss_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //if next
                if (checkEnd()) {
                    nextGame();
                } else {
                    startGame();
                }

            }
        });
        return root;
    }

    private void nextGame() {
        this.player = dataHandler.nextPlayer();
        updateView(player.getName(), player.getTime());
    }

    private void saveScore(Player player) throws IOException, ClassNotFoundException {
        if (this.player.isSaved() | (this.player.getName().equals("")) | this.player.getTime() != -1.0)
            return;
        dataHandler.add(player);
        this.player.setSaved(true);
    }

    public void startGame() {
        dataHandler.setCurrentPlayer(player);
        Intent myIntent = new Intent(getActivity(), SinglePlayerActivity.class);
        startActivity(myIntent);
    }

    public void resetGame() {
        updateView("", -1);
        dataHandler.clearCurrentPlayer();
    }

    private void updateView(String username, double time) {
        usernameTextV.setText(username);

        if (time == -1) {
            result.setText("");
        } else {
            String displayTime = new DecimalFormat("#.###").format(time);
            result.setText(displayTime);
        }
    }

    private boolean checkEnd() {
        return this.player != null && this.player.getTime() != -1.0;
    }
}