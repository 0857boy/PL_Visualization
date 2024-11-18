package cycuice.sourceAnalyzer.lexer;

/**
 * 存放lexer分析結果
 */
public class TokenizeResult {
    private boolean isError;      // 是否有錯誤(tokenize過程也只會有no closing quote錯誤)
    private String errorMessage;  // 錯誤訊息
    private Token resultToken;    // lexer分析的token結果

    public static TokenizeResult createNormalResult(Token resultToken) {
        return new TokenizeResult(false, null, resultToken);
    } // createNormalResult()

    public static TokenizeResult createErrorResult(String errorMessage) {
        return new TokenizeResult(true, errorMessage, null);
    } // createErrorResult()

    private TokenizeResult(boolean isError, String errorMessage, Token resultToken) {
        this.isError = isError;
        this.errorMessage = errorMessage;
        this.resultToken = resultToken;
    } // TokenizeResult(

    public boolean isError() {
        return isError;
    } // isError()

    public String getErrorMessage() {
        return errorMessage;
    } // getErrorMessage()

    public Token getResultToken() {
        return resultToken;
    } // getResultToken()
} // class TokenizeResult
