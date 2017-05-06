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
import java.util.AbstractMap;
import java.util.ArrayList;


public class Server implements Runnable {

    Socket ss;

	PrintStream printer;
    static PrintWriter writer;

	static SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss:SSS");
	static final String SEPARATOR = "#YOLO#";

	static File usersFile = new File("files/users.txt");
	static File keysFile = new File("files/keys.txt");
	static File messagesFile = new File("files/messages.txt");

	Server(Socket csocket) {
		this.ss = csocket;
	}

	public static void main(String[] args) throws IOException {

        ServerSocket s1 = new ServerSocket(11113);
        System.out.println(time() + "Server is up! Listening...\n");

		File theDir = new File("files");
		if (!theDir.exists()) { boolean result = theDir.mkdir(); }  // Create files folder

		if (!usersFile.exists()) { usersFile.createNewFile(); } // Create users.txt

		if (!keysFile.exists()) { keysFile.createNewFile(); } // Create keys.txt

		if (!messagesFile.exists()) {  messagesFile.createNewFile(); } // Create messages.txt TODO: Maybe one messages file per user?

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
		String username, password;
		String wholeLine;
		String messageTitle, messageContent, messageStartDate, messageEndDate;

		
        try {
			
			OutputStream os = ss.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);

			InputStream is = ss.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);

            // User Input
            Scanner sc = new Scanner(ss.getInputStream()).useDelimiter("\n");

			// Output to Client
			Scanner usersScanner = new Scanner(usersFile);
			Scanner messagesScanner = new Scanner(messagesFile);
			Scanner keysScanner = new Scanner(keysFile);

