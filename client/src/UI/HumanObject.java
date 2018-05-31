package UI;

import javax.swing.*;
import java.awt.*;

import AliveObjects.*;
import Interpreter.*;
import Places.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class HumanObject extends JPanel{

    private String caption;
    private int x;
    private int y;

    public BufferedImage image;

    public static int xPoint;
    public static int yPoint;

    private Human human;

    public static BufferedImage man1;
    public static BufferedImage man2;
    public static BufferedImage man3;
    public static BufferedImage girl;
    public static BufferedImage policeman;
    public static BufferedImage bandit;
    public static BufferedImage banditP;

    static {
        try{
            man1 = ImageIO.read(new File("resources/man1.png"));
            man2 = ImageIO.read(new File("resources/man2.png"));
            man3 = ImageIO.read(new File("resources/man3.png"));
            girl = ImageIO.read(new File("resources/girl.png"));
            policeman = ImageIO.read(new File("resources/policeman.png"));
            bandit = ImageIO.read(new File("resources/bandit.png"));
            banditP = ImageIO.read(new File("resources/banditPotracheno.png"));

            xPoint = (int)((Math.abs(SearchableThing.maxX) + Math.abs(SearchableThing.minX)) / 2) + 30;
            yPoint = (int)((Math.abs(SearchableThing.maxY) + Math.abs(SearchableThing.minY)) / 2) + 30;
        } catch (IOException e){
            System.err.println(e);
        }
    }



    public HumanObject(Human human){
        super();
        this.human = human;
        setBounds(xPoint + human.getX(), yPoint + human.getY(), 32, 32);
        setToolTipText(human.toString());

        if(human.getType() == HumanType.NORMAL){
            if(human.getGender().equals("female")){
                image = HumanObject.girl;
            } else if (human.getSizeValue() > 100){
                image = HumanObject.man3;
            } else {
                if(human.getYearOfBirth() > 1995){
                    image = HumanObject.man1;
                } else {
                    image = HumanObject.man2;
                }
            }
        } else if(human.getType() == HumanType.POLICE){
            image = HumanObject.policeman;
        } else {
            if(human.getConditionInCommunity().getState() == StateOfFreedom.FREE){
                image = HumanObject.bandit;
            } else {
                image = HumanObject.banditP;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(image, 0, 0, this);
         // see javadoc for more info on the parameters
    }

}
