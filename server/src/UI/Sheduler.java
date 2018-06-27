package UI;

import java.awt.*;

import Interpreter.*;
import DB.*;
import java.util.*;

public class Sheduler{
    private LoginWindow loginWindow = null;
    private ServerWindow serverWindow = null;

    private CollectionStorage storage;
    private Interpreter interpreter = null;


    private String fileName;
    private String userName;

    public Sheduler(String savingDataFileName, String fileName){
        this.fileName = fileName;
        this.storage = new CollectionStorage("Storage", savingDataFileName);
        this.interpreter = new Interpreter(this, savingDataFileName, storage);
        this.interpreter.loadData();

        startLogin();
    }


    public void updateTree(){
        serverWindow.updateTree();
    }

    public CollectionStorage getStorage(){
        return storage;
    }

    public Interpreter getInterpreter(){
        return interpreter;
    }

    private void startLogin(){
        loginWindow = new LoginWindow(this, fileName);
        loginWindow.setVisible(true);
    }

    private void startServerWindow(){
        serverWindow = new ServerWindow(this, interpreter, userName);
        serverWindow.setVisible(true);
    }

    public void loginFinished(String username){
        loginWindow.setVisible(false);
        loginWindow.dispose();
        loginWindow = null;
        this.userName = username;
        startServerWindow();
    }
}
