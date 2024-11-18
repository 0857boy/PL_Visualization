package cycuice.sourceAnalyzer.lexer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import cycuice.sourceAnalyzer.signal.EndOfLineSignal;
import cycuice.sourceAnalyzer.signal.StaticErrorSignal;

public class Lexer {
    private int currentCharLine;
    private int currentCharColumn;
    private Queue<Character> charQueue;  // 字元佇列，暫存該行原始碼字元

    public static boolean isAvailableChar(char ch) {
        return ch != ' ' && ch != '\n' && ch != '\t';
    } // isAvailableChar()
  
    private static boolean isTerminalChar(char ch) {
        return ch == ' ' || ch == '\n' || ch == '\t' || ch == '(' ||
               ch == ')' || ch == '\'' || ch == '\"';
    } // isTerminalChar()

    public Lexer() {
        this.currentCharLine = 1;
        this.currentCharColumn = 0;
        this.charQueue = new LinkedList<Character>();
    } // Lexer()

    /**
     * 將一段不含換行字元的字串切割成token串列，並儲存成分析結果回傳
     * 
     * @param lineOfSourceCode - 不含換行的字串
     * @return 分析結果
     */
    public ArrayList<TokenizeResult> tokenize(String lineOfSourceCode) {
        this.charQueue.clear();
        pushValidStringToCharQueue(lineOfSourceCode);

        ArrayList<TokenizeResult> results = new ArrayList<TokenizeResult>();
        try {
            while (!this.charQueue.isEmpty()) {
                Token token = getToken();
                results.add(TokenizeResult.createNormalResult(token));
            } // while
        } // try
        catch (EndOfLineSignal signal) {
        } // catch
        catch (StaticErrorSignal signal) {
            results.add(TokenizeResult.createErrorResult(signal.getErrorMessage()));
        } // catch
        
        this.currentCharLine += 1;
        this.currentCharColumn = 0;
        return results;
    } // tokenize()

    /**
     * 將字串中的字元，去除掉註解字元以及其後的字元後，依序存入字元佇列中
     * 
     * @param str - 欲存入不含換行的字串
     */
    private void pushValidStringToCharQueue(String str) {
        boolean isInString = false;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\"') {
                isInString = !isInString;
            } // if

            if (!isInString && str.charAt(i) == ';') {
                break;
            } // if

            this.charQueue.add(Character.valueOf(str.charAt(i)));
        } // for
    } // pushStringToCharQueue()

    private char peekNextChar() throws EndOfLineSignal {
        if (this.charQueue.isEmpty()) {
            throw new EndOfLineSignal();
        } // if
        
        return this.charQueue.peek().charValue();
    } // peekNextChar()

    private char getNextChar() throws EndOfLineSignal {
        if (this.charQueue.isEmpty()) {
            throw new EndOfLineSignal();
        } // if

        this.currentCharColumn += 1;
        return this.charQueue.poll().charValue();
    } // getNextChar()

    private char getNextAvailableChar() throws EndOfLineSignal {
        char ch;
        do {
            ch = getNextChar();
        } while (!isAvailableChar(ch));

        return ch;
    } // getNextAvailableChar()

    private Token getToken() throws StaticErrorSignal, EndOfLineSignal {
        char firstChar = getNextAvailableChar();
        int line = this.currentCharLine;
        int column = this.currentCharColumn;
        TokenType type = null;
        String tokenValue = null;
        if (firstChar == '\"') {
            tokenValue = getStringTokenValueExceptFirstQuote();
            type = TokenType.STRING;
        } // if
        else if (isTerminalChar(firstChar)) {
            tokenValue = String.valueOf(firstChar);
            type = recognizeTokenType(tokenValue);
        } // else if
        else {
            String leftTokenValue = "";
            try {
                char nextChar = peekNextChar();
                while (!isTerminalChar(nextChar)) {
                    getNextChar();
                    leftTokenValue += nextChar;
                    nextChar = peekNextChar();
                } // while
            } // try
            catch (EndOfLineSignal signal) {
            } // catch
            finally {
                tokenValue = firstChar + leftTokenValue;
                type = recognizeTokenType(tokenValue);
            } // finally
        } // else

        return new Token(type, tokenValue, line, column);
    } // getToken()

    private String getCharAfterBackslash() throws EndOfLineSignal {
        char ch = getNextChar();
        if ( ch == 'n' ) {
            return "\n";
        } // if
        else if ( ch == 't' ) {
            return "\t";
        } // else if
        else if ( ch == '\"' ) {
            return "\"";
        } // else if
        else if ( ch == '\\' ) {
            return "\\";
        } // else if
        else {
            return "\\" + ch;
        } // else
    } // getCharAfterBackslash()

    private String getStringTokenValueExceptFirstQuote() throws StaticErrorSignal {
        String stringValue = "";
        char ch;
        try {
            do {
                ch = getNextChar();
                if (ch == '\\') {
                    stringValue += getCharAfterBackslash();
                } // if
                else {
                    if (ch != '\"') {
                        stringValue += ch;
                    } // if
                } // else
            } while (ch != '\"');
        } // try
        catch (EndOfLineSignal signal) {
            String errorMessage = "ERROR (no closing quote) : END-OF-LINE encountered at Line " +
                                  this.currentCharLine + " Column " + (this.currentCharColumn + 1) + "\n";
            throw new StaticErrorSignal(errorMessage);
            //throw new StaticErrorSignal(errorMessage);
        } // catch

        return stringValue;
    } // getStringTokenValueExceptFirstQuote()

    /**
     * 依據token的字串值，判斷其token類型(string除外)
     * 
     * @param tokenValue - 欲判斷的token字串值
     * @return token類型
     */
    private TokenType recognizeTokenType(String tokenValue) {
        if ( tokenValue.matches( "\\(" ) ) {
            return TokenType.LEFT_PAREN;
        } // if
        else if ( tokenValue.matches( "\\)" ) ) {
            return TokenType.RIGHT_PAREN;
        } // else if
        else if ( tokenValue.matches( "\\." ) ) {
            return TokenType.DOT;
        } // else if
        else if ( tokenValue.matches( "\'" ) ) {
            return TokenType.QUOTE;
        } // else if
        else if ( tokenValue.matches( "nil|#f" ) ) {
            return TokenType.NIL;
        } // else if
        else if ( tokenValue.matches( "t|#t" ) ) {
            return TokenType.T;
        } // else if
        else if ( tokenValue.matches( "[+-]?\\d+" ) ) {  // integer regex
            return TokenType.INT;
        } // else if
        else if ( tokenValue.matches( "[+-]?[0-9]+\\.[0-9]*|[+-]?[0-9]*\\.[0-9]+" ) ) {  // float regex
            return TokenType.FLOAT;
        } // else if
        else {
            return TokenType.SYMBOL;
        } // else
    } // recognizeTokenType()
} // class Lexer
