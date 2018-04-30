package Interpreter;

import java.io.InputStreamReader;
import java.lang.StringBuilder;
import java.io.IOException;

public class Interpreter{
    private boolean shouldWork = true;
    
    public void go() throws IOException{
    InputStreamReader reader = new InputStreamReader(System.in);
    StringBuilder string = new StringBuilder();
    
        while(shouldWork){
            System.out.print("> ");
            int symbolNum = 0;
            while(symbolNum != -1){
                symbolNum = reader.read();
                string.append((char)(symbolNum));
                if( (char)(symbolNum) == '\n' ){
                    System.out.println(string.toString());
                    System.out.print("> ");
                    string.setLength(0);
                }
            }
            shouldWork = false;
        }
    }
}
