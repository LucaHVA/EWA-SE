package org.example.backend.sockets;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class AnnouncementDistributor extends TextWebSocketHandler {
    List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        for (WebSocketSession webSocketSession : sessions) {
            try {
                webSocketSession.sendMessage(new TextMessage(message.getPayload()));
            } catch (IOException e) {
                sessions.remove(webSocketSession);
            }
        }
    }
}
