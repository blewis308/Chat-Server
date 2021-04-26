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
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ChatServer extends Thread{
    public static int PORT = 9000;
    public static int BACKLOG = 5;

    Socket clsocket;

    public static void main(String args[])
    {
        Server server = new Server(PORT);
        System.out.println("starting server...");
        server.start();
    }
}

// public class Server {
//     public ArrayList<ClientThread> clients;
//     private int port = 9000; // default
    
//     public Server(int port) {
//         this.port = port;
        
//         clients = new ArrayList<ClientThread>();
//     }
    
//     public void start() {
//         try {
//             ServerSocket ss = new ServerSocket(port);
//             System.out.println("server started, waiting for client...");
//             while(true) {
//                 Socket socket = ss.accept();
//                 System.out.println("client accepted...");
//                 ClientThread client = new ClientThread(socket);
//                 clients.add(client);
//                 client.start();
//             }
//         }
//         catch (IOException e) {
//             System.err.println("Exception in server: " + e);
//         }
//     }
// }

// public class ClientThread extends Thread {
//     Socket socket;
//     DataInputStream dataIn;
//     DataOutputStream dataOut;
    
//     static ArrayList<String> usernames = new ArrayList<>();
    
//     int command;
//     short msgLen;
//     String msgData;
    
//     public ClientThread(Socket socket)
//     {
//         this.socket = socket;
//         try {
//             dataIn = new DataInputStream(socket.getInputStream());
//             dataOut = new DataOutputStream(socket.getOutputStream());
            
//             while(true) {
//                 dataIn.read();
//                 command = dataIn.readByte();
                
//                 if(command != 3) {
                    
//                 }
                
//                 switch(command)
//                 {
//                     case 0: // join
//                         joinServer(msgData);
//                         break;
//                     case 1: // leave
//                         leaveServer(msgData);
//                         break;
//                     case 2: // talk
//                         talk(msgData);
//                         break;
//                     case 3: // list

//                     case 4: // direct

//                     case 5: // error / default
//                     default: // default

//                 }
//             }
//         }
//         catch (IOException e) {
//             System.err.println("Exception in Client thread: " + e);
//         }
//     }

//     public void joinServer(String message){

//         byte[] messageLength;// 

//         boolean taken = false;
        
//         for (int i = 0; i < usernames.size(); i++) {
//             if (usernames.contains(message)){
//                 message = "1";
//                 taken = true;
//                 break;
//             }
//         }

//         if (!taken){
//             usernames.add(message);
//             message = "0";
//         }

//         messageLength = message.getBytes(StandardCharsets.US_ASCII);

//         msgData = message;
//         msgLen = (short)messageLength.length;
        
        
//     }

//     public void leaveServer(String message){

//     }

//     public void talk(String message){

//     }
// }