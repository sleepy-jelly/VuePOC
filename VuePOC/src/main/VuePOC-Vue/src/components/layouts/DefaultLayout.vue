<template>
    <div>
        <CommonHeader/>
            <Sidebar />
                <main id="main" class="main">
            <slot/>
        </main>
    </div>
</template>
  
<script setup lang="ts">
    import CommonHeader from './Header.vue'
    import Sidebar from './Sidebar.vue'
</script>