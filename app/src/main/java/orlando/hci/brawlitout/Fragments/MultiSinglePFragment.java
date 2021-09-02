package orlando.hci.brawlitout.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.text.DecimalFormat;

import orlando.hci.brawlitout.Activities.MultiSinglePlayerActivity;
import orlando.hci.brawlitout.Activities.MultiScoreActivity;
import orlando.hci.brawlitout.Activities.SinglePlayerActivity;
import orlando.hci.brawlitout.R;
import orlando.hci.brawlitout.Utils.DataHandlerSingleton;
import orlando.hci.brawlitout.Utils.Player;

public class MultiSinglePFragment extends Fragment {

    private DataHandlerSingleton dataHandler;

    private Button ss_btn;
    private TextView result;
    private TextView usernameTextV;

    boolean doubleBackToExitPressedOnce = false;
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





        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                if (doubleBackToExitPressedOnce) {
                    dataHandler.setIsmultirunning(false);
                    dataHandler.clearMultiplayerList();
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                    return;
                }
                doubleBackToExitPressedOnce = true;
                Toast.makeText(getActivity().getApplicationContext(), "Please click BACK again to end the game", Toast.LENGTH_SHORT).show();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()
        setHasOptionsMenu(true);
    }


    @Override
    public void onResume() {
        super.onResume();
        this.player = dataHandler.getCurrentMultiPlayer();
        if (this.player != null) {
            updateView(this.player.getName(), this.player.getTime());
            try {
                saveScore(this.player);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (!dataHandler.Ismultirunning() && !dataHandler.showScore()) getActivity().getSupportFragmentManager().popBackStack();

    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_multi_single, container, false);




        MultiSinglePFragment thisf = this;
        ss_btn = (Button) root.findViewById(R.id.button_start);
        result = (TextView) root.findViewById(R.id.chronometer);
        usernameTextV = (TextView) root.findViewById(R.id.text_username);


        ss_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //if next
                if (checkEnd()) {
                    try {
                        nextGame();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    startGame();
                }

            }
        });
        return root;
    }

    public void startGame() {
        dataHandler.setCurrentMultiPlayer(player);
        Intent myIntent = new Intent(getActivity(), MultiSinglePlayerActivity.class);
        startActivity(myIntent);
    }

    private void nextGame() throws InterruptedException {
        this.player = dataHandler.nextPlayer();
        if (this.player == null) {
            endgame();
            return;
        }

        updateView(player.getName(), player.getTime());
    }

    private void endgame() throws InterruptedException {
        dataHandler.setshowScore(true);
        dataHandler.setIsmultirunning(false);
        showScore();
    }

    private void saveScore(Player player) throws IOException, ClassNotFoundException {
        if (this.player.isSaved() | (this.player.getName().equals("")) | this.player.getTime() < 0)
            return;
        dataHandler.add(player);
        this.player.setSaved(true);
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

        if (checkEnd()) {
            ss_btn.setText(R.string.next);
        } else {
            ss_btn.setText(R.string.start);
        }

    }

    private boolean checkEnd() {
        return this.player != null && this.player.getTime() > 0;
    }

    private void showScore() throws InterruptedException {
        Intent myIntent = new Intent(getActivity(), MultiScoreActivity.class);
        startActivity(myIntent);
    }

}