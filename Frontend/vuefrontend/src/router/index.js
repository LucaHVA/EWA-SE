import { createRouter, createWebHashHistory } from "vue-router";
import welcomeComponent from "@/components/WelcomeComponent";
import Leaderbord from "@/components/pages/Leaderbord.vue";
import unknownRoute from "@/components/UnknownRoute";



const routes = [
    { path: '/', redirect: '/home' },
    { path: '/home', name: 'home', component: welcomeComponent },
    { path: '/pages/Leaderbord', name: 'Leaderbord', component: Leaderbord },
];

routes.push(
    { path: '/:pathUnknown', name: 'unknownRoute', component: unknownRoute }
);

export const router = createRouter(
    {
        history: createWebHashHistory(),
        routes: routes
    },
);