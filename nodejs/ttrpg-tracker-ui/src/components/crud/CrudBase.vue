<script setup>
    import { ref } from 'vue'
    const props = defineProps(['apiUrlEnd','headerText']);
    const data = ref([]);
    const apiUrl = ref(`http://localhost:3300/api/`+props.apiUrlEnd);


    //TODO currently this is all still sync-loaded, including the <Suspense> tag on the child. Need to make it async with a loading icon
    data.value = await (await fetch(apiUrl.value)).json();

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
                <tr v-for="datum in data">
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
