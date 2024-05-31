package org.example.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.sql.Time;

@Entity
@Table(name = "\"gameHistory\"")
public class GameHistory implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "game_history_id")
    private Long gameHistoryId;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    //@Column(name = "placement")
    private int placement;

    //@Column(name = "startTime")
    private Time startTime;

    //@Column(name = "endTime")
    private Time endTime;

    public GameHistory() {
    }

    public GameHistory(Long gameHistoryId, User user, int placement, Time startTime, Time endTime) {
        this.gameHistoryId = gameHistoryId;
        this.user = user;
        this.placement = placement;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return gameHistoryId;
    }


    public void setId(Long id) {
        this.gameHistoryId = id;
    }

    public Long getGameHistoryId() {
        return gameHistoryId;
    }

    public void setGameHistoryId(Long gameHistoryId) {
        this.gameHistoryId = gameHistoryId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPlacement() {
        return placement;
    }

    public void setPlacement(int placement) {
        this.placement = placement;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
}
