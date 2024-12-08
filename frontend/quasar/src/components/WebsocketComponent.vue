<template>
  <div>
    <slot :isConnected="isConnected" :sendMessage="sendMessage"></slot>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick, defineEmits } from 'vue'

const emit = defineEmits(['message'])

const isConnected = ref(false)
let socket

const sendMessage = (message) => {
  if (isConnected.value) {
    socket.send(message)
  }
}

onMounted(async () => {
  await nextTick()
  const hostname = window.location.hostname
  const wsUrl = `ws://${hostname}:7090`
  socket = new WebSocket(wsUrl)

  socket.onopen = () => {
    console.log('WebSocket connection established')
    isConnected.value = true
  }

  socket.onclose = () => {
    console.log('WebSocket connection closed')
    isConnected.value = false
  }

  socket.onerror = (error) => {
    console.error('WebSocket error:', error)
  }

  socket.onmessage = (event) => {
    console.log('WebSocket message received:', event.data)
    emit('message', event.data)
  }
})

onUnmounted(() => {
  if (socket) {
    socket.close()
  }
})
</script>