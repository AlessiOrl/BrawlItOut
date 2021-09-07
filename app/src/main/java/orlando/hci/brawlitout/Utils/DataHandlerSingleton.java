package orlando.hci.brawlitout.Utils;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DataHandlerSingleton {
    public static final String DATA_PATH = "saved_data";

    private static DataHandlerSingleton instance = null;

    private final ArrayList<Player> players;
    private ArrayList<Player> multiplayer = new ArrayList<>();
    private boolean ismultirunning;
    private Player currentPlayer = null;
    private Player currentmultiPlayer = null;

    private int currentPlayerIndex = 0;
    private final Context context;
    private boolean showScore;

    // Costruttore invisibile
    private DataHandlerSingleton(Context context) throws IOException, ClassNotFoundException {
        this.context = context;
        this.players = this.load();
        this.multiplayer.add(new Player("Player 1"));

    }

    public static DataHandlerSingleton getInstance(Context context) throws IOException, ClassNotFoundException {
        // Crea l'oggetto solo se NON esiste:
        if (instance == null) {
            instance = new DataHandlerSingleton(context);
        }
        return instance;
    }

    public void add(Player player) throws IOException, ClassNotFoundException {
        players.add(player);
        players.sort(Player::compareTo);
        if (players.size() > 20) {
            players.remove(20);
        }
        save(players);
    }

    public void add(int position, Player player) throws IOException, ClassNotFoundException {
        players.add(position, player);
        save(players);
    }

    public void clearMultiplayerList() {
        this.ismultirunning = false;
        this.multiplayer = new ArrayList<>();
        this.multiplayer.add(new Player("Player 1"));
        this.currentPlayerIndex = 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<Player> load() throws IOException, ClassNotFoundException {
        //Check if file exists, if not create it
        File file = context.getFileStreamPath(DATA_PATH);
        if (!file.exists()) save(new ArrayList<>());

        FileInputStream fis = context.openFileInput(DATA_PATH);
        ObjectInputStream is = new ObjectInputStream(fis);
        ArrayList<Player> players = (ArrayList<Player>) is.readObject();
        is.close();
        fis.close();
        return players;

    }

    public void remove(int position) throws IOException, ClassNotFoundException {
        players.remove(position);
        save(players);
    }

    public void save(ArrayList<Player> ps) throws IOException {
        FileOutputStream fos = context.openFileOutput(DATA_PATH, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(ps);
        os.close();
        fos.close();
    }

    public Player nextPlayer() {
        if (multiplayer.size() <= currentPlayerIndex) return null;
        return multiplayer.get(currentPlayerIndex++);
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentMultiPlayer(Player player) {
        this.currentmultiPlayer = player;
    }

    public Player getCurrentMultiPlayer() {
        return currentmultiPlayer;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Player> getMultiplayers() {
        return multiplayer;
    }

    public void clearCurrentPlayer() {
        this.currentPlayer = null;
    }


    public void setIsmultirunning(boolean ismultirunning) {
        this.ismultirunning = ismultirunning;
    }

    public boolean Ismultirunning() {
        return this.ismultirunning;
    }

    public void setshowScore(boolean showScore) {
        this.showScore = showScore;
    }

    public boolean showScore() {
        return this.showScore;
    }

    public void restartMultiGame() {
        this.currentPlayerIndex = 0;
        this.currentmultiPlayer = null;
        for (Player p : this.multiplayer) {
            p.setTime(-1.0);
        }
    }
}

