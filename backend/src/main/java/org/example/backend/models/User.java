package org.example.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "\"user\"")
public class User implements Identifiable<Long>{

    @Id
    @SequenceGenerator(initialValue = 30000, name = "UserGenerator")
    @GeneratedValue(generator = "UserGenerator")
    private Long id;
    private String username;
    private String email;
    private String password;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String profilePicture;

    public User() {
    }

    public User(Long id,String username, String email, String password) {
        this.id= id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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
}
