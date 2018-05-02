 package Interpreter;

import java.nio.channels.SocketChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.ByteBuffer;
import java.io.*;
import java.net.*;

public class Receiver extends Thread{
    private SocketChannel sc;
    private Interpreter interpreter = null;
    
    private String host = "localhost";
    private int port = 3128;
    
    
    public Receiver(String fileName){
        interpreter = new Interpreter(fileName);
        
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
            while(sc.isConnected()){
                if(sc.socket().isClosed()){
                    break;
                }
                
                //get 
                buf.clear();
                int bytesRead = sc.read(buf);
                //
                
                if(bytesRead > 0){
                    String text = new String(buf.array(), 0, bytesRead);
                    System.out.println("--> " + text);
                    
                    if(!interpreter.getCommand(text)){
                        buf.clear();
                        bytesRead = sc.read(buf);
                    }
                    // send
                    //buf.clear();
                    //buf.put("hello".getBytes());
                    //buf.flip();
                    //sc.write(buf);
                    //
                }
                Thread.sleep(100);
            }
        } catch (Exception e){
            System.err.println(e);
            return;
        }
    }
}
