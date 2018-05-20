import Interpreter.*;
import UI.*;


class Server{
    public static void main(String[] args){

        String host = "localhost";
        int port = 8081;

        if(args.length == 2){
            host = args[0];
            port = Integer.parseInt(args[1]);
        }

        String fileName = System.getenv("ServerFileName");
        if(fileName == null){
            System.err.println("Environment variable was not set. Use \"dataFile\" instead of it");
            fileName = "dataFile";
        }
        Sheduler shed = new Sheduler(fileName, "resources/loginData");



        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                shed.getInterpreter().saveData();
            }
        });


        Receiver r = new Receiver(shed.getInterpreter(), host, port);

    }
}
