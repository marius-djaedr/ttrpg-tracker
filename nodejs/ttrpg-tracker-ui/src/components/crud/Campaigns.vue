<script setup>
    import { ref } from 'vue'
    import CrudBase from './CrudBase.vue'
    import SortHeader from '../tidbits/SortHeader.vue'

    const emit = defineEmits({
        selectCampaign: ({obj}) => {
            return true;
        }
    });
    defineExpose({onCharacterSelected,onSessionSelected});

    const crudBaseRef = ref()

    const modalFormId = ref('')
    const modalFormName = ref('')
    const modalFormSystem = ref('')
    const modalFormGm = ref('')
    const modalFormStatus = ref('')

    function sortOrSearch(obj){
        crudBaseRef.value.sortOrSearch(obj);
    }

    function statusSortOrSearch(obj){
        //TODO map from search field to true/false/null
        crudBaseRef.value.sortOrSearch(obj);
    }

    function loadModal(obj){
        modalFormId.value = obj.obj._id
        modalFormName.value = obj.obj.Name
        modalFormSystem.value = obj.obj.System
        modalFormGm.value = obj.obj.Gm
        //TODO doesn't handle existing value of ongoing correctly, but that's probably fine
        modalFormStatus.value = obj.obj.Completed
    }

    function submitModal(){
        let obj = {};
        obj._id = modalFormId.value;
        obj.Name = modalFormName.value;
        obj.System = modalFormSystem.value;
        obj.Gm = modalFormGm.value;
        obj.Completed = modalFormStatus.value;
        crudBaseRef.value.createOrUpdate(obj);
    }

    function selectCampaign(obj){
        console.log('selectCampaign');
        emit('selectCampaign',obj);
    }

    function onCharacterSelected(obj){
        console.log('onCharacterSelected');
        let id = obj.obj==null? '' : obj.obj.ParentId;
        crudBaseRef.value.sortOrSearch({field:'_id', sort:false, search:id});
    }

    function onSessionSelected(obj){
        console.log('onSessionSelected');
        let id = obj.obj==null? '' : obj.obj.ParentId;
        crudBaseRef.value.sortOrSearch({field:'_id', sort:false, search:id});
        //TODO this currently does not show the campaign associated with the character for the session, only campaigns I ran or played without character
    }
</script>

<template>
    <CrudBase ref="crudBaseRef" api-url-end="campaigns" header-text="Campaign"
            @load-modal="loadModal" @submit-emit="submitModal" @select-row="selectCampaign">
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
                <span v-if="Completed == null || Completed == ''">ongoing</span>
                <span v-else-if="Completed == true || Completed == 'true'">completed</span>
                <span v-else>abandoned</span>
            </td>
        </template>
        <template v-slot:modal-form>
            <input v-model="modalFormName" placeholder="Name">
            <input v-model="modalFormSystem" placeholder="System">
            <input v-model="modalFormGm" placeholder="GM">
            <select v-model="modalFormStatus">
                <option value="">ongoing</option>
                <option value="true">completed</option>
                <option value="false">abandoned</option>
            </select>
        </template>
    </CrudBase>
</template>