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
    byte[] msgBytes;
    String msgData;
    //NEEDS TO BE THREADSAFED
    PrintWriter logFile = new PrintWriter(new File("connectionLog.txt"));

    public ClientThread(Socket socket) throws IOException
    {
        this.socket = socket;
        dataIn = new DataInputStream(socket.getInputStream());
        dataOut = new DataOutputStream(socket.getOutputStream());
        logFile.println("Connection accepted. IP Address: " + socket.getRemoteSocketAddress() + " Port: " + socket.getPort());
    }

    public void run()
    {
        try {
            while(true) {
                command = dataIn.readByte();

                if(command != (byte)3)
                {
                    msgLen = dataIn.readShort();
                    msgBytes = new byte[3 + Integer.valueOf(msgLen)];
                    dataIn.read(msgBytes);
                    msgData = asciiToString(msgBytes);
                }

                switch((int)command)
                {
                    case 0: // join
                        joinServer(msgData);
                        break;
                    case 1: // leave
                        leaveServer(msgData);
                        break;
                    case 2: // talk
                        System.out.println("Sent a message");
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
            }
        }
        catch (IOException e) {
            System.err.println("Exception in Client thread: " + e);
        }
    }

    void joinServer(String message) throws IOException {

        boolean taken = false;
        String workingCommands = "Available Commands: \n join \n leave \n talk \n list \n direct \n error \n";

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
            //System.out.println("username: " + message);
            usernameIndex = Server.usernames.size();
            Server.usernames.add(message);
            command = 0;
            out = new byte[]{command};
            dataOut.write(out);

            //System.out.println("index: " + usernameIndex);
            //System.out.println(Server.usernames.get(usernameIndex));
            String talkmsg = "- " + Server.usernames.get(usernameIndex) + " connected -";

            talk(talkmsg);

            sendMessage((byte)2,(short) workingCommands.length(), workingCommands);
        }
    }

    public void leaveServer(String message) throws UnsupportedEncodingException
    {
        talk("- "+Server.usernames.get(usernameIndex)+" disconnected -");
    }

    public void talk(String message) throws UnsupportedEncodingException
    {
        byte command = (byte)2;
        byte[] msgData = stringToAscii(message);
        short msgLen = (short) message.length();

        Server.sendall(command, msgLen, msgData);
    }

    public void list(){

        StringBuilder userList = new StringBuilder();
        //userList.append("Connected users: \n");

        for (int i = 0; i < Server.usernames.size(); i++) {
            userList.append(" \n" + Server.usernames.get(i));
        }

        msgLen = (short) userList.toString().length();

        sendMessage(command ,msgLen  ,userList.toString());
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

        msgLen = (short) message.length();

        Server.clients.get(userIndex).sendMessage(command, msgLen, message);
    }

    public void error(){
        String message = "[Error] Invalid command";
        byte error = 5;
        msgLen = (short) message.length();

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
            return paramString.getBytes(StandardCharsets.US_ASCII);
        }
        else {
            return new byte[1];
        }
    }
}