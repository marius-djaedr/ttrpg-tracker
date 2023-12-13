<script setup>
    import { ref } from 'vue'
    import CrudBase from './CrudBase.vue'
    import SortHeader from '../tidbits/SortHeader.vue'

    const emit = defineEmits({
        selectCharacter: ({obj}) => {
            return true;
        }
    });
    defineExpose({onCampaignSelected,onSessionSelected});

    const crudBaseRef = ref()

    const selectedParentId = ref('')

    const modalFormId = ref('')
    const modalFormParentId = ref('')
    const modalFormName = ref('')
    const modalFormRace = ref('')
    const modalFormClassRole = ref('')
    const modalFormPronouns = ref('')
    const modalFormTragicStory = ref('')
    const modalFormDiedInGame = ref('')

    function sortOrSearch(obj){
        crudBaseRef.value.sortOrSearch(obj);
    }

    function loadModal(obj){
        if(obj.obj.parentId==null || obj.obj.parentId == ''){
            //if not update, check if create
            if(selectedParentId.value == null || selectedParentId.value == ''){
                alert("You must select a parent first");
                crudBaseRef.value.cancelModal();
            }else{
                modalFormParentId.value = selectedParentId.value
            }
        }else{
            modalFormId.value = obj.obj._id
            modalFormParentId.value = obj.obj.parentId
            modalFormName.value = obj.obj.name
            modalFormRace.value = obj.obj.race
            modalFormClassRole.value = obj.obj.classRole
            modalFormPronouns.value = obj.obj.pronouns
            modalFormTragicStory.value = obj.obj.tragicStory
            modalFormDiedInGame.value = obj.obj.diedInGame
        }
    }

    function submitModal(){
        let obj = {};
        obj._id = modalFormId.value
        obj.parentId = modalFormParentId.value
        obj.name = modalFormName.value
        obj.race = modalFormRace.value
        obj.classRole = modalFormClassRole.value
        obj.pronouns = modalFormPronouns.value
        obj.tragicStory = modalFormTragicStory.value
        obj.diedInGame = modalFormDiedInGame.value
        crudBaseRef.value.createOrUpdate(obj);
    }

    function selectCharacter(obj){
        emit('selectCharacter',obj);
    }

    function onCampaignSelected(obj){
        crudBaseRef.value.deselect();
        let id = obj.obj==null? '' : obj.obj._id;
        selectedParentId.value = id;
        crudBaseRef.value.sortOrSearch({field:'parentId', sort:false, search:id});
    }

    function onSessionSelected(obj){
        console.warn('Currently no support for up-filtering on select');
//        let id = obj.obj==null? '' : obj.obj.parentId;
//        crudBaseRef.value.sortOrSearch({field:'_id', sort:false, search:id});
    }
</script>

<template>
    <CrudBase ref="crudBaseRef" api-url-end="characters" header-text="Character" 
            @load-modal="loadModal" @submit-emit="submitModal" @select-row="selectCharacter">
        <template v-slot:header-th>
            <SortHeader @submit-field="sortOrSearch" header-data-field="name">Name</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="race">Race</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="classRole">Class/Role</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="pronouns">Pronouns</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="tragicStory">Tragic Backstory</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="diedInGame">Died in Game</SortHeader>
        </template>
        <template v-slot:table-data="{name, race, classRole, pronouns, tragicStory, diedInGame}">
            <td>{{name}}</td>
            <td>{{race}}</td>
            <td>{{classRole}}</td>
            <td>{{pronouns}}</td>
            <td>{{tragicStory}}</td>
            <td>{{diedInGame}}</td>
        </template>
        <template v-slot:modal-form>
            <input class="form-control" v-model="modalFormName" placeholder="Name">
            <input class="form-control" v-model="modalFormRace" placeholder="Race">
            <input class="form-control" v-model="modalFormClassRole" placeholder="Class/Role">
            <input class="form-control" v-model="modalFormPronouns" placeholder="Pronouns">
            <select class="form-control" v-model="modalFormTragicStory">
                <option value="">unknown</option>
                <option value="true">Tragic Story</option>
                <option value="false">Not Tragic</option>
            </select>
            <select class="form-control" v-model="modalFormDiedInGame">
                <option value="">unknown</option>
                <option value="true">Died In Game</option>
                <option value="false">Lived</option>
            </select>
        </template>
    </CrudBase>
</template>