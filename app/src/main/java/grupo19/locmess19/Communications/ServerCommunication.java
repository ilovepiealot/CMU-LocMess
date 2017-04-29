package grupo19.locmess19.Communications;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import static android.content.ContentValues.TAG;

/**
 * Created by super on 24/04/2017.
 */

public class ServerCommunication {

    private String ip;
    private int port;

    public ServerCommunication(String ip, int port) {
        this.ip = ip;
        this.port = port;

    }

    public boolean registered = false;
    public boolean logged = false;
    public boolean created = false;
    public boolean getted = false;
    public String message_title;
    public String message_content;
    public String start_date;
    public String end_date;


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

                        oos.writeObject("Login:" + username + ":" + password);
                        //blocks
                        // String a = (String) ois.readObject();
                        logged = (String.valueOf(ois.readObject())).equals("true");
                        Log.d(TAG, String.valueOf(logged));

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

                        oos.writeObject("createnewmessage:" + "#YOLO#" + message_title + "#YOLO#" + messageContent + "#YOLO#" + startdate + "#YOLO#" + enddate);
                        //blocks
                        // String a = (String) ois.readObject();
                        created = (String.valueOf(ois.readObject())).equals("true");
                        Log.d(TAG, String.valueOf(created));

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

        return getted;
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
