package orlando.hci.brawlitout.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import orlando.hci.brawlitout.Adapters.PlayerAdapter;
import orlando.hci.brawlitout.Utils.Player;
import orlando.hci.brawlitout.R;
import java.util.ArrayList;

public class MultiPFragment extends Fragment {

    private ArrayList<Player> players = new ArrayList<>();
    private int playercount = 0;
    private Button ok_btn;
    private EditText nplayer_edit;
    private PlayerAdapter platerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_multi, container, false);
        ok_btn = root.findViewById(R.id.ok_btn);
        nplayer_edit = root.findViewById(R.id.edit_nplayer);
        this.platerAdapter = new PlayerAdapter(players);
        RecyclerView recyclerView = root.findViewById(R.id.player_recyclerview);
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(platerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playercount = Integer.parseInt(nplayer_edit.getText().toString());
                for (int i = 0; i < playercount; i++) {
                    players.add(new Player(i, "Player " + (i+1)));
                    Toast.makeText(requireActivity(), "AO", Toast.LENGTH_SHORT).show();
                    platerAdapter.notifyItemInserted(i);
                }

            }
        });


        return root;
    }
}