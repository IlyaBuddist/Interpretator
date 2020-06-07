package src.parser;

import src.parser.ast.*;

import java.util.ArrayList;
import java.util.List;

public final class Parser {
    private static final Token EOF = new Token (Types_of_tokens.END_OF_FILE,"");
private final List<Token> tokens;//список токенов
private final int size;
private int pos;

public Parser(List<Token> tokens){
    this.tokens = tokens;
    size = tokens.size();
}
    public Statement parse(){
        final BlockStatement result = new BlockStatement();
        while (!match(Types_of_tokens.END_OF_FILE)){
            result.add(statement());//добав выражение
        }
        return result;
    }
private Statement block(){
    final BlockStatement block = new BlockStatement();
    consume(Types_of_tokens.LEFT_BRACE);
    while (!match(Types_of_tokens.RIGHT_BRACE)){
        block.add(statement());//добав выражение
    }
    return block;
}
private Statement statementOrBlock(){
    if(get(0).getType() == Types_of_tokens.LEFT_BRACE) return block();
    return statement();
}
private Statement statement(){
    if (match(Types_of_tokens.PRINT)){
        return new PrintStatement(expression());
    }
    if(match(Types_of_tokens.IF)){
        return ifElse();
    }
    if (match(Types_of_tokens.WHILE)){
        return whileStatement();
    }
    if (match(Types_of_tokens.DO)){
        return doWhileStatement();
    }
    if (match(Types_of_tokens.BREAK)){
        return new BreakStatement();
    }
    if (match(Types_of_tokens.CONTINUE)){
        return new ContinueStatement();
    }
    if (match(Types_of_tokens.RETURN)){
        return new ReturnStatement(expression());
    }
    if (match(Types_of_tokens.FOR)){
        return forStatement();
    }
    if (match(Types_of_tokens.USERFUNFTION)){
        return functionDefine();
    }

    if (get(0).getType() == Types_of_tokens.WORD && get(1).getType() == Types_of_tokens.LEFT_BRACKET){
        return new FunctionStatement(function());
    }
    return assignmentStatement();
}

private Statement assignmentStatement() {
        final Token current = get(0);
        if(match(Types_of_tokens.WORD) && get(0).getType() == Types_of_tokens.EQUALS){
            final String variable = current.getText();
            consume(Types_of_tokens.EQUALS);
            return new Assignment_statement(variable, expression());
        }
    throw new RuntimeException("unknown statement");
}

private Statement ifElse() {
    final Expression condition = expression();//лог выражения
    final Statement ifStatement= statementOrBlock();
    final Statement elseStatement;
    if(match(Types_of_tokens.ELSE)){
        elseStatement = statementOrBlock();
    } else {
        elseStatement = null;
    }
     return new IfStatement(condition,ifStatement, elseStatement);
    }
private Statement whileStatement(){


    final Expression condition = expression();
    final Statement statement= statementOrBlock();
    return new WhileStatement(condition,statement);
}
private Statement doWhileStatement(){

    final Statement statement= statementOrBlock();
    consume(Types_of_tokens.WHILE);
    final Expression condition = expression();
        return new DoWhileStatement(condition,statement);
}
private  Statement forStatement(){
    final Statement initialization = assignmentStatement();
    consume(Types_of_tokens.COMMA);
    final Expression termination = expression();
    consume(Types_of_tokens.COMMA);
    final Statement increment = assignmentStatement();
    final Statement statement = statementOrBlock();
    return new ForStatement(initialization,termination,increment,statement);

}

