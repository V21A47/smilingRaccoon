import Interpreter.Reader;
import AliveObjects.Human;

import Interpreter.CSVManager;

import AliveObjects.HumanType;
import java.io.IOException;

import com.google.gson.Gson;

public class Client{
    public static void main(String[] args){
        Reader reader = new Reader();
        Gson g = new Gson();
        
        Human a = new Human("Ivan", 32, HumanType.NORMAL, 100, 20);
        
        //CSVManager.writeToFile(a, "file");
        
        System.out.println(g.toJson(a));
        
        try{
            reader.go();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
