# ANTLR Grammar Project

## 目錄結構
- `grammars/`：存放 ANTLR 的文法文件。
- `generated/`：自動生成的代碼（不需要手動修改）。
- `tests/`：測試用的輸入文件。
- `examples/`：文法的使用示例。
- `scripts/`：執行 ANTLR 和測試的輔助腳本。

## 如何生成代碼
1. 進入 `scripts/` 目錄。
2. 執行腳本：`./generate.sh`。
3. 生成的代碼將保存在 `generated/` 目錄中。

## 如何運行測試
1. 將測試數據放入 `tests/`。
2. 執行腳本：`./run-tests.sh`。

## grammar 生成語法指令
```bash
antlr4 -Dlanguage=Python3 -o generated/OurScheme -no-listener -visitor grammars/OurScheme.g4
```
