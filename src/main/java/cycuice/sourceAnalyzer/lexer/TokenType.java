package cycuice.sourceAnalyzer.lexer;

import cycuice.sourceAnalyzer.common.Indexable;
import cycuice.sourceAnalyzer.common.Nameable;

public enum TokenType implements Indexable, Nameable {
    LEFT_PAREN(0, "left-parenthesis"),
    RIGHT_PAREN(1, "right-parenthesis"),
    INT(5, "integer"),
    FLOAT(6, "float"),
    STRING(7, "string"),
    DOT(3, "dot"),
    NIL(8, "nil"),
    T(9, "t"),
    QUOTE(2, "quote"),
    SYMBOL(4, "symbol");

    private int indexOfType;
    private String nameOfType;

    private TokenType(int indexOfType, String nameOfType) {
        this.indexOfType = indexOfType;
        this.nameOfType = nameOfType;
    } // TokenType()

    @Override
    public int getIndex() {
        return this.indexOfType;
    } // getIndex()

    @Override
    public String getName() {
        return this.nameOfType;
    } // getName()
} // class TokenType
