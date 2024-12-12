import express from 'express';
import cors from 'cors'; // 引入 cors 模組
import { spawn } from 'child_process';
import fs from 'fs';
import http from 'http';
import WebSocket, { WebSocketServer } from 'ws';
import antlr4 from 'antlr4';
import OurSchemeLexer from './antlr/generated/OurScheme/OurSchemeLexer.js';
import OurSchemeParser from './antlr/generated/OurScheme/OurSchemeParser.js';

const app = express();
const port = 3000;

app.use(cors()); // 使用 cors 中間件
app.use(express.json());

const server = http.createServer(app);
const wss = new WebSocketServer({ server });

let connections = new Map(); // 用來儲存所有連線
const maxConnections = 500; // 設定最大連線數量
const timeout = 600000; // 設定連線超時10分鐘
const memoryLimit = 150000; // 設定虛擬記憶體限制

wss.on('connection', (ws) => {
    if (connections.size >= maxConnections) {
        ws.send('Too many connections. Please try again later.\n');
        ws.close();
        console.log('Connection refused: too many connections.');
        return;
    }

    const id = Date.now();
    connections.set(id, { ws, lastActive: Date.now() });
    console.log(`New connection established with ID: ${id}`);

    let interpreter;
    let interpreterRunning = false;
    let currentInterpreterType = null;
    let timeoutId = setTimeout(() => {
        ws.send('Timeout.\n');
        ws.close();
        console.log(`Connection ${id} closed due to timeout.`);
    }, timeout);

    ws.on('message', (message) => {
        clearTimeout(timeoutId);
        timeoutId = setTimeout(() => {
            ws.close();
        }, timeout);

        connections.get(id).lastActive = Date.now();

        let parsedMessage;
        try {
            parsedMessage = JSON.parse(message);
        } catch (error) {
            ws.send('Invalid message format.\n');
            console.log(`Invalid message format from connection ${id}: ${message}`);
            return;
        }

        const { interpreterType, payload } = parsedMessage;

        // 如果接收到不同的 interpreterType，關閉舊的Interpreter
        if (interpreterRunning && currentInterpreterType !== interpreterType) {
            interpreter.kill();
            interpreterRunning = false;
            console.log(`Interpreter for connection ${id} killed due to type change.`);
        }

        if (!interpreterRunning) {
            switch (interpreterType) {
                case 'OurScheme':
                    interpreter = spawn('sh', ['-c', `ulimit -v ${memoryLimit}; ./InterpreterOurScheme`]);
                    break;
                case 'OurC': 
                    interpreter = spawn('sh', ['-c', `ulimit -v ${memoryLimit}; ./InterpreterOurC`]);
                    break;
                default:
                    ws.send('Unknown interpreter type.\n');
                    ws.close();
                    console.log(`Unknown interpreter type from connection ${id}: ${interpreterType}`);
                    return;
            }

            currentInterpreterType = interpreterType;
            interpreterRunning = true;
            console.log(`Interpreter ${interpreterType} started for connection ${id}.`);

            interpreter.stdout.on('data', (data) => {
                ws.send(data.toString());
            });

            interpreter.on('close', () => {
                interpreterRunning = false;
                ws.close();
                console.log(`Interpreter for connection ${id} closed.`);
            });

            interpreter.on('error', (error) => {
                console.error('Interpreter error:', error);
                ws.send('Interpreter error.\n');
                ws.close();
                console.log(`Interpreter error for connection ${id}: ${error.message}`);
            });
        }

        if (ws.readyState === WebSocket.OPEN && interpreterRunning) {
            if (payload !== null && payload !== undefined) {
                try {
                    interpreter.stdin.write(payload);
                    console.log(`Payload received from connection ${id}: { ${payload} }`);
                } catch (error) {
                    console.error('Failed to write to interpreter:', error);
                    console.log(`Failed to write to interpreter for connection ${id}: { ${error.message} }`);
                }
            }
        }
    });

    ws.on('close', async () => {
        if (interpreter) {
            interpreter.kill();
        }
        interpreterRunning = false;
        connections.delete(id);
        console.log(`Connection ${id} closed.`);
    });
});

setInterval(() => {
    const now = Date.now();
    connections.forEach((connection, id) => {
        if (now - connection.lastActive > timeout) {
            connection.ws.close();
            connections.delete(id);
            console.log(`Connection ${id} closed due to inactivity.`);
        }
    });
}, 60000); // 每分鐘檢查一次，並清除超時的連線

// 新增的路由來處理語法樹的生成和回傳
app.post('/syntax-tree', (req, res) => {
    const { payload, interpreterType } = req.body;
    if (!payload || !interpreterType) {
        return res.status(400).send('payload and interpreterType are required.');
    }

    try {
        let chars, lexer, tokens, parser;
        switch (interpreterType) {
            case 'OurScheme':
                chars = new antlr4.InputStream(payload);
                lexer = new OurSchemeLexer(chars);
                tokens = new antlr4.CommonTokenStream(lexer);
                parser = new OurSchemeParser(tokens);
                parser.buildParseTrees = true;
                break;
            // case 'OurC':
            //     chars = new antlr4.InputStream(code);
            //     lexer = new OurCLexer(chars);
            //     tokens = new antlr4.CommonTokenStream(lexer);
            //     parser = new OurCParser(tokens);
            //     parser.buildParseTrees = true;
            //     break;
            default:
                return res.status(400).send('Unknown interpreter type.');
        }

        const tree = parser.program();

        // 將解析樹轉換為 JSON 格式
        const treeJson = tree.toStringTree(parser.ruleNames);

        console.log(treeJson); // 打印解析樹
        res.json({ parseTree: treeJson });
    } catch (error) {
        console.error('Error generating syntax tree:', error);
        res.status(500).send('Error generating syntax tree.');
    }
});

server.listen(port, () => {
    console.log(`Server is running on http://localhost:${port}`);
});