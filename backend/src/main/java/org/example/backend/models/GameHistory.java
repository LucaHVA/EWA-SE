package org.example.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "\"gameHistory\"")
public class GameHistory implements Identifiable<Long> {

    @Id
    @SequenceGenerator(initialValue = 30000, name = "GameHistoryGenerator")
    @GeneratedValue(generator = "GameHistoryGenerator")
    private Long gameHistoryId;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    private int placement;
    private LocalTime startTime;
    private LocalTime endTime;

    public GameHistory() {
    }

    public GameHistory(Long gameHistoryId, User user, int placement, LocalTime startTime, LocalTime endTime) {
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
