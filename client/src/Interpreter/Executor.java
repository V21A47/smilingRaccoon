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
        if(numberOfCalls == maxNumberOfCalls){
            return null;
        }

        Socket newSocket = null;
        try{
            newSocket = new Socket(host, port);
        } catch (IOException e){
            Thread.sleep(500);
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

            try{
                os = ch.getOutputStream();
                is = ch.getInputStream();
                ch.connect();
            } catch (IOException e){
                System.err.println(e);
            }
        } catch (JSchException e){
            System.err.println(e);
        }

    }

    public void execute(String command, String operand){
        try{
            // Send
            String message = "";

            if(operand == null){
                message = command;
            } else {
                message = new String(command + " " + operand);
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(message);
            oos.flush();

            byte [] serializedObject = bos.toByteArray();
            String amount = String.valueOf(serializedObject.length);

            socket.getOutputStream().write(serializedObject);


            Thread.sleep(25);


            // Receive
            serializedObject = new byte[1024];
            socket.getInputStream().read(serializedObject);
            ByteArrayInputStream bis = new ByteArrayInputStream(serializedObject);
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object obj = ois.readObject();
            String str = (String)obj;
            System.out.println(str);


        } catch (ConnectException|StreamCorruptedException e){
            System.err.println("Server is not available now");
        } catch (Exception e){
            System.err.println(e);
        }

    }
}
