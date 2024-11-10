package cycuice.sourceAnalyzer;

import cycuice.sourceAnalyzer.common.PrettyPrintable;
import cycuice.sourceAnalyzer.stree.STree;

/**
 * 每個分析的結果，若單次分析未完成，isComplete將為false，並且resultMessage及resultTree為null
 */
public class AnalysisResult {
    private boolean isComplete;    // 表示是否分析完成(完成指一段完整文法的結束、或者遇到錯誤)
    private boolean isError;       // 此次分析是否遇到our sheme的錯誤(文法錯誤或執行錯誤)
    private STree resultTree;      // 此次分析的結果樹
    private String resultMessage;  // 此次分析的結果訊息，可能為pretty-print、或錯誤訊息

    public static AnalysisResult createCompleteResult(STree resultTree) {
        PrettyPrintable prettyPrintableObject = resultTree;
        return new AnalysisResult(true, false, resultTree, prettyPrintableObject.getPrettyPrintString());
    } // createCompleteResult()

    public static AnalysisResult createIncompleteResult() {
        return new AnalysisResult(false, false, null, null);
    } // createIncompleteResult()

    public static AnalysisResult createErrorResult(String errorMessage) {
        return new AnalysisResult(true, true, null, errorMessage);
    } // createErrorResult()

    private AnalysisResult(boolean isComplete, boolean isError, STree resultTree, String resultMessage) {
        this.isComplete = isComplete;
        this.isError = isError;
        this.resultTree = resultTree;
        this.resultMessage = resultMessage;
    } // AnalysisResult()

    public boolean isComplete() {
        return isComplete;
    } // isComplete()

    public boolean isError() {
        return isError;
    } // isError()

    public STree getResultTree() {
        return resultTree;
    } // getResultTree()

    public String getResultMessage() {
        return resultMessage;
    } // getResultMessage()
} // class AnalysisResult
