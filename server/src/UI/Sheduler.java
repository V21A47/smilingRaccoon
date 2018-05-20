package UI;

import java.awt.*;

import Interpreter.*;


public class Sheduler{
    private LoginWindow loginWindow = null;
    private ServerWindow serverWindow = null;

    private Interpreter interpreter = null;
    private String fileName;
    private String userName;

    public Sheduler(String fileName, Interpreter interpreter){
        this.fileName = fileName;
        this.interpreter = interpreter;
        if(interpreter == null){
            System.err.println("ERRORRORO");
        }
        startLogin();
    }

    private void startLogin(){
        loginWindow = new LoginWindow(this, fileName);
        loginWindow.setVisible(true);
    }

    public void loginFinished(String username){
        loginWindow.setVisible(false);
        loginWindow.dispose();
        loginWindow = null;
        this.userName = username;
        serverWindow = new ServerWindow(this, interpreter, userName);
        serverWindow.setVisible(true);
    }
}
