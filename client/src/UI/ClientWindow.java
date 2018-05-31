package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.HashSet;

import AliveObjects.*;
import Interpreter.*;
import Places.*;

public class ClientWindow extends JFrame{
    private JButton buttonStart = null;
    private JButton buttonStop = null;

    private JLabel labelName = null;
    private JLabel labelGender = null;
    private JLabel labelType = null;
    private JLabel labelYear1 = null;
    private JLabel labelYear2 = null;
    private JLabel labelMass = null;
    private JLabel labelMass2 = null;
    private JLabel labelX = null;
    private JLabel labelY = null;
    private JLabel labelCondition = null;
    private JLabel labelInfo;


    private JTextField textFieldName;
    private JTextField textFieldYear;
    private JTextField textFieldX;
    private JComboBox textFieldXop;
    private JTextField textFieldY;
    private JComboBox textFieldYop;

    private JSlider slider;

    private JCheckBox checkIsAlive;
    private JCheckBox checkMale;
    private JCheckBox checkFemale;
    private JCheckBox checkNormal;
    private JCheckBox checkPolice;
    private JCheckBox checkBandit;
    private JCheckBox checkArrested;
    private JCheckBox checkImprisoned;
    private JCheckBox checkFree;

    private JPanel panel;
    private HashSet<HumanObject> humanPanelsSet;

    private Executor executor;
    private ActionManager actManager;



    public void changeTitle(String text){
        setTitle("Client window: " + text);
    }


    public void update(){
        HashSet<Human> set = executor.getSet();

        panel.removeAll();

        humanPanelsSet.clear();

        for(Human human : set){
            HumanObject p = new HumanObject(actManager, human);
            humanPanelsSet.add(p);
            panel.add(p);
        }
        panel.repaint();
        panel.revalidate();
        //revalidate();
    }

    public HashSet<HumanObject> getHumansInAct(){
        HashSet<HumanObject> set = new HashSet<>();
        set.clear();

        String name = textFieldName.getText().trim();

        labelInfo.setText("");

        int year = 0;
        try{
            year = Integer.parseInt(textFieldYear.getText());
        } catch (NumberFormatException error){
            labelInfo.setText("год рождения: [0:2018]");
            return set;
        }

        if(year < 0 || year > 2018){
            return set;
        }

        int x = 0;
        try{
            x = Integer.parseInt(textFieldX.getText());
            if(x < SearchableThing.minX || x > SearchableThing.maxX){
                labelInfo.setText("x может быть [" + SearchableThing.minX + ", " + SearchableThing.maxX + "]");
                return set;
            }
        } catch (NumberFormatException error){
            labelInfo.setText("x может быть [" + SearchableThing.minX + ", " + SearchableThing.maxX + "]");
            return set;
        }

        int y = 0;
        try{
            y = Integer.parseInt(textFieldY.getText());
            if(y < SearchableThing.minY || y > SearchableThing.maxY){
                labelInfo.setText("y может быть [" + SearchableThing.minY + ", " + SearchableThing.maxY + "]");
                return set;
            }
        } catch (NumberFormatException error){
            labelInfo.setText("y может быть [" + SearchableThing.minY + ", " + SearchableThing.maxY + "]");
            return set;
        }

        if((checkNormal.isSelected() || checkIsAlive.isSelected() || checkMale.isSelected() || checkFemale.isSelected() ||
            checkPolice.isSelected() || checkBandit.isSelected() || checkArrested.isSelected() || checkImprisoned.isSelected() ||
            checkFree.isSelected()) == false){

            return set;
        }

        String xOp = textFieldXop.getSelectedItem().toString();
        String yOp = textFieldYop.getSelectedItem().toString();

        int mass = slider.getValue();

        for(HumanObject obj : humanPanelsSet){


            //System.out.println(obj.getHuman().getX());
            boolean exp = (obj.getHuman().getSizeValue() <= mass &&  // mass
                ( (year == 0 || obj.getHuman().getYearOfBirth() == year))  &&    //name
                ( (name.equals("") || obj.getHuman().getName().equals(name)) ) &&  // year of birth
                (
                    ( xOp.equals("=") && ( x == obj.getHuman().getX()) ) ||
                    ( xOp.equals("<") && ( obj.getHuman().getX() < x ) ||
                    ( xOp.equals(">") && ( obj.getHuman().getX() > x) )
                ) && // x
                (
                    ( yOp.equals("=") && ( y == obj.getHuman().getY()) ) ||
                    ( yOp.equals("<") && ( y > obj.getHuman().getY()) ) ||
                    ( yOp.equals(">") && ( y < obj.getHuman().getY()) )
                ) && // y
                (
                    ( checkIsAlive.isSelected() == obj.getHuman().isAlive() ) &&
                    (
                        checkMale.isSelected() && obj.getHuman().getGender().equals("male")    ||
                        checkFemale.isSelected() && obj.getHuman().getGender().equals("female")
                    ) &&
                    (
                        checkNormal.isSelected() && obj.getHuman().getType() == HumanType.NORMAL ||
                        checkPolice.isSelected() && obj.getHuman().getType() == HumanType.POLICE ||
                        checkBandit.isSelected() && obj.getHuman().getType() == HumanType.BANDIT
                    ) &&
                    (
                        checkArrested.isSelected() && obj.getHuman().getConditionInCommunity().getState() == StateOfFreedom.ARRESTED ||
                        checkImprisoned.isSelected() && obj.getHuman().getConditionInCommunity().getState() == StateOfFreedom.IMPRISONED ||
                        checkFree.isSelected() && obj.getHuman().getConditionInCommunity().getState() == StateOfFreedom.FREE
                    )
                )
                ));
                //System.out.println(obj.getHuman() + "   " + exp);
            if(exp){
                obj.shouldDisappear(true);
                set.add(obj);
            }

        }

        System.out.println(set.size());
        return set;
    }

