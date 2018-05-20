package UI;

import Interpreter.*;
import AliveObjects.*;


import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class ServerWindow extends JFrame{
    private Sheduler sheduler = null;
    private Interpreter interpreter = null;

    private JButton buttonAdd = null;
    private JButton buttonRemove = null;
    private JButton buttonEdit = null;
    private JTree tree = null;
    private JScrollPane treeScroll = null;
    private JLabel labelInf = null;

    private JMenuBar menuBar = null;
    private JMenu menu = null;
    private JMenuItem menuItemSave = null;
    private JMenuItem menuItemLoad = null;
    private JMenuItem menuItemImport = null;
    private JMenuItem menuItemClear = null;

    private HashMap<DefaultMutableTreeNode, Human> mapOfNodes;

    public ServerWindow(Sheduler sheduler, Interpreter interpreter, String userName){
        super("ServerWindow: " + userName + "  :  " + interpreter.getFileName());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.sheduler = sheduler;
        this.interpreter = interpreter;
        this.mapOfNodes = new HashMap<DefaultMutableTreeNode, Human>();

        {// MenuBar

            menuBar = new JMenuBar();
            menu = new JMenu("Коллекция");

            menuItemSave = new JMenuItem("Сохранить");
            menuItemLoad = new JMenuItem("Загрузить");
            menuItemImport = new JMenuItem("Импортировать");
            menuItemClear = new JMenuItem("Очистить");

            menu.add(menuItemSave);
            menu.add(menuItemLoad);
            menu.add(menuItemImport);
            menu.addSeparator();
            menu.add(menuItemClear);

            menuBar.add(menu);
            this.setJMenuBar(menuBar);
        }

        {// Buttons & Tree
            buttonAdd = new JButton("Добавить");
            buttonRemove = new JButton("Удалить");
            buttonEdit = new JButton("Изменить");

            buttonAdd.setMaximumSize(new Dimension(120, 20));
            buttonAdd.setMinimumSize(new Dimension(120, 20));

            buttonRemove.setMaximumSize(new Dimension(120, 20));
            buttonRemove.setMinimumSize(new Dimension(120, 20));

            buttonEdit.setMaximumSize(new Dimension(120, 20));
            buttonEdit.setMinimumSize(new Dimension(120, 20));

            labelInf = new JLabel();
            labelInf.setMaximumSize(new Dimension(500, 20));
            labelInf.setMinimumSize(new Dimension(250, 20));

            DefaultMutableTreeNode root = new DefaultMutableTreeNode("Collection");

            tree = new JTree(root);

            updateTree();

            treeScroll = new JScrollPane(tree,
                                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);



            tree.setMinimumSize(new Dimension(800, 300));
            tree.setMaximumSize(new Dimension(1900, 1200));
        }

        {// Layout&bee
            GroupLayout layout = new GroupLayout(getContentPane());
            getContentPane().setLayout(layout);

            layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 15, 15)
                    .addComponent(buttonAdd)
                    .addGap(10)
                    .addComponent(buttonEdit)
                    .addGap(10)
                    .addComponent(buttonRemove)
                    .addGap(80)
                    .addComponent(labelInf)
                    .addGap(0, 250, 1200)
                )
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 15, 15)
                    .addComponent(tree)
                    .addGap(0, 15, 15)
                )
            );

            layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(0, 15, 15)
                .addGroup(layout.createParallelGroup()
                    .addComponent(buttonAdd)
                    .addComponent(buttonEdit)
                    .addComponent(buttonRemove)
                    .addComponent(labelInf)
                )
                .addGap(0, 15, 15)
                .addComponent(tree)
                .addGap(0, 15, 15)
            );
        }


        {// Events
            buttonRemove.addActionListener(new ButtonRemoveEventListener());
            menuItemSave.addActionListener(new MenuItemSaveEventListener());
            menuItemLoad.addActionListener(new MenuItemLoadEventListener());
            menuItemImport.addActionListener(new MenuItemImportEventListener());
            menuItemClear.addActionListener(new MenuItemClearEventListener());
        }

        pack();
        this.setBounds(100, 100, 1200, 900);
    }

    private DefaultMutableTreeNode getNodeOfObject(Object obj){
        Human human = (Human)obj;
        return new DefaultMutableTreeNode( human.getType() + "  " + human.getName() +
                                            human.getYearOfBirth() + "  " + human.getX() +
                                            "   " + human.getY());
    }

    private void addNode(DefaultMutableTreeNode node){
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
        root.add(node);
        model.reload();
    }

    public void updateTree(){
        Object[] objects = sheduler.getStorage().getArrayOfHumans();
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
        root.removeAllChildren();
        mapOfNodes.clear();

        for(Object h : objects){
            System.out.println( ((Human)h) );
            DefaultMutableTreeNode node = getNodeOfObject(h);

            mapOfNodes.put(node, (Human)h);

            addNode(node);
        }

        model.reload();
        System.out.println(root.getChildCount() + "    " +  mapOfNodes.size());
    }

    class ButtonRemoveEventListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            labelInf.setText("");
            try{
                DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
                DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
                TreePath[] paths = tree.getSelectionPaths();

                if(paths == null){
                    labelInf.setText("Нужно выбрать элементы, чтобы удалить их.");
                    return;
                }

                for(TreePath path : paths){
                    try{
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
                        Human h = mapOfNodes.get(node);
                        mapOfNodes.remove(node);
                        sheduler.getStorage().remove(h);
                        root.remove(node);
                    } catch (NullPointerException error){
                        return;
                    }
                }
                updateTree();

            } catch (java.lang.IllegalArgumentException error){
                System.err.println(error);
                return;
            } catch (NullPointerException error){
                System.err.println(error);
                return;
            }
        }
    }

    class MenuItemSaveEventListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            labelInf.setText(sheduler.getStorage().save());
            updateTree();
        }
    }

    class MenuItemLoadEventListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            labelInf.setText(sheduler.getStorage().load());
            updateTree();
        }
    }

    class MenuItemClearEventListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            labelInf.setText(sheduler.getStorage().clear());
            updateTree();
        }
    }

    class MenuItemImportEventListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            labelInf.setText("");
            String fileName = JOptionPane.showInputDialog("Введите имя файла");

            labelInf.setText(sheduler.getStorage().importFromFile(fileName));
            updateTree();
        }
    }
}
