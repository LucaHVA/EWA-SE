package org.example.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "\"user\"")
public class User implements Identifiable<Long> {

    public enum Role {
        ADMIN, USER
    }

    @Id
    @SequenceGenerator(initialValue = 30000, name = "UserGenerator")
    @GeneratedValue(generator = "UserGenerator")
    private Long userId;

    private String username;
    private String email;
    private String password;

    @Lob
    private String profilePicture;

    @ElementCollection(targetClass = Role.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Player> players;

    @OneToMany(mappedBy = "user")
    private Set<GameHistory> gameHistories;

    @OneToMany(mappedBy = "host")
    private List<Game> games;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference("user-friend")
    private Set<Friend> friendRequests;

    @OneToMany(mappedBy = "friend")
    @JsonManagedReference("friend-user")
    private Set<Friend> receivedFriendRequests;

    public User() {
        this.roles.add(Role.USER);  // Default role
    }

    public User(Long userId, String username, String email, String password) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles.add(Role.USER);  // Default role
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<GameHistory> getGameHistories() {
        return gameHistories;
    }
@JsonIgnore
    public void setGameHistories(Set<GameHistory> gameHistories) {
        this.gameHistories = gameHistories;
    }

    public Set<Friend> getReceivedFriendRequests() {
        return receivedFriendRequests;
    }

    public void setReceivedFriendRequests(Set<Friend> receivedFriendRequests) {
        this.receivedFriendRequests = receivedFriendRequests;
    }

    public Set<Friend> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(Set<Friend> friendRequests) {
        this.friendRequests = friendRequests;
    }
}
