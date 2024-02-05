<script setup>
    import { ref } from 'vue'
    import CrudBase from './CrudBase.vue'
    import moment from 'moment'
    import VueDatepicker from '@vuepic/vue-datepicker';
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
    const modalFormDuration = ref('')
    const modalFormPlayed = ref('')

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
            console.info(obj)
            modalFormId.value = obj.obj._id
            modalFormParentId.value = obj.obj.parentId
            modalFormDate.value = moment(obj.obj.date)
            modalFormDuration.value = obj.obj.duration
            modalFormPlayed.value = obj.obj.played
        }
    }

    function submitModal(){
        console.log(modalFormDate.value)
        console.log(moment(modalFormDate.value))
        let obj = {};
        obj._id = modalFormId.value
        obj.parentId = modalFormParentId.value
        obj.date = moment(modalFormDate.value).format("YYYY-MM-DD")
        obj.duration = modalFormDuration.value
        obj.played = modalFormPlayed.value
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

    function format(date){
        return moment(date).format("YYYY-MM-DD")
    }
</script>

<template>
    <CrudBase ref="crudBaseRef" api-url-end="sessions" header-text="Session" 
            @load-modal="loadModal" @submit-emit="submitModal" @select-row="selectSession">
        <template v-slot:header-th>
            <SortHeader @submit-field="sortOrSearch" header-data-field="date">Date</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="duration">Short Session</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="played">Play or Ran?</SortHeader>
        </template>
        <template v-slot:table-data="{date, duration, played}">
            <td>{{moment(date).format("YYYY-MM-DD")}}</td>
            <td>{{duration}}</td>
            <td>{{played}}</td>
        </template>
        <template v-slot:modal-form>
            <VueDatepicker v-model="modalFormDate" :enable-time-picker="false" :format="format" />
            <select class="form-control" v-model="modalFormDuration">
                <option value="Regular">Regular</option>
                <option value="Short">Short</option>
            </select>
            <select class="form-control" v-model="modalFormPlayed">
                <option value="Ran">Ran</option>
                <option value="Played">Played</option>
                <option value="Played (no character)">Played (no character)</option>
            </select>
        </template>
    </CrudBase>
</template>