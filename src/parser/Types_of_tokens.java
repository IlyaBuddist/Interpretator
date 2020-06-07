package src.parser;

public enum Types_of_tokens {

    DIGIT,//числа
    HEX_DIGIT,//16-ные числа
    WORD,//константы
    PRINT,//напечатать
    TEXT,//строка
    IF,//если
    ELSE,//
    WHILE,//
    FOR,//
    DO,
    BREAK,
    CONTINUE,
    USERFUNFTION,//функция пользователя
    RETURN,//

    OP_PLUS,//сложение
    OP_SUBTRACTION,//вычитание
    OP_MULTIPLY,//умножение
    OP_DIVIDE,//деление
    LEFT_BRACKET,//(
    RIGHT_BRACKET,//)
    LEFT_BRACE,//{
    RIGHT_BRACE,//}

    EXCLAMATION,//!
    EXCLAMATION_EQUALS,//!=
    EQUALS,//=
    EQUALS_EQUALS,//==
    LESSER,//<
    LESSER_EQUALS,//<=
    GREATER,//>
    GREATER_EQUALS,//>=

    BAR,//|
    BAR_BAR,//||
    AMP,//&
    AMP_AMP,//&&

    COMMA,//,
    END_OF_FILE//конец

}
