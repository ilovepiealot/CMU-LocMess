import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Date;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;


public class Server implements Runnable {

	static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss:SSS");
	static final String SEPARATOR = "#YOLO#";

	Socket ss;
	static File usersFile = new File("files/users.txt");
	static File keysFile = new File("files/keys.txt");
	static File messagesGlobalFile = new File("files/messages.txt");
	static File messagesFile;
	static File locationsFile = new File("files/locations.txt");
	static int messageID = 0;
	String messageIDString = "";
	static ConcurrentHashMap<Integer, String> sessionIDs;

	public Server(Socket csocket) {
		this.ss = csocket;
		if(this.sessionIDs == null) this.sessionIDs = new ConcurrentHashMap<Integer, String>();
	}

	public static void main(String[] args) throws IOException {

        ServerSocket s1 = new ServerSocket(11113);
        System.out.println(time() + "Server is up! Listening...\n");

		File theDir = new File("files");
		if (!theDir.exists()) { theDir.mkdir(); }  // Create files folder
		
		if (!usersFile.exists()) { usersFile.createNewFile(); } // Create users.txt
		
		if (!keysFile.exists()) { keysFile.createNewFile(); } // Create keys.txt

		if (!messagesGlobalFile.exists()) {  messagesGlobalFile.createNewFile(); } // Create messages.txt
		
		if (!locationsFile.exists()) {  locationsFile.createNewFile(); } // Create messages.txt


		while(true) {
            Socket sss = s1.accept();
            System.out.println(time() + "Connected " + sss.getPort());
            new Thread(new Server(sss)).start();
        }
    }

