<script lang="ts" setup>
import { ref, watch, nextTick } from 'vue'
import { fetchEventSource } from '@microsoft/fetch-event-source'

const file = ref<File | null>(null)
const messages = ref([
  { role: 'assistant', content: '안녕하세요~ sleep-jelly 음성 도우미입니다. 요약을 원하시는 MP3 파일을 업로드해 주세요.' }
])
const scrollArea = ref<HTMLElement | null>(null)
const isLoading = ref(false)

const handleFileUpload = (e: Event) => {
  const target = e.target as HTMLInputElement
  if (target.files?.length) {
    file.value = target.files[0]
  }
}

const submitFile = async () => {
  if (!file.value) {
    alert('MP3 파일을 선택해주세요.')
    return
  }

  const formData = new FormData()
  formData.append('file', file.value)
  formData.append('prompt', '')      // empty userPrompt
  formData.append('history', '')     // no chat history

  const assistantMessage = { role: 'assistant', content: '' }
  messages.value.push({ role: 'user', content: `[음성 파일 업로드: ${file.value.name}]` })
  messages.value.push(assistantMessage)

  isLoading.value = true

  try {
    await fetchEventSource('/api/OpenAI/StreamAudioSum', {
      method: 'POST',
      body: formData, // <-- still use formData for file upload
      async onopen(response) {
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`)
        }
      },
      onmessage(ev) {
        assistantMessage.content += ev.data // append directly, NO REPLACE
        messages.value = [...messages.value] // reactivity
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
    alert('파일 처리에 실패했습니다.')
    isLoading.value = false
  }
}

watch(messages, async () => {
  await nextTick()
  if (scrollArea.value) {
    scrollArea.value.scrollTop = scrollArea.value.scrollHeight
  }
})
</script>


<template>
  <main id="main" class="main">
    <div class="pagetitle">
      <h1>sleep-jelly</h1>
      <nav>
        <ol class="breadcrumb">
          <li class="breadcrumb-item"><a href="/">sleep-jelly</a></li>
          <li class="breadcrumb-item active">Audio-Summary</li>
        </ol>
      </nav>
    </div>

    <section class="section dashboard">
      <div class="row">
        <div class="col-lg-12">
          <div class="row">
            <div class="post-item clearfix">
              <div class="col-xxl-12 col-md-12">
                <div class="card">
                  <div class="card-body">
                    <h5 class="card-title">Audio-Summary</h5>

                    <div class="chat-container">
                      <div class="messages" ref="scrollArea">
                        <div v-for="(msg, index) in messages" :key="index" :class="['message', msg.role]">
                          <div v-if="msg.role === 'assistant'" class="bubble" v-html="msg.content"></div>
                          <div v-else class="bubble">{{ msg.content }}</div>
                        </div>
                        <div v-if="isLoading" class="loading-indicator">Typing...</div>
                      </div>

                      <form @submit.prevent="submitFile" class="input-bar">
                        <input type="file" accept="audio/mp3" @change="handleFileUpload" class="form-control" />
                        <button type="submit" class="send-btn" :disabled="isLoading || !file">Upload</button>
                      </form>
                    </div>

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