<template>
    <q-card>
        <q-card-section>
            <q-btn icon="fullscreen" @click="toggleFullScreen" class="fullscreen-btn" />
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

const drawTree = (data, width, height) => {
    if (!treeContainer.value) return; // 檢查 treeContainer 是否為 null

    const minWidth = 513; // 最小寬度
    const minHeight = 800; // 最小高度

    const containerWidth = Math.max(width || treeContainer.value.clientWidth, minWidth);
    const containerHeight = Math.max(height || treeContainer.value.clientHeight, minHeight);

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
            .y((d) => d.y)
            .source((d) => ({ x: d.source.x, y: d.source.y + 20 })) // 調整連結的起點
            .target((d) => ({ x: d.target.x, y: d.target.y - 20 })) // 調整連結的終點
        )
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
        .append('text')
        .attr('dy', 0)
        .attr('x', 0)
        .style('text-anchor', 'middle')
        .style('font-size', '30px')
        .text((d) => d.data.name);
};

onMounted(() => {
    if (treeContainer.value) {
        drawTree(props.parseTree);
        window.addEventListener('resize', () => drawTree(props.parseTree, treeContainer.value.clientWidth, treeContainer.value.clientHeight));
    }
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

watch(
    () => props.parseTree,
    (newTree) => {
        if (treeContainer.value) {
            drawTree(newTree);
        }
    },
);
</script>