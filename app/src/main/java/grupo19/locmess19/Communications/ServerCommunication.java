package grupo19.locmess19.Communications;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import static android.content.ContentValues.TAG;


public class ServerCommunication {

    public static final String SEPARATOR = "#YOLO#";

    private String ip;
    private int port;
    String a;

    private boolean registered = false;
    private boolean logged = false;
    private boolean created = false;
    private boolean destroyed = false;

    private HashMap<String, String> userKeys = null;
    private HashMap<String, String> allKeys = null;
    private Map<String,String> messageTitles = null;
    private ArrayList<String[]> locationList = null;
    private ArrayList<String[]> messageList = null;
    private String[] locationDetails = null;
    private int sessionID = -1;

    public ServerCommunication(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public int login(final String username, final String password) {

        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    Socket s = null;
                    try {
                        s = new Socket(ip, port);

                        Object[] o = createCommunication(s);
                        ObjectInputStream ois = (ObjectInputStream) o[0];
                        ObjectOutputStream oos = (ObjectOutputStream) o[1];

                        oos.writeObject("login:" + username + ":" + password);
                        //blocks
                        sessionID = (int) ois.readObject();
                        Log.d(TAG, String.valueOf(logged));
                        oos.writeObject("quit");

                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            //waits for result
            t.join();

        } catch (InterruptedException e) {;
            e.printStackTrace();
        }
        return sessionID;
    }

    public boolean register(final String username, final String password) {

        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    Socket s = null;
                    try {

                        s = new Socket(ip, port);

                        Object[] o = createCommunication(s);
                        ObjectInputStream ois = (ObjectInputStream) o[0];
                        ObjectOutputStream oos = (ObjectOutputStream) o[1];
                        oos.writeObject("register:" + username + ":" + password);
                        //blocks
                        registered = (String.valueOf(ois.readObject())).equals("true");
                        Log.d(TAG, String.valueOf(registered));
                        oos.writeObject("quit");
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

            t.start();
            //waits for result
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return registered;
    }

    public boolean createNewMessage(final String message_title, final String messageContent, final String startdate, final String enddate, final String location, final int sessionID, final String wkeys, final String bkeys) {

        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    Socket s = null;

                    try {
                        s = new Socket(ip, port);

                        Object[] o = createCommunication(s);
                        ObjectInputStream ois = (ObjectInputStream) o[0];
                        ObjectOutputStream oos = (ObjectOutputStream) o[1];
                        oos.writeObject("createnewmessage:" + SEPARATOR + message_title + SEPARATOR + messageContent + SEPARATOR + startdate + SEPARATOR + enddate + SEPARATOR + location + SEPARATOR + sessionID + SEPARATOR + wkeys + SEPARATOR + bkeys);
                        //blocks
                        created = (String.valueOf(ois.readObject())).equals("true");
                        Log.d(TAG, String.valueOf(created));
                        oos.writeObject("quit");
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

            t.start();
            //waits for result
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return created;
    }

    public boolean saveMessageToInbox(final String message_title, final String messageContent, final String startdate, final String enddate, final String location, final int sessionID, final String id, final String wkeys, final String bkeys) {

        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    Socket s = null;

                    try {

                        s = new Socket(ip, port);

                        Object[] o = createCommunication(s);
                        ObjectInputStream ois = (ObjectInputStream) o[0];
                        ObjectOutputStream oos = (ObjectOutputStream) o[1];
                        oos.writeObject("savemessagetoinbox:" + SEPARATOR + message_title + SEPARATOR + messageContent + SEPARATOR + startdate + SEPARATOR + enddate + SEPARATOR + location + SEPARATOR + sessionID + SEPARATOR + id + SEPARATOR + wkeys + SEPARATOR + bkeys);
                        //blocks
                        created = (String.valueOf(ois.readObject())).equals("true");
                        Log.d(TAG, String.valueOf(created));
                        oos.writeObject("quit");
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

            t.start();
            //waits for result
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return created;
    }

    public Map<String, String> getTitles(final int sessionID, final String box) {

        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    Socket s = null;

                    try {

                        s = new Socket(ip, port);

                        Object[] o = createCommunication(s);
                        ObjectInputStream ois = (ObjectInputStream) o[0];
                        ObjectOutputStream oos = (ObjectOutputStream) o[1];
                        oos.writeObject("getTitles:" + sessionID + ":" + box);
                        //blocks
                        messageTitles = (Map<String, String>) ois.readObject();
                        oos.writeObject("quit");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

            t.start();
            //waits for result
            t.join();
        } catch (InterruptedException e) {;
            e.printStackTrace();
        }
        return messageTitles;
    }

    public String getMessage(final String id, final int sessionID){
        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    Socket s = null;

                    try {

                        s = new Socket(ip, port);

                        Object[] o = createCommunication(s);
                        ObjectInputStream ois = (ObjectInputStream) o[0];
                        ObjectOutputStream oos = (ObjectOutputStream) o[1];
                        oos.writeObject("getMessage:" + id + ":" + sessionID);
                        //blocks
                        a = (String) ois.readObject();
                        oos.writeObject("quit");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

            t.start();
            //waits for result
            t.join();
        } catch (InterruptedException e) {;
            e.printStackTrace();
        }
        return a;
    }

    public boolean saveNewKey(final int sessionID, final String key, final String value) {

        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    Socket s = null;

                    try {

                        s = new Socket(ip, port);
                        Object[] o = createCommunication(s);
                        ObjectInputStream ois = (ObjectInputStream) o[0];
                        ObjectOutputStream oos = (ObjectOutputStream) o[1];
                        oos.writeObject("savenewkey:" + sessionID + SEPARATOR + key + SEPARATOR + value);
                        //blocks
                        created = (String.valueOf(ois.readObject())).equals("true");
                        Log.d(TAG, String.valueOf(created));
                        oos.writeObject("quit");
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            //waits for result
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return created;
    }

    public HashMap<String, String> getUserKeys(final int sessionID) {

        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    Socket s = null;
                    try {

                        s = new Socket(ip, port);
                        Object[] o = createCommunication(s);
                        ObjectInputStream ois = (ObjectInputStream) o[0];
                        ObjectOutputStream oos = (ObjectOutputStream) o[1];
                        oos.writeObject("getuserkeys:" + sessionID);
                        userKeys = (HashMap<String, String>) ois.readObject();
                        oos.writeObject("quit");
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            //waits for result
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userKeys;
    }

    public HashMap<String, String> getAllKeys() {

        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    Socket s = null;
                    try {

                        s = new Socket(ip, port);
                        Object[] o = createCommunication(s);
                        ObjectInputStream ois = (ObjectInputStream) o[0];
                        ObjectOutputStream oos = (ObjectOutputStream) o[1];
                        oos.writeObject("getallkeys:");
                        allKeys = (HashMap<String, String>) ois.readObject();
                        oos.writeObject("quit");
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            //waits for result
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allKeys;
    }


    public ArrayList<String[]> getExistingLocations() {

        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    Socket s = null;
                    try {

                        s = new Socket(ip, port);

                        Object[] o = createCommunication(s);
                        ObjectInputStream ois = (ObjectInputStream) o[0];
                        ObjectOutputStream oos = (ObjectOutputStream) o[1];
                        oos.writeObject("getexistinglocations");
                        locationList = (ArrayList<String[]>) ois.readObject();
                        oos.writeObject("quit");
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            //waits for result
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return locationList;
    }

    public boolean saveNewLocationGPS(final String locName, final String locLatitude, final String locLongitude, final String locRadius) {

        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    Socket s = null;

                    try {

                        s = new Socket(ip, port);

                        Object[] o = createCommunication(s);
                        ObjectInputStream ois = (ObjectInputStream) o[0];
                        ObjectOutputStream oos = (ObjectOutputStream) o[1];
                        oos.writeObject("savenewlocationGPS:" + locName + SEPARATOR + locLatitude + SEPARATOR + locLongitude + SEPARATOR + locRadius);
                        //blocks
                        created = (String.valueOf(ois.readObject())).equals("true");
                        Log.d(TAG, String.valueOf(created));
                        oos.writeObject("quit");
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            //waits for result
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return created;
    }

    public boolean saveNewLocationWifi(final String locName, final String locSSID) {

        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    Socket s = null;

                    try {

                        s = new Socket(ip, port);

                        Object[] o = createCommunication(s);
                        ObjectInputStream ois = (ObjectInputStream) o[0];
                        ObjectOutputStream oos = (ObjectOutputStream) o[1];
                        oos.writeObject("savenewlocationWifi:" + locName + SEPARATOR + locSSID);
                        //blocks
                        created = (String.valueOf(ois.readObject())).equals("true");
                        Log.d(TAG, String.valueOf(created));
                        oos.writeObject("quit");
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            //waits for result
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return created;
    }

    public boolean deleteLocation(final String locName) {

        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    Socket s = null;

                    try {

                        s = new Socket(ip, port);

                        Object[] o = createCommunication(s);
                        ObjectInputStream ois = (ObjectInputStream) o[0];
                        ObjectOutputStream oos = (ObjectOutputStream) o[1];
                        oos.writeObject("deletelocation:" + locName);
                        //blocks
                        destroyed = (String.valueOf(ois.readObject())).equals("true");
                        Log.d(TAG, String.valueOf(destroyed));
                        oos.writeObject("quit");
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            //waits for result
            t.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return destroyed;
    }

    public String[] getLocationDetails(final String locName) {

        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    Socket s = null;

                    try {

                        s = new Socket(ip, port);

                        Object[] o = createCommunication(s);
                        ObjectInputStream ois = (ObjectInputStream) o[0];
                        ObjectOutputStream oos = (ObjectOutputStream) o[1];
                        oos.writeObject("getlocationdetails:" + locName);
                        //blocks
                        locationDetails = (String[]) ois.readObject();
                        oos.writeObject("quit");
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            //waits for result
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return locationDetails;
    }

    public boolean deleteKey(final int sessionID, final String key, final String value) {

        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    Socket s = null;

                    try {

                        s = new Socket(ip, port);
                        Object[] o = createCommunication(s);
                        ObjectInputStream ois = (ObjectInputStream) o[0];
                        ObjectOutputStream oos = (ObjectOutputStream) o[1];
                        oos.writeObject("deletekey:" + sessionID + SEPARATOR + key + SEPARATOR + value);
                        created = (String.valueOf(ois.readObject())).equals("true");
                        Log.d(TAG, String.valueOf(created));
                        oos.writeObject("quit");
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

            t.start();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return created;
    }

    public ArrayList<String[]> getExistingMessages() {

        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    Socket s = null;
                    try {

                        s = new Socket(ip, port);
                        Object[] o = createCommunication(s);
                        ObjectInputStream ois = (ObjectInputStream) o[0];
                        ObjectOutputStream oos = (ObjectOutputStream) o[1];
                        oos.writeObject("getexistingmessages");
                        messageList = (ArrayList<String[]>) ois.readObject();
                        oos.writeObject("quit");
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            //waits for result
            t.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return messageList;
    }

    public boolean deleteMessage(final String id_title, final int sessionID) {

        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    Socket s = null;

                    try {

                        s = new Socket(ip, port);

                        Object[] o = createCommunication(s);
                        ObjectInputStream ois = (ObjectInputStream) o[0];
                        ObjectOutputStream oos = (ObjectOutputStream) o[1];
                        String id = id_title.split(":")[0];
                        oos.writeObject("deleteMessage:" + id + ":" + sessionID);
                        //blocks
                        destroyed = (String.valueOf(ois.readObject())).equals("true");
                        Log.d(TAG, String.valueOf(destroyed));
                        oos.writeObject("quit");
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            //waits for result
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return destroyed;
    }

    private Object[] createCommunication(Socket s) {

        try {

            OutputStream os = s.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);

            InputStream is = s.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);

            return new Object[]{ois, oos};
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ServerCommunication", "error creating communication");
        }
        return null;
    }
}
