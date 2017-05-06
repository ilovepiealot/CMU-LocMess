package grupo19.locmess19.DataTypes;


import android.location.Location;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Date;

public class Message {

    private Location location;
    private User user;
    private ArrayList<SimpleEntry<String,String>> whiteList;
    private ArrayList<SimpleEntry<String,String>> blackList;
    private Date validity;

    public Message(Location location, User user, Date validity) {
        this.location = location;
        this.user = user;
        this.whiteList = new ArrayList<SimpleEntry<String,String>>();
        this.blackList = new ArrayList<SimpleEntry<String,String>>();
        this.validity = validity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<SimpleEntry<String,String>> getWhiteList() {
        return whiteList;
    }

    public void addToWhiteList(SimpleEntry<String,String> whiteListItem) {
        this.whiteList.add(whiteListItem);
    }

    public ArrayList<SimpleEntry<String,String>> getBlackList() {
        return blackList;
    }

    public void addToBlackList(SimpleEntry<String,String> blacklistItem) {
        this.blackList.add(blacklistItem);
    }

    public Date getValidity() {
        return validity;
    }

    public void setValidity(Date validity) {
        this.validity = validity;
    }
}
