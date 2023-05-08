<script setup>
    import { ref } from 'vue'
    import CrudBase from './CrudBase.vue'
    import SortHeader from '../tidbits/SortHeader.vue'

    
    const crudBaseRef = ref()

    const modalFormId = ref('')
    const modalFormName = ref('')
    const modalFormDice = ref('')
    const modalFormClassed = ref('')
    const modalFormFramework = ref('')
    const modalFormPublisher = ref('')
    const modalFormDesigner = ref('')

    function sortOrSearch(obj){
        crudBaseRef.value.sortOrSearch(obj);
    }

    function loadModal(obj){
        modalFormId.value = obj.obj._id
        modalFormName.value = obj.obj.name
        modalFormDice.value = obj.obj.dice
        modalFormClassed.value = obj.obj.classed
        modalFormFramework.value = obj.obj.framework
        modalFormPublisher.value = obj.obj.publisher
        modalFormDesigner.value = obj.obj.designer
    }

    function submitModal(){
        let obj = {};
        obj._id = modalFormId.value;
        obj.name = modalFormName.value;
        obj.dice = modalFormDice.value;
        obj.classed = modalFormClassed.value;
        obj.framework = modalFormFramework.value;
        obj.publisher = modalFormPublisher.value;
        obj.designer = modalFormDesigner.value;
        crudBaseRef.value.createOrUpdate(obj);
    }
</script>


<template>
    <CrudBase ref="crudBaseRef" api-url-end="systems" header-text="System Reference"
            @load-modal="loadModal" @submit-emit="submitModal">
        <template v-slot:header-th>
            <SortHeader @submit-field="sortOrSearch" header-data-field="name">Name</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="dice">Dice</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="classed">Classed</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="framework">Framework</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="publisher">Publisher</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="designer">Designer</SortHeader>
        </template>
        <template v-slot:table-data="{name, dice, classed, framework, publisher, designer}">
            <td>{{name}}</td>
            <td>{{dice}}</td>
            <td>{{classed}}</td>
            <td>{{framework}}</td>
            <td>{{publisher}}</td>
            <td>{{designer}}</td>
        </template>
        <template v-slot:modal-form>
            <input v-model="modalFormName" placeholder="Name">
            <input v-model="modalFormDice" placeholder="Dice">
            <input v-model="modalFormClassed" placeholder="Classed">
            <input v-model="modalFormFramework" placeholder="Framework">
            <input v-model="modalFormPublisher" placeholder="Publisher">
            <input v-model="modalFormDesigner" placeholder="Designer">
        </template>
    </CrudBase>
</template>