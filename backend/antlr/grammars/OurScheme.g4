grammar OurScheme;

// 詞法規則：定義所有的基本標記（Tokens）
LEFT_PAREN : '(';
RIGHT_PAREN : ')';
DOT : '.';
QUOTE : '\'';
COLON : ':';
NUMBER : [0-9]+ ('.' [0-9]+)?; // 支援整數和浮點數
STRING : '"' (~["\\] | '\\' .)* '"'; // 字串，允許空格
SYMBOL : [a-zA-Z_][a-zA-Z0-9_]*; // 符號或標識符，不允許空格
NIL : 'nil' | '#f';
T : '#t';
WS : [ \t\r\n]+ -> skip; // 跳過空白字符
COMMENT : ';' ~[\r\n]* -> skip; // 跳過單行註解

// 語法規則：描述語言的結構
program
    : expression+ EOF // 程式由多個表達式組成，並以 EOF 結束
    ;

expression
    : atom
    | list
    | quotedExpression
    | functionCall // 支援函數呼叫
    ;

atom
    : NUMBER
    | STRING
    | SYMBOL
    | NIL
    | T
    ;

list
    : LEFT_PAREN (expression (DOT expression)?)* RIGHT_PAREN
    ;

quotedExpression
    : QUOTE expression
    ;

// 支援的函數
functionCall
    : 'create-error-object' LEFT_PAREN STRING RIGHT_PAREN
    | 'error-object?' LEFT_PAREN expression RIGHT_PAREN
    | 'read' LEFT_PAREN RIGHT_PAREN
    | 'write' LEFT_PAREN expression RIGHT_PAREN
    | 'display-string' LEFT_PAREN (STRING | errorObject) RIGHT_PAREN
    | 'newline' LEFT_PAREN RIGHT_PAREN
    | 'eval' LEFT_PAREN expression RIGHT_PAREN
    | 'set!' LEFT_PAREN SYMBOL expression RIGHT_PAREN
    ;

// 錯誤物件
errorObject
    : 'ERROR' COLON STRING
    ;