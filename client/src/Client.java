import Interpreter.Reader;
import java.io.IOException;

public class Client{    
    public static void main(String[] args){
        Reader reader = new Reader();
        try{
            reader.go();
        } catch (IOException e){
            System.err.println(e);
        }
    }
}
