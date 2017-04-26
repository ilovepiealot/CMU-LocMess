import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.ObjectOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.FileNotFoundException;

public class Server implements Runnable {

    Socket ss;
    PrintStream printer;
    static PrintWriter writer;

    Server(Socket csocket) {
      this.ss = csocket;
    }

    public static void main(String[] args) throws IOException {
        
        ServerSocket s1 = new ServerSocket(11113);
        System.out.println("Listening");

        while(true) {
            Socket sss = s1.accept();
            System.out.println("Connected");
            new Thread(new Server(sss)).start();
        }
    }

    public void run() {

		String[] arr;
		String[] vs = null;
		boolean logged = false;
		boolean registered = true;
		String line = "";
		String username;
		String password;
		String wholeLine;
		
		
		
        try {
			
			OutputStream os = ss.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			InputStream is = ss.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			System.out.println("criou oos");
            // User Input 
            Scanner sc = new Scanner(ss.getInputStream()).useDelimiter("\n"); 
			// Output to Client
			// oos.writeObject("entrou");
			
			File infile = new File("users.txt");
			Scanner input = new Scanner(infile);
			
			while (!(line = (String) ois.readObject()).equals("quit")) {
				System.out.println("line = " + line);
				vs = line.split(":");

				switch (vs[0]) {
				case "Login":

					username = vs[1];
					password = vs[2];
					
					 while (input.hasNext()) {
						wholeLine = input.nextLine();
						arr = wholeLine.split("\\s+");
						System.out.println(arr[0]);
						System.out.println(arr[1]);
						System.out.println(username);
						System.out.println(password);
						if (username.equals(arr[0]) && password.equals(arr[1])){
							logged = true;
							break;
						}
									
					}
					System.out.println("logged: " + logged);
					oos.writeObject(logged);

					break;
				case "register":
				
					username = vs[1];
					password = vs[2];
					PrintWriter writer; 
					
					while (input.hasNext()) {
						wholeLine = input.nextLine();
						arr = wholeLine.split("\\s+");
						if (username.equals(arr[0])){
							registered = false;
							break;
						}
									
					}
					if (registered){
						try {
							writer = new PrintWriter(new FileWriter(infile, true));
							writer.println(username + " " + password);
							writer.close();
						} catch (IOException e) {
							System.out.println(e);
						}
					}
					
					System.out.println("registered: " + registered);
					oos.writeObject(registered);
					
					break;
				case "update":

					break;
				}
			}
			
		} catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }
		

	}
}
