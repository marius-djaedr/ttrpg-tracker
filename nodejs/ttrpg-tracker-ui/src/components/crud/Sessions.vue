<script setup>
import CrudBase from './CrudBase.vue'
</script>

<template>
    <h3>Session List</h3>
    <CrudBase api-url-end="sessions" single-url-end="session">
        <template v-slot:header-th>
            <th>Date</th>
            <th>Short Session</th>
            <th>Play or Ran?</th>
        </template>
        <template v-slot:table-data="{ParentId, Date, ShortSession, PlayedWithoutCharacter}">
            <td>{{Date}}</td>
            <td>{{ShortSession}}</td>
            <td>
                <!-- TODO double check logic-->
                <span v-if="PlayedWithoutCharacter == null || PlayedWithoutCharacter == true">Played</span>
                <span v-else>Ran</span>
            </td>
        </template>
    </CrudBase>
</template>