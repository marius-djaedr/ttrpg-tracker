<script setup>
    import { ref, watchEffect } from 'vue'
    const props = defineProps(['apiUrlEnd']);
    const data = ref([]);

    const apiUrl = `http://localhost:3300/api/`+props.apiUrlEnd;

    data.value = await (await fetch(apiUrl)).json();

</script>

<template>
   
</template>