<script setup>
import CrudBase from './CrudBase.vue'
</script>

<template>
    <h3>Campaign List</h3>
    <CrudBase api-url-end="campaigns" single-url-end="campaign">
        <template v-slot:header-th>
            <th>Name</th>
            <th>System</th>
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