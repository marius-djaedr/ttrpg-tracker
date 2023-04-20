<script setup>
    import { ref } from 'vue'
    import CrudBase from './CrudBase.vue'
    import moment from 'moment'
    import SortHeader from '../tidbits/SortHeader.vue'
    const crudBaseRef = ref()

    function sortOrSearch(obj){
        crudBaseRef.value.sortOrSearch(obj);
    }

    function playedSortOrSearch(obj){
        //TODO map from search field to true/false/null
        crudBaseRef.value.sortOrSearch(obj);
    }
</script>

<template>
    <CrudBase ref="crudBaseRef" api-url-end="sessions" header-text="Session">
        <template v-slot:header-th>
            <SortHeader @submit-field="sortOrSearch" header-data-field="Date">Date</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="ShortSession">Short Session</SortHeader>
            <SortHeader @submit-field="playedSortOrSearch" header-data-field="PlayedWithoutCharacter">Play or Ran?</SortHeader>
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