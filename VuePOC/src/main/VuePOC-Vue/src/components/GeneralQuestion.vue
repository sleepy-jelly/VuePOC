<script lang="ts" setup>
import { ref, watch, nextTick } from 'vue'
import { fetchEventSource } from '@microsoft/fetch-event-source'

// ====== State ======
const inputUserPrompt = ref('')
const messages = ref([{ role: 'assistant', content: '안녕하세요~ sleep-jelly 입니다 무엇을 도와드릴까요?' }])
const scrollArea = ref<HTMLElement | null>(null)
const isLoading = ref(false)

// ====== Methods ======
async function sendMessage() {
  const prompt = inputUserPrompt.value.trim()
  const history = messages.value


  if (!prompt) return

  // Add user message
  messages.value.push({ role: 'user', content: prompt })

  // Prepare assistant message
  const assistantMessage = { role: 'assistant', content: '' }
  messages.value.push(assistantMessage)

  // Clear input
  inputUserPrompt.value = ''
  isLoading.value = true

  try {
    await fetchEventSource('/api/OpenAI/StreamChat', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ 
        userPrompt: prompt, 
        historyJson: JSON.stringify(history)
      }),
      async onopen(response) {
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`)
      },
      onmessage(ev) {
        assistantMessage.content += ev.data;
        messages.value = [...messages.value]
      },
      onerror(err) {
        console.error('Streaming error:', err)
        throw err
      },
      onclose() {
        isLoading.value = false
      }
    })
  } catch (err) {
    alert('Something went wrong while chatting with AI.')
    isLoading.value = false
  }
}

// ====== Auto Scroll ======
watch(messages, async () => {
  await nextTick()
  if (scrollArea.value) {
    scrollArea.value.scrollTop = scrollArea.value.scrollHeight
  }
})

</script>

<template>
  <!-- Main Content -->
  <main id="main" class="main">
      <div class="pagetitle">
          <h1>sleep-jelly</h1>
          <nav>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/">sleep-jelly</a></li>
                <li class="breadcrumb-item active">general-question</li>
            </ol>
          </nav>
      </div>

      <section class="section dashboard">
        <div class="row">
          <div class="col-lg-12">
            <div class="row">
                <div class="post-item clearfix">
                    <div class="col-xxl-12 col-md-12"></div>
                      <div class="card">
                        <div class="card-body">
                          <h5 class="card-title">General Question</h5>
                          <div class="chat-container">
                            <div class="messages" ref="scrollArea">
                              <div v-for="(msg, index) in messages" :key="index" :class="['message', msg.role]">
                                <div v-if="msg.role === 'assistant'" class="bubble" v-html="msg.content"></div>
                                <div v-else class="bubble">{{ msg.content }}</div>
                              </div>
                              <div v-if="isLoading" class="loading-indicator">Typing...</div>
                            </div>

                            <form @submit.prevent="sendMessage" class="input-bar">
                              <input
                                v-model="inputUserPrompt"
                                type="text"
                                placeholder="Send a message..."
                                class="chat-input"
                                :disabled="isLoading"
                              />
                              <button type="submit" class="send-btn" :disabled="isLoading">Send</button>
                            </form>
                          </div>
                      </div>
                  </div>
                </div>
              </div>
            </div>
        </div>
      </section>
  </main>
</template>

<style scoped>
  @import '../../src/index.css';

.chat-container {
  display: flex;
  flex-direction: column;
  height: 70vh;
  max-width: 1480px;
  margin: auto;
  background: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.messages {
  flex: 1;
  padding: 1rem;
  overflow-y: auto;
}

.message {
  display: flex;
  margin-bottom: 0.5rem;
}

.message.user {
  justify-content: flex-end;
}

.message.assistant {
  justify-content: flex-start;
}

.bubble {
  max-width: 70%;
  padding: 0.75rem 1rem;
  border-radius: 1rem;
  background: #e5e5ea;
  color: #000;
  white-space: pre-wrap;
  word-wrap: break-word; 
}


.message.user .bubble {
  background: #007bff;
  color: #fff;
}

.input-bar {
  display: flex;
  padding: 0.75rem;
  border-top: 1px solid #ddd;
}

.chat-input {
  flex: 1;
  padding: 0.5rem 1rem;
  border-radius: 1rem;
  border: 1px solid #ccc;
}

.send-btn {
  margin-left: 0.5rem;
  padding: 0.5rem 1rem;
  border: none;
  background: #007bff;
  color: white;
  border-radius: 1rem;
  cursor: pointer;
}
</style>