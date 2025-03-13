package com.restaurant.entities;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class StatusHistory {
    private int statusHistoryId;
    private DishStatus status;
    private LocalDateTime changedAt;

    public StatusHistory(int statusHistoryId, DishStatus status, LocalDateTime changedAt) {
        this.statusHistoryId = statusHistoryId;
        this.status = status;
        this.changedAt = changedAt;
    }
}