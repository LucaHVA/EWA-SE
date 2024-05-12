export class AnnouncementsAdaptor {
    socketUrl;
    socket=null;

    constructor(socketUrl, handlerCB) {
        this.socketUrl = socketUrl;
        this.handler = handlerCB;

        this.socketUrl = socketUrl.replace("http://","ws://");
        this.socket = new WebSocket(this.socketUrl);

        this.socket.onopen = () => console.log("Open socket");
        this.socket.onmessage = (msg) => handlerCB(msg.data);
        this.socket.onerror = (err) => console.log(err);
        this.socket.onclose = () => console.log("Closed socket");
        console.log(`Created announcements adaptor on ${this.socketUrl}`);
    }

    sendMessage(message) {
        console.log("New announcement:", message);
        this.socket.send(message);
    }

    close() {
        console.log(`Closed announcements adaptor on ${this.socketUrl}`)
        this.socket.close();
    }
}