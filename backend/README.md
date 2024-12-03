# 後端


## 如何生成 ANTLR 代碼
1. 進入 `backend/antlr/` 目錄。
2. 執行以下命令生成代碼：
    ```bash
    antlr4 -Dlanguage=Python3 -o generated/OurScheme -no-listener -visitor grammars/OurScheme.g4
    ```

## 如何運行後端服務
1. 構建 Docker 映像：
    ```bash
    docker build -t backend-image -f Dockerfile-backend .
    ```
2. 運行 Docker 容器：
    ```bash
    docker run -d -p 7090:3000 backend-image
    ```

## 主要功能
- 語法樹解析：使用 ANTLR 生成語法樹。
- 程式碼執行：逐行執行 OurScheme 程式碼。
- WebSocket 通信：實時回傳執行狀態。

## 文件說明
- `InterpreterOurScheme.cpp`：包含 OurScheme 語言的解釋器邏輯。
- `server.js`：設置 WebSocket 服務器，處理前端請求並調用解釋器。
- `Dockerfile-backend`：定義了後端服務的 Docker 映像構建過程。
- `package.json`：列出了後端服務所需的依賴項。