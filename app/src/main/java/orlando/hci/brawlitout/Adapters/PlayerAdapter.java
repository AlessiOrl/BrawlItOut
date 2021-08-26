package orlando.hci.brawlitout.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            modifyBtn = itemView.findViewById(R.id.modifyBtn);
            cardView = (CardView) itemView.findViewById(R.id.cardview_sound);

            //add to fav btn
            modifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Player player = players.get(position);
                    modifyClick(player, modifyBtn);

                }
            });
        }

        private void createTableOnFirstStart() {
        }

        // modify click
        private void modifyClick(Player player, Button modifyBtn) {
            //allert
            return;
        }
    }
}