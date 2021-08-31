package orlando.hci.brawlitout.Utils;

import java.io.Serializable;

import javax.xml.namespace.QName;

public class Player implements Serializable {


    private String name;
    private double time;

    public Player(String name) {

        this.name = name;
        this.time = -1.0;
    }

    public Player(String name, double time) {

        this.name = name;
        this.time = time;
    }


    public String getName() {
        return name;
    }

    public double getTime() {
        return time;
    }

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

}
