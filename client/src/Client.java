import Interpreter.Reader;
import java.io.IOException;

import java.io.*;
import java.net.*;

public class Client{    
    private static int numberOfCalls = 0;
    
    private static int maxNumberOfCalls = 15;
    
    private static String host = "localhost";
    private static int port = 3128;
    
    
    private static Socket getSocket() throws InterruptedException{
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
    
    public static void main(String[] args){
        boolean isShowed = false;
        Reader reader = new Reader();
        
        Socket s = null;
        try{
            s = getSocket();
            if(s == null){
                return;
            }
        } catch (InterruptedException e){
            System.err.println(e);
        }
        
        while(true){
        
            // work HERE ->
            try{
                for(int i = 0; i <10; i+=1){
                    s.getOutputStream().write((args[0] + ": " + i*i).getBytes());
                    
                    byte[] buf = new byte[64*1024];
                    int r = s.getInputStream().read(buf);
                    if(r > 0){
                        String text = new String(buf, 0, r);
                        System.out.println(text);
                    }
                    
                    Thread.sleep(Integer.parseInt(args[1])*1000);
                }
                
                
                s.close();
                break;
        
            } catch (ConnectException e){
                System.err.println("Server is not available now");
                try{
                    s = getSocket();
                    if(s == null){
                        return;
                    }
                } catch (InterruptedException ex){
                    System.err.println(ex);
                }

            } catch (SocketException e){
                System.err.println("Server is not available now");
                try{
                    s = getSocket();
                    if(s == null){
                        return;
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
}
