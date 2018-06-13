package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.HashSet;

import java.util.ResourceBundle;
import java.util.Locale;

import AliveObjects.*;
import Interpreter.*;
import Places.*;

public class ClientWindow extends JFrame{
    private ResourceBundle bundle = null;
    public Locale locale = null;

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

    private JMenuBar menuBar = null;
    private JMenu menu = null;
    private JMenuItem menuRussian = null;
    private JMenuItem menuIcelandic = null;
    private JMenuItem menuBulgarian = null;
    private JMenuItem menuEnglish = null;

    private JPanel panel;
    private HashSet<HumanObject> humanPanelsSet;

    private Executor executor;
    private ActionManager actManager;



    public void changeTitle(boolean v){
        if(v){
            setTitle(bundle.getString("windowTitle") + bundle.getString("Connection"));
        } else {
            setTitle(bundle.getString("windowTitle"));
        }
    }


    public void update(){
        HashSet<Human> set = executor.getSet();

        panel.removeAll();

        humanPanelsSet.clear();

        for(Human human : set){
            HumanObject p = new HumanObject(actManager, human, bundle);
            humanPanelsSet.add(p);
            panel.add(p);
        }
        panel.repaint();
        panel.revalidate();
    }

    public HashSet<HumanObject> getHumansInAct(){
        HashSet<HumanObject> set = new HashSet<>();
        set.clear();

        String name = textFieldName.getText().trim();

        labelInfo.setText("");

        Integer year = 0;
        try{
            if(textFieldYear.getText().isEmpty()){
                year = null;
            } else {
                year = Integer.parseInt(textFieldYear.getText());
            }
        } catch (NumberFormatException error){
            labelInfo.setText( bundle.getString("yearOfBirth") + ": [0:2018]" );
            return set;
        }

        if(year != null && (year < 0 || year > 2018) ){
            return set;
        }

        Integer x = 0;
        try{
            if(textFieldX.getText().isEmpty()){
                x = null;
            } else {
                x = Integer.parseInt(textFieldX.getText());
            }

            if(x != null && (x < SearchableThing.minX || x > SearchableThing.maxX)){
                labelInfo.setText(bundle.getString("possibleXValues") + " [" + SearchableThing.minX + ", " + SearchableThing.maxX + "]");
                return set;
            }
        } catch (NumberFormatException error){
            labelInfo.setText(bundle.getString("possibleXValues") + " [" + SearchableThing.minX + ", " + SearchableThing.maxX + "]");
            return set;
        }

        Integer y = 0;
        try{
            if(textFieldY.getText().isEmpty()){
                y = null;
            } else {
                y = Integer.parseInt(textFieldY.getText());
            }

            if(y != null && (y < SearchableThing.minY || y > SearchableThing.maxY) ){
                labelInfo.setText(bundle.getString("possibleYValues") + " [" + SearchableThing.minY + ", " + SearchableThing.maxY + "]");
                return set;
            }
        } catch (NumberFormatException error){
            labelInfo.setText(bundle.getString("possibleYValues") + " [" +  SearchableThing.minY + ", " + SearchableThing.maxY + "]");
            return set;
        }

        String xOp = textFieldXop.getSelectedItem().toString();
        String yOp = textFieldYop.getSelectedItem().toString();

        int mass = slider.getValue();

        for(HumanObject obj : humanPanelsSet){
            Human human = obj.getHuman();

            if( !name.equals("") && !name.equals(human.getName()) ){
                continue;
            }

            if( year != null && human.getYearOfBirth() != year ){
                continue;
            }

            if( x != null ){
                if( xOp.equals("=") && human.getX() != x ||
                    xOp.equals("<") && human.getX() >= x ||
                    xOp.equals(">") && human.getX() <= x){
                    continue;
                }
            }

            if( y != null ){
                if( yOp.equals("=") && human.getX() != y ||
                    yOp.equals("<") && human.getX() >= y ||
                    yOp.equals(">") && human.getX() <= y){
                    continue;
                }
            }

            if( !(checkIsAlive.isSelected() && human.isAlive() || !checkIsAlive.isSelected() && !human.isAlive()) ){
                continue;
            }

            if( ( checkMale.isSelected() || checkFemale.isSelected() ) ){
                if( !(checkMale.isSelected() && checkMale.isSelected()) ){
                    if((checkMale.isSelected() && !human.getGender().equals("male")) ||
                        (checkFemale.isSelected() && !human.getGender().equals("female")) ){
                        continue;
                    }
                }
            }

            if( ( checkNormal.isSelected() || checkPolice.isSelected() || checkBandit.isSelected()) ){
                if( !(checkNormal.isSelected() && checkPolice.isSelected() && checkBandit.isSelected() ) ){
                    if((checkNormal.isSelected() && human.getType() != HumanType.NORMAL) ||
                        (checkPolice.isSelected() && human.getType() != HumanType.POLICE) ||
                        (checkBandit.isSelected() && human.getType() != HumanType.BANDIT)){
                        continue;
                    }
                }
            }

            if( ( checkArrested.isSelected() || checkImprisoned.isSelected() || checkFree.isSelected()) ){
                if( !(checkArrested.isSelected() && checkImprisoned.isSelected() && checkFree.isSelected() ) ){
                    if((checkArrested.isSelected() && human.getConditionInCommunity().getState() != StateOfFreedom.ARRESTED) ||
                        (checkImprisoned.isSelected() && human.getConditionInCommunity().getState() != StateOfFreedom.IMPRISONED) ||
                        (checkFree.isSelected() && human.getConditionInCommunity().getState() != StateOfFreedom.FREE)){
                        continue;
                    }
                }
            }

            if( human.getSizeValue() > slider.getValue() ){
                continue;
            }
            obj.shouldDisappear(true);
            set.add(obj);

        }

        return set;
    }

