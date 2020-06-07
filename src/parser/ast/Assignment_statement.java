package src.parser.ast;

import src.library.Value;
import src.library.Variables;

public class Assignment_statement implements Statement {
    private final String variable;
    private  final Expression expression;

    public Assignment_statement(String variable, Expression expression){
        this.variable = variable;
        this.expression = expression;

    }
    @Override
    public void execute() {
        final Value result = expression.eval();
        Variables.set(variable, result);
    }

    @Override
    public String toString() {
        return String.format("%s = %s",variable, expression);
    }
}