    public void run() {
		String line = "";
		String[] vs = null;
		String[] mes = null;
		String username = "";
    			
        try {
			OutputStream os = ss.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);

			InputStream is = ss.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			
            // User Input
            // Scanner sc = new Scanner(ss.getInputStream()).useDelimiter("\n");
			
			while (!(line = (String) ois.readObject()).equals("quit")) {
				System.out.println(time() + "Received: \"" + line + "\"");
				vs = line.split(":");

				switch (vs[0]) {
					case "login":
						oos.writeObject(loginUser(vs[1], vs[2]));
						break;
					case "logout":
						int sessionID = Integer.parseInt(vs[1]);
						oos.writeObject(logOutUser(sessionID));
						break;
					case "register":
						oos.writeObject(registerUser(vs[1], vs[2]));
						break;
									
					case "savenewkey":
						String[] keyval = vs[1].split(SEPARATOR);
						username = sessionIDs.get(Integer.parseInt(keyval[0]));
						oos.writeObject(saveNewKey(username, keyval[1], keyval[2]));
						break;
						
					case "deletekey":
						String[] user_key = vs[1].split(SEPARATOR);
						username = sessionIDs.get(Integer.parseInt(user_key[0]));
						oos.writeObject(deleteKey(username, user_key[1], user_key[2]));
						break;
						
					case "getuserkeys":
						username = sessionIDs.get(Integer.parseInt(vs[1]));
						HashMap<String, String> userKeys = getUserKeys(username);
						oos.writeObject(userKeys);
						break;
						
					case "getallkeys":
						HashMap<String, String> keys = getAllKeys();
						oos.writeObject(keys);
						break;
						
					case "savenewlocationGPS":
						String[] locval = vs[1].split(SEPARATOR);
						oos.writeObject(saveNewLocationGPS(locval[0], locval[1], locval[2], locval[3]));
						break;

					case "savenewlocationWifi":
						locval = vs[1].split(SEPARATOR);
						oos.writeObject(saveNewLocationWifi(locval[0], locval[1]));
						break;

					case "getlocationdetails":
						String locDetailsName = vs[1];
						oos.writeObject(getLocationDetails(locDetailsName));
						break;

					case "getexistinglocations":
						ArrayList<String[]> locationList = getExistingLocations();
						oos.writeObject(locationList);
						break;
						
					case "deletelocation":
						oos.writeObject(deleteLocation(vs[1]));
						break;
				
					case "createnewmessage":
						mes = line.split(SEPARATOR);
						messageID++;
						messageIDString = String.valueOf(messageID);
						username = sessionIDs.get(Integer.parseInt(mes[6]));
						oos.writeObject(createNewMessage(mes[1], mes[2], mes[3], mes[4], mes[5], username, messageIDString, mes[7], mes[8]));
						break;
					case "savemessagetoinbox":
						mes = line.split(SEPARATOR);
						username = sessionIDs.get(Integer.parseInt(mes[6]));
						oos.writeObject(saveMessageToInbox(mes[1], mes[2], mes[3], mes[4], mes[5], username, mes[7], mes[8], mes[9]));
						break;
					case "getTitles":
						username = sessionIDs.get(Integer.parseInt(vs[1]));
						oos.writeObject(getTitles(username, vs[2]));
						break;
					
					case "getMessage":
						username = sessionIDs.get(Integer.parseInt(vs[2]));
						oos.writeObject(getMessage(vs[1], username));
						break;
						
					case "deleteMessage":
						username = sessionIDs.get(Integer.parseInt(vs[2]));
						oos.writeObject(deleteMessage(vs[1], vs[2]));
						break;

					case "getexistingmessages":
						ArrayList<String[]> messageList = getExistingMessages();
						oos.writeObject(messageList);
						break;
											
					default:
						break;	
				}
			}
		} catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }
	}

    public int loginUser(String username, String password){
		boolean logged = false;
		int sessionID = 0;
		try {
			Scanner usersScanner = new Scanner(usersFile);
	    	while (usersScanner.hasNext()) {
				String wholeLine = usersScanner.nextLine();
				String[] arr = wholeLine.split("\\s+");
				String res = time() + username + ":" + password + " == " + arr[0] + ":" + arr[1] + " ? ";
				if (username.equals(arr[0]) && password.equals(arr[1])) {
					logged = true;
					System.out.println(res + "YES!");
					
					messagesFile = new File("files/" + username + "_messages.txt");
					try{
						if (!messagesFile.exists()) {  messagesFile.createNewFile(); } // Create messages.txt
					} catch (IOException e){
						System.out.println(e);
					}
					
					break;
				} else
					System.out.println(res + "NO!");
			}
	    	usersScanner.close();
	    	while(!sessionIDs.containsValue(username)){
		    	int randomNr = ThreadLocalRandom.current().nextInt(1, 1000);
		    	if(!sessionIDs.containsKey(randomNr)){
		    		sessionIDs.put(randomNr, username);
		    		sessionID = randomNr;
		    	}
	    	}
			System.out.println(time() + "User \"" + username + "\" with sessionID=" + sessionID + " logged in: " + logged + "\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return sessionID;
    }

    public boolean logOutUser(int sessionID) {
    	if (sessionIDs.containsKey(sessionID)) {
    		String username = sessionIDs.get(sessionID);
    		return sessionIDs.remove(sessionID, username);
    	}
    	return false;
    }
    
    public boolean registerUser(String username, String password){
		boolean registered = true;
		try {
			Scanner usersScanner = new Scanner(usersFile);
	    	PrintWriter writer;
			while (usersScanner.hasNext()) {
				String wholeLine = usersScanner.nextLine();
				String[] arr = wholeLine.split("\\s+");
				if (username.equals(arr[0])) {
					registered = false;
					break;
				}
			}
			if (registered && username != null && password != null) {
					writer = new PrintWriter(new FileWriter(usersFile, true));
					writer.println(username + " " + password);
					File userKeysFile = new File("files/" + username + "_keys.txt");
					if (!userKeysFile.exists()) { userKeysFile.createNewFile(); }
					File userMessagesFile = new File("files/" + username + "_messages.txt");
					if (!userMessagesFile.exists()) { userMessagesFile.createNewFile(); }
					writer.close();
			}
			usersScanner.close();
			System.out.println(time() + "User \"" + username + "\" registered: " + registered + "\n");
		} catch (IOException e) {
			System.out.println(e);
		}
		return registered;
    }
        
    public boolean saveNewKey(String username, String key, String value){

    	try {
			File userKeysFile = new File("files/" + username + "_keys.txt");

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
		return true;
    }
    
    public boolean deletePrivateKey(String username, String key){
    	try {
			File keysFile = new File("files/" + username + "_keys.txt");
			File tempFile = new File("files/" + username + "_keysTemp.txt");
			Writer bwriter = new BufferedWriter(new FileWriter(tempFile));
			Scanner keysScanner = new Scanner(keysFile);
			boolean renamed = false, deleted = false;
			while (keysScanner.hasNext()) {
				String wholeLine = keysScanner.nextLine();
				String[] arr = wholeLine.split(SEPARATOR);
				if (!arr[0].equals(key)) {
					bwriter.write(wholeLine + System.getProperty("line.separator"));
				}
			}
			keysScanner.close();
			bwriter.close();
			deleted = keysFile.delete();
			renamed = tempFile.renameTo(keysFile);
			System.out.println(time() + "Deleted key <" + key + "> from user " + username + " : renamed=" + renamed + " deleted=" + deleted + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
    }
    
    public boolean deleteGlobalKey(String key, String value){
    	try {
			File keysFile2 = new File("files/keys.txt");
			File tempFile2 = new File("files/keysTemp.txt");
			Writer bwriter2 = new BufferedWriter(new FileWriter(tempFile2));
			Scanner keysScanner2 = new Scanner(keysFile2);
			boolean check2 = true;
			while (keysScanner2.hasNext()) {
				String wholeLine = keysScanner2.nextLine();
				String[] arr = wholeLine.split(SEPARATOR);
				if (!arr[0].equals(key) && !arr[1].equals(value)) {
					bwriter2.write(wholeLine + System.getProperty("line.separator"));
				}
			}
			keysScanner2.close();
			bwriter2.close();
			keysFile2.delete();
			check2 = tempFile2.renameTo(keysFile2);
			System.out.println(time() + "Deleted global key <" + key + "," + value + ">" + " : " + check2 + "\n");
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
    }
    
    public boolean deleteKey(String username, String key, String value){
	    deleteGlobalKey(key, value);
	    deletePrivateKey(username, key);
    	return true;
    }
    
    public HashMap<String, String> getUserKeys(String username){
		File usersKeyFile = new File("files/" + username + "_keys.txt");
		Scanner userKeysScanner;
		HashMap<String, String> userKeys = new HashMap<String, String>();
		try {
			userKeysScanner = new Scanner(usersKeyFile);
			while (userKeysScanner.hasNext()) {
				String wholeLine = userKeysScanner.nextLine();
				String[] arr = wholeLine.split(SEPARATOR);
				userKeys.put(arr[0],arr[1]);
			}
			userKeysScanner.close();
			System.out.println(time() + "Get all keys from user " + username + "\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return userKeys;
    }
    
    public HashMap<String, String> getAllKeys(){
		File keysFile = new File("files/keys.txt");
		Scanner keysScanner;
		HashMap<String, String> userKeys = new HashMap<String, String>();
		try {
			keysScanner = new Scanner(keysFile);
			while (keysScanner.hasNext()) {
				String wholeLine = keysScanner.nextLine();
				String[] arr = wholeLine.split(SEPARATOR);
				userKeys.put(arr[0],arr[1]);
			}
			keysScanner.close();
			System.out.println(time() + "Get all keys \n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return userKeys;
    }
   
    public boolean saveNewLocationGPS(String locName, String locLatitude, String locLongitude, String locRadius){
    	try {
			File locationsFile = new File("files/locations.txt");
			if (!locationsFile.exists()) {
				locationsFile.createNewFile();
			}
			PrintWriter locWriter = new PrintWriter(new FileWriter(locationsFile, true));
			locWriter.println(locName + SEPARATOR + locLatitude + SEPARATOR + locLongitude + SEPARATOR + locRadius);
			locWriter.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		System.out.println(time() + "Created Location <" + locName + ": " + locLatitude + ", " + locLongitude + ", " + locRadius + ">\n");
		return true;
    }

    public boolean saveNewLocationWifi(String locName, String ssid){
    	try {
			File locationsFile = new File("files/locations.txt");
			if (!locationsFile.exists()) {
				locationsFile.createNewFile();
			}
			PrintWriter locWriter = new PrintWriter(new FileWriter(locationsFile, true));
			locWriter.println(locName + SEPARATOR + ssid);
			locWriter.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		System.out.println(time() + "Created Location <" + locName + ": " + ssid + ">\n");
		return true;
    }
	
	public String[] getLocationDetails(String locDetailsName) {
		File locationsFile = new File("files/locations.txt");
		String[] locDetailsGPS = new String[4];
		String[] locDetailsWifi = new String[2];
		Boolean isGPS = false;
		try {
			Scanner locationsFileScanner = new Scanner(locationsFile);
			while (locationsFileScanner.hasNext()) {
				String wholeLine = locationsFileScanner.nextLine();
				String[] arr = wholeLine.split(SEPARATOR);
				if (arr[0].equals(locDetailsName)) {
					if (arr.length == 4) {
						isGPS = true;
						locDetailsGPS[0] = locDetailsName;
						locDetailsGPS[1] = arr[1];
						locDetailsGPS[2] = arr[2];
						locDetailsGPS[3] = arr[3];						
					} else {
						locDetailsWifi[0] = locDetailsName;
						locDetailsWifi[1] = arr[1];
					}
				}
			}
			locationsFileScanner.close();
			System.out.println(time() + "Location details: + " + locDetailsName + "\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (isGPS) {
			return locDetailsGPS;
		} else {
			return locDetailsWifi;
		}
	}

    public ArrayList<String[]> getExistingLocations(){
    	File locationsFile = new File("files/locations.txt");
		Scanner locationsFileScanner;
		ArrayList<String[]> locationList = new ArrayList<String[]>();
		try {
			locationsFileScanner = new Scanner(locationsFile);
			while (locationsFileScanner.hasNext()) {
				String wholeLine = locationsFileScanner.nextLine();
				String[] arr = wholeLine.split(SEPARATOR);
				locationList.add(arr);
			}
			locationsFileScanner.close();
			System.out.println(time() + "Get all available locations from the server\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return locationList;
    }

    public boolean deleteLocation(String locDeleteName){
		File locationsFile = new File("files/locations.txt");
		File tempFile = new File("files/locationsTemp.txt");
		Writer bwriter;
		boolean check = false;
		try {
			bwriter = new BufferedWriter(new FileWriter(tempFile));
			Scanner locationsFileScanner = new Scanner(locationsFile);
			while (locationsFileScanner.hasNext()) {
				String wholeLine = locationsFileScanner.nextLine();
				String[] arr = wholeLine.split(SEPARATOR);
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
			System.out.println(time() + "Deleted location: <" + locDeleteName + "> : " + check + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return check;
		
    }
        
    public boolean createNewMessage(String messageTitle, String messageContent, String messageStartDate, String messageEndDate, String location, String username2, String messageIDString, String wkeys, String bkeys){
		boolean created = true;
		PrintWriter messagesWriter;
		PrintWriter messagesGlobalWriter;
		
			try {
				messagesWriter = new PrintWriter(new FileWriter(messagesFile, true));
				messagesWriter.println("\n" + messageTitle + SEPARATOR + messageContent + SEPARATOR + messageStartDate + SEPARATOR + messageEndDate + SEPARATOR + location + SEPARATOR + username2 + SEPARATOR + messageIDString + SEPARATOR + wkeys + SEPARATOR + bkeys);
				messagesWriter.close();
				messagesGlobalWriter = new PrintWriter(new FileWriter(messagesGlobalFile, true));
				messagesGlobalWriter.println("\n" + messageTitle + SEPARATOR + messageContent + SEPARATOR + messageStartDate + SEPARATOR + messageEndDate + SEPARATOR + location + SEPARATOR + username2 + SEPARATOR + messageIDString + SEPARATOR + wkeys + SEPARATOR + bkeys);
				messagesGlobalWriter.close();
			} catch (IOException e) {
				System.out.println(e);
			}
		return created;
    }

        public boolean saveMessageToInbox(String messageTitle, String messageContent, String messageStartDate, String messageEndDate, String location, String username2, String messageIDString, String wkeys, String bkeys){
		
		boolean created = true;
		PrintWriter messagesWriter;

			try {
				messagesWriter = new PrintWriter(new FileWriter(messagesFile, true));
				messagesWriter.println("\n" + messageTitle + SEPARATOR + messageContent + SEPARATOR + messageStartDate + SEPARATOR + messageEndDate + SEPARATOR + location + SEPARATOR + username2 + SEPARATOR + messageIDString + SEPARATOR + wkeys + SEPARATOR + bkeys);
				messagesWriter.close();
			} catch (IOException e) {
				System.out.println(e);
			}
		return created;
    } 

	public HashMap<String, String> getTitles(String username, String box) {
		HashMap<String, String> messageTitles = new HashMap<String,String>();
		//caso sejam titulos da outbox apenas é lido o ficheiro do utilizador
		//messagesFile = new File("files/messages_" + username + ".txt"); 		
		if (box.equals("outbox")){
			try {
				Scanner	messagesScanner = new Scanner(messagesFile);
				while (messagesScanner.hasNext()) {
					String wholeLine = messagesScanner.nextLine();
					if (! (wholeLine.isEmpty())){
						String[] arr = wholeLine.split(SEPARATOR);
						//compara o user que fez o pedido com o campo de criador da mensagem, se igual entao retorna para outbox
						if (username.equals(arr[5])){							
							System.out.println("arr[0]: " + arr[0]);
							System.out.println("arr[1]: " + arr[6]);
							messageTitles.put(arr[0],arr[6]);
						}
					}
				}
				messagesScanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		//caso sejam titulos para a inbox é verificado o ficheiro global
		else if (box.equals("inbox")){											 
			try {
				Scanner	messagesScanner = new Scanner(messagesFile);
				Scanner messagesGlobalScanner = new Scanner(messagesGlobalFile);
				while (messagesScanner.hasNext()) {
					String wholeLine = messagesScanner.nextLine();
					if (! (wholeLine.isEmpty())){
						String[] arr = wholeLine.split(SEPARATOR);
						//são seleccionadas as mensagens que não foram criadas pelo utilizador corrente
						if (!(username.equals(arr[5]))){					
							System.out.println("arr[0]: " + arr[0]);
							System.out.println("arr[1]: " + arr[6]);
							messageTitles.put(arr[0],arr[6]);
						}
					}
				}
				messagesScanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return messageTitles;
	}

	public String getMessage(String id, String username){
		String titletest = "";
		try {
			Scanner messagesGlobalScanner = new Scanner(messagesGlobalFile);
			while (messagesGlobalScanner.hasNext()) {
				String wholeLine = messagesGlobalScanner.nextLine();
				if (! (wholeLine.isEmpty())){
					String[] arr = wholeLine.split(SEPARATOR);
					if(id.equals(arr[6])){
						titletest = wholeLine;
					}
				}
			}
			messagesGlobalScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return titletest;
	}
	
	public boolean deleteMessage(String messageDeleteID, String username){
		File messagesFileTemp = new File("files/messagesTemp_" + username + ".txt");
		File messagesGlobalFileTemp = new File("files/messagesTemp.txt");
		
		Writer writer;
		Writer writerGlobal;
		boolean check = false;
		try {
			writer = new BufferedWriter(new FileWriter(messagesFileTemp));
			Scanner messagesFileScanner = new Scanner(messagesFile);
			writerGlobal = new BufferedWriter(new FileWriter(messagesGlobalFileTemp));
			Scanner messagesGlobalFileScanner = new Scanner(messagesGlobalFile);
			
			while (messagesFileScanner.hasNext()) {
				String wholeLine = messagesFileScanner.nextLine();
				if (! (wholeLine.isEmpty())){
				String[] arr = wholeLine.split(SEPARATOR);
				if (arr[6].equals(messageDeleteID)) {
						System.out.println(messageDeleteID);
						System.out.println(arr[0]);
						continue;
					}
					writer.write(wholeLine + System.getProperty("line.separator"));
				}
			}
			
			while (messagesGlobalFileScanner.hasNext()) {
				String wholeLine = messagesGlobalFileScanner.nextLine();
				if (! (wholeLine.isEmpty())){
					String[] arr = wholeLine.split(SEPARATOR);
					if (arr[6].equals(messageDeleteID)) {
						System.out.println(messageDeleteID);
						System.out.println(arr[0]);
						continue;
					}
					writerGlobal.write(wholeLine + System.getProperty("line.separator"));
				}
			}
			
			messagesFileScanner.close();
			messagesGlobalFileScanner.close();
			
			writer.close(); 
			writerGlobal.close();
			
			messagesFile.delete();
			messagesGlobalFile.delete();
			if (messagesFileTemp.renameTo(messagesFile) && messagesGlobalFileTemp.renameTo(messagesGlobalFile)){
				check = true;
			}
			System.out.println(time() + "Deleted message: <" + messageDeleteID + "> : " + check + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return check;
	}

    public ArrayList<String[]> getExistingMessages(){
    	File messagesFile = new File("files/messages.txt");
		Scanner messagesFileScanner;
		ArrayList<String[]> messagesList = new ArrayList<String[]>();
		try {
			messagesFileScanner = new Scanner(messagesFile);
			while (messagesFileScanner.hasNext()) {
				String wholeLine = messagesFileScanner.nextLine();
				if (!(wholeLine.isEmpty())){
					String[] arr = wholeLine.split(SEPARATOR);
					messagesList.add(arr);
				}
			}
			messagesFileScanner.close();
			System.out.println(time() + "Get all available messages from the server\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return messagesList;
    }

	public static String time(){
		return DATE_FORMAT.format(new Date()) + " - ";
	}
}

