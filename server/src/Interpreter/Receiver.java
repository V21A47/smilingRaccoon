package Interpreter;

import java.nio.channels.SocketChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.ByteBuffer;
import java.io.*;
import java.net.*;
import java.util.*;

public class Receiver extends Thread{
    private SocketChannel sc;
    private Interpreter interpreter = null;
    
    private String host = "localhost";
    private int port = 3128;
    
    
    public Receiver(Interpreter inter){
        interpreter = inter;
        
        try{
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress(host, port));
            
            while(true){
                new Receiver(interpreter, ssc.accept());
            }
        } catch (Exception e){
            System.err.println(e);
        }
    }
    
    
    public Receiver(Interpreter i, SocketChannel s){
        interpreter = i;
        this.sc = s;
        start();
    }
    
    
    public void run(){
        try{                        
            ByteBuffer buf = ByteBuffer.allocate(64*1024);
            ByteArrayInputStream bais = null;
            
            while(sc.isConnected()){
                if(sc.socket().isClosed()){
                    break;
                }
                
                //get 
                buf.clear();
                int bytesRead = sc.read(buf);
                //
                
                if(bytesRead > 0){
                    String text =  new String(buf.array(), 0, bytesRead);

                    buf.clear();
                    
                    buf.put((interpreter.getCommand(text)).getBytes());
                    
                    buf.flip();
                    sc.write(buf);
                }
                Thread.sleep(100);
            }
        } catch (Exception e){
            System.err.println(e);
            System.err.println(e.getMessage());
            return;
        }
    }
}
