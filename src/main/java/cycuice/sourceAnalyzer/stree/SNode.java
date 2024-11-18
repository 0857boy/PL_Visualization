package cycuice.sourceAnalyzer.stree;

public class SNode {
    private SNodeType type;        // 此node的型別
    private int intValue;          // int值
    private double floatValue;     // float值
    private String commonValue;    // string值(不儲存雙引號)、symbol名稱、function名稱
    private SNode leftChild;       // 左子樹
    private SNode rightChild;      // 右子樹

    public static SNode createInternalNode(SNode leftChild, SNode rightChild) {
        return new SNode(SNodeType.INTERNAL_NODE, 0, 0.0, null, leftChild, rightChild);
    } // createInternalNode()

    public static SNode createIntNode(int intValue) {
        return new SNode(SNodeType.INT_NODE, intValue, 0.0, null, null, null);
    } // createIntNode()

    public static SNode createFloatNode(double floatValue) {
        return new SNode(SNodeType.FLOAT_NODE, 0, floatValue, null, null, null);
    } // createFloatNode()

    public static SNode createStringNode(String stringValue) {
        return new SNode(SNodeType.STRING_NODE, 0, 0.0, stringValue, null, null);
    } // createStringNode()

    public static SNode createSymbolNode(String symbolName) {
        return new SNode(SNodeType.SYMBOL_NODE, 0, 0.0, symbolName, null, null);
    } // createSymbolNode()

    public static SNode createNilNode() {
        return new SNode(SNodeType.NIL_NODE, 0, 0.0, null, null, null);
    } // createNilNode()

    public static SNode createTNode() {
        return new SNode(SNodeType.T_NODE, 0, 0.0, null, null, null);
    } // createTNode()

    public static SNode createQuoteNode() {
        return new SNode(SNodeType.QUOTE_NODE, 0, 0.0, null, null, null);
    } // createQuoteNode()

    public static SNode createFunctionNode(String functionName) {
        return new SNode(SNodeType.FUNCTION_NODE, 0, 0.0, functionName, null, null);
    } // createFunctionNode()

    private SNode(SNodeType type, int intValue, double floatValue, String commonValue, SNode leftChild, SNode rightChild) {
        this.type = type;
        this.intValue = intValue;
        this.floatValue = floatValue;
        this.commonValue = commonValue;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    } // SNode()

    public SNodeType getType() {
        return type;
    } // getType()

    /**
     * 獲取此SNode的標準輸出值，例如: float值一律輸出成小數點後三位的標準格式
     * 
     * @return 字串型別的SNode標準輸出值
     */
    public String getStandardValue() {
        if (type == SNodeType.INTERNAL_NODE) {
            return null;
        } // if
        else if (type == SNodeType.INT_NODE) {
            return Integer.toString(this.intValue);
        } // else if
        else if (type == SNodeType.FLOAT_NODE) {
            return String.format("%.3f", this.floatValue);
        } // else if
        else if (type == SNodeType.STRING_NODE) {
            return "\"" + this.commonValue + "\"";
        } // else if
        else if (type == SNodeType.SYMBOL_NODE) {
            return this.commonValue;
        } // else if
        else if (type == SNodeType.NIL_NODE) {
            return "nil";
        } // else if
        else if (type == SNodeType.T_NODE) {
            return "#t";
        } // else if
        else if (type == SNodeType.QUOTE_NODE) {
            return "quote";
        } // else if
        else if (type == SNodeType.FUNCTION_NODE) {
            return "#<procedure " + this.commonValue + ">";
        } // else if
        else if (type == SNodeType.LAMBDA_NODE) {
            return "#<procedure lambda>";
        } // else if
        else {  // type == SNodeType.ERROR_NODE
            return "\"" + this.commonValue + "\"";
        } // else
    } // getStandardValue()

    public SNode getLeftChild() {
        return leftChild;
    } // getLeftChild()

    public SNode getRightChild() {
        return rightChild;
    } // getRightChild()
} // class SNode
