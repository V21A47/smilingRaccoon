package UI;

import Interpreter.*;

public class Sheduler{
    private ClientWindow clientWindow = null;
    private Executor executor;
    private Parser parser;

    public String command = null;

    public Sheduler(String host, int port){
        executor = new Executor(this, host, port);

        parser = new Parser(executor);

        clientWindow = new ClientWindow(executor);
        clientWindow.setVisible(true);
    }

    public ClientWindow getClientWindow(){
        return clientWindow;
    }

    public Parser getParser(){
        return parser;
    }

    public Executor getExecutor(){
        return executor;
    }

}
