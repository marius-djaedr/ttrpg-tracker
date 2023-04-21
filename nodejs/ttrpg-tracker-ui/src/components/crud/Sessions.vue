<script setup>
    import { ref } from 'vue'
    import CrudBase from './CrudBase.vue'
    import moment from 'moment'
    import Datepicker from '@vuepic/vue-datepicker';
    import '@vuepic/vue-datepicker/dist/main.css';
    import SortHeader from '../tidbits/SortHeader.vue'

    const emit = defineEmits({
        selectSession: ({obj}) => {
            return true;
        }
    });
    defineExpose({onCampaignSelected,onCharacterSelected});

    const crudBaseRef = ref()

    const modalFormId = ref('')
    const modalFormParentId = ref('')
    const modalFormDate = ref('')
    const modalFormShortSession = ref('')
    const modalFormPlayedWithoutCharacter = ref('')

    function sortOrSearch(obj){
        crudBaseRef.value.sortOrSearch(obj);
    }

    function playedSortOrSearch(obj){
        //TODO map from search field to true/false/null
        crudBaseRef.value.sortOrSearch(obj);
    }

    function loadModal(obj){
        if(obj.obj.ParentId==null || obj.obj.ParentId == ''){
            alert("You must select a parent first");
            crudBaseRef.value.cancelModal();
        }else{
            modalFormId.value = obj.obj._id
            modalFormParentId.value = obj.obj.ParentId
            modalFormDate.value = obj.obj.Date
            modalFormShortSession.value = obj.obj.ShortSession
            modalFormPlayedWithoutCharacter.value = obj.obj.PlayedWithoutCharacter
        }
    }

    function submitModal(){
        let obj = {};
        obj._id = modalFormId.value
        obj.ParentId = modalFormParentId.value
        obj.Date = modalFormDate.value
        obj.ShortSession = modalFormShortSession.value
        obj.PlayedWithoutCharacter = modalFormPlayedWithoutCharacter.value
        crudBaseRef.value.createOrUpdate(obj);
    }

    function selectSession(obj){
        console.log('selectSession');
        emit('selectSession',obj);
    }

    function onCampaignSelected(obj){
        console.log('onCampaignSelected');
        crudBaseRef.value.deselect();
        let id = obj.obj==null? '' : obj.obj._id;
        crudBaseRef.value.sortOrSearch({field:'ParentId', sort:false, search:id});
        //TODO this currently does not show sessions associated with characters in the campaign, only sessions I ran or played without character
    }

    function onCharacterSelected(obj){
        console.log('onCharacterSelected');
        crudBaseRef.value.deselect();
        let id = obj.obj==null? '' : obj.obj._id;
        crudBaseRef.value.sortOrSearch({field:'ParentId', sort:false, search:id});
    }
</script>

<template>
    <CrudBase ref="crudBaseRef" api-url-end="sessions" header-text="Session" 
            @load-modal="loadModal" @submit-emit="submitModal" @select-row="selectSession">
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
        <template v-slot:modal-form>
            <Datepicker v-model="modalFormDate" />
            <select v-model="modalFormShortSession">
                <option value="true">Short</option>
                <option value="false">Regular</option>
            </select>
            <select v-model="PlayedWithoutCharacter">
                <option value="">Play No Character</option>
                <option value="true">Play Character</option>
                <option value="false">Ran</option>
            </select>
        </template>
    </CrudBase>
</template>