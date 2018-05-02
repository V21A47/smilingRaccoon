import Interpreter.Interpreter;

import java.nio.channels.SocketChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.ByteBuffer;
import java.io.*;
import java.net.*;

class Server extends Thread{
    private SocketChannel sc;
    
    private static String host = "localhost";
    private static int port = 3128;
    
    public static void main(String[] args){
        try{
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress(host, port));
            
            while(true){
                new Server(ssc.accept());
            }
        } catch (Exception e){
            System.err.println(e);
        }
    }
    
    public Server(SocketChannel s){
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
                    System.out.println(text);
                    
                    // send
                    buf.clear();
                    buf.put("hello".getBytes());
                    buf.flip();
                    sc.write(buf);
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
