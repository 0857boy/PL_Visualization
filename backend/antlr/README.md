# ANTLR Grammar Project

## 目錄結構
- `grammars/`：存放 ANTLR 的文法文件。
- `generated/`：根據grammars/中的文法文件生成的 javascript 文件。
- `scripts/`：執行 ANTLR 和測試的輔助腳本。

## grammar 生成語法指令
```bash
antlr4 -Dlanguage=JavaScript -o generated/OurScheme -no-listener grammars/OurScheme.g4
```
