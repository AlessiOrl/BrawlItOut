package orlando.hci.brawlitout.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;

import orlando.hci.brawlitout.Utils.Player;
import orlando.hci.brawlitout.R;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

    private ArrayList<Player> players;

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
            cardView = (CardView) itemView.findViewById(R.id.cardview_sound);
            context = itemView.getContext();
            //add to fav btn
            modifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Player player = players.get(position);
                    modifyClick(player, position);
                }
            });
        }

        private void createTableOnFirstStart() {
        }

        // modify click
        private void modifyClick(Player player,int position) {
            LayoutInflater layoutinflater = LayoutInflater.from(context);
            View promptUserView = layoutinflater.inflate(R.layout.dialog_prompt_user, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.context);

            alertDialogBuilder.setView(promptUserView);

            final EditText userAnswer = (EditText) promptUserView.findViewById(R.id.username);

            alertDialogBuilder.setTitle("What's your username?");

            // prompt for username
            alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // and display the username on main activity layout
                    String name = userAnswer.getText().toString();
                    if (!name.equals("")) player.setName(name);
                    notifyItemChanged(position);
                }
            });
            // all set and time to build and show up!
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}
