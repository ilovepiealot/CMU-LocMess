import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.ObjectOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.text.SimpleDateFormat;


public class Server implements Runnable {

    Socket ss;
    PrintStream printer;
    static PrintWriter writer;
    Server(Socket csocket) {
      this.ss = csocket;
    }
	static SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss:SSS");

	static File infile = new File("files/users.txt");

	static File infile2 = new File("files/messages.txt");



    public static void main(String[] args) throws IOException {

        ServerSocket s1 = new ServerSocket(11113);
        System.out.println(time() + "Server is up! Listening...\n");

		File theDir = new File("files");
		if (!theDir.exists()) {
			boolean result = theDir.mkdir();
		}

		if (!infile.exists()) {
			infile.createNewFile();
		}

		if (!infile2.exists()) {
			infile2.createNewFile();
		}

		while(true) {
            Socket sss = s1.accept();
            System.out.println(time() + "Connected " + sss.getPort());
            new Thread(new Server(sss)).start();
        }
    }

    public void run() {

		String[] arr;
		String[] vs = null;
		String[] mes = null;
		boolean logged = false;
		boolean registered = true;
		boolean created = true;
		boolean viewed = true;
		String line = "";
		String username;
		String password;
		String wholeLine;
		
		String messageTitle;
		String messageContent;
		String messageStartDate;
		String messageEndDate;
		
		
        try {
			
			OutputStream os = ss.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			InputStream is = ss.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			//System.out.println("criou oos");
            // User Input 
            Scanner sc = new Scanner(ss.getInputStream()).useDelimiter("\n"); 
			// Output to Client
			// oos.writeObject("entrou");

			Scanner input = new Scanner(infile);
			Scanner input2 = new Scanner(infile2);
			
			while (!(line = (String) ois.readObject()).equals("quit")) {
				System.out.println(time() + "Received: \"" + line + "\"");
				vs = line.split(":");

				switch (vs[0]) {
				case "Login":

					username = vs[1];
					password = vs[2];
					
					 while (input.hasNext()) {
						wholeLine = input.nextLine();
						arr = wholeLine.split("\\s+");
						String res = time() + username + ":" + password + " == " + arr[0] + ":" + arr[1] + " ? ";
						if (username.equals(arr[0]) && password.equals(arr[1])){
							logged = true;
							System.out.println(res + "YES!");
							break;
						}
						else
							System.out.println(res + "NO!");
					 }
					System.out.println(time() + "User \""+ username + "\" logged in: " + logged + "\n");
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
					if (registered && username != null && password != null){
						try {
							writer = new PrintWriter(new FileWriter(infile, true));
							writer.println(username + " " + password);
							writer.close();
						} catch (IOException e) {
							System.out.println(e);
						}
					}
					
					System.out.println(time() + "User \""+ username + "\" registered: " + registered + "\n");
					oos.writeObject(registered);
					
					break;
				case "update":

					break;
					
				case "createnewmessage" :
				
					mes = line.split("#YOLO#");
					messageTitle = mes[1];
					messageContent = mes[2];
					messageStartDate = mes[3];
					messageEndDate = mes[4];
					PrintWriter writer2;
					try {
							writer2 = new PrintWriter(new FileWriter(infile2, true));
							writer2.println("\n" + messageTitle + "#YOLO#" + messageContent + "#YOLO#" + messageStartDate + "#YOLO#" + messageEndDate);
							writer2.close();
						} catch (IOException e) {
							System.out.println(e);
						}
					System.out.println("created: " + created);
					oos.writeObject(created);
					
				case "getTitles" :
					oos.writeObject(viewed);
					break;
				}
			}
			
		} catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }
		

	}

	public static String time(){
		return dateformat.format(new Date()) + " - ";
	}
}
