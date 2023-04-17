<script setup>
import CrudBase from './CrudBase.vue'
</script>

<template>
    <h3>Character List</h3>
    <CrudBase api-url-end="characters" single-url-end="character">
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