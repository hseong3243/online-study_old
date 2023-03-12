import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from "./router/router"
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

import "bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import axios from "axios";
import {useTokenStore} from "@/store/token";
import VueCookies from "vue-cookies";


const pinia = createPinia();
pinia.use(piniaPluginPersistedstate)
const app = createApp(App);
axios.defaults.headers.common['Access-Control-Allow-Origin'] = 'http://localhost:8080'
axios.interceptors.request.use(
    (config) => {
        const store = useTokenStore()
        if(store.token != null)
            config.headers.Authorization = 'Bearer ' + store.token;
        return config;
    }
)



app.use(pinia)
app.use(VueCookies)
app.use(router)
app.mount('#app')

// createApp(App).mount('#app')
// createApp(App).use(router).use(pinia).mount('#app');

