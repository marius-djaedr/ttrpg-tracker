<script setup>
import { ref, computed } from 'vue'

</script>

<template>
  <header>
    <nav class="navbar navbar-inverse">
        <div class="container-fluid">
            <div class="navbar-header">
              <router-link class="navbar-brand" to="/">TTRPG Tracker</router-link>
            </div>
            <ul class="nav navbar-nav">
                <li><router-link to="/calendar">Calendar</router-link></li>
                <li><router-link to="/data">Data</router-link></li>
                <li><router-link to="/aggregation">Aggregation</router-link></li>
                <li><router-link to="/system-ref">System Reference</router-link></li>
            </ul>
        </div>
    </nav>
  </header>

  <main>
    <router-view></router-view>
  </main>
</template>
