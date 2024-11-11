# PL Visualization Project

這是一個使用 Spring Boot 和 Maven 的專案，旨在進行程式語言的視覺化分析。

## 主要功能

- **DemoApplication**: 主應用程式入口，位於 [`DemoApplication.java`](src/main/java/com/pl_visualization_project/demo/DemoApplication.java)。
- **DemoController**: 控制器，處理 HTTP 請求，位於 [`DemoController.java`](src/main/java/com/pl_visualization_project/demo/DemoController.java)。
- **Parser**: LL(1) 文法分析器，位於 [`Parser.java`](src/main/java/cycuice/sourceAnalyzer/parser/Parser.java)。
- **STree**: 樹結構，位於 [`STree.java`](src/main/java/cycuice/sourceAnalyzer/stree/STree.java)。
- **Lexer**: 字元分析器，位於 [`Lexer.java`](src/main/java/cycuice/sourceAnalyzer/lexer/Lexer.java)。
- **PrettyPrintable**: 格式化輸出介面，位於 [`PrettyPrintable.java`](src/main/java/cycuice/sourceAnalyzer/common/PrettyPrintable.java)。
- **DevelopingException**: 自訂例外，位於 [`DevelopingException.java`](src/main/java/cycuice/sourceAnalyzer/customException/DevelopingException.java)。

## 流程
1. 使用者將程式碼提交至前端介面。
2. 後端的 DemoController 接收請求，並依序經過 Lexer 和 Parser 解析。
3. 解析後的語法樹（STree）經過 PrettyPrintable 格式化，並返回給前端。
4. 前端將視覺化結果呈現給使用者，使其可以查看程式碼的語法結構。


