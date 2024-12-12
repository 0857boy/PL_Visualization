<template>
    <q-card>
      <q-card-section>
        <div ref="treeContainer" class="tree-container"></div>
      </q-card-section>
    </q-card>
  </template>
  
  <script setup>
  import { ref, onMounted, watch } from 'vue';
  import * as d3 from 'd3';
  
  const props = defineProps({
    parseTree: {
      type: Object,
      required: true,
    },
  });
  
  const treeContainer = ref(null);
  
  // 繪製樹
  const drawTree = (data) => {
    const containerWidth = 800;
    const containerHeight = 600;
  
    const root = d3.hierarchy(data);
  
    // 調整節點間隔
    const treeLayout = d3.tree()
      .size([containerWidth - 100, containerHeight - 100])
      .separation((a, b) => (a.parent === b.parent ? 1 : 2));
  
    treeLayout(root);
  
    // 清空舊的 SVG
    d3.select(treeContainer.value).selectAll('svg').remove();
  
    const svg = d3
      .select(treeContainer.value)
      .append('svg')
      .attr('width', containerWidth)
      .attr('height', containerHeight)
      .append('g')
      .attr('transform', 'translate(50,50)');
  
    // 繪製連線
    svg
      .selectAll('.link')
      .data(root.links())
      .enter()
      .append('path')
      .attr('class', 'link')
      .attr('d', d3.linkVertical()
        .x((d) => d.x)
        .y((d) => d.y))
      .attr('fill', 'none')
      .attr('stroke', '#555')
      .attr('stroke-width', 1.5);
  
    // 繪製節點
    const node = svg
      .selectAll('.node')
      .data(root.descendants())
      .enter()
      .append('g')
      .attr('class', 'node')
      .attr('transform', (d) => `translate(${d.x},${d.y})`);
  
    node
      .append('circle')
      .attr('r', 5)
      .attr('fill', 'steelblue');
  
    // 顯示層級與節點標籤
    node
      .append('text')
      .attr('dy', 15) // 將文字移至節點下方
      .attr('x', 0)
      .style('text-anchor', 'middle')
      .text((d) => `${d.data.name}:${d.depth}`);
  };
  
  onMounted(() => {
    drawTree(props.parseTree);
  });
  
  watch(
    () => props.parseTree,
    (newTree) => {
      drawTree(newTree);
    },
  );
  </script>
  
  <style scoped>
  .tree-container {
    width: 100%;
    height: 100%;
    overflow: auto;
  }
  
  .link {
    fill: none;
    stroke: #555;
    stroke-opacity: 0.6;
    stroke-width: 2px;
  }
  
  .node circle {
    fill: steelblue;
    stroke: steelblue;
    stroke-width: 3px;
  }
  
  .node text {
    font: 12px sans-serif;
  }
  </style>