    public HashSet<HumanObject> getSet(){
        return humanPanelsSet;
    }

    public ClientWindow(Executor executor){
        super("Client window");
        this.executor = executor;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //update();


        { //initializing
            humanPanelsSet = new HashSet();

            buttonStart = new JButton("Старт");
            buttonStart.setMaximumSize(new Dimension(80, 20));
            buttonStart.setMinimumSize(new Dimension(80, 20));

            buttonStop = new JButton("Стоп");
            buttonStop.setMaximumSize(new Dimension(80, 20));
            buttonStop.setMinimumSize(new Dimension(80, 20));

            slider = new JSlider(0, 180, 180);
            slider.setMaximumSize(new Dimension(200, 20));
            slider.setMinimumSize(new Dimension(200, 20));

            labelName = new JLabel("Имя");
            labelName.setMaximumSize(new Dimension(50, 20));
            labelName.setMinimumSize(new Dimension(50, 20));

            labelInfo = new JLabel("Подсказки");
            labelInfo.setMaximumSize(new Dimension(200, 20));
            labelInfo.setMinimumSize(new Dimension(200, 20));

            labelGender = new JLabel("Пол");
            labelGender.setMaximumSize(new Dimension(50, 20));
            labelGender.setMinimumSize(new Dimension(50, 20));

            labelType = new JLabel("Тип");
            labelType.setMaximumSize(new Dimension(50, 20));
            labelType.setMinimumSize(new Dimension(50, 20));

            labelYear1 = new JLabel("Год");
            labelYear1.setMaximumSize(new Dimension(50, 20));
            labelYear1.setMinimumSize(new Dimension(50, 20));
            labelYear2 = new JLabel("рожд.");
            labelYear2.setMaximumSize(new Dimension(50, 20));
            labelYear2.setMinimumSize(new Dimension(50, 20));

            labelMass = new JLabel("Масса <=");
            labelMass.setMaximumSize(new Dimension(70, 20));
            labelMass.setMinimumSize(new Dimension(70, 20));

            labelMass2 = new JLabel("180");
            labelMass2.setMaximumSize(new Dimension(50, 20));
            labelMass2.setMinimumSize(new Dimension(50, 20));

            labelX = new JLabel("Х");
            labelX.setMaximumSize(new Dimension(50, 20));
            labelX.setMinimumSize(new Dimension(50, 20));

            labelY = new JLabel("У");
            labelY.setMaximumSize(new Dimension(50, 20));
            labelY.setMinimumSize(new Dimension(50, 20));

            labelCondition = new JLabel("Состояние");
            labelCondition.setMaximumSize(new Dimension(80, 20));
            labelCondition.setMinimumSize(new Dimension(80, 20));

            textFieldName = new JTextField();
            textFieldName.setMaximumSize(new Dimension(150, 20));
            textFieldName.setMinimumSize(new Dimension(150, 20));

            textFieldYear = new JTextField();
            textFieldYear.setMaximumSize(new Dimension(150, 20));
            textFieldYear.setMinimumSize(new Dimension(150, 20));

            textFieldX = new JTextField();
            textFieldX.setMaximumSize(new Dimension(100, 20));
            textFieldX.setMinimumSize(new Dimension(100, 20));

            String [] values = {"=", "<", ">"};
            textFieldXop = new JComboBox(values);
            textFieldXop.setMaximumSize(new Dimension(50, 20));
            textFieldXop.setMinimumSize(new Dimension(50, 20));

            textFieldY = new JTextField();
            textFieldY.setMaximumSize(new Dimension(100, 20));
            textFieldY.setMinimumSize(new Dimension(100, 20));

            textFieldYop = new JComboBox(values);
            textFieldYop.setMaximumSize(new Dimension(50, 20));
            textFieldYop.setMinimumSize(new Dimension(50, 20));


            checkIsAlive = new JCheckBox("Живой");
            checkIsAlive.setMaximumSize(new Dimension(150, 20));
            checkIsAlive.setMinimumSize(new Dimension(150, 20));

            checkMale = new JCheckBox("М");
            checkMale.setMaximumSize(new Dimension(150, 20));
            checkMale.setMinimumSize(new Dimension(150, 20));

            checkFemale = new JCheckBox("Ж");
            checkFemale.setMaximumSize(new Dimension(150, 20));
            checkFemale.setMinimumSize(new Dimension(150, 20));

            checkNormal = new JCheckBox("Обычный житель");
            checkNormal.setMaximumSize(new Dimension(150, 20));
            checkNormal.setMinimumSize(new Dimension(150, 20));

            checkPolice = new JCheckBox("Полицейский");
            checkPolice.setMaximumSize(new Dimension(150, 20));
            checkPolice.setMinimumSize(new Dimension(150, 20));

            checkBandit = new JCheckBox("Бандит");
            checkBandit.setMaximumSize(new Dimension(150, 20));
            checkBandit.setMinimumSize(new Dimension(150, 20));

            checkArrested = new JCheckBox("Аррестован");
            checkArrested.setMaximumSize(new Dimension(150, 20));
            checkArrested.setMinimumSize(new Dimension(150, 20));

            checkImprisoned = new JCheckBox("Заключен");
            checkImprisoned.setMaximumSize(new Dimension(150, 20));
            checkImprisoned.setMinimumSize(new Dimension(150, 20));

            checkFree = new JCheckBox("Свободен");
            checkFree.setMaximumSize(new Dimension(150, 20));
            checkFree.setMinimumSize(new Dimension(150, 20));

            panel = new JPanel();
            //panel.setBackground(Color.lightGray);
            panel.setMaximumSize(new Dimension(1900, 1200));
            panel.setMinimumSize(new Dimension(600, 600));
        }

        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        {
            layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(panel)
                .addGap(5)
                .addGroup(layout.createParallelGroup()
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelName)
                        .addComponent(textFieldName)
                    )
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelGender)
                        .addGroup(layout.createParallelGroup()
                            .addComponent(checkMale)
                            .addComponent(checkFemale)
                        )
                    )
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelType)
                        .addGroup(layout.createParallelGroup()
                            .addComponent(checkNormal)
                            .addComponent(checkPolice)
                            .addComponent(checkBandit)
                        )
                    )
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup()
                            .addComponent(labelYear1)
                            .addComponent(labelYear2)
                        )
                        .addComponent(textFieldYear)
                    )
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30)
                        .addComponent(labelMass)
                        .addComponent(labelMass2)
                    )
                    .addComponent(slider)
                    .addGap(30)
                    .addComponent(checkIsAlive)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelX)
                        .addComponent(textFieldXop)
                        .addComponent(textFieldX)
                    )
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelY)
                        .addComponent(textFieldYop)
                        .addComponent(textFieldY)
                    )
                    .addComponent(labelCondition)

                    .addComponent(checkArrested)
                    .addComponent(checkImprisoned)
                    .addComponent(checkFree)

                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonStart)
                        .addGap(35)
                        .addComponent(buttonStop)
                    )
                    .addComponent(labelInfo)

                )
                .addGap(5)
            );

            layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(5)
                .addGroup(layout.createParallelGroup()
                    .addComponent(panel)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup()
                            .addComponent(labelName)
                            .addComponent(textFieldName)
                        )
                        .addGroup(layout.createParallelGroup()
                            .addComponent(labelGender)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(checkMale)
                                .addComponent(checkFemale)
                            )
                        )
                        .addGroup(layout.createParallelGroup()
                            .addComponent(labelType)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(checkNormal)
                                .addComponent(checkPolice)
                                .addComponent(checkBandit)
                            )
                        )
                        .addGroup(layout.createParallelGroup()
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelYear1)
                                .addComponent(labelYear2)
                            )
                            .addGroup(layout.createSequentialGroup()
                                .addGap(5)
                                .addComponent(textFieldYear)
                            )
                        )
                        .addGroup(layout.createParallelGroup()
                            .addComponent(labelMass)
                            .addComponent(labelMass2)
                        )
                        .addComponent(slider)
                        .addComponent(checkIsAlive)
                        .addGroup(layout.createParallelGroup()
                            .addComponent(labelX)
                            .addComponent(textFieldXop)
                            .addComponent(textFieldX)
                        )
                        .addGroup(layout.createParallelGroup()
                            .addComponent(labelY)
                            .addComponent(textFieldYop)
                            .addComponent(textFieldY)
                        )

                        .addComponent(labelCondition)
                        .addComponent(checkArrested)
                        .addComponent(checkImprisoned)
                        .addComponent(checkFree)
                        .addGap(0, 75, 100)
                        .addGroup(layout.createParallelGroup()
                            .addComponent(buttonStart)
                            .addComponent(buttonStop)
                        )
                        .addComponent(labelInfo)
                        .addGap(0, 5, 500)
                    )
                )
            .addGap(5)
            );
        }

        {
            buttonStart.addActionListener(new ButtonStartEventListener());
            buttonStop.addActionListener(new ButtonStopEventListener());

            slider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    // меняем надпись
                    int value = ((JSlider)e.getSource()).getValue();
                    labelMass2.setText(String.valueOf(value));
                }
            });

        }
        pack();
        setBounds(100, 100, 1000, 800);

        update();
        actManager = new ActionManager(this);
        actManager.start();
    }

    class ButtonStartEventListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            actManager.shouldWork = true;
        }
    }

    class ButtonStopEventListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            actManager.stopAct();
            actManager.shouldWork = false;

            update();
        }
    }
}
