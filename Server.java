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
}