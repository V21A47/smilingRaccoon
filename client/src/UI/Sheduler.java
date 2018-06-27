package UI;

import Interpreter.*;
import DB.*;

public class Sheduler{
    private ClientWindow clientWindow = null;
    private Executor executor;
    private Parser parser;
    private Db dataBase = null;
    private LoginWindow loginWindow = null;
    public String command = null;

    public Sheduler(String host, int port){
        executor = new Executor(this, host, port);

        parser = new Parser(executor);
        dataBase = new Db();

        login();
    }

    public ClientWindow getClientWindow(){
        return clientWindow;
    }

    public Db getDataBase(){
        return this.dataBase;
    }

    public void startClientWindow(){
        clientWindow = new ClientWindow(executor);
        clientWindow.setVisible(true);
    }

    public Parser getParser(){
        return parser;
    }

    public Executor getExecutor(){
        return executor;
    }

    private void login(){
        loginWindow = new LoginWindow(this);
        loginWindow.setVisible(true);
    }

    public void loginFinished(String username){
        loginWindow.setVisible(false);
        loginWindow.dispose();
        loginWindow = null;
        startClientWindow();
    }

}
