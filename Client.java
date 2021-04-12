class Client
{
    Socket socket;
    InetAddress IP;
    int PORT;
    String username;

    Client(Socket socket)
    {
        this.socket = socket;
        PORT = socket.getPort();
        IP = socket.getLocalAddress();
    }
}
