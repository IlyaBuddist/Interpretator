package src.parser.ast;

import src.library.Functions;

import java.util.List;

public class FunctionDefine implements Statement {


    private final String name;
    private final List<String> argNames;
    private final Statement body;

    public FunctionDefine(String name, List<String> argNames, Statement body){
        this.name = name;
        this.argNames = argNames;
        this.body = body;
    }

    @Override
    public void execute() {
        Functions.set(name,new UserDefinedFunction(argNames,body));
    }

    @Override
    public String toString() {
        return "userfunction (" + argNames.toString() +
                ") "  + body.toString();
    }
}
