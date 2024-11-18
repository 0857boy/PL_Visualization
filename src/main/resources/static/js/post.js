var editor = ace.edit("editor");  // 假設您已經創建了 Ace Editor 的實例

$(document).keypress(function(e) {
    if (e.which == 13) {  // Enter 鍵的鍵碼是 13
        var content = editor.getValue();  // 獲取 Ace Editor 的內容
        var lines = content.split('\n');  // 將內容按換行符分割為行數組
        var lastLine = lines[lines.length - 1];  // 獲取最後一行

        $.ajax({
            type: "POST",
            url: "http://localhost:8081/processTest",  // 替換為實際的後端接口 URL
            data: {code: lastLine},  // 將獲取的 Ace Editor 內容放入 POST 請求的數據中
            success: function(data) {
                console.log("Data sent to server successfully.");
            },
            error: function(err) {
                console.error("Failed to send data to server: " + err);
            }
        });
    }
});