    private FunctionDefine functionDefine(){
        final String name = consume(Types_of_tokens.WORD).getText();
        consume(Types_of_tokens.LEFT_BRACKET);
        final List<String> argNames = new ArrayList<>();
        while(!match(Types_of_tokens.RIGHT_BRACKET)){
            argNames.add(consume(Types_of_tokens.WORD).getText());
            match(Types_of_tokens.COMMA);
        }
        final Statement body = statementOrBlock();
        return new FunctionDefine(name, argNames, body);
    }
    private FunctionalExpression function(){
        final String name = consume(Types_of_tokens.WORD).getText();
        consume(Types_of_tokens.LEFT_BRACKET);
        final FunctionalExpression function = new FunctionalExpression(name);
        while(!match(Types_of_tokens.RIGHT_BRACKET)){
            function.addArgument(expression());
            match(Types_of_tokens.COMMA);
        }
        return function;
    }
private Expression expression(){
        return logicalOr();
    }

private Expression logicalOr(){
    Expression result = logicalAnd();
    while (true) {

        if (match(Types_of_tokens.BAR_BAR)) {
            result = new ConditionalExpression(ConditionalExpression.Operator.OR,result,logicalAnd());
            continue;
        }
        break;
    }
    return result;
}
private Expression logicalAnd(){
    Expression result = equality();
    while (true) {

        if (match(Types_of_tokens.AMP_AMP)) {
            result = new ConditionalExpression(ConditionalExpression.Operator.AND,result,equality());
            continue;
        }
        break;
    }
    return result;
}
private  Expression equality(){
    Expression result = conditional();
    if (match(Types_of_tokens.EQUALS_EQUALS)){
        return new ConditionalExpression(ConditionalExpression.Operator.EQUALS,result,conditional());

    }
    if (match(Types_of_tokens.EXCLAMATION_EQUALS)){
        return new ConditionalExpression(ConditionalExpression.Operator.NOT_EQUALS,result,conditional());
    }
    return result;
}
private  Expression conditional(){
    Expression result = additive();
    while (true){

        if (match(Types_of_tokens.LESSER)){
            result = new ConditionalExpression(ConditionalExpression.Operator.LT,result,additive());
            continue;
        }
        if (match(Types_of_tokens.LESSER_EQUALS)){
            result = new ConditionalExpression(ConditionalExpression.Operator.LTEQ,result,additive());
            continue;
        }
        if (match(Types_of_tokens.GREATER)){
            result = new ConditionalExpression(ConditionalExpression.Operator.GT,result,additive());
            continue;
        }
        if (match(Types_of_tokens.GREATER_EQUALS)){
            result = new ConditionalExpression(ConditionalExpression.Operator.GTEQ,result,additive());
            continue;
        }
        break;
    }
    return result;
}
private Expression additive() {
    Expression result = multiplicative();
    while (true){
        if (match(Types_of_tokens.OP_PLUS)){
            result = new BinaryExpression('+',result,multiplicative());
            continue;
        }
        if (match(Types_of_tokens.OP_SUBTRACTION)){
            result = new BinaryExpression('-',result,multiplicative());
            continue;
        }
        break;
    }
return result;
    }
private Expression multiplicative(){
    Expression result = unary();
    while (true){
        if (match(Types_of_tokens.OP_MULTIPLY)){
            result = new BinaryExpression('*',result,unary());
       continue;
        }
        if (match(Types_of_tokens.OP_DIVIDE)){
           result = new BinaryExpression('/',result,unary());
        continue;
        }
        break;
    }
return result;
}
private Expression unary(){
    if (match(Types_of_tokens.OP_SUBTRACTION)){
        return new UnaryExpression('-', primary());
    }
    if (match(Types_of_tokens.OP_PLUS)){
        return primary();
    }

return primary();
}
private Expression primary(){
final Token current = get(0);
if (match(Types_of_tokens.DIGIT)){
    return new ValueExpression(Double.parseDouble (current.getText()));
}
    if (match(Types_of_tokens.HEX_DIGIT)){
        return new ValueExpression(Long.parseLong (current.getText(), 16));
    }
    if (get(0).getType() == Types_of_tokens.WORD && get(1).getType() == Types_of_tokens.LEFT_BRACKET){
        return function();
    }
    if (match(Types_of_tokens.WORD)){
        return new VariableExpression(current.getText());
    }
    if (match(Types_of_tokens.TEXT)){
        return new ValueExpression(current.getText());
    }
if (match(Types_of_tokens.LEFT_BRACKET)){
    Expression result = expression();
    match(Types_of_tokens.RIGHT_BRACKET);
    return result;
}
throw new RuntimeException("Unknown expression");
}
    private Token consume(Types_of_tokens type){//проверка типа токена
        final Token current = get(0);
        if (type != current.getType()) throw new RuntimeException("src.parser.Token " + current + "doesn't match" + type);
        pos++;
        return current;
    }
private boolean match(Types_of_tokens type){//проверка типа токена
    final Token current = get(0);
    if (type != current.getType()) return false;
    pos++;
    return true;
}
    private Token get(int relativePosition){//получение тек символа
        final int position = pos + relativePosition;
        if (position >= size)
            return EOF;
        return tokens.get(position);
    }

}
