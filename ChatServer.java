/*
    For more up-to-date info, check the Discord or your email.

    General Goal:
        To implement a chat server using an interoperable protocol.
        Server runs on port 9000

    Commands will be formatted as follows:
        <command num> <length> <message ascii>
        <1 byte> <2 byte> <length # of chars>

    Commands so far:
    Join -      <0> <len of name> <username>
    Leave -     <1> <null> <null>
    Talk -      <2> <len of message> <message>
    List -      <3>
    Direct -    <4>
    Error -     <5>

    Server will respond by sending this same format to all clients (including
    the sender) and will append the senders username to the beginning of <message>
*/

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer extends Thread{
    public static int PORT = 9000;
    public static int BACKLOG = 5;

    Socket clsocket;

    public static void main(String args[])
    {
        
    }
}

class Server {
    private ArrayList<ClientThread> clients;
    private int port = 9000;
    
    public Server(int port) {
        this.port = port;
        
        clients = new ArrayList<ClientThread>();
    }
    
    public void start() {
        try {
            ServerSocket ss = new ServerSocket(port);
            while(true) {
                Socket socket = ss.accept();
                ClientThread client = new ClientThread(socket);
                clients.add(client);
                client.start();
            }
        }
        catch (IOException e) {
            System.err.println("Exception in server: " + e);
        }
    }
}

class ClientThread extends Thread {
    Socket socket;
    DataInputStream dataIn;
    DataOutputStream dataOut;
    
    String username;
    
    int command;
    byte[] msgLen = new byte[2];
    byte[] msgData;
    
    public ClientThread(Socket socket)
    {
        this.socket = socket;
        try {
            dataIn = new DataInputStream(socket.getInputStream());
            dataOut = new DataOutputStream(socket.getOutputStream());
            
            while(true) {
                dataIn.read();
                command = dataIn.readByte();
                
                if(command != 3) {
                    
                }
                
                switch(command)
                {
                    case 0: // join
                        break;
                    case 1: // leave
                        break;
                    case 2: // talk
                        break;
                    case 3: // list
                        break;
                    case 4: // direct
                        break;
                    case 5: // error / default
                    default: // default
                        break;
                }
            }
        }
        catch (IOException e) {
            System.err.println("Exception in Client thread: " + e);
        }
    }
}