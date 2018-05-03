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

    public void execute(String command, String operand){
        
        try{
            // sends command to the server
            
            if(operand == null){
                socket.getOutputStream().write(command.getBytes());
            } else {
                socket.getOutputStream().write((command+" "+operand).getBytes());
            }
            
            byte[] buf = new byte[64*1024];
            
            int l = 0;
            while(l == 0){
                l = socket.getInputStream().read(buf);
                if(l > 0){
                    System.out.println(new String(buf, 0, l));
                }
            }
            //oos.flush();
            
            //oos.writeObject("a");
            //ois = new ObjectInputStream(socket.getInputStream());
            //byte[] buf = new byte[64*1024];
            
            // prints the answer
            //String text = (String)(ois.readObject());
            //System.out.println(text);
            
            /*

            */
            
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
