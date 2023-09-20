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

    const selectedParentId = ref('')

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
        if(obj.obj.parentId==null || obj.obj.parentId == ''){
            //if not update, check if create
            if(selectedParentId.value == null || selectedParentId.value == ''){
                alert("You must select a parent first");
                crudBaseRef.value.cancelModal();
            }else{
                modalFormParentId.value = selectedParentId.value
            }
        }else{
            console.info(obj)
            modalFormId.value = obj.obj._id
            modalFormParentId.value = obj.obj.parentId
            modalFormDate.value = obj.obj.date
            modalFormShortSession.value = obj.obj.shortSession
            modalFormPlayedWithoutCharacter.value = obj.obj.playedWithoutCharacter
        }
    }

    function submitModal(){
        let obj = {};
        obj._id = modalFormId.value
        obj.parentId = modalFormParentId.value
        obj.date = modalFormDate.value
        obj.shortSession = modalFormShortSession.value
        obj.playedWithoutCharacter = modalFormPlayedWithoutCharacter.value
        crudBaseRef.value.createOrUpdate(obj);
    }

    function selectSession(obj){
        emit('selectSession',obj);
    }

    function onCampaignSelected(obj){
        crudBaseRef.value.deselect();
        let id = obj.obj==null? '' : obj.obj._id;
        selectedParentId.value = id;
        crudBaseRef.value.sortOrSearch({field:'parentId', sort:false, search:id});
        //TODO this currently does not show sessions associated with characters in the campaign, only sessions I ran or played without character
    }

    function onCharacterSelected(obj){
        crudBaseRef.value.deselect();
        let id = obj.obj==null? '' : obj.obj._id;
        selectedParentId.value = id;
        crudBaseRef.value.sortOrSearch({field:'parentId', sort:false, search:id});
    }
</script>

<template>
    <CrudBase ref="crudBaseRef" api-url-end="sessions" header-text="Session" 
            @load-modal="loadModal" @submit-emit="submitModal" @select-row="selectSession">
        <template v-slot:header-th>
            <SortHeader @submit-field="sortOrSearch" header-data-field="date">Date</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="shortSession">Short Session</SortHeader>
            <SortHeader @submit-field="playedSortOrSearch" header-data-field="playedWithoutCharacter">Play or Ran?</SortHeader>
        </template>
        <template v-slot:table-data="{date, shortSession, playedWithoutCharacter}">
            <td>{{moment(date).format("YYYY-MM-DD")}}</td>
            <td>{{shortSession}}</td>
            <td>
                <!-- TODO double check logic-->
                <span v-if="playedWithoutCharacter === null || playedWithoutCharacter === '' || playedWithoutCharacter === true">Played</span>
                <span v-else>Ran</span>
            </td>
        </template>
        <template v-slot:modal-form>
            <Datepicker v-model="modalFormDate" />
            <select class="form-control" v-model="modalFormShortSession">
                <option value="true">Short</option>
                <option value="false">Regular</option>
            </select>
            <select class="form-control" v-model="modalFormPlayedWithoutCharacter">
                <option value="true">Play No Character</option>
                <option value="">Play Character</option>
                <option value="false">Ran</option>
            </select>
        </template>
    </CrudBase>
</template>