import {createRouter, createWebHashHistory} from "vue-router";
import welcomeComponent from "@/components/WelcomeComponent.vue";
import leaderboardComponent from "@/components/pages/Leaderboard.vue";
import loginComponent from "@/components/pages/LoginComponent.vue";
import unknownRoute from "@/components/UnknownRoute.vue";


const routes = [
    {
        path: '/',
        redirect: '/home'
    },

    {
        path: '/home',
        name: 'home',
        component: welcomeComponent
    },

    {
        path: '/leaderboard',
        name: 'leaderboard',
        component: leaderboardComponent
    },

    {
        path: '/login',
        name: 'login',
        component: loginComponent
    }
];

routes.push(
    {path: '/:pathUnknown', name: 'unknownRoute', component: unknownRoute}
);

export const router = createRouter(
    {
        history: createWebHashHistory(),
        routes: routes
    },
);