			while (!(line = (String) ois.readObject()).equals("quit")) {
				System.out.println(time() + "Received: \"" + line + "\"");
				vs = line.split(":");

				switch (vs[0]) {
					case "login":

						username = vs[1];
						password = vs[2];
						while (usersScanner.hasNext()) {
							wholeLine = usersScanner.nextLine();
							arr = wholeLine.split("\\s+");
							String res = time() + username + ":" + password + " == " + arr[0] + ":" + arr[1] + " ? ";
							if (username.equals(arr[0]) && password.equals(arr[1])) {
								logged = true;
								System.out.println(res + "YES!");
								break;
							} else
								System.out.println(res + "NO!");
						}
						System.out.println(time() + "User \"" + username + "\" logged in: " + logged + "\n");
						oos.writeObject(logged);
						break;

					case "register":
						username = vs[1];
						password = vs[2];
						PrintWriter writer;
						while (usersScanner.hasNext()) {
							wholeLine = usersScanner.nextLine();
							arr = wholeLine.split("\\s+");
							if (username.equals(arr[0])) {
								registered = false;
								break;
							}
						}
						if (registered && username != null && password != null) {
							try {
								writer = new PrintWriter(new FileWriter(usersFile, true));
								writer.println(username + " " + password);
								writer.close();
							} catch (IOException e) {
								System.out.println(e);
							}
						}
						System.out.println(time() + "User \"" + username + "\" registered: " + registered + "\n");
						oos.writeObject(registered);
						break;
					case "update":
						break;
					case "createnewmessage":
						mes = line.split(SEPARATOR);

						messageTitle = mes[1];
						messageContent = mes[2];
						messageStartDate = mes[3];
						messageEndDate = mes[4];
						PrintWriter messagesWriter;
						try {
							messagesWriter = new PrintWriter(new FileWriter(messagesFile, true));
							messagesWriter.println("\n" + messageTitle + SEPARATOR + messageContent + SEPARATOR + messageStartDate + SEPARATOR + messageEndDate);
							messagesWriter.close();
						} catch (IOException e) {
							System.out.println(e);
						}
						System.out.println("created: " + created);
						oos.writeObject(created);
						break;
					case "getTitles":
						oos.writeObject(viewed);
						break;
					case "savenewkey":
						String[] keyval = vs[1].split(SEPARATOR);
						username = keyval[0];
						String key = keyval[1];
						String value = keyval[2];
						try {
							File userKeysFile = new File("files/" + username + "_keys.txt");
							if (!userKeysFile.exists()) {
								userKeysFile.createNewFile();
							}
							PrintWriter keyValWriter = new PrintWriter(new FileWriter(keysFile, true));
							keyValWriter.println(key + SEPARATOR + value);
							keyValWriter.close();

							PrintWriter keyValUserWriter = new PrintWriter(new FileWriter(userKeysFile, true));
							keyValUserWriter.println(key + SEPARATOR + value);
							keyValUserWriter.close();

						} catch (IOException e) {
							System.out.println(e);
						}
						System.out.println(time() + "Created key <" + key + "," + value + "> in user " + username + "\n");
						oos.writeObject(true);
						break;

					case "getuserkeys":
						username = vs[1];
						File usersKeyFile = new File("files/" + username + "_keys.txt");
						Scanner userKeysScanner = new Scanner(usersKeyFile);
						ArrayList<AbstractMap.SimpleEntry<String, String>> userKeys = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
						while (userKeysScanner.hasNext()) {
							wholeLine = userKeysScanner.nextLine();
							arr = wholeLine.split(SEPARATOR);
							userKeys.add(new AbstractMap.SimpleEntry<String,String>(arr[0],arr[1]));
						}
						userKeysScanner.close();
						System.out.println(time() + "Get all keys from user " + username + "\n");
						oos.writeObject(userKeys);
						break;
					case "getexistinglocations":
						File locationsFile = new File("files/locations.txt");
						Scanner locationsFileScanner = new Scanner(locationsFile);
						ArrayList<String[]> locationList = new ArrayList<String[]>();
						while (locationsFileScanner.hasNext()) {
							wholeLine = locationsFileScanner.nextLine();
							arr = wholeLine.split(SEPARATOR);
							locationList.add(arr);
						}
						locationsFileScanner.close();
						System.out.println(time() + "Get all available locations from the server\n");
						oos.writeObject(locationList);
						break;
					case "savenewlocation":
						String[] locval = vs[1].split(SEPARATOR);
						String locName = locval[0];
						String locLatitude = locval[1];
						String locLongitude = locval[2];
						String locRadius = locval[3];
						try {
							locationsFile = new File("files/locations.txt");
							if (!locationsFile.exists()) {
								locationsFile.createNewFile();
							}
							PrintWriter locWriter = new PrintWriter(new FileWriter(locationsFile, true));
							locWriter.println(locName + SEPARATOR + locLatitude + SEPARATOR + locLongitude + SEPARATOR + locRadius);
							locWriter.close();
						} catch (IOException e) {
							System.out.println(e);
						}
						System.out.println(time() + "Created Location <" + locName + ": " + locLatitude + ", " + locLongitude + ", radius: " + locRadius + ">\n");
						oos.writeObject(true);
						break;
					case "deletelocation":
						String locDeleteName = vs[1];
						String locationString = "files/locations.txt";
						locationsFile = new File("files/locations.txt");
						File tempFile = new File("files/locationsTemp.txt");
						BufferedWriter bwriter = new BufferedWriter(new FileWriter(tempFile));
						locationsFileScanner = new Scanner(locationsFile);
						boolean check = false;
						while (locationsFileScanner.hasNext()) {
							wholeLine = locationsFileScanner.nextLine();
							arr = wholeLine.split(SEPARATOR);
							if (arr[0].equals(locDeleteName)) {
								System.out.println(locDeleteName);
								System.out.println(arr[0]);
								continue;
							}
							bwriter.write(wholeLine + System.getProperty("line.separator"));
						}
						locationsFileScanner.close();
						bwriter.close(); 
						locationsFile.delete();
						check = tempFile.renameTo(locationsFile);
						System.out.println(time() + "Deleted location: <" + locDeleteName + ">\n");
						oos.writeObject(check);												
					default:
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
