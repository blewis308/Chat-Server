import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    static ArrayList<ClientThread> clients;
    static ArrayList<String> usernames;
    private int port = 9000; // default
    
    public Server(int port) {
        this.port = port;
        
        clients = new ArrayList<ClientThread>();
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
                client.start();
            }
        }
        catch (IOException e) {
            System.err.println("Exception in server: " + e);
        }
    }
    
    boolean hasUserName(String username)
    {
        return usernames.contains(username);
    }
}