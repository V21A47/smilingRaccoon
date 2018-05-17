package Interpreter;

import java.nio.channels.SocketChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.ByteBuffer;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


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

    @Override
    public void run(){
        try{
            System.err.println("Connection to " + sc.getRemoteAddress() + " now is active.");
            ByteBuffer buff = null;

            while(sc.isOpen()){

                // Receive
                byte [] get = new byte[1024];
                sc.read(ByteBuffer.wrap(get));
                ByteArrayInputStream bis = new ByteArrayInputStream(get);
                ObjectInputStream ois = new ObjectInputStream(bis);
                Object obj = ois.readObject();
                String str = (String)obj;

                String result = interpreter.getCommand(str);

                System.out.println(sc.getRemoteAddress() + ":   " + str);

                Thread.sleep(25);

                // Send
                get = new byte[1024];
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos=new ObjectOutputStream(bos);
                String message = result;
                oos.writeObject(message);
                oos.flush();

                byte[]serializedObject = bos.toByteArray();
                sc.write(ByteBuffer.wrap(serializedObject));

                Thread.sleep(25);
            }

        } catch(IOException e){
            try{
                System.err.println("Connection to " + sc.getRemoteAddress() + " was closed.");
            } catch (IOException eee){}
            return;
        } catch (Exception e){
            System.err.println(e);
            System.err.println(e.getMessage());
            return;
        }
    }
}
