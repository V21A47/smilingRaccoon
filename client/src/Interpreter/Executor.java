package Interpreter;

import AliveObjects.Human;
import java.io.*;
import java.net.*;

public class Executor{

    private int numberOfCalls = 0;
    private int maxNumberOfCalls = 30;
    
    private String host = "localhost";
    private int port = 3128;
    
    private Socket socket = null;
    
    private Socket getSocket() throws InterruptedException{
        Socket newSocket = null;
        
        //System.err.println("Try to connect: " + numberOfCalls);
        if(numberOfCalls == maxNumberOfCalls){
            return null;
        }
        
        try{
            newSocket = new Socket(host, port);
        } catch (SocketException e){
            Thread.sleep(1000);
            numberOfCalls+=1;
            newSocket = getSocket();
        } catch (UnknownHostException e){
            Thread.sleep(1000);
            numberOfCalls+=1;
            newSocket = getSocket();
        } catch (IOException e){
            Thread.sleep(1000);
            numberOfCalls+=1;
            newSocket = getSocket();
        }
        
        numberOfCalls = 0;
        return newSocket;
    }
    
    public Executor(){
        try{
            socket = getSocket();
            if(socket == null){
                System.err.println("Server is not available");
                System.exit(1);
            }
        } catch (InterruptedException e){
            System.err.println(e);
        }
    }

    public void execute(String command, Human humanObject){

        /*
        if(humanObject == null){
            System.out.println("got command:\n\t"+command+"\n\tnull");
        } else {
            System.out.println("got command:\n\t"+command+"\n\t"+humanObject.toString());
        }
        */
        
        try{
            // sends command to the server
            socket.getOutputStream().write(command.getBytes());
            
            byte[] buf = new byte[64*1024];
            
            // prints the answer
            int l = 0;
            while(l == 0){
                l = socket.getInputStream().read(buf);
                if(l > 0){
                    System.out.println(new String(buf, 0, l));
                }
            }
            
        } catch (ConnectException e){
            System.err.println("Server is not available now");
            try{
                socket = getSocket();
                if(socket == null){
                    System.err.println("Server is not available");
                    System.exit(1);
                }
            } catch (InterruptedException ex){
                System.err.println(ex);
            }

        } catch (SocketException e){
            System.err.println("Server is not available now");
            try{
                socket = getSocket();
                if(socket == null){
                    System.err.println("Server is not available");
                    System.exit(1);
                }
            } catch (InterruptedException ex){
                System.err.println(ex);
            }
        } catch (Exception e){
            System.err.println(e);
            System.err.println("Something bad was done");
        }
        
    }
}
