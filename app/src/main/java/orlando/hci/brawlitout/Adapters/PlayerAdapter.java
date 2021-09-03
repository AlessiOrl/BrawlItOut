package orlando.hci.brawlitout.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import orlando.hci.brawlitout.R;
import orlando.hci.brawlitout.Utils.Player;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

    private final ArrayList<Player> players;

    public PlayerAdapter(ArrayList<Player> players) {
        this.players = players;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gamer,
                parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PlayerAdapter.ViewHolder holder, int position) {
        final Player player = players.get(position);
        holder.titleTextView.setText(player.getName());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        Button modifyBtn;
        CardView cardView;
        Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            modifyBtn = itemView.findViewById(R.id.modifyBtn);
            cardView = itemView.findViewById(R.id.cardview_sound);
            context = itemView.getContext();
            //add to fav btn
            modifyBtn.setOnClickListener(view -> {
                int position = getAdapterPosition();
                Player player = players.get(position);
                modifyClick(player, position);
            });
        }

        // modify click
        private void modifyClick(Player player,int position) {
            LayoutInflater layoutinflater = LayoutInflater.from(context);
            View promptUserView = layoutinflater.inflate(R.layout.dialog_prompt_user, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.context);

            alertDialogBuilder.setView(promptUserView);

            final EditText userAnswer = promptUserView.findViewById(R.id.username);

            alertDialogBuilder.setTitle("What's your username?");

            // prompt for username
            alertDialogBuilder.setPositiveButton("Ok", (dialog, id) -> {
                // and display the username on main activity layout
                String name = userAnswer.getText().toString().trim();
                if (!name.equals("")) player.setName(name);
                notifyItemChanged(position);
            });
            // all set and time to build and show up!
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}
