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

        text.append(human.getBirthCertificateNumber());
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

        text.append(System.lineSeparator());

        try {
            writer.write(text.toString());
            writer.close();
        } catch (IOException ex){
            System.out.println("An error while writing to file " + fileName + " was occurred.");
        }
    }


    public static ArrayList<Human> readFromFile(String fileName) throws FileNotFoundException{
        InputStreamReader reader;
        Gson gson = new Gson();

        reader = new InputStreamReader(new FileInputStream(fileName));

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
                if(listOfValues.size() != 9){
                    System.out.println("Incorrect file data");
                    return null;
                }
                String jsonText;
                if(listOfValues.get(1).equals("обычный житель")){
                    listOfValues.set(1, "NORMAL");
                } else if (listOfValues.get(1).equals("полицейский")){
                    listOfValues.set(1, "POLICE");
                } else if (listOfValues.get(1).equals("бандит")){
                    listOfValues.set(1, "BANDIT");
                }
                try {
                    jsonText = "{" +
                            "\"birthCertificateNumber\":\"" + listOfValues.get(0) + "\"," +
                            "\"type\":\"" + listOfValues.get(1) + "\"," +
                            "\"condition\":{" +
                            "\"publicAcceptance\":" +  listOfValues.get(2) + "," +
                            "\"state\":\"" + listOfValues.get(3) + "\"," +
                            "\"remainingTime\":" + listOfValues.get(4) + "}," +
                            "\"isAlive\":" + listOfValues.get(5) + "," +
                            "\"age\":" + listOfValues.get(6) + "," +
                            "\"sizeValue\":" + listOfValues.get(7) + "," +
                            "\"name\":\"" + listOfValues.get(8) + "\"" +
                            "}";

                    //System.out.println(jsonText);
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
