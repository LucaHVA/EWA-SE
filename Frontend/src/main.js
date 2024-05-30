import { createApp } from 'vue';
import { router } from "@/router";
import AppComponent from './App.vue';
import './assets/css/main.css';
import { AnnouncementsAdaptor } from '@/services/AnnouncementsAdaptor';

// Ensure the WebSocket URL is correctly set
const websocketUrl = process.env.VUE_APP_WEBSOCKET_URL;
if (!websocketUrl) {
    console.error("WebSocket URL is not defined. Please set VUE_APP_WEBSOCKET_URL in your environment variables.");
} else {
    console.log("WebSocket URL:", websocketUrl);
}

// Define the WebSocket message handler
function handleWebSocketMessage(data) {
    try {
        const message = JSON.parse(data);
        if (message.type === 'START_GAME') {
            router.push({ name: 'game', params: { id: message.gameId } });
        }
    } catch (error) {
        console.error("Error parsing WebSocket message:", error);
    }
}

// Initialize the WebSocket connection only if the URL is defined
let socketConnection;
if (websocketUrl) {
    socketConnection = new AnnouncementsAdaptor(websocketUrl, handleWebSocketMessage);
}

// Create and mount the Vue app
const app = createApp(AppComponent);
app.use(router);

if (socketConnection) {
    app.provide('$socketConnection', socketConnection);
}

app.mount('#app');

// Optional: Log to confirm app initialization
console.log("Vue app initialized with WebSocket connection to", websocketUrl);

