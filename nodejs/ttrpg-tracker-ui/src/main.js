import { createApp } from 'vue'
import {createRouter, createWebHashHistory} from 'vue-router'
import App from './App.vue'

import './assets/main.css'

import TheWelcome from './components/TheWelcome.vue'
import Campaigns from './components/crud/Campaigns.vue'
import Characters from './components/crud/Characters.vue'
import Sessions from './components/crud/Sessions.vue'
import SingleCampaign from './components/crud/SingleCampaign.vue'
import SingleCharacter from './components/crud/SingleCharacter.vue'
import SingleSession from './components/crud/SingleSession.vue'
import NotFound from './components/NotFound.vue'

const routes = [
    {path:'/', component: TheWelcome},
    {path:'/campaigns', component: Campaigns},
    {path:'/characters', component: Characters},
    {path:'/sessions', component: Sessions},
    {path:'/campaign/:id', component: SingleCampaign, props:true},
    {path:'/character/:id', component: SingleCharacter, props:true},
    {path:'/session/:id', component: SingleSession, props:true},
    {path: '/:pathMatch(.*)*', name: 'NotFound', component: NotFound },
]
  
const router = createRouter({
    history: createWebHashHistory(),
    routes
  })

const app = createApp(App)
app.use(router)
app.mount('#app')
