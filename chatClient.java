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

public class chatClient {
    public static final int PORT = 9000;
    static final int JOIN = 0;
    static final int LEAVE = 1;
    static final int TALK = 2;
    static final int LIST = 3;
    static final int DIRECT = 4;


    public static void main(String[] args){
        if (args.length != 1){
            System.out.printf("Usage: chatClient.java <ip address> \n");
            System.exit(-1);
        }

        Scanner stdin = new Scanner(System.in);
        Socket server = new Socket(args[1], PORT);
        String username = "";
        boolean usernameAccepted = false;
        String message;
        ByteBuffer command = ByteBuffer.allocate(1);
        ByteBuffer msgLength = ByteBuffer.allocate(2);
        byte[] msg;
        DataInputStream inputStream = new DataInputStream(server.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(server.getOutputStream());
        ByteBuffer byteOutput;
        command.order(ByteOrder.BIG_ENDIAN);
        msgLength.order(ByteOrder.BIG_ENDIAN);

        //Join request
        command.putInt(JOIN);
        while (!usernameAccepted){
            System.out.printf("Please enter the username you would like to use: ");
            username = stdin.nextLine();
            msgLength.putInt(username.length());
            msg = stringToAscii(username);
            outputStream.write(command.array());
            outputStream.write(msgLength.array());
            outputStream.write(msg);
        }

    }

    //translates the words recieved from the client from ASCII to strings - written by Tanner Waters
    private static String asciiToString(byte[] PAB) {
        if (PAB == null || PAB.length == 0)
        return "";
        char[] arrayOfChar = new char[PAB.length];
        for (byte b = 0; b < PAB.length; b++)
        arrayOfChar[b] = (char)PAB[b];
        return new String(arrayOfChar);
    }

    //makes sure the string bytes are translated from US-ASII - written by Tanner Waters
    private static byte[] stringToAscii(String paramString) throws UnsupportedEncodingException {
        return paramString.getBytes("US-ASCII");
    }

}
