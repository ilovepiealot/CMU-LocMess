package grupo19.locmess19.DataTypes;


import android.location.Location;

import java.util.ArrayList;
import java.util.Date;

public class Message {

    private Location location;
    private User user;
    private ArrayList<KeyValuePair> whiteList;
    private ArrayList<KeyValuePair> blackList;
    private Date validity;

    public Message(Location location, User user, Date validity) {
        this.location = location;
        this.user = user;
        this.whiteList = new ArrayList<KeyValuePair>();
        this.blackList = new ArrayList<KeyValuePair>();
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

    public ArrayList<KeyValuePair> getWhiteList() {
        return whiteList;
    }

    public void addToWhiteList(KeyValuePair whiteListItem) {
        this.whiteList.add(whiteListItem);
    }

    public ArrayList<KeyValuePair> getBlackList() {
        return blackList;
    }

    public void addToBlackList(KeyValuePair blacklistItem) {
        this.blackList.add(blacklistItem);
    }

    public Date getValidity() {
        return validity;
    }

    public void setValidity(Date validity) {
        this.validity = validity;
    }
}
