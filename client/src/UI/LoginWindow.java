package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.util.HashMap;
import java.io.IOException;
import java.io.FileNotFoundException;
import DB.*;

import java.util.ResourceBundle;
import java.util.Locale;

public class LoginWindow extends JFrame{
    private JLabel labelUserName = null;
    private JLabel labelPassword = null;
    private JLabel labelErrorWhileEnter = null;
    private JTextField textFieldUserName = null;
    private JPasswordField textFieldPassword = null;
    private JButton buttonEnter = null;
    private JButton buttonReg = null;

    private JMenuBar menuBar = null;
    private JMenu menu = null;
    private JMenuItem menuRussian = null;
    private JMenuItem menuIcelandic = null;
    private JMenuItem menuBulgarian = null;
    private JMenuItem menuEnglish = null;

    private ResourceBundle bundle = null;
    public Locale locale = null;

    private Sheduler sheduler;

    public LoginWindow(Sheduler sheduler){
        super("Client: LoginWindow");

        try{
            bundle = ResourceBundle.getBundle("langProperties/package_en_NZ");
        } catch (Exception e){
            System.err.println(e + "\nNo localization file data was found");
            System.exit(1);
        }
        locale = new Locale("en", "NZ", "UNIX");

        this.setTitle(bundle.getString("windowTitle"));

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.sheduler = sheduler;

        {// MenuBar

            menuBar = new JMenuBar();
            menu = new JMenu(bundle.getString("menuName"));

            menuRussian = new JMenuItem(bundle.getString("RussianLang"));
            menuIcelandic = new JMenuItem(bundle.getString("IcelandicLang"));
            menuBulgarian = new JMenuItem(bundle.getString("BulgarianLang"));
            menuEnglish = new JMenuItem(bundle.getString("EnglishLang"));

            menu.add(menuRussian);
            menu.add(menuIcelandic);
            menu.add(menuBulgarian);
            menu.add(menuEnglish);

            menuBar.add(menu);
            this.setJMenuBar(menuBar);
        }

        {// Initializing objects
            labelUserName = new JLabel("Username");

            labelUserName.setMaximumSize(new Dimension(120, 20));
            labelUserName.setMinimumSize(new Dimension(120, 20));

            labelPassword = new JLabel("Password");
            labelPassword.setMaximumSize(new Dimension(120, 20));
            labelPassword.setMinimumSize(new Dimension(120, 20));

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
            buttonEnter.setMaximumSize(new Dimension(120, 20));
            buttonEnter.setMinimumSize(new Dimension(120, 20));

            buttonReg = new JButton("Registate");
            buttonReg.setMaximumSize(new Dimension(170, 20));
            buttonReg.setMinimumSize(new Dimension(170, 20));
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
                .addComponent(labelErrorWhileEnter)
                .addGap(0, 350, 500)
            );
        }

        buttonEnter.addActionListener(new EnterEventListener());
        buttonReg.addActionListener(new RegEventListener());
        menuRussian.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                locale = new Locale("ru", "RU");
                changeBundle("package_ru_RU");
            }
        });
        menuIcelandic.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                locale = new Locale("is", "IS");
                changeBundle("package_is_IS");
            }
        });
        menuBulgarian.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                locale = new Locale("bg", "BG");
                changeBundle("package_bg_BG");
            }
        });
        menuEnglish.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                locale = new Locale("en", "NZ", "UNIX");
                changeBundle("package_en_NZ");
            }
        });
        pack();
        this.setBounds(100, 100, 1200, 900);
        changeBundle("package_en_NZ");
    }

    private void changeBundle(String newBundleFile){
        try{
            bundle = ResourceBundle.getBundle("langProperties/" + newBundleFile);
        } catch (Exception e){
            System.err.println(e + "\nNo localization file data: " + newBundleFile + " was not found!");
            return;
        }

        this.setTitle(bundle.getString("windowTitle"));
        labelUserName.setText(bundle.getString("Username"));
        labelPassword.setText(bundle.getString("Password"));
        buttonEnter.setText(bundle.getString("Enter"));
        buttonReg.setText(bundle.getString("Registation"));
        labelErrorWhileEnter.setText("");
    }

    private class EnterEventListener implements ActionListener{

        public void actionPerformed(ActionEvent e){
            String name, password;
            name = textFieldUserName.getText();
            password = new String(textFieldPassword.getPassword());

            if(name.isEmpty()){
                labelErrorWhileEnter.setText(bundle.getString("ER_Username"));
                return;
            } else if(password.isEmpty()){
                labelErrorWhileEnter.setText(bundle.getString("ER_Password"));
                return;
            } else {
                labelErrorWhileEnter.setText("");
            }
            name = name.trim();

            String p = sheduler.getDataBase().getUserPassword(name);
            if(p == null){
                labelErrorWhileEnter.setText(bundle.getString("ER_NoUser"));
                return;
            } else if (p.equals(password) == false){
                labelErrorWhileEnter.setText(bundle.getString("ER_IncPassword"));
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
                labelErrorWhileEnter.setText(bundle.getString("ER_Username"));
                return;
            } else if(password.isEmpty()){
                labelErrorWhileEnter.setText(bundle.getString("ER_Password"));
                return;
            } else if(password.length() < 4) {
                labelErrorWhileEnter.setText(bundle.getString("ER_ShortPassword"));
                return;
            } else {
                labelErrorWhileEnter.setText("");
            }
            name = name.trim();

            User.setDB(sheduler.getDataBase());
            if(User.userExist(name)){
                labelErrorWhileEnter.setText(bundle.getString("ER_UserExists"));
                return;
            }
            User a = new User(name, password, false);
            labelErrorWhileEnter.setText(bundle.getString("ER_User") + name + bundle.getString("ER_Success"));
        }
    }
}
