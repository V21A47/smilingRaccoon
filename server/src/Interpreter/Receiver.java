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
                    
                    //System.out.println("-> " + text);
                    
                    
                    //bais = new ByteArrayInputStream(buf.array());
                    //ObjectInputStream ois = new ObjectInputStream(bais);
                    
                    
                    
                    //System.out.println((String)(ois.readObject()));
                    
                    /*
                    byte[] shortBuffer = new byte[bytesRead];
                    
                    for(int i = 0; i < bytesRead; i++){
                        shortBuffer[i] = buf.array()[i];
                    }
                    
                    
                    bais = new ByteArrayInputStream(shortBuffer);
                    System.out.println("read");
                    
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    System.out.println("read");
                    System.out.println(buf.array());
                    
                    
                    Object text = ois.readObject();
                    System.out.println("read");
                    String message = (String)(text);
                    
                    
                    System.out.println("--> " + text);
                    */
                    /*

                    // send
                    //buf.clear();
                    //buf.put("hello".getBytes());
                    //buf.flip();
                    //sc.write(buf);
                    //
                    */
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
