export class AnnouncementsAdaptor {
    socketUrl;
    socket = null;
    reconnect = true;

    constructor(socketUrl, handlerCB) {
        this.socketUrl = socketUrl;
        this.handler = handlerCB;

        this.socketUrl = socketUrl.replace("http://", "ws://");
        this.initWebSocket();
    }

    initWebSocket() {
        this.socket = new WebSocket(this.socketUrl);

        this.socket.onopen = () => {
            console.log("Socket opened");
            this.reconnect = true;
        };
        this.socket.onmessage = (event) => this.handler(event.data);
        this.socket.onerror = (error) => this.handleError(error);
        this.socket.onclose = () => this.handleClose();

        console.log(`Created announcements adaptor on ${this.socketUrl}`);
    }

    sendMessage(message) {
        if (this.socket.readyState === WebSocket.OPEN) {
            console.log("Sending announcement:", message);
            this.socket.send(message);
        } else {
            console.error("WebSocket connection not open. Unable to send message.");
        }
    }

    handleClose() {
        console.log(`Socket closed for ${this.socketUrl}`);
        if (this.reconnect) {
            setTimeout(() => this.initWebSocket(), 3000);
        }
    }

    handleError(error) {
        console.error("WebSocket error:", error);
        if (this.reconnect) {
            this.initWebSocket();
        }
    }

    close() {
        if (this.socket) {
            console.log(`Closed announcements adaptor on ${this.socketUrl}`);
            this.reconnect = false;
            this.socket.close();
        }
    }
}