    public HashSet<HumanObject> getSet(){
        return humanPanelsSet;
    }

    public ClientWindow(Executor executor){
        super();

        try{
            bundle = ResourceBundle.getBundle("langProperties/package_en_NZ");
        } catch (Exception e){
            System.err.println(e + "\nNo localization file data was found");
            System.exit(1);
        }
        locale = new Locale("en", "NZ", "UNIX");

        this.setTitle(bundle.getString("windowTitle"));
        this.executor = executor;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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


        { //initializing
            humanPanelsSet = new HashSet();

            buttonStart = new JButton(bundle.getString("startButtonLabel"));
            buttonStart.setMaximumSize(new Dimension(120, 20));
            buttonStart.setMinimumSize(new Dimension(120, 20));

            buttonStop = new JButton(bundle.getString("stopButtonLabel"));
            buttonStop.setMaximumSize(new Dimension(120, 20));
            buttonStop.setMinimumSize(new Dimension(120, 20));

            slider = new JSlider(0, 180, 180);
            slider.setMaximumSize(new Dimension(260, 20));
            slider.setMinimumSize(new Dimension(260, 20));

            labelName = new JLabel(bundle.getString("nameLabel"));
            labelName.setMaximumSize(new Dimension(90, 20));
            labelName.setMinimumSize(new Dimension(90, 20));

            labelInfo = new JLabel();
            labelInfo.setMaximumSize(new Dimension(200, 20));
            labelInfo.setMinimumSize(new Dimension(200, 20));

            labelGender = new JLabel(bundle.getString("genderLabel"));
            labelGender.setMaximumSize(new Dimension(90, 20));
            labelGender.setMinimumSize(new Dimension(90, 20));

            labelType = new JLabel(bundle.getString("typeLabel"));
            labelType.setMaximumSize(new Dimension(90, 20));
            labelType.setMinimumSize(new Dimension(90, 20));

            labelYear1 = new JLabel(bundle.getString("yearLabel1"));
            labelYear1.setMaximumSize(new Dimension(90, 20));
            labelYear1.setMinimumSize(new Dimension(90, 20));
            labelYear2 = new JLabel(bundle.getString("yearLabel2"));
            labelYear2.setMaximumSize(new Dimension(90, 20));
            labelYear2.setMinimumSize(new Dimension(90, 20));

            labelMass = new JLabel(bundle.getString("massLabel") + " <=");
            labelMass.setMaximumSize(new Dimension(90, 20));
            labelMass.setMinimumSize(new Dimension(90, 20));

            labelMass2 = new JLabel("180");
            labelMass2.setMaximumSize(new Dimension(50, 20));
            labelMass2.setMinimumSize(new Dimension(50, 20));

            labelX = new JLabel("Х");
            labelX.setMaximumSize(new Dimension(90, 20));
            labelX.setMinimumSize(new Dimension(90, 20));

            labelY = new JLabel("У");
            labelY.setMaximumSize(new Dimension(90, 20));
            labelY.setMinimumSize(new Dimension(90, 20));

            labelCondition = new JLabel(bundle.getString("conditionLabel"));
            labelCondition.setMaximumSize(new Dimension(100, 20));
            labelCondition.setMinimumSize(new Dimension(100, 20));

            textFieldName = new JTextField();
            textFieldName.setMaximumSize(new Dimension(170, 20));
            textFieldName.setMinimumSize(new Dimension(170, 20));

            textFieldYear = new JTextField();
            textFieldYear.setMaximumSize(new Dimension(170, 20));
            textFieldYear.setMinimumSize(new Dimension(170, 20));

            textFieldX = new JTextField();
            textFieldX.setMaximumSize(new Dimension(120, 20));
            textFieldX.setMinimumSize(new Dimension(120, 20));

            String [] values = {"=", "<", ">"};
            textFieldXop = new JComboBox(values);
            textFieldXop.setMaximumSize(new Dimension(50, 20));
            textFieldXop.setMinimumSize(new Dimension(50, 20));

            textFieldY = new JTextField();
            textFieldY.setMaximumSize(new Dimension(120, 20));
            textFieldY.setMinimumSize(new Dimension(120, 20));

            textFieldYop = new JComboBox(values);
            textFieldYop.setMaximumSize(new Dimension(50, 20));
            textFieldYop.setMinimumSize(new Dimension(50, 20));

            checkIsAlive = new JCheckBox(bundle.getString("isAliveLabel"));
            checkIsAlive.setMaximumSize(new Dimension(170, 20));
            checkIsAlive.setMinimumSize(new Dimension(170, 20));

            checkMale = new JCheckBox(bundle.getString("shortMaleLabel"));
            checkMale.setMaximumSize(new Dimension(170, 20));
            checkMale.setMinimumSize(new Dimension(170, 20));

            checkFemale = new JCheckBox(bundle.getString("shortFemaleLabel"));
            checkFemale.setMaximumSize(new Dimension(170, 20));
            checkFemale.setMinimumSize(new Dimension(170, 20));

            checkNormal = new JCheckBox(bundle.getString("normalTypeLabel"));
            checkNormal.setMaximumSize(new Dimension(170, 20));
            checkNormal.setMinimumSize(new Dimension(170, 20));

            checkPolice = new JCheckBox(bundle.getString("policeTypeLabel"));
            checkPolice.setMaximumSize(new Dimension(170, 20));
            checkPolice.setMinimumSize(new Dimension(170, 20));

            checkBandit = new JCheckBox(bundle.getString("banditTypeLabel"));
            checkBandit.setMaximumSize(new Dimension(170, 20));
            checkBandit.setMinimumSize(new Dimension(170, 20));

            checkArrested = new JCheckBox(bundle.getString("arrestedCondLabel"));
            checkArrested.setMaximumSize(new Dimension(170, 20));
            checkArrested.setMinimumSize(new Dimension(170, 20));

            checkImprisoned = new JCheckBox(bundle.getString("imprisonedCondLabel"));
            checkImprisoned.setMaximumSize(new Dimension(170, 20));
            checkImprisoned.setMinimumSize(new Dimension(170, 20));

            checkFree = new JCheckBox(bundle.getString("freeCondLabel"));
            checkFree.setMaximumSize(new Dimension(170, 20));
            checkFree.setMinimumSize(new Dimension(170, 20));

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
        }
        pack();
        setBounds(100, 100, 1000, 800);


        update();

        actManager = new ActionManager(this);
        actManager.start();
        changeBundle("package_en_NZ");
    }

