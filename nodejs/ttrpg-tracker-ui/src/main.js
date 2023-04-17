import { createApp } from 'vue'
import {createRouter, createWebHashHistory} from 'vue-router'
import App from './App.vue'

import './assets/main.css'

import TheWelcome from './components/TheWelcome.vue'
import Data from './components/Data.vue'
//import Aggregation from './components/Aggregation.vue'
import NotFound from './components/NotFound.vue'

const routes = [
    {path:'/', component: TheWelcome},
    {path:'/data', component: Data},
//    {path:'/aggregation', component: Aggregation},
    {path: '/:pathMatch(.*)*', name: 'NotFound', component: NotFound },
]
  
const router = createRouter({
    history: createWebHashHistory(),
    routes
  })

const app = createApp(App)
app.use(router)
app.mount('#app')
