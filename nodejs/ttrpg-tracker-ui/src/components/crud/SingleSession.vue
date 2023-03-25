<script setup>
    import { ref } from 'vue'
    const props = defineProps(['id']);
    const session = ref({});
    
    //TODO currently this is all still sync-loaded. Need to make it async with a loading icon
    session.value = await (await fetch(`http://localhost:3300/api/sessions/`+props.id)).json();
</script>

<template>
   SESSION DETAIL PAGE [{{ id }}]

   <div>{{ session }}</div>
</template>