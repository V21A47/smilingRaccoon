import Interpreter.Interpreter;

class Server{
    public static void main(String[] args){
        Interpreter i = new Interpreter("testFileName");
        
        i.go();
        
    }
}
