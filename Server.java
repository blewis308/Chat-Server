import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;

public class Server {
    static ArrayList<ClientThread> clients;
    static ArrayList<String> usernames = new ArrayList<>();
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
                client.usernameIndex = clients.size()-1;
                client.start();
            }
        }
        catch (IOException e) {
            System.err.println("Exception in server: " + e);
        }
    }
    
    static void sendall(byte command, short msglen, byte[] message)
    {
        ByteBuffer buffer;
        try{
            buffer = ByteBuffer.allocate(3 + msglen);
            buffer.put(command);
            buffer.putShort(msglen);
            buffer.put(message);
            
            for(ClientThread c : clients)
            {
                c.dataOut.write(buffer.array());
            }
            
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}