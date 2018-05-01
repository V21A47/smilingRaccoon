import Interpreter.Interpreter;
import AliveObjects.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.Iterator;

class Server{
    public static void main(String[] args){
        Interpreter i = new Interpreter("t");
        
        i.go();
    
    }
}
