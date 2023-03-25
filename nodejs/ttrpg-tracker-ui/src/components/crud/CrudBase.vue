<script setup>
    import { ref } from 'vue'
    const props = defineProps(['apiUrlEnd','singleUrlEnd']);
    const data = ref([]);
    const apiUrl = ref(`http://localhost:3300/api/`+props.apiUrlEnd);


    //TODO currently this is all still sync-loaded, including the <Suspense> tag on the child. Need to make it async with a loading icon
    data.value = await (await fetch(apiUrl.value)).json();

</script>

<template>
    <h3>Campaign List</h3>
    <a class="btn btn-secondary" href="#">Create New</a>
    <table class="table table-striped">
        <thead>
            <tr>
                <slot name="header-th"></slot>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="datum in data">
                <slot name="table-data" v-bind="datum"></slot>
                <td>
                    <router-link class="btn btn-primary btn-sm" v-bind:to="`/${singleUrlEnd}/${datum._id}`">Details</router-link>
                    <a class="btn btn-danger btn-sm" href="#"
                        onclick="return confirm('Are you sure you want to delete this record ?');">Delete</a>
                </td>
            </tr>
        </tbody>
    </table>
</template>