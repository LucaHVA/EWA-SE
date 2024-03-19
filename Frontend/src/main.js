import {createApp} from 'vue'
import {router} from "@/router";
import AppComponent from './App.vue'


createApp(AppComponent).use(router).mount('#app')

