
import java.util.*;
import java.time.*;
import java.io.*;

public class Logger extends Thread {
    private static PrintWriter logFile;
    private Queue<String> logQueue = new LinkedList<>();

    //Empty constructor
    Logger() throws FileNotFoundException{
        if (logFile == null){
            logFile = new PrintWriter(new File("serverLog.txt"));
        }
    }

    public void run(){
        try {
            while (true){
                if (this.logQueue.size() > 0){
                    printLog(logQueue.remove());
                }
            }

        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void addLog(String logStr){
        if (logStr != null){
            this.logQueue.add(logStr);
        }
    }

    private static void printLog(String logStr){
        if (logStr != null){
            logFile.printf("%s\n", logStr);
        }
    }

}