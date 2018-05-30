import Interpreter.*;
import UI.*;

import java.io.IOException;

public class Client{

    public static void main(String[] args){
        String host = "localhost";
        int port = 8081;

        if(args.length == 2){
            host = args[0];
            port = Integer.parseInt(args[1]);
        }

        Sheduler sheduler = new Sheduler(host, port);
    }


}
