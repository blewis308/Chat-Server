import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ClientThread extends Thread {
    Socket socket;
    DataInputStream dataIn;
    DataOutputStream dataOut;
    int usernameIndex;
    int clientIndex;
    
//    static ArrayList<String> usernames = new ArrayList<>();
    
    byte command;
    short msgLen;
    String msgData;
    
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
                        joinServer(msgData);
                        break;
                    case 1: // leave
                        leaveServer(msgData);
                        break;
                    case 2: // talk
                        talk(msgData);
                        break;
                    case 3: // list

                    case 4: // direct

                    case 5: // error / default
                    default: // default

                }
            }
        }
        catch (IOException e) {
            System.err.println("Exception in Client thread: " + e);
        }
    }

    void joinServer(String message){

        byte[] messageLength;
        boolean taken = false;
        
        for (int i = 0; i < Server.clients.size(); i++) {
            if(Server.usernames.contains(message))
            {
                command = 1;
                taken = true;
                break;
            }
        }

        if (!taken){
            Server.usernames.add(message);
            command = 0;
            usernameIndex = Server.usernames.size()-1;
            msgData = "- "+Server.usernames.get(usernameIndex)+" connected -";
            messageLength = message.getBytes(StandardCharsets.US_ASCII);
            msgLen = (short)messageLength.length;
        }

        
    }

    public void leaveServer(String message){

    }

    public void talk(String message){

        byte[] messageLength;

        messageLength = message.getBytes(StandardCharsets.US_ASCII);

        msgData = "["+Server.usernames.get(usernameIndex)+"] "+ message;
        msgLen = (short)messageLength.length;
    }
}