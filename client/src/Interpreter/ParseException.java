package Interpreter;

import java.lang.Throwable;

class ParseException extends Throwable{ 
    public ParseException(String message){
        super(message);
    }
    
}
