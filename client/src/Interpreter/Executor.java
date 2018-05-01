package Interpreter;

import AliveObjects.Human;

class Executor{
    public void execute(String command, Human humanObject){
        System.out.println("got command:\n"+command+"\n"+humanObject.toString());
        if(command.equals("add")){
            CSVManager.writeToFile(humanObject, "testFile");
        }
    } 
}
