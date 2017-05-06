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


import static android.content.ContentValues.TAG;


public class ServerCommunication {

    public static final String SEPARATOR = "#YOLO#";

    private String ip;
    private int port;

    private boolean registered = false;
    private boolean logged = false;
    private boolean created = false;
    private boolean getted = false;

    private ArrayList<SimpleEntry<String,String>> userKeys = null;
    private ArrayList<String[]> locationList = null;


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

    public boolean createNewMessage(final String message_title, final String messageContent, final String startdate, final String enddate) {

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

                        oos.writeObject("createnewmessage:" + SEPARATOR + message_title + SEPARATOR + messageContent + SEPARATOR + startdate + SEPARATOR + enddate);
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

    public boolean getTitles() {

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

                        oos.writeObject("getTitles:");
                        //blocks
                        // String a = (String) ois.readObject();
                        getted = (String.valueOf(ois.readObject())).equals("true");
                        Log.d(TAG, String.valueOf(getted));

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

        return getted;
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

    public ArrayList<SimpleEntry<String, String>> getUserKeys(final String username) {

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

                        userKeys = (ArrayList<SimpleEntry<String, String>>) ois.readObject();

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

    public boolean saveNewLocation(final String locName, final String locLatitude, final String locLongitude, final String locRadius) {

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

                        oos.writeObject("savenewlocation:" + locName + SEPARATOR + locLatitude + SEPARATOR + locLongitude + SEPARATOR + locRadius);
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
