<template>
  <q-card>
    <q-card-section>
      <div ref="treeContainer" class="tree-container"></div>
      <q-btn icon="fullscreen" @click="toggleFullScreen" class="fullscreen-btn" />
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

const drawTree = (data, width, height) => {
  const containerWidth = width || treeContainer.value.clientWidth;
  const containerHeight = height || treeContainer.value.clientHeight;

  d3.select(treeContainer.value).selectAll('*').remove(); // 清除之前的圖形

  const root = d3.hierarchy(data);

  const treeLayout = d3.tree()
    .size([containerWidth - 100, containerHeight - 100])
    .separation((a, b) => (a.parent === b.parent ? 1 : 2));

  treeLayout(root);

  const svg = d3
    .select(treeContainer.value)
    .append('svg')
    .attr('width', containerWidth)
    .attr('height', containerHeight)
    .style('background-color', 'white')
    .append('g')
    .attr('transform', 'translate(50,50)');

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

  node
    .append('text')
    .attr('dy', 15)
    .attr('x', 0)
    .style('text-anchor', 'middle')
    .text((d) => d.data.name);
};

onMounted(() => {
  drawTree(props.parseTree);
  window.addEventListener('resize', () => drawTree(props.parseTree, treeContainer.value.clientWidth, treeContainer.value.clientHeight));
});

const toggleFullScreen = () => {
  if (!document.fullscreenElement) {
    treeContainer.value.requestFullscreen().then(() => {
      drawTree(props.parseTree, window.innerWidth, window.innerHeight);
    });
  } else {
    document.exitFullscreen().then(() => {
      drawTree(props.parseTree);
    });
  }
};

onMounted(() => {
  drawTree(props.parseTree);
  window.addEventListener('resize', () => drawTree(props.parseTree));
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
  width: 100%; /* 設定寬度為 100% */
  height: 100%; /* 設定高度為 100% */
  min-width: 800px; /* 設定最小寬度 */
  min-height: 600px; /* 設定最小高度 */
  overflow: auto; /* 設定超出容器時顯示滾動條 */
}

.fullscreen-btn {
  position: absolute;
  top: 10px;
  right: 10px;
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