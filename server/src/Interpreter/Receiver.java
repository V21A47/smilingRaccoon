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
import java.net.ConnectException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import AliveObjects.*;


public class Receiver extends Thread{
    private SocketChannel sc;
    private Interpreter interpreter = null;

    private String host = "localhost";
    private int port = 3129;

    private HashSet<Human> set;

    public Receiver(Interpreter inter, String host, int port){
        this.host = host;
        this.port = port;

        interpreter = inter;

        set = new HashSet<>();

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


    private void send(Serializable obj) throws ConnectException, InterruptedException{
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos=new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            byte[] serializedObject = bos.toByteArray();
            sc.write(ByteBuffer.wrap(serializedObject));

            Thread.sleep(10);
        } catch (IOException e){
            System.err.println(e);
            throw new ConnectException(e.getMessage());
        }
    }


    private Object receive() throws ConnectException{
        try{
                byte [] get = new byte[1024];
                sc.read(ByteBuffer.wrap(get));
                ByteArrayInputStream bis = new ByteArrayInputStream(get);
                ObjectInputStream ois = new ObjectInputStream(bis);
                return ois.readObject();

        } catch (IOException|ClassNotFoundException e){
            //System.err.println(e);
            throw new ConnectException(e.getMessage());
        }
    }


    @Override
    public void run(){
        try{
            System.err.println("Connection to " + sc.getRemoteAddress() + " now is active.");
            ByteBuffer buff = null;

            while(sc.isOpen()){
                //Byte in = (Byte)receive();

                // If this set is different to the set from storage - change this one and send the differences
                if(set == null){
                    set = new HashSet<>();//System.out.println("set is null: 1");
                }


                if(! set.equals(interpreter.getStorage().getSet()) ){
                    HashSet<Human> setFromStorage = new HashSet<>();
                    setFromStorage.addAll(interpreter.getStorage().getSet());
                    HashSet<Human> tempToAdd = new HashSet<>();
                    HashSet<Human> tempToRemove = new HashSet<>();

                    for(Human human : setFromStorage){
                        if(!set.contains(human)){
                            System.out.println("Will be added:  " + human);
                            tempToAdd.add(human);
                        }
                    }

                    for(Human human : set){
                        if(!setFromStorage.contains(human)){
                            System.out.println("Will be removed:  " + human);
                            tempToRemove.add(human);
                        }
                    }

                    set = setFromStorage;

                    if(tempToAdd.size() > 0 || tempToRemove.size() > 0){

                        for(Human human : tempToRemove){
                            byte answer = ((Byte)receive()).byteValue();

                            send(new Byte( (byte)2));
                            //System.out.println("command 2 was sent");
                            answer = ((Byte)receive()).byteValue();
                            if(answer == 1){
                                send(human);

                                //Thread.sleep(80);
                                if(((Byte)receive()).byteValue() != 1){
                                    return;
                                }

                                //System.out.println(human + " was sent");
                            }
                        }

                        for(Human human : tempToAdd){
                            byte answer = ((Byte)receive()).byteValue();

                            send(new Byte( (byte)1));
                            //System.out.println("command 1 was sent");
                            answer = ((Byte)receive()).byteValue();
                            if(answer == 1){
                                send(human);

                                //Thread.sleep(80);
                                if(((Byte)receive()).byteValue() != 1){
                                    return;
                                }

                                //System.out.println(human + " was sent");
                            }
                        }

                    } else {
                        byte answer = ((Byte)receive()).byteValue();
                        send( new Byte( (byte)0));
                    }
                } else {
                    byte answer = ((Byte)receive()).byteValue();
                    send( new Byte( (byte)0));
                }
            }

        } catch (IOException e){
            try{
                System.err.println("Connection to " + sc.getRemoteAddress() + " was closed.");
                System.err.println(e.getMessage());
            } catch (IOException eee){}
            return;
        } catch (Exception e){
            System.err.println("Unexpected problem was found in Reciever");
            System.err.println(e);
            return;
        }
    }
}
