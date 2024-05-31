package org.example.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.example.backend.enums.FriendshipStatus;

@NamedQueries({
        @NamedQuery(
                name = "Get_received_friend_request",
                query = "SELECT f " +
                        "FROM Friend f " +
                        "WHERE f.friend.id = ?1 AND f.status = org.example.backend.enums.FriendshipStatus.PENDING"
        ),
        @NamedQuery(
                name = "Get_sent_friend_request",
                query = "SELECT f " +
                        "FROM Friend f " +
                        "WHERE f.user.id = ?1 AND f.status = org.example.backend.enums.FriendshipStatus.PENDING"
        ),
        @NamedQuery(
                name = "Get_accepted_friends",
                query = "SELECT f " +
                        "FROM Friend f " +
                        "WHERE (f.user.id = ?1 OR f.friend.id = ?1) " +
                        "AND f.status = org.example.backend.enums.FriendshipStatus.ACCEPTED"
        ),
        @NamedQuery(
                name = "Find_friends_by_userId_or_friendId",
                query = "SELECT f FROM Friend f WHERE f.user.id = ?1 OR f.friend.id = ?1"
        )
})
@Entity
@Table(name = "\"friend\"")
public class Friend implements Identifiable<Long> {
    @Id
    @SequenceGenerator(initialValue = 30000, name = "UserGenerator")
    @GeneratedValue(generator = "UserGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-friend")
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    @JsonBackReference("friend-user")
    private User friend;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    public Friend() {
    }

    public Friend(Long id, User user, User friend, FriendshipStatus status) {
        this.id = id;
        this.user = user;
        this.friend = friend;
        this.status = status;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }
}
