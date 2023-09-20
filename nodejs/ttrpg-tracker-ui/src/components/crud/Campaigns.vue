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
        modalFormName.value = obj.obj.name
        modalFormSystem.value = obj.obj.system
        modalFormGm.value = obj.obj.gm
        //TODO doesn't handle existing value of ongoing correctly, but that's probably fine
        modalFormStatus.value = obj.obj.completed
    }

    function submitModal(){
        let obj = {};
        obj._id = modalFormId.value;
        obj.name = modalFormName.value;
        obj.system = modalFormSystem.value;
        obj.gm = modalFormGm.value;
        obj.completed = modalFormStatus.value;
        crudBaseRef.value.createOrUpdate(obj);
    }

    function selectCampaign(obj){
        emit('selectCampaign',obj);
    }

    function onCharacterSelected(obj){
        console.warn('Currently no support for up-filtering on select');
        // let id = obj.obj==null? '' : obj.obj.parentId;
        // crudBaseRef.value.sortOrSearch({field:'_id', sort:false, search:id});
    }

    function onSessionSelected(obj){
        console.warn('Currently no support for up-filtering on select');
        // let id = obj.obj==null? '' : obj.obj.parentId;
        // crudBaseRef.value.sortOrSearch({field:'_id', sort:false, search:id});
        //TODO this currently does not show the campaign associated with the character for the session, only campaigns I ran or played without character
        //which this leads to a problem when a session gets selected then deselected
    }
</script>

<template>
    <CrudBase ref="crudBaseRef" api-url-end="campaigns" header-text="Campaign"
            @load-modal="loadModal" @submit-emit="submitModal" @select-row="selectCampaign">
        <template v-slot:header-th>
            <SortHeader @submit-field="sortOrSearch" header-data-field="name">Name</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="system">System</SortHeader>
            <SortHeader @submit-field="sortOrSearch" header-data-field="gm">Gm</SortHeader>
            <SortHeader @submit-field="statusSortOrSearch" header-data-field="completed">Status</SortHeader>
        </template>
        <template v-slot:table-data="{name, system, gm, completed}">
            <td>{{name}}</td>
            <td>{{system}}</td>
            <td>{{gm}}</td>
            <!-- <td>{{completed}}</td> -->
            <td>
                <!-- TODO double check logic-->
                <span v-if="completed === false || completed === 'false'">abandoned</span>
                <span v-else-if="completed === true || completed === 'true'">completed</span>
                <span v-else>ongoing</span>
            </td>
        </template>
        <template v-slot:modal-form>
            <input class="form-control" v-model="modalFormName" placeholder="Name">
            <input class="form-control" v-model="modalFormSystem" placeholder="System">
            <input class="form-control" v-model="modalFormGm" placeholder="GM">
            <select class="form-control" v-model="modalFormStatus">
                <option value="">ongoing</option>
                <option value="true">completed</option>
                <option value="false">abandoned</option>
            </select>
        </template>
    </CrudBase>
</template>