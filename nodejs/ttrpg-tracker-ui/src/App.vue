<script setup>
import { ref, computed } from 'vue'

</script>

<template>
  <header>
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
              <router-link class="navbar-brand" to="/">TTRPG Tracker</router-link>
            </div>
            <ul class="nav navbar-nav">
                <li><router-link to="/data">Data</router-link></li>
<!--                <li><router-link to="/aggregation">Aggregation</router-link></li> -->
            </ul>
        </div>
    </nav>
  </header>

  <main class="box">
    <Suspense>
      <router-view></router-view>
    </Suspense>
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
