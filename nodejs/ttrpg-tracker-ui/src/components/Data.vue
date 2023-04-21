<script setup>
    import { ref } from 'vue'
    import Campaigns from './crud/Campaigns.vue'
    import Characters from './crud/Characters.vue'
    import Sessions from './crud/Sessions.vue'

    
    const campaignRef = ref()
    const characterRef = ref()
    const sessionRef = ref()

    function selectCampaign(obj){
        characterRef.value.onCampaignSelected(obj);
        sessionRef.value.onCampaignSelected(obj);
    }

    function selectCharacter(obj){
        campaignRef.value.onCharacterSelected(obj);
        sessionRef.value.onCharacterSelected(obj);
    }

    function selectSession(obj){
        campaignRef.value.onSessionSelected(obj);
        characterRef.value.onSessionSelected(obj);
    }
</script>

<template>
    <Campaigns ref="campaignRef" @select-campaign="selectCampaign"></Campaigns>
    <Characters ref="characterRef" @select-character="selectCharacter"></Characters>
    <Sessions ref="sessionRef" @select-session="selectSession"></Sessions>
</template>