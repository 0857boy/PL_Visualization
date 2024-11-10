package cycuice.sourceAnalyzer;

import java.util.ArrayList;

import cycuice.sourceAnalyzer.lexer.Lexer;
import cycuice.sourceAnalyzer.lexer.TokenizeResult;
import cycuice.sourceAnalyzer.parser.ParseResult;
import cycuice.sourceAnalyzer.parser.Parser;

/**
 * 原始碼分析器，每次分析一行原始碼，將分析結果儲存於results中。每次分析產生一至多個結果
 */
public class SourceAnalyzer {
    private Lexer lexer;  // 負責將原始碼切割成token串列的lexer
    private Parser parser;  // 負責分析原始碼的parser
    private ArrayList<AnalysisResult> results;  // 儲存每次分析結果的容器

    public SourceAnalyzer() {
        lexer = new Lexer();
        parser = new Parser();
        results = new ArrayList<AnalysisResult>();
    } // SourceAnalyzer()

    /**
     * 分析一行our scheme原始碼，並儲存此次分析對應的結果，每次呼叫此函式，都會將先前已分析結果清除
     * 
     * @param lineOfSourceCode - 一行的原始碼
     */
    public ArrayList<AnalysisResult> analyze(String lineOfSourceCode) {
        ArrayList<TokenizeResult> tokenizeResults = lexer.tokenize(lineOfSourceCode);
        ArrayList<ParseResult> parseResults = parser.parse(tokenizeResults);

        ArrayList<AnalysisResult> results = new ArrayList<AnalysisResult>();
        for (ParseResult parseResult : parseResults) {
            results.add(convertParseResultToAnalysisResult(parseResult));
        } // for

        return results;
    } // analyze()

    /**
     * 取得目前已分析的結果數量
     * 
     * @return 目前已分析的結果數量
     */
    public int getResultCount() {
        return results.size();
    } // getResultCount()

    /**
     * 取得第resultNum個分析結果
     * 
     * @param resultNum - 第幾個分析結果(從1開始)
     * @return 第resultNum個分析結果
     */
    public AnalysisResult getResult(int resultNum) {
        return results.get(resultNum - 1);
    } // getResult()

    private AnalysisResult convertParseResultToAnalysisResult(ParseResult parseResult) {
        if (parseResult.isComplete()) {
            if (parseResult.isError()) {
                return AnalysisResult.createErrorResult(parseResult.getErrorMessage());
            } // if
            else {
                return AnalysisResult.createCompleteResult(parseResult.getResultTree());
            } // else
        } // if

        return AnalysisResult.createIncompleteResult();
    } // convertParseResultToAnalysisResult()
} // class SourceAnalyzer
