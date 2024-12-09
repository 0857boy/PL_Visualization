const express = require('express');
const { spawn } = require('child_process');
const fs = require('fs');
const http = require('http');
const WebSocket = require('ws');
const app = express();
const port = 3000;

app.use(express.json());

const server = http.createServer(app);
const wss = new WebSocket.Server({ server });

let connections = new Map(); // 用來儲存所有連線
const maxConnections = 500; // 設定最大連線數量
const timeout = 28800000; // 設定超時時間為 8 小時
const memoryLimit = 150000; // 設定虛擬記憶體限制

wss.on('connection', (ws) => {
    if (connections.size >= maxConnections) {
        ws.send('Too many connections. Please try again later.\n');
        ws.close();
        return;
    }

    const id = Date.now();
    connections.set(id, { ws, lastActive: Date.now() });

    let interpreter;
    let interpreterRunning = false;
    let currentInterpreterType = null;
    let timeoutId = setTimeout(() => {
        ws.send('Timeout.\n');
        ws.close();
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
            return;
        }

        const { interpreterType, payload } = parsedMessage;

        // 如果接收到不同的 interpreterType，關閉舊的Interpreter
        if (interpreterRunning && currentInterpreterType !== interpreterType) {
            interpreter.kill();
            interpreterRunning = false;
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
                    return;
            }

            currentInterpreterType = interpreterType;
            interpreterRunning = true;

            interpreter.stdout.on('data', (data) => {
                ws.send(data.toString());
            });

            interpreter.on('close', () => {
                interpreterRunning = false;
                ws.close();
            });

            interpreter.on('error', (error) => {
                console.error('Interpreter error:', error);
                ws.send('Interpreter error.\n');
                ws.close();
            });
        }

        if (ws.readyState === WebSocket.OPEN && interpreterRunning) {
            if (payload !== null && payload !== undefined) {
                try {
                    interpreter.stdin.write(payload);
                } catch (error) {
                    console.error('Failed to write to interpreter:', error);
                }
            }
        }
    });

    ws.on('close', async () => {
        if (interpreter) {
            interpreter.kill();
        }
        connections.delete(id);
    });
});

setInterval(() => {
    const now = Date.now();
    connections.forEach((connection, id) => {
        if (now - connection.lastActive > timeout) {
            connection.ws.close();
            connections.delete(id);
        }
    });
}, 60000); // 每分鐘檢查一次，並清除超時的連線

server.listen(port, () => {
    console.log(`Server is running at http://localhost:${port}`);
});