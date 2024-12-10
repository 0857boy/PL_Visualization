<template>
  <q-card class="q-ma-xs" style="width: 100%;">
    <q-card-section>
      <div class="text-h6">{{ title }}</div>
      <q-btn flat round icon="download" class="absolute-top-right q-mt-sm q-mr-sm" @click="exportText" />
      <q-input v-model="text" readonly filled autogrow type="textarea" class="wrap" ref="textInput" />
    </q-card-section>
  </q-card>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue'

const props = defineProps({
  initialText: {
    type: String,
    required: true
  },
  title: {
    type: String,
    required: true
  }
})

const text = ref(props.initialText)
const textInput = ref(null)

const exportText = () => {
  const blob = new Blob([text.value], { type: 'text/plain;charset=utf-8' })
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = `${props.title}.txt`
  link.click()
  URL.revokeObjectURL(url)
}

watch(() => props.initialText, (newText) => {
  text.value = newText
  nextTick(() => {
    if (textInput.value) {
      textInput.value.$el.scrollTop = textInput.value.$el.scrollHeight
    }
  })
})

</script>

<style>
.absolute-top-right {
  position: absolute;
  top: 0;
  right: 0;
}
</style>