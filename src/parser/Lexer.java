package src.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Lexer {
    private static final String OPERATOR_CHARS = "+-*/(){}=<>!&|,";//симолы строки

    private static final Map<String, Types_of_tokens> OPERATORS;
    static {
        OPERATORS = new HashMap<>();
        OPERATORS.put("+", Types_of_tokens.OP_PLUS);
        OPERATORS.put("-", Types_of_tokens.OP_SUBTRACTION);
        OPERATORS.put("*", Types_of_tokens.OP_MULTIPLY);
        OPERATORS.put("/", Types_of_tokens.OP_DIVIDE);
        OPERATORS.put("(", Types_of_tokens.LEFT_BRACKET);
        OPERATORS.put(")", Types_of_tokens.RIGHT_BRACKET);
        OPERATORS.put("{", Types_of_tokens.LEFT_BRACE);
        OPERATORS.put("}", Types_of_tokens.RIGHT_BRACE);
        OPERATORS.put("=", Types_of_tokens.EQUALS);
        OPERATORS.put("<", Types_of_tokens.LESSER);
        OPERATORS.put(">", Types_of_tokens.GREATER);
        OPERATORS.put(",", Types_of_tokens.COMMA);
        OPERATORS.put("!", Types_of_tokens.EXCLAMATION);
        OPERATORS.put("&", Types_of_tokens.AMP);
        OPERATORS.put("|", Types_of_tokens.BAR);
        OPERATORS.put("==", Types_of_tokens.EQUALS_EQUALS);
        OPERATORS.put("!=", Types_of_tokens.EXCLAMATION_EQUALS);
        OPERATORS.put("<=", Types_of_tokens.LESSER_EQUALS);
        OPERATORS.put(">=", Types_of_tokens.GREATER_EQUALS);
        OPERATORS.put("&&", Types_of_tokens.AMP_AMP);
        OPERATORS.put("||", Types_of_tokens.BAR_BAR);
    }
    private final String input;
    private final int length;//длина строки
    private List<Token> tokens;//список токенов
    private int pos;

    public Lexer(String input) {//конструктор сохраняет строку(input) в поле
        this.input = input;
        length = input.length();
        tokens = new ArrayList<>();
    }

    public List<Token> tokenize() {//возвращает список из токенов
        while (pos < length) {
            final char current = peek(0);//текущий символ
            if (Character.isDigit(current)) tokenizeNumber();//проверка число
            else if (Character.isLetter(current)) tokenizeWord();
            else if (current == '#') {
                next();
                tokenizeHexNumber();
            }
            else if (current == '"') {
                tokenizeText();
            }
            else if (OPERATOR_CHARS.indexOf(current) != -1) {//проверка символ
                tokenizeOperator();
            } else {
                next();
            }

        }
        return tokens;
    }

    private void tokenizeNumber() {
        final StringBuilder buffer = new StringBuilder();//буффер
        char current = peek(0);//текущий символ
        while (true) {//
            if (current == '.') {
                if (buffer.indexOf(".") != -1) throw new RuntimeException("Invalid float number");
            } else if (!Character.isDigit(current)) {
                break;
            }
            buffer.append(current);//добавляем в буффер тек  символ
            current = next();//след символ

        }
        addToken(Types_of_tokens.DIGIT, buffer.toString());//добавляем число и его текст
    }

    private void tokenizeHexNumber() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (Character.isDigit(current) || isHexNumber(current)) {
            buffer.append(current);
            current = next();

        }
        addToken(Types_of_tokens.HEX_DIGIT, buffer.toString());
    }

    private static boolean isHexNumber(char current) {
        return "abcdef".indexOf(Character.toLowerCase(current)) != -1;
    }

    private void tokenizeOperator() {
        char current = peek(0);//текущий символ
        if (current == '/'){
            if (peek(1)=='/'){
                next();
                next();
                tokenizeComment();
                return;
            } else if (peek(1)=='*'){
                next();
                next();
                tokenizeMultilineComment();
                return;
            }
        }
        final StringBuilder buffer = new StringBuilder();
        while (true){
            final String text = buffer.toString();
            if(!OPERATORS.containsKey(text + current) && !text.isEmpty()){
               addToken(OPERATORS.get(text));
               return;
            }
            buffer.append(current);
            current = next();
        }
    }

    private void tokenizeWord() {
        final StringBuilder buffer = new StringBuilder();//буффер
        char current = peek(0);//текущий символ
        while (true) {//пока идут числовые символы они добавляются в буффер

            if (!Character.isLetterOrDigit(current) && (current != '_') && (current != '$')) {
                break;
            }
            buffer.append(current);//добавляем в буффер тек  символ
            current = next();//след символ
        }

        final String word = buffer.toString();
        switch (word){
            case "print": addToken(Types_of_tokens.PRINT); break;
            case "if":    addToken(Types_of_tokens.IF); break;
            case "else":    addToken(Types_of_tokens.ELSE); break;
            case "while":   addToken(Types_of_tokens.WHILE);break;
            case "for":   addToken(Types_of_tokens.FOR);break;
            case "do":   addToken(Types_of_tokens.DO);break;
            case "break":   addToken(Types_of_tokens.BREAK);break;
            case "continue":addToken(Types_of_tokens.CONTINUE);break;
            case "function":addToken(Types_of_tokens.USERFUNFTION);break;
            case "return":addToken(Types_of_tokens.RETURN);break;
            default:
                addToken(Types_of_tokens.WORD, word);
                break;
        }

    }
    private void tokenizeText() {
        next();//пропуск одной "
        final StringBuilder buffer = new StringBuilder();//буффер
        char current = peek(0);//текущий символ
        while (true) {//пока идут числовые символы они добавляются в буффер
            if (current == '\\'){
                current = next();
                switch (current){
                    case '"': current = next(); buffer.append('"');continue;
                    case 'n': current = next(); buffer.append('\n');continue;
                    case 't': current = next(); buffer.append('\t');continue;
                }
                buffer.append('\\');
                continue;
            }
            if ( current == '"') {//условие выхода
                break;
            }
            buffer.append(current);//добавляем в буффер тек  символ
            current = next();//след символ
        }
        next();//пропуск закрывающей кавычки
        addToken(Types_of_tokens.TEXT, buffer.toString());

    }
private void tokenizeComment(){
     char current = peek(0);
     while("\r\n\0".indexOf(current) == -1){
         current = next();
     }
}
private void tokenizeMultilineComment(){
    char current = peek(0);
    while(true){
        if(current == '\0') throw new RuntimeException("Missing close tag");
        if(current == '*' && peek(1) == '/') break;
        current = next();
    }
    next();
    next();
}
private char next(){//увеличываем позицию и возвр тек символ
       pos++;
       return peek(0);
    }
private char peek(int relativePosition){//для просмотра символов
final int position = pos + relativePosition;//берем символ след после тек
if (position >= length)
    return '\0';
return input.charAt(position);
  }
private void addToken(Types_of_tokens type){
    addToken(type, "");
}//добавление токенов по типу(для операций)
private void addToken(Types_of_tokens type, String text){
    tokens.add(new Token(type,text));
}//добавление токенов по типу(для чисел)
}
