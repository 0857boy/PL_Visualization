package cycuice.sourceAnalyzer.customException;

/**
 * 表示當前這個功能尚未開發，正在開發中
 */
public class DevelopingException extends CustomException {
    public DevelopingException() {
        super();
    } // DevelopingException()

    public DevelopingException(String message) {
        super(message);
    } // DevelopingException()
} // class DevelopingException
