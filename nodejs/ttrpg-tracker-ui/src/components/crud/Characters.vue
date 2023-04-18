<script setup>
import CrudBase from './CrudBase.vue'
</script>

<template>
    <CrudBase api-url-end="characters" header-text="Character List">
        <template v-slot:header-th>
            <th>Name</th>
            <th>Race</th>
            <th>Class/Role</th>
            <th>Gender</th>
            <th>Tragic Backstory</th>
            <th>Died in Game</th>
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