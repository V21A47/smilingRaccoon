package Interpreter;

import AliveObjects.Human;


public class Interpreter {
    private String fileName;
    private CollectionStorage storage;

    public Interpreter(String fileName){
        this.fileName = fileName;
        storage = new CollectionStorage("Main storage", fileName);
    }

    public void saveData(){
            storage.save();
            System.out.println("The data was saved to file " + fileName);
    }
    public void loadData(){
        storage.load();
        if(!storage.isEmpty()){
            System.out.println("Some data from " + fileName + " was added");
        }
    }

    public void getCommand(String command, Object object){
    
        Human human = null;
        String path = null;
        
        if(object instanceof Human){
            human = (Human)(object);
        } else if (object instanceof String){
            path = (String)(object);
        }
        
        switch (command) {
            case "remove_lower":
                storage.remove_lower(human);
                return;
            case "remove_greater":
                storage.remove_greater(human);
                return;
            case "remove":
                storage.remove(human);
                return;
            case "add_if_max":
                storage.add_if_max(human);
                return;
            case "add_if_min":
                storage.add_if_min(human);
                return;
            case "add":
                storage.add(human);
                return;
            case "import":
                storage.importFromFile(path);
                return;
                
        }
    }
    
    public boolean getCommand(String command){
        switch(command){
            case "load":
                storage.load();
                return true;
            case "info":
                storage.info();
                return true;
            case "save":
                storage.save();
                return true;
            case "clear":
                storage.clear();
                return true;
            case "exit":
                storage.save();
                System.exit(0);
            default:
                return false;
        }
    }
}

