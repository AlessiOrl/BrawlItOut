package orlando.hci.brawlitout.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;

import orlando.hci.brawlitout.Adapters.ScoreboardAdapter;
import orlando.hci.brawlitout.R;
import orlando.hci.brawlitout.Utils.DataHandlerSingleton;

public class ScoreMultiFragment extends Fragment {
    private DataHandlerSingleton dataHandler;

    private RecyclerView recyclerView;
    private Button close_btn;
    private Context context;
    ScoreboardAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        try {
            dataHandler = DataHandlerSingleton.getInstance(getActivity().getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        View root = inflater.inflate(R.layout.fragment_multi_score, container, false);
        recyclerView = root.findViewById(R.id.scoreboardMulti);
        close_btn = root.findViewById(R.id.close_button);

        setAdapter();

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataHandler.setIsmultirunning(false);
                getActivity().getSupportFragmentManager().popBackStack();
                dataHandler.setshowScore(false);
                dataHandler.clearMultiplayerList();
            }
        });
        return root;
    }

    private void setAdapter() {
        adapter = new ScoreboardAdapter(dataHandler.getMultiplayers());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}
