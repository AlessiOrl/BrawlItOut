package orlando.hci.brawlitout.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import orlando.hci.brawlitout.R;

public class DataFragment extends Fragment {
    public static final String DATA_PATH = "saved_data";
    public void add(Player player) throws IOException, ClassNotFoundException {

        FileInputStream fis = getContext().openFileInput(DATA_PATH);
        ObjectInputStream is = new ObjectInputStream(fis);
        ArrayList<Player> players = (ArrayList<Player>)is.readObject();
        is.close();
        fis.close();
        players.add(player);
        FileOutputStream fos = getContext().openFileOutput(DATA_PATH, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(players);
        os.close();
        fos.close();
        Toast.makeText(getActivity(),"Data saved!",Toast.LENGTH_SHORT).show();

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Player> load() throws IOException, ClassNotFoundException {
        FileInputStream fis = getContext().openFileInput(DATA_PATH);
        ObjectInputStream is = new ObjectInputStream(fis);
        ArrayList<Player> players = (ArrayList<Player>)is.readObject();
        is.close();
        fis.close();
        return players;
    }

    public void remove(Player player) throws IOException, ClassNotFoundException {
        FileInputStream fis = getContext().openFileInput(DATA_PATH);
        ObjectInputStream is = new ObjectInputStream(fis);
        ArrayList<Player> players = (ArrayList<Player>)is.readObject();
        is.close();
        fis.close();
        players.remove(player);
        FileOutputStream fos = getContext().openFileOutput(DATA_PATH, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(players);
        os.close();
        fos.close();
        Toast.makeText(getActivity(),"Data saved!",Toast.LENGTH_SHORT).show();
    }

    public void clear() throws IOException {
        FileOutputStream fos = getContext().openFileOutput(DATA_PATH, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(new ArrayList<Player>());
        os.close();
        fos.close();
        Toast.makeText(getActivity(),"Data cleared",Toast.LENGTH_SHORT).show();
    }
}
