package orlando.hci.brawlitout.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import orlando.hci.brawlitout.R;

public class DataFragment extends Fragment {
    public static final String SHARED_PREFERENCES = "sharedPrefs";
    public void add(Player player){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Integer.toString(player.getId()), player.getName()+'/'+Float.toString(player.getTime()));
        //editor.putFloat(Integer.toString(player.getId()), player.getTime());
        editor.apply();
        Toast.makeText(getActivity(),"Data saved!",Toast.LENGTH_SHORT).show();

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Player> load(){
        ArrayList<Player> usersList = new ArrayList<>();
        Activity activity = getActivity();
        if(activity != null){
            SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
            Map<String,String> players = (Map<String, String>) sharedPreferences.getAll();
            LinkedHashMap<String, Float> sortedMap = new LinkedHashMap<>();

            for (Map.Entry<String, String> entry : players.entrySet()) {
                String[] parts = entry.getValue().split("/");
                usersList.add(new Player(Integer.parseInt(entry.getKey()),parts[0],Float.parseFloat(parts[1])));
            }
            usersList.sort(Comparator.comparing(Player::getTime));
            return usersList;

        }
        return usersList;
    }

    public void remove(Integer key){
        String k = Integer.toString(key);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(k);
    }

    public void clear(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
    }
}
