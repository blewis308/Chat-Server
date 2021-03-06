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

import java.net.*;

public class ChatServer extends Thread{
    public static int PORT = 9000;
    public static int BACKLOG = 5;

    Socket clsocket;

    public static void main(String args[])
    {
        try{
            Server server = new Server(PORT);
            System.out.println("starting server...");
            server.start();
        }
        catch (Exception ex){
            System.err.println(ex);
        }
    }
}