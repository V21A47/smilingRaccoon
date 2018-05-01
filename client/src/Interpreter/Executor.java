package Interpreter;

import AliveObjects.Human;

class Executor{
    public void execute(String command, Human humanObject){
        if(humanObject == null){
            System.out.println("got command:\n\t"+command+"\n\tnull");
        } else {
            System.out.println("got command:\n\t"+command+"\n\t"+humanObject.toString());
        }
    }
}
