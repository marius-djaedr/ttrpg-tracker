<script setup>
    import { ref, resolveDirective } from 'vue'
    import {GChart} from 'vue-google-charts'
    import SortHeader from '../tidbits/SortHeader.vue'

    const apiUrl = ref(`http://localhost:3300/api/aggregation/`);
    const gChartKey = ref('');
    const chartSelected = ref(false);
    const chartName = ref('');
    const chartTypeGoogle = ref('');
    const chartData = ref([]);
    const chartOptions = ref({});
    const chartSettings = ref({});

    
    const mapping = ref([]);

    loadData();

    function loadData(){
        chartSelected.value = false;
        //TODO loading of some kind
        fetch(apiUrl.value+`mapping`)
            .then(res => res.json())
            .then(res => {
                mapping.value = res.data;
                calculatePageData();
            })
    }

    function selectAgg(id){
    //TODO loading of some kind
        fetch(apiUrl.value+id)
        .then(res => res.json())
        .then(res => {
            chartSelected.value = false;

            chartName.value = null;
            chartTypeGoogle.value = null;
            chartOptions.value = null;
            chartSettings.value = null;
            chartData.value = null;

            gChartKey.value = ''+new Date();

            chartName.value = res.data.chartTypeReadable + ' - '+res.data.name;
            chartTypeGoogle.value = res.data.chartTypeGoogle;
            chartOptions.value = res.data.options;
            chartSettings.value = res.data.settings;
            chartData.value = [ res.data.columnDefinitions].concat( res.data.rows);

            chartOptions.value.height = 500;
            
            chartSelected.value = true;
        })
        .catch(err =>{
            console.error(err)
        })
    }

    function rerunAgg(){

        let con = confirm('The aggregation will run in the background. This page might have aberrant behavior. Continue?');
        if(con){
            fetch(apiUrl.value+`run`,{
                method: 'POST'
            }).then(res => {
                if(res.ok){
                    let con = confirm('The aggregation is complete. Reload?');
                    if(con){
                        loadData();
                    }
                }else{
                    alert('Aggregation failed: '+res.statusText);
                }
            });
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
        filteredData.value = mapping.value.filter((row, index) => {
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


</script>

<template>
    <button class="btn btn-warning btn-block"  @click="rerunAgg">Rerun Aggregation</button> 
    <hr>
    <div v-if="chartSelected">
        <h1>{{ chartName }}</h1>
        <GChart
            :key="gChartKey"
            :type="chartTypeGoogle"
            :data="chartData"
            :options="chartOptions"
            :settings="chartSettings"
        />
        <hr>
    </div>
    <div>
        <button class="btn btn-default btn-sm"  @click="prevPage">Previous</button> 
        <button class="btn btn-default btn-sm"  @click="nextPage">Next</button>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th></th>
                    <SortHeader @submit-field="sortOrSearch" header-data-field="name">Name</SortHeader>
                    <SortHeader @submit-field="sortOrSearch" header-data-field="chartTypeReadable">Type</SortHeader>
                </tr>
            </thead>
            <tbody>
                <tr v-for="agg in pagedData">
                    <td>
                        <button class="btn btn-info btn-sm" @click="selectAgg(agg.id)">Show</button>
                    </td>
                    <td>{{ agg.name }}</td>
                    <td>{{ agg.chartTypeReadable }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<style scoped>
.table-striped>tbody>tr:nth-child(odd) {
	background-color: var(--color-background-mute);
}
</style>