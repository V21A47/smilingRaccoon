package Interpreter;

import java.io.InputStreamReader;
import java.lang.StringBuilder;
import java.io.IOException;

public class Reader{
    private boolean shouldWork = true;

    private Executor executor;
    public Reader(Executor executor){
        this.executor = executor;
    }

    public void go() throws IOException{
        InputStreamReader reader = new InputStreamReader(System.in);
        StringBuilder string = new StringBuilder();

        Parser parser = new Parser(executor);

        int bracketsAmount = 0;

            while(shouldWork){
                System.out.print("> ");
                int symbolNum = 0;
                while(symbolNum != -1){
                    symbolNum = reader.read();

                    if( (char)(symbolNum) == '{' ){
                        bracketsAmount += 1;
                    } else if( (char)(symbolNum) == '}'){
                        bracketsAmount -= 1;
                    }

                    if( (char)(symbolNum) != '\n' ){
                        string.append((char)(symbolNum));
                    }

                    if( (char)(symbolNum) == '\n' && bracketsAmount <= 0){

                        try{
                            parser.parse(string.toString());
                        } catch (ParseException e){
                            System.err.println(e.getMessage());
                        }

                        System.out.print("> ");
                        string.setLength(0);
                    }
                }
                shouldWork = false;
            }
    }
}
