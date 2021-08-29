package orlando.hci.brawlitout.Fragments;

import android.content.Context;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import orlando.hci.brawlitout.Adapters.PlayerAdapter;
import orlando.hci.brawlitout.Utils.Player;
import orlando.hci.brawlitout.R;
import java.util.ArrayList;

public class MultiPFragment extends Fragment {

    private ArrayList<Player> players = new ArrayList<>();
    private int playercount = 0;
    private Button up_btn;
    private Button down_btn;
    private Button start_btn;
    private TextView nplayer_text;
    private PlayerAdapter platerAdapter;
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        View root = inflater.inflate(R.layout.fragment_multi, container, false);
        up_btn = root.findViewById(R.id.up_btn);
        down_btn = root.findViewById(R.id.down_btn);
        start_btn = root.findViewById(R.id.btn_start);
        nplayer_text = root.findViewById(R.id.player_nmbr);
        RecyclerView recyclerView = root.findViewById(R.id.player_recyclerview);

        this.platerAdapter = new PlayerAdapter(players);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(platerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playercount += 1;
                players.add(new Player(playercount, "Player " + (playercount)));
                platerAdapter.notifyItemInserted(playercount);
                nplayer_text.setText(""+playercount);
            }
        });

        down_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playercount == 0) return;
                playercount -= 1;
                players.remove(playercount);
                platerAdapter.notifyItemRemoved(playercount);
                nplayer_text.setText(""+playercount);
            }
        });

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SinglePFragment singlePFragment = new SinglePFragment();
                FragmentTransaction fragmentTransaction =  getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, singlePFragment);
                fragmentTransaction.commit();
            }
        });
        return root;
    }

}
