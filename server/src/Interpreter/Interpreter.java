package Interpreter;

import AliveObjects.Human;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Scanner;

public class Interpreter {
    private String fileName;
    private CollectionStorage storage;
    private Gson gson = new Gson();
    private boolean stop = false;

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

    private void getCommand(String message) throws IllegalArgumentException{
        message = message.trim();
        if(message.contains("{") && message.contains("}")){
            // others
            String parameters = message.substring(message.indexOf("{")).trim();
            message = message.substring(0, message.indexOf("{")).trim();

            switch (message) {
                case "remove_lower":
                    storage.remove_lower(gson.fromJson(parameters, Human.class));
                    return;
                case "remove_greater":
                    storage.remove_greater(gson.fromJson(parameters, Human.class));
                    return;
                case "remove":
                    storage.remove(gson.fromJson(parameters, Human.class));
                    return;
                case "add_if_max":
                    storage.add_if_max(gson.fromJson(parameters, Human.class));
                    return;
                case "add_if_min":
                    storage.add_if_min(gson.fromJson(parameters, Human.class));
                    return;
                case "add":
                    storage.add(gson.fromJson(parameters, Human.class));
                    return;
                case "import":
                    storage.importFromFile(parameters.substring(1, parameters.lastIndexOf("}")).trim());
                    return;
            }

        } else {
            // load, clear, info, save
            switch (message) {
                case "load":
                    storage.load();
                    return;
                case "info":
                    storage.info();
                    return;
                case "save":
                    storage.save();
                    return;
                case "clear":
                    storage.clear();
                    return;
                case "exit":
                    stop = true;
                    storage.save();
                    return;
            }
        }

        throw new IllegalArgumentException("No such command ");
    }

    public void go(){
        int counter = 0;
        StringBuilder message = new StringBuilder();
        Scanner sc = new Scanner(System.in);

        int last_index = 0;
        //TODO EOF problem...
        while(!stop){
            message.setLength(0);
            counter = 0;
            last_index=0;
            System.out.print("> ");
            do {
                if(counter!= 0)
                    System.out.print(": ");

                if (sc.hasNextLine()) {
                    message.append(sc.nextLine());
                    if(message.length()!= 0 && !Character.isAlphabetic((int)message.charAt(0))){
                        continue;
                    }
                } else {
                    stop = true;
                }

                for (int i = last_index; i < message.length(); i++) {
                    if (message.charAt(i) == '{') {
                        counter += 1;
                    } else if (message.charAt(i) == '}') {
                        counter -= 1;
                    }
                }

                last_index = message.length();
            } while (counter != 0);

            try{
                if(!message.toString().trim().isEmpty()) {
                    getCommand(message.toString());
                }

            } catch (IllegalArgumentException ex){
                System.out.println(ex.getMessage());
            } catch (JsonSyntaxException ex){
                System.out.println("A json parsing error was found");
            } catch (StringIndexOutOfBoundsException ex){
                System.out.println("An error while reading was found");
                break;
            } catch (NullPointerException ex){
                System.out.println("An error while parsing json was found");
            }
        }
    }
}
