import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.logging.*;
import java.util.logging.Logger;

public class Server {
    private static final Logger logger = Logger.getLogger("ChatServer");
    static ArrayList<ClientThread> clients;
    static ArrayList<String> usernames = new ArrayList<>();
    private int port = 9000; // default
    public static Queue clientNumberToRemove = new LinkedList<>();
    private RemoveClientThread removeClientThread;
    private Handler fileHandler;
    java.util.logging.Formatter basicFormat;

    public Server(int port) throws FileNotFoundException, IOException{
        this.port = port;
        basicFormat = new SimpleFormatter();
        fileHandler = new FileHandler("chatServer.log");
        fileHandler.setFormatter(basicFormat);
        removeClientThread = new RemoveClientThread("Remove Client");
        clients = new ArrayList<ClientThread>();
        removeClientThread.start();
    }

    public void start() {
        try {
            ServerSocket ss = new ServerSocket(port);
            System.out.println("server started, waiting for client...");
            logger.addHandler(fileHandler);
            addLog("Started server.");
            while(true) {
                Socket socket = ss.accept();
                System.out.println("client accepted...");
                ClientThread client = new ClientThread(socket);
                clients.add(client);
                client.usernameIndex = clients.size()-1;
                client.start();
            }
        }
        catch (IOException e) {
            System.err.println("Exception in server: " + e);
        }
    }

    public static void removeClient(){
        int currentNumber = -1;
        ClientThread client;
        while (true){
            if (clientNumberToRemove.size() > 0){
                client = (ClientThread)clientNumberToRemove.remove();
                currentNumber = client.usernameIndex;
                System.err.printf("Removing %d\n", currentNumber);
                clients.remove(currentNumber);
                usernames.remove(currentNumber);
            }
        }
    }

    static void sendall(byte command, short msglen, byte[] message)
    {
        ByteBuffer buffer;
        //System.out.println("in sendall: " + command + " " + msglen + " " + message);
        try{
            buffer = ByteBuffer.allocate(3 + msglen);
            buffer.put(command);
            buffer.putShort(msglen);
            buffer.put(message);

            //System.out.println("Before for loop");
            //System.out.println(clients.size());
            for(int c = 0; c < clients.size(); c++)
            {
                //System.out.println("Sending data for user #" + c);
                clients.get(c).dataOut.write(buffer.array());
                //System.out.println("Data sent for user #" + c);
            }
            //System.out.println("After for loop");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    //makes sure the string bytes are translated from US-ASII - written by Tanner Waters
    private static byte[] stringToAscii(String paramString) throws UnsupportedEncodingException {
        if (paramString != null){
            return paramString.getBytes("US-ASCII");
        }
        else {
            return new byte[1];
        }
    }

    public static void addLog(String theStr){
        logger.info(theStr + "\n");
    }
}

class RemoveClientThread extends Thread {
    private Thread thread;
    private String threadName;

    RemoveClientThread(String name){
        this.threadName = name;
    }

    public void run(){
        try {
            System.err.printf("Running remove clients thread.\n");
            Server.removeClient();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void start(){
        if (thread == null){
            thread = new Thread(this, this.threadName);
            thread.start();
        }
    }

}