package cycuice.sourceAnalyzer.parser;

import cycuice.sourceAnalyzer.stree.STree;

public class ParseResult {
    private boolean isComplete;   // 是否分析完成(完成指一段完整文法的結束、或者遇到錯誤)
    private boolean isError;      // 是否有錯誤
    private STree resultTree;     // parser分析後的S-Tree
    private String errorMessage;  // 錯誤訊息

    public static ParseResult createCompleteResult(STree resultTree) {
        return new ParseResult(true, false, resultTree, null);
    } // createCompleteResult()

    public static ParseResult createIncompleteResult() {
        return new ParseResult(false, false, null, null);
    } // createIncompleteResult()

    public static ParseResult createErrorResult(String errorMessage) {
        return new ParseResult(true, true, null, errorMessage);
    } // createErrorResult()

    private ParseResult(boolean isComplete, boolean isError, STree resultTree, String errorMessage) {
        this.isComplete = isComplete;
        this.isError = isError;
        this.resultTree = resultTree;
        this.errorMessage = errorMessage;
    } // ParseResult()

    public boolean isComplete() {
        return isComplete;
    } // isComplete()

    public boolean isError() {
        return isError;
    } // isError()

    public String getErrorMessage() {
        return errorMessage;
    } // getErrorMessage()

    public STree getResultTree() {
        return resultTree;
    } // getResultTree()
} // class ParseResult
