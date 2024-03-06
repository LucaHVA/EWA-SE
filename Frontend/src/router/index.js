import {createRouter, createWebHashHistory} from "vue-router";
import welcomeComponent from "@/components/WelcomeComponent.vue";
import leaderboardComponent from "@/components/pages/LeaderboardComponent.vue";
import loginComponent from "@/components/pages/LoginComponent.vue";
import registerComponent from "@/components/pages/RegisterComponent.vue";
import unknownRoute from "@/components/UnknownRoute.vue";
import lobbySelectComponent from "@/components/pages/LobbySelectComponent.vue";
import gameSettingsComponent from "@/components/pages/GameSettingsComponent.vue";
import profileComponent from "@/components/pages/ProfileComponent.vue";


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
    },

    {
        path: '/register',
        name: 'register',
        component: registerComponent
    },

    {
        path: '/lobbySelect',
        name: 'lobbySelect',
        component: lobbySelectComponent
    },

    {
        path: '/gameSettings',
        name: 'gameSettings',
        component: gameSettingsComponent
    },

    {
        path: '/profilePage',
        name: 'profilePage',
        component: profileComponent
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