import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;

public class Server {
    static ArrayList<ClientThread> clients;
    static ArrayList<String> usernames = new ArrayList<>();
    private int port = 9000; // default
    public static Logger logKeeper = new Logger();
    public Queue clientNumberToRemove = new LinkedList<>();

    public Server(int port) {
        this.port = port;

        clients = new ArrayList<ClientThread>();
        logKeeper.start();
    }

    public void start() {
        try {
            ServerSocket ss = new ServerSocket(port);
            System.out.println("server started, waiting for client...");
            while(true) {
                Socket socket = ss.accept();
                System.out.println("client accepted...");
                ClientThread client = new ClientThread(socket);
                clients.add(client);
                client.usernameIndex = clients.size()-1;
                client.start();
                watchForRemove.start();
            }
        }
        catch (IOException e) {
            System.err.println("Exception in server: " + e);
        }
    }

    public void removeClient(){
        int currentNumber = -1;
        while (true){
            if (clientNumberToRemove.size() > 0){
                currentNumber = clientNumberToRemove.remove();
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

            for(int c = 0; c < clients.size(); c++)
            {
                clients.get(c).dataOut.write(buffer.array());
            }
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
        logKeeper.addLog(theStr);
    }
}

class LeaveThread extends Thread {
    private Thread thread;
    private String threadName;

    LeaveThread(String name){
        this.threadName = name;
    }

    public void run(){
        try {
            System.err.printf("Running watch thread.\n");
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