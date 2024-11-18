package cycuice.sourceAnalyzer.signal;

public class StaticErrorSignal extends Signal {
    private String errorMessage;

    public StaticErrorSignal(String errorMessage) {
        super();
        this.errorMessage = errorMessage;
    } // StaticErrorSignal()

    public String getErrorMessage() {
        return this.errorMessage;
    } // getErrorMessage()
} // class StaticErrorSignal
