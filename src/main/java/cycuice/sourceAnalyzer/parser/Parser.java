package cycuice.sourceAnalyzer.parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Queue;

import cycuice.sourceAnalyzer.common.Indexable;
import cycuice.sourceAnalyzer.lexer.Token;
import cycuice.sourceAnalyzer.lexer.TokenType;
import cycuice.sourceAnalyzer.lexer.TokenizeResult;
import cycuice.sourceAnalyzer.signal.StaticErrorSignal;
import cycuice.sourceAnalyzer.stree.STree;

/**
 * 利用LL(1)文法分析器進行文法剖析，並產生S-Tree
 * 
 * LL(1)文法如下:
 * <SEXP_0> ::= <ATOM>
 *              | LEFT-PAREN <SEXP_1>
 *              | QUOTE SEXP_0
 * 
 * <SEXP_1> ::= <SEXP_0> <SEXP_2> <SEXP_3> RIGHT-PAREN
 *              | RIGHT-PAREN
 * 
 * <SEXP_2> ::= <SEXP_0> <SEXP_2>
 *              | ε
 * 
 * <SEXP_3> ::= DOT SEXP_0
 *              | ε
 *  
 * <ATOM> ::= INT
 *            | FLOAT
 *            | STRING
 *            | NIL
 *            | T
 *            | SYMBOL
 */
public class Parser {
    private static enum SyntaxNodeType implements Indexable {
        TERMINAL(5),
        SEXP_0(0),
        SEXP_1(1),
        SEXP_2(2),
        SEXP_3(3),
        ATOM(4),
        NULL(6);

        private int indexOfType;

        private SyntaxNodeType(int indexOfType) {
            this.indexOfType = indexOfType;
        } // SyntaxNodeType()

        @Override
        public int getIndex() {
            return this.indexOfType;
        } // getIndex()
    } // enum SyntaxNodeType

    private static class SyntaxNode {
        private SyntaxNodeType type;
        private TokenType terminalType;

        /**
         * 建立一個非終端文法節點
         */
        public SyntaxNode(SyntaxNodeType type) {
            this.type = type;
            this.terminalType = null;
        } // SyntaxNode()

        /**
         * 建立一個終端文法節點(即某個token)
         */
        public SyntaxNode(TokenType terminalType) {
            this.type = SyntaxNodeType.TERMINAL;
            this.terminalType = terminalType;
        } // SyntaxNode()

        public SyntaxNodeType getSyntaxNodeType() {
            return this.type;
        } // getSyntaxNodeType()

        public TokenType getTerminalType() {
            return this.terminalType;
        } // getTerminalType()
    } // class SyntaxNode

    /**
     * LL(1)文法分析表，表格內容如下(空值放-1):
     *        | ( | ) | ' | . | symbol | int | float | string | nil |  t  |
     * SEXP_0 | 1 |   | 2 |   |   0    |  0  |   0   |   0    |  0  |  0  |
     * SEXP_1 | 3 | 4 | 3 |   |   3    |  3  |   3   |   3    |  3  |  3  |
     * SEXP_2 | 5 | 6 | 5 | 6 |   5    |  5  |   5   |   5    |  5  |  5  |
     * SEXP_3 | 6 | 6 | 6 | 7 |   6    |  6  |   6   |   6    |  6  |  6  |
     * ATOM   |   |   |   |   |   8    |  9  |   10  |   11   |  12 |  13 |
     */
    private static final int[][] parsingTable;

    /**
     * 文法列表(其index意義為文法分析表內所存放數值)
     * 0 : ATOM
     * 1 : ( SEXP_1
     * 2 : ' SEXP_0
     * 3 : SEXP_0 SEXP_2 SEXP_3 )
     * 4 : )
     * 5 : SEXP_0 SEXP_2
     * 6 : ε
     * 7 : . SEXP_0
     * 8 : SYMBOL
     * 9 : INT
     * 10: FLOAT
     * 11: STRING
     * 12: NIL
     * 13: T
     */
    private static final SyntaxNode[][] syntaxList;  // 對應文法列表
    
