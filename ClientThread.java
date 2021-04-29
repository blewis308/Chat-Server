import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.*;

public class ClientThread extends Thread {
    Socket socket;
    DataInputStream dataIn;
    DataOutputStream dataOut;
    int usernameIndex;
    int clientIndex;
    byte[] out;
    
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
                        talk("["+Server.usernames.get(usernameIndex)+"] "+ msgData);
                        break;
                    case 3: // list
                        list();
                        break;
                    case 4: // direct
                        direct(msgData);
                    case 5: // error / default
                    default: // default
                        error();
                }
                //send stuff back
            }
        }
        catch (IOException e) {
            System.err.println("Exception in Client thread: " + e);
        }
    }

    void joinServer(String message) throws IOException {

        boolean taken = false;
        
        for (int i = 0; i < Server.clients.size(); i++) {
            if(Server.usernames.contains(message))
            {
                command = 1;
                out = new byte[]{command};
                dataOut.write(out);

                taken = true;
                break;
            }
        }

        if (!taken){
            Server.usernames.add(message);
            command = 0;
            out = new byte[]{command};
            dataOut.write(out);

            usernameIndex = Server.usernames.size()-1;
            talk("- "+Server.usernames.get(usernameIndex)+" connected -");
        }

        
    }

    public void leaveServer(String message){
        talk("- "+Server.usernames.get(usernameIndex)+" disconnected -");
    }

    public void talk(String message){
        byte command = 2;
        byte[] msgData = message.getBytes();
        short msgLen = Short.valueOf(message);

        for (int i = 0; i < Server.usernames.size(); i++) {// iterate through client threads and send the message to everyone
            Server.clients.get(i).sendMessage(command, msgLen, message);
        }
//        sendMessage();
    }

    public void list(){

        StringBuilder ul = new StringBuilder();
        ul.append("Connected users: \n");

        for (int i = 0; i < Server.usernames.size(); i++) {
            ul.append(Server.usernames.get(i)+" \n");
        }

        msgLen = Short.valueOf(ul.toString());

        sendMessage(command ,msgLen  ,ul.toString());
    }

    public void direct(String message){

        String messageArray[] = message.split(" ");
        String sendTo = messageArray[0];

        message = message.replace(sendTo+" ", "");

        int userIndex = 0;

        for (int i = 0; i < Server.usernames.size(); i++) {
            if (sendTo.equals(Server.usernames.get(i))){
                userIndex = i;
                break;
            }
        }

        msgLen = Short.valueOf(message);

        Server.clients.get(userIndex).sendMessage(command, msgLen, message);
    }

    public void error(){
        String message = "[Error] Invalid command";
        byte error = 5;
        msgLen = Short.valueOf(message);

        sendMessage(error, msgLen, message);
    }
    
    public boolean sendMessage(byte command, short msgLen, String message){
        byte[] msg;
        ByteBuffer bytePkg;
        try {
            //Three bytes for header, then however many needed for the actual data
            bytePkg = ByteBuffer.allocate(3 + msgLen);
            //Pack the information: both header and data
            bytePkg.put(command);
            bytePkg.putShort(msgLen);
            msg = stringToAscii(message);
            bytePkg.put(msg);
            //Send to the server
            dataOut.write(bytePkg.array());
        } catch (Exception e){
            System.err.println(e);
            return false;
        }

        return true;
    }
    
    public String asciiToString(byte[] PAB) {
        if (PAB == null || PAB.length == 0)
        return "";
        char[] arrayOfChar = new char[PAB.length];
        for (byte b = 0; b < PAB.length; b++) {
            arrayOfChar[b] = (char)PAB[b];
        }
        return new String(arrayOfChar);
    }

    public byte[] stringToAscii(String paramString) throws UnsupportedEncodingException {
        if (paramString != null){
            return paramString.getBytes("US-ASCII");
        }
        else {
            return new byte[1];
        }
    }
}