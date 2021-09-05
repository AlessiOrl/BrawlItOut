package orlando.hci.brawlitout.Adapters;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import orlando.hci.brawlitout.R;
import orlando.hci.brawlitout.Utils.Player;

public class ScoreboardAdapter extends RecyclerView.Adapter<ScoreboardAdapter.MyViewHolder> {
    private final ArrayList<Player> usersList;
    private boolean medals;

    public ScoreboardAdapter(ArrayList<Player> usersList, boolean medals) {
        usersList.sort(Player::compareTo);
        this.usersList = usersList;
        this.medals = medals;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name = usersList.get(position).getName();
        double time = usersList.get(position).getTime();
        if (medals) {
            switch (position) {
                case 0:
                    holder.image.setImageResource(R.drawable.medals_gold);
                    holder.image.setVisibility(View.VISIBLE);
                    holder.place.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    holder.image.setImageResource(R.drawable.medals_silver);
                    holder.image.setVisibility(View.VISIBLE);
                    holder.place.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    holder.image.setImageResource(R.drawable.medals_bronze);
                    holder.image.setVisibility(View.VISIBLE);
                    holder.place.setVisibility(View.INVISIBLE);
                    break;
                default:
                    holder.place.setText(Integer.toString(position + 1));
                    holder.place.setVisibility(View.VISIBLE);
                    holder.image.setVisibility(View.INVISIBLE);
                    break;
            }
        } else {
            holder.place.setText(Integer.toString(position + 4));
            holder.place.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.INVISIBLE);
        }
        holder.nameTxt.setText(name);
        holder.time.setText(new DecimalFormat("#.###").format(time));
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView place;
        private final TextView nameTxt;
        private final TextView time;
        private final ImageView image;

        public MyViewHolder(final View view) {
            super(view);
            place = view.findViewById(R.id.place);
            nameTxt = view.findViewById(R.id.nameTxt);
            time = view.findViewById(R.id.time);
            image = view.findViewById(R.id.image);
        }
    }


}
