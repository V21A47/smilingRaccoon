package Interpreter;

import AliveObjects.Human;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.util.HashSet;
import java.util.ArrayList;

public class Parser{
    private HashSet<String> setOfCOmmandsWithoutOperand = new HashSet<>();
    private HashSet<String> setOfCommandsWithOperand = new HashSet<>();
    private Executor executor;

    public Parser(Executor executor){
        this.executor = executor;
        setOfCommandsWithOperand.add("remove_greater");
        setOfCOmmandsWithoutOperand.add("save");
        setOfCOmmandsWithoutOperand.add("story");
        setOfCommandsWithOperand.add("add_if_min");
        setOfCOmmandsWithoutOperand.add("info");
        setOfCommandsWithOperand.add("import");
        setOfCOmmandsWithoutOperand.add("load");
        setOfCOmmandsWithoutOperand.add("clear");
        setOfCommandsWithOperand.add("remove");
        setOfCommandsWithOperand.add("add");
        setOfCommandsWithOperand.add("add_if_max");
        setOfCommandsWithOperand.add("remove_lower");
    }

    /**
    * Parses a string, devides it to command and operand parts. Operand should be conrained in { } brackets.
    * Then it sends command's data to Executor. "Import" command sends by parts of the command and each element
    * of a file.
    *
    * @param string the string which consists of a command and a possible operand
    */
    public void parse(String string) throws ParseException{

        string = string.trim();

        if(string.isEmpty()){
            return;
        }

        String command = "",
               operand = "";

        if(string.indexOf('{') >= 0){
            if(string.indexOf('{') == 0){
                throw new ParseException("No command was found.");
            } else if(string.indexOf('}') <= 0 || string.indexOf('}') < string.indexOf('{')){
                throw new ParseException("Bad command format.");
            }

            operand = string.substring(string.indexOf('{'));
            command = string.substring(0, string.indexOf('{'));
        } else {
            command = string;
        }

        command = command.toLowerCase().trim();

        if(!setOfCOmmandsWithoutOperand.contains(command) && !setOfCommandsWithOperand.contains(command)){
            throw new ParseException("No such command");
        }

        // if a command have an operand
        if(setOfCommandsWithOperand.contains(command)){
            if(operand.trim().isEmpty()){
                throw new ParseException("Need an operand.");
            }

            if(command.equals("import")){
                executor.execute(command, operand);
            } else {
                Human humanObject = null;
                humanObject = parseJson(operand);

                if(humanObject != null){
                                        // was humanObject
                    executor.execute(command, operand);
                }
            }

        } else if(setOfCOmmandsWithoutOperand.contains(command)){
            executor.execute(command, null);
        }
    }

    /**
    * Jast parses json-string into Human object.
    *
    * @param operand a json-string which contains object properties
    * @return a human object
    */
    private Human parseJson(String operand){
        Gson gson = new Gson();
        Human h = null;
        try{
            h = gson.fromJson(operand, Human.class);
        } catch (JsonSyntaxException e){
            System.err.println("Incorrect operand.");
        }
        return h;
    }

}
