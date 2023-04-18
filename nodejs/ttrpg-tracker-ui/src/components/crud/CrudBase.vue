<script setup>
    import { ref } from 'vue'
    const props = defineProps(['apiUrlEnd','headerText']);
    const data = ref([]);
    const sortedData = ref([]);
    const currentSort = ref('');
    const currentSortDir = ref('asc');
    const apiUrl = ref(`http://localhost:3300/api/`+props.apiUrlEnd);

    //TODO loading icon
    fetch(apiUrl.value)
        .then(res => res.json())
        .then(res => {
            data.value = res;
            calculatePageData();
        })

    function sort(s){
        console.info("sort");
        //if s == current sort, reverse
        if(s === currentSort.value) {
            currentSortDir.value = currentSortDir.value==='asc'?'desc':'asc';
        }
        currentSort.value = s;

        calculatePageData();
    }

    function calculatePageData(){
        //https://www.raymondcamden.com/2018/02/08/building-table-sorting-and-pagination-in-vuejs
        //https://www.raymondcamden.com/2021/03/11/adding-filtering-to-my-vuejs-table-sorting-and-pagination-demo
        //TODO paginate
        sortedData.value = data.value;
        //sort
        sortedData.value.sort((a,b)=>{
            let modifier=1;
            if(currentSortDir.value === 'desc') modifier = -1;
            if(a[currentSort.value] < b[currentSort.value]) return -1 * modifier;
            if(a[currentSort.value] > b[currentSort.value]) return 1 * modifier;
            return 0;
        }); 
    }

    defineExpose({sort})
</script>

<template>
    <div class="box">
        <h3>{{props.headerText}}</h3>
        <!-- TODO this actually doesn't do anything right now -->
        <a class="btn btn-success btn-sm" href="#/Data" onclick="return confirm('this will eventually create a new record ?');">Create New</a>
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
                <tr v-for="datum in sortedData">
                    <td>
                        <!-- TODO this actually doesn't do anything right now -->
                        <a class="btn btn-info btn-sm" href="#/Data"
                            onclick="return confirm('this will eventually filter the lower records ?');">Select</a>
                    </td>
                    <slot name="table-data" v-bind="datum"></slot>
                    <td>
                        <!-- TODO this actually doesn't do anything right now -->
                        <a class="btn btn-warning btn-sm" href="#/Data"
                            onclick="return confirm('this will eventually update this record ?');">Update</a>
                    </td>
                    <td>
                        <!-- TODO this actually doesn't do anything right now -->
                        <a class="btn btn-danger btn-sm" href="#/Data"
                            onclick="return confirm('this will eventually delete this record ?');">Delete</a>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
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
