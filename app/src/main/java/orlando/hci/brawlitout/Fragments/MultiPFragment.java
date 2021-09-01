package orlando.hci.brawlitout.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import orlando.hci.brawlitout.Adapters.PlayerAdapter;
import orlando.hci.brawlitout.Utils.DataHandlerSingleton;
import orlando.hci.brawlitout.Utils.Player;
import orlando.hci.brawlitout.R;

import java.io.IOException;
import java.util.ArrayList;

public class MultiPFragment extends Fragment {

    private DataHandlerSingleton dataHandler;

    private ArrayList<Player> multiplayer_list = new ArrayList<>();
    private int playercount = 0;
    private Button up_btn;
    private Button down_btn;
    private Button start_btn;
    private TextView nplayer_text;
    private PlayerAdapter playerAdapter;

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

        if (this.dataHandler.Ismultirunning()) startgame();

        setHasOptionsMenu(true);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_multi, container, false);


        up_btn = root.findViewById(R.id.up_btn);
        down_btn = root.findViewById(R.id.down_btn);
        start_btn = root.findViewById(R.id.btn_start);
        nplayer_text = root.findViewById(R.id.player_nmbr);
        RecyclerView recyclerView = root.findViewById(R.id.player_recyclerview);

        this.playerAdapter = new PlayerAdapter(multiplayer_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(playerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playercount += 1;
                multiplayer_list.add(new Player("Player " + (playercount)));
                playerAdapter.notifyItemInserted(playercount);
                nplayer_text.setText("" + playercount);
            }
        });

        down_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playercount == 0) return;
                playercount -= 1;
                multiplayer_list.remove(playercount);
                playerAdapter.notifyItemRemoved(playercount);
                nplayer_text.setText("" + playercount);
            }
        });

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataHandler.setmultiplayerlist(multiplayer_list);
                if (dataHandler.getMultiplayers().isEmpty())
                    return; //todo: error message no players added
                dataHandler.setCurrentMultiPlayer(dataHandler.nextPlayer());
                dataHandler.setIsmultirunning(true);
                clearGame();
                startgame();
            }
        });
        return root;
    }

    private void updateView() {
        playerAdapter.notifyItemRangeChanged(0, playercount);
    }

    public void clearGame() {
        playercount = 0;
        multiplayer_list = new ArrayList<>();
        nplayer_text.setText("");
    }

    public void startgame() {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, new MultiSinglePFragment())
                .addToBackStack(null)
                .commit();
    }
}
