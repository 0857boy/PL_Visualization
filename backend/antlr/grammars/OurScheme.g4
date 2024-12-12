grammar OurScheme;


// Lexer rules (tokens)
LEFT_PAREN     : '(' ;
RIGHT_PAREN    : ')' ;
DOT            : '.' ;
QUOTE          : '\'' ;
INT            : ('+' | '-')? [0-9]+ ;
FLOAT          : ('+' | '-')? ([0-9]+ '.' [0-9]* | '.' [0-9]+) ;
STRING         : '"' ('\\' . | ~["\\])* '"' ;
NIL            : 'nil' | '#f' ;
T              : 't' | '#t' ;
SYMBOL         : [a-zA-Z!$%&*+-./:<=>?@^_~]+ ;
WS             : [ \t\r\n]+ -> skip ;
COMMENT        : ';' ~[\r\n]* -> skip ;

// Parser rules
sExp           : atom
               | LEFT_PAREN sExpList? RIGHT_PAREN
               | QUOTE sExp ;
atom           : SYMBOL
               | INT
               | FLOAT
               | STRING
               | NIL
               | T
               | LEFT_PAREN RIGHT_PAREN ;
sExpList       : sExp (sExp)* (DOT sExp)? ;


program        : sExp* EOF ;