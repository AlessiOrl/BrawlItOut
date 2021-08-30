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

    private DataHandlerSingleton dataHandler ;

    private ArrayList<Player> multi_players = new ArrayList<>();
    private int playercount = 0;
    private Button up_btn;
    private Button down_btn;
    private Button start_btn;
    private TextView nplayer_text;
    private PlayerAdapter platerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        this.platerAdapter = new PlayerAdapter(multi_players);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(platerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        try {
            dataHandler= DataHandlerSingleton.getInstance(getActivity().getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playercount += 1;
                multi_players.add(new Player("Player " + (playercount)));
                platerAdapter.notifyItemInserted(playercount);
                nplayer_text.setText(""+playercount);
            }
        });

        down_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playercount == 0) return;
                playercount -= 1;
                multi_players.remove(playercount);
                platerAdapter.notifyItemRemoved(playercount);
                nplayer_text.setText(""+playercount);
            }
        });

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Player p : multi_players) {
                    SinglePFragment singlePFragment = null;
                    try {
                        singlePFragment = new SinglePFragment(p, getActivity());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    FragmentTransaction fragmentTransaction =  getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment, singlePFragment);
                    fragmentTransaction.commit();
                }

            }
        });
        return root;
    }

}