    static {
        parsingTable = new int[][]{
            {1, -1, 2, -1, 0, 0, 0, 0, 0, 0},
            {3, 4, 3, -1, 3, 3, 3, 3, 3, 3},
            {5, 6, 5, 6, 5, 5, 5, 5, 5, 5},
            {6, 6, 6, 7, 6, 6, 6, 6, 6, 6},
            {-1, -1, -1, -1, 8, 9, 10, 11, 12, 13}
        };
        syntaxList = new SyntaxNode[][]{
            {new SyntaxNode(SyntaxNodeType.ATOM)},
            {new SyntaxNode(TokenType.LEFT_PAREN), new SyntaxNode(SyntaxNodeType.SEXP_1)},
            {new SyntaxNode(TokenType.QUOTE), new SyntaxNode(SyntaxNodeType.SEXP_0)},
            {new SyntaxNode(SyntaxNodeType.SEXP_0), new SyntaxNode(SyntaxNodeType.SEXP_2), new SyntaxNode(SyntaxNodeType.SEXP_3), new SyntaxNode(TokenType.RIGHT_PAREN)},
            {new SyntaxNode(TokenType.RIGHT_PAREN)},
            {new SyntaxNode(SyntaxNodeType.SEXP_0), new SyntaxNode(SyntaxNodeType.SEXP_2)},
            {new SyntaxNode(SyntaxNodeType.NULL)},
            {new SyntaxNode(TokenType.DOT), new SyntaxNode(SyntaxNodeType.SEXP_0)},
            {new SyntaxNode(TokenType.SYMBOL)},
            {new SyntaxNode(TokenType.INT)},
            {new SyntaxNode(TokenType.FLOAT)},
            {new SyntaxNode(TokenType.STRING)},
            {new SyntaxNode(TokenType.NIL)},
            {new SyntaxNode(TokenType.T)}
        };
    } // static

    private Queue<TokenizeResult> tokenizeResultQueue;  // tokenize result佇列，暫存該行待分析的lexer分析結果
    private Stack<SyntaxNode> syntaxStack;  // 文法分析時的動態堆疊
    private ArrayList<Token> grammarCheckedTokenBuffer;  // 已通過文法分析的token串暫存

    private static int findParsingTable(int rowIndex, int columnIndex) {
        return parsingTable[rowIndex][columnIndex];
    } // findParsingTable()

    public Parser() {
        this.tokenizeResultQueue = new LinkedList<TokenizeResult>();
        this.syntaxStack = new Stack<SyntaxNode>();
        this.grammarCheckedTokenBuffer = new ArrayList<Token>();
    } // Parser()

    /**
     * 以一連串token進行文法分析，生成對應S-Tree回傳
     */
    public ArrayList<ParseResult> parse(ArrayList<TokenizeResult> tokenizeResultList) {
        this.tokenizeResultQueue.clear();
        pushTokenizeResultsToQueue(tokenizeResultList);  // 將tokenize結果推入token佇列

        ArrayList<ParseResult> results = new ArrayList<ParseResult>();
        try {
            boolean isIncomplete = false;
            while (!this.tokenizeResultQueue.isEmpty()) {
                isIncomplete = true;
                if (this.syntaxStack.empty()) {  // 剛進入parse迴圈時，若為一個新的S-Exp分析循環，則先推入SEXP_0
                    this.syntaxStack.push(new SyntaxNode(SyntaxNodeType.SEXP_0));
                } // if

                verifyNextToken();  // 驗證下一個token是否符合文法
                if (this.syntaxStack.empty()) {  // 剛驗證完一個token時，若stack為空，表示文法正確，可生成S-Tree
                    isIncomplete = false;
                    results.add(ParseResult.createCompleteResult(STree.createSTree(this.grammarCheckedTokenBuffer)));
                    this.grammarCheckedTokenBuffer.clear();
                } // if
            } // while

            if (isIncomplete) {
                results.add(ParseResult.createIncompleteResult());
            } // if
        } // try
        catch (StaticErrorSignal signal) {
            results.add(ParseResult.createErrorResult(signal.getErrorMessage()));
            reset();
        } // catch

        return results;
    } // parse()

