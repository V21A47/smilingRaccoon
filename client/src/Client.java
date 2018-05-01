import Interpreter.Reader;
import AliveObjects.Human;

import java.io.IOException;


public class Client{
    public static void main(String[] args){
        Reader reader = new Reader();
        
        try{
            reader.go();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
