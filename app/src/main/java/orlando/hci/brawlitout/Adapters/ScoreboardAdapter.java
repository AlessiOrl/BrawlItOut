package orlando.hci.brawlitout.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;

import orlando.hci.brawlitout.R;
import orlando.hci.brawlitout.Utils.Player;

public class ScoreboardAdapter extends RecyclerView.Adapter<ScoreboardAdapter.MyViewHolder> {
    private ArrayList<Player> usersList;

    public ScoreboardAdapter(ArrayList<Player> usersList) {
        usersList.sort(Comparator.comparing(Player::getTime));
        this.usersList = usersList;
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

        holder.place.setText(Integer.toString(position + 1));
        holder.nameTxt.setText(name);
        holder.time.setText(new DecimalFormat("#.###").format(time));
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView place;
        private TextView nameTxt;
        private TextView time;

        public MyViewHolder(final View view) {
            super(view);
            place = view.findViewById(R.id.place);
            nameTxt = view.findViewById(R.id.nameTxt);
            time = view.findViewById(R.id.time);
        }
    }


}
