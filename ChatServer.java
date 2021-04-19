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
        try
        {
            ServerSocket serverSocket = new ServerSocket(PORT, BACKLOG);
            while(true)
            {
                Socket socket = serverSocket.accept();
                Client user = new Client(socket);
                user.start();
            }
        }
        catch (Exception exception)
        {
            System.err.println(exception);
            System.exit(1);
            return;
        }
    }

    public void run()
    {
        try
        {
            Client user = new Client(this.clsocket);
        }
        catch (Exception e)
        {
            System.err.println(e);
            System.exit(1);
        }
    }

    /*
    Need method to parse the commands from the client
    Maybe have it take the data from the client as input then run the command variable through a switch statement?
    The switch could send it to a method to actually deal with it. This might be too insignificant to have a whole method for each command

     */
    public void parseCommand(byte commandNumber, short messageLength, String message){

        switch (commandNumber){
            case 0: //join

                break;
            case 1: //leave

                break;
            case 2: //talk

                break;
            case 3: //list
                //This space intentionally left blank until implemented
            case 4: //direct
                //This space intentionally left blank until implemented
            default: //error

        }
    }
}
