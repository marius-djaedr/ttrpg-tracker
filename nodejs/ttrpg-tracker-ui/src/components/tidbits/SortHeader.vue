<script setup>
    import { ref } from 'vue'
    const props = defineProps(['headerDataField']);
    const emit = defineEmits({
        submitField: ({field,sort,search}) => {
            return true;
        }
    })

    const searchValue = ref('')
    const sortText = ref('--')

    function sortClick(){
        //TODO reset other sort buttons as well
        if(sortText.value === '/\\'){
            sortText.value = '\\/'
        }else{
            sortText.value = '/\\'
        }
        emit('submitField', {field:props.headerDataField, sort:true, search:null});
    }
    
    function searchEnter(){
        emit('submitField', {field:props.headerDataField, sort:false, search:searchValue.value});
    }
</script>

<template>
    <th>
        <input v-model="searchValue" @keyup.enter="searchEnter" size="5"/>
        <button @click="sortClick">{{sortText}}</button><br>
        <slot />
    </th>
</template>