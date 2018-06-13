package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.util.HashMap;
import java.io.IOException;
import java.io.FileNotFoundException;
import DB.*;

public class LoginWindow extends JFrame{
    private JLabel labelUserName = null;
    private JLabel labelPassword = null;
    private JLabel labelErrorWhileEnter = null;
    private JTextField textFieldUserName = null;
    private JPasswordField textFieldPassword = null;
    private JButton buttonEnter = null;
    private JButton buttonReg = null;
    private JButton buttonChange = null;
    private String fileName;
    private Sheduler sheduler;

    public LoginWindow(Sheduler sheduler, String dataFile){
        super("LoginWindow");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.sheduler = sheduler;
        fileName = dataFile;

        {// Initializing objects
            labelUserName = new JLabel("Username");

            labelUserName.setMaximumSize(new Dimension(80, 20));
            labelUserName.setMinimumSize(new Dimension(80, 20));

            labelPassword = new JLabel("Password");
            labelPassword.setMaximumSize(new Dimension(80, 20));
            labelPassword.setMinimumSize(new Dimension(80, 20));

            labelErrorWhileEnter = new JLabel();
            labelErrorWhileEnter.setMaximumSize(new Dimension(350, 20));
            labelErrorWhileEnter.setMinimumSize(new Dimension(350, 20));

            textFieldUserName = new JTextField();
            textFieldUserName.setMaximumSize(new Dimension(120, 20));
            textFieldUserName.setMinimumSize(new Dimension(120, 20));

            textFieldPassword = new JPasswordField();
            textFieldPassword.setMaximumSize(new Dimension(120, 20));
            textFieldPassword.setMinimumSize(new Dimension(120, 20));

            buttonEnter = new JButton("Enter");
            buttonEnter.setMaximumSize(new Dimension(80, 20));
            buttonEnter.setMinimumSize(new Dimension(80, 20));

            buttonReg = new JButton("Registate");
            buttonReg.setMaximumSize(new Dimension(120, 20));
            buttonReg.setMinimumSize(new Dimension(120, 20));

            buttonChange = new JButton("Change password");
            buttonChange.setMaximumSize(new Dimension(160, 20));
            buttonChange.setMinimumSize(new Dimension(160, 20));
        }

        GroupLayout layout = new GroupLayout(getContentPane());
        {// Layout&beeeee
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 250, 900)
                    .addComponent(labelUserName)
                    .addGap(10)
                    .addComponent(textFieldUserName)
                    .addGap(0, 250, 900)
                )
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 250, 900)
                    .addComponent(labelPassword)
                    .addGap(10)
                    .addComponent(textFieldPassword)
                    .addGap(0, 250, 900)
                )
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 380, 900)
                    .addComponent(buttonReg)
                    .addGap(30)
                    .addComponent(buttonEnter)
                    .addGap(0, 380, 900)
                )
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 380, 900)
                    .addComponent(buttonChange)
                    .addGap(0, 450, 900)
                )
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 250, 900)
                    .addComponent(labelErrorWhileEnter)
                    .addGap(0, 250, 900)
                )
            );

            layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(0, 350, 500)
                .addGroup(layout.createParallelGroup()
                    .addComponent(labelUserName)
                    .addComponent(textFieldUserName)
                )
                .addGroup(layout.createParallelGroup()
                    .addComponent(labelPassword)
                    .addComponent(textFieldPassword)
                )
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                    .addComponent(buttonEnter)
                    .addComponent(buttonReg)
                )
                .addGap(10)
                .addComponent(buttonChange)
                .addComponent(labelErrorWhileEnter)
                .addGap(0, 350, 500)
            );
        }

        buttonEnter.addActionListener(new EnterEventListener());
        buttonReg.addActionListener(new RegEventListener());
        buttonChange.addActionListener(new ChangeEventListener());

        pack();
        this.setBounds(100, 100, 1200, 900);
    }

    private class EnterEventListener implements ActionListener{

        /*
        HashMap<String, String> getData(){
            HashMap<String, String> map = new HashMap<>();
            try{
                FileReader fileReader = new FileReader(fileName);
                StringBuilder tempName = new StringBuilder();
                StringBuilder tempPassword = new StringBuilder();

                boolean readingName = true;
                int ci = fileReader.read();

                while(ci != -1){
                    char c = (char)ci;
                    switch(c){
                        // Starts reading password
                        case '\t':
                            if(readingName){
                                readingName = false;
                            }
                            break;
                        // A line was read
                        case '\n':
                            // If password wasn't read -> error
                            if(!readingName){
                                readingName = true;
                                map.put(tempName.toString(), tempPassword.toString());
                                tempName = new StringBuilder();
                                tempPassword = new StringBuilder();
                            } else {
                                System.err.println("Error while reading user information file");
                                return null;
                            }
                            break;
                        // Add new char
                        default:
                            if(readingName){
                                tempName.append(c);
                            } else {
                                tempPassword.append(c);
                            }
                            break;
                    }
                    ci = fileReader.read();
                }
                if(map.isEmpty()){
                    System.err.println("No data aboud users was found");
                    labelErrorWhileEnter.setText("Данные о пользователях не найдены");
                    return null;
                }
            } catch (FileNotFoundException error){
                System.err.println("File with user information was not found");
                labelErrorWhileEnter.setText("Данные о пользователях не найдены");
                return null;
            } catch (IOException error){
                System.err.println(error);
                return null;
            }
            return map;
        }
        */

        public void actionPerformed(ActionEvent e){
            String name, password;
            name = textFieldUserName.getText();
            password = new String(textFieldPassword.getPassword());

            if(name.isEmpty()){
                labelErrorWhileEnter.setText("Введите имя пользователя");
                return;
            } else if(password.isEmpty()){
                labelErrorWhileEnter.setText("Введите пароль");
                return;
            } else {
                labelErrorWhileEnter.setText("");
            }
            name = name.trim();

            String p = sheduler.getDataBase().getUserPassword(name);
            if(p == null){
                labelErrorWhileEnter.setText("Такого пользователя нет");
                return;
            } else if (p.equals(password) == false){
                labelErrorWhileEnter.setText("Пароль введен с ошибкой");
                return;
            }
            sheduler.loginFinished(name);
        }
    }

    private class RegEventListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String name, password;
            name = textFieldUserName.getText();
            password = new String(textFieldPassword.getPassword());

            if(name.isEmpty()){
                labelErrorWhileEnter.setText("Введите имя пользователя");
                return;
            } else if(password.isEmpty()){
                labelErrorWhileEnter.setText("Введите пароль");
                return;
            } else if(password.length() < 4) {
                labelErrorWhileEnter.setText("Пароль должен содержать больше 4 символов");
                return;
            } else {
                labelErrorWhileEnter.setText("");
            }
            name = name.trim();

            User.setDB(sheduler.getDataBase());
            if(User.userExist(name)){
                labelErrorWhileEnter.setText("Такой пользователь уже существует");
                return;
            }
            User a = new User(name, password);
            labelErrorWhileEnter.setText("Пользователь " + name + " успешно зарегистрирован");
        }
    }

    private class ChangeEventListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String name, password;
            name = textFieldUserName.getText();
            password = new String(textFieldPassword.getPassword());

            if(name.isEmpty()){
                labelErrorWhileEnter.setText("Введите имя пользователя");
                return;
            } else if(password.isEmpty()){
                labelErrorWhileEnter.setText("Введите пароль");
                return;
            } else if(password.length() < 4) {
                labelErrorWhileEnter.setText("Пароль должен содержать больше 4 символов");
                return;
            } else {
                labelErrorWhileEnter.setText("");
            }
            name = name.trim();

            User.setDB(sheduler.getDataBase());
            if(!User.userExist(name)){
                labelErrorWhileEnter.setText("Пользователь должен существовать");
                return;
            }

            User a = User.getUser(name);
            a.changePassword(password);
            labelErrorWhileEnter.setText("Пароль успешно изменен");

        }
    }
}
