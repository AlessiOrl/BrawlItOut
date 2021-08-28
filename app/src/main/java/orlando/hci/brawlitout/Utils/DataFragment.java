package orlando.hci.brawlitout.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import orlando.hci.brawlitout.R;

public class DataFragment extends Fragment {
    public static final String SHARED_PREFERENCES = "sharedPrefs";
    public void add(Player player){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(player.getName(), player.getTime());
        editor.apply();
        Toast.makeText(getActivity(),"Data saved!",Toast.LENGTH_SHORT).show();

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Player> load(){
        ArrayList<Player> usersList = new ArrayList<>();
        Activity activity = getActivity();
        if(activity != null){
            SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
            Map<String,Float> players = (Map<String, Float>) sharedPreferences.getAll();
            LinkedHashMap<String, Float> sortedMap = new LinkedHashMap<>();

            players.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

            for (Map.Entry<String, Float> entry : players.entrySet()) usersList.add(new Player(entry.getKey(),entry.getValue()));

            return usersList;

        }
        return usersList;
    }

    public void remove(String key){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
    }

    public void clear(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
    }
}
