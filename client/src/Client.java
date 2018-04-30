import Interpreter.Interpreter;

import java.io.IOException;

class Client{
    public static void main(String[] args){
        Interpreter i = new Interpreter();
        
        try{
            i.go();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
