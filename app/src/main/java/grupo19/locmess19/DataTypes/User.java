package grupo19.locmess19.DataTypes;


import java.util.ArrayList;

public class User {

    private String name;
    private String password;
    private ArrayList<Message> inbox;
    private ArrayList<Message> outbox;
    private ArrayList<KeyValuePair> keys;


    public User(String name, String password) {
        this.name = name;
        this.password = password;
        inbox = new ArrayList<Message>();
        outbox = new ArrayList<Message>();
        keys = new ArrayList<KeyValuePair>();
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

    public ArrayList<KeyValuePair> getKeys() {
        return keys;
    }

    public void addKey(KeyValuePair key) {
        this.keys.add(key);
    }
}
