package orlando.hci.brawlitout.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import orlando.hci.brawlitout.Adapters.RecyclerAdapter;
import orlando.hci.brawlitout.R;
import orlando.hci.brawlitout.Utils.DataFragment;
import orlando.hci.brawlitout.Utils.Player;

public class ScoreFragment extends Fragment {

    private ArrayList<Player> usersList;
    private RecyclerView recyclerView;
    private Context context;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        usersList = new ArrayList<>();
        //setContentView(R.layout.fragment_score);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        View root = inflater.inflate(R.layout.fragment_score, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        try {
            setUserInfo();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        setAdapter();
        return root;
    }

    private void setAdapter(){
        RecyclerAdapter adapter = new RecyclerAdapter(usersList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setUserInfo() throws IOException, ClassNotFoundException {
        DataFragment dataFragment = new DataFragment();
        usersList = dataFragment.load();
        usersList.add(new Player("name",(float)1.0));

        //LinkedHashMap preserve the ordering of elements in which they are inserted


    }

}