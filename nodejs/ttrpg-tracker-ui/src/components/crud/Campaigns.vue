<script setup>
import { ref } from 'vue'
import CrudBase from './CrudBase.vue'
const crudBaseRef = ref()

</script>

<template>
    <CrudBase ref="crudBaseRef" api-url-end="campaigns" header-text="Campaign List">
        <template v-slot:header-th>
            <th>Name</th>
            <th @click="crudBaseRef.sort('System')">System</th>
            <th>GM</th>
            <th>Completed</th>
        </template>
        <template v-slot:table-data="{Name, System, Gm, Completed}">
            <td>{{Name}}</td>
            <td>{{System}}</td>
            <td>{{Gm}}</td>
            <td>{{Completed}}</td>
        </template>
    </CrudBase>
</template>