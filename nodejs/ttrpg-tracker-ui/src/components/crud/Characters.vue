<script setup>
    import { ref } from 'vue'
    import CrudBase from './CrudBase.vue'
    import SortHeader from '../tidbits/SortHeader.vue'
    const crudBaseRef = ref()

    const modalFormId = ref('')
    const modalFormParentId = ref('')
    const modalFormName = ref('')
    const modalFormRace = ref('')
    const modalFormClassRole = ref('')
    const modalFormGender = ref('')
    const modalFormTragicStory = ref('')
    const modalFormDiedInGame = ref('')

    function sortOrSearch(obj){
        crudBaseRef.value.sortOrSearch(obj);
    }

    function loadModal(obj){
        if(obj.obj.ParentId==null || obj.obj.ParentId == ''){
            alert("You must select a parent first");
            crudBaseRef.value.cancelModal();
        }else{
            modalFormId.value = obj.obj._id
            modalFormParentId.value = obj.obj.ParentId
            modalFormName.value = obj.obj.Name
            modalFormRace.value = obj.obj.Race
            modalFormClassRole.value = obj.obj.ClassRole
            modalFormGender.value = obj.obj.Gender
            modalFormTragicStory.value = obj.obj.TragicStory
            modalFormDiedInGame.value = obj.obj.DiedInGame
        }
    }

    function submitModal(){
        let obj = {};
        obj._id = modalFormId.value
        obj.ParentId = modalFormParentId.value
        obj.Name = modalFormName.value
        obj.Race = modalFormRace.value
        obj.ClassRole = modalFormClassRole.value
        obj.Gender = modalFormGender.value
        obj.TragicStory = modalFormTragicStory.value
        obj.DiedInGame = modalFormDiedInGame.value
        crudBaseRef.value.createOrUpdate(obj);
    }
</script>

<template>
    <CrudBase ref="crudBaseRef" api-url-end="characters" header-text="Character" @load-modal="loadModal" @submit-emit="submitModal">
        <template v-slot:header-th>
            <SortHeader @submit-field="sortOrSearch" header-data-field="Name">Name</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="Race">Race</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="ClassRole">Class/Role</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="Gender">Gender</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="TragicStory">Tragic Backstory</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="DiedInGame">Died in Game</SortHeader>
        </template>
        <template v-slot:table-data="{ParentId, Name, Race, ClassRole, Gender, TragicStory, DiedInGame}">
            <td>{{Name}}</td>
            <td>{{Race}}</td>
            <td>{{ClassRole}}</td>
            <td>{{Gender}}</td>
            <td>{{TragicStory}}</td>
            <td>{{DiedInGame}}</td>
        </template>
        <template v-slot:modal-form>
            <input v-model="modalFormName" placeholder="Name">
            <input v-model="modalFormRace" placeholder="Race">
            <input v-model="modalFormClassRole" placeholder="Class/Role">
            <input v-model="modalFormGender" placeholder="Gender">
            <select v-model="modalFormTragicStory">
                <option value="">unknown</option>
                <option value="true">Tragic Story</option>
                <option value="false">Not Tragic</option>
            </select>
            <select v-model="modalFormDiedInGame">
                <option value="">unknown</option>
                <option value="true">Died In Game</option>
                <option value="false">Lived</option>
            </select>
        </template>
    </CrudBase>
</template>