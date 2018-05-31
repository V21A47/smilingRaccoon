package UI;

import AliveObjects.*;
import Interpreter.*;
import Places.*;

import java.util.HashSet;

public class ActionManager extends Thread{

    public ClientWindow window;
    private HashSet<HumanObject> humanPanelsSet;
    public boolean shouldWork = false;
    private int alpha = 0;

    public int getAlpha(){
        return alpha;
    }

    public void stopAct(){
        if(window.getHumansInAct() == null){
            return;
        }

        for(HumanObject obj : window.getHumansInAct()){
            obj.shouldDisappear(false);
        }
        alpha = 0;
    }

    public ActionManager(ClientWindow win){
        this.window = win;
        this.humanPanelsSet = humanPanelsSet;
    }

    public void run(){
        try{
            boolean flag = false;
            int i = 0;
            int mAlpha = 17;
            while(true){
                if(shouldWork){
                    if(window.getHumansInAct() != null){
                        //System.out.println( window.getHumansInAct().size());

                        if(alpha < 240 && !flag){
                            this.alpha += mAlpha;
                        } else if(alpha > 240) {
                            flag = true;
                            this.alpha -= mAlpha;
                        } else if(flag && alpha > 20){
                            this.alpha -= mAlpha;
                        } else {
                            flag = false;
                            this.alpha -= mAlpha;
                        }

                        window.update();
                        Thread.sleep(250);
                    }
                } else {
                    i = 0;
                    mAlpha = 17;
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e){
            System.err.println(e);
            return;
        }
    }
}
