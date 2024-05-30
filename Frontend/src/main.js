import {createApp} from 'vue'
import {router} from "@/router";
import AppComponent from './App.vue'
import './assets/css/main.css'


createApp(AppComponent).use(router).mount('#app')
