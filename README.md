# PL Visualization Project

## 心智圖
![XMIND](/IMGS/XMIND.png)

## 主題
- 使用 React 與 React Flow 開發的語法樹生成與執行進度顯示應用

## 功能
1. 語法樹生成
   - 支援多種程式語言
   - 使用後端 (Flask + ANTLR) 生成語法樹，並以 JSON 格式傳遞至前端
   - 前端基於 React Flow 動態生成樹結構

2. 程式執行進度追蹤
   - 逐行執行程式碼
   - 動態高亮執行行號
   - 同步高亮 React Flow 節點

3. 動態互動
   - 支援暫停與繼續執行
   - 即時顯示執行狀態（如目前行號或錯誤訊息）
   - 節點點擊顯示詳細資訊

## 架構
1. 前端
   - 技術：React + React Flow + WebSocket
   - 功能：
     - 程式碼編輯器 (Monaco Editor)
     - 使用 React Flow 展示語法樹
     - 支援節點高亮與互動

2. 後端
   - 技術：Flask + WebSocket
   - 功能：
     - 語法樹解析模組 (ANTLR)
     - 程式碼執行模組 (逐行執行程式碼)
     - 動態數據回傳至前端

3. 執行模組
   - 語法解析：ANTLR 生成 AST
   - 執行器：逐行執行程式碼，並透過 WebSocket 發送行號

4. 通信模組
   - 技術：WebSocket
   - 功能：
     - 連接前後端，提供實時行號回傳
     - 支援多用戶

## 流程
1. 用戶輸入程式碼，選擇程式語言
2. 前端發送請求至後端 `/parse` 接口
3. 後端生成語法樹，並以 JSON 格式回傳
4. 前端基於 React Flow 渲染語法樹
5. 用戶點擊「執行」，前端發送請求至 `/execute`
6. 後端逐行執行程式碼，透過 WebSocket 即時回報行號
7. 前端根據行號高亮語法樹節點與程式碼行號
Test
## 項目結構
<!-- PROJECT TREE START -->