    /**
     * 重置parser分析狀態，即把暫存tokenize results、stack、token buffer清空
     */
    private void reset() {
        this.tokenizeResultQueue.clear();
        this.syntaxStack.clear();
        this.grammarCheckedTokenBuffer.clear();
    } // reset()

    private void pushTokenizeResultsToQueue(ArrayList<TokenizeResult> tokenizeResultList) {
        for (TokenizeResult tokenizeResult : tokenizeResultList) {
            this.tokenizeResultQueue.add(tokenizeResult);
        } // for
    } // pushTokenizeResultsToQueue()

    private void pushSyntaxInStack(SyntaxNode[] syntaxArrayToPush) {
        for (int i = syntaxArrayToPush.length - 1; i >= 0; i--) {
            this.syntaxStack.push(syntaxArrayToPush[i]);
        } // for
    } // pushSyntaxInStack()

    private Token getNextToken() throws StaticErrorSignal {
        TokenizeResult nexTokenizeResult = this.tokenizeResultQueue.remove();
        if (nexTokenizeResult.isError()) {
            throw new StaticErrorSignal(nexTokenizeResult.getErrorMessage());
        } // if

        return nexTokenizeResult.getResultToken();
    } // getNextToken()

    /**
     * 驗證下一個lexer分析結果的token是否符合文法，若符合則將該token推入token buffer
     * 若下個lexer分析結果為error、或下個token不符合文法，則拋出StaticErrorSignal(文法錯誤信號)
     * 分析步驟:
     * 從syntax stack上層pop一個接下來的應得文法
     * 若接下來應得文法為非終端文法，則查表檢查是否正確，若正確則push適當的文法內容後，重複上一步的檢查步驟
     * 若接下來應得文法為終端文法，則檢查與當前token是否匹配，匹配則將token存入暫存
     * 過程中，若發現不匹配或查表值為-1則拋出文法錯誤例外
     */
    private void verifyNextToken() throws StaticErrorSignal {
        Token nextToken = getNextToken();
        boolean eatToken = false;
        while (!eatToken) {  // 若token還沒被吃掉(讀掉)，就繼續
            SyntaxNode nextSyntaxNode = this.syntaxStack.pop();
            if (nextSyntaxNode.type == SyntaxNodeType.TERMINAL) {  // 下一個應得文法為終端文法
                if (nextToken.getType() == nextSyntaxNode.getTerminalType()) {  // 匹配
                    this.grammarCheckedTokenBuffer.add(nextToken);
                    eatToken = true;
                } // if
                else {                                                          // 不匹配，文法錯誤
                    if (nextSyntaxNode.getTerminalType() == TokenType.RIGHT_PAREN) {  // 期望下個token是右括號
                        String errorMessage = "ERROR (unexpected token) : ')' expected when token at Line " +
                                              nextToken.getLine() + " Column " + nextToken.getColumn() + " is >>" +
                                              nextToken.getValue() + "<<";
                        throw new StaticErrorSignal(errorMessage);
                    } // if
                    else {                                                            // 期望下個token是atom或左括號
                        String errorMessage = "ERROR (unexpected token) : atom or '(' expected when token at Line " +
                                              nextToken.getLine() + " Column " + nextToken.getColumn() + " is >>" +
                                              nextToken.getValue() + "<<";
                        throw new StaticErrorSignal(errorMessage);
                    } // else
                } // else
            } // if
            else if (nextSyntaxNode.type != SyntaxNodeType.NULL) {  // 下一個應得文法為非終端、且非空文法
                int syntaxIndex = findParsingTable(nextSyntaxNode.getSyntaxNodeType().getIndex(), nextToken.getType().getIndex());
                if (syntaxIndex == -1) {  // 文法錯誤
                    String errorMessage = "ERROR (unexpected token) : atom or '(' expected when token at Line " +
                                          nextToken.getLine() + " Column " + nextToken.getColumn() + " is >>" +
                                          nextToken.getValue() + "<<";
                    throw new StaticErrorSignal(errorMessage);
                } // if

                SyntaxNode[] syntaxArrayToPush = syntaxList[syntaxIndex];
                pushSyntaxInStack(syntaxArrayToPush) ;
            } // else if
        } // while
    } // verifyNextToken()
} // class Parser
