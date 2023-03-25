<script setup>
    import { ref } from 'vue'
    const props = defineProps(['id']);
    const campaign = ref({});
    const characters = ref([]);
    const sessions = ref([]);
    
    //TODO currently this is all still sync-loaded. Need to make it async with a loading icon
    campaign.value = await (await fetch(`http://localhost:3300/api/campaigns/`+props.id)).json();
    characters.value = await (await fetch(`http://localhost:3300/api/campaigns/`+props.id+`/characters`)).json();
    sessions.value = await (await fetch(`http://localhost:3300/api/campaigns/`+props.id+`/sessions`)).json();
</script>

<template>
   CAMPAIGN DETAIL PAGE [{{ id }}]

   <div>{{ campaign }}</div>
   <div>{{ characters }}</div>
   <div>{{ sessions }}</div>
</template>