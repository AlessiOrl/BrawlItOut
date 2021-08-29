package orlando.hci.brawlitout.Utils;

import javax.xml.namespace.QName;

public class Player {

    private int id;
    private String name;
    private Float time;
    private Float lastTime;

    public Player(int id, String name){
        this.id = id;
        this.name = name;
        this.time = (float)-1.0;
    }

    public Player(String name, Float time){
        this.name = name;
        this.time = time;
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return player.getName() == this.name;
    }

    public String getName() {
        return name;
    }

    public Float getTime() {
        return time;
    }

    public Float getLastTime() { return lastTime; }

    @Override
    public String toString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(Float time) {
        this.time = time;
    }

    public void setLastTime(Float lastTime) {this.lastTime = lastTime;}
}
