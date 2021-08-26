package orlando.hci.brawlitout.Utils;

import javax.xml.namespace.QName;

public class Player {

    private int id;
    private String name;
    private Double time;

    public Player(int id, String name){
        this.id = id;
        this.name = name;
        this.time = -1.0;
    }

    public String getName() {
        return name;
    }

    public Double getTime() {
        return time;
    }
    @Override
    public String toString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(Double time) {
        this.time = time;
    }
}
