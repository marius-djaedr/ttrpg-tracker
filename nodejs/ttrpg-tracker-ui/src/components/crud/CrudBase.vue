<script setup>
    import { ref } from 'vue'
    import Modal from '../Modal.vue'
    const props = defineProps(['apiUrlEnd','headerText']);
    const apiUrl = ref(`http://localhost:3300/api/`+props.apiUrlEnd);
    const data = ref([]);

    const currentSort = ref('');
    const currentSortDir = ref('asc');
    const pageSize = ref(10);
    const currentPage = ref(1);
        
    const filters = ref({});
    const filteredData = ref([]);
    const pagedData = ref([]);

    const isModalVisible = ref(false);
    const modalTitle = ref('');

    //TODO loading icon
    fetch(apiUrl.value)
        .then(res => res.json())
        .then(res => {
            data.value = res;
            calculatePageData();
        })
    
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

    function modalCreate(){
        console.log('START CREATE')
        modalTitle.value = 'Create ' + props.headerText;
        //TODO anything else with start create?
        isModalVisible.value = true;
    }

    function modalUpdate(id){
        console.log('START UPDATE '+id)
        modalTitle.value = 'Update ' + props.headerText;
        //TODO anything else with start update?
        isModalVisible.value = true;
    }

    function cancelModal(){
        console.log('CANCEL')
        //TODO anything else with cancel?
        isModalVisible.value = false;
    }

    function submitModal(){
        console.log('SUBMIT')
        //TODO anything else with submit?
        isModalVisible.value = false;
    }

    defineExpose({sortOrSearch})
</script>

<template>
    <div class="box">
        <h3>{{props.headerText}} List</h3>
        <button class="btn btn-basic btn-sm"  @click="prevPage">Previous</button> 
        <button class="btn btn-basic btn-sm"  @click="nextPage">Next</button>

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
                        <!-- TODO this actually doesn't do anything right now -->
                        <button class="btn btn-info btn-sm" 
                            onclick="return confirm('this will eventually filter the lower records ?');">Select</button>
                    </td>
                    <slot name="table-data" v-bind="datum"></slot>
                    <td>
                        <button class="btn btn-warning btn-sm" @click="modalUpdate(datum._id)">Update</button>
                    </td>
                    <td>
                        <!-- TODO this actually doesn't do anything right now -->
                        <button class="btn btn-danger btn-sm" 
                            onclick="return confirm('this will eventually delete this record ?');">Delete</button>
                    </td>
                </tr>
            </tbody>
        </table>
        <button class="btn btn-success btn-sm" @click="modalCreate">Create New</button>
    </div>

    <Modal v-show="isModalVisible" @cancel="cancelModal" @submit="submitModal">
        <template v-slot:header>{{modalTitle}}</template>
        <template v-slot:body>
            <slot name="modal-form"></slot>
        </template>
        <template v-slot:footer></template>
    </Modal>
</template>

<style scoped>
  .box {
                background-color: #fff;
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
</style>
