import Interpreter.*;

class Server{
    public static void main(String[] args){
    
        String fileName = System.getenv("ServerFileName");
        if(fileName == null){
            System.err.println("Environment variable was not set. Use \"dataFile\" instead of it");
            fileName = "dataFile";
        }
        
        Interpreter inter = new Interpreter(fileName);
    
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                inter.saveData();
            }
        });

        
        inter.loadData();
        
        Receiver r = new Receiver(inter);
        
    }
}
