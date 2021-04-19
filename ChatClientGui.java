/*

    For more up-to-date info, check the Discord or your email.

    General Goal:
        To implement a chat server using an interoperable protocol.
        Server runs on port 9000

    Client will communicate to the server with the format:
    <Command#><Length><Message (ASCII)>
    <1 Byte><2 Bytes><Length# of Chars>

    (Client-side)
    Join - <0><Length of name><Username>
    The server will reply with message set to: 0 - accepted, 1 - name taken, based on the availability of the username with each attempt until one succeeds.
    Leave - <1><Length><Message>
    Tells the server that the client is disconnecting.
    Talk - <2><Length of Message><Message>
    The server will respond by sending this same format to all clients (including the sender) and insert the sender's username in brackets to the beginning of <Message>.
    List - <3><NULL><NULL>
    Server responds with Talk format (still command 3) and sends all connected usernames in Message.
    Direct - <4><Length><Message>

    Server should send a Talk message including all available commands as soon as the client connects.
    Error - <5><Length of message><Error Message>
    Uses username "Error" and sends message to violating client.

*/

import java.io.*;
import java.util.*;
import java.net.*;
import java.nio.*;


public class ChatClient {
    public static final int PORT = 9000;
    static final byte JOIN = 0;
    static final byte LEAVE = 1;
    static final byte TALK = 2;
    static final byte LIST = 3;
    static final byte DIRECT = 4;
    private static boolean leaving = false;


    public static void main(String[] args){
        if (args.length != 1){
            System.out.printf("Usage: chatClient.java <ip address> \n");
            System.exit(-1);
        }

        try {
            Scanner stdin = new Scanner(System.in);
            Socket server = new Socket(args[0], PORT);
            String username = "";
            boolean usernameAccepted = false;
            String message;
            ByteBuffer command = ByteBuffer.allocate(1);
            ByteBuffer msgLength = ByteBuffer.allocate(2);
            byte[] msg;
            DataInputStream inputStream = new DataInputStream(server.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(server.getOutputStream());
            ByteBuffer byteOutput;
            byte tempByte;
            String svrMsg = "";
            boolean done = false;
            OutputThread outputToScreen;
            Gui thisGui;

            //Start gui
            thisGui = new Gui();

            //Get command list from the server and print it


            //Join request
            while (!usernameAccepted){
                System.out.printf("Please enter the username you would like to use: ");
                username = stdin.nextLine();

                //Join the server and check the response.
                if (joinServer(username, outputStream, inputStream)){
                    usernameAccepted = true;
                    System.out.printf("Username accepted.\n");
                } else {
                    System.out.printf("That username is already taken.\n");
                }
            }

            //Now start receiving messages from the server
            outputToScreen = new OutputThread("client", inputStream);

            //Accept and process input.
            while (!done){
                message = stdin.nextLine();
                done = processInput(message, outputStream, inputStream);
            }


            server.close();
        } catch (Exception ex) {
            System.err.println(ex);
            System.err.println(ex.getMessage());
            System.exit(1);
        }

    }

    private static boolean processInput(String input, DataOutputStream outputStream, DataInputStream inputStream){
        int i = 0;
        if (input == null){
            return false;
        }
        while (Character.isWhitespace(input.charAt(i))){
            i++;
        }
        //If the first non-whitespace char is a '/', then this is a command string
        if (input.charAt(i) == '/'){
            i++;
            switch (input.charAt(i)){
                case 'a':
                    //sendMessage(LIST, null, null, outputStream);
                    break;
                case 'w':
                    //sendMessage(DIRECT, (short)input.length(), input, outputStream);
                    break;
                default:
                    sendMessage(TALK, (short)input.length(), input, outputStream);
            }
        }
        else {
            sendMessage(TALK, (short)input.length(), input, outputStream);
        }

        return true;
    }

    private static boolean sendMessage(byte command, short msgLen, String message, DataOutputStream outputStream){
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
            outputStream.write(bytePkg.array());
        } catch (Exception e){
            System.err.println(e);
            return false;
        }

        return true;
    }

    private static boolean joinServer(String username, DataOutputStream outputStream, DataInputStream inputStream){
        ByteBuffer byteOutput;
        byte[] msg;
        byte tempByte;

        try {
            //Pack the ByteBuffer
            byteOutput = ByteBuffer.allocate(3+username.length());
            byteOutput.put(JOIN);
            byteOutput.putShort((short) username.length());
            msg = stringToAscii(username);
            byteOutput.put(msg);
            //Send to the server
            outputStream.write(byteOutput.array());
            //Get the response
            tempByte = inputStream.readByte();

            //Return true if username not already taken, false otherwise
            if (tempByte == 0){
                return true;
            } else {
                return false;
            }
        }
        catch (Exception ex){
            System.err.println(ex);
        }

        //If we got here then something went wrong. Return false - we did not connect.
        return false;
    }

    protected static void printMessages(DataInputStream dataInputStream){
        byte command;
        short length;
        String message;
        byte[] msgBytes;
        //Keep waiting to receive messages until the client is ready to leave the server
        try{
            while (!leaving){
                command = dataInputStream.readByte();
                length = dataInputStream.readShort();
                msgBytes = new byte[length];
                dataInputStream.read(msgBytes, 3, length);
                message = asciiToString(msgBytes);
                System.out.printf("%s\n", message);
            }
        } catch (Exception e){
            System.err.println(e);
        }
    }

    //translates the words recieved from the client from ASCII to strings - written by Tanner Waters
    private static String asciiToString(byte[] PAB) {
        if (PAB == null || PAB.length == 0)
        return "";
        char[] arrayOfChar = new char[PAB.length];
        for (byte b = 0; b < PAB.length; b++) {
            arrayOfChar[b] = (char)PAB[b];
        }
        return new String(arrayOfChar);
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

class OutputThread extends Thread {
    private Thread thread;
    private String threadName;
    private DataInputStream serverOutput;

    OutputThread(String name, DataInputStream serverStream){
        this.threadName = name;
        this.serverOutput = serverStream;
    }

    public void run(){
        try {
            ChatClient.printMessages(this.serverOutput);
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