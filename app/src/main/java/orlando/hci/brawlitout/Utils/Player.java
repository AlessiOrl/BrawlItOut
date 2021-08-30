package orlando.hci.brawlitout.Utils;

import java.io.Serializable;

import javax.xml.namespace.QName;

public class Player implements Serializable {


    private String name;
    private Float time;

    public Player(String name) {

        this.name = name;
        this.time = (float) -1.0;
    }

    public Player(String name, Float time) {

        this.name = name;
        this.time = time;
    }


    public String getName() {
        return name;
    }

    public Float getTime() {
        return time;
    }

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

}
