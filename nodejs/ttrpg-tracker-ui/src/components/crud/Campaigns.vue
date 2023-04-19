<script setup>
    import { ref } from 'vue'
    import CrudBase from './CrudBase.vue'
    import SortHeader from './SortHeader.vue'
    const crudBaseRef = ref()

    function sortOrSearch(obj){
        crudBaseRef.value.sortOrSearch(obj);
    }

    function statusSortOrSearch(obj){
        //TODO map from search field to true/false/null
        crudBaseRef.value.sortOrSearch(obj);
    }
</script>

<template>
    <CrudBase ref="crudBaseRef" api-url-end="campaigns" header-text="Campaign List">
        <template v-slot:header-th>
            <SortHeader @submit-field="sortOrSearch" header-data-field="Name">Name</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="System">System</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="Gm">Gm</SortHeader>
            <SortHeader @submit-field="statusSortOrSearch" header-data-field="Completed">Status</SortHeader>
        </template>
        <template v-slot:table-data="{Name, System, Gm, Completed}">
            <td>{{Name}}</td>
            <td>{{System}}</td>
            <td>{{Gm}}</td>
            <td>
                <!-- TODO double check logic-->
                <span v-if="Completed == null">ongoing</span>
                <span v-else-if="Completed == true">completed</span>
                <span v-else>abandoned</span>
            </td>
        </template>
    </CrudBase>
</template>