package org.example.backend;

import org.example.backend.sockets.AnnouncementDistributor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.Set;

@Configuration
@EnableWebSocket
public class APIConfig implements WebMvcConfigurer, WebSocketConfigurer {

    public Set<String> UNSECURED_PATHS =
            Set.of("/users/login", "/h2-console", "/favicon.ico");

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public int getTokenDurationOfValidity() {
        return tokenDurationOfValidity;
    }

    public void setTokenDurationOfValidity(int tokenDurationOfValidity) {
        this.tokenDurationOfValidity = tokenDurationOfValidity;
    }

    @Value("${jwt.issuer:private company}")
    private String issuer;

    @Value("${jwt.passphrase:This is very secret information for my private encryption key.}")
    private String passphrase;

    @Value("${jwt.duration-of-validity:1200}") // default 20 minutes;
    private int tokenDurationOfValidity;

    @Autowired
    private AnnouncementDistributor announcementDistributor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(this.announcementDistributor, "/announcements")
                .setAllowedOrigins("http://localhost:*");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:*", "https://front-end-catan.onrender.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE)
                .exposedHeaders(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE)
                .allowCredentials(true);
    }
}
