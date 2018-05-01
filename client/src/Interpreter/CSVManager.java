package Interpreter;

import AliveObjects.Human;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;

public class CSVManager {
    public static void clearFile(String fileName){
        FileWriter writer;
        try {
            writer = new FileWriter(fileName);
            writer.write("");
            writer.close();
        } catch (IOException ex){
            System.out.println("An error while writing to file " + fileName + " was occurred");
        }
    }

    public static void writeToFile(Human human, String fileName){
        FileWriter writer;
        try {
            writer = new FileWriter(fileName, true);
        } catch (IOException ex){
            System.out.println("An error while writing to file " + fileName + " was occurred");
            return;
        }

        StringBuilder text = new StringBuilder();

        text.append(human.getX());
        text.append(",");
        
        text.append(human.getY());
        text.append(",");

        text.append(human.getType());
        text.append(",");

        text.append(human.getConditionInCommunity().getPublicAcceptance());
        text.append(",");

        text.append(human.getConditionInCommunity().getState());
        text.append(",");

        text.append(human.getConditionInCommunity().getRemainingTime());
        text.append(",");

        text.append(human.isAlive());
        text.append(",");

        text.append(human.getAge());
        text.append(",");

        text.append(human.getSizeValue());
        text.append(",");

        text.append(human.getName());
        text.append(",");
        
        text.append(human.getYearOfBirth());

        text.append(System.lineSeparator());

        try {
            writer.write(text.toString());
            writer.close();
        } catch (IOException ex){
            System.out.println("An error while writing to file " + fileName + " was occurred.");
        }
    }


    public static ArrayList<Human> readFromFile(String fileName){
        InputStreamReader reader;
        Gson gson = new Gson();

        try{
            reader = new InputStreamReader(new FileInputStream(fileName));
        } catch (FileNotFoundException e){
            System.err.println("File was not found");
            return null;
        }
        
        int data = 0;
        char symbol;

        ArrayList<String> listOfValues = new ArrayList<>();
        ArrayList<Human> listOfHumans = new ArrayList<>();
        StringBuilder tempString = new StringBuilder();

        while(data != -1){
            try {
                data = reader.read();
                symbol = (char)data;
            } catch (IOException ex){
                System.out.println("Error while reading a file");
                return null;
            }

            if(symbol == ','){
                listOfValues.add(tempString.toString());

                tempString.setLength(0);
            } else if (symbol == System.lineSeparator().charAt(0)){
                listOfValues.add(tempString.toString());
                //
                if(listOfValues.size() != 11){
                    System.out.println("Incorrect file data");
                    return null;
                }
                String jsonText;
                if(listOfValues.get(2).equals("обычный житель")){
                    listOfValues.set(2, "NORMAL");
                } else if (listOfValues.get(2).equals("полицейский")){
                    listOfValues.set(2, "POLICE");
                } else if (listOfValues.get(2).equals("бандит")){
                    listOfValues.set(2, "BANDIT");
                }
                try {
                    jsonText = "{" +
                            "\"x\":" + listOfValues.get(0) + "," +
                            "\"y\":" + listOfValues.get(1) + "," +
                            "\"type\":\"" + listOfValues.get(2) + "\"," +
                            "\"condition\":{" +
                            "\"publicAcceptance\":" +  listOfValues.get(3) + "," +
                            "\"state\":\"" + listOfValues.get(4) + "\"," +
                            "\"remainingTime\":" + listOfValues.get(5) + "}," +
                            "\"isAlive\":" + listOfValues.get(6) + "," +
                            "\"age\":" + listOfValues.get(7) + "," +
                            "\"sizeValue\":" + listOfValues.get(8) + "," +
                            "\"name\":\"" + listOfValues.get(9) + "\"," +
                            "\"yearOfBirth\":" + listOfValues.get(10) +
                            "}";

                    System.out.println(jsonText);
                }catch (IndexOutOfBoundsException ex) {
                    System.out.println("Error while parsing json was occurred");
                    return null;
                }

                listOfHumans.add(gson.fromJson(jsonText, Human.class));
                tempString.setLength(0);
                listOfValues.clear();
            } else {
                tempString.append(String.valueOf(symbol));
            }
        }
        try {
            reader.close();
        } catch (IOException ex){
            System.out.println("Error while closing file");
        }
        return listOfHumans;
    }
}
