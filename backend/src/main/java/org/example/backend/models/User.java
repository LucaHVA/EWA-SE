package org.example.backend.models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "\"user\"")
public class User implements Identifiable<Long> {

    @Id
    @SequenceGenerator(initialValue = 30000, name = "UserGenerator")
    @GeneratedValue(generator = "UserGenerator")
    //@Column(name = "user_id")
    private Long userId;

    //@Column(name = "username")
    private String username;

    //@Column(name = "email")
    private String email;

    //@Column(name = "password")
    private String password;

    @Lob
    //@Column(name = "profile_picture", columnDefinition = "MEDIUMTEXT")
    private String profilePicture;

    @OneToMany(mappedBy = "user")
    private Set<Player> players;

    @OneToMany(mappedBy = "user")
    private Set<GameHistory> gameHistories;

    public User() {
    }

    public User(Long userId, String username, String email, String password) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public Long getId() {
        return userId;
    }

    @Override
    public void setId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Set<GameHistory> getGameHistories() {
        return gameHistories;
    }

    public void setGameHistories(Set<GameHistory> gameHistories) {
        this.gameHistories = gameHistories;
    }
}