    private void changeBundle(String newBundleFile){
        try{
            bundle = ResourceBundle.getBundle("langProperties/" + newBundleFile);
            update();
        } catch (Exception e){
            System.err.println(e + "\nNo localization file data: " + newBundleFile + " was not found!");
            return;
        }

        this.setTitle(bundle.getString("windowTitle"));
        buttonStart.setText(bundle.getString("startButtonLabel"));
        buttonStop.setText(bundle.getString("stopButtonLabel"));
        labelName.setText(bundle.getString("nameLabel"));
        labelGender.setText(bundle.getString("genderLabel"));
        labelType.setText(bundle.getString("typeLabel"));
        labelYear1.setText(bundle.getString("yearLabel1"));
        labelYear2.setText(bundle.getString("yearLabel2"));
        labelMass.setText(bundle.getString("massLabel") + " <=");
        labelCondition.setText(bundle.getString("conditionLabel"));
        checkIsAlive.setText(bundle.getString("isAliveLabel"));
        checkMale.setText(bundle.getString("shortMaleLabel"));
        checkFemale.setText(bundle.getString("shortFemaleLabel"));
        checkNormal.setText(bundle.getString("normalTypeLabel"));
        checkPolice.setText(bundle.getString("policeTypeLabel"));
        checkBandit.setText(bundle.getString("banditTypeLabel"));
        checkArrested.setText(bundle.getString("arrestedCondLabel"));
        checkImprisoned.setText(bundle.getString("imprisonedCondLabel"));
        checkFree.setText(bundle.getString("freeCondLabel"));
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
