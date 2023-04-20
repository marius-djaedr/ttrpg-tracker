<script setup>
    import { ref } from 'vue'
    import CrudBase from './CrudBase.vue'
    import SortHeader from '../tidbits/SortHeader.vue'
    const crudBaseRef = ref()

    function sortOrSearch(obj){
        crudBaseRef.value.sortOrSearch(obj);
    }
</script>

<template>
    <CrudBase ref="crudBaseRef" api-url-end="characters" header-text="Character">
        <template v-slot:header-th>
            <SortHeader @submit-field="sortOrSearch" header-data-field="Name">Name</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="Race">Race</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="ClassRole">Class/Role</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="Gender">Gender</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="TragicStory">Tragic Backstory</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="DiedInGame">Died in Game</SortHeader>
        </template>
        <template v-slot:table-data="{ParentId, Name, Race, ClassRole, Gender, TragicStory, DiedInGame}">
            <td>{{Name}}</td>
            <td>{{Race}}</td>
            <td>{{ClassRole}}</td>
            <td>{{Gender}}</td>
            <td>{{TragicStory}}</td>
            <td>{{DiedInGame}}</td>
        </template>
    </CrudBase>
</template>