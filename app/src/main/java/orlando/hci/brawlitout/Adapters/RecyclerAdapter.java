package orlando.hci.brawlitout.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import orlando.hci.brawlitout.R;
import orlando.hci.brawlitout.Utils.Player;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private ArrayList<Player> usersList;

    public RecyclerAdapter(ArrayList<Player> usersList){
        this.usersList = usersList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView index;
        private TextView place;
        private TextView nameTxt;
        private TextView time;
        public MyViewHolder(final View view){
            super(view);
            place = view.findViewById(R.id.place);
            nameTxt = view.findViewById(R.id.nameTxt);
            time = view.findViewById(R.id.time);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name = usersList.get(position).getName();
        Float time = usersList.get(position).getTime();

        holder.place.setText(Integer.toString(position+1));
        holder.place.setTextSize(30);
        holder.nameTxt.setText(name);
        holder.nameTxt.setTextSize(30);
        holder.time.setText(time.toString());
        holder.time.setTextSize(30);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }


}
