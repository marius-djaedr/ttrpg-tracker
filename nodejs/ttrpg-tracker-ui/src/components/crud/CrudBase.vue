<script setup>
    import { ref } from 'vue'
    import Modal from '../tidbits/Modal.vue'
    const props = defineProps(['apiUrlEnd','headerText']);
    const emit = defineEmits({
        loadModal: ({obj}) => {
            return true;
        },
        submitEmit: () => {
            return true;
        },
        selectRow: ({obj}) => {
            return true;
        }
    })

    defineExpose({sortOrSearch, createOrUpdate, cancelModal, deselect})

    const apiUrl = ref(`http://localhost:3300/api/`+props.apiUrlEnd);

    ///////////////////////////////////////////////////////////////////////////////
    // Read
    const data = ref([]);

    loadData();

    function loadData(){
        //TODO loading icon
        fetch(apiUrl.value)
            .then(res => res.json())
            .then(res => {
                data.value = res;
                calculatePageData();
            })
    }

    ///////////////////////////////////////////////////////////////////////////////
    // Delete

    function deleteRecord(obj){
        let del = confirm('Are you sure you want to delete this record?');
        if(del){
            //TODO loading icon
            fetch(apiUrl.value+'/'+obj._id,{
                method: 'DELETE',
            }).then(res => loadData())
        }
    }

    ///////////////////////////////////////////////////////////////////////////////
    // Create, Update

    function create(obj){
        //TODO loading icon
        fetch(apiUrl.value,{
            method: 'POST',
            headers: {
                'Content-type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(obj)
        }).then(res => loadData())
    }

    function update(obj){
        //TODO loading icon
        fetch(apiUrl.value+'/'+obj._id,{
            method: 'PUT',
            headers: {
                'Content-type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(obj)
        }).then(res => loadData())
    }

    const isModalVisible = ref(false);
    const modalTitle = ref('');
    const modalCreateOrUpdate = ref('')

    function modalCreate(){
        modalTitle.value = 'Create ' + props.headerText;
        modalCreateOrUpdate.value = 'create'
        isModalVisible.value = true;
        emit('loadModal',{obj:{}})
    }

    function modalUpdate(datum){
        modalTitle.value = 'Update ' + props.headerText;
        modalCreateOrUpdate.value = 'update'
        isModalVisible.value = true;
        emit('loadModal',{obj:datum})
    }

    function cancelModal(){
        modalCreateOrUpdate.value = null;
        isModalVisible.value = false;
    }

    function submitModal(){
        emit('submitEmit')
        modalCreateOrUpdate.value = null;
        isModalVisible.value = false;
    }

    function createOrUpdate(obj){
        console.log('createOrUpdate')
        if(modalCreateOrUpdate.value==='create'){
            create(obj)
        }else{
            update(obj)
        }
    }

    ///////////////////////////////////////////////////////////////////////////////
    // sort, filter, pagination

    const currentSort = ref('');
    const currentSortDir = ref('asc');
    const pageSize = ref(10);
    const currentPage = ref(1);
        
    const filters = ref({});
    const filteredData = ref([]);
    const pagedData = ref([]);
    
    function sortOrSearch(obj){
        let field = obj['field'];
        if(obj['sort']){
            if(field === currentSort.value) {
                currentSortDir.value = currentSortDir.value==='asc'?'desc':'asc';
            }
            currentSort.value = field;
        }else{
            let search = obj['search'];
            if(search==null){
                search = '';
            }
            filters.value[field] = search;
        }
        currentPage.value = 1;
        calculatePageData();
    }

    function nextPage(){
        if((currentPage.value*pageSize.value) < filteredData.value.length) currentPage.value++;
        calculatePageData();
    }

    function prevPage(){
        if(currentPage.value > 1) currentPage.value--;
        calculatePageData();
    }

    function calculatePageData(){
        //filter
        filteredData.value = data.value.filter((row, index) => {
            for(let key in filters.value){
                if(!row[key].includes(filters.value[key])) return false;
            }
            return true;
        });


        //sort
        pagedData.value = filteredData.value.sort((a,b) => {
            let modifier=1;
            let aval = a[currentSort.value];
            let bval = b[currentSort.value];
            if(currentSortDir.value === 'desc') modifier = -1;
            if(aval!=null){
                if(bval!=null){
                    if(aval < bval) return -1 * modifier;
                    if(aval > bval) return 1 * modifier;
                }
                return modifier;
            }
            if(bval!=null) return -1 * modifier
            return 0;
        }); 

         //paginate
         pagedData.value = pagedData.value.filter((row, index) => {
            let start = (currentPage.value-1)*pageSize.value;
            let end = currentPage.value*pageSize.value;
            if(index >= start && index < end) return true;
        });
    }

    ///////////////////////////////////////////////////////////////////////////////
    // select
    const noSelect = ref(true);

    function select(obj){
        console.log('select');
        filters.value._id = obj._id;
        calculatePageData();
        noSelect.value = false;
        emit('selectRow',{obj:obj});
    }

    function deselect(){
        console.log('deselect');
        delete filters.value._id;
        calculatePageData();
        noSelect.value = true;
    }

    function deselectButton(){
        console.log('deselectButton');
        deselect()
        emit('selectRow',{obj:null});
    }
</script>

<template>
    <div class="box">
        <h3>{{props.headerText}} List</h3>
        <button class="btn btn-default btn-sm"  @click="prevPage">Previous</button> 
        <button class="btn btn-default btn-sm"  @click="nextPage">Next</button>

        <table class="table table-striped">
            <thead>
                <tr>
                    <th></th>
                    <slot name="header-th"></slot>
                    <th></th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="datum in pagedData">
                    <td>
                        <button v-show="noSelect" class="btn btn-info btn-sm" @click="select(datum)">Select</button>
                        <button v-show="!noSelect" class="btn btn-info btn-sm" @click="deselectButton">Deselect</button>
                    </td>
                    <slot name="table-data" v-bind="datum"></slot>
                    <td>
                        <button class="btn btn-warning btn-sm" @click="modalUpdate(datum)">Update</button>
                    </td>
                    <td>
                        <button class="btn btn-danger btn-sm" @click="deleteRecord(datum)">Delete</button>
                    </td>
                </tr>
            </tbody>
        </table>
        <button v-show="noSelect" class="btn btn-success btn-sm" @click="modalCreate">Create New</button>
    </div>

    <Modal v-show="isModalVisible" @cancel="cancelModal" @submit="submitModal">
        <template v-slot:header>{{modalTitle}}</template>
        <template v-slot:body>
            <div class="form-inline">
                <slot name="modal-form"></slot>
            </div>
        </template>
        <template v-slot:footer></template>
    </Modal>
</template>

<style scoped>
  .box {
                background-color: var(--color-background-soft);
                margin-top: 25px;
                padding: 20px;
                -webkit-box-shadow: 10px 10px 20px 1px rgba(0, 0, 0, 0.75);
                -moz-box-shadow: 10px 10px 20px 1px rgba(0, 0, 0, 0.75);
                box-shadow: 10px 10px 20px 1px rgba(0, 0, 0, 0.75);
                border-radius: 10px 10px 10px 10px;
                -moz-border-radius: 10px 10px 10px 10px;
                -webkit-border-radius: 10px 10px 10px 10px;
                border: 0px solid #000000;
            }
.table-striped>tbody>tr:nth-child(odd) {
	background-color: var(--color-background);
}
</style>
