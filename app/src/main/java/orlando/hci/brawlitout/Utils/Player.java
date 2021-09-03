package orlando.hci.brawlitout.Utils;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Player implements Serializable, Comparable<Player> {


    private String name;
    private double time;
    private boolean saved;

    public Player(String name) {
        this(name, -1.0);
    }

    public Player(String name, double time) {

        this.name = name;
        this.time = time;
        this.saved = false;
    }


    public String getName() {
        return name;
    }

    public double getTime() {
        return time;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    @Override
    public int compareTo(Player o) {
        return Double.compare(this.time, o.getTime());
    }
}
