package Interpreter;

import AliveObjects.Human;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;

import UI.*;

public class Executor extends Thread{

    private String host = "localhost";
    private int port = 3129;

    private InputStream is;
    private OutputStream os;
    private Sheduler sheduler;

    private Socket socket = null;
    private boolean shouldWork = true;

    private HashSet<Human> set;


    public HashSet getSet(){
        return set;
    }

    private Socket getSocket() {
        Socket newSocket = null;
        try{
            newSocket = new Socket(host, port);
            is = newSocket.getInputStream();
            os = newSocket.getOutputStream();

        } catch (IOException e){
            try{
                Thread.sleep(500);
            } catch (InterruptedException er){
                System.err.println(er);
                return null;
            }
            newSocket = getSocket();
        }


        return newSocket;
    }

    public Executor(Sheduler sheduler, String host, int port){
        this.host = host;
        this.port = port;
        set = new HashSet<Human>();

        this.sheduler = sheduler;
        start();

    }

    public void run(){
        while(shouldWork){
            if(socket == null){
                socket = getSocket();
            }

            if(sheduler.getClientWindow() != null){
                sheduler.getClientWindow().changeTitle(true);
            }

            execute(null, null);

            try{
                Thread.sleep(100);
            } catch (InterruptedException e){
                System.err.println(e);
                return;
            }
        }
    }

    public Sheduler getSheduler(){
        return sheduler;
    }

    private boolean send(Serializable obj) throws IOException{
        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(obj);
        oos.flush();

        byte [] serObj = baos.toByteArray();

        os.write(serObj);

        return true;
    }

    private Object receive() throws IOException, ClassNotFoundException{
            byte [] serializedObject = new byte[1024*64];
            is.read(serializedObject);
            ByteArrayInputStream bis = new ByteArrayInputStream(serializedObject);
            ObjectInputStream ois = new ObjectInputStream(bis);

            return ois.readObject();
    }

    public void execute(String command, String operand){
        try{
            send(new Byte( (byte)0 ));

            Byte answer = (Byte)receive();


            if(answer != null){
                switch(answer.byteValue()){
                    case 0:
                        break;

                    case 1:
                        // Add an object
                        send(new Byte( (byte)1));
                        Human human = (Human)receive();
                        send(new Byte( (byte)1));
                        set.add(human);
                        break;

                    case 2:
                        // Delete an object
                        send(new Byte( (byte)1));
                        human = (Human)receive();
                        send(new Byte( (byte)1));
                        set.remove(human);
                        break;
                }

                if(answer != 0){
                    for(Human human : set){
                    }
                    if(sheduler.getClientWindow() != null){
                        sheduler.getClientWindow().update();
                    } else {
                    }
                }

            } else {
                System.out.println("Got null!");
            }

        } catch (IOException er){
            if(sheduler.getClientWindow() != null &&
                        sheduler.getClientWindow().getTitle().equals(" is not connected now!") == false){
                sheduler.getClientWindow().changeTitle(false);
            }
            socket = null;

        } catch (ClassNotFoundException e){
            System.err.println(e);
        } catch (Exception e){
            System.err.println(e);
        }

    }
}
