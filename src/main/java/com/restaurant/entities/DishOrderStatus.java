package com.restaurant.entities;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DishOrderStatus {
    private int statusHistoryId;
    private DishStatus status;
    private LocalDateTime changedAt;

    public DishOrderStatus(int statusHistoryId, DishStatus status, LocalDateTime changedAt) {
        this.statusHistoryId = statusHistoryId;
        this.status = status;
        this.changedAt = changedAt;
    }
}