package src.parser.ast;

import src.library.Value;
import src.library.Variables;

public class VariableExpression implements Expression{
    private final String name;
    public VariableExpression(String name) {
        this.name = name;
    }
    public Value eval(){
        if (!Variables.isExists(name)) throw new RuntimeException("Constant does not exists");
        return Variables.get(name);
    }
    public String toString(){
       // return String.format("%S [%f]" , name , Constants.get(name));
    return String.format("%s", name);
    }
}
