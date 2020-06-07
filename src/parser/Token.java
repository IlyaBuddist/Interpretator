package src.parser;

public final class Token {
    private Types_of_tokens type;
    private String text;//хранит имя переменной
    public Token(){

    }
    public Token(Types_of_tokens type, String text ){
        this.type = type;
        this.text = text;

    }

    public Types_of_tokens getType() {
        return type;
    }

    public void setType(Types_of_tokens type) {

        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {

        this.text = text;
    }

    public String toString(){
        return type +" " + text ;
    }
}
