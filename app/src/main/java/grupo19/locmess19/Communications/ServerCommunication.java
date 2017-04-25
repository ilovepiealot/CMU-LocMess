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

    //private boolean registed;
    public boolean logged = false;

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
