<template>
  <q-card >
    <q-card-section class="tight-card-section">
      <div class="text-h6">{{ title }}</div>
      <q-btn flat rounded icon="download" class="absolute-top-right q-mt-xs q-mr-xs" @click="exportText" />
      <q-input v-model="text" readonly filled autogrow type="textarea" class="wrap tight-input" ref="textInput" />
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

.tight-card-section {
  padding: 8px;
  /* 調整內邊距 */
}

.tight-input .q-field__control {
  padding: 4px;
  /* 調整內邊距 */
}

.tight-input .q-field__control-container {
  margin: 0;
  /* 調整外邊距 */
}
</style>