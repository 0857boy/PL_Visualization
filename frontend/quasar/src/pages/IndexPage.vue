<template>
  <WebSocketComponent v-slot="{ isConnected: wsConnected, sendMessage, connect, disconnect, connecting }"
    @message="updateOutput" @connected="handleConnected" @disconnected="handleDisconnected">
    <q-page class="q-pa-md">
      <div class="row q-col-gutter-md" style="text-align: center">
        <div class="col-12">
          <q-btn-group push>
            <q-select filled v-model="interpreterType" :options="interpreterOptions" label="文法"
              :disable="isInterpreterTypeLocked">
              <q-tooltip anchor="bottom right" self="top middle"> 選擇Interpreter文法 </q-tooltip>
            </q-select>
            <template v-if="wsConnected">
              <q-btn icon="link_off" @click="disconnect" color="red" round>
                <q-tooltip anchor="bottom middle" self="top middle"> 中斷連線 </q-tooltip>
              </q-btn>
            </template>
            <template v-else>
              <q-btn icon="link" @click="() => connect(interpreterType)" color="primary" round v-if="!connecting">
                <q-tooltip anchor="bottom middle" self="top middle"> 連線到Interpreter </q-tooltip>
              </q-btn>
              <q-spinner v-else color="primary" />
            </template>
          </q-btn-group>
          <div class="col-12">
            <q-input filled v-model="code" label="輸入程式碼" type="textarea" autogrow class="q-mt-sm" :autocorrect="off"
              :spellcheck="false">
              <template v-slot:before>
                <template v-if="!executing">
                  <q-btn v-if="wsConnected" icon="play_arrow" @click="executeCode(sendMessage)" color="green" round
                    size="xs">
                    <q-tooltip anchor="bottom middle" self="top middle"> 執行程式碼 </q-tooltip>
                  </q-btn>
                </template>
                <q-spinner v-else color="green" size="xs" />
              </template>
            </q-input>
          </div>
        </div>
        <div class="col-6">
          <TextArea :initialText="input" :title="inputTitle" @update:text="updateInput" />
        </div>
        <div class="col-6">
          <TextArea :initialText="output" :title="outputTitle" readonly />
        </div>
      </div>
    </q-page>
  </WebSocketComponent>
</template>

<script setup>
import { ref } from 'vue'
import { useQuasar } from 'quasar'
import TextArea from 'components/TextArea.vue'
import WebSocketComponent from 'components/WebSocketComponent.vue'

const $q = useQuasar()

const code = ref('')
const output = ref('')
const input = ref('')
const inputTitle = ref('Input')
const outputTitle = ref('Output')
const interpreterType = ref('OurScheme')
const isInterpreterTypeLocked = ref(false)
const executing = ref(false)

const interpreterOptions = ['OurScheme', 'OurC']

const executeCode = (sendMessage) => {
  if (!interpreterType.value) {
    alert('必須選擇一個Interpreter類型')
    return
  }
  executing.value = true
  code.value += '\n' // 添加換行符
  input.value += code.value
  const message = {
    interpreterType: interpreterType.value,
    payload: code.value,
  }
  sendMessage(JSON.stringify(message))
  code.value = '' // 清空輸入程式碼
  setTimeout(() => {
    executing.value = false
  }, 1000) // 模擬執行完成後的狀態變更
}

const updateInput = (newInput) => {
  input.value = newInput
}

const updateOutput = (message) => {
  output.value += message // 將 WebSocket 回傳的訊息添加到 output
}

const lockInterpreterType = () => {
  isInterpreterTypeLocked.value = true
}

const unlockInterpreterType = () => {
  isInterpreterTypeLocked.value = false
}

const clearInputOutput = () => {
  input.value = ''
  output.value = ''
}

const handleConnected = () => {
  lockInterpreterType()
  clearInputOutput()
  $q.notify({
    type: 'positive',
    message: '連線成功',
    timeout: 1200,
    position: 'top',
  })
}

const handleDisconnected = () => {
  unlockInterpreterType()
  $q.notify({
    type: 'warning',
    message: '連線取消',
    timeout: 1200,
    position: 'top',
  })
}
</script>