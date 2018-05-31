package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import AliveObjects.*;
import Places.*;


class ChangeObjectWindow extends JFrame{
    private JLabel labelTask;
    private JLabel labelName;
    private JLabel labelGender;
    private JLabel labelType;
    private JLabel labelIsAlive;
    private JLabel labelX;
    private JLabel labelY;
    private JLabel labelYearOfBirth;
    private JLabel labelMass;
    private JLabel labelCondition;
    private JLabel labelTime;
    private JLabel labelInf;

    private JTextField textFieldName;
    private JTextField textFieldX;
    private JTextField textFieldY;
    private JTextField textFieldYearOfBirth;
    private JTextField textFieldMass;
    private JTextField textFieldTime;

    private JRadioButton radioGenderMale;
    private JRadioButton radioGenderFemale;

    private JCheckBox checkIsAlive;

    private JComboBox comboType;
    private JComboBox comboCondition;

    private JButton buttonEdit;

    private ServerWindow serverWindow;
    private Human oldHuman;


    public ChangeObjectWindow(ServerWindow win, Human human){
        super("Add object");
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.serverWindow = win;
        this.oldHuman = human;

        {// Initializing objects
            labelTask = new JLabel("Измените данные элемента");
            labelTask.setMaximumSize(new Dimension(250, 20));
            labelTask.setMinimumSize(new Dimension(250, 20));

            labelName = new JLabel("Имя");
            labelName.setMaximumSize(new Dimension(60, 20));
            labelName.setMinimumSize(new Dimension(60, 20));

            labelGender = new JLabel("Пол");
            labelGender.setMaximumSize(new Dimension(60, 20));
            labelGender.setMinimumSize(new Dimension(60, 20));

            labelType = new JLabel("Тип");
            labelType.setMaximumSize(new Dimension(60, 20));
            labelType.setMinimumSize(new Dimension(60, 20));

            labelIsAlive = new JLabel("Живой");
            labelIsAlive.setMaximumSize(new Dimension(60, 20));
            labelIsAlive.setMinimumSize(new Dimension(60, 20));

            labelX = new JLabel("Х");
            labelX.setMaximumSize(new Dimension(60, 20));
            labelX.setMinimumSize(new Dimension(60, 20));

            labelY = new JLabel("У");
            labelY.setMaximumSize(new Dimension(60, 20));
            labelY.setMinimumSize(new Dimension(60, 20));

            labelYearOfBirth = new JLabel("Год рожд.");
            labelYearOfBirth.setMaximumSize(new Dimension(90, 20));
            labelYearOfBirth.setMinimumSize(new Dimension(100, 20));

            labelMass = new JLabel("Масса");
            labelMass.setMaximumSize(new Dimension(100, 20));
            labelMass.setMinimumSize(new Dimension(100, 20));

            labelCondition = new JLabel("Состояние");
            labelCondition.setMaximumSize(new Dimension(100, 20));
            labelCondition.setMinimumSize(new Dimension(100, 20));

            labelTime = new JLabel("Ост. время");
            labelTime.setMaximumSize(new Dimension(100, 20));
            labelTime.setMinimumSize(new Dimension(100, 20));

            labelInf = new JLabel("");
            labelInf.setMaximumSize(new Dimension(400, 20));
            labelInf.setMinimumSize(new Dimension(400, 20));


            textFieldName = new JTextField();
            textFieldName.setMaximumSize(new Dimension(150, 20));
            textFieldName.setMinimumSize(new Dimension(150, 20));


            textFieldX = new JTextField();
            textFieldX.setMaximumSize(new Dimension(150, 20));
            textFieldX.setMinimumSize(new Dimension(150, 20));

            textFieldY = new JTextField();
            textFieldY.setMaximumSize(new Dimension(150, 20));
            textFieldY.setMinimumSize(new Dimension(150, 20));

            textFieldYearOfBirth = new JTextField();
            textFieldYearOfBirth.setMaximumSize(new Dimension(150, 20));
            textFieldYearOfBirth.setMinimumSize(new Dimension(150, 20));

            textFieldMass = new JTextField();
            textFieldMass.setMaximumSize(new Dimension(150, 20));
            textFieldMass.setMinimumSize(new Dimension(150, 20));

            textFieldTime = new JTextField();
            textFieldTime.setMaximumSize(new Dimension(150, 20));
            textFieldTime.setMinimumSize(new Dimension(150, 20));

            radioGenderMale = new JRadioButton("М");
            radioGenderMale.setMaximumSize(new Dimension(150, 20));
            radioGenderMale.setMinimumSize(new Dimension(150, 20));

            radioGenderFemale = new JRadioButton("Ж");
            radioGenderFemale.setMaximumSize(new Dimension(150, 20));
            radioGenderFemale.setMinimumSize(new Dimension(150, 20));

            radioGenderMale.setSelected(true);

            ButtonGroup radioGroup = new ButtonGroup();

            radioGroup.add(radioGenderMale);
            radioGroup.add(radioGenderFemale);

            checkIsAlive = new JCheckBox("Живой");
            checkIsAlive.setSelected(true);
            checkIsAlive.setMaximumSize(new Dimension(150, 20));
            checkIsAlive.setMinimumSize(new Dimension(150, 20));

            String[] typeItems = {
                "Обычный житель",
                "Полицейский",
                "Бандит"
            };
            comboType = new JComboBox(typeItems);
            comboType.setMaximumSize(new Dimension(150, 20));
            comboType.setMinimumSize(new Dimension(150, 20));

            String[] conditionItems = {
                "Свободен",
                "Аррестован",
                "Заключен"
            };
            comboCondition = new JComboBox(conditionItems);
            comboCondition.setMaximumSize(new Dimension(150, 20));
            comboCondition.setMinimumSize(new Dimension(150, 20));

            buttonEdit = new JButton("Изменить");
        }

        int borderGap = 15;
        int gap = 10;
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        {// Layout
            layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, borderGap, 1200)
                    .addComponent(labelTask)
                    .addGap(0, borderGap, 1200)
                )
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, borderGap, 1200)
                    .addComponent(labelName)
                    .addGap(gap)
                    .addComponent(textFieldName)
                    .addGap(2*gap)
                    .addComponent(labelYearOfBirth)
                    .addGap(gap)
                    .addComponent(textFieldYearOfBirth)
                    .addGap(0, borderGap, 1200)
                )
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, borderGap, 1200)
                    .addComponent(labelGender)
                    .addGap(gap)
                    .addGroup(layout.createParallelGroup()
                        .addComponent(radioGenderMale)
                        .addComponent(radioGenderFemale)
                    )
                    .addGap(2*gap)
                    .addComponent(labelMass)
                    .addGap(gap)
                    .addComponent(textFieldMass)
                    .addGap(0, borderGap, 1200)
                )
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, borderGap, 1200)
                    .addComponent(labelType)
                    .addGap(gap)
                    .addComponent(comboType)
                    .addGap(2*gap)
                    .addComponent(labelCondition)
                    .addGap(gap)
                    .addComponent(comboCondition)
                    .addGap(0, borderGap, 1200)
                )
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, borderGap, 1200)
                    .addComponent(labelIsAlive)
                    .addGap(gap)
                    .addComponent(checkIsAlive)
                    .addGap(2*gap)
                    .addComponent(labelTime)
                    .addGap(gap)
                    .addComponent(textFieldTime)
                    .addGap(0, borderGap, 1200)
                )
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, borderGap, 1200)
                    .addComponent(labelX)
                    .addGap(gap)
                    .addComponent(textFieldX)
                    .addGap(250 + gap*2)
                    .addGap(0, borderGap, 1200)
                )
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, borderGap, 1200)
                    .addComponent(labelY)
                    .addGap(gap)
                    .addComponent(textFieldY)
                    .addGap(250 + gap*2)
                    .addGap(0, borderGap, 1200)
                )
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, borderGap, 1200)
                    .addComponent(labelInf)
                    .addGap(2*gap)
                    .addComponent(buttonEdit)
                    .addGap(0, borderGap, 1200)
                )
            );

            layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(0, borderGap, 900)
                .addComponent(labelTask)
                .addGap(gap)
                .addGroup(layout.createParallelGroup()
                    .addComponent(labelName)
                    .addComponent(textFieldName)
                    .addComponent(labelYearOfBirth)
                    .addComponent(textFieldYearOfBirth)
                )
                .addGap(gap)
                .addGroup(layout.createParallelGroup()
                    .addComponent(labelGender)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(radioGenderMale)
                        .addComponent(radioGenderFemale)
                    )
                    .addComponent(labelMass)
                    .addComponent(textFieldMass)
                )
                .addGap(gap)
                .addGroup(layout.createParallelGroup()
                    .addComponent(labelType)
                    .addComponent(comboType)
                    .addComponent(labelCondition)
                    .addComponent(comboCondition)
                )
                .addGap(gap)
                .addGroup(layout.createParallelGroup()
                    .addComponent(labelIsAlive)
                    .addComponent(checkIsAlive)
                    .addComponent(labelTime)
                    .addComponent(textFieldTime)
                )
                .addGap(gap)
                .addGroup(layout.createParallelGroup()
                    .addComponent(labelX)
                    .addComponent(textFieldX)
                )
                .addGap(gap)
                .addGroup(layout.createParallelGroup()
                    .addComponent(labelY)
                    .addComponent(textFieldY)
                )
                .addGap(gap)
                .addGroup(layout.createParallelGroup()
                    .addComponent(labelInf)
                    .addComponent(buttonEdit)
                )
                .addGap(0, borderGap, 900)
            );
        }

        pack();
        buttonEdit.addActionListener(new ButtonEditEventListener());
        this.setBounds(250, 250, 600, 350);

        setValues(human);
    }
