<script setup lang="ts">
defineProps<{
  msg: string
}>()
</script>

<template>
  <div class="greetings">
    <h1 class="green">{{ msg }}</h1>
    <h3>
        style tho!!!!
    </h3>
  </div>
</template>

<style scoped>

    @import '../assets/base.css';

</style>
