package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ServerWindow extends JFrame{

    public ServerWindow(){
        super("ServerWindow");

        JFrame.setDefaultLookAndFeelDecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    class ButtonEventListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
        }
    }

}
