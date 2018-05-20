package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.util.HashMap;
import java.io.IOException;
import java.io.FileNotFoundException;

public class LoginWindow extends JFrame{
    private JLabel labelUserName = null;
    private JLabel labelPassword = null;
    private JLabel labelErrorWhileEnter = null;
    private JTextField textFieldUserName = null;
    private JPasswordField textFieldPassword = null;
    private JButton buttonEnter = null;
    private String fileName;
    private Sheduler sheduler;

    public LoginWindow(Sheduler sheduler, String dataFile){
        super("LoginWindow");

        //JFrame.setDefaultLookAndFeelDecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.sheduler = sheduler;
        fileName = dataFile;


        labelUserName = new JLabel("Username");

        labelUserName.setMaximumSize(new Dimension(80, 20));
        labelUserName.setMinimumSize(new Dimension(80, 20));

        labelPassword = new JLabel("Password");
        labelPassword.setMaximumSize(new Dimension(80, 20));
        labelPassword.setMinimumSize(new Dimension(80, 20));

        labelErrorWhileEnter = new JLabel();
        labelErrorWhileEnter.setMaximumSize(new Dimension(270, 20));
        labelErrorWhileEnter.setMinimumSize(new Dimension(270, 20));

        textFieldUserName = new JTextField();
        textFieldUserName.setMaximumSize(new Dimension(120, 20));
        textFieldUserName.setMinimumSize(new Dimension(120, 20));

        textFieldPassword = new JPasswordField();
        textFieldPassword.setMaximumSize(new Dimension(120, 20));
        textFieldPassword.setMinimumSize(new Dimension(120, 20));

        buttonEnter = new JButton("Enter");
        buttonEnter.setMaximumSize(new Dimension(80, 20));
        buttonEnter.setMinimumSize(new Dimension(80, 20));

        GroupLayout layout = new GroupLayout(getContentPane());
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
                .addComponent(buttonEnter)
                .addGap(0, 250, 900)
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
            .addComponent(buttonEnter)
            .addComponent(labelErrorWhileEnter)
            .addGap(0, 350, 500)
        );

        buttonEnter.addActionListener(new EnterEventListener());
        pack();
        this.setBounds(100, 100, 1200, 900);
    }

    private class EnterEventListener implements ActionListener{
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

            HashMap<String, String> map = getData();
            if(map == null){
                return;
            }

            if(map.containsKey(name)){
                if(map.get(name).equals(password)){
                    labelErrorWhileEnter.setText("Здравствуйте, " + name);
                    sheduler.loginFinished(name);
                } else {
                    labelErrorWhileEnter.setText("Ошибка в пароле.");
                }
            } else {
                labelErrorWhileEnter.setText("Такой пользователь не найден.");
            }
        }
    }
}
