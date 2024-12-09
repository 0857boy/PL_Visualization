<template>
  <WebSocketComponent v-slot="{ isConnected: wsConnected, sendMessage }" @message="updateOutput">
    <q-page class="q-pa-md">
      <div class="row q-col-gutter-md">
        <div class="col-6">
          <TextArea :initialText="input" :title="inputTitle" @update:text="updateInput" />
          <q-input filled v-model="code" label="輸入程式碼" type="textarea" autogrow class="q-mt-sm">
            <q-tooltip anchor="bottom right" self="top middle">
              輸入你的程式碼，然後點擊需要的功能按鈕
            </q-tooltip>
          </q-input>
          <div class="q-mt-sm">
            <q-btn-group push>
              <q-select
                filled
                v-model="interpreterType"
                :options="interpreterOptions"
                label="Type"
                class="q-mr-sm"
                :disable="isInterpreterTypeLocked"
              >
                <q-tooltip anchor="bottom right" self="top middle">
                  選擇Interpreter類型
                </q-tooltip>
              </q-select>
              <q-btn icon="visibility" @click="visualizeAST(sendMessage)" color="secondary" round>
                <q-tooltip anchor="bottom middle" self="top middle">
                  可視化 AST
                </q-tooltip>
              </q-btn>
              <template v-if="wsConnected">
                <q-btn icon="send" @click="executeCode(sendMessage)" color="primary" round>
                  <q-tooltip anchor="bottom middle" self="top middle">
                    發送程式碼
                  </q-tooltip>
                </q-btn>
              </template>
              <template v-else>
                <div class="q-mr-sm">
                  <q-skeleton type="circle" />
                  <q-tooltip anchor="bottom middle" self="top middle">
                    websocket 未連線，請稍後再試
                  </q-tooltip>
                </div>
              </template>
            </q-btn-group>
          </div>
        </div>
        <div class="col-6">
          <TextArea :initialText="output" :title="outputTitle" readonly />
        </div>
      </div>
    </q-page>
  </WebSocketComponent>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue'
import TextArea from 'components/TextArea.vue'
import WebSocketComponent from 'components/WebSocketComponent.vue'

const code = ref('')
const output = ref('')
const input = ref('')
const inputTitle = ref('Input')
const outputTitle = ref('Output')
const interpreterType = ref('OurScheme')
const isInterpreterTypeLocked = ref(false)

const interpreterOptions = ['OurScheme', 'OurC']

const executeCode = (sendMessage) => {
  if (!interpreterType.value) {
    alert('必須選擇一個Interpreter類型')
    return
  }
  isInterpreterTypeLocked.value = true
  code.value += '\n' // 添加換行符
  input.value += code.value
  const message = {
    interpreterType: interpreterType.value,
    payload: code.value
  }
  sendMessage(JSON.stringify(message))
  code.value = '' // 清空輸入程式碼
}

const updateInput = (newInput) => {
  input.value = newInput
}

const updateOutput = (message) => {
  output.value += message // 將 WebSocket 回傳的訊息添加到 output
}

// 監聽 input 和 output 的變化，並滾動到頁面底部
const scrollToBottom = () => {
  nextTick(() => {
    window.scrollTo(-1, document.body.scrollHeight)
  })
}

watch(input, scrollToBottom)
watch(output, scrollToBottom)
</script>