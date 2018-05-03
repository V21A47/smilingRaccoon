package Interpreter;

import AliveObjects.Human;
import java.io.*;
import java.net.*;

import com.jcraft.jsch.*;

public class Executor{

    private int numberOfCalls = 0;
    private int maxNumberOfCalls = 30;
    
    private String host = "localhost";
    private int port = 8081;
    
    private Socket socket = null;
    private JSch jsch = null;
    private Channel ch = null;
    private OutputStream os;
    private InputStream is;
    
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
    
        jsch = new JSch();
        
        try{
            Session session = jsch.getSession("s242425", "helios.cs.ifmo.ru", 2222);
            session.setPassword("vng051");
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            session.connect();
            ch = session.getStreamForwarder("helios.cs.ifmo.ru", port);
            ch.connect();
            try{
                os = ch.getOutputStream();
                is = ch.getInputStream();
            } catch (IOException e){
                System.err.println(e);
            }
        } catch (JSchException e){
            System.err.println(e);
        }
        
    }

    public void execute(String command, String operand){
        
        try{

            
            // sends command to the server
            
            if(operand == null){
                os.write(command.getBytes());
            } else {
                os.write((command+" "+operand).getBytes());
            }
            
            byte[] buf = new byte[64*1024];
            
            int l = 0;
            while(l == 0){
                l = is.read(buf);
                if(l > 0){
                    System.out.println(new String(buf, 0, l));
                }
            }
            
        } catch (ConnectException e){
            System.err.println("Server is not available now");

        } catch (SocketException e){
            System.err.println("Server is not available now");
        } catch (Exception e){
            System.err.println(e);
            System.err.println("Something bad was done");
        }
        
    }
}
