<script setup>
    import CrudBase from './CrudBase.vue'
    import moment from 'moment'
</script>

<template>
    <CrudBase api-url-end="sessions" header-text="Session List">
        <template v-slot:header-th>
            <th>Date</th>
            <th>Short Session</th>
            <th>Play or Ran?</th>
        </template>
        <template v-slot:table-data="{ParentId, Date, ShortSession, PlayedWithoutCharacter}">
            <td>{{moment(Date).format("YYYY-MM-DD")}}</td>
            <td>{{ShortSession}}</td>
            <td>
                <!-- TODO double check logic-->
                <span v-if="PlayedWithoutCharacter == null || PlayedWithoutCharacter == true">Played</span>
                <span v-else>Ran</span>
            </td>
        </template>
    </CrudBase>
</template>