// 獲取繪製區域的元素
let graphSection = document.getElementById("graph-section");
// 獲取繪製區域的寬度
let svgWidth = graphSection.clientWidth;
// 獲取繪製區域的高度
let svgHeight = graphSection.clientHeight;
// 設置邊距
let margin = { top: 30, right: 20, bottom: 20, left: 20 };
// 設置縮放因子
let scaleFactor = 0.8;
// 設置動畫持續時間
let duration = 500;
// 設置半徑比例尺
let radiusScale = d3.scaleSqrt().range([2, 20]);
// 在繪製區域中增加svg元素
let svg = d3.select('#graph-section').append('svg').attr('width', svgWidth).attr('height', svgHeight);

let jsonData = null;  // 用於存儲 JSON 數據的全局變數

// 加載json數據
/*d3.json('tree.json').then(function (data) {
  // 設置半徑比例尺的輸入範圍
  radiusScale.domain([0, d3.max(data, function (d) { return d.value; })]);
  // 將數據轉為層級數據
  var root = d3.hierarchy(data);
  // 繪製樹狀圖
  drawTree(root);
});
*/

// 繪製樹狀圖的函數
function drawTree(root) {
  // 清除之前的圖形
  svg.selectAll('*').remove();

  // 定義樹狀布局
  var treeLayout = d3.tree().size([svgWidth - margin.left - margin.right, (svgHeight - margin.top - margin.bottom) * scaleFactor]);
  // 建立樹狀布局
  treeLayout(root);

  // 定義鏈接線
  var links = svg.selectAll('line')
    // 設置數據
    .data(root.links())
    .enter()
    .append('line')
    // 線的起點x座標
    .attr('x1', function (d) { return d.source.x + margin.left; })
    // 線的起點y座標
    .attr('y1', function (d) { return d.source.y * scaleFactor + margin.top; })
    // 線的終點x座標
    .attr('x2', function (d) { return d.source.x + margin.left; })
    // 線的終點y座標
    .attr('y2', function (d) { return d.source.y * scaleFactor + margin.top; })
    // 設置線的顏色
    .style('stroke', 'white')
    // 設置線的寬度
    .style('stroke-width', '5px')
    // 初始時設置透明度為0
    .style('opacity', 0);

  // 逐一將透明度由0變為1
  links.transition()
    .duration(duration)
    .delay(function (d, i) { return i * duration; })
    .style('opacity', 1)
    .attr('x2', function (d) { return d.target.x + margin.left; })
    .attr('y2', function (d) { return d.target.y * scaleFactor + margin.top; });

  // 創建節點
  var nodes = svg.selectAll('.node')
    // 設置數據
    .data(root.descendants())
    .enter()
    .append('g')
    .attr('class', 'node')
    // 設置節點位置
    .attr('transform', function (d) { return 'translate(' + (d.x + margin.left) + ',' + (d.y * scaleFactor + margin.top) + ')'; })
    // 初始時設置透明度為0
    .style('opacity', 0);

  // 逐一將透明度由0變為1
  nodes.transition()
    .duration(duration)
    .delay(function (d, i) { return i * duration; })
    .style('opacity', 1);

  // 添加背景圓形
  nodes.append('circle')
    // 使用固定的半徑
    .attr('r', 25)
    // 設置填充顏色
    .style('fill', 'lightgray');

  // 添加前景圓形
  nodes.append('circle')
    // 初始時設置半徑為0
    .attr('r', 0)
    // 設置填充顏色
    .style('fill', 'white')
    .transition()
    .duration(duration)
    .delay(function (d, i) { return i * duration; })
    // 變為固定的半徑
    .attr('r', 20);

  // 在節點上添加文本
  nodes.append('text')
    .attr('dy', '.35em')
    // 文本水平居中
    .attr('text-anchor', 'middle')
    // 文本垂直居中
    .attr('dominant-baseline', 'middle')
    // 設置文本顏色
    .attr('fill', 'gray')
    // 設置字體
    .style('font-family', 'Arial')
    // 設置字體粗細
    .style('font-weight', 'bolder')
    .text(function (d) {
      if (d.data.value === undefined) {
        // 如果 value 為空，則不顯示任何內容
        return '';
      } else {
        // 否則，顯示 value 的值
        return d.data.value;
      }
    });
}

// 更新畫面的函數
function update() {
  // 更新繪製區域的寬度
  svgWidth = graphSection.clientWidth;
  // 更新繪製區域的高度
  svgHeight = graphSection.clientHeight;
  // 更新 svg 的寬度和高度
  svg.attr('width', svgWidth).attr('height', svgHeight);

  // 如果有數據，則使用新的數據更新視覺化
  if (jsonData) {
    // 更新半徑比例尺的輸入範圍
    radiusScale.domain([0, d3.max(jsonData, function (d) { return d.value; })]);
    // 將數據轉為層級數據
    var root = d3.hierarchy(jsonData);
    // 繪製樹狀圖
    drawTree(root);
  }
}

// 創建一個監視器
let resizeObserver = new ResizeObserver(update);
// 開始監視繪製區域
resizeObserver.observe(graphSection);


$(document).ready(function () {

  // Load any saved input
  var savedInput = localStorage.getItem('editorInput');
  if (savedInput) {
    editor.setValue(savedInput);
  }

  editor.commands.addCommand({
    name: 'sendLine',
    bindKey: { win: 'Enter', mac: 'Enter' },
    exec: function (editor) {
      var text = editor.getCopyText(); // 獲取選擇的文本或空字符串
      if (text === '') {
        var line = editor.session.getLine(editor.getCursorPosition().row); // 獲取當前行的內容
        $.post("/set-cookie", { text: line }, function (data) {  // POST line to your server
          console.log(data);
          jsonData = JSON.parse(data);
          console.log('json update success');

          // 使用新的數據來更新視覺化
          update();
        });
        editor.insert("\n"); // 插入一個新行
        return;
      }
      // POST text to your server
      $.post("/set-cookie", { text: text }, function (data) {
        console.log(data);
      });
      editor.gotoLine(editor.getCursorPosition().row + 2, 0, false); // 跳轉到下一行
    },

    readOnly: true // 如果不需要用 Enter 鍵插入新行，則設定為 true
  });

  // Save the input value whenever it changes
  editor.session.on('change', function () {
    localStorage.setItem('editorInput', editor.getValue());
  });
});