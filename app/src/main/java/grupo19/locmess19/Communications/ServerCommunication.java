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
    int id;

    private boolean registered = false;
    private boolean logged = false;
    private boolean created = false;
    private boolean getted = false;
    private boolean destroyed = false;

    private HashMap<String, String> userKeys = null;
    private HashMap<String, String> allKeys = null;
    private Map<String,String> messageTitles = null;
    private ArrayList<String[]> locationList = null;
    private ArrayList<String[]> messageList = null;
    private String[] locationDetails = null;


    public ServerCommunication(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public boolean login(final String username, final String password) {

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
                        // String a = (String) ois.readObject();
                        logged = (String.valueOf(ois.readObject())).equals("true");
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

        return logged;
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
                        // String a = (String) ois.readObject();
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

    public boolean createNewMessage(final String message_title, final String messageContent, final String startdate, final String enddate, final String location, final String username, final String starttime, final String endtime) {

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

                        oos.writeObject("createnewmessage:" + SEPARATOR + message_title + SEPARATOR + messageContent + SEPARATOR + startdate + SEPARATOR + enddate + SEPARATOR + location + SEPARATOR + username + SEPARATOR + starttime + SEPARATOR + endtime);
                        //blocks
                        // String a = (String) ois.readObject();
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

    public boolean saveMessageToInbox(final String message_title, final String messageContent, final String startdate, final String enddate, final String location, final String username, final String id, final String starttime, final String endtime) {

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

                        oos.writeObject("savemessagetoinbox:" + SEPARATOR + message_title + SEPARATOR + messageContent + SEPARATOR + startdate + SEPARATOR + enddate + SEPARATOR + location + SEPARATOR + username + SEPARATOR + id + SEPARATOR + starttime + SEPARATOR + endtime);
                        //blocks
                        // String a = (String) ois.readObject();
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

    public Map<String, String> getTitles(final String username, final String box) {

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

                        oos.writeObject("getTitles:" + username + ":" + box);
                        //blocks
                        //a = (String) ois.readObject();

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

    public String getMessage(final String id, final String username){
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

                        oos.writeObject("getMessage:" + id + ":" + username);
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

    public boolean saveNewKey(final String username, final String key, final String value) {

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

                        oos.writeObject("savenewkey:" + username + SEPARATOR + key + SEPARATOR + value);
                        //blocks
                        // String a = (String) ois.readObject();
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

    public HashMap<String, String> getUserKeys(final String username) {

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
                        oos.writeObject("getuserkeys:" + username);

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

        return userKeys;
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
                        // String a = (String) ois.readObject();
                        // locationDetails = (String[]) ois.readObject();
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
                        // String a = (String) ois.readObject();
                        // locationDetails = (String[]) ois.readObject();
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
                        // String a = (String) ois.readObject();
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
                        // String a = (String) ois.readObject();
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

    public boolean deleteKey(final String key, final String username) {

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

                        oos.writeObject("deletekey:" + key + SEPARATOR + username);
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

    public boolean deleteMessage(final String id_title, final String username) {

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
                        oos.writeObject("deleteMessage:" + id + ":" + username);
                        //blocks
                        // String a = (String) ois.readObject();
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
