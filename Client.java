import java.util.*;
import java.io.*;
import java.net.*;

class Client extends Thread
{
    Socket socket;
    InetAddress IP;
    int PORT;
    String username;
    BufferedReader output;
    PrintWriter input;

    Client(Socket socket)
    {
        this.socket = socket;
        PORT = socket.getPort();
        IP = socket.getLocalAddress();
        input = new PrintWriter(this.socket.getOutputStream());
        output = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }
    
    public void run()
    {
        try{
            System.out.println("in run method.");
        }
    }
}