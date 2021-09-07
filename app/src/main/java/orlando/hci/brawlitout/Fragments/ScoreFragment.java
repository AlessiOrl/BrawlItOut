package orlando.hci.brawlitout.Fragments;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import orlando.hci.brawlitout.Adapters.ScoreboardAdapter;
import orlando.hci.brawlitout.R;
import orlando.hci.brawlitout.Utils.DataHandlerSingleton;
import orlando.hci.brawlitout.Utils.Player;

//TODO: add multiplayer implementation
public class ScoreFragment extends Fragment {

    private DataHandlerSingleton dataHandler;
    private RecyclerView recyclerView;
    private ScoreboardAdapter adapter;
    private View root;
    private static int counter = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        try {
            dataHandler = DataHandlerSingleton.getInstance(getActivity().getApplicationContext());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getApplicationContext();
        View root = inflater.inflate(R.layout.fragment_score, container, false);
        recyclerView = root.findViewById(R.id.scoreboard);
        if (counter == 0) {
            Toast toast = Toast.makeText(getActivity(), "You can delete players swiping on the sides", Toast.LENGTH_LONG);
            toast.show();
            counter++;
        }
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        setAdapter();
        this.root = root;
        return root;
    }

    private void setAdapter() {

        adapter = new ScoreboardAdapter(dataHandler.getPlayers(), true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Player deletedPlayer = dataHandler.getPlayers().get(position);
            //usersList.remove(position);
            try {
                dataHandler.remove(position);
                adapter.notifyItemRemoved(position - 1);

                Snackbar.make(recyclerView, deletedPlayer + " removed", Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> {
                            try {
                                dataHandler.add(position, deletedPlayer);
                                //adapter.notifyItemRangeChanged(position, dataHandler.getPlayers().size() - position); efficient but not cool
                                adapter.notifyDataSetChanged(); //cool but not efficient
                            } catch (IOException | ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }).setAnchorView(getActivity().findViewById(R.id.nav_view))
                        .show();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            setAdapter();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(getContext(), R.color.remove))
                    .addActionIcon(R.drawable.round_delete_sweep_20)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater layoutinflater = LayoutInflater.from(getContext());
        alertDialogBuilder.setTitle("Delete leaderboard");
        alertDialogBuilder.setMessage("Are you sure that you want to clear the entire leaderboard?\n\nThis operation cannot be undone.");

        // prompt for username
        alertDialogBuilder.setPositiveButton("Delete leaderboard", (dialog, id) -> {
            // and display the username on main activity layout
            Toast toast = Toast.makeText(getActivity(), "All player deleted", Toast.LENGTH_LONG);
            toast.show();
            try {
                dataHandler.clearPlayers();
            } catch (IOException e) {
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
        });
        alertDialogBuilder.setNegativeButton("Cancel", null);
        // all set and time to build and show up!

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.parseColor("#E53935"));

        return super.onOptionsItemSelected(item);
    }
}