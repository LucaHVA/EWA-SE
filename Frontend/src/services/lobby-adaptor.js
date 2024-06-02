export class LobbyAdaptor {
    constructor(socketUrl, handlerCB) {
        this.socketUrl = socketUrl.replace("http://", "ws://");
        this.handler = handlerCB;

        this.socket = new WebSocket(this.socketUrl);

        this.socket.onopen = () => console.log("Socket connection opened");
        this.socket.onmessage = (msg) => handlerCB(JSON.parse(msg.data));
        this.socket.onerror = (err) => console.error("Socket error:", err);
        this.socket.onclose = () => console.log("Socket connection closed");

        console.log(`LobbyAdaptor created for ${this.socketUrl}`);
    }

    sendMessage(message) {
        console.log("Sending message:", message);
        this.socket.send(JSON.stringify(message));
    }

    close() {
        console.log(`Closing LobbyAdaptor for ${this.socketUrl}`);
        this.socket.close();
    }
}
