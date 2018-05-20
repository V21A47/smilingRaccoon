import Interpreter.*;
import UI.*;


class Server{
    public static void main(String[] args){

        String host = "localhost";
        int port = 8081;

        LoginWindow loginWindow = new LoginWindow("resources/loginData");
        loginWindow.setVisible(true);


        if(args.length == 2){
            host = args[0];
            port = Integer.parseInt(args[1]);
        }

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

        Receiver r = new Receiver(inter, host, port);

    }
}
