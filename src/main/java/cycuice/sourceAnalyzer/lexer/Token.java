package cycuice.sourceAnalyzer.lexer;

public class Token {
    private TokenType type;
    private String value;
    private int line;
    private int column;

    public Token(TokenType type, String value, int line, int column) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.column = column;
    } // Token()

    public TokenType getType() {
        return type;
    } // getType()

    public String getValue() {
        return value;
    } // getValue()

    public int getLine() {
        return line;
    } // getLine()

    public int getColumn() {
        return column;
    } // getColumn()
} // class Token
