<script setup>
    import { ref, watchEffect } from 'vue'
    const props = defineProps(['apiUrlEnd']);
    const data = ref([]);

    const apiUrl = `http://localhost:3300/api/`+props.apiUrlEnd;

    //TODO currently this is all still sync-loaded, including the <Suspense> tag on the child. Need to make it async with a loading icon
    data.value = await (await fetch(apiUrl)).json();

    console.log(data.value);

</script>

<template>
    <h3><a class="btn btn-secondary" href="/student">Create New</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Campaign List</h3>
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
                    <a class="btn btn-primary btn-sm" href="/student/{{this._id}}">Edit</a>
                    <a class="btn btn-danger btn-sm" href="/student/delete/{{this._id}}"
                    onclick="return confirm('Are you sure you want to delete this record ?');">Delete</a>
                </td>
            </tr>
        </tbody>
    </table>
</template>