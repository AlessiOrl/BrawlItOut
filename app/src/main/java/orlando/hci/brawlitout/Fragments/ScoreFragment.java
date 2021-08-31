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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

import orlando.hci.brawlitout.Adapters.RecyclerAdapter;
import orlando.hci.brawlitout.R;
import orlando.hci.brawlitout.Utils.DataHandlerSingleton;
import orlando.hci.brawlitout.Utils.Player;

public class ScoreFragment extends Fragment {

    private DataHandlerSingleton dataHandler ;


    private ArrayList<Player> usersList;
    private RecyclerView recyclerView;
    private Context context;
    RecyclerAdapter adapter;




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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        try {
            dataHandler= DataHandlerSingleton.getInstance(getActivity().getApplicationContext());
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
        adapter = new RecyclerAdapter(usersList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setUserInfo() throws IOException, ClassNotFoundException {

        usersList = dataHandler.getPlayers();
        //todo: remove, this is a debug line
        dataHandler.add(new Player("name", (float) (Math.round((float)1.2345 *1000.0)/1000.0)));
        usersList.forEach(x -> x.setTime((float) (Math.round(x.getTime()*1000.0)/1000.0)));
        //usersList.add(new Player("name", (float) (Math.round((float)1.2345 *1000.0)/1000.0)));
        //adapter.notifyItemInserted(usersList.size()-1);

        //LinkedHashMap preserve the ordering of elements in which they are inserted


    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            //usersList.remove(position);
            try {
                dataHandler.remove(position);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            adapter.notifyItemRemoved(position);
            setAdapter();

        }
    };


}