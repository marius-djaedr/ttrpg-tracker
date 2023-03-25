<script setup>
import CrudBase from './CrudBase.vue'
</script>

<template>
    <CrudBase api-url-end="sessions" single-url-end="session">
        <template v-slot:header-th>
            <th></th>
            <th>Date</th>
            <th>Short Session</th>
            <th>Play or Ran?</th>
        </template>
        <template v-slot:table-data="{ParentId, Date, ShortSession, PlayedWithoutCharacter}">
            <td>
                <!-- TODO double check logic-->
                <router-link v-if="PlayedWithoutCharacter != null" class="btn btn-primary btn-sm" v-bind:to="`/campaign/${ParentId}`">Parent Campaign</router-link>
                <router-link v-else class="btn btn-primary btn-sm" v-bind:to="`/character/${ParentId}`">Parent Character</router-link>
            </td>
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