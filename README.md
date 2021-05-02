# Chat Server
 a Java based chat server/client program

## How to run

1. Use `javac ChatServer.java Server.java ClientThread.java` to compile the Server and its components.

2. Use `javac ChatClientGui.java Gui.java` to compile the Client

3. Run Server **first** by using `java ChatServer`

4. Run Clients by using `java ChatClientGui <server-ip>` where **`<server-ip>` is the IP Address of the Server**


## How to use

After running both server and client, and having connected to the server, you first enter your username in the textbox centered at the bottom.
This can be any username you wish, so as it is only ascii characters.
Close the server by using Ctrl + C.

Once you have selected your username, there are a few commands listed that are available:

#### **JOIN**
This is what you just did, joining a chat server.

#### **LEAVE**
To leave a chat server, select the options tab in the top left, then click disconnect.

#### **TALK**
To talk in a chat server, type what you wish to talk and send your message using the enter key.

#### **LIST**
To list the currently connected users, type **`/all`** and the connected users will be listed.

#### **DIRECT**
To directly message a specific user, use **`/w <username> <message>`** to send a message to that user.


## Things to note:
**All** communications are logged; this includes direct messages too. Hate speech and non-LeTourneau appropriate messages are not tolerated. If needed, your username, IP Address, and messages will be reviewed by administration; subsequent action will be taken.

Please report any bugs, crashes, or other unexpected behavior to an admin. Issues presented will be addressed and fixed in a timely manner.

Have fun! This is a welcoming environment; all people from every walk of life are invited with open arms!