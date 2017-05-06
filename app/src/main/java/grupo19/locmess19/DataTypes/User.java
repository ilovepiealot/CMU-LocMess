package grupo19.locmess19.DataTypes;


import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

public class User {

    private String name;
    private String password;
    private ArrayList<Message> inbox;
    private ArrayList<Message> outbox;
    private ArrayList<SimpleEntry<String,String>> keys;


    public User(String name, String password) {
        this.name = name;
        this.password = password;
        inbox = new ArrayList<Message>();
        outbox = new ArrayList<Message>();
        keys = new ArrayList<SimpleEntry<String,String>>();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Message> getInbox() {
        return inbox;
    }

    public void addToInbox(Message inboxItem) {
        this.inbox.add(inboxItem);
    }

    public ArrayList<Message> getOutbox() {
        return outbox;
    }

    public void addToOutbox(Message outboxItem) {
        this.inbox.add(outboxItem);
    }

    public ArrayList<SimpleEntry<String,String>> getKeys() {
        return keys;
    }

    public void addKey(SimpleEntry<String,String> key) {
        this.keys.add(key);
    }
}
