package cycuice.sourceAnalyzer.stree;

public enum SNodeType {
    INTERNAL_NODE,
    INT_NODE,
    FLOAT_NODE,
    STRING_NODE,
    SYMBOL_NODE,
    NIL_NODE,
    T_NODE,
    QUOTE_NODE,
    FUNCTION_NODE,  // user function型別
    LAMBDA_NODE,    // lambda function型別
    ERROR_NODE      // error object型別
} // enum SNodeType
