<script setup>
import { ref, computed } from 'vue'
import TheWelcome from './components/TheWelcome.vue'
import Campaigns from './components/crud/Campaigns.vue'
import NotFound from './components/NotFound.vue'

const routes = {
  '/': TheWelcome,
  '/campaigns': Campaigns,
}

const currentPath = ref(window.location.hash)
window.addEventListener('hashchange', () => {
  currentPath.value = window.location.hash
})

const currentView = computed(() => {
  return routes[currentPath.value.slice(1) || '/'] || NotFound
})

</script>

<template>
  <header>
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="#/">TTRPG Tracker</a>
            </div>
            <ul class="nav navbar-nav">
                <li><a href="#/campaigns">Campaigns</a></li>
                <li><a href="#/characters">Characters</a></li>
                <li><a href="#/sessions">Sessions</a></li>
                <li><a href="#/aggregation">Aggregation</a></li>
            </ul>
        </div>
    </nav>
  </header>

  <main class="box">
    <component :is="currentView" />
  </main>
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