/*
    private JTextField textFieldName;
    private JTextField textFieldX;
    private JTextField textFieldY;
    private JTextField textFieldYearOfBirth;
    private JTextField textFieldMass;
    private JTextField textFieldTime;

    private JRadioButton radioGenderMale;
    private JRadioButton radioGenderFemale;

    private JCheckBox checkIsAlive;

    private JComboBox comboType;
    private JComboBox comboCondition;

    private JButton buttonAdd;
*/

    private void setValues(Human human){
        textFieldName.setText(human.getName());
        textFieldX.setText(String.valueOf(human.getX()));
        textFieldY.setText(String.valueOf(human.getY()));
        textFieldYearOfBirth.setText(String.valueOf(human.getYearOfBirth()));
        textFieldMass.setText(String.valueOf(human.getSizeValue()));
        textFieldTime.setText(String.valueOf(human.getConditionInCommunity().getRemainingTime()));

        if(human.isAlive()){
            checkIsAlive.setSelected(true);
        }

        if(human.getGender().equals("male")){
            radioGenderMale.setSelected(true);
        } else {
            radioGenderFemale.setSelected(true);
        }

        switch(human.getConditionInCommunity().getState()){
            case FREE:
                comboCondition.setSelectedItem("Свободен");
                break;
            case ARRESTED:
                comboCondition.setSelectedItem("Аррестован");
                break;
            default:
                comboCondition.setSelectedItem("Заключен");
                break;
        }

        switch(human.getType()){
            case NORMAL:
                comboType.setSelectedItem("Обычный житель");
                break;
            case POLICE:
                comboType.setSelectedItem("Полицейский");
                break;
            default:
                comboType.setSelectedItem("Бандит");
                break;
        }
    }
    class ButtonEditEventListener implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            labelInf.setText("");
            if(textFieldName.getText().isEmpty()){
                labelInf.setText("Укажите имя");
                return;
            } else if(textFieldX.getText().isEmpty()){
                labelInf.setText("Укажите X координату");
                return;
            } else if(textFieldY.getText().isEmpty()){
                labelInf.setText("Укажите Y координату");
                return;
            } else if(textFieldYearOfBirth.getText().isEmpty()){
                labelInf.setText("Укажите год рождения");
                return;
            } else if(textFieldMass.getText().isEmpty()){
                labelInf.setText("Укажите массу");
                return;
            }

            try{
                int x = Integer.parseInt(textFieldX.getText());
                if(x < SearchableThing.minX || x > SearchableThing.maxX){
                    labelInf.setText("Укажите корректную X координату( [" + SearchableThing.minX + ", " + SearchableThing.maxX + "] )");
                    return;
                }
            } catch (NumberFormatException error){
                labelInf.setText("Укажите корректную X координату( [" + SearchableThing.minX + ", " + SearchableThing.maxX + "] )");
                return;
            }

            try{
                int y = Integer.parseInt(textFieldY.getText());
                if(y < SearchableThing.minY || y > SearchableThing.maxY){
                    labelInf.setText("Укажите корректную Y координату( [" + SearchableThing.minY + ", " + SearchableThing.maxY + "] )");
                    return;
                }
            } catch (NumberFormatException error){
                labelInf.setText("Укажите корректную Y координату( [" + SearchableThing.minY + ", " + SearchableThing.maxY + "] )");
                return;
            }

            try{
                int year = Integer.parseInt(textFieldYearOfBirth.getText());
                if(year < 1900 || year > 2018){
                    labelInf.setText("Укажите корректный год рождения ( [0, 2018] )");
                    return;
                }
            } catch (NumberFormatException error){
                labelInf.setText("Укажите корректный год рождения ( [0, 2018] )");
                return;
            }

            try{
                double m = Double.parseDouble(textFieldMass.getText());
                if(m <= 0 || m > 180){
                    labelInf.setText("Укажите корректную массу ( (0, 180] )");
                    return;
                }
            } catch (NumberFormatException error){
                labelInf.setText("Укажите корректную массу ( (0, 180] )");
                return;
            }

            String s = comboCondition.getSelectedItem().toString();
            String state = "FREE";
            if(s.equals("Аррестован")){
                state = "ARRESTED";
            } else if(s.equals("Заключен")){
                state = "IMPRISONED";
            }

            try{
                int m = Integer.parseInt(textFieldTime.getText());
                if(m < 0 || m > 1000 || ( !state.equals("FREE") && m == 0) ){
                    labelInf.setText("Укажите корректное время в днях ( [1, 1000] )");
                    return;
                } else if(state.equals("FREE") && m != 0){
                    labelInf.setText("Этот человек не находится под стражей");
                    return;
                }
            } catch (NumberFormatException error){
                if(!state.equals("FREE")){
                    labelInf.setText("Укажите корректное время в днях ( [1, 1000] )");
                    return;
                } else {
                    textFieldTime.setText("0");
                }
            }

            s = comboType.getSelectedItem().toString();
            String type = "NORMAL";
            if(s.equals("Полицейский")){
                type = "POLICE";
            } else if(s.equals("Бандит")){
                type = "BANDIT";
            }

            String gender = "male";
            if(radioGenderFemale.isSelected()){
                gender = "female";
            }

            s = "true";
            if(! checkIsAlive.isSelected()){
                s = "false";
            }

            String operand ="{" +
                            "\"x\":" + Integer.parseInt(textFieldX.getText()) + "," +
                            "\"y\":" + Integer.parseInt(textFieldY.getText()) + "," +
                            "\"type\":\"" + type + "\"," +
                            "\"condition\":{" +
                            "\"state\":\"" + state + "\"," +
                            "\"remainingTime\":" + Integer.parseInt(textFieldTime.getText()) + "}," +
                            "\"isAlive\":" + s + "," +
                            "\"yearOfBirth\":" + Integer.parseInt(textFieldYearOfBirth.getText()) + "," +
                            "\"sizeValue\":" + Double.parseDouble(textFieldMass.getText()) + "," +
                            "\"gender\":\"" + gender + "\"," +
                            "\"name\":\"" + textFieldName.getText() + "\"" +
                            "}";

            serverWindow.getSheduler().getStorage().remove(oldHuman);

            if(serverWindow.getInterpreter().getCommand("add " + operand).indexOf("can't be") > 0){
                labelInf.setText("Такой объект уже есть в коллекции");
                serverWindow.getSheduler().getStorage().add(oldHuman);
                serverWindow.updateTree();
            } else {
                setVisible(false);
                dispose();
            }

        }
    }
}