├── IMGS
│   ├── XMIND.png
├── README.md
├── changes.log
├── mvnw
├── mvnw.cmd
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── com
│   │   │   │   ├── pl_visualization_project
│   │   │   │   │   ├── demo
│   │   │   │   │   │   ├── DemoApplication.java
│   │   │   │   │   │   ├── DemoController.java
│   │   │   ├── cycuice
│   │   │   │   ├── sourceAnalyzer
│   │   │   │   │   ├── AnalysisResult.java
│   │   │   │   │   ├── SourceAnalyzer.java
│   │   │   │   │   ├── common
│   │   │   │   │   │   ├── Indexable.java
│   │   │   │   │   │   ├── JSONStringifyable.java
│   │   │   │   │   │   ├── Nameable.java
│   │   │   │   │   │   ├── PrettyPrintable.java
│   │   │   │   │   ├── customException
│   │   │   │   │   │   ├── CustomException.java
│   │   │   │   │   │   ├── DevelopingException.java
│   │   │   │   │   ├── lexer
│   │   │   │   │   │   ├── Lexer.java
│   │   │   │   │   │   ├── Token.java
│   │   │   │   │   │   ├── TokenType.java
│   │   │   │   │   │   ├── TokenizeResult.java
│   │   │   │   │   ├── parser
│   │   │   │   │   │   ├── ParseResult.java
│   │   │   │   │   │   ├── Parser.java
│   │   │   │   │   ├── signal
│   │   │   │   │   │   ├── EOFSignal.java
│   │   │   │   │   │   ├── EndOfLineSignal.java
│   │   │   │   │   │   ├── Signal.java
│   │   │   │   │   │   ├── StaticErrorSignal.java
│   │   │   │   │   ├── stree
│   │   │   │   │   │   ├── SNode.java
│   │   │   │   │   │   ├── SNodeType.java
│   │   │   │   │   │   ├── STree.java
│   │   │   │   │   │   ├── STreeBuilder.java
│   │   ├── resources
│   │   │   ├── application.properties
│   │   │   ├── static
│   │   │   │   ├── css
│   │   │   │   │   ├── menu.css
│   │   │   │   │   ├── style.css
│   │   │   │   ├── js
│   │   │   │   │   ├── post.js
│   │   │   │   │   ├── script.js
│   │   │   │   │   ├── tree.js
│   │   │   ├── templates
│   │   │   │   ├── home.html
│   │   │   │   ├── index.html
│   │   │   │   ├── test.html
│   ├── test
│   │   ├── java
│   │   │   ├── com
│   │   │   │   ├── pl_visualization_project
│   │   │   │   │   ├── demo
│   │   │   │   │   │   ├── DemoApplicationTests.java
├── target
│   ├── classes
│   │   ├── application.properties
│   │   ├── com
│   │   │   ├── pl_visualization_project
│   │   │   │   ├── demo
│   │   │   │   │   ├── DemoApplication.class
│   │   │   │   │   ├── DemoController.class
│   │   ├── cycuice
│   │   │   ├── sourceAnalyzer
│   │   │   │   ├── AnalysisResult.class
│   │   │   │   ├── SourceAnalyzer.class
│   │   │   │   ├── common
│   │   │   │   │   ├── Indexable.class
│   │   │   │   │   ├── JSONStringifyable.class
│   │   │   │   │   ├── Nameable.class
│   │   │   │   │   ├── PrettyPrintable.class
│   │   │   │   ├── customException
│   │   │   │   │   ├── CustomException.class
│   │   │   │   │   ├── DevelopingException.class
│   │   │   │   ├── lexer
│   │   │   │   │   ├── Lexer.class
│   │   │   │   │   ├── Token.class
│   │   │   │   │   ├── TokenType.class
│   │   │   │   │   ├── TokenizeResult.class
│   │   │   │   ├── parser
│   │   │   │   │   ├── ParseResult.class
│   │   │   │   │   ├── Parser$SyntaxNode.class
│   │   │   │   │   ├── Parser$SyntaxNodeType.class
│   │   │   │   │   ├── Parser.class
│   │   │   │   ├── signal
│   │   │   │   │   ├── EOFSignal.class
│   │   │   │   │   ├── EndOfLineSignal.class
│   │   │   │   │   ├── Signal.class
│   │   │   │   │   ├── StaticErrorSignal.class
│   │   │   │   ├── stree
│   │   │   │   │   ├── SNode.class
│   │   │   │   │   ├── SNodeType.class
│   │   │   │   │   ├── STree$JSONTreeNode.class
│   │   │   │   │   ├── STree.class
│   │   │   │   │   ├── STreeBuilder.class
│   │   ├── static
│   │   │   ├── css
│   │   │   │   ├── menu.css
│   │   │   │   ├── style.css
│   │   │   ├── js
│   │   │   │   ├── post.js
│   │   │   │   ├── script.js
│   │   │   │   ├── tree.js
│   │   ├── templates
│   │   │   ├── home.html
│   │   │   ├── index.html
│   │   │   ├── test.html
│   ├── generated-sources
│   │   ├── annotations
│   ├── generated-test-sources
│   │   ├── test-annotations
│   ├── maven-status
│   │   ├── maven-compiler-plugin
│   │   │   ├── compile
│   │   │   │   ├── default-compile
│   │   │   │   │   ├── createdFiles.lst
│   │   │   │   │   ├── inputFiles.lst
│   │   │   ├── testCompile
│   │   │   │   ├── default-testCompile
│   │   │   │   │   ├── createdFiles.lst
│   │   │   │   │   ├── inputFiles.lst
│   ├── test-classes
│   │   ├── com
│   │   │   ├── pl_visualization_project
│   │   │   │   ├── demo
│   │   │   │   │   ├── DemoApplicationTests.class
<!-- PROJECT TREE END -->
