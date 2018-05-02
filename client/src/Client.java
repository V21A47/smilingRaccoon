import Interpreter.Reader;
import java.io.IOException;

import java.io.*;
import java.net.*;

public class Client{    
    public static void main(String[] args){
        boolean isShowed = false;
        Reader reader = new Reader();
        
        Socket s = null;
        try{
            s = new Socket("localhost", 3128);
        } catch (UnknownHostException e){
            System.err.println(e);
            return;
        } catch (IOException e){
            System.out.println(e);
        }
        
        while(true){
            try{
                for(int i = 0; i <10; i+=1){
                    s.getOutputStream().write((args[0] + ": " + i*i).getBytes());
                    
                    byte[] buf = new byte[64*1024];
                    int r = s.getInputStream().read(buf);
                    if(r > 0){
                        String text = new String(buf, 0, r);
                        System.out.println(text);
                    }
                    
                    Thread.sleep(Integer.parseInt(args[1])*1000);
                }
                
                
                s.close();
                break;
        
            } catch (ConnectException e){
                if(!isShowed){
                    System.err.println("Server is not available now");
                    isShowed = true;
                }
            } catch (SocketException e){
                if(!isShowed){
                    System.err.println("Server is not available now !!!");
                    isShowed = true;
                }
            } catch (Exception e){
                System.err.println(e);
                System.err.println("Something bad was done");
            }
            
            if(isShowed){
                while(!s.isConnected()){
                    try{
                        s = new Socket("localhost", 3128);
                        
                    }  catch (UnknownHostException e){
                        System.err.println(e);
                        return;
                    } catch (IOException e){
                        System.out.println(e);
                    }
                    try{
                        Thread.sleep(1000);
                    } catch (InterruptedException e){
                        System.err.println(e);
                    }
                }
                isShowed = false;
                continue;
            } else {
                break;
            }
        }
    }
}
