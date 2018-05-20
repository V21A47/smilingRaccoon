package UI;

import Interpreter.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ServerWindow extends JFrame{
    private Sheduler sheduler = null;
    private Interpreter interpreter = null;

    private JButton buttonAdd = null;
    private JButton buttonRemove = null;
    private JButton buttonEdit = null;
    private JTree treeElements = null;

    private JMenuBar menuBar = null;
    private JMenu submenu = null;
    private JMenuItem menuItemSave = null;
    private JMenuItem menuItemLoad = null;
    private JMenuItem menuItemImport = null;
    private JMenuItem menuItemClear = null;

    public ServerWindow(Sheduler sheduler, Interpreter interpreter, String userName){
        super("ServerWindow: " + userName + "  :  " + interpreter.getFileName());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.sheduler = sheduler;
        this.interpreter = interpreter;

        this.setBounds(100, 100, 1200, 900);
    }

    class ButtonEventListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
        }
    }

}
