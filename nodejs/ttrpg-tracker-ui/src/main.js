import { createApp } from 'vue'
import {createRouter, createWebHashHistory} from 'vue-router'
import App from './App.vue'

import './assets/main.css'

import TheWelcome from './components/welcome/TheWelcome.vue'
import Data from './components/crud/Data.vue'
import SystemRef from './components/crud/SystemRef.vue'
import Aggregation from './components/aggregation/Aggregation.vue'
import NotFound from './components/NotFound.vue'

const routes = [
    {path:'/', component: TheWelcome},
    {path:'/data', component: Data},
    {path:'/aggregation', component: Aggregation},
    {path:'/system-ref', component: SystemRef},
    {path: '/:pathMatch(.*)*', name: 'NotFound', component: NotFound },
]
  
const router = createRouter({
    history: createWebHashHistory(),
    routes
  })

const app = createApp(App)
app.use(router)
app.mount('#app')
