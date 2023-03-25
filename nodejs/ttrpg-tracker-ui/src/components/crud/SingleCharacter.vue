<script setup>
    import { ref } from 'vue'
    const props = defineProps(['id']);
    const character = ref({});
    const sessions = ref([]);
    
    //TODO currently this is all still sync-loaded. Need to make it async with a loading icon
    character.value = await (await fetch(`http://localhost:3300/api/characters/`+props.id)).json();
    sessions.value = await (await fetch(`http://localhost:3300/api/characters/`+props.id+`/sessions`)).json();
</script>

<template>
   CHARACTER DETAIL PAGE [{{ id }}]

   <div>{{ character }}</div>
   <div>{{ sessions }}</div>
</template>