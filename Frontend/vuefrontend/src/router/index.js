import {createRouter, createWebHashHistory} from "vue-router";
import welcomeComponent from "@/components/WelcomeComponent.vue";
import leaderboard from "@/components/pages/Leaderboard.vue";
import unknownRoute from "@/components/UnknownRoute.vue";


const routes = [
    {path: '/', redirect: '/home'},
    {path: '/home', name: 'home', component: welcomeComponent},
    {path: '/leaderboard', name: 'leaderboard', component: leaderboard},
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