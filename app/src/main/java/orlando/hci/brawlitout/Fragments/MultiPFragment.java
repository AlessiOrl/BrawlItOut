package orlando.hci.brawlitout.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
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

import java.io.IOException;
import java.util.ArrayList;

import orlando.hci.brawlitout.Adapters.PlayerAdapter;
import orlando.hci.brawlitout.R;
import orlando.hci.brawlitout.Utils.DataHandlerSingleton;
import orlando.hci.brawlitout.Utils.Player;

public class MultiPFragment extends Fragment {

    private DataHandlerSingleton dataHandler;

    private Button up_btn;
    private Button down_btn;
    private Button start_btn;
    private TextView nplayer_text;
    private PlayerAdapter playerAdapter;


    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            this.dataHandler = DataHandlerSingleton.getInstance(getActivity().getApplicationContext());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        setHasOptionsMenu(true);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_multi, container, false);

        if (dataHandler.Ismultirunning()) startgame();
        up_btn = root.findViewById(R.id.up_btn);
        down_btn = root.findViewById(R.id.down_btn);
        start_btn = root.findViewById(R.id.btn_start);
        nplayer_text = root.findViewById(R.id.player_nmbr);
        RecyclerView recyclerView = root.findViewById(R.id.player_recyclerview);
        this.playerAdapter = new PlayerAdapter(dataHandler.getMultiplayers());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(playerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateView();
        up_btn.setOnClickListener(view -> {
            if (dataHandler.getMultiplayers().size() == 20) return;
            dataHandler.getMultiplayers().add(new Player("Player " + (dataHandler.getMultiplayers().size() + 1)));
            playerAdapter.notifyItemInserted(dataHandler.getMultiplayers().size());
            nplayer_text.setText("" + dataHandler.getMultiplayers().size());


        });

        down_btn.setOnClickListener(view -> {
            if (dataHandler.getMultiplayers().size() == 0 || dataHandler.getMultiplayers().size() == 1 && dataHandler.getMultiplayers().get(0).getName().equals("Player 1"))
                return;
            dataHandler.getMultiplayers().remove(dataHandler.getMultiplayers().size() - 1);
            playerAdapter.notifyItemRemoved(dataHandler.getMultiplayers().size());
            if (dataHandler.getMultiplayers().size() == 0) {
                up_btn.performClick();
            } else {
                nplayer_text.setText("" + dataHandler.getMultiplayers().size());
            }
        });

        start_btn.setOnClickListener(view -> {
            //dataHandler.setmultiplayerlist(multiplayer_list);
            if (dataHandler.getMultiplayers().isEmpty()) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.error_no_player, Toast.LENGTH_SHORT).show();
                return;
            }
            dataHandler.setCurrentMultiPlayer(dataHandler.nextPlayer());
            dataHandler.setIsmultirunning(true);
            //clearGame();
            startgame();
        });
        return root;
    }

    private void updateView() {
        if (nplayer_text != null)
            nplayer_text.setText(Integer.toString(dataHandler.getMultiplayers().size()));
        playerAdapter.notifyDataSetChanged();
    }

    public void startgame() {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, new MultiSinglePFragment())
                .setReorderingAllowed(true)
                .addToBackStack("start")
                .commit();
    }

}
