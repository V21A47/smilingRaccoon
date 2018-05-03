package Interpreter;

import AliveObjects.Human;
import com.google.gson.Gson;


public class Interpreter {
    private String fileName;
    private CollectionStorage storage;
    private Gson gson;
    
    public Interpreter(String fileName){
        this.fileName = fileName;
        gson = new Gson();
        
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

    public String getCommand(String text){
        String command = "",
               operand = "",
               path = "";
               
        command = text;
        if(command.indexOf('{')>0){
            command = text.substring(0, text.indexOf('{')).trim();
            if(!command.equals("import")){
                operand = text.substring(text.indexOf('{'), text.length()).trim();
            } else {
                path = text.substring(text.indexOf('{')+1, text.length()-1).trim();
            }            
        }
        
        Human human = null;
        
        if(!operand.equals("")){
            System.out.println("trying!");
            human = gson.fromJson(operand, Human.class);
        }
        
        //System.out.println(command + "\n" + operand + "\n" + path + "\n" + human);
        
        switch (command) {
            case "remove_lower":
                return storage.remove_lower(human);
            case "remove_greater":
                return storage.remove_greater(human);
            case "remove":
                return storage.remove(human);
            case "add_if_max":
                return storage.add_if_max(human);
            case "add_if_min":
                return storage.add_if_min(human);
            case "add":
                return storage.add(human);
            case "import":
                return storage.importFromFile(path);
            case "load":
                return storage.load();
            case "info":
                return storage.info();
            case "save":
                return storage.save();
            case "clear":
                return storage.clear();
            default:
                return "No such command";
        }
    }
}

